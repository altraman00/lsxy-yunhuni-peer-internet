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
        <a a href="#" onclick="window.location.href='${globalWebsite}'">
        <img class="logo" src="${resPrefixUrl }/images/index/logo_top.png" /></a>
        <div class="text">
            如果有壹耘账号，<a href="${ctx }/login">立即登录</a>
        </div>
    </div>
</div>


<div class="register_container">
    <div class="row box">
        <div class="col-md-5 text-center">
            <img src="${resPrefixUrl }/images/register/reg_img.jpg" />
        </div>
        <div class="col-md-7 register-box">
            <div class="row">
                <P class="col-md-3 text-right">壹耘会员注册</P>
            </div>
            <div class="row">
                <form:form role="form" action="${ctx }/reg/register" method="post" class="register-form" id="defaultForm" acceptCharset="utf-8">
                    <!-- 防止表单重复提交要加这个隐藏变量 -->
                    <input type="hidden" name="submission_token" value="${submission_token}" />
                    <div class="form-group">
                        <lable class="col-lg-3 text-right">会员名称</lable>
                        <div class="col-lg-9 ">
                            <input type="text" name="userName" placeholder="请输入账号" class="form-control input-form" id="form-username" />
                            <p class="tips">2~25个字符，建议中文名称</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <lable class="col-lg-3 text-right">手机号</lable>
                        <div class="col-lg-9">
                            <input type="text" name="mobile" placeholder="请输入手机号" class="form-control input-form" id="form-mobile" maxlength="11"  />
                            <p class="tips"></p>
                        </div>
                    </div>
                    <div class="form-group">
                        <lable class="col-lg-3 text-right">邮箱地址</lable>
                        <div class="col-lg-9">
                            <input type="text" name="email" placeholder="请输入邮箱地址" class="form-control input-form" id="form-email"  />
                            <p class="tips">注册后，邮箱将会收到账号激活邮件</p>
                        </div>
                    </div>
                    <div class="form-group reader">
                        <div class="col-lg-9 col-lg-offset-3">
                            <input type="checkbox"  id="registerRead"> 点击阅读<a href="${globalWebsite}/develop/book/platform" target="_blank">注册协议</a> <span class="tips-error" id="registerReadMsg" hidden>请先阅读注册协议</span>
                        </div>
                    </div>
                    <div class="form-group ">
                        <div class="col-lg-9 col-lg-offset-3">
                            <a id="validateBtn"  class="btn btn-primary  btn-form">注册</a>
                        </div>
                    </div>
                    <!--提示-->
                </form:form>
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
    <p>Copyright 2016 壹耘 粤ICP备16048993号 All Rights Reserved 广州流水行云科技有限公司</p>
</div>
<!--/footer-->
<!--modal-->
<div class="shadow-bg" id="shadow-bg"  ></div>
<div id="modal-mobile" class="modal-phone">
    <div class="title">验证手机号</div>
    <div class="content">
        <div class="input margin-auto">
            手机号 :  <span id="mobile_number">1361****986</span>
        </div>
        <div class="input in-block" id="second-codeblock" ></div>
        <div class="input">
            <%--<div class="code-title">验证码 :</div>--%>
            <input class="code form-control" type="text" name="mobileCode" id="mobileCode" placeholder="手机验证码"/>
            <button class="code-button" id="send-code" >获取验证码</button>
        </div>

        <p id="mobileCodeTips" class="text-center tips-error" ></p>
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
            tipsmsg("请输入手机验证码","mobileCodeTips");
            return;
        }
        var mobile = $("#form-mobile").val();
        $.get(ctx + "/mc/check", {"mc":mobileCode,"mobile":mobile},
            function(response){
                if(response.data.flag){
                    document.getElementById('defaultForm').submit();
                }else{
                    tipsmsg(response.data.err,"mobileCodeTips");
                }
        });

    });

    var isVc = false;       //是否需要图形验证码
    function send_mobile_code(){
        var sendResult = false; //是否发送手机验证码成功

        var mobile = $("input[name='mobile']").val();
        var vCode = "";
        if(isVc){
            //校验四位验证码是否正确
            if($('#second-code').length>0){
                var second = $('#second-code').val();
                if(second.length!=4){
                    tipsmsg('请输入图形验证码','mobileCodeTips'); return false;
                }
            }
            vCode = $("#second-code").val();
        }
        //ajax发送验证码
        $.ajax({
            type: "get",
            url: ctx + "/mc/send",
            data: {"mobile":mobile,validateCode:vCode},   //id
            async: false,
            dataType: "json",
            success: function(response) {
                if(response.data.flag){
                    //发送成功
                    sendResult = true;
                }else if(response.data.vc){
                    sendResult = false;
                    //发送不成功，且要输入图形验证码
                    tipsmsg(response.data.err,'mobileCodeTips');
                    isVc = true;

                    //启动二次校验
                    $('#second-code').show();
                    var html = '<input class="code form-control" type="text" name="" id="second-code" onkeyup="clearErrMsg()" placeholder="图形验证码"/>';
                    html += '<a class="code-img"><img id="imgValidateCode" src="' + ctx + '/vc/get?dt='+ new Date() +'" onclick="changeImgCode()"></a>';
                    $('#second-codeblock').html(html);

                }else{
                    sendResult = false;
                    tipsmsg(response.data.err,'mobileCodeTips');
                }
            }
        });

        //获取验证码
        return sendResult;
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


    function clearErrMsg(){
        $('.tips-error').hide();
    }

    function changeImgCode(){
        $("#imgValidateCode").prop("src",ctx + "/vc/get?dt="+(new Date().getTime()));
    }
</script>

</body>
</html>