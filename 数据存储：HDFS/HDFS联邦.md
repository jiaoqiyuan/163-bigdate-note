# HDFS联邦（ [HDFS Federation][1]）

- 单个NN的HDFS结构图如下：

![单个Namenode][2]

- HDFS联邦出现的原因:单个HDFS存在很多不足：

    - 扩展性
    
    单个HDFS无法水平扩展,当Namenode元数据膨胀时无法解决数据存储问题.

    - 隔离性

    所有元数据都存放到一个空间内对于有特殊安全需求的业务来说无法接受.

    - 性能

    一个Namenode无法提供很好的负载均衡,对于有大量请求的Namenode来说,无法很好地满足业务需要,多个Namenode可以提供很好的负载均衡,降低单个Namenode的负载压力.



- HDFS联邦

    - 多个NN组成的HDFS联邦结构如下：

    ![HDFS联邦][3]

    - 特点
    
        - 分布式命名空间

        - 通用的底层存储

    - HDFS联邦允许NN水平扩展，解决了元数据的膨胀问题，底层使用block pool来统一存储系统，不同的NN对应不同的pool。

    - HDFS联邦参数说明

        ![HDFS federation][4]

    - 组成HDFS联邦的必要步骤：

        1. 确认需要加入到HDFS的clusterID，clusterID是一个字符串，这里的clusterID在namenode格式化前可以指定也可以在格式化时随机生成.

        2. 查看clusterID
            ```
            cat ${dfs.namenode.dir}/current/VERSION
            ```
        3. 使用该clusterID对新加入的NameNode进行格式化
            ```
            hdfs namenode -format -cluster cid
            ```

HDFS联邦在hadoop官网的HDFS中有详细介绍，[HDFS federation][1].


[1]: http://hadoop.apache.org/docs/stable/hadoop-project-dist/hadoop-hdfs/Federation.html
[2]: https://github.com/jiaoqiyuan/163-bigdate-note/raw/master/%E6%95%B0%E6%8D%AE%E5%AD%98%E5%82%A8%EF%BC%9AHDFS/img/HDFS%E5%8D%95%E8%8A%82%E7%82%B9%E6%9E%B6%E6%9E%84.gif
[3]: https://github.com/jiaoqiyuan/163-bigdate-note/raw/master/%E6%95%B0%E6%8D%AE%E5%AD%98%E5%82%A8%EF%BC%9AHDFS/img/HDFS%E8%81%94%E9%82%A6%E6%9E%B6%E6%9E%84.gif
[4]: https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%95%B0%E6%8D%AE%E5%AD%98%E5%82%A8%EF%BC%9AHDFS/img/HDFS%E8%81%94%E9%82%A6%E5%8F%82%E6%95%B0%E8%AF%B4%E6%98%8E.png