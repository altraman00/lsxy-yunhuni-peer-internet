<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@include file="/inc/import.jsp" %>
<!DOCTYPE html>
<html lang="en" class="no-js">
<head>
    <%@include file="/inc/meta.jsp" %>

    <!--index.css-->
    <link href="${resPrefixUrl }/stylesheets/register.css" rel="stylesheet" type="text/css"/>
    <title>注册失败</title>
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
            <p class="p">账号失败：${erInfo}</p>
            <p><a href="${ctx}/reg/register_page">点击重新注册</a></p>
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