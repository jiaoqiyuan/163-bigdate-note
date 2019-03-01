# HDFS管理和使用

- ACL相关操作
    - 为目录添加访问权限
        ```
        hadoop fs -setfacl -m user:abc:r-x /user/hadoop
        ```
    
    - 为目录添加可继承权限
        ```
        hadoop fs -setfacl -m default:user:abc:r-x, default:group:xxx:r-x, default:other::r-x /user/hadoop
        ```
    
    - 删除目录权限
        ```
        hadoop fs -setfacl -b /user/hadoop
        ```

    - 删除特定权限，保留其他权限
        ```
        hadoop fs -setfacl -x user:abc:rwx /user/hadoop
        ```

- 数据均衡
    hadoop自带相关工具，用于解决HDFS各个节点中数据分布不均衡的问题（特别是新增服务器后），使得各个节点的存储使用率接近于总体的中位数水平。
    ```
    $HADOOP_HOME/sbin/start-balancer.sh
    ```

- 升级回滚
    - 准备滚动升级
        
        - 执行hdfs dfssamin -rollingUpgrade prepare来生成备份的fsimage
        - 执行hdfs dfssamin -rollingUpgrade query查看是否已经完成备份
        

    - 升级ANN（Active NameNode）和SNN（Standby NameNode）
        - 关闭 NN2（SNN），使用-rollingUpgrade选项重启NN2，此时NN2还是SNN
        - 手动触发切换，讲NN2（SNN）切换到ANN
        - 关闭NN1，并升级NN1的软件包；再通过-rollingUpgrade started启动NN1，这时NN1为SNN

        其实这段话的意思就是把SNN先升级然后作为ANN运行起来，然后关闭NN1，升级NN1后启动NN1作为ANN使用。
    
    - 升级DN（DataNode）
        - 选择个别DN节点，执行：
            ```
            hdfs dfsadmin -shutdownDatanode <DATANODE_HOST:IPC_PORT> upgrade
            ```
            关闭一个DN
        - 重启该DN
        - 重复上述步骤完成所有DN升级重启
        - 重启完看一下DN的日志里有没有重启标记，一般是版本号发生了变更，同时可以正常地汇报心跳到NN哪里。

    - 执行
        ```
        hdfs dfsadmin -rollingUpgrade finalize
        ```
        结束这次滚动升级。
        - 注意这个命令会删除NN和DN上面的备份，所以要确认所有升级完成后再执行这个命令。

    - 回滚（需要停止服务，但是滚动升级不需要停止服务）
        - 关闭所有NN和DN
        - 回复所有服务器上的版本到升级之前的状态
        - 通过-rollingUpgrade rollback启动ANN
        - 通过-bootstrapStandby正常启动SNN
        - 通过-rollback命令启动所有的DN
        - 回滚完成后同样需要去查看一下NN和DN的版本信息和服务是否正常启动起来。

- 其他管理命令
    - dfsadmin -safemode [enter|leave|get]
    - 添加DN节点或者排除DN节点是需要使用的 
        ```
        dfsadmin -refreshNodes
        ```
    - 重新配置DN参数的命令 
        ```
        dfsadmin -reconfig <datanode:port> <start|status>
        ```



    