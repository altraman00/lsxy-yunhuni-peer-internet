<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@include file="/inc/import.jsp" %>
<!DOCTYPE html>
<html lang="en" class="no-js">
<head>
    <%@include file="/inc/metaLogin.jsp" %>
    <!--index.css-->
    <link href="${resPrefixUrl }/stylesheets/register.css" rel="stylesheet" type="text/css"/>
    <title>重置密码</title>
</head>
<body class="register-bg">
<!--  container -->

<div class="register-header">
    <div class="box">
        <a a href="#" onclick="window.location.href='${globalWebsite}'">
        <img class="logo" src="${resPrefixUrl }/images/index/logo_top.png"/></a>
        <div class="text">
            如果有壹耘账号，<a href="${ctx}/login">立即登录</a>
        </div>
    </div>
</div>


<div class="register_container">
    <div class="row box">
        <div class="col-md-12 text-left article-box">
            <p class="size16"><strong>尊敬的lxp5820用户：</strong></p>
            <br/>
            <p>以下为壹耘重置密码的安全链接，如果您没有发起重置密码的请求，请不用理会该邮件，谢谢！</p>
            <br/>
            <a href="#">http://localhost/forget/resetpassword?key=123456</a>
            <br/>
            <br/>
            <br/>
            <p>请注意：</p>
            <p>为安全起见，此链接将在自发送之时起 72 小时后过期。</p>
            <p>如果您无法访问此链接，请将整个 URL 复制并粘贴到浏览器中。</p>
            <br/>
            <br/>
            <br/>
            <br/>
            <p>壹耘团队</p>
            <p>2016年06月22日</p>
        </div>
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


</body>
</html>