<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@include file="/inc/import.jsp" %>
<!DOCTYPE html>
<html lang="en" class="no-js">
<head>
    <title>注册成功</title>
    <%@include file="/inc/metaLogin.jsp" %>
    <!--index.css-->
    <link href="${resPrefixUrl }/stylesheets/register.css" rel="stylesheet" type="text/css"/>
</head>
<body class="register-bg">
<!--  container -->

<div class="register-header">
    <div class="box">
        <a a href="#" onclick="window.location.href='<%=SystemConfig.getProperty("global.official.website.url")%>'">
        <img class="logo" src="${resPrefixUrl }/images/index/logo_top.png"/></a>
        <div class="text">
            如果有壹耘账号，<a href="${ctx }/login">立即登录</a>
        </div>
    </div>
</div>


<div class="register_container">
    <div class="row box">
        <div class="col-md-12 text-center">
            <img src="${resPrefixUrl }/images/register/icon_12.png"/>
            <p class="p">账号注册成功，请在72小时之内前往邮箱${email},收取邮件完成账号激活</p>
            <p><a class="btn_actemail"  data-email="${email}" >去邮箱收取邮件</a></p>
        </div>
        <div id="accountId" style="display:none">${accountId}</div>
    </div>
</div>
<!--/container -->
<!--footer-->
<div class="register_footer">
    <p>Copyright 2016 壹耘 粤ICP备16048993号 All Rights Reserved 广州流水行云科技有限公司</p>
</div>
<!--/footer-->



<!--jquery-->
<script src="${resPrefixUrl }/bower_components/jquery/dist/jquery.min.js"></script>
<!--bootstrapvalidator-->
<script src="${resPrefixUrl }/bower_components/bootstrapvalidator/dist/js/bootstrapValidator.js"></script>
<script src="${resPrefixUrl }/js/register/reg_success.js" ></script>

</body>
</html>