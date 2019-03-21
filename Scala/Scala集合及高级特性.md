# Scala集合类

## 集合

Scala集合集成关系，关键特质如下：

![4][4]

重点关注一下几种集合：

| 集合 | 描述 |
|:----:|:----:|
| List | 元素以线性方式存储，集合中可以存放重复对象 |
| Set | 集合中的元素不按特定方式排序且没有重复对象 |
| Map | 键对象和值对象映射的集合，每一个元素都包含一个键值对 |
| Tuple | 元组是不同类型的值的集合 |
| Option | 表示有可能包含值的容器，也可能不包含值 |

### Set

```scala
val set = Set(1, 2, 3)
println(set.getClass.getName)

println(set.exists(_ % 2 == 0)) //true
println(set.drop(1))    //Set(2, 3)
```

- Set是不重复元素的集合

- Set不保留元素的插入顺序

- 缺省情况下，Set是以HashSet实现的，其元素根据hashCode方法的值进行组织。

- 如果使用的是sortedSet的话，里面存在链式hashSet可以记住元素的插入顺序

可变集合和不可变集合：

- 默认情况下Scala使用的是不可变集合，如果想使用可变集合，需要引入`scala.collection.mutable.Set`包。

- 可变Set和不可变Set都有添加或删除元素的操作，对不可变set进行操作会产生一个新的set，原来的set并没有改变，对可变set进行操作，改变的是该set本身。

### Map

```scala
//Map 初始化
val colors = Map("red" -> "#FF0000", "azure" -> "#F0FFFF")

//空哈希表，键为字符串，值为整数
var A: Map[Char, Int] = Map()
```

- Map是一种可迭代的键值对结构(key/value)

- 所有值都可以通过键来获取。

- Map中的键是唯一的

- Map有可变与不可变之分，可变对象可以修改，不可变对象不能修改

- 默认scala使用不可变的Map，如果要使用可变Map需要显式引入`import scala.collection.mutable.Map` 类

### 元组

```
val t = new Tuple3(1, 3.14, "Fred") //Tuple3表示有元组有3个元素
```

- 元组是不可变的，但是可以包含不同类型的元素

- 元组的值通过将单个值包含在圆括号中构成

- 目前scala支持的元组最大长度是22


## 序列

### 不可变序列

![5][5]

不可变集合中添加新元素会生成一个新集合。

```scala
//vector
//1. 创建Vector对象
var v1 = Vector(1, 2, 3)

//2. 索引Vector
println(v1(0))

//3. 遍历Vector
for(ele <- v1) {
    print(ele + " ")
}

//4. 倒转Vector
var v2 = Vector(1.1, 2.2, 3.3, 4.4)
for(ele <- v2.reverse) {
    print(ele + " ")
}
```

```scala
//range
scala> Array.range(1, 10)
res0: Array[Int] = Array(1, 2, 3, 4, 5, 6, 7, 8, 9)

scala> List.range(1, 10)
res1: List[Int] = List(1, 2, 3, 4, 5, 6, 7, 8, 9)

scala> Vector.range(1, 10)
res2: scala.collection.immutable.Vector[Int] = Vector(1, 2, 3, 4, 5, 6, 7, 8, 9)
```

### 可变序列

![6][6]

Seq是一个特质，返回的是List：

```scala
scala> Seq(1, 2, 3)
res0: Seq[Int] = List(1, 2, 3)
```

```scala
//ArrayBuffer
//如果不想每次都是用全限定名，则可以先导入ArrayBuffer类
import scala.collection.multable.ArrayBuffer
val b = ArrayBuffer[Int]()
b += 1
b += (2, 3, 4, 5)
b ++= Array(6, 7, 8, 9, 10)
b.trimEnd(5)
b.insert(5, 6)
b.remove(1)
b.remove(1, 3)
b.toArray
b.toBuffer
```

## 集合操作

常用操作：

