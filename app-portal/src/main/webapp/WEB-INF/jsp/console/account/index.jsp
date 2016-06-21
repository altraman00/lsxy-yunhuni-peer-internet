<%@page import="org.springframework.context.annotation.Import"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@include file="/inc/import.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/inc/meta.jsp" %>

    <style>
        .t-status {
            position: relative;
            line-height: 100%;
        }

        .t-status h4 {
            margin-left: 55px;
        }
        .t-status p {
            margin-bottom: 0px;
            margin-left: 55px;
            font-size: 12px;
        }

        .c-status {
            position: relative;
            line-height: 100%;
        }

        .c-status h4 {
            margin-left: 45px;
            font-size: 16px;
        }
        .c-status p {
            margin-bottom: 0px;
            margin-left: 45px;
            font-size: 9px;
        }

        .type {
            position: absolute;
            top: 0;
            padding: 0px 10px 0px 0px;
            border-right: 1px solid #ededed;
            margin-left: 5px;
            width: 40px;
            height: 30px;
            font-size: 30px;
        }

        .min-type {
            position: absolute;
            top: 0;
            padding: 0px 10px 0px 0px;
            margin-left: 5px;
            width: 30px;
            height: 30px;
            font-size: 28px;
        }
    </style>
</head>

<body>
<%@include file="/inc/headerNav.jsp" %>
<!-- 主界面 -->

<div class="container-fluid new_main">
    <div class="col_side">
        <%@include file="/inc/leftMenu.jsp" %>
    </div>
    <div class="col_main">
       账户管理<br/>
        <a href="${ctx}/console/account/securitySettings">安全设置</a><br>
        <a href="${ctx}/console/account/information">基本资料</a><br>
        <a href="${ctx}/console/account/certification">实名认证</a><br>
        <a href="${ctx}/file/index">文件上传</a><br>
    </div>
</div>
<%@include file="/inc/footer.jsp" %>
<script src="${resPrefixUrl}/common/scripts/ichart/ichart.1.2.min.js"></script>
<script src="${resPrefixUrl}/portal/console/account/scripts/index.js"></script>
<script>
    setCookie('hsy_last_page', 'admin', 10);
</script>
</body>
</html>