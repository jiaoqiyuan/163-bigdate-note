## Sqoop介绍

为解决传统数据仓库的痛点，比如系统闭源、成本昂贵、扩容复杂、性能有明显瓶颈，Apache社区开源了Sqoop这个数据创数框架。

- Sqoop由Cloudera开发并贡献给Apache社区。

- Sqoop是Apache顶级项目。

- 结构化数据与大数据系统间的数据同步。

- 有两个版本：1.4.x和1.99.x，其中1.99.x是基于sqoop2的框架，但功能还没完全覆盖1.4.x的sqoop，所以建议还是先使用1.4.x。

## Sqoop功能

- 将关系型数据库数据导入HDFS，解决HDFS的数据来源问题，使数据能够方便地进入HDFS。

- 支持HDFS数据导出到关系型数据库，Hadoop计算出的数据可以方便地写回到数据库中。

- 支持关系型数据库直接将数据导入到Hive。

- Sqoop是批处理类型的任务，不是常驻服务，不需要像WEB服务一样常驻运行，在需要的时候提交任务就可以完成数据的导入导出。

- Sqoop是使用命令行进行任务的提交，提供类似于Shell的脚本的Sqopp命令，提交方式很简便。

- Sqoop支持各种存储类型，包括行存、列存以及各种数压缩算法。

## Sqoop架构

如果自己设计一个数据库数据导入导出工具的话，会怎么实现呢？首先比较简单而且也容易想到的方式是使用JDBC，将数据从数据库中拉取出来然后写入HDFS，这种方法简单易行，但是缺点也很明显，数据量较大时，效率不高。

Sqoop是怎么做的呢，它其实是依赖MapReduce的计算框架，将数据导入并行化，采用分而治之的思想，每个Map只处理一部分数据，然后由Reduce将Map的中间结果聚合起来。

![Sqoop框架][1]

其实并不需要Reduce，只是用Map就可以完成数据的并行导入导出工作了，每个Map使用JDBC将数据从数据库抽取出来，写入到HDFS，就可以完成数据的导入任务。

由于使用了MapReduce并发计算的特性，Sqoop可以显著提高数据导入导出的效率。在实际使用中，Sqoop一般不会称为性能的瓶颈，在磁盘读写和宽带都不是瓶颈的前提下，数据的导入导出效率往往取决于DB的性能。

上面的框架中Sqoop和数据库之间使用的是JDBC，所以逻辑上讲，所有支持JDBC操作的数据库都支持使用Sqoop将数据导入到HDFS中，当然各个数据库之间会存在差异，目前在不改造Sqoop的前提下，Sqoop支持的数据库有：MySQL，Oracle，SqlServer， postgreSQL，DB2等，基本涵盖了所有主流的数据库。

## Sqoop任务的执行流程

Sqoop执行流程如下图所示：

![Sqoop执行流程][2]

### 数据对象生成

在实际开发过程中，在使用JDBC访问数据库里数据的时候，通常会使用一个Java Bean存储数据库数据，JavaBean中每个成员变量对应数据库中的一个Column（列），并且JavaBean中会有大量的get、set函数来获取和修改数据，程序运行过程中，JavaBean可以用来保留数据，这样，对数据的操作就转换成了对JavaBean对象的操作。

Sqoop也使用类似的机制，在导入导出前会现根据数据库的数据结构，生成对应的JavaBean对象，加入有一个名为Order的数据表，存放的是订单信息，其中id是主键，在导入过程中就会生成一个Order的JavaBean，JavaBean里的属性分别对应于数据库中的几个Column（列）。

![数据对象生成][3]

生成JavaBean的一个关键问题是怎样做类型映射，也就是把数据库中的列数据类型和JavaBean中的成员变量的类型映射起来，Sqoop中定义的映射关系如下：

| SQL TYPE | JAVA TYPE |
|:--------:|:---------:|
| INTEGER | java.lang.Integer |
| VARCHAR | java.lang.String |
| LONGVARCHAR | java.lang.String |
| NUMERIC | java.math.BigDecimal |
| DECIMAL | java.math.BigDecimal |
| BOOLEAN | java.lang.Boolean |
| DATE | java.sql.Date |
| ... | ... |

### 数据导入到Hive中

![Hive导入][4]

如果命令指定要新建HIVE Table，Sqoop会先生成HIVE table的定义语句，Hive table的column和数据库的column建立一一映射的关系，然后Sqoop会生成一个rawData语句，rawdata语句会在Hive MateStore中注册元数据，并进行数迁移。

上面的Hive的建表语句和rawdata语句都会被写入到一个script脚本中，最后会sqoop启动.hive命令执行刚刚生成的script脚本，提交Hive任务，完成Hive导出。


[1]: https://github.com/jiaoqiyuan/163-bigdate-note/raw/master/%E6%95%B0%E6%8D%AE%E8%8E%B7%E5%8F%96%E5%92%8C%E9%A2%84%E5%A4%84%E7%90%86%EF%BC%9ASqoop/img/Sqoop%E6%A1%86%E6%9E%B6.png
[2]: https://github.com/jiaoqiyuan/163-bigdate-note/raw/master/%E6%95%B0%E6%8D%AE%E8%8E%B7%E5%8F%96%E5%92%8C%E9%A2%84%E5%A4%84%E7%90%86%EF%BC%9ASqoop/img/Sqoop%E4%BB%BB%E5%8A%A1%E6%89%A7%E8%A1%8C%E6%B5%81%E7%A8%8B.png
[3]: https://github.com/jiaoqiyuan/163-bigdate-note/raw/master/%E6%95%B0%E6%8D%AE%E8%8E%B7%E5%8F%96%E5%92%8C%E9%A2%84%E5%A4%84%E7%90%86%EF%BC%9ASqoop/img/%E6%95%B0%E6%8D%AE%E5%AF%B9%E8%B1%A1%E7%94%9F%E6%88%90.png
[4]: https://github.com/jiaoqiyuan/163-bigdate-note/raw/master/%E6%95%B0%E6%8D%AE%E8%8E%B7%E5%8F%96%E5%92%8C%E9%A2%84%E5%A4%84%E7%90%86%EF%BC%9ASqoop/img/Hive%E5%AF%BC%E5%85%A5.png