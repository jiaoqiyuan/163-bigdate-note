## Hive中创建数据库

- 最简单的创建数据库方法：

    ```
    hive> create database bigdata;
    OK
    Time taken: 0.065 seconds
    hive> show databases;
    OK
    bigdata
    default
    Time taken: 0.01 seconds, Fetched: 2 row(s)
    ```

- 重复创建数据库会报错：

    ```
    hive> create database bigdata;
    FAILED: Execution Error, return code 1 from org.apache.hadoop.hive.ql.exec.DDLTask. Database bigdata already exists
    ```

- 更严谨的做法是：

    ```
    hive> create database if not exists bigdata;
    OK
    Time taken: 0.013 seconds
    ````

- 删除一个数据库

    ```
    hive> drop database if exists bigdata;
    OK
    Time taken: 0.327 seconds
    hive> show databases;
    OK
    default
    Time taken: 0.018 seconds, Fetched: 1 row(s)
    ```

- 创建外部表

    - 拷贝json解析包到hive目录下的lib

    ```
    cp /tmp/hivecourse/json-serde-1.3.6-jar-with-dependencies.jar /mnt/home/1015146591/apps/hive-1.2.2/lib
    ```

    - 在Hive文件中增加该jar包配置

    ```
    <property>
        <name>hive.aux.jars.path</name>
        <value>file:///mnt/home/1015146591/apps/hive-1.2.2/lib/json-serde-1.3.6-jar-with-dependencies.jar</value>
    </property>
    ```

    - 创建Hive数据表，建表语句如下：

    ```sql
    CREATE EXTERNAL TABLE IF NOT EXISTS `bigdata.weblog` (
        `time_tag`      bigint      COMMENT '时间',
        `active_name`   string      COMMENT '事件名称',
        `device_id`     string      COMMENT '设备id',
        `session_id`    string      COMMENT '会话id',
        `user_id`       string      COMMENT '用户id',
        `ip`            string      COMMENT 'ip地址',
        `address`       map<string, string> COMMENT '地址',
        `req_url`       string      COMMENT 'http请求地址',
        `action_path`   array<string>   COMMENT '访问路径',
        `product_id`    string      COMMENT '商品id',
        `order_id`      string      COMMENT '订单id'
    ) PARTITIONED BY(
        `day` string COMMENT '日期'
    ) ROW FORMAT SERDE
        'org.openx.data.jsonserde.JsonSerDe'
    STORED AS INPUTFORMAT
        'org.apache.hadoop.mapred.TextInputFormat'
    OUTPUTFORMAT
        'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
    LOCATION
        '/user/hadoop/weblog';
    ```

    - 添加分区指令

    ```sql
    alter table bigdata.weblog add partition (day='2018-05-29') location '/user/hadoop/weblog/day=2018-05-29';
    alter table bigdata.weblog add partition (day='2018-05-30') location '/user/hadoop/weblog/day=2018-05-30';
    alter table bigdata.weblog add partition (day='2018-05-31') location '/user/hadoop/weblog/day=2018-05-31';
    alter table bigdata.weblog add partition (day='2018-06-01') location '/user/hadoop/weblog/day=2018-06-01';
    alter table bigdata.weblog add partition (day='2018-06-02') location '/user/hadoop/weblog/day=2018-06-02';
    alter table bigdata.weblog add partition (day='2018-06-03') location '/user/hadoop/weblog/day=2018-06-03';
    alter table bigdata.weblog add partition (day='2018-06-04') location '/user/hadoop/weblog/day=2018-06-04';
    alter table bigdata.weblog add partition (day='2018-06-05') location '/user/hadoop/weblog/day=2018-06-05';
    alter table bigdata.weblog add partition (day='2018-06-06') location '/user/hadoop/weblog/day=2018-06-06';
    alter table bigdata.weblog add partition (day='2018-06-07') location '/user/hadoop/weblog/day=2018-06-07';
    ```

    