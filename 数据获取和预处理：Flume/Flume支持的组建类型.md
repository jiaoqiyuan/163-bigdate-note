## Source

- avro source / thrift source : 监听端口，端口的数据协议分别为avro/thrift。

    > avro是数据序列化系统，用于大批量数据交换应用。

- exec source : 获取unix命令执行时的标准输出， 如tail -F /var/log/xxxx

- taildir source : 监听指定的文件或文件夹，几乎实时地读取追加的行。

- kafka souce ： 从指定的kafka topic中消费数据。

## Channel

- memory channel：将数据缓存在内存中

    - 高吞吐

    - 容量低

    - Agent宕机时丢失数据

- file channel：利用本地文件缓存数据

    - 容量大

    - Agent宕机时数据可恢复

    - 速度慢，吞吐量小

    **通常情况下流量达到2M/s时，就不适合用file channel缓存了**

- kafka channel：

    - 结合Flume的source和sink，作为一种高可靠的channel

    - 在flume没有sink的情况下，将event写入制定topic，供其他程序使用

    - 在flume没有source的情况下，提供一种地延迟和容错的方式将kafka中数据直接发送到sink端。

## Sink

- HDFS Sink：支持按文件大小或运行时间来周期性生成文件。

- Hive Sink：将text或json格式数据直接写入hive表或分区。

- kafka Sink

- HBase sink

- ElasticSearch Sink