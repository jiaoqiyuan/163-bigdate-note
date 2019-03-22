package com.bigdata.etl.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ParseLogJob {

    public static Text parseLog(String row) throws ParseException {
        String[] logPart = StringUtils.split(row, "\u1111");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        long timeTag = dateFormat.parse(logPart[0]).getTime();

        String activeName = logPart[1];

        JSONObject bizData = JSON.parseObject(logPart[2]);
        JSONObject logData = new JSONObject();
        logData.put("active_name", activeName);
        logData.put("time_tag", timeTag);
        logData.putAll(bizData);

        return new Text(logData.toJSONString());
    }

    public static class LogMapper extends Mapper<LongWritable, Text, NullWritable, Text> {
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            try {
                Text parsedLog = parseLog(value.toString());
                context.write(null, parsedLog);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //创建job
        Configuration config = new Configuration();
        Job job = Job.getInstance(config);
        //通过job设置一些参数
        job.setJarByClass(ParseLogJob.class);
        job.setJobName("parselog");
        job.setMapperClass(LogMapper.class);
        //设置reduce个数为0
        job.setNumReduceTasks(0);

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
    }

}
