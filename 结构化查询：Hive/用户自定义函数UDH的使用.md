Hive自带了很多函数，但是还是有些业务场景是hive自带函数无法满足的，这时就需要用根据业务户编写自定义函数以满足业务需要。

## 根据用户生日解析星座

Hive中并没有将用户生日解析成星座的函数，如果不自定义函数，就需要在sql中写一堆case...when...进行判断，就如：

```sql
case...when...
case...when...
case...when...
case...when...
case...when...
case...when...

```

这种方式可以实现功能，但是每次在这种场景下都要书写这没多sql语句，很麻烦。

可以将这些语句封装成用户自定义函数，每次需要将用户生日转换成星座的时候调用这个自定义的函数就可以了，省力，也不容易出错。

## 用户自定义函数UDF

用户自定义函数创建步骤：

- 继承UDF类，并实现evaluate函数

- 编译成jar包

- hive会话中加入jar包

- create function定义函数

## 实例

1. 在MR课程创建的工程中的pom中添加相关依赖，如下：

    ```xml
    <dependency>
        <groupId>org.apache.hive</groupId>
        <artifactId>hive-exec</artifactId>
        <version>1.2.1</version>
        <scope>provided</scope>
    </dependency>
    ```

    为防止中文出现乱码，在pom中指定编码格式为UTF-8:

    ```xml
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.encoding>UTF-8</maven.compiler.encoding>
    </properties>

    ```

2. 编写计算星座的函数：

    ```java
    package com.bigdata.etl.udf;

    import org.apache.hadoop.hive.ql.exec.UDF;
    import java.util.Calendar;
    import java.util.Date;
    import java.text.SimpleDateFormat;

    public class UDFZodiac extends UDF {
        private SimpleDateFormat df;
        public UDFZodiac() {
            df = new SimpleDateFormat("yyyy-MM-dd");
        }

        public String evaluate(Calendar bday) {
            return this.evaluate(bday.get(Calendar.MONTH + 1), bday.get(Calendar.DAY_OF_MONTH));
        }

        public String evaluate(String bday) {
            Date date = null;
            try {
                date = df.parse(bday);
            } catch (Exception e) {
                return null;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return this.evaluate(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        }

        public String evaluate(Integer month, Integer day) {
            if (month == 1) {
                if (day < 20) {
                    return "摩羯座";
                } else {
                    return "水瓶座";
                }
            }
            if (month == 2) {
                if (day < 19) {
                    return "水瓶座";
                } else {
                    return "双鱼座";
                }
            }
            if (month == 3) {
                if (day < 21) {
                    return "双鱼座";
                } else {
                    return "白羊座";
                }
            }
            if (month == 4) {
                if (day < 20) {
                    return "白羊座";
                } else {
                    return "金牛座";
                }
            }
            if (month == 5) {
                if (day < 21) {
                    return "金牛座";
                } else {
                    return "双子座";
                }
            }
            if (month == 6) {
                if (day < 21) {
                    return "双子座";
                } else {
                    return "巨蟹座";
                }
            }
            if (month == 7) {
                if (day < 23) {
                    return "巨蟹座";
                } else {
                    return "狮子座";
                }
            }
            if (month == 8) {
                if (day < 23) {
                    return "狮子座";
                } else {
                    return "处女座";
                }
            }
            if (month == 9) {
                if (day < 23) {
                    return "处女座";
                } else {
                    return "天秤座";
                }
            }
            if (month == 10) {
                if (day < 23) {
                    return "天秤座";
                } else {
                    return "天蝎座";
                }
            }
            if (month == 11) {
                if (day < 23) {
                    return "天蝎座";
                } else {
                    return "射手座";
                }
            }
            if (month == 12) {
                if (day < 23) {
                    return "射手座";
                } else {
                    return "摩羯座";
                }
            }
            return null;
        }
    }
    ```

