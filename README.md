# JerryMouse

<div align="center">  
<img src="http://ww1.sinaimg.cn/large/006d4JA0ly1g2epuyjhh8j30ml0n7dgd.jpg" width="150" height="150"/>
</br>
</div>

[Readme in English](https://github.com/apache-foundation/JerryMouse/blob/master/REDME_en.md)
## 简介 
这个项目是一个使用 Java 实现的一个NIO HTTP 服务器，可以解析 GET , POST 等 HTTP 请求
并且支持长连接和支持静态资源的解析，实现了 servlet-api 支持 Servlet 的装载。 
## 特性

* 基于 NIO 的多路复用 Reactor 模型
* Servlet 容器
* 支持请求分发
* 能够解析 GET/POST
* 能够解析 web.xml
* 静态资源支持
* Cookies
* HttpRequest
* HttpResponse
* log4j 日志

## TODO

* 生命周期
* Session 管理
* 热部署 
* 过滤器

## NIO Reactor 模型 

![](http://ww1.sinaimg.cn/large/006d4JA0ly1g24ju3h7iaj30xe0n8abs.jpg)


## 压力测试
使用JMeter进行压力测试：connection:close 以下测试总请求次数都为 20000 次    

2 个线程，每个线程循环访问10000次，吞吐量为个 633 请求/sec



