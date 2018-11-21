# YARN配置

    注意这里配置的YARN后期不会使用，只是学习一下怎么配置YARN，后期使用YARN时通用/home/hadoop目录下配置的YARN。

1. 配置/mnt/home/1015146591/hadoop-2.7.6/etc/hadoop/yarn-site.xml
```
<configuration>
    <property>
        <name>yarn.resourcemanager.webapp.address</name>
        <value>bigdata0.novalocal:8088</value>
    </property>
    <property>
        <name>yarn.resourcemanager.hostname</name>
        <value>bigdata0.novalocal</value>
    </property>

    <property>
        <name>yarn.nodemanager.log-dirs</name>
        <value>/mnt/home/1015146591/yarn/0/logs,/mnt/home/1015146591/yarn/1/logs,/mnt/home/1015146591/yarn/2/logs</value>
    </property>

    <property>
        <name>yarn.nodemanager.local-dirs</name>
        <value>/mnt/home/1015146591/yarn/0/local,/mnt/home/1015146591/yarn/1/local,/mnt/home/1015146591/yarn/2/local</value>
    </property>
    <property>
        <name>yarn.nodemanager.aux-services</name>
        <value>mapreduce_shuffle</value>
    </property>
</configuration>

```

2. 配置/mnt/home/1015146591/hadoop-2.7.6/etc/hadoop/mapred-site.xml(这个文件一开始不存在，自己手动创建就行)
```
<?xml version="1.0"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<configuration>
    <property>
        <name>mapreduce.framework.name</name>
        <value>yarn</value>
    </property>
    <property>
        <name>mapreduce.jobhistory.address</name>
        <value>bigdata0.novalocal:10020</value>
    </property>
    <property>
        <name>mapreduce.jobhistory.webapp.address</name>
        <value>bigdata0.novalocal:19888</value>
    </property>
</configuration>
```

3. 启动resourcemanager
```
yarn resourcemanager
```
后台启动resourcemanager的方法：
```
hadoop-current/sbin/yarn-daemon.sh start resourcemanager
```

4. 同样的方法，配置bigdata1, bigdata2, bigdata3几台机器，注意这里没有配置bigdata4，可能bigdata4用于其他特殊用途把，之前的HDFS也没有配置bigdata4。

5. 启动nodemanager
```
yarn nodemanager
```
后台启动nodemanager的方法：
```
hadoop-current/sbin/yarn-daemon.sh start nodemanager
```

6. 运行yarn示例（注意要把HDFSdatanode打开）
```
hadoop jar hadoop-current/share/hadoop/mapreduce/hadoop-mapreduce-examples-2.7.6.jar pi 5 10
```

7. 关于配置完yarn运行wordcount和pi示例时偶尔会出现shuffle不存在错误的说明（有矛盾，暂时不能这么解释）：

现象：按照老师讲得yarn配置完bigdat0123后，在bigdat0123上运行wordcount和pi示例，有时候又shuffle错误，有时候没有。

原因：老师在bigdat0上的/home/hadoop目录下的yarn-site.xml没有配置shuffle导致的。

解释：可以通过webUI和运行示例时的日志信息查找原因，从本地运行日志信息可以获取到当前这次运行的示例被分配的application号，

凡是出现shuffle错误的应用都不会运行在bigdat0的container中，凡是出现shuffle错误的应用肯定是分配到了bigdata0的container中运行了。也就是说resourcemanager只要不把作业分配到bigdata0的container中执行就不会有shuffle错误。

- 运行过程中没有出现shuffle错误， 该应用id是10709，![application][1]。

在bigdata123上分别查看它运行过的applicationid，![application][2]，![application][3]，![application][4]，![application][5]。

- 再看一个运行出现shuffle错误的情况，该应用id是 ![application][6].

在bigdata123上分别查看它运行过的applicationid，![application][7]，![application][8]，![application][9]，![application][10]。