| 操作符 | 描述 | 集合类型 |
|:-----:|:-----:|:------:|
| coll :+ elem / elem +: coll | 有elem被追加到coll集合的尾部或头部 | Seq |
| coll + elem / coll + (e1, e2, ...) | 添加了给定元素的到coll集合中 | Set Map |
| coll - elem / coll - (e1, e2, ...) | 移除coll中指定的元素 | Set, Map, ArrayBuffer |
| coll ++ coll2 / coll2 ++: coll | 两个集合相加，返回包含了两个集合的元素的新集合 | Iterable |
| coll -- coll2 | 从coll中移除coll2中的元素 | Set, Map, ArrayBuffer |
| elem :: lst / lst2 ::: lst | 和+:以及++:的作用相同 | List |
| list ::: list2 | 等同于list ++: list2 | List |
| set | set2 / set & set2 / set & ~set2 | 并集、交集和两个集和的差异。|等同于++，&~等同于-- | Set |
| coll += elem / coll += (e1, e2, ...) / coll++= coll2 / coll -= elem / coll -= (e1, e2, ...) / coll --= coll2 | 通过添加或移除给定元素来修改coll | 可变集合 |
| elem +=: coll / coll2 ++=:coll | 通过向前追加给定元素或集合来修改coll | ArrayBuffer |

其中如果集合有序，可以加入:来确定集合运算后的顺序。=表示对可变集合进行操作

### 追加元素

有先后次序追加：

```scala
scala> val list1 = List(1, 2, 3)
list1: List[Int] = List(1, 2, 3)

scala> val list2 = list1 :+ 4
list2: List[Int] = List(1, 2, 3, 4)

scala> val list3 = 5 +: list2
list3: List[Int] = List(5, 1, 2, 3, 4)

scala> list1 ++ list2
res1: List[Int] = List(1, 2, 3, 1, 2, 3, 4)

scala> list1 ::: list2
res2: List[Int] = List(1, 2, 3, 1, 2, 3, 4)
```

无先后次序追加：

```scala
scala> val set = Set(1, 3, 5, 7)
set: scala.collection.immutable.Set[Int] = Set(1, 3, 5, 7)

scala> set + 2
res3: scala.collection.immutable.Set[Int] = Set(5, 1, 2, 7, 3)
```

构建列表：

```scala
scala> "a" :: "b" :: Nil
res4: List[String] = List(a, b)
```

### 移除元素

set移除元素：

```scala
scala> val set1 = Set(1,2,3,4,5,6,7,8,9)
set1: scala.collection.immutable.Set[Int] = Set(5, 1, 6, 9, 2, 7, 3, 8, 4)

scala> set1 - 9
res5: scala.collection.immutable.Set[Int] = Set(5, 1, 6, 2, 7, 3, 8, 4)

scala> val set2 = Set(1, 2, 3)
set2: scala.collection.immutable.Set[Int] = Set(1, 2, 3)

scala> set1 -- set2
res7: scala.collection.immutable.Set[Int] = Set(5, 6, 9, 7, 8, 4)
```

### 改值操作

可变集合改值操作，就是在刚才不可变集合操作上加个=：

```scala
scala> import collection.mutable.Set
import collection.mutable.Set

scala> val set1 = Set(1, 2, 3)
set1: scala.collection.mutable.Set[Int] = Set(1, 2, 3)

scala> set1 += 4
res8: set1.type = Set(1, 2, 3, 4)

scala> set1 -= 4
res9: set1.type = Set(1, 2, 3)

scala> val set2 = Set(4, 5, 6)
set2: scala.collection.mutable.Set[Int] = Set(5, 6, 4)

scala> set1 ++ set2
res10: scala.collection.mutable.Set[Int] = Set(1, 5, 2, 6, 3, 4)

scala> set1 --= set2
res11: set1.type = Set(1, 2, 3)
```


# Scala高级特性

## 隐式转换

> 隐式转换函数指的是那种以implicit关键字声明的带有单个参数的函数。这样的函数将被自动应用，将值从一种类型转换成另一种类型。

