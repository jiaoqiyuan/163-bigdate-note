> 学习环境为Linux，Windows和Mac环境暂时不考虑，以下内容均以Linux为基础。一下记录的是一些学习笔记，可能不太完整，建议参考其他Scala完整教程进行学习。

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

