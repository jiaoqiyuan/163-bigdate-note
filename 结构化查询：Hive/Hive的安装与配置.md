## Hive安装依赖

- Hadoop

- 元数据存储

    - 内嵌数据库Derby ----内嵌模式

    - 外部数据库

        - 本地数据库  ----本地模式

        - 远程数据库  ----远程模式

- 三种配置模式的特点和区别

    - 内嵌模式：内嵌的Derby数据库存储元数据，一次只能连接一个客户端

    - 本地模式：外部数据库存储元数据，metastore服务和hive运行在同一个进程。

    - 远程模式：外部数据库存储元数据，metastore服务和hive运行在不同的进程。

- 在hive-site.xml中可以通过jdbcURL、驱动、用户名、密码等配置信息，设置不同的模式。

- 生产环境下一般使用远程模式，测试环境下一般使用内嵌模式。

## Hive安装与配置

- Hive安装包结构

| 文件夹 | 存放内容 |
|:-------:|:-------:|
| bin | 执行文件目录 |
| conf | 配置文件目录 |
| examples | 样例目录 |
| hcatalog | 表和数据管理层 |
| lib | jar包目录 |
| scripts | 数据库脚本目录 |
| LICENSE | 许可文件 |
| NOTICE | 版本信息 |
| README.txt | 说明文件 |
| RELEASE_NOTES.txt | 更新历史 |

- Hive Cli启动流程

    1. bin/hive-config.sh初始化配置信息

    2. conf/hive-env.sh初始化一些环境变量

    3. 检查hive-exec-\*.jar, hive-metastore-\*.jar, hive-cli-\*.jar几个主要jar包是否存在。

    4. 添加hadoop相关classpath

    5. 执行lib/hive-service-x.x.x.jar

- 下载、解压、重命名Hive

    ```
    [1015146591@bigdata4 apps]$ wget https://mirrors.tuna.tsinghua.edu.cn/apache/hive/hive-1.2.2/apache-hive-1.2.2-bin.tar.gz
    [1015146591@bigdata4 apps]$ tar xzvf apache-hive-1.2.2-bin.tar.gz -C /mnt/home/1015146591/apps
    [1015146591@bigdata4 apps]$ mv apache-hive-1.2.2-bin/ hive-1.2.2
    ```

- 修改配置文件conf/hive-env.sh（**这里主要是配置HADOOP_HOME，如果之前配置过HADOOP_HOME，这里无需配置hive-env.sh**），配置HADOOP_HOME。

    ```
    # 将改行注释去掉，配置正确的Hadoop目录即可
    HADOOP_HOME=${bin}/../../hadoop
    ```

- 配置conf/hive-site.xml（hive-site.xml的优先级高于hive-default.xml的优先级，两者共有的字段只有hive-site.xml中的配置生效）

    复制hive-default.xml.template为hive-site.xml，在configuration中最后添加两个property

    ```conf

    <property>
        <name>system:java.io.tmpdir</name>
        <value>/mnt/home/1015146591/apps/hive-1.2.2/tmp</value>
    </property>

    <property>
        <name>system:user.name</name>
        <value>1015146591</value>
    </property>
    ```

    另外修改一下hive-site.xml中hive.metastore.warehouse.dir的值，这个值表示回头你创建的数据库在HDFS上的存放位置：

    ```
    <property>
        <name>hive.metastore.warehouse.dir</name>
        <value>/user/1015146591/warehouse</value>
        <description>location of default database for the warehouse</description>
    </property>

    ```

- 启动hive（z在hive目录下启动）

    ```
    ./bin/hive
    ```

    ```
    hive> show databases;
    OK
    default
    Time taken: 1.293 seconds, Fetched: 1 row(s)
    ```
