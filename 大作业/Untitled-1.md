# weblog数据表
## 创建数据库
```sql
create database if not exists bigdata;
```


## 创建存放日志信息的外部数据表weblog
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
    '/user/1015146591/weblog';
```

## 将hdfs上解析出的数据加载到外部表weblog中
```sql
alter table bigdata.weblog add partition (day='2018-05-29') location '/user/1015146591/weblog/day=2018-05-29';
alter table bigdata.weblog add partition (day='2018-05-30') location '/user/1015146591/weblog/day=2018-05-30';
alter table bigdata.weblog add partition (day='2018-05-31') location '/user/1015146591/weblog/day=2018-05-31';
alter table bigdata.weblog add partition (day='2018-06-01') location '/user/1015146591/weblog/day=2018-06-01';
alter table bigdata.weblog add partition (day='2018-06-02') location '/user/1015146591/weblog/day=2018-06-02';
alter table bigdata.weblog add partition (day='2018-06-03') location '/user/1015146591/weblog/day=2018-06-03';
alter table bigdata.weblog add partition (day='2018-06-04') location '/user/1015146591/weblog/day=2018-06-04';
alter table bigdata.weblog add partition (day='2018-06-05') location '/user/1015146591/weblog/day=2018-06-05';
alter table bigdata.weblog add partition (day='2018-06-06') location '/user/1015146591/weblog/day=2018-06-06';
alter table bigdata.weblog add partition (day='2018-06-07') location '/user/1015146591/weblog/day=2018-06-07';
```

## 查询指定分区数据
select * from weblog where day='2018-05-30' limit 10;


# member数据表
## 创建存放成员信息的外部表member
```sql
CREATE EXTERNAL TABLE IF NOT EXISTS `bigdata.member` (
    `birthday`          string      COMMENT '生日',
    `gender`            string      COMMENT '性别',
    `user_id`           string      COMMENT '用户id',
    `nick_name`         string      COMMENT '昵称',
    `name`              string      COMMENT '姓名',
    `device_type`       string      COMMENT '设备类型',
    `register_time`     string    COMMENT '注册时间'
) PARTITIONED BY (
    `day` string COMMENT '日期'
) ROW FORMAT DELIMITED
    FIELDS TERMINATED BY ','    
LOCATION
    '/user/1015146591/hive/member';
```

## 将hdfs上的数据加载到hive中bigdata.weblog外部表中（不用理会）
sqoop import --connect "jdbc:mysql://10.173.32.6:3306/sqoop? characterEncoding=UTF-8&useCursorFetch=true" --username root --password Gg/ru,.#5 --table member --target-dir "/user/1015146591/sqooptest" --hive-import --hive-table bigdata.member --hive-overwrite

## 导入mysql中sqoop数据库中的member表到hdfs的/user/1015146591/hive/member中
sqoop import --connect "jdbc:mysql://10.173.32.6:3306/sqoop? characterEncoding=UTF-8&useCursorFetch=true" --username root --password Gg/ru,.#5 --table member --target-dir '/user/1015146591/hive/member/day=2018-05-29' -m 1 --delete-target-dir

## 将hdfs上的数据加载到hive中的bigdata.member外部表中：
alter table bigdata.member add partition (day='2018-05-29') location '/user/1015146591/hive/member/day=2018-05-29';


# product数据表

## 创建存放商品信息的外部表product
```sql
CREATE EXTERNAL TABLE IF NOT EXISTS `bigdata.product` (
    `price`         bigint      COMMENT     '价格',
    `product_id`    string      COMMENT     '商品编号',
    `product_name`  string      COMMENT     '商品名称'
) PARTITIONED BY(
    `day` string COMMENT '日期'
) ROW FORMAT DELIMITED
    FIELDS TERMINATED BY ','    
LOCATION
    '/user/1015146591/hive/product';
```

## 从mysql导入数据到hdfs的/user/1015146591/hive/product目录中
```bash
sqoop import --connect "jdbc:mysql://10.173.32.6:3306/sqoop? characterEncoding=UTF-8&useCursorFetch=true" --username root --password Gg/ru,.#5 --table product --target-dir '/user/1015146591/hive/product/day=2018-05-29' -m 1 --delete-target-dir
```

## 将hdfs上的数据加载到hive中的bigdata.product外部表中
```bash
alter table bigdata.product add partition (day='2018-05-29') location '/user/1015146591/hive/product/day=2018-05-29';
```

# orders数据表
## 创建存放交易订单的外部表orders
```sql
create external table `bigdata.orders` (
    `user_id`       string  comment '用户id',
    `product_id`    string  comment '商品id',
    `pay_amount`    double  comment '金额',
    `order_time`    string  comment '下单时间',
    `order_id`      string  comment '订单id'
) PARTITIONED BY(
    `day` string COMMENT '日期'
) ROW FORMAT DELIMITED
    FIELDS TERMINATED BY ','    
