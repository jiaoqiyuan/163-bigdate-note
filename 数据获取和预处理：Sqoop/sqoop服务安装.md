## 验证Java和Hadoop已经安装

1. 终端输入java命令查看返回值：

```bash
1015146591@bigdata4.novalocal:/mnt/home/1015146591 $ java -version
openjdk version "1.8.0_171"
OpenJDK Runtime Environment (build 1.8.0_171-b10)
OpenJDK 64-Bit Server VM (build 25.171-b10, mixed mode)
1015146591@bigdata4.novalocal:/mnt/home/1015146591 $ 
```

2. 启动Hadoop自带MapReduce示例程序：

```bash
hadoop jar share/hadoop/mapreduce/hadoop-mapreduce-examples-2.7.6.jar pi 2 2
```

OK，都可以运行就好。

## 下载Sqoop安装包

1. 下载地址： [sqoop-1.4.7.bin__hadoop-2.6.0.tar.gz][1]。

2. 解压到本地目录，我在自己的home目录下创建了一个apps文件，用于存放需要安装的软件：

```
tar xzvf sqoop-1.4.7.bin__hadoop-2.6.0.tar.gz -C ~/apps
```

## 配置Sqoop

1. 进入sqoop的配置目录conf，添加sqoop-env.sh，这里直接拷贝模板文件即可:

    ```bash
    cp sqoop-env-template.sh sqoop-env.sh
    ```

2. 修改sqoop-env.sh文件，由于之前已经配置过HADOOP_HOME环境变量，而且加入到了系统环境变量中，所以可以直接使用HADOOP_HOME这个环境变量。

    ```conf
    #Set path to where bin/hadoop is available
    export HADOOP_COMMON_HOME=${HADOOP_HOME}

    #Set path to where hadoop-*-core.jar is available
    export HADOOP_MAPRED_HOME=${HADOOP_HOME}
    export HADOOP_HDFS_HOME=${HADOOP_HOME}
    export HIVE_HOME=/mnt/home/1015146591/apps/hive-1.2.2
    export PATH=$HIVE_HOME/bin:$PATH
    ```

    老师的配置方法是：

    ```conf
    export HADOOP_PREFIX="/home/hadoop/hadoop-current"
    export HADOOP_HOME=${HADOOP_PREFIX}
    export PATH=$PATH:$HADOOP_PREFIX/bin:$HADOOP_PREFIX/sbin
    export HADOOP_COMMON_HOME=${HADOOP_HOME}
    export HADOOP_HDFS_HOME=${HADOOP_HOME}
    export HADOOP_MAPRED_HOME=${HADOOP_HOME}
    ```

3. 连接数据库：

    ```
    mysql -u root -pGg/ru,.#5 -h 10.173.32.6 -P3306 -Dsqoop
    mysql> 

    ```
    查看数据库表：

    ```
    mysql> select * from sqoop_test;
    +------+-------+
    | id   | name  |
    +------+-------+
    |    1 | allen |
    |    2 | bob   |
    +------+-------+
    2 rows in set (0.00 sec)

    ```

## 使用Sqoop导入数据到HDFS

1. 下载MySQL的JDBC驱动: [mysql-connector-java-5.1.47.tar.gz][2]，我在官网只找到了5.1.47的版本，不知道能不能使用，也可以直接从老师的sqoop目录下拷贝：

```
cp /home/hadoop/sqoop-1.4.7.bin__hadoop-2.6.0/lib/mysql-connector-java-5.1.46.jar .
```

2. 执行sqoop命令验证安装是否成功：

    ```
    ./sqoop import --connect "jdbc:mysql://10.173.32.6:3306/sqoop?characterEncoding=UTF-8&useCursorFetch=true" --username root --password Gg/ru,.#5 --table sqoop_test --delete-target-dir --target-dir /user/1015146591/sqoop_test --split-by id
    ```

3. 在HDFS上查看导入的数据：

    ```bash
    1015146591@bigdata4.novalocal:/mnt/home/1015146591/apps/sqoop-1.4.7.bin__hadoop-2.6.0/bin $ hadoop fs -ls /user/1015146591/sqoop_test
    Found 3 items
    -rw-r-----   3 1015146591 supergroup          0 2019-01-21 15:57 /user/1015146591/sqoop_test/_SUCCESS
    -rw-r-----   3 1015146591 supergroup          8 2019-01-21 15:57 /user/1015146591/sqoop_test/part-m-00000
    -rw-r-----   3 1015146591 supergroup          6 2019-01-21 15:57 /user/1015146591/sqoop_test/part-m-00001
    ```

    文件内的具体内容如下：

    ```
    1015146591@bigdata4.novalocal:/mnt/home/1015146591/apps/sqoop-1.4.7.bin__hadoop-2.6.0/bin $ hadoop fs -text /user/1015146591/sqoop_test/part-m-00000
    19/01/21 15:59:18 INFO lzo.GPLNativeCodeLoader: Loaded native gpl library from the embedded binaries
    19/01/21 15:59:18 INFO lzo.LzoCodec: Successfully loaded & initialized native-lzo library [hadoop-lzo rev 5e360bdefd8923280b1a0234b845448050bf0caa]
    1,allen
    1015146591@bigdata4.novalocal:/mnt/home/1015146591/apps/sqoop-1.4.7.bin__hadoop-2.6.0/bin $ hadoop fs -text /user/1015146591/sqoop_test/part-m-00001
    19/01/21 15:59:26 INFO lzo.GPLNativeCodeLoader: Loaded native gpl library from the embedded binaries
    19/01/21 15:59:26 INFO lzo.LzoCodec: Successfully loaded & initialized native-lzo library [hadoop-lzo rev 5e360bdefd8923280b1a0234b845448050bf0caa]
    2,bob
    1015146591@bigdata4.novalocal:/mnt/home/1015146591/apps/sqoop-1.4.7.bin__hadoop-2.6.0/bin $ 
    ```



[1]: https://mirrors.tuna.tsinghua.edu.cn/apache/sqoop/1.4.7/sqoop-1.4.7.bin__hadoop-2.6.0.tar.gz
[2]: https://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-java-5.1.47.tar.gz
