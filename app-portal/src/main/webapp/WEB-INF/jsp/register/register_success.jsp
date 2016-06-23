<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@include file="/inc/import.jsp" %>
<!DOCTYPE html>
<html lang="en" class="no-js">
<head>
    <%@include file="/inc/meta.jsp" %>
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!--bootstrap.css-->
    <link href="${resPrefixUrl }/bower_components/bootstrap/dist/css/bootstrap.css" rel="stylesheet" type="text/css"/>
    <!--index.css-->
    <link href="${resPrefixUrl }/stylesheets/screen.css" rel="stylesheet" type="text/css"/>
    <!--vaildator.css-->
    <link rel="stylesheet" href="${resPrefixUrl }/bower_components/bootstrapvalidator/dist/css/bootstrapValidator.css">

    <title>注册成功</title>
</head>
<body class="register-bg">
<!--  container -->

<div class="register-header">
    <div class="box">
        <img class="logo" src="${resPrefixUrl }/images/index/logo_top.png"/>
        <div class="text">
            如果有云呼你账号，<a href="${ctx }/login">立即登录</a>
        </div>
    </div>
</div>


<div class="register_container">
    <div class="row box">
        <div class="col-md-12 text-center">
            <img src="${resPrefixUrl }/images/register/icon_12.png"/>
            <p class="p">账号注册成功，请24小时内前往邮箱收取邮件激活账号 ${email}</p>
            <p><a href="#">去邮箱收取邮件</a></p>
        </div>
    </div>
</div>
<!--/container -->
<!--footer-->
<div class="register_footer">
    <p>Copyright 2016 云呼你 粤ICP备16048993号 All Rights Reserved 广州流水行云科技有限公司</p>
</div>
<!--/footer-->



<!--jquery-->
<script src="${resPrefixUrl }/bower_components/jquery/dist/jquery.min.js"></script>
<!--bootstrapvalidator-->
<script src="${resPrefixUrl }/bower_components/bootstrapvalidator/dist/js/bootstrapValidator.js"></script>


</body>
</html>