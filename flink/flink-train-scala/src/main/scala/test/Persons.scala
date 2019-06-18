package test

import java.io.{File, PrintWriter}

import scala.io.Source
import scala.util.Random

class Persons {

    var name: String = ""
    val id: String = "1234"
    private var gender: Int = 0

    private[this] var age: Int = 0

    //    def compare(obj: Persons): Int = {
    //        this.age - obj.age
    //    }
}

object Test_Person {
    def main(args: Array[String]): Unit = {
        val person = new Persons()
        println(person.name)
        person.name = "hello"
        println(person.name)
    }
}

/** *******************/

//单例对象不带参数
object Logger {
    def log(msg: String) = {
        println(s"INFO:$msg")
    }
}

//伴生对象
class Logger_test {
    def method = {
        Logger.log("This is a log")
    }
}

object Logger_test {
    def main(args: Array[String]): Unit = {
        val test = new Logger_test
        test.method
    }
}

/** ************************/
//
class AccountInfo {
    var id = AccountInfo.newUniqueNumber()
}

//伴生对象
object AccountInfo {
    private var lastNumber = 10

    private def newUniqueNumber() = {
        lastNumber += 1
        lastNumber
    }

    def main(args: Array[String]): Unit = {
        val accountInfo = new AccountInfo
        println(accountInfo.id)
    }
}

/** ************************/

class User(val name: String, val password: String) {

}

object User {
    def apply(name: String, password: String) = new User(name, password)

    def unapply(arg: User): Option[(String, String)] = {
        if (arg == null) None
        else {
            Some(arg.name, arg.password)
        }
    }

    def main(args: Array[String]): Unit = {
        val obj = new User("zhangsan", "123")
        println(obj.isInstanceOf[User])

        //调用的是伴生对象中的apply方法
        val obj1 = User("zhangsan", "123456")
        println("result: " + obj.isInstanceOf[User])

        obj1 match {
            case User(name, password) => println("name: " + name + " Password: " + password)
            case _ => println("None")
        }
    }
}

/** ***************************/

class Point_1(val xc: Int, val yc: Int) {
    var x = xc
    var y = yc

    def move(dx: Int, dy: Int) = {
        x += dx
        y += dy

        println("x: " + x + " y: " + y)
    }
}

class Localtion_1(override val xc: Int, override val yc: Int, val zc: Int) extends Point_1(xc, yc) {
    var z: Int = zc

    def move(dx: Int, dy: Int, dz: Int) = {
        x += dx
        y += dy
        z += dz

        println("moved to x: " + x + " y: " + y + " z: " + z)
    }
}

object Point_1_Test {
    def main(args: Array[String]): Unit = {
        val obj = new Location(5, 6, 7)

        obj.move(1, 2, 3)

        println(obj.isInstanceOf[Location])

        println(classOf[Location])

    }
}

/** *******抽象类 ************/

abstract class Person_1 {
    var name: String

    def id: Int

    def smile() = {
        println("smile")
    }
}

class Employee_1 extends Person_1 {
    override var name: String = "Jerry"

    override def id: Int = {
        name.hashCode
    }

    override def smile(): Unit = super.smile()
}

//优先使用特质，因为一个类可以混用多个特质，但只能继承一个抽象类
//如果需要构造函数参数，只能使用抽象类，因为特质无法构造带参数的构造函数

/** *******************/
//带有抽象方法的特质
trait Iterator[A] {
    def hasNext: Boolean

    def next: A
}

trait ConsoleLogger {
    def log(msg: String) = {
        println("msg: " + msg)
    }
}

class IntIterator(to: Int) extends Iterator[Int] with ConsoleLogger {

    private var current = 0

    override def hasNext: Boolean = current < to

    override def next: Int = {
        if (hasNext) {
            log("has next")
            val t = current
            current += 1
            t
        } else 0
    }
}

object TraitTest {
    def main(args: Array[String]): Unit = {
        val iterator = new IntIterator(10)
        println(iterator.next)
        println(iterator.next)
        println(iterator.next)

    }
}


/** ************/

trait Logger {
    def log(msg: String)
}

trait ConsoleLogger_1 extends Logger {
    override def log(msg: String): Unit = println(msg)
}

//给日志加上时间戳
trait TimestampLogger extends ConsoleLogger_1 {
    override def log(msg: String): Unit = super.log(s"${java.time.Instant.now()} $msg")
}

