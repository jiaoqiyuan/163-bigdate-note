## 基本配置

- 安装包结构

    - bin：执行目录，flume-ng启动进程

    - conf：配置文件目录，flume-env.sh, flume-conf.properties, log4j.properties

    - lib：jar包目录

    - logs：日志文件目录

    - docs：帮助文档目录

    - tools：工具包目录

- 以配置文件方式保存Flume agent的配置信息：

    ```conf
    agent.sources = seqGenSrc
    agent.channels = memoryChannel
    agent.sinks = loggerSink

    # For each one of the sources, the type is defined
    agent.sources.seqGenSrc.type = seq

    # The channel can be defined as follows.
    agent.sources.seqGenSrc.channels = memoryChannel

    # Each sink's type must be defined
    agent.sinks.loggerSink.type = logger

    #Specify the channel the sink should use
    agent.sinks.loggerSink.channel = memoryChannel

    # Each channel's type is defined.
    agent.channels.memoryChannel.type = memory

    # Other config values specific to each type of channel(sink or source)
    # can be defined as well
    # In this case, it specifies the capacity of the memory channel
    agent.channels.memoryChannel.capacity = 100

    ```

## 基于Zookepper配置

```
- /flume
    - /a1 [agent config file]
    - /a2 [agent config file]


bin/flume-ng agent --conf conf -z zkhost:2181,zkhost1:2181 -p /flume --name a1
z:zookepper连接地址
p:zookepper中保存agent配置的根路径
a1：agent名称
```

## Flume特性

- 模块化设计：source/channel/sink三种组建可以根据业务自由组合，构建相对复杂的日志流管道

- 可接入性：支持集成多种主流系统和框架，像HDFS、HBase、Hive、Kafka、ES、Thrift、Avro等，都能很好地和Flume集成。

- 容错性：event是事务的，保证了数据在源端和目的端的一致性

- 可扩展性：可以根据自己业务的需要来定制实现某些组件（Source、Channel、Sink）

