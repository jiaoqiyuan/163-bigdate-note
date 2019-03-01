#!/bin/bash
# https://blog.csdn.net/xianjie0318/article/details/82142248
HIVE_DB=bigdata.
 
#201703
datename=`date -d last-day +"%Y%m"`
echo $datename
#jdbc:mysql://${DB1IP}:${DB1PORT}/${DATABASW}?useUnicode=true&characterEncoding=utf-8&tinyInt1isBit=false
DB1IP='jdbc:mysql://10.173.32.6:3306/sqoop? characterEncoding=UTF-8&useCursorFetch=true'
echo $DB1IP
#username
DB1NAME=root
echo $DB1NAME
#password
DB1PASS=Gg/ru,.#5
echo $DB1PASS
#mysql table name
tablename='member'
echo $tablename
 
today=`date +"%Y-%m-%d" -d  "-0 days"`
yesterday=`date +"%Y-%m-%d" -d  "-1 days"`
 
creat_eventbase_partitioned="
create table ${HIVE_DB}eventbase_$datename (
 birthday string , gender string ,user_id string, nick_name string, name string, device_type string, register_time string)
partitioned by (day string)
stored as PARQUET;
"
 
insert_eventbase_partitioned="
set hive.exec.dynamic.partition.mode=nonstrict;
insert into table
${HIVE_DB}eventbase_$datename
partition(day)
    select
    tmp.birthday, tmp.gender, tmo.user_id, tmp.nick_name, tmp.name, tmp.device_type, tmp.register_time,
    ${today} day
    from
        ${HIVE_DB}eventbase_temp tmp
 distribute by time;
 "
 
 
#sqoop mysql-->hive
sqoop import --connect $DB1IP \
    --username $DB1NAME --password $DB1PASS \
    --table $tablename \
    --hive-import
    --hive-table ${HIVE_DB}eventbase_temp \
    --hive-drop-import-delims
    --where "start_time < '${today} 00:00:00' and start_time >= '${yesterday} 00:00:00' "
 
isok=$?
echo $isok
if [ $isok == "0" ]
    then
    #导入分区库成功
    I_STATS=0
    echo '-->mysql -> hive success'
else
  #导入分区库失败
    I_STATS=1
    echo $I_STATS
    echo '-->mysql -> hive failed'
    exit 0
fi
 
#建表
hive -e "$creat_eventbase_partitioned"
echo '-->start'
 
#插入分区库 --！！！--是否删除temp--
hive -e "$insert_eventbase_partitioned"
isok=$?
echo $isok
if [ $isok == "0" ]
      then
      #导入分区库成功
      I_STATS=0
    echo '-->insert_eventbase_partitioned success'
else
    #导入分区库失败
      I_STATS=1
      echo $I_STATS
    echo '-->insert_eventbase_partitioned failed'
      exit 0
fi
 
#删除temp--
hive -e "drop table ${HIVE_DB}eventbase_temp;"
isok=$?
echo $isok
if [ $isok == "0" ]
      then
      #导入分区库成功
      I_STATS=0
    echo '-->truncate success'
else
    #导入分区库失败
      I_STATS=1
      echo $I_STATS
    echo '-->truncate failed'
      exit 0
fi
 
 
#更新impala
impala-shell -q 'invalidate metadata'
 
 
echo "---->finish-->$datename"
 
echo $I_STATS