//如果日志过长，截断
trait ShorterLogger extends ConsoleLogger_1 {
    val maxLength = 15

    override def log(msg: String): Unit = super.log(
        if (msg.length <= maxLength) msg
        else s"${msg.substring(0, maxLength - 3)} ..."
    )
}

class Account {
    protected var balance: Double = 0.0

}

class SavingAccount extends Account with ConsoleLogger_1 {
    def withdraw(amount: Double) = {
        if (amount > balance) log("insufficent funds.")
        else balance -= amount
    }
}

//测试特质为类提供可以堆叠的改变
object TraitTest1 {
    def main(args: Array[String]): Unit = {

        val acc1 = new SavingAccount with ConsoleLogger_1 with TimestampLogger with ShorterLogger
        val acc2 = new SavingAccount with ConsoleLogger_1 with ShorterLogger with TimestampLogger

        acc1.withdraw(100.0)
        acc2.withdraw(100.0)

        val acc3 = new SavingAccount with ConsoleLogger_1
        acc3.withdraw(100.0)
        val acc4 = new SavingAccount with ConsoleLogger_1 with TimestampLogger
        acc4.withdraw(100.0)
        val acc5 = new SavingAccount with ConsoleLogger_1 with ShorterLogger
        acc5.withdraw(100.0)
    }
}

/** *********************/

trait Logger_1 {
    def log(msg: String)

    def info(msg: String) = {
        log("INFO: " + msg)
    }

    def server(msg: String) {
        log("SERVER: " + msg)
    }

    def warn(msg: String) {
        log("WARN: " + msg)
    }
}

class Account_1 {
    protected var balance: Double = 0.0
}

class SavingAccount_1 extends Account_1 with Logger_1 {
    override def log(msg: String): Unit = println(msg)

    def withdraw(amount: Double) = {
        if (amount > balance) warn("Insufficent funds.")
        else {
            balance -= amount
            info("You withdraw ...")
        }
    }
}

object TratiTest2 {
    def main(args: Array[String]): Unit = {
        val acc = new SavingAccount_1
        acc.withdraw(100.0)
    }
}


/** ******样例类 ***********/

object CaseClassDemo {
    def main(args: Array[String]): Unit = {

        case class Message(sender: String, recipient: String, body: String)

        val message1 = Message("Jerry", "Tom", "Hello")

        println(message1.sender)

        //message1.sender = "kate"

        val message2 = Message("Jerry", "Tom", "Hello")

        println(message1 == message2) //true，说明样例类是基于值进行比较的

        //样例类拷贝，浅拷贝
        val message3 = message1.copy()
        println(message3.sender + message3.recipient + message3.body)
        println(message1 == message3)
        //不完全拷贝,对部分参数赋值
        val message4 = message1.copy(sender = "john")
        println(message4.sender + message4.recipient + message4.body)
    }
}

/** **********************/

object PatternDemo {
    def main(args: Array[String]): Unit = {
        val site = "google.com"
        val GOOGLE = "google.com"
        site match {
            case GOOGLE => println("success")
            case _ => println("fail")
        }

        val list = List(1, 2, 3)
        list match {
            case List(_, _, 3) => println("success")
            case _ => println("failed")
        }
    }
}

object PatternDemo1 {
    def main(args: Array[String]): Unit = {

        abstract class Notification

        case class Email(sender: String, title: String, body: String) extends Notification
        case class SMS(caller: String, message: String) extends Notification
        case class VoiceRecording(contactName: String, link: String) extends Notification

        //做信息的甄别
        def showNotification(notification: Notification): String = {
            notification match {
                case Email(sender, title, body) if (sender == "zhangsan") => "You have an important message from " + sender
                case Email(sender, title, body) => "You have an message from " + sender
                case SMS(caller, message) => "You get a SMS from " + caller
                case VoiceRecording(contactName, link) => "You have a VoiceRecording message from " + contactName
                case _ => "You get a unknown message."
            }
        }

        val email = Email("zhangsan", "important", "hello scala")
        val email1 = Email("lisi", "important", "hello scala")

        println(showNotification(email1))
    }

