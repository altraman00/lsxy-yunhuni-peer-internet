<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@include file="/inc/import.jsp" %>
<!DOCTYPE html>
<html lang="en" class="no-js">
<head>
    <title>注册</title>
    <%@include file="/inc/metaLogin.jsp" %>
    <!--index.css-->
    <link href="${resPrefixUrl }/stylesheets/register.css" rel="stylesheet"  type="text/css" />
</head>
<body class="register-bg">
<!--  container -->

<div class="register-header">
    <div class="box">
        <img class="logo" src="${resPrefixUrl }/images/index/logo_top.png" />
        <div class="text">
            如果有云呼你账号，<a href="${ctx }/login">立即登录</a>
        </div>
    </div>
</div>


<div class="register_container">
    <div class="row box">
        <div class="col-md-5 text-center">
            <img src="${resPrefixUrl }/images/register/reg_img.jpg" width="100%" />
        </div>
        <div class="col-md-7 register-box">
            <div class="row">
                <P class="col-md-3 text-right">云呼你会员注册</P>
            </div>
            <div class="row">
                <form role="form" action="${ctx }/reg/register" method="post" class="register-form" id="defaultForm" accept-charset="utf-8">
                    <!-- 防止表单重复提交要加这个隐藏变量 -->
                    <input type="hidden" name="submission_token" value="${submission_token}" />
                    <div class="form-group">
                        <lable class="col-lg-3 text-right">会员名称</lable>
                        <div class="col-lg-9 ">
                            <input type="text" name="userName" placeholder="请输入账号" class="form-control input-form" id="form-username" />
                            <p class="tips">6~25个字符，建议中文名称</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <lable class="col-lg-3 text-right">绑定手机</lable>
                        <div class="col-lg-9">
                            <input type="text" name="mobile" placeholder="请输入手机号" class="form-control input-form" id="form-mobile" maxlength="11"  />
                            <p class="tips"></p>
                        </div>
                    </div>
                    <div class="form-group">
                        <lable class="col-lg-3 text-right">绑定邮箱地址</lable>
                        <div class="col-lg-9">
                            <input type="text" name="email" placeholder="请输入邮箱" class="form-control input-form" id="form-email"  />
                            <p class="tips">注册后，邮箱地址收到账号激活邮件</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-lg-9 col-lg-offset-3">
                            <a id="validateBtn"  class="btn btn-primary  btn-form">注册</a>
                        </div>
                    </div>
                    <!--提示-->
                        <input type="hidden" name="${_csrf.parameterName}"
                               value="${_csrf.token}" />
                </form>
                <div class="form-group">
                    <div class="col-lg-9 col-lg-offset-3">
                        <span id="showtips" class="tips-error" style="display: none" >用户名已经存在</span>
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>
<!-- /container -->
<!--footer-->
<div class="register_footer">
    <p>Copyright 2016 云呼你 粤ICP备16048993号 All Rights Reserved 广州流水行云科技有限公司</p>
</div>
<!--/footer-->
<!--modal-->
<div class="shadow-bg" id="shadow-bg"  ></div>
<div id="modal-mobile" class="modal-phone">
    <div class="title">验证手机号</div>
    <div class="content">
        <div class="input">
            手机号 :  <span id="mobile_number">1361****986</span>
        </div>
        <div class="input">
            <div class="code-title">验证码 :</div>
            <input class="code form-control" type="text" name="mobileCode" id="mobileCode"/>
            <button class="code-button" id="send-code" >发送验证码</button>
            <span id="mobileCodeTips" class="tips-error" style="display: none" ></span>
        </div>
    </div>
    <div class="footer">
        <a id="modal-hidden" class="cancel">返回</a>
        <a id="modal-sendmobile" class="sure">确认</a>
    </div>
</div>
<!--modalend--->

<!--jquery-->
<script src="${resPrefixUrl }/bower_components/jquery/dist/jquery.min.js"></script>
<!--bootstrapvalidator-->
<script src="${resPrefixUrl }/bower_components/bootstrapvalidator/dist/js/bootstrapValidator.min.js"></script>

<script src="${resPrefixUrl }/js/register/reg.js" ></script>

<!--must-->
<script type="text/javascript">

    /*确认*/
    $('#modal-sendmobile').click(function(){
        var mobileCode = $("#mobileCode").val();
        if(mobileCode == ""){
            tipsmsg("请填入验证码","mobileCodeTips");
            return;
        }
        var mobile = $("#form-mobile").val();
        $.get(ctx + "/mc/check", {"mc":mobileCode,"mobile":mobile},
            function(data){
                if(data.flag){
                    document.getElementById('defaultForm').submit();
                }else{
                    tipsmsg(data.err,"mobileCodeTips");
                }
        });

    });

    function send_mobile_code(){
        var mobile = $("input[name='mobile']").val();
        //异步发送验证码
        $.get(ctx + "/mc/send",{"mobile":mobile});
        return true;
    }


    //检验账号是否注册
    function reg_isexit(){
        var bol = false;
        var userName = $('#form-username').val();
        var mobile = $('#form-mobile').val();
        var email = $('#form-email').val();
        var _csrf = $("input[name='_csrf']").val();
        $.ajax({
            type: "post",
            url: ctx + "/reg/info_check",
            data: { userName: userName,mobile:mobile,email:email,_csrf:_csrf},   //id
            async: false,
            dataType: "json",
            success: function(result) {
                if(result.flag){
                    bol = true;
                }else{
                    //显示showtips
                    tipsmsg(result.err);
                }
            }
        });
        return bol;
    }


</script>

</body>
</html>