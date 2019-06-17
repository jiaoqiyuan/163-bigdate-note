package learnScala

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

object MyList {

    def sum(ints: List[Int]): Int = ints match {
        case Nil => 0
        case Cons(x, xs) => x + sum(xs)
    }

    def product(ds: List[Double]): Double = ds match {
        case Nil => 1.0
        case Cons(0.0, _) => 0.0
        case Cons(x, xs) => x * product(xs)
    }

    def apply[A](as: A*): List[A] = {
        if (as.isEmpty) Nil
        else Cons(as.head, apply(as.tail: _*))
    }


    def hw3_1() = {
        val x = MyList(1, 2, 3, 4, 5) match {
            case Cons(x, Cons(2, Cons(4, _))) => x
            case Nil => 42
            case Cons(x, Cons(y, Cons(3, Cons(4, _)))) => x + y
            case Cons(h, t) => h + sum(t)
            case _ => 101
        }
        println(x)
    }

    def tail[A](ds: List[A]): List[A] = ds match {
        case Nil => Nil
        case Cons(x, xs) => xs
    }

    def setHead[A](list: List[A], h: A): List[A] = list match {
        case Nil => sys.error("setHead error")
        case Cons(_, t) => Cons(h, t)
    }

    def drop[A](l: List[A], n: Int): List[A] = l match {
        case Nil => sys.error("setHead error")
        case Cons(_, t) => if (n > 1) drop(t, n-1) else t
    }

    def dropWhile[A](l: List[A], f: A => Boolean): List[A] = l match {
        case Nil => sys.error("setHead error")
        case Cons(h, t) => if(f(h)) dropWhile(t, f) else l
        //case _ => l
    }

    def dropWhile1[A](l: List[A])(f: A => Boolean): List[A] = l match {
        case Cons(h, t) if (f(h)) => dropWhile1(t)(f)
        case _ => l
    }

    def hw3_2(): Unit = {
        val x = MyList(1, 2, 3, 4, 5)
        //println(tail(x))
        //println(setHead(x, 10))
        //println(drop(x, 2))
        println(dropWhile(x, (x: Int) => x < 2))

    }

    def append[A](a1: List[A], a2: List[A]): List[A] = a1 match {
        case Nil => a2
        case Cons(h, t) => Cons(h, append(t, a2))
    }

//    def init[A](l: List[A]): List[A] = l match {
//        case Nil => Nil
//        case (_, Nil) => Nil
//        case Cons(h, t) => Cons(h, init(t))
//    }

    def foldRight[A, B](as: List[A], z: B)(f: (A, B) => B): B = as match {
        case Nil => z
        case Cons(x, xs) => f(x, foldRight(xs, z)(f))
    }

    def sum2(ns: List[Int]) = {
        //foldRight(ns, 0)((x, y) => x + y)
        foldRight(ns, 0)(_ + _)
    }

    def product2(ns: List[Double]) = {
        //foldRight(ns, 1.0)((x, y) => x * y)
        foldRight(ns, 1.0)(_ * _)
    }

    def length[A](as:List[A]): Int = {
        foldRight(as, 0)((_, acc) => acc + 1)
    }

    def foldLeft[A, B](list: List[A], value: B)(f: (A, B) => B): B = list match {
        case Nil => value
        case Cons(x, xs) => foldLeft(xs, f(x, value))(f)
    }

    def sum3(list: List[Int]) = {
        foldLeft(list, 0)(_ + _)
    }

    def product3(list: List[Double]) = {
        foldLeft(list, 1.0)(_ * _)
    }

    def length2[A](list: List[A]): Int = {
        foldLeft(list, 0)((_, acc) => acc + 1)
    }

//    def reverse[A](list: List[A]): List[A] = {
//
//    }

    def main(args: Array[String]): Unit = {
//        hw3_1()
        //hw3_2()

        val x = MyList(1, 2, 3, 4, 5)
        val y = MyList(1.0, 2.0, 3.0, 4.0, 5.0)
        println(sum3(x))
        println(product3(y))

    }

}