LOCATION
    '/user/1015146591/hive/orders';
```

## 从mysql导入sqoop.orders表中的数据到hdfs的/user/1015146591/hive/orders目录中
```bash
sqoop import --connect "jdbc:mysql://10.173.32.6:3306/sqoop? characterEncoding=UTF-8&useCursorFetch=true" --username root --password Gg/ru,.#5 --table orders --target-dir '/user/1015146591/hive/orders/day=2018-05-29' -m 1 --delete-target-dir
```

## 将hdfs上的数据加载到hive中的bigdata.orders外部表中
```bash
alter table bigdata.orders add partition (day='2018-05-29') location '/user/1015146591/hive/orders/day=2018-05-29';
```

mysql -u root -pGg/ru,.#5 -h 10.173.32.6 -P3306


# 第四步

## 1. 计算每天有多少的pv、uv、订单量和收入,注册用户数（如何一个sql语法全部查出来）

### PV

PV（page view，页面浏览量）

用户每打开1个网站页面，记录1个PV。用户多次打开同一页面，PV值累计多次。主要用来衡量网站用户访问的网页数量。是评价网站流量最常用的指标之一。


```sql
select count(req_url) from weblog;
2175228
```

### UV

UV（ unique visitor，网站独立访客）

通过互联网访问、流量网站的自然人。1天内相同访客多次访问网站，只计算为1个独立访客。该概念的引入，是从用户个体的角度对访问数据进行划分。

```sql
select count(distinct user_id) from weblog;
100569
```

### 订单量

直接计算order_id的个数即可

```sql
select count(distinct order_id) from orders;
15104
```

### 收入

计算pay_amount的总和即可

```sql
select sum(pay_amount) from orders;
939095.0
```

### 注册用户数

计算member表中user_id的个数就可以了吧

```sql
select count(user_id) from member;
30180
```

将所有查询信息放入一个sql中

```sql
create table if not exists `bigdata.puoir`
as 
select 
    pv, uv, order_num, income, register_num
from
    (select count(req_url) as pv from weblog) q1,
    (select count(distinct user_id) as uv from weblog) q2,
    (select count(distinct order_id) as order_num from orders) q3,
    (select sum(pay_amount) as income from orders) q4,
    (select count(user_id) as register_num from member) q5;
2175228	100569	15104	939095.0	30180
```

## 2. 计算访问product页面的用户中，有多少比例在30分钟内下单并且支付成功对应的商品
```sql
select 
    format_number(q1.good_user / q2.all_user, 2)
from 
    (select 
            count(distinct wl.user_id) as good_user
        from 
            weblog wl 
        join orders od on 
            wl.user_id=od.user_id
        where 
            wl.product_id=od.product_id and 
            (cast(substring(od.order_time, 1, 10) as bigint) - cast(substring(wl.time_tag, 1, 10) as bigint) < 1800)
    ) q1,
    (select count(distinct user_id) as all_user from weblog) q2
```
15104
100569
0.15

## 3、更改第二个sql，需要做到 支持变更不同的页面类型（正则）和目标事件，支持指定时间间隔的转化率分析（需要写明设计思路）

## 4、通过sql 计算每个商品的每天的pv，uv 并存入新建hive表，表名字段名，设计格式可以自己定义

```sql
select from_unixtime(cast(substring(time_tag, 1, 10) as bigint), 'yyyy-MM-dd'), product_id, count(req_url), count(distinct user_id)
from bigdata.weblog
group by from_unixtime(cast(substring(time_tag, 1, 10) as bigint), 'yyyy-MM-dd'), product_id;


select from_unixtime(cast(substring(time_tag, 1, 10) as bigint), 'yyyy-MM-dd'), product_id, count(product_id)
from bigdata.weblog
group by from_unixtime(cast(substring(time_tag, 1, 10) as bigint), 'yyyy-MM-dd'), product_id;


select product_name, from_unixtime(cast(substring(time_tag, 1, 10) as bigint), 'yyyy-MM-dd'), count(distinct user_id)
from bigdata.weblog wl join product pc on wl.product_id = pc.product_id 
group by from_unixtime(cast(substring(time_tag, 1, 10) as bigint), 'yyyy-MM-dd'), product_name
order by product_name, time_tag;

select product_name, from_unixtime(cast(substring(time_tag, 1, 10) as bigint) as time_tmp, 'yyyy-MM-dd'), count(distinct user_id)
from bigdata.weblog wl join product pc on wl.product_id = pc.product_id 
group by time_tmp, product_name
order by product_name, time_tag;
```