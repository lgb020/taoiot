物联网-公众号交互中间件


![流程图](http://cdn.flizi.cn/img/taoiot_lct.png)


# 使用场景: 

1. 查找树莓派信息局域网IP (查看下面的案例1)
2. 树莓派登录提醒功能(查看下列案例2)
2. 树莓派拍照上传(开发中)


公众号可以及时的接收消息,很香;格式多样: 文字, 图片, 语音, 位置; 微信支付,直通设备

# 使用说明

## 获取账号密码
在公众号中发送,获取token和userId
```
taoiot:get_token
```

## mqtt链接服务器

1. 必须设置clientId, 自己随意设置,每个自己的设备要不一样
2. 必须设置username和password, username填写userId, 密码填写token

## mqtt发送消息给自己公众号,主题格式如下:

```
${userId}/taoiot/msg
```
举例: 如果 userId = 2020, 主题就是: 2020/taoiot/msg

## MQTT发送模板给自己公众号,主题格式如下:
**得先关注公众号才能发送模板消息**
```
${userId}/taoiot/temp/1
```
举例: 如果 userId = 2020, 主题就是: 2020/taoiot/temp/1

## 公众号发送到MQTT 格式录下: 

```
mqtt:${topic}:${context}
```

举例: userId=2020的用户 给 light/001 主题发送消息'off', 则发送: mqtt:light/001:off

**注意: 如果要接收这条消息,则应该订阅主题: 2020/light/001  (!!别忘记加UserId!!)**

# 公众号[飞立物联]

![微信搜索公众号: 飞立物联](http://cdn.flizi.cn/img/taoiot_qr.jpg)

MQTT地址: 122.51.85.179:1883

# 支付说明

todo 

# 部署说明

todo

# 案例

1. 获取局域网IP
```python
# encoding: utf-8

import paho.mqtt.client as mqtt
import socket


# 公众号微信搜索:  飞立物联
# 微信公众号模板消息必须用户先关注了才能发(避免不良开发商骚扰用户的一种措施).所以请先手动关注一下公众号

# 源码已经在github上了: http://github.com/taoroot/taoiot 
# 使用文档都在上面
# 记得点个Start啦....

# 填写你的userId,在公众号里面发送 taoiot:get_token 获取
USERNAME=""
# 填写你的token,在公众号里面发送 taoiot:get_token 获取
PASSWORD=""
# 一般可以不用变, 如果你有多个设备,想做区分的话就需要修改了,确保自己能分别出来,消息是哪个设备发给你的
CLIENT_ID="pi"


# The callback for when the client receives a CONNACK response from the server.
def on_connect(client, userdata, flags, rc):
    # 0 代表正常
    if rc == 0: 
        print("连接成功")
    else:
        print("连接失败,正在重连...")

    # 订阅主题: 
    client.subscribe(USERNAME + "/pi/status")

def get_host_ip():
    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        s.connect(('8.8.8.8', 80))
        ip = s.getsockname()[0]
    finally:
        s.close()
    return ip

# 接收来自公众号的消息,并根据内容返回公众号消息
def on_message(client, userdata, msg):
    
    print("接收到主题: " + msg.topic + "  发送的内容: " + msg.payload.decode())
    # 公众号发送 mqtt:pi/status:ip 获取局域网地址
    if msg.payload.decode() == "ip":
        client.publish(USERNAME + "/taoiot/mp/msg", payload=get_host_ip(), qos=0, retain=False)

client = mqtt.Client(client_id=CLIENT_ID)
client.username_pw_set(USERNAME, PASSWORD)
client.on_connect = on_connect
client.on_message = on_message

client.connect("mqtt.flizi.cn", 1883, 60)

client.loop_forever()
```
