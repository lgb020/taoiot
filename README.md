物联网-公众号交互中间件


![流程图](http://cdn.flizi.cn/img/taoiot_lct.png)


# 使用场景: 

1. 查找树莓派信息(局域网IP, CPU温度...) 
2. 树莓派拍照上传
3. 树莓派自助服务(打印,充电)


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