    val arr = Array("haha", 1, 2.3, 'c')
    val obj = arr(Random.nextInt(4))
    println(obj)
    obj match {
        case x: Int => println(x)
        case s: String => println(s.toUpperCase())
        case d: Double => println(Int.MaxValue)
        case _ => println("others")
    }
}

/** **********偏函数 **********/

object PartialFunctionDemo {
    //create a Partial Function
    val div1 = (x: Int) => 100 / x

    val div2 = new PartialFunction[Int, Int] {
        override def isDefinedAt(x: Int): Boolean = x != 0

        override def apply(x: Int): Int = 100 / x
    }

    val div3: PartialFunction[Int, Int] = {
        case d: Int if (d != 0) => 100 / d
    }

    val res: PartialFunction[Int, String] = {
        case 1 => "one"
        case 2 => "two"
        case _ => "other"

    }

    //orElse 组合多个偏函数
    val r1: PartialFunction[Int, String] = {
        case 1 => "one"
    }
    val r2: PartialFunction[Int, String] = {
        case 2 => "two"
    }
    val r3: PartialFunction[Int, String] = {
        case _ => "other"
    }

    val res1 = r1.orElse(r2.orElse(r3)) //相当于res

    //andThen
    val r4: PartialFunction[Int, String] = {
        case cs if (cs == 1) => "one"
    }
    val r5: PartialFunction[String, String] = {
        case cs if (cs eq "one") => "The number is one."
    }
    val res2 = r4.andThen(r5)


    def main(args: Array[String]): Unit = {
        //div1(0)
        //println(div2.isDefinedAt(1))
        //println(div2.isDefinedAt(0))
        //div2(0)
        //div3(0)

        //        println(res.isDefinedAt(1))
        //        println(res.isDefinedAt(3))
        //        println(res(1))
        //        println(res(3))
        //
        //
        //        println(res1.isDefinedAt(1))
        //        println(res1.isDefinedAt(3))
        //        println(res1(1))
        //        println(res1(3))

        println(res2.isDefinedAt(1))
        println(res2.isDefinedAt(3))
        println(res2(1))
        //        println(res2(3))
    }
}

/** ********密封类 ************/
//使用 sealed 修饰的类或者特质，不能在类定义文件之外定义他的子类
//作用：避免滥用继承；用于模式匹配，便于编译器检查

sealed abstract class Furniture

case class Couch() extends Furniture

case class Chair() extends Furniture

object SealedDemo {
    def findPlaceToSit(furniture: Furniture): String = furniture match {
        case a: Couch => "Lie on the couch"
        case b: Chair => "Sit on the chair"
    }

    val chair = Chair()
    val couch = Couch()

    def main(args: Array[String]): Unit = {
        println(findPlaceToSit(couch))
    }
}

/** **********Option类 **************/
object OptionDemo {
    def main(args: Array[String]): Unit = {
        val map = Map("a" -> 1, "b" -> 2)
        println(map.getOrElse("c", 0))
        println(map.get("a"))

        val a = map.get("c")
        println(a)
    }
}

/** ********字符串插值器 ************/
object StringDemo {
    def main(args: Array[String]): Unit = {
        val name = "Jerry"
        //s插值器
        val res = s"Hello, $name"
        val res1 = s"1+1 = ${1 + 1}"

        println(res)
        println(res1)

        //f插值器
        val height = 1.9d
        val name1 = "Tom"
        val res2 = f"$name1 is $height%2.2f meters tail"
        println(res2)

        //raw插值器，类似于s插值器，但不对其中内容做转换
        val str = s"a\nb"
        println(str)
        val str1 = raw"a\nb"
        println(str1)

    }
}

/** *********文件操作 **************/
object FileDemo extends App {
    //读取行数据
    val source = Source.fromFile("/home/jony/scala_file.txt")
    val lines = source.getLines()
    for (line <- lines) {
        println(line)
    }
    source.close()

    //读取字符
    val source1 = Source.fromFile("/home/jony/scala_file.txt")
    val iter = source1.buffered
    var sum = 0
    while (iter.hasNext) {
        if (iter.head == 'h') {
            sum += 1
        }
        println(iter.next())
    }
    println("sum:" + sum)
    source1.close()

    //读取网络文件
    val source2 = Source.fromURL("https://www.baidu.com")
    val lines2 = source2.getLines()
    for (line <- lines2) {
        println(line)
    }
    source2.close()

