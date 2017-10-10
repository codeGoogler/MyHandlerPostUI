## 你真正了解UI线程更新的几种方式吗——面试必备之UI刷新大解密

我们都知道一般面试的时候都会问道Handler的运行机制。有些时候面试官不会直接问道。但是如果问道UI线程更新的方式有哪几种，你知道多少？



#### 今天我们用demo来探讨一下主线程中更新的几种方式

通过简单的点击按钮来实现更新一张我女神的图片


测试效果：

![效果图.gif](http://upload-images.jianshu.io/upload_images/4614633-aed7e3ceb20638d3.gif?imageMogr2/auto-orient/strip)



首先我们要知道Handler的运行机制：



![Handler运行机制之流程](http://upload-images.jianshu.io/upload_images/4614633-31952ced903b6b35.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



这里不再做过多的讲解。请参考

- [Handler消息机制完全解析（一）Message中obtain()与recycle()的来龙去脉Handler消息机]()

- [Handler消息机制完全解析（二）MessageQueue的队列管理](http://mp.weixin.qq.com/s?__biz=MzI3OTU0MzI4MQ==&mid=2247484369&idx=1&sn=169be204810889ad17689de356b3d31f&chksm=eb476f4fdc30e659b1db1452fa9ff2407bf3df7699589b2e16511805802364d80f0df63b540a&scene=21#wechat_redirect)

- [Handler消息机制完全解析（三）—Handler解析之深入浅出](http://mp.weixin.qq.com/s?__biz=MzI3OTU0MzI4MQ==&mid=2247484449&idx=1&sn=7dec78fff4bd0efa5a5e5dbc3e755f9b&chksm=eb4768bfdc30e1a917f00e7d4683cf18254f1bb69d926e553c3d72a5c485419a97e1ec893d5b&scene=21#wechat_redirect)



#### 第一种方式


![第一种方式.png](http://upload-images.jianshu.io/upload_images/4614633-30a1e562209f17b6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

通过Handler发送post一个Runnable对象来实践，post源码：

![11.png](http://upload-images.jianshu.io/upload_images/4614633-3839cb06eb3a9865.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![111.png](http://upload-images.jianshu.io/upload_images/4614633-01193da5a2c56777.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

 post(Runnable r)----->  sendMessageDelayed(getPostMessage(r), 0);---->sendMessageAtTime。可以看出最终是通过sendMessageDelayed方法进行发送一个对象的，。




#### 第二种方式

![第二种方式.png](http://upload-images.jianshu.io/upload_images/4614633-2e26b90d34d2891d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

第二种方式是通handler的post方法。与第一种方式大同小异。

#### 第三种方式

![第三种方式.png](http://upload-images.jianshu.io/upload_images/4614633-83ab302f9eb5aacb.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

这种方式我们也经常用到。其实每部也是调用的是Handler的post方法，内部代码如下：

![ ](http://upload-images.jianshu.io/upload_images/4614633-891a190e48ecdede.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

先判断当前的线程是否为主线程，如果是当前的线程是主线程，则直接运行，是非主线程的话，调用post方法。

#### 第四种方式

![第四种方式.png](http://upload-images.jianshu.io/upload_images/4614633-5233bbdba861edbe.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

这种方式直接调用的View里面的post

View中的post源码如下：

![](http://upload-images.jianshu.io/upload_images/4614633-613698f658ff4da5.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![](http://upload-images.jianshu.io/upload_images/4614633-b974744c8887bd51.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



![](http://upload-images.jianshu.io/upload_images/4614633-613698f658ff4da5.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


![](http://upload-images.jianshu.io/upload_images/4614633-b974744c8887bd51.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



 很多人都不知道，View自己内部也封装了自己的异步处理机制，从上面就可以看出，调用的是ViewRootImpl.getRunQueue()的post方法，而在Handler内部调用post的时候，先调用的是sendMessageDelayed方法，然后调用sendMessageAtTime方法，紧接着调用enqueueMessage，最终调用的是queue.enqueueMessage，最终执行的方式都是一样的。


最终效果图点击送一个美女：

![效果图.gif](http://upload-images.jianshu.io/upload_images/4614633-aed7e3ceb20638d3.gif?imageMogr2/auto-orient/strip)

> 项目地址：
>
> https://github.com/androidstarjack

### 更多文章

[ 2017上半年技术文章集合—184篇文章分类汇总](http://blog.csdn.net/androidstarjack/article/details/77923753)

[那些年不容错过的智能刷新加载框](http://mp.weixin.qq.com/s?__biz=MzI3OTU0MzI4MQ==&mid=100001024&idx=1&sn=2a01ffd977ca426ae2f91c5db626f4d5&chksm=6b47699e5c30e088c4c3bd7202e33f789307b1c4eea7effdf1872bbb43be952af27e12697eec#rd)

[Android中自定义View坐标系那些事](http://mp.weixin.qq.com/s?__biz=MzI3OTU0MzI4MQ==&mid=100001024&idx=1&sn=2a01ffd977ca426ae2f91c5db626f4d5&chksm=6b47699e5c30e088c4c3bd7202e33f789307b1c4eea7effdf1872bbb43be952af27e12697eec#rd)

[NDK项目实战—高仿360手机助手之卸载监听](http://blog.csdn.net/androidstarjack/article/details/77984865)

[MediaPlayer实现金额的语音播报功能](http://mp.weixin.qq.com/s/EEXYy5_MRiTKuAKb9pX3Fw)

[高级UI特效仿直播点赞效果—一个优美炫酷的点赞动画](http://mp.weixin.qq.com/s?__biz=MzI3OTU0MzI4MQ==&mid=100000969&idx=1&sn=626d821d16346764fdce33e65f372031&chksm=6b4768575c30e14163ae8fb9f0406db0b3295ce47c4bc27b1df7a3abee1fa0bb71ef27b4e959#rd)

[一个实现录音和播放的小案例](http://mp.weixin.qq.com/s?__biz=MzI3OTU0MzI4MQ==&mid=100000959&idx=1&sn=a5acb0f44fbadeaa9351df067438922c&chksm=6b4768215c30e1371a3c750f2b826f38b3a263c937272ae208717f73f92ed3e8fd8b6a674686#rd)


#### 相信自己，没有做不到的，只有想不到的

如果你觉得此文对您有所帮助， 欢迎加入微信公众号：终端研发部

![技术+职场](https://user-gold-cdn.xitu.io/2017/8/1/d354d51a5c58fb8a5ba576f2d9ea7a8e)
