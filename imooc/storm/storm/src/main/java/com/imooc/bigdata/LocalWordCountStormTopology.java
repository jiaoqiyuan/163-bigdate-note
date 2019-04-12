package com.imooc.bigdata;

import org.apache.commons.io.FileUtils;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class LocalWordCountStormTopology {
    /**
     * 使用 Strom 完成词频统计功能
     */

    public static class DataSourceSpout extends BaseRichSpout {

        private SpoutOutputCollector collector;

        @Override
        public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
            this.collector = collector;
        }

        /**
         * 1.读取指定目录下的文件数据
         * 2.把读取到的每一行数据发送出去
         */
        @Override
        public void nextTuple() {
            //获取所有文件
            Collection<File> files = FileUtils.listFiles(new File("/home/jony/Desktop"), new String[]{"txt"}, true);
            for (File file : files) {
                try {
                    //获取文件所有内容
                    List<String> lines = FileUtils.readLines(file);
                    for (String line : lines) {
                        //将每行内容发送出去
                        line.replaceAll(",", "");
                        this.collector.emit(new Values(line));
                    }

                    //数据读取完成后更改文件名，否则一致读取
                    FileUtils.moveFile(file, new File(file.getAbsolutePath() + System.currentTimeMillis()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("line"));
        }
    }

    /**
     * 对数据进行分割
     */
    public static class SplitBolt extends BaseRichBolt {

        private OutputCollector collector;

        @Override
        public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
            this.collector = collector;
        }

        /**
         * 对line按照逗号进行分割
         *
         * @param input
         */
        @Override
        public void execute(Tuple input) {
            String line = input.getStringByField("line");
            String[] words = line.split(" ");
            for (String word : words) {
                this.collector.emit(new Values(word));
            }
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("word"));
        }
    }

    /**
     * 词频汇总Bolt
     */
    public static class CountBolt extends BaseRichBolt {

        @Override
        public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {

        }

        Map<String, Integer> map = new HashMap<String, Integer>();

        /**
         * 1.获取每个单词
         * 2.对所有单词进行汇总
         * 3.输出
         *
         * @param input
         */
        @Override
        public void execute(Tuple input) {
            //获取每个单词
            String word = input.getStringByField("word");

            //对所有单词汇总
            Integer count = map.get(word);
            if (count == null) {
                count = 0;
            }
            count++;

            map.put(word, count);

            //输出
            System.out.println("------------------------------------");
            Set<Map.Entry<String, Integer>> entrySet = map.entrySet();
            for (Map.Entry<String, Integer> entry : entrySet) {
                System.out.println(entry);
            }
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {

        }
    }

    public static void main(String[] args) {
        //根据Spout和Bolt构建TopologyBuilder
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("DataSourceSpout", new DataSourceSpout());
        builder.setBolt("SplitBolt", new SplitBolt()).shuffleGrouping("DataSourceSpout");
        builder.setBolt("CountBolt", new CountBolt()).shuffleGrouping("SplitBolt");

        //创建本地集群
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("LocalWordCountStormTopology", new Config(), builder.createTopology());

    }
}
