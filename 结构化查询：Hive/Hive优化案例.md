    SQL写法不同，运行的效率也会出现巨大差异

## 影响运行效率的因素与优化原则

最重要的两个因素（其实也就是影响MR的两个因素，因为Hive的SQL语句最终要转化成MR执行）：

- Job数量的多少

- 数据分布是否均衡

有时数据量不是很大，但是需要关联很多张表时，就需要启动很多个job，那么消耗的时间就比较长。

如果数据不能均匀分布在每个结点，而是某一个节点承担了过多的计算任务，那么在其他节点都完成计算任务后，计算任务较多的节点很长时间都没有计算完成，那么整个任务就卡在了这里。

优化方法：

- 减少Job数量

- 减少数据倾斜

## 分区和裁剪

在日常编写HiveSQL时，当对两表做关联的时候，如果涉及到分区表，那么就尽量将分区字段放到子语句当中，这样，两表在做关联的时候数据量已经得到压缩。

另外在select的时候尽量只选取我们需要的字段，这样可以减少读入字段和分区的数目，节省中间存储和数据整合的开销。应尽量减少select *的使用。

例如下面的SQL语句：

```sql
select 
    t1.user_id,
    gender,
    req_url
from
    (select user_id, req_url from bigdata.weblog where day='2018-05-29') t1
left outer join
    (select user_id, gender from bigdata.member) t2
on t1.user_id=t2.user_id
```

## 避免迪卡尔及积

如果在两表关联时发生多对多的关联，多对多是指关联字段在量表中均多次出现，比如在orders表中，同一个用户可能有多笔订单记录;在用户标签库user_tag_value中同一个用户可能会有多个标签。

这两张表（orders和user_tag_value）以user_id做关联时，就会产生迪卡尔积，就会产生大量的重复记录，这种情况是应该尽量避免的。

**避免多对多关联**

下面这种SQL应该尽量避免：

```sql
select 
    *
from
    (select user_id, pay_amount from bigdata.orders) t1
join
    (select user_id, tag, value from bigdata.user_tag_value) t2
on t2.user_id=t2.user_id
```

## 替代union

多次使用union会导致hive多次执行去重操作，导致运行效率比较低。

```sql
select 
    user_id
from
    (select user_id from ...)
union
    (select user_id from ...)
union
    (select user_id from ...)
```

一个更好的替代方法是使用union all，union all与union的差别是union all不会执行去重操作，相同的记录会得到保留，然后在最外层使用一次去重即可。

```sql
select
    distinct user_id
from
    (select user_id from ...
    union all
    select user_id from ...
    union all
    select user_id from ...)
```

## 减少count distinct

在使用count和distinct的时候，每次使用都会调用一个Reduce任务来完成，一个Reduce任务如果处理的数据量过大会导致整个job难以完成。

```sql
select
    count(distinct user_id)
from
    bigdata.weblog
where
    active_name='order'
```

如果数据量比较大，我们可以使用count...group by来优化：

```sql
select
    user_id,
    count(1)
from
    bigdata.weblog
where
    active_name = 'order'
group by
    user_id
```

