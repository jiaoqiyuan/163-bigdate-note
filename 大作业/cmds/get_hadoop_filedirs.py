# encoding:UTF-8
import commands

cmd = "hadoop fs -ls /user/1015146591/weblog | grep ^d | awk '{print $8}'"
status, dirs = commands.getstatusoutput(cmd)

#创建数据表weblog
create_weblog_cmd = "cd /mnt/home/1015146591/workspace/homework_hive; \
    /mnt/home/1015146591/apps/hive-1.2.2/bin/hive -e \"CREATE EXTERNAL TABLE IF NOT EXISTS \`bigdata.weblog\` ( \
    \`time_tag\`      bigint      COMMENT '时间', \
    \`active_name\`   string      COMMENT '事件名称', \
    \`device_id\`     string      COMMENT '设备id', \
    \`session_id\`    string      COMMENT '会话id', \
    \`user_id\`       string      COMMENT '用户id', \
    \`ip\`            string      COMMENT 'ip地址', \
    \`address\`       map<string, string> COMMENT '地址', \
    \`req_url\`       string      COMMENT 'http请求地址', \
    \`action_path\`   array<string>   COMMENT '访问路径', \
    \`product_id\`    string      COMMENT '商品id', \
    \`order_id\`      string      COMMENT '订单id' \
) PARTITIONED BY( \
    \`day\` string COMMENT '日期' \
) ROW FORMAT SERDE \
    'org.openx.data.jsonserde.JsonSerDe' \
STORED AS INPUTFORMAT \
    'org.apache.hadoop.mapred.TextInputFormat' \
OUTPUTFORMAT \
    'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat' \
LOCATION \
    '/user/1015146591/weblog';"

#status, output = commands.getstatusoutput(create_weblog_cmd)

#alter_cmd = "cd /mnt/home/1015146591/workspace/homework_hive; /mnt/home/1015146591/apps/hive-1.2.2/bin/hive -e \""
all_cmd = create_weblog_cmd
for dir in dirs.split('\n'):
    day = dir.split('=')[-1]
    all_cmd += "alter table bigdata.weblog add partition (day='" + day + "') location '" + dir + "';"
all_cmd += "\""
print all_cmd
status, outputs = commands.getstatusoutput(all_cmd)
print status
