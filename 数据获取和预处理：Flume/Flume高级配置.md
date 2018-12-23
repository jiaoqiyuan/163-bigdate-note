## 多个Flume Agent串联

![multi-agent][1]

Flume串联需要满足前一个agent的sink和后一个agent的source以avro的方式连接起来即可。

大型客户端生成日志，存储系统处理能力不足的情况下，可以将flume配置成层级结构：

![flume层级][2]

可以在每台服务器上配置一个flume agent，然后将这些agent再串联一个公用的agent进行日志收集，这个共用的agent将收集到的日志写到hdfs上。


**这里会存在一个隐患，如果公用的agent挂了，那么日志系统整个就崩溃了.**

针对这种情况，可以使用flume的负载均衡模式，配置如下：

```conf
#load-balance
agent1.sources=r1
agent1.channels=c1
agent1.sinks=k1 k2  #配置多个sink，分别指向第二级的agent

agent1.sinkgroups = g1
agent1.sinkgroups.g1.sinks = k1 k2
agent1.sinkgroups.g1.processor.type = load_balance

agent1.sources.r1.channels = c1
agent1.sinks.k1.channel = c1
agent1.sinks.k2.channel = c1
```

## 场景：采集客户端的不同服务生成的日志，根据类型发送到不同的存储服务。

![多路复用][3]

flume支持扇出的模式，扇出有两种方式：复制和多路复用，支持同一份source发送到不同的sink。

- 复制：

    ```conf
    agent_foo.sources = r1
    agent_foo.channels = c1 c2 c3
    agent_foo.sinks = k1 k2 k3

    agent_foo.sources.r1.channels = c1 c2 c3
    agent_foo.sinks.k1.channel = c1
    agent_foo.sinks.k2.channel = c2
    agent_foo.sinks.k3.channel = c3
    ```

- 多路复用：

    ```conf
    agent_foo.sources = r1
    agent_foo.channels = c1 c2 c3
    agent_foo.sinks = k1 k2 k3

    agent_foo.sources.r1.channels = c1 c2 c3
    agent_foo.sinks.k1.channel = c1
    agent_foo.sinks.k2.channel = c2
    agent_foo.sinks.k3.channel = c3

    agent_foo.sources.r1.selector.type = multiplexing
    agent_foo.sources.r1.selector.header = state
    agent_foo.sources.r1.selector.mapping.CZ = c1
    agent_foo.sources.r1.selector.mapping.US = c2
    agent_foo.sources.r1.selector.default = c3
    ```

    假如event内的数据格式如下：
    ```
    {"headers" : {"state" : "CZ"}, "body" : "msg1"}
    {"headers" : {"state" : "US"}, "body" : "msg2"}
    {"headers" : {"state" : "XX"}, "body" : "msg3"}
    ```

    那么msg1将分配到c1,msg2将分配到c2,msg3将分配到c3。

    headers怎么设置呢？flume有内置方法。

## 拦截器

使用拦截器可以向event中添加自己想要内容。

- Timestamp Interceptor：在event中插入当前时间戳

- Host Interceptor：插入主机名

- Static Interceptor：插入固定内容

- Regex Filetering Interceptor：插入正则表达式匹配的内容

## 拦截器示例

- 需求：根据日志内的时间进行分类

- 拦截器配置：

```conf
a1.sources.r1.interceptors = i1
a1.sources.r1.interceptors.i1.type = regex_extractor
a1.sources.r1.interceptors.i1.regex = (\\d\\d\\d\\d-\\d\\d-\\d\\d)
a1.sources.r1.interceptors.i1.serializers = s1
a1.sources.r1.interceptors.i1.serializers.s1.name = date

# sink
a1.sinks.k1.hdfs.path = /user/flume/%{date}
```

- 预期处理结果：

```
{"headers" : {"date" : "2018-05-11"}, "body" : "msg"}
```

- 配置flume

    ```conf
    agent.sources = fileSrc
    agent.channels = memoryChannel
    agent.sinks = hdfsSink

    # For each one of the sources, the type is defined
    agent.sources.fileSrc.type = taildir
    agent.sources.fileSrc.positionFile = /mnt/home/1015146591/flume_log/hdfs_sink/interceptor/positionFile
    agent.sources.fileSrc.filegroups = f1
    agent.sources.fileSrc.filegroups.f1 = /mnt/hadoop/log/.*.log

    agent.sources.fileSrc.interceptors = i1
    agent.sources.fileSrc.interceptors.i1.type = regex_extractor
    agent.sources.fileSrc.interceptors.i1.regex = (\\d\\d\\d\\d-\\d\\d-\\d\\d)
    agent.sources.fileSrc.interceptors.i1.serializers = s1
    agent.sources.fileSrc.interceptors.i1.serializers.s1.name = date

    # The channel can be defined as follows.
    agent.sources.fileSrc.channels = memoryChannel

    # Each sink's type must be defined
    agent.sinks.hdfsSink.type = hdfs
    agent.sinks.hdfsSink.hdfs.path = /user/1015146591/flume_data/interceptor/%{date}
    agent.sinks.hdfsSink.hdfs.fileType = DataStream
    agent.sinks.hdfsSink.hdfs.rollSize = 0
    agent.sinks.hdfsSink.hdfs.rollCount = 0
    agent.sinks.hdfsSink.hdfs.rollInterval = 60

    #Specify the channel the sink should use
    agent.sinks.hdfsSink.channel = memoryChannel

    # Each channel's type is defined.
    agent.channels.memoryChannel.type = memory

    # Other config values specific to each type of channel(sink or source)
    # can be defined as well
    # In this case, it specifies the capacity of the memory channel
    agent.channels.memoryChannel.capacity = 100
    ```

- 启动flume

```
./bin/flume-ng agent --conf conf --conf-file conf/flume-conf-taildir2hdfs-interceptor.properties --name agent 
```

- 查看hdfs上文件

```
[1015146591@bigdata4 ~]$ hadoop fs -ls  /user/1015146591/flume_data/interceptor
Found 5 items
drwxr-x---   - 1015146591 supergroup          0 2018-12-18 10:29 /user/1015146591/flume_data/interceptor/2018-05-29
drwxr-x---   - 1015146591 supergroup          0 2018-12-18 10:29 /user/1015146591/flume_data/interceptor/2018-05-30
drwxr-x---   - 1015146591 supergroup          0 2018-12-18 10:29 /user/1015146591/flume_data/interceptor/2018-05-31
drwxr-x---   - 1015146591 supergroup          0 2018-12-18 10:29 /user/1015146591/flume_data/interceptor/2018-06-01
drwxr-x---   - 1015146591 supergroup          0 2018-12-18 10:29 /user/1015146591/flume_data/interceptor/2018-06-02

```



[1]: https://flume.apache.org/_images/UserGuide_image03.png
[2]: https://flume.apache.org/_images/UserGuide_image02.png
[3]: https://flume.apache.org/_images/UserGuide_image01.png