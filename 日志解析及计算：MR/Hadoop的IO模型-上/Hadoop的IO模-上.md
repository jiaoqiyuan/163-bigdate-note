# Hadoop的IO模型-上

## 数据序列化

数据中心中最稀缺的资源就是网络带宽，在数据量巨大的分布式系统中，数据的紧凑高效传输和解析十分重要。

- 数据序列化需要满足以下条件：

    - 紧凑：序列化出来的字节尽可能少。

    - 快速：编码和解码速度要快。

    - 可扩展：如果增加了新的序列化协议，可以在原有基础上进行扩展。

    - 支持互操作：需要在不同语言编写的程序之间使用相同的序列化协议。


**Hadoop为什么不使用Java自带的序列化方法？**

因为Java的序列化方法很重，最终编码出来的字节码中有很多冗余信息，这样就不满足“紧凑”的条件，因此Hadoop自己实现了一套序列化接口，就叫Writable接口。

## Writable接口

Hadoop使用自己的序列格式Writable，它紧凑且速度快。Writable接口定义如下：
```java
public interface Writable {
  /** 
   * Serialize the fields of this object to <code>out</code>.
   * 
   * @param out <code>DataOuput</code> to serialize this object into.
   * @throws IOException
   */
  void write(DataOutput out) throws IOException;

  /** 
   * Deserialize the fields of this object from <code>in</code>.  
   * 
   * <p>For efficiency, implementations should attempt to re-use storage in the 
   * existing object where possible.</p>
   * 
   * @param in <code>DataInput</code> to deseriablize this object from.
   * @throws IOException
   */
  void readFields(DataInput in) throws IOException;
}
```

Hadoop本身提供了对Java基本类型的Writable封装，对String类型封装为Text类，也提供了对Array和Map集合类型的Writable封装。如下所示：

| Java基本类型 | Writable | 序列化后长度 |
|:-----------:|:--------:|:-----------:|
| 布尔型 boolean | BooleanWritable | 1 |
| 字节型 byte | ByteWritable | 1 |
| 整形 int | IntWritable or VIntWritable | 4 or 1~5 |
| 浮点型 float | FloatWritable | 4 |
| 长整型 | LongWritable or VLongWritable | 8 or 1~9 |
| 双精度浮点类型 double | DoubleWritable | 8 |

- 集合类型的封装一般来说，首先要在序列化的时候把序列的大小写入进去，然后再将集合中的元素依次序列化。

- 在Hadoop框架中不仅可以使用Hadoop已经提供的序列化类型，还可使用自定义的序列化类型

### 自定义Writable类型

自定义Writable封装类一般由基本类型、集合类型和引用类型组成。自定义的Writable类型首先需要实现Writable接口，在实现readFields和write方法的时候有以下原则：

- 基本类型可以用read{类型}，write{类型}，如readInt方法。

- String类型和可变长类型可以使用Hadoop提供的WritableUtils类辅助。

- 集合类型参考Hadoop的ArrayWritable等类型的自定义实现，一般情况下，在序列化过程中，先将集合大小写进去，然后将集合的元素一一序列化；在反序列化过程在，先将集合的大小读取出来，然后在内存中开辟这个大小的空间，再将集合中的元素一一反序列化到内存中。

- 引用类型本身必须也实现了Writable接口，直接调用引用类型的readFields和write方法。

### 通用Writable类型 

- ObjectWritable

    处理null、Java数组、字符串String、Java基本类型、枚举和Writable的子类6中情况，但是使用ObjectWritable将类名、实例序列化，一般情况下类名会比较长，造成资源浪费。

    实际应用场景中一般不适用ObjectWritable。

- GenericWritable

    如果类型的数量不是很多，而且可以事先知道，可以通过一个静态数组指定所有类型，再序列化的时候使用数组下标代替类名进行序列化。数组下边使用一个字节就能存储，这样相比于ObjectWritable大大减少了资源浪费。


## 文件压缩

文件压缩可以减少磁盘存储空间的使用，也可以减少数据在网络上的传输时间，与Hadoop结合的常用算法有以下几种：

- Gzip：压缩效率和速度剧中，不可切分。

- Bzip2：压缩效率搞，但是速度慢，可切分。

- LZO，LZ4，Snappy：压缩速度块，但是压缩效率不高，不可切分。

MR原理中讲过，文件分片后会交给Map进行处理，如果文件不支持分片，那就无法被分到不同的Map进行处理，最终这个文件会被放到一个Map任务上进行处理，这样的话MR程序的运行效率就会很低（本来可以并行处理的现在编程单机处理了）。所以一般在选择文件压缩格式时都要选择可以切分的压缩格式。

Tips：

- LZO在加索引的情况下可以切分。

- Snappy和LZ4的解压缩速率比LZO高出很多。

### 文件分片

文件分片对于MapReduce的执行效率十分重要，如果文件不支持分片会产生两个问题：

- 一个Map只能处理一个文件，Map处理时间长。

- 牺牲了数据本地性，一个大文件的多个快存在不同的节点上，如果文件不能分片，则需要将所有的块通过网络传输到一个Map上计算。

### 合理选择压缩格式

综合节约磁盘空间，网络带宽和MR的执行效率考虑，一般使用如下压缩格式：

- 对于经常访问的数据，使用LZO + 索引的方式存储，因为它的解压速度非常快，这样对于读取文件的速度就非常快。

- 对于不经常访问的数据，采用bzip2压缩存储，比如历史比较久远的数据

- Map和Reduce中间的数据传输，采用Snappy压缩，因为它的压缩和解压速度都非常快。

## 实际操作

代码：

- [ParseLogJob.java](https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%97%A5%E5%BF%97%E8%A7%A3%E6%9E%90%E5%8F%8A%E8%AE%A1%E7%AE%97%EF%BC%9AMR/Hadoop%E7%9A%84IO%E6%A8%A1%E5%9E%8B-%E4%B8%8A/etl/src/main/com/bigdata/etl/job/ParseLogJob.java)

- [LogBeanWritable.java](https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%97%A5%E5%BF%97%E8%A7%A3%E6%9E%90%E5%8F%8A%E8%AE%A1%E7%AE%97%EF%BC%9AMR/Hadoop%E7%9A%84IO%E6%A8%A1%E5%9E%8B-%E4%B8%8A/etl/src/main/com/bigdata/etl/mr/LogBeanWritable.java)