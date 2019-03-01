# 完整编写Map和Reduce

## 完整的Mapper（Reducer）

Mapper和Reducer类调用run方法，润方法分别调用setup，map和cleanup三个方法完成用户操作。

Mapper的run方法:
```java
/**
* Expert users can override this method for more complete control over the
* execution of the Mapper.
* @param context
* @throws IOException
*/
public void run(Context context) throws IOException, InterruptedException {
setup(context);
try {
    while (context.nextKeyValue()) {
    map(context.getCurrentKey(), context.getCurrentValue(), context);
    }
} finally {
    cleanup(context);
}
}
```

Reducer的run方法：

```java
/**
* Advanced application writers can use the 
* {@link #run(org.apache.hadoop.mapreduce.Reducer.Context)} method to
* control how the reduce task works.
*/
public void run(Context context) throws IOException, InterruptedException {
setup(context);
try {
    while (context.nextKey()) {
    reduce(context.getCurrentKey(), context.getValues(), context);
    // If a back up store is used, reset it
    Iterator<VALUEIN> iter = context.getValues().iterator();
    if(iter instanceof ReduceContext.ValueIterator) {
        ((ReduceContext.ValueIterator<VALUEIN>)iter).resetBackupStore();        
    }
    }
} finally {
    cleanup(context);
}
}
```

首先调用setup方法，然后再循环中调用map或Reduce方法，循环结束后调用cleanup方法。

- setup在map方法之前执行，并且只执行一次，利用这个特性，我们可以做以下操作：

    - 初始化资源型操作，比如数据库连接，文件读取等。

    - 抽象出业务共同点，初始化好业务需要的变量。

- cleanup方法在map方法执行之后，他在finally代码块中，所以能保证一定会执行，一般用来释放资源，比如释放数据库资源。

- 重写run方法

    为了满足业务需要，我们可以修改润方法的执行流程。比如map后的操作，或者对异常数据的统一处理。

    ```java
    public void run(Context context) throws IOException, InterruptedException {
        setup(context);
        try {
        while (context.nextKeyValue()) {
            map(context.getCurrentKey(), context.getCurrentValue(), context);
        }
        afterMap(context);
        } catch(Exception e) {
            handleException(context);
            throw e;
        } finally {
            cleanup(context);
        }
    }
    ```

## IP解析

- 案例分析：

    将IP解析成国家，省份，城市的具体工具。

- IP地址库

    集群HDFS上的/user/hadoop/lib/17monipdb.dat

- [IP解析工具类](https://gitlab.com/yktceshi/testdata/blob/master/IPUtil.java)




