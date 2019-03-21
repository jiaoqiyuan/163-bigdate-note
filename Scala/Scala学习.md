> 学习环境为Linux，Windows和Mac环境暂时不考虑，以下内容均以Linux为基础。一下记录的是一些学习笔记，可能不太完整，建议参考其他Scala完整教程进行学习。

# Scala入门

## Scala安装

学习Scala需要Java环境，所以需要先安装JDK，关于JDK的安装，这里不再说明，直接下载并设置环境变量即可。

学习Scala的目的就是进行Spark程序开发，因为Spark是基于Scala进行开发的，而且Scala编译出的程序也运行在JVM上，与Java代码可以进行相互调用，但是Scala语法更加灵活，Java也在不断改进语法，向Scala和其他语言的优秀设计思想学习。

这里直接通过IDE的方式进行学习，这也是Scala官网推荐的方法，这里IDE使用的是IDEA社区版，可以下载Scala插件包，很简单，Google一下就能搞定。

安装就这样把！

不过也可以单独下载到本地，解压后执行，就可以进入到命令行模式中，命令行模式跟python解释器的操作有点像，但二者还是有区别的。

python是解释型语言，程序是边解释边运行的，但是Scala是编译性语言，是要先编译再执行的，命令模式下也是快速进行编译后执行的，这是与python本质不同的地方。

### Scala命令行模式初体验

下载了个scala安装包，然后添加环境变量后启动：

```
➜  ~ scala
Welcome to Scala 2.12.8 (Java HotSpot(TM) 64-Bit Server VM, Java 1.8.0_201).
Type in expressions for evaluation. Or try :help.

scala> 8 * 5
res0: Int = 40
```

Scala解释器读取一个表达式进行求值，然后打印出结果，再继续接收下一个表达式，这个过程叫做读取(Read)->求值(Eval)->打印(Print)->循环(Loop)，也就是REPL。

其实说Scala是解释器并不准确，因为它不像Python那样是解释型语言，它接受的表达式都是要编译成字节码然后交给Java虚拟机运行的，所以大部分Scala程序员更倾向于称它为REPL。

## Scala常用类型

