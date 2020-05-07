物联网-公众号交互项目

目前还处于开发设计阶段, 可能弃坑也说不定....


![流程图](http://cdn.flizi.cn/img/taoiot_lct.png)

公众号可以及时的接收消息,很香;格式多样: 文字, 图片, 语音, 位置; 微信支付,直通设备

# 消息推送

## taoiot指令

主要是和系统本身交互

### taoiot:get_token
```json
{
  "userId": 1,
  "token": "token值"
}
```

### taoiot:post_token
```json
{
  "id": 1,
  "token": "新的token值,注意:原来token失效"
}
```

## mqtt指令

从公众号可以对不同的设备发送消息,从设备发送的消息都会发送到当前的公众号(仅个人可见)
只支持QOS0

公众号发送格式:
```
mqtt:${channel}:${topic}:${context}
channel: 不同的设备的编号(自行定义)
topic: 硬件主题,不能是taoiot
context: 内容(字符串, 就算写的是0, 发送过去的也是"0")
username: userId (鉴权使用, 发送 taoiot:get_token 获取)
password: token (鉴权使用, 发送 taoiot:get_token 获取)
举例: 
    mqtt:1000:light:off
    mqtt:1000:light:on
```

硬件订阅主题格式(标准mqtt格式):
```
mqtt:${userId}.${channel}.${topic}
userId: 用户的ID
channel: 不同的设备的编号(自行定义)
topic: 硬件主题(如果topic是taoiot,那么对发送的内容有格式要求)
username: userId (鉴权使用, 发送 taoiot:get_token 获取)
password: token (鉴权使用, 发送 taoiot:get_token 获取)

举例: 
    topic://1:1000:light:off
    topic://1:1000:light:on
```

# 推送特殊格式

微信消息分两类,一类是客服消息,一个模板消息

- **客服消息**: 就是我们普通的微信公众号里面的了解消息, 主要用于交互
但是必须用户发送消息,服务器才能响应消息(接下来24小时内,服务器都可以一直发送消息到公众号,过了就必须让用户再发一次消息),
所以客服消息主要是拿来控制硬件的,比如发送指令,发送图片,位置, 文字上来

- **模板消息**: 主要是让硬件主动发送消息上来: 比如温度报警, 内存不足, 设备断点. 

