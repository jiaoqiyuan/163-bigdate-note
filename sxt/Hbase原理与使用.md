## Hbase 分布式环境搭建

|  | NN-1 | NN-2 | DN | ZK | ZKFC | JNN | RS | NM | HbaseMaster | HbaseRegionserver |
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| node01 | * |   |   |   | * | * |   |   | * |   |
| node02 |   | * | * | * | * | * |   | * |   | * |
| node03 |   |   | * | * |   | * | * | * |   | * |
| node04 |   |   | * | * |   |   | * | * |   | * |

1. 下载 Hbase。

2. 同步四台虚拟机时间，使用 `netdata` 命令：

    ```
    yum install -y ntp
    ntpdate ntp1.aliyun.com
    ```
3. 启动 Hadoop 集群。
    
    ```
    star-dfs.sh
    ```
4.  解压 Hbase 文件夹：

    ```
    tar xzvf hbase-0.98.12.1-hadoop2-bin.tar.gz -C /opt/sxt/
    ```

5. 配置 Hbase。

    解压后配置 `node01/node02/node03/node04` 环境变量：

    ```conf
    # Hbase
    export HBASE_HOME=/opt/sxt/hbase-0.98.12.1
    export PATH=$HBASE_HOME/bin:$PATH
    ```

    生效 `node01/node02/node03/node04` 环境变量：

    ```
    [root@node01 ~]# source /etc/profile
    [root@node02 ~]# source /etc/profile
    [root@node03 ~]# source /etc/profile
    [root@node04 ~]# source /etc/profile
    ```

    配置 `conf/hbase-env.sh` ：

    ```conf
    export JAVA_HOME=/opt/sxt/jdk1.8.0_18
    export HBASE_MANAGES_ZK=false
    ```

    配置 `conf/hbase-site.xml` ：

    ```xml
    <property>
        <name>hbase.rootdir</name>
        <value>hdfs://mycluster/hbase</value>
    </property>

    <property>
        <name>hbase.cluster.distributed</name>
        <value>true</value>
    </property>

    <property>
        <name>hbase.zookeeper.quorum</name>
        <value>node02,node03,node04</value>
    </property>
    ```

    配置 `conf/regionservers` ：

    ```
    node02
    node03
    node04
    ```

    拷贝 `hdfs-site.xml` 到 hbase 的 conf 目录：

    ```
    [root@node01 conf]# cp ../../hadoop-2.6.5/etc/hadoop/hdfs-site.xml .
    ```

    拷贝配置后的 hbase 目录到 node02/node03/node04 ：

    ```
    [root@node01 sxt]# scp -r hbase-0.98.12.1/ node02:`pwd`
    [root@node01 sxt]# scp -r hbase-0.98.12.1/ node03:`pwd`
    [root@node01 sxt]# scp -r hbase-0.98.12.1/ node04:`pwd`
    ```

6. 启动 hbase ：

    ```
    [root@node01 bin]# start-hbase.sh
    ```

7. 启动 `hbase shell` ：

    ```
    [root@node01 bin]# hbase shell
    ```

    ```
    HBase Shell, version 0.98.12.1-hadoop2, rb00ec5da604d64a0bdc7d92452b1e0559f0f5d73, Sun May 17 12:55:03 PDT 2015
    Type 'help "COMMAND"', (e.g. 'help "get"' -- the quotes are necessary) for help on a specific command.
    Commands are grouped. Type 'help "COMMAND_GROUP"', (e.g. 'help "general"') for help on a command group.

    COMMAND GROUPS:
    Group name: general
    Commands: status, table_help, version, whoami

    Group name: ddl
    Commands: alter, alter_async, alter_status, create, describe, disable, disable_all, drop, drop_all, enable, enable_all, exists, get_table, is_disabled, is_enabled, list, show_filters

    Group name: namespace
    Commands: alter_namespace, create_namespace, describe_namespace, drop_namespace, list_namespace, list_namespace_tables

    Group name: dml
    Commands: append, count, delete, deleteall, get, get_counter, get_splits, incr, put, scan, truncate, truncate_preserve

    Group name: tools
    Commands: assign, balance_switch, balancer, catalogjanitor_enabled, catalogjanitor_run, catalogjanitor_switch, close_region, compact, compact_rs, flush, hlog_roll, major_compact, merge_region, move, split, trace, unassign, zk_dump

    Group name: replication
    Commands: add_peer, disable_peer, disable_table_replication, enable_peer, enable_table_replication, list_peers, list_replicated_tables, remove_peer, set_peer_tableCFs, show_peer_tableCFs

    Group name: snapshots
    Commands: clone_snapshot, delete_all_snapshot, delete_snapshot, list_snapshots, restore_snapshot, snapshot

    Group name: security
    Commands: grant, revoke, user_permission

    Group name: visibility labels
    Commands: add_labels, clear_auths, get_auths, list_labels, set_auths, set_visibility

    SHELL USAGE:
    Quote all names in HBase Shell such as table and column names.  Commas delimit
    command parameters.  Type <RETURN> after entering a command to run it.
    Dictionaries of configuration used in the creation and alteration of tables are
    Ruby Hashes. They look like this:

    {'key1' => 'value1', 'key2' => 'value2', ...}

    and are opened and closed with curley-braces.  Key/values are delimited by the
    '=>' character combination.  Usually keys are predefined constants such as
    NAME, VERSIONS, COMPRESSION, etc.  Constants do not need to be quoted.  Type
    'Object.constants' to see a (messy) list of all constants in the environment.

    If you are using binary keys or values and need to enter them in the shell, use
    double-quote'd hexadecimal representation. For example:

    hbase> get 't1', "key\x03\x3f\xcd"
    hbase> get 't1', "key\003\023\011"
    hbase> put 't1', "test\xef\xff", 'f1:', "\x01\x33\x40"

    The HBase shell is the (J)Ruby IRB with the above HBase-specific commands added.
    For more on the HBase Shell, see http://hbase.apache.org/book.html

    ```

