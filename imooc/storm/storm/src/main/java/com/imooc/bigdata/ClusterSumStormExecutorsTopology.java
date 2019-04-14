package com.imooc.bigdata;

import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
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
import org.apache.storm.utils.Utils;

import java.util.Map;

/**
 * 使用 strom 实现累积求和操作
 */

public class ClusterSumStormExecutorsTopology {

    /**
     * Spout 需要继承 BaseRichSpout，数据源需要产生数据并发射
     */
    public static class DataSourceSpout extends BaseRichSpout {

        private SpoutOutputCollector collector;

        /**
         * 初始化方法，执行前被调用一次
         * @param conf      配置参数
         * @param context   上下文
         * @param collector 数据发射器
         */
        @Override
        public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
            this.collector = collector;
        }

        int number = 0;
        /**
         * 会产生数据，在生产上是从消息队列中获取数据。这个方法是死循环，会一致不停执行
         */
        @Override
        public void nextTuple() {
            this.collector.emit(new Values(++number));

            System.out.println("Spout: " + number);

            //每隔一秒发送一次，防止太快
            Utils.sleep(1000);
        }

        /**
         * 声明输出字段
         * @param declarer
         */
        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("num"));
        }
    }

    /**
     * 数据累计求和 Bolt ：接收数据并处理
     */
    public static class SumBolt extends BaseRichBolt {

        /**
         * 初始化方法，会被执行一次
         * @param stormConf
         * @param context
         * @param collector
         */
        @Override
        public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {

        }

        int sum = 0;
        /**
         * 也是一个死循环，用于获取 Spout 发送过来的数据
         * @param input
         */
        @Override
        public void execute(Tuple input) {
            // Bolt 中获取值可以根据 index 获取，也可以根据上一个环节中定义的 field 的名称获取（建议使用这种方法获取）
            Integer value = input.getIntegerByField("num");
            sum += value;
            System.out.println("Bolt: sum = " + sum);
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {

        }
    }

    public static void main(String[] args) {
        //根据Spout和Bolt构建出TopologyBuilder，Storm中任何作业都是通过Topology提交的，Topology中需要指定Spout和Bolt的顺序
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("DataSourceSpout", new DataSourceSpout(), 2);
        builder.setBolt("SumBolt", new SumBolt(), 2).shuffleGrouping("DataSourceSpout");


        //创建一个集群模式运行的Storm集群
        String name = ClusterSumStormExecutorsTopology.class.getSimpleName();
        try {
            Config config = new Config();
            config.setNumWorkers(2);
            config.setNumAckers(0);
            StormSubmitter.submitTopology(name, config, builder.createTopology());
        } catch (AlreadyAliveException e) {
            e.printStackTrace();
        } catch (InvalidTopologyException e) {
            e.printStackTrace();
        } catch (AuthorizationException e) {
            e.printStackTrace();
        }


    }
}
