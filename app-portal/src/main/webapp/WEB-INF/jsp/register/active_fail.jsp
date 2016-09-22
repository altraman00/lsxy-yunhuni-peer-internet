<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@include file="/inc/import.jsp" %>
<!DOCTYPE html>
<html lang="en" class="no-js">
<head>
    <title>激活失败</title>
    <%@include file="/inc/metaLogin.jsp" %>
    <!--index.css-->
    <link href="${resPrefixUrl }/stylesheets/register.css" rel="stylesheet" type="text/css"/>
</head>
<body class="register-bg">
<!--  container -->

<div class="register-header">
    <div class="box">
        <a a href="#" onclick="window.open('<%=SystemConfig.getProperty("global.official.website.url")%>')">
        <img class="logo" src="${resPrefixUrl }/images/index/logo_top.png"/></a>
        <div class="text">
            如果有云呼你账号，<a href="${ctx }/login">立即登录</a>
        </div>
    </div>
</div>


<div class="register_container">
    <div class="row box">
        <div class="col-md-12 text-center">
            <img src="${resPrefixUrl }/images/register/sign-error-icon.png"/>
            <p class="p">激活失败：${erInfo}</p>
            <c:if test="${errorCode!=null}">
                <p><a href="${ctx }/login">点击重新登录</a></p>
            </c:if>
            <c:if test="${errorCode==null}">
                <p><a href="${ctx}/reg/index">点击重新注册</a></p>
            </c:if>
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