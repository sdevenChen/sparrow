#时间轮算法
时间轮是一种实现延迟功能（定时器） 的巧妙算法。 如果一个系统存在大量的任务调度，时间轮可以高效的利用线程资源来进行批量化调度。 
把大批量的调度任务全部都绑定时间轮上，通过时间轮进行所有任务的管理，触发以及运行。 能够高效地管理各种延时任务，周期任务，通知任务等。

[![时间轮示意](https://ask.qcloudimg.com/http-save/yehe-4831957/1lte1iq5fh.png?imageView2/2/w/1620 "时间轮示意")](https://ask.qcloudimg.com/http-save/yehe-4831957/1lte1iq5fh.png?imageView2/2/w/1620)