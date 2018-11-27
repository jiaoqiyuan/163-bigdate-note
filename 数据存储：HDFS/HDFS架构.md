# [HDFS架构][1]

![HDFS架构图][2]

- HDFS包括namenode和Datanode

    - namenode负责记录文件的命名空间(文件的名称,副本数,文件的位置等等),管理客户端的访问请求(读,创建,修改文件名等)

    - datanode一般有很多个,是实际存储数据的节点,namenode通过特定的RPC接口实现文件系统中的打开,关闭,重命名等操作,同时维护这不同文件名的数据块在不同datanode上的映射.

    - datanode可以响应客户端的请求,也可以接受来自namenode的数据块的创建,删除,复制等指令.

- namenode

    - 负责名称空间的管理

    - 负责文件到数据块的影射,以及数据块和存储节点对应关系

- datanode

    - 向管理节点汇报数据块信息

    - 存储节点之间通过复制操作来实现数据均衡和备份

    - 与客户端交互,执行数据读写请求

- 客户端Client

    - 向Datanode和Namenode发起读写请求.


- HDFS内含相关概念

    - Rack机架

        存储节点放置在不同的机架上,这与数据备份放置策略有关

    - Block模块

        数据切分成热定大小的数据块,分发到不同的存储节点.
        
        ![BlockReplication][3]

    - Replication副本

        数据块在不同存储节点之间,通过复制方式来拷贝多个副本进行存储.


[1]: http://hadoop.apache.org/docs/stable/hadoop-project-dist/hadoop-hdfs/HdfsDesign.html
[2]: http://hadoop.apache.org/docs/stable/hadoop-project-dist/hadoop-hdfs/images/hdfsarchitecture.png
[3]: http://hadoop.apache.org/docs/stable/hadoop-project-dist/hadoop-hdfs/images/hdfsdatanodes.png
