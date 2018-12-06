# MR程序的输出

- Java程序如何写数据到文件?

    FileOutputStream将数据写入文件

- Reducer中的Key/Value写到哪里去?

    OutputFormat把数据写入文件系统,默认是TextOutputFormat.

**下面将的都是以FileOutputFormat为例,并且使用默认的TextOutputFormat.其实还有其他的OutputFormat,比如写文件时可以使用SequenceFileOutputFormat将数据以二进制流的形式写到文件中.**

## OutputFormat

```java
public abstract class OutputFormat<K, V> {

  /** 
   * Get the {@link RecordWriter} for the given task.
   *
   * @param context the information about the current task.
   * @return a {@link RecordWriter} to write the output for the job.
   * @throws IOException
   */
  public abstract RecordWriter<K, V> getRecordWriter(TaskAttemptContext context) throws IOException, InterruptedException;

  /** 
   * Check for validity of the output-specification for the job.
   *  
   * <p>This is to validate the output specification for the job when it is
   * a job is submitted.  Typically checks that it does not already exist,
   * throwing an exception when it already exists, so that output is not
   * overwritten.</p>
   *
   * @param context information about the job
   * @throws IOException when output should not be attempted
   */
  public abstract void checkOutputSpecs(JobContext context) throws IOException, InterruptedException;

  /**
   * Get the output committer for this output format. This is responsible
   * for ensuring the output is committed correctly.
   * @param context the task context
   * @return an output committer
   * @throws IOException
   * @throws InterruptedException
   */
  public abstract OutputCommitter getOutputCommitter(TaskAttemptContext context ) throws IOException, InterruptedException;
}
```

- getRecordWriter()获取写数据的工具类,这个工具类就是RecordWriter,跟之前将的RecordReader相对应,RecordReader负责将数据从HDFS上读取出来,这里的RecordWirter负责将数据写入到HDFS上.

- checkOutputSpecs()检查输出目的地是否已经准备好或者可创建,在job提交时就运行.MR程序的输出目录如果已经存在,会报异常,这个异常就是在checkOutputSpecs()中产生的.

- getOutputCommitter()获取committer,committer是输出的提交者,用来确保作业和任务都完全成功或者失败.

    reduce任务是分配在不同节点上的,在写数据时每一个reduce任务都会讲数据写到磁盘上,只有所有reduce都写成功后这个任务才是真正执行成功,如何让所有的任务都执行成功来保证任务的一致性就是有committer决定的.


## RecordWriter

```java
public abstract class RecordWriter<K, V> {
  /** 
   * Writes a key/value pair.
   *
   * @param key the key to write.
   * @param value the value to write.
   * @throws IOException
   */      
  public abstract void write(K key, V value) throws IOException, InterruptedException;

  /** 
   * Close this <code>RecordWriter</code> to future operations.
   * 
   * @param context the context of the task
   * @throws IOException
   */ 
  public abstract void close(TaskAttemptContext context ) throws IOException, InterruptedException;
}
```

- write()将key/value值写到HDFS上,具体写什么,往哪儿写,程序可以自定义.

- close()方法关闭输出目的地的资源,比如说关闭文件流.


## 自定义OutputFormat-多路输出

自己实现一个根据key值将value输出到不同文件(目录)的OutputFormat.

## 实操

[源码地址]()