    //写文件（JAVA）
    val outpath = new PrintWriter("/home/jony/fileresult.txt")
    for (i <- 1 to 100) {
        outpath.println(i)
    }
    outpath.close()
}

/** ******高阶函数 ********/
object HFunDemo extends App {
    //传入参z数是一个函数
    val arr =  Array(1,2,3,4,5)
    val fun = (x: Int) => x * 2
    val res = arr.map(fun)
    println(res.toBuffer)

    //传入匿名函数
    val res1 = arr.map(_ + 2)
    println(res1.toBuffer)

    //返回值是函数
    def urlBuilder(ssl:Boolean, domainName: String): (String, String) => String = {
        val schema = if (ssl) "https://" else "http://"
        (endpoint: String, query: String) => s"$schema$domainName/$endpoint?$query"
    }

    val domainName = "google.com"
    val getUrl:(String, String) => String = urlBuilder(true, domainName)
    val endpoint = "users"
    val query = "id=1"
    println(getUrl(endpoint, query))
}

/*********闭包**********/
//闭包：函数的返回值依赖于函数外的一个或多个变量
object FunDemo extends App {
    val multiply = (x: Int) => x * 5

    var factor = 5
    val multiply1 = (x: Int) => {
        x * factor
    }

    factor = 10
    println(multiply1(10))

    val multiply2 = (x: Int) => {
        factor += 10
        x * factor
    }

    println(multiply2(10))
    println("factor: " + factor)

}

/**********柯里化***********/
object CurryDemo extends App {
    def add(x: Int, y: Int) = {
        x + y
    }

    println(add(1,2))

    //柯里化
    def curryAdd(x: Int)(y: Int) = x + y
    println(curryAdd(1)(2))

    //模拟柯里化实现过程
    def first(x: Int) = (y: Int) => x + y
    val second = first(1)
    println(second(2))

    val one = curryAdd(1)_
    println(one(3))

    val list = List(1,2,3,4)
    println(list.foldLeft(0)(_ + _))
}

/*********隐式转换*********/

object ImplicitDemo extends App {
    val a: Int = 1
    println(a)

    val map = Map("a" -> 1)
    val list = 1.to(10)

    //使用隐式转换扩展已有类的功能
    //定义隐式类，将File类转换成自定义类RichFile
    implicit class RichFile(file: File) {
        def read: String = Source.fromFile(file).mkString
    }

    val file = new File("/home/jony/scala_file.txt").read
    println(file)

}


/************隐式类***********/
object ImplicitClass {

    //定义隐式类，只能定义在类、trait、object 内部
    implicit class IntWithTimes(x: Int) {
        def times[A](f: => A) = {
            def loop(current: Int): Unit= {
                if (current > 0) {
                    f
                    //递归方法需要指明返回类型
                    loop(current - 1)
                }
            }
            loop(x)
        }
    }

}

import ImplicitClass._
object ImplicitTest extends App {
    10 times println("success")
    10.times(println("100"))
}

/*********隐式函数*********/
object ImplicitDemo1 extends App {
    var a: Int = 10
    var b: Double = 10.9

    b = 100
    b = a

    //自定义一个隐式转换函数，Double => Int
    implicit def DoubleToInt(x: Double): Int = x.toInt
    a = b
    a = 100.99
    println(a)
}

/******隐式参数**********/
object ImplicitDemo3 extends App {

    //自带一特质，带有一个抽象方法
    trait Adder[T]{
        def add(x: T, y: T): T
    }

    //创建一个隐式对象
    implicit val a = new Adder[Int] {
        override def add(x: Int, y: Int): Int = x + y
    }

    //定义带有隐式参数的方法
    def addTest(x: Int, y: Int)(implicit adder: Adder[Int]) = {
        adder.add(x, y)
    }

    println(addTest(1,2))
}

/************actor模型**************
    actor是一个并行计算模型，一切皆actor，actor之间通过消息和邮箱通信
    actor任务调度：1.基于线程 2.基于事件
    actor可以看做一个对象
    actor作用：1.接收消息(mailbox) 2.处理消息：创建actor；指定下一个消息；给其他actor发送消息

  Java多线程编程     vs      scala actor 编程：
  1. 共享数据-锁(synchronized) vs 无
  2. 死锁     vs      无
  3. 线程内部代码顺序执行     vs      actor内部代码顺序执行
  **/

