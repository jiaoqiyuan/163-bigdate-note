mysql -u root -pGg/ru,.#5 -h 10.173.32.6 -P3306
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




# 第四步

## 1. 计算每天有多少的pv、uv、订单量和收入,注册用户数（如何一个sql语法全部查出来）

### PV

PV（page view，页面浏览量）

用户每打开1个网站页面，记录1个PV。用户多次打开同一页面，PV值累计多次。主要用来衡量网站用户访问的网页数量。是评价网站流量最常用的指标之一。

UV（ unique visitor，网站独立访客）

通过互联网访问、流量网站的自然人。1天内相同访客多次访问网站，只计算为1个独立访客。该概念的引入，是从用户个体的角度对访问数据进行划分。

```sql
select day, count(req_url), count(distinct user_id) from bigdata.weblog group by day;
select day, count(active_name), count(distinct user_id) from bigdata.weblog where active_name = 'pageview' group by day;
2175228
```

### 订单量 

直接计算order_id的个数即可

```sql
select from_unixtime(cast(substring(order_time, 1, 10) as bigint), 'yyyy-MM-dd'), count(distinct order_id), sum(pay_amount) from bigdata.orders group by from_unixtime(cast(substring(order_time, 1, 10) as bigint), 'yyyy-MM-dd');
15104
```


### 注册用户数

计算member表中user_id的个数就可以了吧

```sql
select from_unixtime(cast(substring(register_time, 1, 10) as bigint), 'yyyy-MM-dd'), count(distinct user_id) from bigdata.member group by from_unixtime(cast(substring(register_time, 1, 10) as bigint), 'yyyy-MM-dd');
30180
```

将所有查询信息放入一个sql中

```sql
select r1.day, pv, uv, orders_count, income, register_count from 
(select day, count(req_url) pv, count(distinct user_id) uv from bigdata.weblog group by day) r1 left join 
(select from_unixtime(cast(substring(order_time, 1, 10) as bigint), 'yyyy-MM-dd') day, count(distinct order_id) orders_count, sum(pay_amount) income from bigdata.orders group by from_unixtime(cast(substring(order_time, 1, 10) as bigint), 'yyyy-MM-dd')) r2 on r1.day = r2.day left join 
(select from_unixtime(cast(substring(register_time, 1, 10) as bigint), 'yyyy-MM-dd') day, count(distinct user_id) register_count from bigdata.member group by from_unixtime(cast(substring(register_time, 1, 10) as bigint), 'yyyy-MM-dd')) r3
on r1.day = r3.day
order by r1.day
```

创建新表：
```sql
create table if not exists `bigdata.puoir` (
    `day`           string      comment     '日期',
    `pv`            bigint      comment     'PV',
    `uv`            bigint      comment     'UV',
    `order_count`   bigint      comment     '订单量',
    `income`        double      comment     '收入',
    `regitster_count`   bigint  comment     '注册用户数'
);
```

插入表中

```sql
insert into table bigdata.puoir
select r1.day, pv, uv, orders_count, income, register_count from 
(select day, count(req_url) pv, count(distinct user_id) uv from bigdata.weblog group by day) r1 left join 
(select from_unixtime(cast(substring(order_time, 1, 10) as bigint), 'yyyy-MM-dd') day, count(distinct order_id) orders_count, sum(pay_amount) income from bigdata.orders group by from_unixtime(cast(substring(order_time, 1, 10) as bigint), 'yyyy-MM-dd')) r2 on r1.day = r2.day left join 
(select from_unixtime(cast(substring(register_time, 1, 10) as bigint), 'yyyy-MM-dd') day, count(distinct user_id) register_count from bigdata.member group by from_unixtime(cast(substring(register_time, 1, 10) as bigint), 'yyyy-MM-dd')) r3
on r1.day = r3.day
order by r1.day
```

## 2. 计算访问product页面的用户中，有多少比例在30分钟内下单并且支付成功对应的商品