```scala
implicit def int2Fraction (n: Int) = Fraction(n, 1)
```

可以给隐式转换起任何名字，建议使用source2target形式。

### 为什么需要隐式转换？

java中使用final修饰的类是不能对其进行继承，进而对其进行扩展的。要想扩展final修饰的类就需要对其进行包装，先引用进来再进行操作，并提供一些扩展接口出去，这样的代码写起来比较麻烦，而且明确支出扩展类名的话你也不知道要调用哪个类。

Scala中使用隐式转换将final修饰的类隐式转换成另一个类，在原类型的基础上可以直接调用转换后的类的方法，这就避免了java中的问题。


### 隐式转换注意事项

- 对于隐式转换，编译器最关心的是它的类型签名，即它将哪一种类型转换成另一种类型，也就是说它应该只接受一个参数。同一个作用于下，隐式转换函数名不能相同

- 不支持嵌套的隐式转换。

- 隐式转换函数的函数名可以是任意的，与函数名称无关，只与函数签名（函数参数和返回值类型）有关。

- 如果当前作用域中存在函数签名相同但函数名不同的两个隐式转换函数，则在进行隐式转换时会报错。

- 代码能够在不适用隐式转换的前提下能编译通过，就不会进行隐式转换。

### 隐式转换的应用

隐式转换常见的用途就是扩展已有类，在不修改原有类的基础上为其添加新的方法和成员。

```scala
//为java.io.File添加read方法
class RichFile(val from: File) {
    def read = Source.fromFile(from.getPath).mkString
}

implicit def file2RichFile(from: File) = new RichFile(from)
```

以后直接在File类上面调用read方法的时候，会自动把这个类转换成RichFile类，并且调用RichFile的read方法。

### 引入隐式转换

Scala会考虑如下的隐式转换函数：

- 位于源或目标类型的伴生对象中的隐式函数（太难以理解）

- 位于当前作用域可以以单个标识符指代的隐式函数（太难以理解）

通俗来说就是隐式转换可以在文件头（即类的头）进行转换，也可以在方法中引入隐式转换，这就可以限制隐式转换的作用于在哪个位置，这根Scala引入其他类时添加的作用域是一样的，可以在文件的作用域下，可以在类的作用域下，也可以在某个方法的作用域下。比如：

```scala
//引入局部化隐式转换
object Main extends App {
    import com.horstmann.impatient.FractionConversions._
    val result = 3 * Franction(4, 5)    //使用引入的转换
    println(result)
}

//选择特定转换
object FractionConversions {
    ...
    implicit def fraction2Double(f: Fraction) = f.num * 1.0 / f.den
}

//排除特定转换
import com.horstmann.impatient.FractionConversions.{
    fraction2Double => _, _
}   //引入除fratcion2Double外的所有成员
```

### 隐式转换规则

- 当表达式的类型与预期类型不同时：

    ```scala
    sqrt(Fraction(1, 4))    //将调用fraction2Double，因为sqrt预期的是一个Double
    ```
- 当对象访问一个不存在的成员时：

    ```scala
    new File("Readme").read     //将调用file2RichFile，因为File没有read方法
    ```

- 当对象调用某个方法，而该方法的参数声明与传入参数不匹配时：

    ```scala
    * Fraction(4, 5)    //将调用int2Fraction，因为Int的*方法不接受Fraction作为参数
    ```

一下三种情况下编译器不会尝试使用隐式转换：

- 如果代码在不适用隐式转换的前提下能够通过编译

- 编译器不会尝试同时执行多个转换

- 存在二义性的转换是个错误。


[4]: https://github.com/jiaoqiyuan/pics/raw/master/scala/scala_collections.png
[5]: https://github.com/jiaoqiyuan/pics/raw/master/scala/scala_seq.png
[6]: https://github.com/jiaoqiyuan/pics/raw/master/scala/scala_seq1.png