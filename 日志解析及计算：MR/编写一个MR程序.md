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

        - 在src/main/java目录下建立com.bigdata.etl.job包，并在该包内创建[ParseLogJob.java](https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%97%A5%E5%BF%97%E8%A7%A3%E6%9E%90%E5%8F%8A%E8%AE%A1%E7%AE%97%EF%BC%9AMR/etl/src/main/com/bigdata/etl/job/ParseLogJob.java)。

        - 编写MR程序可以使用Hadoop提供的Maper的抽象类，继承这个抽象类并重写里面的Map方法，就可以实现Map操作了。


    - 打包MR程序

        - 在命令行执行：
            ```
            mvn clean package
            ```

        - **本地测试 (这里需要注意一下)** 

            **这里需要注意一下,我在本地也搭建了一个hadoop环境,core-site.xml中默认文件地址配置成本地的了,hdfs和yarn都按照服务端配置了一遍,只不过把所有的bigdat0都换成了localhost,namenode,datanode,resourcemanager,nodemanager相关的目录改写成本地的目录,变成本地配置了.**

            这里老师先是在本地进行测试,可以看到输入路径(input目录)和输出路径(output目录)都是在本地而不是在HDFS上的,一开始我也很困惑,ParseLogJob.java中Path明明使用的是org.apache.hadoop.fs.Path,按说应该是使用HDFS上的目录才对,后来发现应该是老师应该是将core-site.xml中的fs.defaultFS设置成了本地目录,所以最终才会是在本地目录进行操作的.

            感觉这里老师这样做的目的是现在本地做测试,测试一下程序的功能没问题后再放集群去运行,在本地测试一般使用小数据量测试一下功能就行,没必要用HDFS进行存储,所以就配置成本地硬盘存储数据了.

            本地hadoop测试环境的core-site.xml配置文件可以这样配置(/home/jony/ideaprojects是我自己配的默认目录,你们可以随意配置成自己电脑上的目录):
            ```
            <configuration>
                <property>
                        <name>fs.defaultFS</name>
                        <!--
                        <value>hdfs://localhost:33271</value>
                        -->
                        <value>file:/home/jony/ideaprojects</value>
                </property>
            </configuration>
            ```

            不过记得运行时参数中的输入输出路径都是相对于你配置的默认路径来说的,比如你本地测试时运行这个命令:
            ```
            hadoop jar ./etl/target/etl-1.0-jar-with-dependencies.jar com.bigdata.etl.job.ParseLogJob ./input ./output
            ```
            
            这里面的input和output都是相对于/home/jony/ideaprojects这个目录来说的.
        
        - 将打包好的程序上传到bigdata0服务端(使用xftp就行,当然也可以上传到bigdata123服务端)。

- 在服务端运行MR程序

    - 服务端配置的fs.defaultFS都是HDFS上的/user/xxxxx这个目录,xxxx是你的用户名.将测试数据上传到HDFS/user/xxxxx目录下

    - 开启bigdata0123上的namenode和datanode

    - 在bigdata0上运行etl程序:

        ```
        hadoop jar ./jars/etl-1.0-jar-with-dependencies.jar com.bigdata.etl.job.ParseLogJob ./input ./output
        ```
        
        这里我是把打包出来的jar包放到了我新建的jars目录下,input是我在我的HDFS上/user/1015146591目录下创建的目录,里面放的是要测试的数据.
