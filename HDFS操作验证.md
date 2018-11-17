# HDFS操作验证

1. 查看HDFS根目录文件
```
hadoop fs -ls /
```

2. 创建文件夹
```
hadoop fs -mkdir /tmp
```

3. 创建个人专属文件夹，这里把1015146591换成你们自己的id就行了，其实我觉得文件名无所谓，只要你自己知道把文件放到了那里就行了，取自己的ID作为文件名可能是为了方便管理吧
```
hadoop fs -mkdir -p /user/1015146591
```

4. 创建用于存放上传的测试文件的文件夹
```
hadoop fs -mkdir /user/1015146591/input
```

5. 使用put命令上传本地文件到hdfs上：
```
cd hadoop-current/
hadoop fs -put *.txt /user/1015146591/input
hadoop fs -ls /user/1015146591/input

```

6. 查看hdfs上文件内容
```
hadoop fs -text /user/1015146591/input/LICENSE.txt
```

7. 从hdfs上下载文件到本地
```
hadoop fs -get /user/1015146591/input/LICENSE.txt test

对比一下两个文件的md5，看二者是不是同一个文件：
md5sum test LICENSE.txt
```

8. 将hdfs服务作为daemon程序运行（就是守护进程，可以理解为后台进程）
- 关闭datanode

- 以daemon形式启动datanode，同样地，把id换成你们自己的目录
```
/mnt/home/1015146591/hadoop-current/sbin/hadoop-daemon.sh start datanode
```

9. 使用tail命令可以实时查看日志输出，注意日志路径以你们自己的为准：
```
tail -f /mnt/home/1015146591/hadoop-2.7.6/logs/hadoop-1015146591-datanode-bigdata0.novalocal.out
```

- 如果你想偷点懒，可以用tailf命令：
```
tailf /mnt/home/1015146591/hadoop-2.7.6/logs/hadoop-1015146591-datanode-bigdata0.novalocal.out
```

10. 停止datanode或者namenode进程的方法，很简单，就是把开启的命令中的start换成stop就行了：
```
/mnt/home/1015146591/hadoop-current/sbin/hadoop-daemon.sh stop datanode
```


