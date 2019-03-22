> Sqoop依赖于Hadoop，在安装Sqoop之前需要安装Hadoop和JAVA环境。Hadoop的安装可以参考之前的HDFS课程。

## Sqoop包结构

sqoop的安装包结构如下：

```
[1015146591@bigdata4 sqoop-1.4.7.bin__hadoop-2.6.0]$ tree -L 1
.
├── bin                     - sqoop命令文件目录
├── build.xml       
├── CHANGELOG.txt
├── COMPILING.txt
├── conf                    - 配置文件路径
├── docs
├── ivy
├── ivy.xml
├── lib                     - jar包依赖目录
├── LICENSE.txt
├── NOTICE.txt
├── pom-old.xml
├── README.txt
├── sqoop-1.4.7.jar         - sqoop核心jar包
├── sqoop-patch-review.py   - sqoop核心jar包
├── sqoop-test-1.4.7.jar    - sqoop核心jar包
├── src
└── testdata
```

## Sqoop配置与安装

1. 下载Sqooptgz包：[sqoop-1.4.7.bin__hadoop-2.6.0.tar.gz][1]。

2. 解压，修改conf/sqoop-env.sh文件，添加java和hadoop配置。


## 任务示例

......(没啥好说的)

[1]: https://mirrors.tuna.tsinghua.edu.cn/apache/sqoop/1.4.7/sqoop-1.4.7.bin__hadoop-2.6.0.tar.gz