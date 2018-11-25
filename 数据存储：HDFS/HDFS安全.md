# HDFS安全

- kerberos
    
    - Kerberos是一种计算机网络认证协议，用来在非安全网络中对个人通信以安全的手段进行身份认证。
    
    - 基于该协议的系统采用客户端/服务端结构，并且能够进行相互认证，即客户端和服务端均可以对对方进行身份认证。可以用于防窃听、防止[重放攻击][1]、保护数据完整性等场合，是一种应用对称密钥体制进行密钥管理的系统。也可以通过第三方插件使用公开密钥加密方法进行认证。

    - 目前支持Kerberos协议的系统有[MIT][2]/[Heimdal Kerberos][3]、[Active Directory][4]


- 一些术语：

    - [KDC][5] —— 密钥分发中心，包括认证服务和票据授予服务等模块（包括数据库）

    - Keytab —— 密钥文件，存储用户的密钥信息

    - Principal —— 与用户含义类似的Kerberos术语，但包含更多实体，如服务器、网络服务等。

    - ticket —— 票据，服务器上包含认证信息的一类缓存文件

    - [TGT][6](ticket-granting ticket) —— 票据许可票据，由认证服务器发送给客户端的票据。客户端一旦持有这个票据，就可以认为客户端已经通过了Kerberos认证。

    - [TGS][7](Ticket granting server) —— 票据许可服务，由票据授予服务模块在验证TGT后发送给客户端的票据，持有这个票据后就能连接远程服务端的特定服务。

-  [Kerberos访问流程][8]
    
    ![Kerberos访问流程图][9]

    - 客户端首先向KDC发起认证请求，获取TGT票据

    - 客户端在向HDFS等服务发起请求前，会想KDC申请这些服务器的TGS票据

    - 客户端通过TGS票据来连接远程的HDFS RPC服务

    - 完成认证后，NN会为客户端的盖茨请求生成单独的Token，用于后续与DN节点的通信和访问授权。

        tips：这里的TGT和TGS都是由KDC生成的，由Kerberos和KDC进行配置，有有效期。token也有有效期，但是token是由namenode配置的。

- 配置HDFS安全和Kerberos的相关参数

    ![相关参数][10]




[1]: https://baike.baidu.com/item/%E9%87%8D%E6%94%BE%E6%94%BB%E5%87%BB
[2]: https://web.mit.edu/kerberos/
[3]: https://www.h5l.org/
[4]: https://docs.microsoft.com/en-us/windows-server/identity/ad-ds/get-started/virtual-dc/active-directory-domain-services-overview
[5]: https://docs.microsoft.com/en-us/windows/desktop/secauthn/key-distribution-center
[6]: https://gerardnico.com/security/kerberos/tgt
[7]: https://www.techopedia.com/definition/27186/ticket-granting-server-tgs
[8]: https://blog.csdn.net/mm_bit/article/details/50788709
[9]: https://github.com/jiaoqiyuan/163-bigdate-note/raw/master/%E6%95%B0%E6%8D%AE%E5%AD%98%E5%82%A8%EF%BC%9AHDFS/img/Kerberos%E8%AE%BF%E9%97%AE%E6%B5%81%E7%A8%8B.png
[10]: https://github.com/jiaoqiyuan/163-bigdate-note/raw/master/%E6%95%B0%E6%8D%AE%E5%AD%98%E5%82%A8%EF%BC%9AHDFS/img/HDFS%E5%AE%89%E5%85%A8%E7%9B%B8%E5%85%B3%E5%8F%82%E6%95%B0.png
