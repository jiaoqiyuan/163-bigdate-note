package test

class Point(val xc: Int, val yc: Int) {
    var x = xc
    var y = yc

    def move(dx: Int, dy: Int): Unit = {
        x = x + dx
        y = y + dy

        println("X: " + x)
        println("Y: " + y)
    }
}

class Location(override val xc: Int, override val yc: Int, val zc: Int) extends Point(xc, yc) {
    var z = zc
    def move(dx: Int, dy: Int, dz: Int): Unit = {
        x = x + dx
        y = y + dy
        z = z + dz
        println("x: " + x + " y: " + y + " z: " + z)
    }
}

class Person {
    var name = ""

    override def toString: String = getClass.getName + "[name=" + name + "]"
}

class Employee extends Person {
    var salary = 0.0

    override def toString: String = super.toString + "[salary=" + salary + "]"
}

object ClassTest {

    def main(args: Array[String]): Unit = {
        val pt = new Point(10, 20)
        pt.move(10, 10)

        val loc = new Location(10, 20, 15)
        loc.move(10, 10, 5)

        val fred = new Employee
        fred.name = "Fred"
        fred.salary = 50000
        println(fred)
    }
}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/


class Marker private(val color: String) {
    println("创建" + this)

    override def toString: String = "color: " + color
}

object Marker {
    private val markers: Map[String, Marker] = Map(
        "red" -> new Marker("red"),
        "blue" -> new Marker("blue"),
        "green" -> new Marker("green")
    )

    def apply(color: String) = {
        if (markers.contains(color)) {
            markers(color)
        } else {
            null
        }
    }

    def getMarker(color: String) = {
        if (markers.contains(color)) {
            markers(color)
        } else {
            null
        }
    }

    def main(args: Array[String]): Unit = {
        println(Marker("red"))
        println(Marker.getMarker("blue"))

    }
}
/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

trait Equal {
    def isEqual(x: Any): Boolean
    def isNotEqual(x: Any): Boolean = !isEqual(x)
}

class Point1(xc: Int, yc: Int) extends Equal {
    var x = xc
    var y = yc
    def isEqual(obj: Any) = {
        obj.isInstanceOf[Point1] && obj.asInstanceOf[Point1].y == y
    }
}

object Test {
    def main(args: Array[String]): Unit = {
        val p1 = new Point1(2, 3)
        val p2 = new Point1(2, 4)
        val p3 = new Point1(2, 3)

        println(p1.isEqual(p2))
        println(p1.isNotEqual(p3))
        println(p1.isNotEqual(2))

    }
}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

object Match_Test {
    def matchTest(x: Int): String = x match {
        case 1 => "one"
        case 2 => "two"
        case _ => "many"
    }

    def main(args: Array[String]): Unit = {
        println(matchTest(3))

        val alice = new Person("Alice", 24)
        val bob = new Person("Bob", 32)
        val charlie = new Person("Charlie", 32)

        for (person <- List(alice, bob, charlie)) {
            person match {
                case Person("Alice", 24) => println("Hi Alice")
                case Person("Bob", 32) => println("Hi, Bob")
                case Person(name, age) => println("Age: " + age + " year, name: " + name + " ?")
            }
        }
    }

    case class Person(name: String, age: Int)
}


/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/



import java.io.{File, PrintWriter}

import scala.io.{Source, StdIn}
import scala.util.matching.Regex

object Regex_Test {
    def main(args: Array[String]): Unit = {
        val pattern = "Scala".r
        val str = "Scala is scalable and cool"

        println(pattern.findFirstIn(str))

        val pattern1 = new Regex("(S|s)cala")
        println(pattern1.findAllIn(str).mkString(","))

        val pattern2 = "(S|s)cala".r
        println(pattern2.replaceFirstIn(str, "Java"))


    }
}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

import java.io.IOException
import java.io.{FileNotFoundException, FileReader}
object Exception_Test {
    def main(args: Array[String]): Unit = {
        try {
            val f =  new FileReader("/home/jony/bigdata/openvp")
        } catch {
            case ex: FileNotFoundException => {
                println("Missing file exception")
            }
            case ex: IOException => {
                println("IO Exception")
            }
        } finally {
            println("exiting finally...")
        }
    }
}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

object Extractor_Test {
    def main(args: Array[String]): Unit = {
        println("Applay method: " + apply("Zara", "gmail.com"))
        println("Unapply method " + unapply("Zara@gmail.com"))
        println("Unapply method: " + unapply("Zara Ali"))

        val x = Extractor_Test(5)
        println(x)
        x match {
            case Extractor_Test(num) => println(x + " is " + num + "'s twice bigger")
            case _ => println("无法计算")
        }
    }

    def apply(user: String, domain: String) = {
        user + "@" + domain
    }


    def unapply(str: String): Option[(String, String)] = {
        val parts = str.split("@")
        if (parts.length == 2) {
            Some(parts(0), parts(1))
        } else {
            None
        }
    }

    def apply(x: Int) = x * 2
    def unapply(z: Int): Option[Int] = if (z % 2 == 0) Some(z / 2) else None

}

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

object File_Test {
    def main(args: Array[String]): Unit = {
        val writer = new PrintWriter(new File("/home/jony/scala_file.txt"))
        writer.write("hello scala!")
        writer.close()

        val line = StdIn.readLine()
        println("Input " + line)

        Source.fromFile("/home/jony/scala_file.txt").foreach(print)
    }
}