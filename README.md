# 网易云课堂大数据笔记

![](https://img.shields.io/badge/language-java-orange.svg)

## 数据平台综述

1. [数据管理技术的演化](https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%95%B0%E6%8D%AE%E5%B9%B3%E5%8F%B0%E7%BB%BC%E8%BF%B0/%E6%95%B0%E6%8D%AE%E7%AE%A1%E7%90%86%E6%8A%80%E6%9C%AF%E7%9A%84%E6%BC%94%E5%8C%96.md)

2. [学习的方法论](https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%95%B0%E6%8D%AE%E5%B9%B3%E5%8F%B0%E7%BB%BC%E8%BF%B0/%E5%AD%A6%E4%B9%A0%E7%9A%84%E6%96%B9%E6%B3%95%E8%AE%BA.md)

3. [分布式系统可扩展性](https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%95%B0%E6%8D%AE%E5%B9%B3%E5%8F%B0%E7%BB%BC%E8%BF%B0/%E5%88%86%E5%B8%83%E5%BC%8F%E7%B3%BB%E7%BB%9F%E5%8F%AF%E6%89%A9%E5%B1%95%E6%80%A7.md)

----

## 数据存储：HDFS

1. [HDFS架构][10]

2. [HDFS环境搭建][1]

3. [HDFS操作验证][2]

4. [YARN配置][3]

5. [HDFS管理与使用][4]

6. [HDFS高可用性][5]

7. [HDFS联邦][6]

8. [HDFS安全][7]

9. [压缩与分片][8]

10. [异常处理][9]

----

## 日志解析及计算：MR

1. [MR的应用场景][11]

2. [MR的原理和运行流程][12]

3. [编写一个MR程序][13]

4. [Hadoop的IO模型-上][14]

5. [Hadoop的IO模型-下][15]

6. [完整编写Map和Reduce][16]

7. [灵活配置Configuration][17]

8. [精准控制Shuffle过程][18]

9. [MR程序的输入][19]

10. [MR程序的输出][20]

11. [简单好用的计数器][21]

12. [MR实现关联操作][22]

13. [MR参数调优][23]

14. [数据倾斜][24]


## 数据获取和预处理：Flume

1. [日志及日志收集系统介绍][25]

2. [Flume Agent组成][26]

3. [Flume支持的组件类型][27]

4. [Flume基本配置][28]

5. [Flume部署][29]

6. [Flume配置示例][30]

7. [Flume高级配置][31]

8. [构建复杂日志收集系统][32]

## 结构化查询：Hive

1. [Hive学习资料](https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E7%BB%93%E6%9E%84%E5%8C%96%E6%9F%A5%E8%AF%A2%EF%BC%9AHive/Hive%E5%AD%A6%E4%B9%A0%E8%B5%84%E6%96%99.md)

2. [Hive解决了什么问题](https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E7%BB%93%E6%9E%84%E5%8C%96%E6%9F%A5%E8%AF%A2%EF%BC%9AHive/Hive%E8%A7%A3%E5%86%B3%E4%BA%86%E4%BB%80%E4%B9%88%E9%97%AE%E9%A2%98.md)


[1]: https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%95%B0%E6%8D%AE%E5%AD%98%E5%82%A8%EF%BC%9AHDFS/HDFS%E7%8E%AF%E5%A2%83%E6%90%AD%E5%BB%BA.md
[2]: https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%95%B0%E6%8D%AE%E5%AD%98%E5%82%A8%EF%BC%9AHDFS/HDFS%E6%93%8D%E4%BD%9C%E9%AA%8C%E8%AF%81.md
[3]: https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%95%B0%E6%8D%AE%E5%AD%98%E5%82%A8%EF%BC%9AHDFS/YARN%E9%85%8D%E7%BD%AE.md
[4]: https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%95%B0%E6%8D%AE%E5%AD%98%E5%82%A8%EF%BC%9AHDFS/HDFS%E7%AE%A1%E7%90%86%E5%92%8C%E4%BD%BF%E7%94%A8.md
[5]: https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%95%B0%E6%8D%AE%E5%AD%98%E5%82%A8%EF%BC%9AHDFS/HDFS%E9%AB%98%E5%8F%AF%E7%94%A8%E6%80%A7.md
[6]: https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%95%B0%E6%8D%AE%E5%AD%98%E5%82%A8%EF%BC%9AHDFS/HDFS%E8%81%94%E9%82%A6.md
[7]: https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%95%B0%E6%8D%AE%E5%AD%98%E5%82%A8%EF%BC%9AHDFS/HDFS%E5%AE%89%E5%85%A8.md
[8]: https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%95%B0%E6%8D%AE%E5%AD%98%E5%82%A8%EF%BC%9AHDFS/%E5%8E%8B%E7%BC%A9%E4%B8%8E%E5%88%86%E7%89%87.md
[9]: https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%95%B0%E6%8D%AE%E5%AD%98%E5%82%A8%EF%BC%9AHDFS/%E5%BC%82%E5%B8%B8%E5%A4%84%E7%90%86.md
[10]: https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%95%B0%E6%8D%AE%E5%AD%98%E5%82%A8%EF%BC%9AHDFS/HDFS%E6%9E%B6%E6%9E%84.md
[11]: https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%97%A5%E5%BF%97%E8%A7%A3%E6%9E%90%E5%8F%8A%E8%AE%A1%E7%AE%97%EF%BC%9AMR/MR%E7%9A%84%E5%BA%94%E7%94%A8%E5%9C%BA%E6%99%AF.md
[12]: https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%97%A5%E5%BF%97%E8%A7%A3%E6%9E%90%E5%8F%8A%E8%AE%A1%E7%AE%97%EF%BC%9AMR/MR%E7%9A%84%E5%8E%9F%E7%90%86%E5%92%8C%E8%BF%90%E8%A1%8C%E6%B5%81%E7%A8%8B.md
[13]: https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%97%A5%E5%BF%97%E8%A7%A3%E6%9E%90%E5%8F%8A%E8%AE%A1%E7%AE%97%EF%BC%9AMR/%E7%BC%96%E5%86%99%E4%B8%80%E4%B8%AAMR%E7%A8%8B%E5%BA%8F/%E7%BC%96%E5%86%99%E4%B8%80%E4%B8%AAMR%E7%A8%8B%E5%BA%8F.md
[14]: https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%97%A5%E5%BF%97%E8%A7%A3%E6%9E%90%E5%8F%8A%E8%AE%A1%E7%AE%97%EF%BC%9AMR/Hadoop%E7%9A%84IO%E6%A8%A1%E5%9E%8B-%E4%B8%8A/Hadoop%E7%9A%84IO%E6%A8%A1-%E4%B8%8A.md
[15]: https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%97%A5%E5%BF%97%E8%A7%A3%E6%9E%90%E5%8F%8A%E8%AE%A1%E7%AE%97%EF%BC%9AMR/Hadoop%E7%9A%84IO%E6%A8%A1%E5%9E%8B-%E4%B8%8B/Hadoop%E7%9A%84IO%E6%A8%A1%E5%9E%8B-%E4%B8%8B.md
[16]: https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%97%A5%E5%BF%97%E8%A7%A3%E6%9E%90%E5%8F%8A%E8%AE%A1%E7%AE%97%EF%BC%9AMR/%E5%AE%8C%E6%95%B4%E7%BC%96%E5%86%99Map%E5%92%8CReduce/%E5%AE%8C%E6%95%B4%E7%BC%96%E5%86%99Map%E5%92%8CReduce.md
[17]: https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%97%A5%E5%BF%97%E8%A7%A3%E6%9E%90%E5%8F%8A%E8%AE%A1%E7%AE%97%EF%BC%9AMR/%E7%81%B5%E6%B4%BB%E5%BA%94%E7%94%A8Configuration/%E7%81%B5%E6%B4%BB%E5%BA%94%E7%94%A8Configuration.md
[18]: https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%97%A5%E5%BF%97%E8%A7%A3%E6%9E%90%E5%8F%8A%E8%AE%A1%E7%AE%97%EF%BC%9AMR/%E7%B2%BE%E7%A1%AE%E6%8E%A7%E5%88%B6Shuffle%E8%BF%87%E7%A8%8B/%E7%B2%BE%E7%A1%AE%E6%8E%A7%E5%88%B6Shuffle%E8%BF%87%E7%A8%8B.md
[19]: https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%97%A5%E5%BF%97%E8%A7%A3%E6%9E%90%E5%8F%8A%E8%AE%A1%E7%AE%97%EF%BC%9AMR/MR%E7%A8%8B%E5%BA%8F%E7%9A%84%E8%BE%93%E5%85%A5/MR%E7%A8%8B%E5%BA%8F%E7%9A%84%E8%BE%93%E5%85%A5.md
[20]: https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%97%A5%E5%BF%97%E8%A7%A3%E6%9E%90%E5%8F%8A%E8%AE%A1%E7%AE%97%EF%BC%9AMR/MR%E7%A8%8B%E5%BA%8F%E7%9A%84%E8%BE%93%E5%87%BA/MR%E7%A8%8B%E5%BA%8F%E7%9A%84%E8%BE%93%E5%87%BA.md
[21]: https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%97%A5%E5%BF%97%E8%A7%A3%E6%9E%90%E5%8F%8A%E8%AE%A1%E7%AE%97%EF%BC%9AMR/%E7%AE%80%E5%8D%95%E5%A5%BD%E7%94%A8%E7%9A%84%E8%AE%A1%E6%95%B0%E5%99%A8/%E7%AE%80%E5%8D%95%E5%A5%BD%E7%94%A8%E7%9A%84%E8%AE%A1%E6%95%B0%E5%99%A8.md
[22]: https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%97%A5%E5%BF%97%E8%A7%A3%E6%9E%90%E5%8F%8A%E8%AE%A1%E7%AE%97%EF%BC%9AMR/MR%E5%AE%9E%E7%8E%B0%E5%85%B3%E8%81%94%E6%93%8D%E4%BD%9C.md
[23]: https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%97%A5%E5%BF%97%E8%A7%A3%E6%9E%90%E5%8F%8A%E8%AE%A1%E7%AE%97%EF%BC%9AMR/MR%E5%8F%82%E6%95%B0%E8%B0%83%E4%BC%98.md
[24]: https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%97%A5%E5%BF%97%E8%A7%A3%E6%9E%90%E5%8F%8A%E8%AE%A1%E7%AE%97%EF%BC%9AMR/%E6%95%B0%E6%8D%AE%E5%80%BE%E6%96%9C.md
[25]: https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%95%B0%E6%8D%AE%E8%8E%B7%E5%8F%96%E5%92%8C%E9%A2%84%E5%A4%84%E7%90%86%EF%BC%9AFlume/%E6%97%A5%E5%BF%97%E5%8F%8A%E6%97%A5%E5%BF%97%E6%94%B6%E9%9B%86%E7%B3%BB%E7%BB%9F.md
[26]: https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%95%B0%E6%8D%AE%E8%8E%B7%E5%8F%96%E5%92%8C%E9%A2%84%E5%A4%84%E7%90%86%EF%BC%9AFlume/Flume%20Agent%E7%9A%84%E7%BB%84%E6%88%90.md
[27]: https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%95%B0%E6%8D%AE%E8%8E%B7%E5%8F%96%E5%92%8C%E9%A2%84%E5%A4%84%E7%90%86%EF%BC%9AFlume/Flume%E6%94%AF%E6%8C%81%E7%9A%84%E7%BB%84%E5%BB%BA%E7%B1%BB%E5%9E%8B.md
[28]: https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%95%B0%E6%8D%AE%E8%8E%B7%E5%8F%96%E5%92%8C%E9%A2%84%E5%A4%84%E7%90%86%EF%BC%9AFlume/Flume%E7%9A%84%E5%9F%BA%E6%9C%AC%E9%85%8D%E7%BD%AE.md
[29]: https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%95%B0%E6%8D%AE%E8%8E%B7%E5%8F%96%E5%92%8C%E9%A2%84%E5%A4%84%E7%90%86%EF%BC%9AFlume/Flume%E5%AE%89%E8%A3%85%E5%92%8C%E9%83%A8%E7%BD%B2.md
[30]: https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%95%B0%E6%8D%AE%E8%8E%B7%E5%8F%96%E5%92%8C%E9%A2%84%E5%A4%84%E7%90%86%EF%BC%9AFlume/Flume%E9%85%8D%E7%BD%AE%E7%A4%BA%E4%BE%8B.md 
[31]: https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%95%B0%E6%8D%AE%E8%8E%B7%E5%8F%96%E5%92%8C%E9%A2%84%E5%A4%84%E7%90%86%EF%BC%9AFlume/Flume%E9%AB%98%E7%BA%A7%E9%85%8D%E7%BD%AE.md
[32]: https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%95%B0%E6%8D%AE%E8%8E%B7%E5%8F%96%E5%92%8C%E9%A2%84%E5%A4%84%E7%90%86%EF%BC%9AFlume/%E6%9E%84%E5%BB%BA%E5%A4%8D%E6%9D%82%E6%97%A5%E5%BF%97%E6%94%B6%E9%9B%86%E7%B3%BB%E7%BB%9F.md