```sql
//找出所有访问product页面的用户数
select count(distinct user_id) from bigdata.weblog where req_url like '%/product%';

//找出有多少访问product页面的用户在30分钟内下单并成功支付
select count(distinct wl.user_id) 
from bigdata.weblog wl 
    join bigdata.orders ord on wl.user_id=ord.user_id and ord.pay_amount != 0
where wl.req_url like '%/product%' 
    and cast(substring(ord.order_time, 1, 10) as bigint) - cast(substring(wl.time_tag, 1, 10) as bigint) < 1800;

//完成。。。。。。。。。。
select 
    q1.good_user, q2.all_user, format_number(q1.good_user / q2.all_user, 2)
from 
    (select count(distinct wl.user_id) as good_user
    from bigdata.weblog wl 
        join bigdata.orders ord on wl.user_id=ord.user_id and ord.pay_amount != 0
    where wl.req_url like '%/product%' 
        and cast(substring(ord.order_time, 1, 10) as bigint) - cast(substring(wl.time_tag, 1, 10) as bigint) < 1800
    ) q1,
    (select count(distinct user_id) as all_user from bigdata.weblog where req_url like '%/product%') q2

select 
    format_number(q1.good_user / q2.all_user, 2)
from 
    (select 
            count(distinct wl.user_id) as good_user
        from 
            bigdata.weblog wl 
        join bigdata.orders od on 
            wl.user_id=od.user_id
        where wl.active_name = 'pay' and 
            (cast(substring(wl.time_tag, 1, 10) as bigint) - cast(substring(od.order_time, 1, 10) as bigint) < 1800)
    ) q1,
    (select count(distinct user_id) as all_user from bigdata.weblog where req_url like '%/product%') q2

```
12215
15104
100569
0.15

## 3、更改第二个sql，需要做到 支持变更不同的页面类型（正则）和目标事件，支持指定时间间隔的转化率分析（需要写明设计思路）

//使用正则找出所有页面类型
```sql
select distinct
    case regexp_extract(req_url, '.*?.com(.*?)$', 1) 
    when '' then  regexp_extract(req_url, '.*?//(.*?)$', 1)
    else 
        case regexp_extract(req_url, '.*?.com/(.*?)/.*?', 1)
        when '' then regexp_extract(req_url, '.*?.com/(.*?)$', 1)
        else regexp_extract(req_url, '.*?.com/(.*?)/.*?', 1)
        end
    end 
from bigdata.weblog;
```

//找出访问每种页面类型的用户中有多少比例的用户是在浏览页面30分钟内下单并成功支付的
```sql
select 
    case regexp_extract(req_url, '.*?.com(.*?)$', 1) 
    when '' then  regexp_extract(req_url, '.*?//(.*?)$', 1)
    else 
        case regexp_extract(req_url, '.*?.com/(.*?)/.*?', 1)
        when '' then regexp_extract(req_url, '.*?.com/(.*?)$', 1)
        else regexp_extract(req_url, '.*?.com/(.*?)/.*?', 1)
        end
    end as page_type,

```

```sql


//计算不同事件类型下每个类型有多少个用户访问
select active_name, count(distinct user_id) as all_users from bigdata.weblog group by active_name;


select r1.active_name, all_users, good_users, format_number(r2.good_users / r1.all_users, 2) from
(select active_name, count(distinct user_id) as all_users from bigdata.weblog group by active_name) r1 join 
(select active_name, count(distinct wl.user_id) as good_users from bigdata.weblog wl join bigdata.orders ord on wl.user_id=ord.user_id where (cast(substring(ord.order_time, 1, 10) as bigint) - cast(substring(wl.time_tag, 1, 10) as bigint) < 1800) group by active_name) r2 
on r1.active_name=r2.active_name
and r1.active_name = 'order';

select r1.page_type, all_users, good_users, format_number(good_users / all_users, 2) from
(select case regexp_extract(req_url, '.*?.com(.*?)$', 1) 
    when '' then  regexp_extract(req_url, '.*?//(.*?)$', 1)
    else 
        case regexp_extract(req_url, '.*?.com/(.*?)/.*?', 1)
        when '' then regexp_extract(req_url, '.*?.com/(.*?)$', 1)
        else regexp_extract(req_url, '.*?.com/(.*?)/.*?', 1)
        end
    end as page_type, 
    count(distinct user_id) as all_users from bigdata.weblog 
    group by 
    (case regexp_extract(req_url, '.*?.com(.*?)$', 1) 
    when '' then  regexp_extract(req_url, '.*?//(.*?)$', 1)
    else 
        case regexp_extract(req_url, '.*?.com/(.*?)/.*?', 1)
        when '' then regexp_extract(req_url, '.*?.com/(.*?)$', 1)
        else regexp_extract(req_url, '.*?.com/(.*?)/.*?', 1)
        end
    end)) r1 join 
    (select case regexp_extract(req_url, '.*?.com(.*?)$', 1) 
    when '' then  regexp_extract(req_url, '.*?//(.*?)$', 1)
    else 
        case regexp_extract(req_url, '.*?.com/(.*?)/.*?', 1)
        when '' then regexp_extract(req_url, '.*?.com/(.*?)$', 1)
        else regexp_extract(req_url, '.*?.com/(.*?)/.*?', 1)
        end
    end as page_type, 
    count(distinct wl.user_id) as good_users from bigdata.weblog wl join bigdata.orders ord on wl.user_id=ord.user_id where (cast(substring(ord.order_time, 1, 10) as bigint) - cast(substring(wl.time_tag, 1, 10) as bigint) < 1800) 
    group by 
    (case regexp_extract(req_url, '.*?.com(.*?)$', 1) 
    when '' then  regexp_extract(req_url, '.*?//(.*?)$', 1)
    else 
        case regexp_extract(req_url, '.*?.com/(.*?)/.*?', 1)
        when '' then regexp_extract(req_url, '.*?.com/(.*?)$', 1)
        else regexp_extract(req_url, '.*?.com/(.*?)/.*?', 1)
        end
    end)) r2 
on r1.page_type=r2.page_type
and r1.page_type = 'product';

select 
            count(distinct wl.user_id) as good_user
        from 
            bigdata.weblog wl 
        join bigdata.orders od on 
            wl.user_id=od.user_id
        where 
            wl.product_id=od.product_id and 
            wl.active_name = 'pay' and 
            (cast(substring(od.order_time, 1, 10) as bigint) - cast(substring(wl.time_tag, 1, 10) as bigint) < 1800)
```

