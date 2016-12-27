<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@include file="/inc/import.jsp" %>
<!DOCTYPE html>
<html lang="en" class="no-js">
<head>
    <title>找回密码</title>
    <%@include file="/inc/metaLogin.jsp" %>
    <!--index.css-->
    <link href="${resPrefixUrl }/stylesheets/register.css" rel="stylesheet"  type="text/css" />
</head>
<body class="register-bg">
<!--  container -->

<div class="register-header">
    <div class="box">
        <a a href="#" onclick="window.location.href='${globalWebsite}'">
        <img class="logo" src="${resPrefixUrl }/images/index/logo_top.png" /></a>
        <div class="text">
            如果有壹耘账号，<a href="${ctx }/login">立即登录</a>
        </div>
    </div>
</div>

<div class="register_container">
    <div class="row box ">
        <div class="col-md-3">
        </div>
        <div class="col-md-6  active-box" >
            <div class="row" >

                <p class="col-lg-offset-1 text-center register-active">重新设置密码</p>
            </div>
            <div class="row">
                <form:form role="form" action="${ctx}/forget/reset_password" method="post" class="register-form" id="defaultForm">
                    <!-- 防止表单重复提交要加这个隐藏变量 -->
                    <input type="hidden" name="submission_token" value="${submission_token}" />

                    <input type="hidden" name="resetType" value="${resetType}" />
                    <input type="hidden" name="emailKey" value="${emailKey}">
                    <div class="form-group">
                        <lable class="col-lg-2 text-right">新的密码</lable>
                        <div class="col-lg-9">
                            <input type="password" name="password" placeholder="请输入密码" class="form-control input-form" id="form-password" maxlength="25"  />
                            <div class="tips">
                                <span class="special-span" >6~18个字符，建议字符数字组合</span>
                                <div id="pw-strength" class="pw-strength">
                                    <div class="pw-bar"></div>
                                    <div class="pw-bar-on"></div>
                                    <div class="pw-txt">
                                        <span>弱</span>
                                        <span>中</span>
                                        <span>强</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <lable class="col-lg-2 text-right">确认密码</lable>
                        <div class="col-lg-9">
                            <input type="password" name="repassword" placeholder="请输入确认密码" class="form-control input-form" id="form-repassword" />
                            <p class="tips"></p>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-lg-9 col-lg-offset-2">
                            <button id="validateBtn" type="submit" class="btn btn-primary  btn-form">完成</button>
                        </div>
                    </div>

                </form:form>
            </div>

        </div>
        <div class="col-md-3">
        </div>
    </div>
</div>
<!-- /container -->
<!--footer-->
<div class="register_footer">
    <p>Copyright 2016 壹耘 粤ICP备16048993号 All Rights Reserved 广州流水行云科技有限公司</p>
</div>
<!--/footer-->

<!--jquery-->
<script src="${resPrefixUrl }/bower_components/jquery/dist/jquery.min.js"></script>

<!--bootstrapvalidator-->
<script src="${resPrefixUrl }/bower_components/bootstrapvalidator/dist/js/bootstrapValidator.js"></script>

<script src="${resPrefixUrl }/js/register/reg_active.js" ></script>

<script type="text/javascript">

</script>

</body>
</html>