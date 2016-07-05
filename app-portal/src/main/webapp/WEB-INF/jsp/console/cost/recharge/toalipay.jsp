<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>支付宝即时到账交易接口</title>
</head>
<body>
<form id="alipaysubmit" name="alipaysubmit" action="${aliPayGateWay}" method="post">
    <input type="hidden" name="service"  value="${aliPayVO.service}" />
    <input type="hidden" name="partner"  value="${aliPayVO.partner}" />
    <input type="hidden" name="seller_id"  value="${aliPayVO.seller_id}" />
    <input type="hidden" name="_input_charset"  value="${aliPayVO._input_charset}" />
    <input type="hidden" name="payment_type"  value="${aliPayVO.payment_type}" />
    <input type="hidden" name="notify_url"  value="${aliPayVO.notify_url}" />
    <input type="hidden" name="return_url"  value="${aliPayVO.return_url}" />
    <input type="hidden" name="anti_phishing_key"  value="${aliPayVO.anti_phishing_key}" />
    <input type="hidden" name="exter_invoke_ip"  value="${aliPayVO.exter_invoke_ip}" />
    <input type="hidden" name="out_trade_no"  value="${aliPayVO.out_trade_no}" />
    <input type="hidden" name="subject"  value="${aliPayVO.subject}" />
    <input type="hidden" name="total_fee"  value="${aliPayVO.total_fee}" />
    <input type="hidden" name="body"  value="${aliPayVO.body}" />
    <input type="hidden" name="sign"  value="${aliPayVO.sign}" />
    <input type="hidden" name="sign_type"  value="${aliPayVO.sign_type}" />
    <input type="submit" value="付款" style="display:none;">
</form>
<script>document.forms['alipaysubmit'].submit();</script>
</body>
</html>
