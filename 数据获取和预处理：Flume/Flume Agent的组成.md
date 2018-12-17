## Flume Agent

![Agent][1]

- Flume将日志从服务器将日志传输到分布式系统的HDFS进行保存。

- Flume Agent由Source、Channel、Sink组成

    - Source：从数据接收器接收数据，并将数据以Flume的event格式传递给一个或多个通道Channel。Flume提供多种数据接收方式，比如Taildir，Kafka等。

    - Channel：Channel是一种短暂的存储容器，它将从source接收到的event格式的数据缓存起来，知道它们被sink消费掉。它在source和sink间起着一种桥梁的作用。支持的类类型有：FileChannel，Memory Channel等。

    - Sink：sink将数据保存到存储器中，它从channel消费数据（event）并将其传递给目标地址，目标地可能是Hive，HDFS，Hbase等。

## 基本工作流程

- Flume的核心是把数据从数据源（source）收集过来，再将收集到的数据发送到指定的目的地（sink）。为保证发送过程一定成功，在送到目的地之前（sink）之前，会先缓存数据（Channel），待数据真正到达目的地（sink）后，Flume再删除自己缓存的数据。

```
Source ---event----> Channel ---envent---> Sink
```

## Flume Event

- Event将传输的数据进行封装，是Flume传输数据的基本单位，如果是文本文件，通常是一行记录。

- Event从source，流向channel，再到sink，本身为一个字节数组，可携带header信息。

- Event是事务的基本单位，代表一个数据的最小完成单元。

```java
FlumeEvent数据结构：
class FlumeEvent implements Event, Writable {
    private Map<String, String> headers;
    private byte[] body;
}

Event示例：
{ "headers" : {"mykey" : "myvalue"}, "body" : "real log msg"}
```

[1]: https://flume.apache.org/_images/DevGuide_image00.png