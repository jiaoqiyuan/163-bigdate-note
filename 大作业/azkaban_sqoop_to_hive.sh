#!/bin/bash
# https://blog.csdn.net/xianjie0318/article/details/82142248
HIVE_DB=bigdata

#jdbc:mysql://${DB1IP}:${DB1PORT}/${DATABASW}?useUnicode=true&characterEncoding=utf-8&tinyInt1isBit=false
DB1IP='jdbc:mysql://10.173.32.6:3306/sqoop? characterEncoding=UTF-8&useCursorFetch=true'
echo $DB1IP
#username
DB1NAME=root
echo $DB1NAME
#password
DB1PASS=Gg/ru,.\#5
echo $DB1PASS
#mysql table name
tablename='member'
echo $tablename
 
today=`date +"%Y-%m-%d" -d  "-0 days"`
yesterday=`date +"%Y-%m-%d" -d  "-1 days"`

create_bigdata_product="
CREATE EXTERNAL TABLE IF NOT EXISTS `${HIVE_DB}.product` (
    `price`         bigint      COMMENT     '价格',
    `product_id`    string      COMMENT     '商品编号',
    `product_name`  string      COMMENT     '商品名称'
) PARTITIONED BY(
    `day` string COMMENT '日期'
) ROW FORMAT DELIMITED
    FIELDS TERMINATED BY ','    
LOCATION
    '/user/1015146591/hive/product';
"

create_bigdata_member="
CREATE EXTERNAL TABLE IF NOT EXISTS `${HIVE_DB}.member` (
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
"

create_bigdata_orders="
create external table `${HIVE_DB}.orders` (
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
"

alter_bigdata_product="
alter table ${HIVE_DB}.product add partition (day='${today}') location '/user/1015146591/hive/product/day=${today}';
"

alter_bigdata_member="
alter table ${HIVE_DB}.member add partition (day='${today}') location '/user/1015146591/hive/member/day=${today}';
"

alter_bigdata_orders="
alter table ${HIVE_DB}.orders add partition (day='${today}') location '/user/1015146591/hive/orders/day=${today}';
"

# sqoop mysql--->/user/1015146591/hive/product/${today}
sqoop import --connect $DB1IP \
    --username $DB1NAME \
    --password $DB1PASS \
    --table product \
    --target-dir "/user/1015146591/hive/product/day=${today}"v\
    --delete-target-dir

isok=$?
echo $isok
if [ $isok == "0" ]
    then
    #导入分区库成功
    I_STATS=0
    echo "-->mysql -> hive ${HIVE_DB}.product success."
else
    #导入分区库失败
    I_STATS=1
    echo $I_STATS
    echo "-->mysql -> hive ${HIVE_DB}.product failed."
    # exit 0
fi

# sqoop mysql--->/user/1015146591/hive/member/${today}
sqoop import --connect  $DB1IP \
    --username $DB1NAME \
    --password $DB1PASS \
    --table member \
    --target-dir "/user/1015146591/hive/member/day=${today}" \
    -m 1 \
    --delete-target-dir

isok=$?
echo $isok
if [ $isok == "0" ]
    then
    #导入分区库成功
    I_STATS=0
    echo "-->mysql -> hive ${HIVE_DB}.member success."
else
    #导入分区库失败
    I_STATS=1
    echo $I_STATS
    echo "-->mysql -> hive ${HIVE_DB}.member failed."
    # exit 0
fi

# sqoop mysql--->/user/1015146591/hive/orders/${today}
sqoop import --connect  $DB1IP \
    --username $DB1NAME \
    --password $DB1PASS \
    --table orders \
    --target-dir "/user/1015146591/hive/orders/day=${today}" \
    -m 1 \
    --delete-target-dir

isok=$?
echo $isok
if [ $isok == "0" ]
    then
    #导入分区库成功
    I_STATS=0
    echo "-->mysql -> hive ${HIVE_DB}.orders success."
else
    #导入分区库失败
    I_STATS=1
    echo $I_STATS
    echo "-->mysql -> hive ${HIVE_DB}.orders failed."
    # exit 0
fi
 
#建表
hive -e "$create_bigdata_product"
hive -e "$create_bigdata_member"
hive -e "$create_bigdata_orders"
echo '-->start'

# 加载数据到对应表的对应分区
hive -e "${alter_bigdata_product}"
hive -e "${alter_bigdata_member}"
hive -e "${alter_bigdata_orders}"
 

echo "---->finish-->"
 
echo $I_STATS