<!-- ![](http://latex.codecogs.com/gif.latex?\\frac{1}{1+sin(x)}) -->


| type | range |
|:----:|:-----:|
| Byte | 8bit |
| Short | 16bit |
| Int | 32 |
| Long | 64 |
| Char | 16 |
| String | 字符序列 |
| Float | 32位IEEE754单精度浮点数 |
| Double | 64位IEEE754单精度浮点数 |
| Boolean | true or false |
| Unit | 无值 |
| Null | null或空引用 |
| Nothing | Nothing类型在Scala的类层级的最低端；它是任何其他类型的子类型 |
| Any | 所有其他类的超类 |
| AnyRef | 所有引用类的基类 |

Unit和其他语言中void等同，用作不返回任何结果的方法的结果类型，Unit只有一个实例值，写成()。

与Java不同的是这些类型都是类，没有Java中的基本类型，在Scala中可以直接对这些基础类型调用方法。Scala并不刻意区分基本类型和引用类型。

也就是说在使用Scala编程的过程中不需要关注Java中的基本类型，只需要关注Scala中的类型即可，这些类型是封装好的，可以直接调用类型中的方法进行操作，Scala中不需要包装类型，Java中的Inboxing和Unboxing是在Scala中的编译器中完成的。举例来说，在Scala中创建了一个Int数组，最终在Scala解释器中就会转换成一个int\[\]数组，最终在JVM虚拟机中执行就是int\[\]数组。

### Scala中字符串相关操作

Scala中使用java.lang.String类来表示字符串。

Scala扩展了Scala的String的操作，通过StringOps类给字符串追加了上百种操作。

Scala将String对象**隐式地转换成**StringOps对象，接着StringOps的方法可以被调用。

### Scala中数值类型的相关操作

Scala提供了RichInt，RichDouble，RichChar等操作类。

Scala有BigInt和BigDecimal类，用于任意大小的数字，可以用常规的数学操作符来操作他们。

Scala中不使用强制转换，而是使用方法进行类型的转换。

## 值和变量声明

使用val声明常量，使用var声明变量。

```java
val answer = 0
answer = 1  //错误，answer是常量，不允许修改

var result = 0
result = 1  //正确，result是变量，可以更改
```

声明变量是必须初始化，也就是赋值一个初始值。Scala可以根据初始值自动推断变量类型，这与python类似，但是Scala会在编译阶段检查类型，而Python只有在运行时才会。

声明多个变量：

```java
scala> val min, max =100
min: Int = 100
max: Int = 100

scala> var greeting, message : String = null
greeting: String = null
message: String = null
```

这样将多个变量声明在一行一般不利于阅读，通常情况下还是每行初始化一个值：

```
scala> val min = 10
min: Int = 10

scala> val max = 100
max: Int = 100

scala> var greeting : String = null
greeting: String = null

scala> var message : String = null
message: String = null
```

## Scala函数与方法

方法定义：

```
def 方法名(参数列表):方法返回值类型 = {方法体}
```

其中方法体中可以没有return，方法体最后一行的值就是方法的返回值。如果想在方法体中间返回，就必须使用return。

不带参数的方法调用一般省略圆括号，比如：

```
scala> "hello".distinct
res0: String = helo
```

Scala中没有静态方法，而是使用单例对象和伴生对象实现静态方法和实例方法，这里单例对象是指以object为关键字定义的类，而伴生对象是单例对象在与class对象共存在一个文件中并共享类名的特殊称谓，伴生对象中的就是静态方法，实例对象中的就是实例方法：

```java
class Account {
    //实例方法
}

object Account {
    //静态方法
}
```

函数跟方法不同，函数的定义如下：

```
val 函数名=(参数列表) => {函数体}
```

这里需要说明的是递归函数必须指明函数返回值类型。

方法可以转化成函数，但是函数不能转换成方法：

```java
val 函数名 = 方法名_
```

在需要函数的地方提供一个方法，会自动转换成函数。

方法与函数的区别：

| 函数 | 方法 |
|:----:|:-----:|
| 可以作为单独表达式单独存在 | 只有参数为空的方法可以单独存在 |
| 必须有参数列表 | 可以没有参数列表 |
| 函数名代表函数对象本身 | 方法名大表方法调用 |
| 函数不可转换成方法 | 方法可以转换成函数 |

## 默认参数、带名参数及边长参数

方法定义时如果指定默认参数值，调用时可以不传递该参数：

```scala
scala> def decorate(str:String, left:String = "[", right:String = "]") = left + str + right
decorate: (str: String, left: String, right: String)String

scala> decorate("Hello")
res1: String = [Hello]

scala> decorate("Hello", "<<<", ">>>")
res2: String = <<<Hello>>>
```

传递参数时也可以指定参数名，这种情况下就不需要与参数列表的顺序一致，比如：

```scala
scala> decorate(left="<<<", right=">>>", str="Hello")
res3: String = <<<Hello>>>
```

未命名的参数一定要放在带名字的参数之前：

```scala
scala> decorate("Hello", left="<<<")
res4: String = <<<Hello]
```

定义方法时允许指定最后一个参数可以重复（也就是边长参数）：

```scala
scala> def sum(args : Int*) = {
     | var result = 0
     | for (arg <- args) result += arg
     | result
     | }
sum: (args: Int*)Int

scala> val s  = sum(1, 3, 4, 7, 4)
s: Int = 19
```

## Scala条件表达式

跟Java条件表达式相同，比较特别的地方是Scala中可以将条件表达式的值赋值给变量：

```scala
if (x > 0) s = 1 else s = -1
or
val s = if (x > 0) 1 else -1
```

可以理解为Scala中的if/else将Java中的if/else与条件表达式?:结合在了一起。

如果if和else分支返回的结果类型一致，则表达式的类型就是分支类型；如果if和else分支类型不一致，则表达式类型就是两个分支类型的公共超类型。

```scala
scala> val y = if(x > 0) "positive" else -1
y: Any = positive
```

如果条件表达式只有if没有else，那么else返回的就是空，也就是说每种表达式都要有返回值，即使不写出来，也会返回空，如：

```scala
if(x > 0) 1
等同于
if(x > 0) 1 else ()
```

## Scala循环表达式

分while、do...wihle、for三种，与Java一样。其中Scala的for循环与Java有较大区别：

```
for(i <- range)
range可以使一个数字区间表示，如 i to j，或者 i until j
```

具体使用：

```scala
scala> var sum = 0
sum: Int = 0

scala> for(i <- 1 to 10)
     | sum += i

scala> sum
res6: Int = 55
```

### 嵌套循环

Scala的for循环比Java灵活很多，比如使用Scala实现一个嵌套循环：

```scala
scala> for(i <- 1 to 3; j <- 1 to 3) {
     | println("i = " + i + " j = " + j)
     | }
i = 1 j = 1
i = 1 j = 2
i = 1 j = 3
i = 2 j = 1
i = 2 j = 2
i = 2 j = 3
i = 3 j = 1
i = 3 j = 2
i = 3 j = 3
```

等价于：

```scala
scala> for(i <- 1 to 3) {
     | for(j <- 1 to 3) {
     | println("i = " + i + " j = " + j)
     | }
     | }
i = 1 j = 1
i = 1 j = 2
i = 1 j = 3
i = 2 j = 1
i = 2 j = 2
i = 2 j = 3
i = 3 j = 1
i = 3 j = 2
i = 3 j = 3
```

### 循环守卫

另外在for循环中可以通过条件判断将不想要的数据排除掉：

```scala
scala> for(i <- 1 to 3 if i != 2) {
     | println(i + " ")
     | }
1 
3 
```

等价于：

```scala
scala> for(i <- 1 to 3) {
     | if(i != 2) {
     | println(i + " ")
     | }
     | }
1 
3 
```

### 引入变量

Scala在for循环中还可以引入变量：

```scala
scala> for (i <- 1 to 3; from = 4 - i; j <- from to 3) {
     | print((10 * i + j) + " ")
     | }
13 22 23 31 32 33 
```

等同于：

```scala
scala> for(i <- 1 to 3) {
     | val from = 4 - i;
     | for(j <- from to 3) {
     | print((10 * i + j) + " ")
     | }
     | }
13 22 23 31 32 33
```

### 退出循环

Scala中没有提供break和continue语句来退出训话，一般情况下有三种方法退出循环：

1. 使用Boolean型的控制变量

2. 使用嵌套函数，可以从函数中return。
   
3. 使用Breaks对象中的break方法，这种方法不常用也不推荐使用。

## 异常处理

> 异常是在程序执行期间发生的事件，它会中断正在执行的程序的正常指令流。为了能及时有效处理程序中的运行错误，必须使用异常类。

Scala通过抛出异常方法的方式来终止相关代码的运行，不必通过返回值。

### 异常处理流程

1. 抛出异常
   
2. 系统查找可以接受该异常的异常处理器
   
3. 控制器在离抛出点最近的处理器中恢复

4. 如果没有找到符合要求的异常处理器，则程序退出

与Java不同的是，Scala所有的异常都是Throwable的子类，没有[受检异常][1]。

### 异常语法

抛出异常：

```scala
if(x >= 0) {
    sqrt(x)
} else throw new IllegalArgumentEcxeption("x should not be negative")
```

捕获异常：

```scala
try{
    process(new URL("http://horstann.com/fred-tiny.gif"))
} catch {
    case _: MalformedURLException => println("Bad URL: " + url)
    case ex: IOException => ex.printStackTrace()
}
```

需要注意的是throw表达式有特殊的类型Nothing，另外异常捕获时更通用的异常应该排在具体的异常之后。


## finally语句

try/finally结构中finally语句不管是否抛出异常都会被执行

```scala
val in = new URL("http://horstmann.com/fred.gif").openStream()
try{
    process(in)
} finally {
    in.close()
}
```

try/catch和try/finally结合一起使用：

```
try{...} catch {...} finally {...}
```

# Scala面向对象

## 类的定义：属性及方法

类是具有共同属性和行为的对象的集合。类定义了对象的属性和方法：

```scala
class Counter {
    private var value = 0
    def increment() {value += 1}
    def current() = value
}
```

Scala中的类不声明为public，一个Scala源文件中可以有多个类。

### 类成员的可见性

Scala中也有类似于Java的权限修饰符(public, private, protected):

- Scala类中所有成员的默认可见性为公有，任何作用域内都可以访问公有成员。

- 除了默认的公有可见性，Scala也提供了private和protected，private成员只对本类型和嵌套类型可见，protected成员对本类型和其集成类型都可见。

- 对于private字段，Scala采用与Java类似的getter和setter方法进行读取和修改，但是还稍微有些不同(???)。


### 类的使用

使用类需要做的就是构造对象并按照通常的方式来调动方法：

```scala
val myCounter = new Counter //或new Counter()
myCounter.increment()
println(myCounter.current)
```

`通过在方法定义时不带()来强制方法调用时不加().`

### 带getter和setter的属性

一对getter/setter通常被称做属性：

- Scala为每个字段都提供getter和setter方法

- 以字段age为例，Scala中getter和setter分别是age和age_=

- 任何时候都可以重新定义getter和setter方法。

- Scala可以实现只读属性，但是不能实现只写属性。

### 自定义属性

- var foo:Scala自动合成一个getter和setter

- val foo1:Scala自动合成一个getter

- 自定义foo和foo_=方法

- 只能自定义foo1方法，不能自定义foo1_=方法

将Scala字段标注为@BeanProperty时，会自动生成符合JavaBean规范的getter和setter方法：

| Scala字段 | 生成的方法 | 何时使用 |
|:--------:|:---------:|:-------:|
| val/var name | 公有的name name_=(仅限var) | 实现一个可以被公开访问并且背后是以字段形式保存的属性 |
| @BeanProperty val/var name | 公有的name getName() name_=(仅限于var) setName(...)(仅限于var) | 与JavaBeans互操作 |
| private val/var name | 私有name name_=(仅限于var) | 用于将字段访问限制在本类的方法，就和Java一样。尽量使用private--除非你真的需要一个公有的属性 |
| private[this] val/var name | 无 | 用于将字段访问限制在同一个对象上调用的方法，并不经常用到|
| private[类名] val/var name | 依赖于具体实现 | 将访问权限赋予外部类，并不经常用到 |

## 类构造方法

Scala类的定义主体就是类的构造器，称为主构造器。在类名之后用圆括号列出主构造器的参数列表，主构造器会执行类定义中的所有语句；Scala自动为主构造器的参数列表创建私有字段，并提供对应的访问方法。

如果类名之后没有参数，则该类具备一个无参主构造器。

```scala
class Person(val name: String, val age: Int) {
    //(...)中的内容就是主构造器参数
    ...
}
```

在主构造器参数前加不同的修饰符会生成不同的字段和方法：

| 主构造器参数 | 生成的字段和方法 |
|:----------:|:-------------:|
| name: String | 对象私有字段，如果没有方法使用name，则没有该字段 |
| private val/var name: String | 私有字段，私有的getter/setter方法 |
| val/var name: String | 私有字段，公有的getter/setter方法 |
| @BeanProperty val/var name: String | 私有字段，公有的Scala版和JavaBean版的getter/setter方法 |


Scala类可以包含零个或多个辅助构造器，辅助构造器使用this进行定义，this的返回类型为Unit，每一个辅助构造器的第一行代码必须以一个对先前已定义的其他辅助构造器或主构造器的调用开始。

```scala
class Person {  //无参主构造器
    private var name = ""
    private var age = 0

    def this(name: String) {    //一个辅助构造器
        this()  //调用主构造器
        this.name = name
    }

    def this(name: String, age: Int) {  //另一个辅助构造器
        this(name)  //调用前一个辅助构造器
        this.age = age
    }
}
```

辅助构造器不能使用val和var修饰参数。

## object对象

Scala中object对象的属性和方法默认都是静态的，只有一个实例：

```scala
object Accounts {
    private var lastNumber = 0
    def newUniqueNumber() = {
        lastNumber += 1
        lastNumber
    }
}
```

使用object对象时只需要使用object对象名就可以直接调用了，比如要调用newUniqueNumber方法：`Accounts.newUniqueNumber()`。对象的构造器在该对象第一次被使用时调用，是个懒加载过程。   

object对象不提供构造器参数。

### 单例对象使用场景

- 作为存放工具函数或常量的地方，与Java中静态变量和静态常量一致。

- 高效地共享单个不可变实例

- 需要用单个实例来协调某个服务时(参考单例模式)

### 伴生对象

如果一个单例对象和它的同名类一起出现时，这时的单例对象被称为这个同名类的“伴生对象”，相应的类被称为这个单例对象的“伴生类”。

类和它的伴生对象必须存放在同一个文件中，可以相互访问私有成员。

没有同名类的单例对象被称为孤立对象，一般情况下Scala程序的入口点main方法就是定义在一个孤立对象里。

```scala
//类的伴生对象可以被访问，但是并不在类的作用域中
class Account {
    val id = Account.newUniqueNumber()
    private var balance = 0.0
    def deposit(amount: Double) {
        balance += amount
    }
    ...
}

object Account {
    private var lastNumber = 0
    private def newUniqueNumber() = {
        lastNumber += 1
        lastNumber
    }
}
```

## apply方法

关于scala apply方法的讲解可以参考这个：[Scala学习笔记--apply 方法详解][2].

apply方法调用约定：

> 用括号传递给实例或单例对象名一个或多个参数时，Scala会在相应的类或对象中查找方法名为apply且参数列表与传入的参数一致的方法，并用传入的参数来调用该apply方法。

实例化单例对象时，并没有使用到new，这是怎么做到的呢？这就是apply的作用。

```scala
//通常一个apply方法返回的是半生类的对象
class Account private (val id: Int, initialBalance: Double) {
    private var balance = initialBalance
    ...
}

object Account{
    def apply(initialBanance: Double) = new Account(newUniqueNumber(), initialBalance)
    ...
}
```

### Array(100)和 new Array(100)有什么不同

|  | Array(100) | new Array(100) |
|:---:|:-----------:|:-------------:|
| 调用方法 | apply(100) | this(100) |
| 输出结果 | 输出只有一个元素100的数组 | 输出包含100个null元素的数组 |

### 为什么设计apply方法

- 保持对象和函数之间使用的一致性

- 面向对象：对象.方法   数学：函数(参数)

- Scala中一切都是对象，包括函数也是对象。Scala中的函数既保留括号调用样式，也可以使用点号调用形式，其对应的方法名即为apply。

unapply方法

- unapply方法用于对对象进行解构操作，与apply方法类似，该方法也会被自动调用。

- 可以认为unapply方法是apply方法的反向操作，apply方法接受构造参数变成对象，而unapply方法接受一个对象从中取值。


## 方法重写和字段重写

### 方法重写

Scala中重写一个非抽象方法必须使用override修饰符：

```scala
public class Person {
    ...
    override def toString = getClass.getName + "[name=" + name + "]"
}
```

继承抽象类和特质类时重写方法可以不写override修饰符，钻石结构中重写方法时需要写override修饰符，[参考链接][3]。

override修饰符可以再多种情况下给出错误提示：

- 拼错重写的方法名

- 在新方法中使用了错误的参数类型

- 在超类中引入了新的方法，但是这个新的方法与子类方法相抵触。

### 字段重写

Scala的字段由一个私有字段和取值器/改值器方法构成

```scala
class Person(val name: String) {
    override def toString = getClass.getName + "[name=" + name + "]"
}

class SecretAgent(codename: String) extends Person(codename) {
    override val name = "secret"    //不想暴露真名
    override val toString = "secret"    //或类名
}
```

重写限制：

|  | 用val | 用def | 用var |
|:---:|:---:|:---:| :---: |
| 重写val | 子类有一个私有字段（与超类的字段名字相同）getter方法重写超类的getter方法 | 错误 | 错误 |
| 重写def | 子类有一个私有字段 getter方法重写超类的方法 | 和java一样 | var可以重写getter/setter对。只重写getter会报错 |
| 重写var | 错误 | 错误 | 仅当超类的var是抽象的才可以 |


## 抽象类

### 抽象方法

如果一个类包含没有实现的成员，则必须使用abstract关键字进行修饰，定义为抽象类，该类不能实例化，必须由其子类继承该抽象类后实现相应的成员，才能实例化继承类。

```scala
abstract class Person(val name: String) {
    def id: Int     //没有方法体，这是一个抽象方法
}
```

- 抽象类中，不需要对抽象方法使用abstract关键字，scala会自动判断，只需要省去方法体即可。

- 某类至少存在一个抽象方法，则该类必须声明称抽象类

- 子类中重写超类的抽象方法时，不需要加override关键字

### 抽象字段

抽象字段就是一个没有初始值的字段

```scala
abstract class Person {
    val id: Int     //没有初始化，这是一个带有抽象的getter方法的抽象字段。
    val name: String      //另一个抽象字段，带有抽象的getter和setter方法。
}
```

- 抽象字段必须声明类型

- 子类重写抽象字段时不需要写override关键字。

## trait特质

Java中是不允许多重继承的，Scala也不允许，多重继承如下所示：

```
          Person
     -------|--------
    |                |
Student          Employee
    |                |
     -------|--------
    Teaching Assistant
```

TA(Teaching Assistant)无法同时继承Student和Employee，但是Scala中引入了一个叫做特质(trait)的东西来实现多重继承。

特质用于在类之间共享程序接口和字段，类似于Java的接口。

类和对象可以扩展特质，但是特质不能被实例化，因此特质没有参数。

```scala
trait Logger {
    def log(msg: String)       //这个是抽象方法
}
```

Java中一个类是可以实现多个接口的，在Scala中一个类可以实现多个特质，特质跟Java的接口作用一摸一样。

### 当作接口使用的特质

- 特质中未被实现的方法默认为抽象方法。

- 重写特质的抽象方法不需要加override关键字。

- 使用特质时用extends关键字。

- 需要多个特质时，用with关键字来添加额外的特质。

- Scala类中只能有一个超类，但是可以有任意数量的特质。

特质与Java中接口的不同是，特质中的方法不需要一定是抽象的，也可以有具体实现，但是让特质拥有具体行为存在一个弊端，那就是当特质改变时，所有混入了该特质的类都需要重新编译。

```scala
trait ConsoleLogger {
    def log(msg: String) {
        println(msg)
    }
}
```

### 继承类的特质

特质也可以继承类，特质继承类时，这个类会自动成为所有混入该特质的超类。

如果特质继承的类扩展了另一个类，那么只有另一类是特质的超类的一个子类才可以混入该特质。

LoggedException是一个特质，它继承了Logged和Exception这两个类

```scala
class UnhappyException extends IOException with LoggedException

class UnhappyFrame extends JFrame with LoggedException  //错误
```

### 带有特质的对象

- 在构造单个对象时，可以为其添加特质。

- 特质可以将对象原本没有的方法与字段加入对象中。

- 如果特质和对象改写了同一个超类的方法，则排在右边的先被执行。



```scala
trait Logged {
    def log(msg: String) { }
}

class SavingsAccount extends Account with Logged {
    def withdeaw(amount: Double) {
        if(amount > balance) log("Insufficient funds")
        else
        ...
    }
}
```

### 特质中的字段

- 特质中的字体可以是具体的也可以是抽象的，如果有初始值那么字段就是具体的。

- 通常对于特质中每一个具体字段，使用该字段的类都会获得一个字段与之对应，这些字段不是被继承的，他们只是简单地加到了子类中。

- 特质中未被初始化的字段在具体的子类中必须被重写。

### 自身类型与结构类型(不理解)

- 带有自身类型的特质只能被混入指定类型的子类

- 结构类型只给出类必须拥有的方法而不是类的名称。

```scala
trait LoggedException extends Logged {
    this: Exception => def log() {
        log(getMessage())
    }
}
```

特质即实现了Java的接口功能，又实现了抽象类的功能，在Scala中还是比较常见的。

## case class 样例类

> case class 是一种特殊的类，经过优化后可以被用于模式匹配。

case class的声明如下：

```scala
abstract class Amount
case class Dollar(value: Double) extends Amount
case class Currency(value: Double, unit: String) extends Amount
```

声明case class时可以直接使用类名加参数的形式，此时会自动发生如下事件：

- 构造器中每一个参数都成为val，除非被显式地声明为var

- 在伴生对象中提供apply方法可以不用new关键字就能构造出相应的对象。

- 提供unapply方法让模式匹配可以工作

- 生成toString, equals, hashCode和copy方法。

### copy方法和带名参数

样例类的copy方法创建一个与现有对象值相同的新对象，可以用带名参数修改某些属性。

```scala
val amt = Currency(29.95, "EUR")
val price = amt.copy()
val price = amt.copy(value = 19.95)     //Currency(19.95, "EUR")
```

### 样例类的密封

当case class的超类使用关键字sealed修饰，则编译器会校验对该超类对象的模式匹配规则中，是否列出了可能的子case类，且该超类的子类只能出现在超类的文件中，形成封闭，而不能出现在其他文件中。

```scala
sealed abstract class Amount
case class Dollar(value: Double) extends Amount
case class Currency(value: Double, unit: String) extends Amount
```

## 模式匹配

语法：变量 match {case 值 => 代码}

如果case值为下划线，则代表不满足以上所有情况。match case中只要一个case分支满足条件并处理了，就不会继续判断下一个case分支了。

```scala
var sign = ...
val ch: Char = ...
ch match {
    case '+' => sign = 1
    case '-' => sign = -1
    case _ => sign = 0
}
```

Scala模式匹配不会意外调入下一个分支。

在case后的条件判断中可以在值后面加一个if条件，进行双重过滤，比如：

```scala
ch match {
    case '+' => sign = 1
    case '-' => sign = -1
    case _ if Character.isDigit(ch) => digit = Character.digit(ch, 10)
    case _ => sign = 0
}
```

**if后的条件可以是任意类型的Boolean类型**。

如果case关键字后面跟着一个变量名，那么匹配到的这个变量值会被赋值到后面的表达式中：

```scala
str(i) match {
    case '+' => sign = 1
    case '-' => sign = -1
    case ch => digit = Character.digit(ch, 10)
}
```

case还可以对类型进行模式匹配，case 变量 : 类型 => 代码

```scala
obj match {
    case x: Int => x
    case s: String => Integer.parseInt(s)
    case _: BigInt => Int.MaxValue
    case _ => 0
}
```

case还可以匹配数组、列表和元组。

```scala
//匹配数组
arr match {
    case Array(0) => "0"        //匹配到数组只有一个元素0
    case Array(x, y) => x + " " + y     //匹配到数组有两个元素
    case Array(0, _*) => "0..."     //匹配到数组第一个元素是0，后面有不确定个元素、
    case _ => "something else"
}

//匹配元组
pair match{
    case (0, _) => "0 ..."      //匹配到第一个元素是0的元组
    case (y, 0) => y + " 0"     //匹配到末尾元素是0的元组
    case _ => "neither is 0"    
}

//列表
lst match {
    case 0 :: Nil => "0"                //匹配到列表只有一个元素0
    case x :: y :: Nil => x + " " + y   //匹配到列表有两个元素
    case 0 :: tail => "0 ..."           //匹配到列表0开头的列表
    case _ => "somthing else"
}
```

# Scala集合类

## 集合

```mermaid
graph Collection;
    <trait>Iterable --> <trait>Seq;
```

## 序列

## 集合操作

# Scala高级特性

## 隐式转换



[1]: https://blog.csdn.net/j754379117/article/details/41966337
[2]: https://blog.csdn.net/shenlei19911210/article/details/78538255
[3]: https://www.cnblogs.com/yjf512/p/8026611.html