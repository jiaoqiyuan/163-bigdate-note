#!/bin/bash
# https://blog.csdn.net/xianjie0318/article/details/82142248

source /mnt/home/1015146591/.bashrc

HIVE_DB=bigdata
DB1IP='jdbc:mysql://10.173.32.6:3306/sqoop? characterEncoding=UTF-8&useCursorFetch=true'
echo $DB1IP
#username
DB1NAME=root
echo $DB1NAME
#password
DB1PASS=Gg/ru,.\#5
echo $DB1PASS

today=`date +"%Y-%m-%d" -d  "-0 days"`

create_bigdata_product="
CREATE EXTERNAL TABLE IF NOT EXISTS \`${HIVE_DB}.product\` ( 
    \`price\`         bigint      COMMENT     '价格',	
    \`product_id\`    string      COMMENT     '商品编号', 
    \`product_name\`  string      COMMENT     '商品名称' 
) PARTITIONED BY( 
    \`day\` string COMMENT '日期' 
) ROW FORMAT DELIMITED 
    FIELDS TERMINATED BY ','     
LOCATION 
    '/user/1015146591/hive/product';
"
echo $create_bigdata_product


create_bigdata_member="
CREATE EXTERNAL TABLE IF NOT EXISTS \`${HIVE_DB}.member\` (
    \`birthday\`          string      COMMENT '生日',
    \`gender\`            string      COMMENT '性别',
    \`user_id\`           string      COMMENT '用户id',
    \`nick_name\`         string      COMMENT '昵称',
    \`name\`              string      COMMENT '姓名',
    \`device_type\`       string      COMMENT '设备类型',
    \`register_time\`     string    COMMENT '注册时间'
) PARTITIONED BY (
    \`day\` string COMMENT '日期'
) ROW FORMAT DELIMITED
    FIELDS TERMINATED BY ','    
LOCATION
    '/user/1015146591/hive/member';
"
echo $create_bigdata_member

create_bigdata_orders="
create external table if not exists \`${HIVE_DB}.orders\` (
    \`user_id\`       string  comment '用户id',
    \`product_id\`    string  comment '商品id',
    \`pay_amount\`    double  comment '金额',
    \`order_time\`    string  comment '下单时间',
    \`order_id\`      string  comment '订单id'
) PARTITIONED BY(
    \`day\` string COMMENT '日期'
) ROW FORMAT DELIMITED
    FIELDS TERMINATED BY ','    
LOCATION
    '/user/1015146591/hive/orders';
"
echo $create_bigdata_orders


alter_bigdata_product="
alter table ${HIVE_DB}.product drop if exists partition (day='${today}');
alter table ${HIVE_DB}.product add partition (day='${today}') location '/user/1015146591/hive/product/day=${today}';
"
alter_bigdata_member="
alter table ${HIVE_DB}.member drop if exists partition (day='${today}');
alter table ${HIVE_DB}.member add partition (day='${today}') location '/user/1015146591/hive/member/day=${today}';
"

alter_bigdata_orders="
alter table ${HIVE_DB}.orders drop if exists partition (day='${today}');
alter table ${HIVE_DB}.orders add partition (day='${today}') location '/user/1015146591/hive/orders/day=${today}';
"

cd /mnt/home/1015146591/workspace/homework_hive

echo "start sqoop mysql--->/user/1015146591/hive/product/${today}"
sqoop import --connect "$DB1IP" \
    --username "$DB1NAME" \
    --password "$DB1PASS" \
    --table product \
    --target-dir "/user/1015146591/hive/product/day=${today}" \
    --m 1 \
    --delete-target-dir

isok=$?
echo $isok

echo "start sqoop mysql--->/user/1015146591/hive/member/${today}"
sqoop import --connect  "$DB1IP" \
    --username "$DB1NAME" \
    --password "$DB1PASS" \
    --table member \
    --target-dir "/user/1015146591/hive/member/day=${today}" \
    -m 1 \
    --delete-target-dir
isok=$?
echo $isok

echo  "start sqoop mysql--->/user/1015146591/hive/orders/${today}"
sqoop import --connect  "$DB1IP" \
    --username "$DB1NAME" \
    --password "$DB1PASS" \
    --table orders \
    --target-dir "/user/1015146591/hive/orders/day=${today}" \
    -m 1 \
    --delete-target-dir
isok=$?
echo $isok
 
echo '-->start'
#建表
hive -e "$create_bigdata_product"
hive -e "$create_bigdata_member"
hive -e "$create_bigdata_orders"
# 加载数据到对应表的对应分区
hive -e "${alter_bigdata_product}"
hive -e "${alter_bigdata_member}"
hive -e "${alter_bigdata_orders}"

echo "---->finish-->"
echo $I_STATS
