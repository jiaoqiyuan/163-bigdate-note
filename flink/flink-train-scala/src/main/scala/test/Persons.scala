package test

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

/*********************/

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

/**************************/
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

/**************************/

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

/*****************************/

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

        obj.move(1, 2,3)

        println(obj.isInstanceOf[Location])

        println(classOf[Location])

    }
}

/*********抽象类************/

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

/*********************/
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
        if(hasNext) {
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


/**************/

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

/***********************/

trait Logger_1 {
    def log(msg: String)
    def info(msg:String) = {
        log("INFO: " + msg)
    }

    def server(msg: String)  {log("SERVER: " + msg)}
    def warn(msg: String)  {log("WARN: " + msg)}
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


/********样例类***********/

object CaseClassDemo {
    def main(args: Array[String]): Unit = {

        case class Message(sender:String, recipient:String, body:String)

        val message1 = Message("Jerry", "Tom", "Hello")

        println(message1.sender)

        //message1.sender = "kate"

        val message2 = Message("Jerry", "Tom", "Hello")

        println(message1 == message2)   //true，说明样例类是基于值进行比较的

        //样例类拷贝，浅拷贝
        val message3 = message1.copy()
        println(message3.sender + message3.recipient + message3.body)
        println(message1 == message3)
        //不完全拷贝,对部分参数赋值
        val message4 = message1.copy(sender = "john")
        println(message4.sender + message4.recipient + message4.body)
    }
}

/************************/

object PatternDemo {
    def main(args: Array[String]): Unit = {
        val site = "google.com"
        val GOOGLE = "google.com"
        site match {
            case GOOGLE => println("success")
            case _ => println("fail")
        }

        val list = List(1,2,3)
        list match {
            case List(_, _, 3) => println("success")
            case _ => println("failed")
        }
    }
}