# 搭建本地测试环境

- 下载hadoop安装包，（通过Xftp下载）

- 解压后创建hadoop-current软连接(我是在我的～目录下操作的，我把hadoop解压到了/home/jony/apps目录中)
    ```
    ln -s /home/jony/apps/hadoop-2.7.6 hadoop-current
    ```

- 配置环境变量

    根据自己机器进行配置就好。

    ```
    # JAVA
    export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
    export PATH=$JAVA_HOME/bin:$PATH

    # hadoop
    export HADOOP_HOME=/home/jony/hadoop-current
    export PATH=$HADOOP_HOME/bin:$PATH
    ```

- 配置core-site.xml(根据自己需要配置默认路径，我这里配置到了本地的/home/jony/ideaprojects)

    ```
    <configuration>
        <property>
                <name>fs.defaultFS</name>
                <!--
                <value>hdfs://localhost:33271</value>
                -->
                <value>file://home/jony/ideaprojects</value>
        </property>
    </configuration>
    ```

- 配置hdfs路径（可能后面会用到，先配置了，也配置成本模式）

    ```
    <configuration>
        <property>
            <name>dfs.namenode.rpc-address</name>
            <value>localhost:33271</value>
        </property>
        <property>
            <name>dfs.namenode.http-address</name>
            <value>localhost:33272</value>
        </property>

        <property>
            <name>dfs.datanode.address</name>
            <value>localhost:33273</value>
        </property>
        <property>
            <name>dfs.datanode.http.address</name>
            <value>localhost:33274</value>
        </property>
        <property>
            <name>dfs.datanode.ipc.address</name>
            <value>localhost:33275</value>
        </property>
        <property>
            <name>dfs.namenode.name.dir</name>
            <value>/home/jony/tmp/name</value>
        </property>

        <property>
            <name>dfs.datanode.data.dir</name>
            <value>/home/jony/tmp/data/0,/home/jony/tmp/data/1,/home/jony/tmp/data/2</value>
        </property>
    </configuration>
    ```

    这里我的端口信息都没改，还是按网易分配的，懒得改了，不过把bigdata0都改成了本地localhost

- 配置yarn

    - 配置yarn-site.xml，注意配置成localhost，另外logs和local路径也配置成本地路径。
        
        ```
        <configuration>
            <property>
                <name>yarn.resourcemanager.webapp.address</name>
                <value>localhost:8088</value>
            </property>
            <property>
                <name>yarn.resourcemanager.hostname</name>
                <value>localhost</value>
            </property>

            <property>
                <name>yarn.nodemanager.log-dirs</name>
                <value>/home/jony/tmp/yarn/0/logs,/home/jony/tmp/yarn/1/logs,/home/jony/tmp/yarn/2/logs</value>
            </property>

            <property>
                <name>yarn.nodemanager.local-dirs</name>
                <value>/home/jony/tmp/yarn/0/local,/home/jony/tmp/yarn/1/local,/home/jony/tmp/yarn/2/local</value>
            </property>
            <property>
                <name>yarn.nodemanager.aux-services</name>
                <value>mapreduce_shuffle</value>
            </property>
        </configuration>        
        ```
    
    - 配置mapred-site.xml，把相关配置改为localhost就行了。

        ```
        <configuration>
            <property>
                <name>mapreduce.framework.name</name>
                <value>yarn</value>
            </property>
            <property>
                <name>mapreduce.jobhistory.address</name>
                <value>localhost:10020</value>
            </property>
            <property>
                <name>mapreduce.jobhistory.webapp.address</name>
                <value>localhost:19888</value>
            </property>
        </configuration>
        ```

- 初始化namenode

```
hdfs namenode -format
```

- 启动namenode和datanode验证一下配置是否成功

```
hdfs namenode
hdfs datanode
```

- 如果没有错误，关闭namenode和datanode

- 启动yarn
```
hadoop-current/sbin/yarn-daemon.sh start resourcemanager

hadoop-current/sbin/yarn-daemon.sh start nodemanager
```

- 运行老师的本地测试程序
    ```
    hadoop jar ./etl/target/etl-1.0-jar-with-dependencies.jar com.bigdata.etl.job.ParseLogJob ./input ./output
    ```
    注意上面这条命令要在你在core-site.xml中配置的默认目录下运行才能成功，主要是因为正确找到了input目录。
    
    当然你也可以在其他目录下运行，不过只要正确指定好jar包的目录和input、output的目录就行了。