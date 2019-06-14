package test

object ArrayTest {

    def myArray() = {
        val myList1 = Array(1.9, 2.3, 4.4, 5.6)
        val myList2 = Array(1.1, 2.2, 3.4, 4.6)

        val myList3 = Array.concat(myList1, myList2)

        for (x <- myList3) {
            println(x)
        }

        val myList4 = Array(3, 4, 5, 6)

        for (x <- myList4) {
            println(x)
        }
    }

    def myList() = {
        val site = List("google", "apple", "microsoft")
        val nums = Nil

        println("site's head: " + site.head)
        println("site's tail: " + site.tail)
        println("site is empty? " + site.isEmpty)

        val site1 = List("alibaba", "tencent", "huawei")
        println(List.concat(site, site1))

        val site2 = List.fill(3)("xiaomi")
        println(site2)

        val squares = List.tabulate(6)(n => n*n)
        println(squares)

        val site3 = site1 ++ site2
        println(site3)

        println("hello"::site2) //在列表开头添加元素
        println(site2 :+ "abc")
        println(site2.addString(new StringBuilder, ",").toString())
    }

    def mySet(): Unit = {
        val set = Set(1, 2, 3)
        for (x <- set) {
            println(x)
        }
        println(set.getClass.getName)
        println(set.exists(_ % 2 == 0))
        println(set.drop(2))
        import scala.collection.mutable.Set
        val mutableSet = Set(1, 2, 3)
        mutableSet.add(4)
        println(mutableSet)
        mutableSet -= 2
        println(mutableSet)
    }

    def myMap(): Unit = {
        val colors1 = Map("red" -> "#FF0000",
            "azure" -> "#F0FFFF",
            "peru" -> "#CD853F")

        val nums: Map[Int, Int] = Map()
        println(colors1.keys)
        println(colors1.values)
        println(nums.isEmpty)

        val colors2 = Map("blue" -> "#0033FF",
            "yellow" -> "#FFFF00",
            "red" -> "#FF0000")

        val colors = colors1 ++ colors2
        println(colors)

        colors.keys.foreach { i =>
            print("key = " + i)
            println("\tvalue = " + colors(i))
        }

        println(colors.contains("red"))

        colors.iterator.foreach{i =>
            println("key = " + i._1)
            println("value = " + i._2)
        }
    }

    def myTuple(): Unit = {
        val t = (4, 3, 2, 1)
        val sum = t._1 + t._2 + t._3 + t._4
        println(sum)

        t.productIterator.foreach(i => println("value = " + i))
        println(t.toString())
    }

    def myOption(): Unit = {
        val site = Map("runoob" -> "www.runoob.com", "google" -> "www.google.com")

        println(site.get("runoob"))
        println(site.get("baidu"))

        val a: Option[Int] = Some(5)
        val b: Option[Int] = None

        println(a.getOrElse(0))
        println(a.getOrElse(100))
        println(b.getOrElse(10))
    }

    def myIterator(): Unit = {
        val it = Iterator("baidu", "google", "taobao")
        it.min
    }

    def main(args: Array[String]): Unit = {
        //myList()
        //mySet()
        //myMap()
        //myTuple()
        myOption()
    }
}
