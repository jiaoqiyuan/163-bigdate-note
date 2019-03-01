# MR参数调优

## 总体原则

    尽可能充分且合理利用集群资源,结合MapReduce运行原理,将数据分而治之.

## MR内存配置

- 根据集群或者所拥有队列的资源,设置默认的每个Task的内存大小,通常为总内存/总核数.需要注意的是:每个Map或者Reduce任务都是单线程任务,每次任务都会用掉一个CPU核数.

- mapreduce.(map)reduce.memory.mb设置YARN上一个Container的内存上限.

- mapreduce.(map)reduce.java.opts设置一个Container中的JVM的最大堆内存数.JVM堆大小一般设为Container大小的70%到80%左右.

- 当一个Map或Reduce使用的内存超过mapreduce.(map)reduce.memory.mb设置的内存上限时,NodeManager会kill掉这个Container.此时要调大上限值.

- 当java程序使用的堆内存超出mapreduce.(map)reduce.java.opts设置的值时,会抛出Out of Memory异常,此时调大这个值.

- mapreduce.(map)reduce.java.opts一定要小于mapreduce.(map)reduce.memory.mb.

## MR个数配置

- mapreduce.job.maps,对于输入为HDFS的文件来说,Map个数由InputFormat决定,设置这个参数不起作用.当输入数据不是来源于HDFS而是自定义的数据源时,这个参数是有可能会起作用的.

- mapreduce.job.reduces可以设置reduce的个数.适当调大reduce个数有助于作业的高效运行,因为reduce个数变多后,每个reduce处理的任务量就变小了.但是有些问题比如Top N问题只能设置一个Reduce,对于不需要Redece的任务可以设置为0.

- map和reduce的数量并非设置的越大越好,因为:
    
    - 每个map和reduce启动和初始化时都会消耗一定的时间.

    - 如果一个任务使用了大量的map和reduce,会占用集群上大量资源,导致其他任务运行变慢.

## Shuffle调优 - Map端参数配置

- mapreduce.task.io.sort.mb可以调整排序Map输出时所用的环形内存缓冲区的大小.调大可以减少溢写文件个数,但是会占用更多的Map任务内存.默认是100M

- mapreduce.map.sort.spill.percent,当map输出到环形内存缓冲区中的数据量占缓冲区大小的比例达到此值时,开始溢写.溢写过程中,缓冲区内剩下的20%空间任然接受数据的写入,当唤醒缓冲区被占满后,就会发生阻塞.默认使用80%

## Map输出压缩

- mapreduce.map.output.compress,设置map输出是否压缩.当map输出结果比较大时,IO成为性能瓶颈时,可以将此值设置为true,但是会消耗cpu.

- mapreduce.map.output.compress.codec,设置压缩类,一般为snappy或者lz4这种压缩解压都比较快的算法.

## Reduce参数配置

![Reduce参数配置][1]

- mapreduce.reduce.shuffle.input.buffer.percent,reduce端的内存缓冲区占JVM堆内存的比例

- mapreduce.reduce.shuffle.merge.percent,当内存缓冲区的数据占比达到此值时,开始溢写过程.

- maoreduce.task.io.sort.factor每一轮合并的文件数,适用于map和reduce.

- mapreduce.reduce.input.buffer.percent,已排序好准备给reduce函数使用的数据占用的内存比例.默认都存磁盘.

## Shuffle调优总结

- 将数据尽可能放在内存中,减少数据读写磁盘的次数.

- 程序编写过程中尽量不要在程序中存放大量的数据,而是尽量将内存空出来交给MapReduce框架.

- 在充分优化程序后,通过加大机器内存和更换高速SSD磁盘来加快程序运行速度.

[1]: https://github.com/jiaoqiyuan/163-bigdate-note/raw/master/%E6%97%A5%E5%BF%97%E8%A7%A3%E6%9E%90%E5%8F%8A%E8%AE%A1%E7%AE%97%EF%BC%9AMR/img/Reduce%E7%AB%AF%E5%8F%82%E6%95%B0%E9%85%8D%E7%BD%AE.png
