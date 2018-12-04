# Hadoop的IO模型-下

实际应用场景中要解析的字段一般都会很多，如果全部都重写一遍序列化操作工作量会很大，可以编写一个通用的序列化接口来避免重复编写序列化代码。可以考虑以下方法：

- 使用map，将所有字段都存放到map中，然后将map传递到reduce端，而且Hadoop框架也提供有MapWritable类型。但是这里有个问题，map是将key值和value值一起存放到map中，每次传输都会把key值信息也传递出去，但其实key值的信息是固定范围内的那些字段值：device_id,ip,order_ip...等。这会造成大量的网络浪费。不过这种方式编程简单，只需要把值放入这个map集合中的即可。

- 只传输value值，，不传输key值。可以使用array将这些value值传输端，但是需要保证每一个value值的相对位置是一定的，不能发生错乱，否则会导致解析错乱。可以自定义一个通用类实现这个功能。




## 代码

都在视频里讲了，看视频吧，多研究一下代码。

代码结构：
```
├── job
│   └── ParseLogJob.java
└── mr
    ├── LogBeanWritable.java
    ├── LogFieldWritable.java
    └── LogGenericWritable.java
```

链接：

[ParseLogJob.java][1]

[LogBeanWritable.java][2]

[LogFieldWritable.java][3]

[LogGenericWritable.java][4] 


[1]: https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%97%A5%E5%BF%97%E8%A7%A3%E6%9E%90%E5%8F%8A%E8%AE%A1%E7%AE%97%EF%BC%9AMR/Hadoop%E7%9A%84IO%E6%A8%A1%E5%9E%8B-%E4%B8%8B/etl/src/main/com/bigdata/etl/job/ParseLogJob.java
[2]: https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%97%A5%E5%BF%97%E8%A7%A3%E6%9E%90%E5%8F%8A%E8%AE%A1%E7%AE%97%EF%BC%9AMR/Hadoop%E7%9A%84IO%E6%A8%A1%E5%9E%8B-%E4%B8%8B/etl/src/main/com/bigdata/etl/mr/LogBeanWritable.java
[3]: https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%97%A5%E5%BF%97%E8%A7%A3%E6%9E%90%E5%8F%8A%E8%AE%A1%E7%AE%97%EF%BC%9AMR/Hadoop%E7%9A%84IO%E6%A8%A1%E5%9E%8B-%E4%B8%8B/etl/src/main/com/bigdata/etl/mr/LogFieldWritable.java
[4]: https://github.com/jiaoqiyuan/163-bigdate-note/blob/master/%E6%97%A5%E5%BF%97%E8%A7%A3%E6%9E%90%E5%8F%8A%E8%AE%A1%E7%AE%97%EF%BC%9AMR/Hadoop%E7%9A%84IO%E6%A8%A1%E5%9E%8B-%E4%B8%8B/etl/src/main/com/bigdata/etl/mr/LogGenericWritable.java