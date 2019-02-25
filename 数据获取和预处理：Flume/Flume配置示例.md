## 实例一 监听端口，输出到控制台

- 监听端口，输出到控制台

    ```
    Source.type = netcat
    Channel.type = memory
    Sink.type = logger

    ./bin/flume-ng agent --conf conf --conf-file conf/conf.properties --name agent -Dflume.root.logger=INFO,console

    ```

- 配置flume-conf-netsrc.properties文件

    ```conf
    agent.sources = netSrc
    agent.channels = memoryChannel
    agent.sinks = loggerSink

    # For each one of the sources, the type is defined
    agent.sources.netSrc.type = netcat
    agent.sources.netSrc.bind = 0.0.0.0
    agent.sources.netSrc.port = 12345

    # The channel can be defined as follows.
    agent.sources.netSrc.channels = memoryChannel

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

- 启动flume

    ```
    ./bin/flume-ng agent --conf conf --conf-file conf/flume-conf-netsrc.properties --name agent -Dflume.root.logger=INFO,console
    ```

- 使用telnet连接12345端口

    ```
    telnet localhost 12345
    ```

    输入一些内容

    ```
    hello
    OK
    workd
    OK
    workd
    OK
    world
    OK
    ```

    在flume启动的终端下面查看输出

    ```
    2018-12-17 15:12:02,438 (SinkRunner-PollingRunner-DefaultSinkProcessor) [INFO - org.apache.flume.sink.LoggerSink.process(LoggerSink.java:95)] Event: { headers:{} body: 68 65 6C 6C 6F 0D                               hello. }
    2018-12-17 15:12:03,616 (SinkRunner-PollingRunner-DefaultSinkProcessor) [INFO - org.apache.flume.sink.LoggerSink.process(LoggerSink.java:95)] Event: { headers:{} body: 77 6F 72 6B 64 0D                               workd. }
    2018-12-17 15:12:05,047 (SinkRunner-PollingRunner-DefaultSinkProcessor) [INFO - org.apache.flume.sink.LoggerSink.process(LoggerSink.java:95)] Event: { headers:{} body: 77 6F 72 6B 64 0D                               workd. }
    2018-12-17 15:12:06,454 (SinkRunner-PollingRunner-DefaultSinkProcessor) [INFO - org.apache.flume.sink.LoggerSink.process(LoggerSink.java:95)] Event: { headers:{} body: 77 6F 72 6C 64 0D                               world. }
    ```

## 示例二 监听端口，输出到服务器文件

- 监听端口，输出到服务器文件

    这里使用file_roll用于将event数据存储到本地文件系统。

    ```
    Source.type = netcat
    Channel.type = memory
    Sink.type = file_roll

    ./bin/flume-ng agent --conf conf --conf-file conf/conf.properties --name agent -Dflume.root.logger=INFO,console
    ```
- 配置flume-conf-netsrc2localfile.properties文件

    ```conf
    agent.sources = netSrc
    agent.channels = memoryChannel
    agent.sinks = fileSink

    # For each one of the sources, the type is defined
    agent.sources.netSrc.type = netcat
    agent.sources.netSrc.bind = 0.0.0.0
    agent.sources.netSrc.port = 12345

    # The channel can be defined as follows.
    agent.sources.netSrc.channels = memoryChannel

    # Each sink's type must be defined
    agent.sinks.fileSink.type = file_roll
    agent.sinks.fileSink.sink.directory=/mnt/home/1015146591/flume_log/file_roll/data
    agent.sinks.fileSink.sink.rollInterval = 60

    #Specify the channel the sink should use
    agent.sinks.fileSink.channel = memoryChannel

    # Each channel's type is defined.
    agent.channels.memoryChannel.type = memory

    # Other config values specific to each type of channel(sink or source)
    # can be defined as well
    # In this case, it specifies the capacity of the memory channel
    agent.channels.memoryChannel.capacity = 100

    ```

- 启动flume

    ```
    ./bin/flume-ng agent --conf conf --conf-file conf/flume-conf-netsrc2localfile.properties --name agent -Dflume.root.logger=INFO,console
    ```
    
- 开启telnet，发送数据

    ```
    telnet localhost 12345
    ```

    发送的数据：
    
    ```
    hello
    OK
    world
    OK
    flume
    OK
    ```

- 在配置文件中填写的路径下面查看telnet发送的数据内容是否写入到了文件中

    ```
    [1015146591@bigdata4 data]$ cat 1545031636278-1 
    hello
    world
    flume
    ```

    60秒后，该目录下又生成了一个文件1545031636278-2

## 示例三 监听服务器文件，输出到HDFS（memChannel）

- 监听服务器文件，输出到HDFS

    ```conf
    Source.type = taildir
    Channel.type = memory
    Sink.type = hdfs
    ```

- 修改flume-env.sh

    ```
    export HADOOP_HOME=/home/hadoop/hadoop-current
    FLUME_CLASSPATH="$HADOOP_HOME/bin:$PATH"

    ```

- 配置flume

    ```conf
    agent.sources = fileSrc
    agent.channels = memoryChannel
    agent.sinks = hdfsSink

    # For each one of the sources, the type is defined
    agent.sources.fileSrc.type = taildir
    #agent.sources.fileSrc.positionFile = /mnt/home/1139372816/workspace/hdfs_sink/mem_ch/positionFile
    agent.sources.fileSrc.positionFile = /mnt/home/1015146591/flume_log/hdfs_sink/mem_ch/positionFile
    agent.sources.fileSrc.filegroups = f1
    agent.sources.fileSrc.filegroups.f1 = /mnt/hadoop/log/.*.log

    # The channel can be defined as follows.
    agent.sources.fileSrc.channels = memoryChannel

    # Each sink's type must be defined
    agent.sinks.hdfsSink.type = hdfs
    agent.sinks.hdfsSink.hdfs.path = /user/1015146591/flume_data/mem_ch/%Y-%m-%d
    agent.sinks.hdfsSink.hdfs.useLocalTimeStamp = true
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
    ./bin/flume-ng agent --conf conf --conf-file conf/flume-conf-taildir2hdfs.properties --name agent
    ```

- 查看hdfs上是否已经被写入数据

    ```
    [1015146591@bigdata4 mem_ch]$ hadoop fs -ls /user/1015146591/flume_data/mem_ch/
    Found 1 items
    drwxr-x---   - 1015146591 supergroup          0 2018-12-17 16:28 /user/1015146591/flume_data/mem_ch/2018-12-17
    [1015146591@bigdata4 mem_ch]$ hadoop fs -ls /user/1015146591/flume_data/mem_ch/2018-12-17
    Found 3 items
    -rw-r-----   3 1015146591 supergroup  203264603 2018-12-17 16:26 /user/1015146591/flume_data/mem_ch/2018-12-17/FlumeData.1545035113452
    -rw-r-----   3 1015146591 supergroup  217865842 2018-12-17 16:27 /user/1015146591/flume_data/mem_ch/2018-12-17/FlumeData.1545035175202
    -rw-r-----   3 1015146591 supergroup  120086602 2018-12-17 16:28 /user/1015146591/flume_data/mem_ch/2018-12-17/FlumeData.1545035235664

    ```

## 示例4 监听服务器文件，输出到HDFS（fileChannel）

- 监听服务器文件，输出到hdfs（file channel）

    ```conf
    Source.type = taildir
    Channel.type = file
    Sink.type = hdfs
    ```

    ```
    ./bin/flume-ng agent --conf conf --conf-file conf/conf.properties --name agen
    ```

- 配置flume

    ```conf
    agent.sources = fileSrc
    agent.channels = fileChannel
    agent.sinks = hdfsSink

    # For each one of the sources, the type is defined
    agent.sources.fileSrc.type = taildir
    #agent.sources.fileSrc.positionFile = /mnt/home/1139372816/workspace/hdfs_sink/mem_ch/positionFile
    agent.sources.fileSrc.positionFile = /mnt/home/1015146591/flume_log/hdfs_sink/file_ch/positionFile
    agent.sources.fileSrc.filegroups = f1
    agent.sources.fileSrc.filegroups.f1 = /mnt/hadoop/log/.*.log

    # The channel can be defined as follows.
    agent.sources.fileSrc.channels = fileChannel

    # Each sink's type must be defined
    agent.sinks.hdfsSink.type = hdfs
    agent.sinks.hdfsSink.hdfs.path = /user/1015146591/flume_data/file_ch/%Y-%m-%d
    agent.sinks.hdfsSink.hdfs.useLocalTimeStamp = true
    agent.sinks.hdfsSink.hdfs.fileType = DataStream
    agent.sinks.hdfsSink.hdfs.rollSize = 0
    agent.sinks.hdfsSink.hdfs.rollCount = 0
    agent.sinks.hdfsSink.hdfs.rollInterval = 60

    #Specify the channel the sink should use
    agent.sinks.hdfsSink.channel = fileChannel

    # Each channel's type is defined.
    agent.channels.fileChannel.type = file
    agent.channels.fileChannel.checkpointDir = /mnt/home/1015146591/flume_log/hdfs_sink/file_ch/checkpoint
    agent.channels.fileChannel.checkpointInterval = 60000
    agent.channels.fileChannel.dataDirs = /mnt/home/1015146591/flume_log/hdfs_sink/file_ch/data

    ```

- 启动flume

    ```
    ./bin/flume-ng agent --conf conf --conf-file conf/flume-conf-taildir2hdfs-filech.properties --name agent
    ```

- 查看hdfs上的文件和本地checkpoing

    ```
    [1015146591@bigdata4 mem_ch]$ hadoop fs -ls /user/1015146591/flume_data/file_ch/2018-12-17
    Found 2 items
    -rw-r-----   3 1015146591 supergroup     524876 2018-12-17 16:45 /user/1015146591/flume_data/file_ch/2018-12-17/FlumeData.1545036286118
    -rw-r-----   3 1015146591 supergroup      23844 2018-12-17 16:46 /user/1015146591/flume_data/file_ch/2018-12-17/FlumeData.1545036395098.tmp

    ```

    ```
    [1015146591@bigdata4 file_ch]$ ls
    checkpoint  data  positionFile
    [1015146591@bigdata4 file_ch]$ ls checkpoint/
    checkpoint  checkpoint.meta  inflightputs  inflighttakes  queueset
    ```