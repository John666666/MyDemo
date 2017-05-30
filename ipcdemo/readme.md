Android 实现IPC的几种方法:
1. Bundle   最简单, 适用于四大组件之间通信，只能传输Bundle支持的数据类型
2. Messenger    对AIDL做了封装, 支持一对多串行通信，通过Handler来处理消息, 适用于低并发消息传递、无返回值的RPC操作。
3. AIDL     支持一对多的RPC操作，需注意线程安全问题(服务端)
4. ContentProvider  对AIDL做了封装， 主要用于跨进程数据CRUD操作
5. 文件共享 (自定义文件、ShardPrefrences)    操作简单，实时性差，不支持并发
6. Socket   很强大， 不仅支持跨进程，跨应用，还支持跨设备通信，不直接支持RPC