//计算每种类型中访问用户最终在30分钟内下单并成功支付的个数
```sql

```

## 4、通过sql 计算每个商品的每天的pv，uv 并存入新建hive表，表名字段名，设计格式可以自己定义

```sql

//按照商品名和日期排序计算每个商品每天的PV
select product_name, from_unixtime(cast(substring(time_tag, 1, 10) as bigint), 'yyyy-MM-dd') c1, count(user_id)
from bigdata.weblog wl join product pc on wl.product_id = pc.product_id 
group by product_name, from_unixtime(cast(substring(time_tag, 1, 10) as bigint), 'yyyy-MM-dd') 
order by product_name, c1;

//按照商品名和日期排序计算每个商品每天的UV
select product_name, from_unixtime(cast(substring(time_tag, 1, 10) as bigint), 'yyyy-MM-dd') c1, count(distinct user_id)
from bigdata.weblog wl join product pc on wl.product_id = pc.product_id 
group by product_name, from_unixtime(cast(substring(time_tag, 1, 10) as bigint), 'yyyy-MM-dd')
order by product_name, c1;

//按照商品名和日期排序计算每个商品每天的PV和UV，并存入新的hive表
create table if not exists `bigdata.pvuvperproduct` (
    `product_name`      string      comment     '商品名称',
    `day`               string      comment     '日期',
    `pv`                bigint      comment     'pv',
    `uv`                bigint      comment     'uv'
);

insert into bigdata.pvuvperproduct 
select product_name, from_unixtime(cast(substring(time_tag, 1, 10) as bigint), 'yyyy-MM-dd') day, count(user_id) pv, count(distinct user_id) uv
from bigdata.weblog wl join bigdata.product pc on wl.product_id = pc.product_id 
group by product_name, from_unixtime(cast(substring(time_tag, 1, 10) as bigint), 'yyyy-MM-dd') 
order by product_name, day;

```

## 5. 计算每个商品每天的pv，uv的环比情况（今天-昨天/昨天），并且筛选出环比增长最大和环比增长最小的（负数小于正数）

