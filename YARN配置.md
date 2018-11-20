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

4. 同样的方法，配置bigdata1, bigdata2, bigdata3几台机器，注意这里没有配置bigdata4，可能bigdata4用于其他特殊用途把，之前的HDFS也没有配置bigdata4。





