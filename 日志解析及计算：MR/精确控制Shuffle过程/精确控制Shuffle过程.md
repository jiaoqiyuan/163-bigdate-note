# 精确控制Shuffle过程

## Shuffle过程

![MR原理][1]

- 首先回顾一下MR的Shuffle运行流程。

    - Map中用户将数据出处理完后放到环形缓冲区中

    - 环形缓冲区中数据占用空间达到一定比例后（一般是会80%），数据会发生溢写操作。

    - 数据溢写前会对数据先进性排序操作，再根据数据中的key值进行hash操作，这样保证了相同key值的数据能够划分到相同的partition中，同时也保证了不同的partition之间的数据量是大致相当的。

    - 在partition中对数据进行排序，然后将多个partition文件合并成一个大文件，大文件内部的partition也是有序的。

    - 然后大文件内的partition会拷贝到reduece端，进行合并操作，将key值放到一个迭代器中供用户进行处理。

- map端

    - 同一partition中的数据分到同一个Reduce（什么是相同的数据？ --> key相同的数据）。

    - sort对输出的数据进行排序（按照什么规则排序？ --> 按照key的大小或者字典顺序来排？，可以自定义）。

    - spill将缓冲区中的数据溢写到文件中。

    - merge将溢写文件排序合并成一个文件。

- Reduce端

    - copy将Map中属于自己的数据拉取到本地。

    - merge sort将数据进行排序合并（按照什么规则排序？ --> key的大小或者字典顺序来排）。

    - 相同key值对应的value放入同一个迭代器中（怎么定义相同key值？ --> hash算法计算出来的值相同就表示key相同，可以自定义）

## 总结：可以控制的流程

- 哪些数据可以分配到同一个reduce上。

- 数据排序的规则

- 哪些数据在reduce端会被放到同一个迭代器中。

## 控制Shuffle的API

- setPartitionerClass():设置控制Partition的类，默认对输出的Key值使用HashPartitioner。

- setSortCmparatorClass():设置控制数据排序的类，如果不设置，默认使用输出key值的比较方法排序。

- setGroupComparatorClass():设置Reduce端哪些key值对应的value放在一个迭代器中。

## 二次排序

例如有如下数据，先根据第一列数据进行排序，再根据第二列数据进行排序：

```
40 20               30 10
40 10               30 20
40 30               30 30
40 5                30 40
30 30               40 5
30 20               40 10
30 10   ------->    40 20
30 40               40 30
50 20               50 10
50 50               50 20
50 10               50 50
50 60               50 60
```

- 构造复合Writable类型，包含两个字段，作为Map的输出Key。

- 设置自定义Partitioner，对复合类型的第一个字段分区。

- 设置排序类，当复合类型的第一个字段相同时，比较第二个字段，否则使用第一个字段的比较结果。

- 设置分组类，按照第一个字段进行分组。

## 访问路径

- 用户访问行为以session为单位。

- 每一条访问日志都记录从这个session开始到当前页面的访问路径，存为一个list。

- 这里先对session进行排序，在对相同session的time_tag进行排序。

## 实操

[源码结构：]()
```
├── copyToBg0
├── etl.iml
├── pom.xml
└── src
    ├── main
    │   ├── com
    │   │   └── bigdata
    │   │       └── etl
    │   │           ├── job
    │   │           │   └── ParseLogJob.java
    │   │           ├── mr
    │   │           │   ├── LogBeanWritable.java
    │   │           │   ├── LogFieldWritable.java
    │   │           │   ├── LogGenericWritable.java
    │   │           │   ├── TextLongGroupComparator.java
    │   │           │   ├── TextLongPartition.java
    │   │           │   └── TextLongWritable.java
    │   │           └── utils
    │   │               └── IPUtil.java
    │   └── resources
    │       └── mr.xml
    └── test
        └── java
            └── com
                └── bigdata
                    └── etl
                        └── job
```





[1]: https://github.com/jiaoqiyuan/163-bigdate-note/raw/master/%E6%97%A5%E5%BF%97%E8%A7%A3%E6%9E%90%E5%8F%8A%E8%AE%A1%E7%AE%97%EF%BC%9AMR/img/MR%E8%BF%90%E8%A1%8C%E8%BF%87%E7%A8%8B.png