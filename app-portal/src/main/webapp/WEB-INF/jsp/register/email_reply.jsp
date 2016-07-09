<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@include file="/inc/import.jsp" %>
<!DOCTYPE html>
<html lang="en" class="no-js">
<head>
    <%@include file="/inc/metaLogin.jsp" %>
    <!--index.css-->
    <link href="${resPrefixUrl }/stylesheets/register.css" rel="stylesheet" type="text/css"/>
    <title>账号激活</title>
</head>
<body class="register-bg">
<!--  container -->

<div class="register-header">
    <div class="box">
        <img class="logo" src="${resPrefixUrl }/images/index/logo_top.png"/>
        <div class="text">
            如果有云呼你账号，<a href="${ctx}/login">立即登录</a>
        </div>
    </div>
</div>


<div class="register_container">
    <div class="row box">
        <div class="col-md-12 text-left article-box">
            <p class="size16"><strong>尊敬的会员用户：</strong></p>
            <br/>
            <p>感谢您注册为云呼你会员用户，以下为云呼你账号激活链接，点击一下激活链接激活账号并设置初始密码：</p>
            <br/>
            <a href="#">http://localhost/forget/resetpassword?key=123456</a>
            <br/>
            <br/>
            <br/>
            <p>请注意：</p>
            <p>为安全起见，此链接将在自发送之时起 24 小时后过期。</p>
            <p>如果您无法访问此链接，请将整个 URL 复制并粘贴到浏览器中。</p>
            <br/>
            <br/>
            <br/>
            <br/>
            <p>云呼你团队</p>
            <p>2016年06月22日</p>
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