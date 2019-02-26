package com.bigdata.etl.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigdata.etl.mr.*;
import com.bigdata.etl.utils.IPUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.CombineTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

//import org.anarres.lzo.hadoop.codec.LzopCodec;

public class ParseLogJob_End extends Configured implements Tool {

    public static LogGenericWritable parseLog(String row) throws Exception {
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
            return new String[]{"active_name", "session_id", "time_tag", "ip", "device_id", "req_url", "user_id", "product_id", "order_id", "error_flag", "error_log"};
        }
    }

    public static class LogMapper extends Mapper<LongWritable, Text, TextLongWritable, LogGenericWritable> {
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            Counter errorCounter = context.getCounter("Log error", "Parse error");
            try {
                //解析每行日志
                LogGenericWritable parsedLog = parseLog(value.toString());

                //获取日志里的session_id和time_tag，然后写入TextLongWritable中传入reduce
                String session = (String) parsedLog.getObject("session_id");
                Long timeTag = (Long) parsedLog.getObject("time_tag");
                TextLongWritable outKey = new TextLongWritable();
                outKey.setText(new Text(session));
                outKey.setCompareValue(new LongWritable(timeTag));

                //写入reduce端
                context.write(outKey, parsedLog);
            } catch (Exception e) {
                errorCounter.increment(1);
                LogGenericWritable v = new LogWritable();
                v.put("error_flag", new LogFieldWritable("error"));
                v.put("error_log", new LogFieldWritable(value));
                TextLongWritable outKey = new TextLongWritable();
                int randomKey = (int) (Math.random() * 100);
                outKey.setText(new Text("error" + randomKey));
                context.write(outKey, v);
            }
        }
    }

    public static class LogReducer extends Reducer<TextLongWritable, LogGenericWritable, Text, Text> {
        private Text sessionID;
        private JSONArray actionPath = new JSONArray();
        //添加多输出
        private MultipleOutputs outputs;

        public void setup(Context context) throws IOException, InterruptedException {

            IPUtil.load("17monipdb.dat");
            outputs = new MultipleOutputs(context);
        }

        public void reduce(TextLongWritable key, Iterable<LogGenericWritable> values, Context context) throws IOException, InterruptedException {
            //获取session_id
            Text sid = key.getText();
            //如果session_id为空或者session_id与sid不相等
            if (sessionID == null || !sid.equals(sessionID)) {
                sessionID = new Text(sid);
                actionPath.clear();
            }

            Long timeStamp = key.getCompareValue().get();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String time_tag = sdf.format(new Date(Long.parseLong(String.valueOf(timeStamp))));

            for (LogGenericWritable v : values) {
                JSONObject datum = JSON.parseObject(v.asJsonString());

                if (v.getObject("error_flag") == null) {

                    String ip = (String) v.getObject("ip");
                    String[] address = IPUtil.find(ip);
                    JSONObject addr = new JSONObject();
                    addr.put("country", address[0]);
                    addr.put("province", address[1]);
                    addr.put("city", address[2]);

                    //获取active_name和url
                    String activeName = (String) v.getObject("active_name");
                    String reqUrl = (String) v.getObject("req_url");

                    String pathUnit = "pageview".equals(activeName) ? reqUrl : activeName;
                    actionPath.add(pathUnit);

                    datum.put("address", addr);
                    datum.put("action_path", actionPath);

                }
                //String outputKey = v.getObject("error_flag") == null ? "part" : "error/part";
                //context.write(new Text(outputKey), new Text(datum.toJSONString()));
                outputs.write(NullWritable.get(), new Text(datum.toJSONString()), "day="+time_tag+"/part");
            }
        }

        public void cleanup(Context context) throws IOException, InterruptedException {
            outputs.close();
        }
    }

    public int run(String[] args) throws Exception {
        //创建job
        Configuration config = getConf();
        //添加自定义配置
        config.addResource("mr.xml");
        Job job = Job.getInstance(config);
        //通过job设置一些参数
        //通过job设置一些参数
        job.setJarByClass(ParseLogJob_End.class);
        job.setJobName("parselog");
        job.setMapperClass(LogMapper.class);
        job.setReducerClass(LogReducer.class);
        job.setMapOutputKeyClass(TextLongWritable.class);
        job.setGroupingComparatorClass(TextLongGroupComparator.class);
        job.setPartitionerClass(TextLongPartition.class);
        job.setMapOutputValueClass(LogWritable.class);
        job.setOutputValueClass(Text.class);

        //设置CombineFileInputFormat
        job.setInputFormatClass(CombineTextInputFormat.class);

        //添加分布式缓存
        job.addCacheFile(new URI(config.get("ip.file.path")));

        //设置OutputFormat
//        job.setOutputFormatClass(LogOutputFormat.class);


        //设置压缩类型
//        FileOutputFormat.setCompressOutput(job, true);
//        FileOutputFormat.setOutputCompressorClass(job, LzopCodec.class);

        FileSystem fs = FileSystem.get(config);
        //添加输入和输出数据
        FileStatus[] fileStatuses = fs.listStatus(new Path(args[0]));
        for (int i = 0; i < fileStatuses.length; i++) {
            MultipleInputs.addInputPath(job, fileStatuses[i].getPath(), TextInputFormat.class, LogMapper.class);
            String inputPath = fileStatuses[i].getPath().toString();
            String dir_name = inputPath.substring(inputPath.lastIndexOf('/')+1);
            System.out.println(dir_name);
        }

        Path outputPath = new Path(args[1]);
        FileOutputFormat.setOutputPath(job, outputPath);
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
        int res = ToolRunner.run(new Configuration(), new ParseLogJob_End(), args);
        System.exit(res);
    }

}