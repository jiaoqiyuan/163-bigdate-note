# 网易云课堂大数据笔记


## 数据平台综述

1. [数据管理技术的演化](https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%95%B0%E6%8D%AE%E5%B9%B3%E5%8F%B0%E7%BB%BC%E8%BF%B0/%E6%95%B0%E6%8D%AE%E7%AE%A1%E7%90%86%E6%8A%80%E6%9C%AF%E7%9A%84%E6%BC%94%E5%8C%96.md)

2. [学习的方法论](https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%95%B0%E6%8D%AE%E5%B9%B3%E5%8F%B0%E7%BB%BC%E8%BF%B0/%E5%AD%A6%E4%B9%A0%E7%9A%84%E6%96%B9%E6%B3%95%E8%AE%BA.md)

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