3. 打包程序上传到bigdata4,并上传到HDFS上：

    这里我在打包的时候出了个问题，提示：

    ```
    [ERROR] Failed to execute goal on project etl: Could not resolve dependencies for project netease.bigdata.course:etl:jar:1.0: Could not find artifact org.pentaho:pentaho-aggdesigner-algorithm:jar:5.1.5-jhyde in nexus-aliyun (http://maven.aliyun.com/nexus/content/groups/public) -> [Help 1]
    ```

    我把mvn从阿里的源改为官方的后可以正常打包了，如果有相关问题可以参考一下这个方法。

    ```
    [1015146591@bigdata4 ~]$ hadoop fs -mkdir /user/1015146591/hive/jar
    [1015146591@bigdata4 ~]$ hadoop fs -put jars/etl-1.0-jar-with-dependencies.jar /user/1015146591/hive/jar/
    [1015146591@bigdata4 ~]$ hadoop fs -ls /user/1015146591/hive/jar
    Found 1 items
    -rw-r-----   3 1015146591 supergroup     439380 2019-01-08 10:16 /user/1015146591/hive/jar/etl-1.0-jar-with-dependencies.jar
    [1015146591@bigdata4 ~]$ mvn clean package
    ```

4. 启动hive，在会话窗口中加载刚刚上传的jar包：

    ```
    hive> add jar hdfs:/user/1015146591/hive/jar/etl-1.0-jar-with-dependencies.jar;
    converting to local hdfs:/user/1015146591/hive/jar/etl-1.0-jar-with-dependencies.jar
    Added [/mnt/home/1015146591/apps/hive-1.2.2/tmp/91095efc-87b4-4b5c-b52d-3e0e886f69ff_resources/etl-1.0-jar-with-dependencies.jar] to class path
    Added resources: [hdfs:/user/1015146591/hive/jar/etl-1.0-jar-with-dependencies.jar]
    ```

5. 创建临时函数：

    ```sql
    hive> create temporary function udf_zodiac as 'com.bigdata.etl.udf.UDFZodiac';
    OK
    Time taken: 0.78 seconds
    ```

6. 在查询语句中使用刚才编写的函数：

    ```sql
    select 
        user_id,
        from_unixtime(cast(birthday/1000 as bigint), 'yyyy-MM-dd'),
        udf_zodiac(from_unixtime(cast(birthday/1000 as bigint), 'yyyy-MM-dd'))
    from
        bigdata.member
    limit 20;
    ```

    查看运行结果：

    ```sql
    hive> select 
        >     user_id,
        >     from_unixtime(cast(birthday/1000 as bigint), 'yyyy-MM-dd'),
        >     udf_zodiac(from_unixtime(cast(birthday/1000 as bigint), 'yyyy-MM-dd'))
        > from
        >     bigdata.member
        > limit 20;
    OK
    0001528166058390	1992-04-26	金牛座
    0001531897045258	1984-02-05	水瓶座
    0001531897084097	1979-01-15	摩羯座
    0001531897369998	1999-02-23	双鱼座
    0001531898051786	1983-06-15	双子座
    0001531898202634	1982-11-11	天蝎座
    0001531898252101	1986-12-26	摩羯座
    0001531898353851	2001-05-16	金牛座
    0001531898480773	1995-01-10	摩羯座
    0001531898489619	1998-03-26	白羊座
    0001531898533512	1997-02-13	水瓶座
    0011528165770489	1976-09-30	天秤座
    0011528165983913	1992-01-02	摩羯座
    0011528166026392	1989-10-26	天蝎座
    0011528166029056	1974-07-28	狮子座
    0011528166125503	1983-02-26	双鱼座
    0011528166158037	1995-01-15	摩羯座
    0011528166191049	1985-04-20	金牛座
    0011528166197979	2001-02-12	水瓶座
    0011531897025121	2001-04-22	金牛座
    Time taken: 1.258 seconds, Fetched: 20 row(s)
    ```

可以看到用户星座已经被成功解析出来了，也就是我们自己编写的函数可是正常使用了。

## 工程代码

工程代码：[**点这里**][1] 。

[1]: https://github.com/jiaoqiyuan/163-bigdate-note/tree/master/%E7%BB%93%E6%9E%84%E5%8C%96%E6%9F%A5%E8%AF%A2%EF%BC%9AHive/etl