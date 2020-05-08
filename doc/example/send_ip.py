# encoding: utf-8

# 通过 pip install paho-mqtt 安装mqtt库
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