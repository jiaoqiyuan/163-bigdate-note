package com.bigdata.etl.homework;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

public class GetLinesUsersJob extends Configured implements Tool{

    public static String parseLog(String row) throws Exception {

        JSONObject bizData = JSON.parseObject(row);
        return bizData.getString("user_id");
    }


    public static class LogMapper extends Mapper<LongWritable, Text, Text, Text> {
        public void map(LongWritable key, Text value, Context context) {
            Counter errorCounter = context.getCounter("Log error", "Parse error");
            try {
                //解析每行日志
                String user = parseLog(value.toString());

                //写入reduce端,使用Text("test")进行占位，本想使用null占位，但是reduce端需要遍历Map的输出，所以不能使用NullWritable
                context.write(new Text(user), new Text("test"));
            } catch (Exception e) {
                errorCounter.increment(1);
                e.printStackTrace();
            }
        }
    }

    public static class LogReducer extends Reducer<Text, Text, NullWritable, NullWritable> {
        private Text userID;

        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            //创建行数和用户数的计数器
            Counter linesCounter = context.getCounter("LinesCounter", "Lines");
            Counter userCounter = context.getCounter("UserCounter", "Users");

            //获取user_id
            Text uid = key;
            //如果session_id为空或者user_id与uid不相等
            if (userID == null || !uid.equals(userID)) {
                userID = new Text(uid);
                userCounter.increment(1);
            }

            for (Text v : values ) {
                linesCounter.increment(1);
            }
            context.write(null, null);
        }
    }


    public int run(String[] args) throws Exception {
        //创建job
        Configuration config = getConf();
        Job job = Job.getInstance(config);
        //通过job设置一些参数
        job.setJarByClass(GetLinesUsersJob.class);
        job.setJobName("getLinesAndUser");
        job.setMapperClass(LogMapper.class);
        job.setReducerClass(LogReducer.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        //设置CombineFileInputFormat
//        job.setInputFormatClass(CombineTextInputFormat.class);


        //添加输入和输出数据
        FileInputFormat.addInputPath(job, new Path(args[0]));
        Path outputPath = new Path(args[1]);
        FileOutputFormat.setOutputPath(job, outputPath);

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
        int res = ToolRunner.run(new Configuration(), new GetLinesUsersJob(), args);
        System.exit(res);

    }
}
