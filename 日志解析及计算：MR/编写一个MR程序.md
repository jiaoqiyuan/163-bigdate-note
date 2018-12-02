# 编写一个MR程序

## 数据需求

用户每天在网站上产生的各种行为，比如浏览，下单，支付等，这些行为都会被网站记录下来，形成用户行为日志，并存储在HDFS上，日志格式如下：

```
2018-06-07 12:27:29.856ΠpageviewΠ{"device_id":"abnbeed", "user_id":"76415","ip":"112.10.180.209","session_id":"3393051","req_url":"http://www.bigdataclass.com/category"}
```

拿到这些日志后，需要将这些字段解析出可以被数据仓库读取的序列化格式。最简单的，存成JSON格式。

    大数据中JSON格式不是最常用的数据格式，这里以JSON格式为例是因为它比较简单，易于处理。

## 开发环境准备

- [IntelliJ IDEA](https://www.jetbrains.com/idea/)：java开发工具

- Xshelll：windows下ssh连接工具

- [FileZilla](https://filezilla-project.org/)：FTP工具（可以使用Xftp替换）

- [Maven](http://maven.apache.org/download.cgi)：项目管理工具

## 本地测试环境准备

- [java1.8环境](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

- [Hadoop2.7.6本地环境](http://www.apache.org/dyn/closer.cgi/hadoop/common/hadoop-2.7.6/hadoop-2.7.6.tar.gz)

- [windows下hadoop环境](https://github.com/xltuo/hadoop-2.7.6-windows-bin)（还是不要用windows下的hadoop啦）

## 本地测试数据

- [测试日志数据](https://gitlab.com/yktceshi/testdata)


------

## 实操环节

- 老师的是Mac环境，我用的是win10，差别不大，而且重要的是win10自带wsl，可以使用linux的命令行，所以还挺方便。

- 创建IDEA项目

    - 打开IDEA -> 选择create new project -> 选择maven -> 填写groupID和ArtifactID -> 填写project name和工程路径 -> finish

    - 打开工程pom.xml文件，添加一些配置信息，具体如下：

        ```xml
        <dependencies>
            <dependency>
                <groupId>org.apache.hadoop</groupId>
                <artifactId>hadoop-client</artifactId>
                <version>2.7.6</version>
                <scope>provided</scope>
            </dependency>
            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>fastjson</artifactId>
                <version>1.2.4</version>
            </dependency>
        </dependencies>

        <build>
            <sourceDirectory>src/main</sourceDirectory>
            <plugins>
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-assembly-plugin</artifactId>
                    <configuration>
                        <descriptorRefs>
                            <descriptorRef>jar-with-dependencies</descriptorRef>
                        </descriptorRefs>
                    </configuration>
                    <executions>
                        <execution>
                            <id>make-assembly</id>
                            <phase>package</phase>
                            <goals>
                                <goal>single</goal>
                            </goals>
                        </execution>
                    </executions>
                </plugin>
            </plugins>
        </build>
        ```
        
        - scope参数表示编译时需要这个jar包但是最终打包时不将这个jar包打包进去。 这样可以使我们的程序打爆出来的jar包比较小。

        - org.apache.maven.plugins的maven-assembly-plugin可以将java程序打包成可执行的jar包。

        - descriptorRef用于设置打包出来的jar包的后缀名。
        
        - execution用于配置运行时的一些参数。

    - 编写MR程序

        - 在src/main/java目录下建立com.bigdata.etl.job包，并在该包内创建ParseLogJob.java。

        - 编写MR程序可以使用Hadoop提供的Maper的抽象类，继承这个抽象类并重写里面的Map方法，就可以实现Map操作了。

    - 打包MR程序

        - 在命令行执行：
            ```
            mvn clean package
            ```

        - 将打包好的程序上传到服务端(使用xftp就行)。

- 在服务端运行MR程序

    - 

