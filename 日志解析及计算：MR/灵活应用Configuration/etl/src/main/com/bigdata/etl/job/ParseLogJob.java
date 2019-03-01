package com.bigdata.etl.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bigdata.etl.utils.IPUtil;
import com.bigdata.etl.mr.LogFieldWritable;
import com.bigdata.etl.mr.LogGenericWritable;
//import org.anarres.lzo.hadoop.codec.LzopCodec;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

public class ParseLogJob extends Configured implements Tool {

    public static LogGenericWritable parseLog(String row) throws ParseException {
        String[] logPart = StringUtils.split(row, "\u1111");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        long timeTag = dateFormat.parse(logPart[0]).getTime();

        String activeName = logPart[1];

        JSONObject bizData = JSON.parseObject(logPart[2]);
        LogGenericWritable logData = new LogWritable();
        logData.put("time_tag", new LogFieldWritable(timeTag));
        logData.put("active_name", new LogFieldWritable(activeName));
        for (Map.Entry<String, Object> entry : bizData.entrySet()) {
            logData.put(entry.getKey(), new LogFieldWritable(entry.getValue()));
        }

        return logData;
    }

    public static class LogWritable extends LogGenericWritable {
        @Override
        protected String[] getFieldName() {
            return new String[] {"active_name", "session_id", "time_tag", "ip", "device_id", "req_url", "user_id", "product_id", "order_id"};
        }
    }

    public static class LogMapper extends Mapper<LongWritable, Text, LongWritable, LogGenericWritable> {
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            try {
                LogGenericWritable parsedLog = parseLog(value.toString());
                context.write(key, parsedLog);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public static class LogReducer extends Reducer<LongWritable, LogGenericWritable, NullWritable, Text> {

        public void setup(Context context) throws IOException, InterruptedException {
            /* 添加分布式缓存后这些代码就可以去掉
            Configuration conf = context.getConfiguration();
            FileSystem fs = FileSystem.get(context.getConfiguration());
//            Path ipFile = new Path("/user/1015146591/lib/17monipdb.dat");
            Path ipFile = new Path(conf.get("ip.file.path"));
            Path localPath = new Path(this.getClass().getResource("/").getPath());
            fs.copyToLocalFile(ipFile, localPath);
            */
            IPUtil.load("17monipdb.dat");
        }

        public void reduce(LongWritable key, Iterable<LogGenericWritable> values, Context context) throws IOException, InterruptedException {
            for (LogGenericWritable v : values ) {
                String ip = (String)v.getObject("ip");
                String[] address = IPUtil.find(ip);
                JSONObject addr = new JSONObject();
                addr.put("country", address[0]);
                addr.put("province", address[1]);
                addr.put("city", address[2]);

                JSONObject datum = JSON.parseObject(v.asJsonString());
                datum.put("address", addr);


                context.write(null, new Text(datum.toJSONString()));
            }
        }
    }

    public int run(String[] args) throws Exception {
        //创建job
        Configuration config = getConf();
        //添加自定义配置
//        config.set("ip.file.path", args[2]);
        config.addResource("mr.xml");
        Job job = Job.getInstance(config);
        //通过job设置一些参数
        job.setJarByClass(ParseLogJob.class);
        job.setJobName("parselog");
        job.setMapperClass(LogMapper.class);
        //设置reduce个数为0
        job.setReducerClass(LogReducer.class);
        job.setMapOutputKeyClass(LongWritable.class);
        job.setMapOutputValueClass(LogWritable.class);
        job.setOutputValueClass(Text.class);

        //添加分布式缓存
        job.addCacheFile(new URI(config.get("ip.file.path")));


        //添加输入和输出数据
        FileInputFormat.addInputPath(job, new Path(args[0]));
        Path outputPath = new Path(args[1]);
        FileOutputFormat.setOutputPath(job, outputPath);

        //设置压缩类型
//        FileOutputFormat.setCompressOutput(job, true);
//        FileOutputFormat.setOutputCompressorClass(job, LzopCodec.class);

        FileSystem fs = FileSystem.get(config);
        if (fs.exists(outputPath)) {
            fs.delete(outputPath, true);
        }


        //运行程序
        if (!job.waitForCompletion(true)) {
            throw new RuntimeException(job.getJobName() + "failed!");
        }
        return 0;
    }

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new ParseLogJob(), args);
        System.exit(res);
    }

}