```sql
// 每个商品每天的pv，uv环比情况
select pup2.product_name product_name, pup2.day day, format_number((pup2.pv - pup1.pv)/pup1.pv, 2) rap, format_number((pup2.uv - pup1.uv)/pup1.uv, 2) rau
from 
    bigdata.pvuvperproduct pup1 join bigdata.pvuvperproduct pup2
on
    pup1.product_name=pup2.product_name
where 
    unix_timestamp(pup2.day, 'yyyy-MM-dd') - unix_timestamp(pup1.day, 'yyyy-MM-dd') = 86400

//将每个商品每天的pv，uv环比情况存到一个临时表中
create table if not exists `bigdata.rapvuvperproduct` (
    `product_name`      string      comment     '商品名称',
    `day`               string      comment     '日期',
    `rap`                float      comment     '商品pv的环比',
    `rau`                float      comment     '商品uv的环比'
);
//插入数据到表中
insert into bigdata.rapvuvperproduct
select pup2.product_name product_name, pup2.day day, format_number((pup2.pv - pup1.pv)/pup1.pv, 2) rap, format_number((pup2.uv - pup1.uv)/pup1.uv, 2) rau
from 
    bigdata.pvuvperproduct pup1 join bigdata.pvuvperproduct pup2
on
    pup1.product_name=pup2.product_name
where 
    unix_timestamp(pup2.day, 'yyyy-MM-dd') - unix_timestamp(pup1.day, 'yyyy-MM-dd') = 86400


// 找出PV环比增长最大商品及时间
/*
select r1.product_name, day, maxrap from bigdata.rapvuvperproduct where rap = (
select map    from
    (
        select product_name, max(rap) maxrap from bigdata.rapvuvperproduct group by product_name
    ) r1 join bigdata.rapvuvperproduct rapup on r1.product_name=rapup.product_name and r1.maxrap = rapup.rap
)

select product_name, day, rap from bigdata.rapvuvperproduct where rap IN (
    select max(maxrap) as maxrap
    from (select product_name, max(rap) maxrap from bigdata.rapvuvperproduct group by product_name) r1 
    join bigdata.rapvuvperproduct rapup on r1.product_name=rapup.product_name and r1.maxrap = rapup.rap);
*/
select product_name, day, rap from rapvuvperproduct a where a.rap IN (select max(b.rap) from rapvuvperproduct b)
select product_name, day, rap from rapvuvperproduct a where a.rap IN (select min(b.rap) from rapvuvperproduct b)
select product_name, day, rau from rapvuvperproduct a where a.rau IN (select max(b.rau) from rapvuvperproduct b)
select product_name, day, rau from rapvuvperproduct a where a.rau IN (select min(b.rau) from rapvuvperproduct b)

//删除临时表
drop table bigdata.rapvuvperproduct;
```

## 6、计算每天的登录用户数中新老用户占比，并且统计新老用户分别的pv uv

```sql
//先所有登录用户，从weblog中找出当天所有用户数即可，时间可以通过脚本获取
select day, count(distinct user_id) all_users from bigdata.weblog where day in (select distinct day from bigdata.weblog) group by day;

//再找出新注册用户，从weblog中找出注册时间是当天的用户。
select wl.day, count(distinct wl.user_id) from bigdata.weblog wl join bigdata.member mb on wl.user_id=mb.user_id and wl.day = from_unixtime(cast(substring(register_time, 1, 10) as bigint), 'yyyy-MM-dd') group by wl.day;

select r1.day, all_users, new_users, format_number(new_users/all_users, 2), 1 - format_number(new_users/all_users, 2) from 
(select day, count(distinct user_id) all_users from bigdata.weblog where day in (select distinct day from bigdata.weblog) group by day) r1 left join 
(select wl.day, count(distinct wl.user_id) new_users from bigdata.weblog wl join bigdata.member mb on wl.user_id=mb.user_id and wl.day = from_unixtime(cast(substring(register_time, 1, 10) as bigint), 'yyyy-MM-dd') group by wl.day) r2 
on r1.day=r2.day;

//统计新用户的PV和UV
select wl.day, count(wl.req_url) as pv, count(distinct wl.user_id) as uv from bigdata.weblog wl join bigdata.member mb on wl.user_id = mb.user_id and wl.day = from_unixtime(cast(substring(mb.register_time, 1, 10) as bigint), 'yyyy-MM-dd') group by wl.day;

//计算每天老用户的pv和uv
select wl.day, count(wl.req_url) as pv, count(distinct wl.user_id) as uv from bigdata.weblog wl join
(select day, user_id, register_time from bigdata.member) r1
on wl.user_id = r1.user_id
where cast(substring(r1.register_time, 1, 10) as bigint) < cast(substring(wl.time_tag, 1, 10) as bigint)
group by wl.day;

```

## 7. 设计udaf

```sql
select collect_list(wl2.active_name, wl2.time_tag) from bigdata.weblog wl1 join bigdata.weblog wl2 on wl1.user_id = wl2.user_id where cast(substring(wl1.time_tag, 1, 10) as bigint) - cast(substring(wl2.time_tag, 1, 10) as bigint) < 1800;

select wl2.user_id, collect_list(wl2.active_name, wl2.time_tag) from bigdata.weblog wl1 join bigdata.weblog wl2 on wl1.user_id = wl2.user_id where (cast(substring(wl1.time_tag, 1, 10) as bigint) - cast(substring(wl2.time_tag, 1, 10) as bigint) < 1800) and (cast(substring(wl1.time_tag, 1, 10) as bigint) > cast(substring(wl2.time_tag, 1, 10) as bigint)) group by wl2.user_id;

select wl2.user_id, wl2.active_name, wl2.time_tag from bigdata.weblog wl1 join bigdata.weblog wl2 on wl1.user_id = wl2.user_id where cast(substring(wl1.time_tag, 1, 10) as bigint) - cast(substring(wl2.time_tag, 1, 10) as bigint) < 1800
```