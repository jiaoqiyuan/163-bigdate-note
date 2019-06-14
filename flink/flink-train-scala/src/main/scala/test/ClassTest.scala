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

