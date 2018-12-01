# MR的原理和运行流程

- Map的运行过程

    以HDFS上的文件作为默认输入源为例（MR也可以有其他的输入源）

    ![Map运行过程][1]

    - block是HDFS上的文件块，split是文件的分片（逻辑划分，不包含具体数据，只包含这些数据的位置信息）。

        - 一个split包含一个或多个block，默认是一对一的关系。

        - 一个split不包含两个文件的block， 不会跨越file边界，也就是说一个split是不会跨文件进行划分的。

    - 当分片完成后，MR程序会将split中的数据以K/V（key/value）的形式读取出来，然后将这些数据交给用户自定义的Map函数进行处理。

        - 一个Map处理一个split。

    - 用户用Map函数处理完数据后将处理后，同样将结果以K/V的形式交给MR的计算框架。

    - MR计算框架会将不同的数据划分成不同的partition，数据相同的多个partition最后会分到同一个reduce节点上面进行处理，也就是说一类partition对应一个reduce。

    - Map默认使用Hash算法对key值进行Hash计算，这样保证了相同key值的数据能够划分到相同的partition中，同时也保证了不同的partition之间的数据量时大致相当的，[参考链接][2]

    - 一个程序中Map和Reduce的数量是有split和partition的数据决定的。


- Reduce处理过程

    ![Reduce处理过程][3]

    - Map处理完后，reduce处理程序在各个Map节点将属于自己的数据拷贝到自己的内存缓冲区中

    - 最后将这些数据合并成一个大的数据集，并且按照key值进行聚合，把聚合后的value值作为一个迭代器给用户使用。

    - 用户使用自定义的reduce函数处理完迭代器中的数据后，一般把结果以K/V的格式存储到HDFS上的文件中。

- Shuffle过程

    - 在上面介绍的MR过程中，还存在一个shuffle过程，发生与Map和Reduce之中。

    ![shuffle][4]

    - Map中的shuffle

        - Collec阶段键数据放在环形缓冲区，唤醒缓冲区分为数据区和索引区。

        - sort阶段对在统一partition内的索引按照key值排序。

        - spill（溢写）阶段根据拍好序的索引将数据按顺序写到文件中。

        - Merge阶段将Spill生成的小文件分批合并排序成一个大文件。

    - Reduce中的shuffle

        - Copy阶段将Map段的数据分批拷贝到Reduce的缓冲区。

        - Spill阶段将内存缓冲区的数据按照顺序写到文件中。

        - Merge阶段将溢出文件合并成一个排好序的数据集。

- Combine优化

    - 整个过程中可以提前对聚合好的value值进行计算，这个过程就叫Combine。

    - Combine在Map端发生时间

        - 在数据排序后，溢写到磁盘前，相同key值的value是紧挨在一起的，可以进行聚合运算，运行一次combiner。

        - 再合并溢出文件输出到磁盘前，如果存在至少3个溢出文件，则运行combiner，可以通过min.num.spills.for.combine设置阈值。

    - Reduce端

        - 在合并溢出文件输出到磁盘前，运行combiner。

    - Combiner不是任何情况下都适用的，需要根据业务需要进行设置。


- MR运行过程

    ![MR运行过程][5]

    - 一个文件分成多个split数据片。

    - 每个split由多一个map进行处理。

    - Map处理完一个数据就把处理结果放到一个环形缓冲区内存中。

    - 环形缓冲区满后里面的数据会被溢写到一个个小文件中。

    - 小文件会被合并成一个大文件，大文件会按照partition进行排序。

    - reduce节点将所有属于自己的数据从partition中拷贝到自己的缓冲区中，并进行合并。 

    - 最后合并后的数据交给reduce处理程序进行处理。

    - 处理后的结果存放到HDFS上。


- MR运行在集群上：YARN（Yet Another Resource Negotiator）

    ![YARN结构][6]

    - ResourceManager负责调度和管理整个集群的资源

        - 主要职责是调度，对应用程序的整体进行资源分配

    - Nodemanager负责节点上的计算资源，内部包含Container， App Master，管理Container生命周期，资源使用情况，节点健康状况，并将这些信息回报给RM。

        - Container中包含一些资源信息，如cpu核数，内存大小

        - 一个应用程序由一个App Master管理，App Master负责将应用程序运行在各个节点的Container中，App Master与RM协商资源分配的问题。

    - MapReduce On Yarn

        ![MR on YARN][7]

        - MR程序在客户端启动，客户端会向RM发送一个请求。

        - RM收到请求后返回一个AppID给客户端。

        - 然后客户端拿着AppID，用户名，队列，令牌向RM发出资源请求。

        - 客户端这时会将程序用到的jar包，资源文件，程序运行中需要的数据等传送到HDFS上。

        - RM接收到客户端的资源请求后，分配一个container0的资源包，由NodeManager启动一个AppMaster。

        - RM将集群的容量信息发送给AppMaster，AppMaster计算这个程序需要的资源量后，根据需要想RM请求更多的container。
        
        - 最后由各个NodeManager在节点上启动MapTask和ReduceTask。


[1]: https://github.com/jiaoqiyuan/163-bigdate-note/raw/master/%E6%97%A5%E5%BF%97%E8%A7%A3%E6%9E%90%E5%8F%8A%E8%AE%A1%E7%AE%97%EF%BC%9AMR/img/Map%E8%BF%90%E8%A1%8C%E8%BF%87%E7%A8%8B.png
[2]: https://zhuanlan.zhihu.com/p/42864264
[3]: https://github.com/jiaoqiyuan/163-bigdate-note/raw/master/%E6%97%A5%E5%BF%97%E8%A7%A3%E6%9E%90%E5%8F%8A%E8%AE%A1%E7%AE%97%EF%BC%9AMR/img/Reduce%E5%A4%84%E7%90%86%E8%BF%87%E7%A8%8B.png
[4]: https://github.com/jiaoqiyuan/163-bigdate-note/raw/master/%E6%97%A5%E5%BF%97%E8%A7%A3%E6%9E%90%E5%8F%8A%E8%AE%A1%E7%AE%97%EF%BC%9AMR/img/shuffle%E8%BF%87%E7%A8%8B.png
[5]: https://github.com/jiaoqiyuan/163-bigdate-note/raw/master/%E6%97%A5%E5%BF%97%E8%A7%A3%E6%9E%90%E5%8F%8A%E8%AE%A1%E7%AE%97%EF%BC%9AMR/img/MR%E8%BF%90%E8%A1%8C%E8%BF%87%E7%A8%8B.png
[6]: https://github.com/jiaoqiyuan/163-bigdate-note/raw/master/%E6%97%A5%E5%BF%97%E8%A7%A3%E6%9E%90%E5%8F%8A%E8%AE%A1%E7%AE%97%EF%BC%9AMR/img/YARN%E6%A1%86%E6%9E%B6.png
[7]: https://github.com/jiaoqiyuan/163-bigdate-note/raw/master/%E6%97%A5%E5%BF%97%E8%A7%A3%E6%9E%90%E5%8F%8A%E8%AE%A1%E7%AE%97%EF%BC%9AMR/img/MRonYarn.png
