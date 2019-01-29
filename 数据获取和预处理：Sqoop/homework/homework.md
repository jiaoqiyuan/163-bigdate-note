
### 1. 安装sqoop

配置本地sqoop参考 [sqoop服务安装][1] 。

### 2. 配置课程视频中给定的数据库连接，通过sqoop将数据导入到hive表中。

首选将hive安装目录下lib目录中的hive-common-1.1.0-cdh5.7.0.jar和hive-shims*拷贝到sqoop安装目录下的lib目录。然后执行：

```bash
./sqoop import --connect "jdbc:mysql://10.173.32.6:3306/sqoop?characterEncoding=UTF-8&useCursorFetch=true" --username root --password Gg/ru,.#5 --table product --split-by price --target-dir "/user/1015146591/sqooptest" --delete-target-dir --hive-import --hive-table sqoophomework –hive-overwrite
```

执行完成后查看hive中default. sqoophomework中的数据：

```
[1015146591@bigdata4 bin]$ hive
hive> select * from default.sqoophomework limit 10;
OK
28	1527235438751389	商品3
21	1527235438751248	商品4
25	1527235438751118	商品5
33	1527235438750902	商品6
25	1527235438750711	商品8
27	1527235438750331	商品11
11	1527235438750030	商品17
28	1527235438749094	商品24
11	1527235438748767	商品26
16	1527235438748507	商品28
Time taken: 1.755 seconds, Fetched: 10 row(s)
hive> 
```

### 3. 查询hive表product表中拥有多少个课程

执行查询语句：

```
hive> select count(product_id) from default.sqoophomework;
Query ID = 1015146591_20190129111455_3bc982ce-0295-4050-963f-69f081580f9d
Total jobs = 1
Launching Job 1 out of 1
Number of reduce tasks determined at compile time: 1
In order to change the average load for a reducer (in bytes):
  set hive.exec.reducers.bytes.per.reducer=<number>
In order to limit the maximum number of reducers:
  set hive.exec.reducers.max=<number>
In order to set a constant number of reducers:
  set mapreduce.job.reduces=<number>
Starting Job = job_1547603700477_1639, Tracking URL = http://bigdata0.novalocal:8088/proxy/application_1547603700477_1639/
Kill Command = /home/hadoop/hadoop-current/bin/hadoop job  -kill job_1547603700477_1639
Hadoop job information for Stage-1: number of mappers: 2; number of reducers: 1
2019-01-29 11:15:04,571 Stage-1 map = 0%,  reduce = 0%
2019-01-29 11:15:10,817 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 3.29 sec
2019-01-29 11:15:16,086 Stage-1 map = 100%,  reduce = 100%, Cumulative CPU 5.51 sec
MapReduce Total cumulative CPU time: 5 seconds 510 msec
Ended Job = job_1547603700477_1639
MapReduce Jobs Launched: 
Stage-Stage-1: Map: 2  Reduce: 1   Cumulative CPU: 5.51 sec   HDFS Read: 13903 HDFS Write: 4 SUCCESS
Total MapReduce CPU Time Spent: 5 seconds 510 msec
OK
100
Time taken: 21.809 seconds, Fetched: 1 row(s)
```

最终得到一共有100门课程。

[1]: https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%95%B0%E6%8D%AE%E8%8E%B7%E5%8F%96%E5%92%8C%E9%A2%84%E5%A4%84%E7%90%86%EF%BC%9ASqoop/sqoop%E6%9C%8D%E5%8A%A1%E5%AE%89%E8%A3%85.md