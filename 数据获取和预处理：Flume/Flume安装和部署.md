## Flume版本

- 0.9.x：统称flume og

- 1.x：统称flume ng

- flume ng经过重大重构，与flume og不能兼容。

## 下载Flume

```
wget http://mirrors.tuna.tsinghua.edu.cn/apache/flume/1.8.0/apache-flume-1.8.0-bin.tar.gz
```

## 安装flume

- 解压flume

```
tar xzvf apache-flume-1.8.0-bin.tar.gz
```

- 配置环境变量，打开~/.bashrc，在文件末尾添加如下内容（FLUME_HOME的具体路径根据你自己解压的flume文件所在路径进行配置）

    ```
    # Flume
    export FLUME_HOME=/mnt/home/1015146591/apps/apache-flume-1.8.0-bin
    export PATH=$FLUME_HOME/bin:$PATH
    ```
    使环境变量生效
    ```
    source ~/.bashrc
    ```

- 修改配置文件

    - 配置flume-env.sh
        
        - 将系统自带的flume-env.sh.template修改为flume-env.sh

        ```
        mv flume-env.sh.template flume-env.sh
        ```

        - 打开配置文件flume-env.sh，配置JAVA_HOME(如果之前配置JDK时已经将JAVA_HOME配置到了~/.bashrc，这里就不需要再配置了)，将JAVA_HOME配置为本地JDK所在路径即可。

    - 配置flume-conf.properties

        - 将系统自带的flume-conf.properties.template改为flume-conf.properties
    
        ```
        mv flume-conf.properties.template flume-conf.properties
        ```

## 启动flume

- 在flume文件夹内开启flume
    ```
    ./bin/flume-ng agent --conf conf --conf-file conf/flume-conf.properties --name agent
    ```

- 查看logs目录下的日志文件：

    ```
    17 Dec 2018 14:32:45,228 INFO  [SinkRunner-PollingRunner-DefaultSinkProcessor] (org.apache.flume.sink.LoggerSink.process:95)  - Event: { headers:{} body: 39 38 35 36 39 30                               985690 }
    17 Dec 2018 14:32:45,228 INFO  [SinkRunner-PollingRunner-DefaultSinkProcessor] (org.apache.flume.sink.LoggerSink.process:95)  - Event: { headers:{} body: 39 38 35 36 39 31                               985691 }
    17 Dec 2018 14:32:45,228 INFO  [SinkRunner-PollingRunner-DefaultSinkProcessor] (org.apache.flume.sink.LoggerSink.process:95)  - Event: { headers:{} body: 39 38 35 36 39 32                               985692 }

    ```