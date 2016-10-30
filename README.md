# jbean 项目简介

此项目为[jmis项目](https://github.com/cn-cerc/jmis)所调用，同时也可以独立使用。

此项目使用业界成熟的MVC架构，以spring为基础，提供快速开发服务的基础框架服务。

其核对象主要有IForm与IService，二者结合可低成本地实现微服务架构，同时保障系统功能弹性与性能弹性：

* IForm，定位于页面控制器，用于接收web输入，以及输出IPage接口。其中IPage实现有：JspPage、JsonPage、RedirectPage等，可自由扩充。实际编写时，可直接继承AbstractForm后快速实现具体的页面控制器。

* IService，定位于业务逻辑，用于接收web输出，以及输出IStatus与DataSet-JSON，并可透过包装类，转化为其它格式如xml的输出，此项与IForm的差别在于：IForm有提供对getRequest().getSession()的访问，可使用HttpSession。IService有提供RESTful接口，可提供第三方访问。

实际使用时，IForm会调用IService，而IService既对内提供业务服务，也对外提供业务服务。

欢迎大家使用，同时反馈更多的建议与意见，也欢迎其它业内人士，对此项目进行协同改进！

