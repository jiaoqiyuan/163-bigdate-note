# HDFS高可用

- Checkpoint流程
    
    ![checkpoint][1]

    - HDFS的namenode包含Primary Namenode和Secondary Namenode
    
    - Primary Namenode和Secondary Namenode都包含fsimage文件和edits文件.

    - fsimage文件记录文件系统元数据的永久性检查点,包含文件系统中的所有目录和文件id的序列化信息.
    
    - edits文件记录文件系统的写操作.
   
    - 实际生产环境中,Primary Namenode会持续生成edits log文件,里面记录的事物条数达到一定的阈值后会触发归档流程,edits log文件会生成多个edits文件.
    
    - Secondary Namenode根据设置好的checkpoint阈值(应该就是一个条件吧)定期从Primary Namenode上下载fsimage文件和归档好的edits log文件进行merge(合并)操作,生成新的fsimage文件,并将这个fsimage文件上传到Primary Namenode,这样就能确保Primary Namenode始终持有最新的fsimage文件.

    - 这样的流程有个问题:Secondary Namenode不能持有Primary Namenode上未归档的edits事务,如果出现PNN宕机,SNN是无法恢复未归档的edits事务的,这就会造成数据丢失.

    - PNN重新启动加载fsimage的等待过程很长,会导致整个服务处于不可用的状态.


- zookeeper

    ![zookeeper][2]
    - 引入zookeeper可以解决上述可用性差的问题.

    - zookeeper中也存在两个namenode,只不过除了主namenode,另一个叫Standby Namenode,会时刻进行数据的同步,保证与主Namenode数据一致.

    - 只有active Namenode可以将自己的edits log写入到journalNod组成的QJM中,Standby Namenode定期从QJM中读取.

- ZKFC 使用Zookeeper来选主,监控NN,触发NN状态切换

- QJM:由多个JournalNode组成的Quorum集群,实现分布式日志的高可用性

- ANN(Active Namenode)将元数据editlog发布到QJM集群中,SNN(Standby Namenode)从QJM集群上读取集群的EditLog,应用自己的元数据.

- 高可用参数配置

    ![参数配置][3]


[1]: https://github.com/jiaoqiyuan/163-bigdate-note/raw/master/%E6%95%B0%E6%8D%AE%E5%AD%98%E5%82%A8%EF%BC%9AHDFS/img/HDFS%E9%AB%98%E5%8F%AF%E7%94%A8.png
[2]: https://github.com/jiaoqiyuan/163-bigdate-note/raw/master/%E6%95%B0%E6%8D%AE%E5%AD%98%E5%82%A8%EF%BC%9AHDFS/img/zookeeper%E6%9E%B6%E6%9E%84.png
[3]: https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%95%B0%E6%8D%AE%E5%AD%98%E5%82%A8%EF%BC%9AHDFS/img/%E9%AB%98%E5%8F%AF%E7%94%A8%E5%8F%82%E6%95%B0%E9%85%8D%E7%BD%AE.png