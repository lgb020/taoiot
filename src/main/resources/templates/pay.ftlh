<html lang="en">
<head>
  <meta name="viewport"
        content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
  <meta charset="UTF-8">
  <title>聚合支付</title>
  <script src="https://res.wx.qq.com/open/js/jweixin-1.4.0.js"></script>
  <script>
    function readyWechat(onBridgeReady) {
        if (typeof WeixinJSBridge == "undefined") {
            if (document.addEventListener) {
                document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
            } else if (document.attachEvent) {
                document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
                document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
            }
        } else {
            onBridgeReady();
        }
    }
  </script>
</head>
<body>
   <#if type == 'PAYED'>
    <h1>已购买</h1>
   </#if>
</body>
</html>
<script>
<#if type == 'WECHAT'>
function onBridgeReady() {
    WeixinJSBridge.invoke(
        'getBrandWCPayRequest', {
            "appId": "${params.appId}",     //公众号名称，由商户传入
            "timeStamp": "${params.timeStamp}", //时间戳，自1970年以来的秒数
            "nonceStr": "${params.nonceStr}", //随机串
            "package": "${params.package}",
            "signType": "${params.signType}", //微信签名方式：
            "paySign": "${params.paySign}" //微信签名,paySign 采用统一的微信支付 Sign 签名生成方法，注意这里 appId 也要参与签名，appId 与 config 中传入的 appId 一致，即最后参与签名的参数有appId, timeStamp, nonceStr, package, signType。
        },
        function (res) {}
    );
}
if (typeof WeixinJSBridge == "undefined") {
    if (document.addEventListener) {
        document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
    } else if (document.attachEvent) {
        document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
        document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
    }
}
</#if>

<#if type == 'ALIPAY'>
    function readyAlipay(callback) {
        if (window.AlipayJSBridge) {
            callback && callback();
        } else {
            document.addEventListener('AlipayJSBridgeReady', callback, false);
        }
    }
    readyAlipay(() => {
        AlipayJSBridge.call("tradePay", { tradeNO: "${params}" }, result => {})
    })
</#if>
   
</script>