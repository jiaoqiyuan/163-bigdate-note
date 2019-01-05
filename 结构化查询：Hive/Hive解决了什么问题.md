## 从MR到Hive

![Hadoop生态系统]()

Hadoop的核心是HDFS和MapReduce计算框架。HDFS是分布式文件系统，是Hadoop文件存储和管理的基础。MR是计算引擎，承担Hadoop下的计算任务。

Sqoop：是关系型数据库与Hadoop之间的数据传输工具。

FLume：是日志收集系统

Zookeeper：分布式协作服务

HBase：列存数据库

Mahout：用于数据挖掘

Pig：服务于流计算

Hive：数据仓库工具

**HDFS解决了文件分布式存储的问题。**

**MapReduce解决了数据处理分布式计算的问题**

**Hive解决了什么问题呢？---- 化繁为简**

Hive将开发人员从手写MapReduce程序的麻烦过程中解放出来，编写SQL程序即可完成MR任务。

## 化繁为简的Hive

![Hive](http://hive.apache.org/images/hive_logo_medium.jpg)

Hive所作的工作不是替代MapReduce，而是将SQL语句翻译成MapReduce任务执行，它提供了一个接口层，供用户使用SQL来调用MapReduce，这极大提高了数据分析人员和开发人员的工作效率。