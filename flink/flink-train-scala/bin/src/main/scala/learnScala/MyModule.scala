package learnScala

import scala.annotation.tailrec

object MyModule {
    def abs(n: Int): Int = {
        if (n < 0) -n
        else n
    }

    private def formatAbs(x: Int)= {
        val msg = "The absolute value of %d is %d"
        msg.format(x, abs(x))
    }

    def factorial(n: Int): Int = {
        def go(n: Int, acc: Int): Int = {
            if (n <= 0) acc
            else go(n-1, n*acc)
        }
        go(n, 1)
    }

    def fabonacci(n: Int): Int = {
        if (n == 1) 0
        else if (n == 2) 1
        else fabonacci(n-1) + fabonacci(n-2)

    }

    private def formatFactorial(n: Int) = {
        val msg = "The factorial of %d is %d."
        msg.format(n, factorial(n))
    }

    def findFirst[A](as: Array[A], p: A => Boolean): Int = {
        def loop(n: Int) : Int =
            if (n >= as.length) -1
            else if (p(as(n))) n
            else loop(n + 1)

        loop(0)
    }

    def isSorted[A](as: Array[A], ordered: (A, A) => Boolean) : Boolean = {
        @tailrec
        def go(n: Int): Boolean =
            if (n+1 >= as.length) true
            else if(ordered(as(n), as(n + 1))) go(n+1)
            else false

        go(0)
    }

    def curry[A, B, C](f: (A, B) => C): A => (B => C) = {
        (a: A) => (b: B) => f(a, b)
    }

    def uncurry[A, B, C](f: A => B => C): (A, B) => C = {
        (a: A, b: B) => f(a)(b)
    }

    def compose[A,B,C](f: B => C, g: A => B): A => C = {
        (a: A) => f(g(a))
    }

    def main(args: Array[String]): Unit = {
        //println(formatAbs(-42))
        //println(factorial(5))
        //println(fabonacci(6))
        //println(formatFactorial(7))
        //println(findFirst(Array(7, 8, 9), (x: Int) => x == 9))
        println(isSorted(Array(7, 8, 9), (x1: Int, x2: Int) => x1 < x2))
    }
}
