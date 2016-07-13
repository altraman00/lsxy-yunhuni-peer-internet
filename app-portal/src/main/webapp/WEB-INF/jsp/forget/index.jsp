<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@include file="/inc/import.jsp" %>
<!DOCTYPE html>
<html lang="en" class="no-js">
<head>
    <title>忘记密码</title>
    <%@include file="/inc/metaLogin.jsp" %>
    <!--index.css-->
    <link href="${resPrefixUrl }/stylesheets/register.css" rel="stylesheet" type="text/css"/>
</head>
<body class="register-bg">
<!--  container -->

<div class="register-header">

    <div class="box">
        <img class="logo" src="${resPrefixUrl }/images/index/logo_top.png"/>
        <div class="text">
            如果有云呼你账号，<a href="${ctx}/login">立即登录</a> <a href="${ctx}/reg/index">立即注册</a>
        </div>
    </div>

</div>


<div class="register_container">
    <div class="row box">
        <div class="forget-box">
            <ul id="myTab" class="nav nav-tabs">
                <li class="active">
                    <a href="#tabemail" class="right" data-toggle="tab">
                        邮箱找回
                    </a>
                </li>
                <li class="">
                    <a href="#tabphone" class="left" data-toggle="tab">
                        手机短信找回
                    </a>
                </li>
            </ul>
            <div id="myTabContent" class="tab-content">
                <div class="tab-pane fade in active" id="tabemail">
                    <form:form action="${ctx}/forget/send_mail" method="post" id="emailForm" >
                    <!-- 防止表单重复提交要加这个隐藏变量 -->
                    <input type="hidden" name="submission_token" value="${submission_token}" />
                    <div class="box">
                        <div class="row">
                            <div class="col-md-12">
                                <input class="form-control" name="email" id="email" placeholder="输入邮箱地址 ,找回密码"  />
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12">
                                <button type="submit" class="btn btn-primary  btn-form ">发送重置密码的邮件</button>
                            </div>
                        </div>
                    </div>
                    </form:form>
                </div>
                <div class="tab-pane fade" id="tabphone">
                    <div class="box">
                        <form:form action="${ctx}/forget/reset_pwd_mobile" method="post" id="mobileForm" >
                        <div class="row">
                            <div class="col-md-12">
                                <input class="form-control" name="mobile" id="mobile" placeholder="输入注册手机号"  />
                                <small class="help-block tips-error" id="mobileTips" ></small>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-7 ">
                                <input class="form-control" name="code" id="code" placeholder="验证码"  />
                            </div>
                            <div class="col-md-5">
                                <button class="sendcode" type="button" id="send-code" >发送验证码</button>
                            </div>
                        </div>
                        <div class="row code-box" id="second-codeblock" ></div>
                        <div class="row">
                            <div class="col-md-12">
                                <span id="mobileCodeTips" class="tips-error" style="display: none" ></span>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12">
                                <a id="mobileBtn" class="btn btn-primary  btn-form btn-mobile-submit">找回密码</a>
                            </div>
                        </div>
                        </form:form>
                    </div>
                </div>
            </div>
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
<script src="${resPrefixUrl }/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>

<!--must-->
<script src="${resPrefixUrl }/js/register/reg.js" ></script>
<script>

    function reg_code(){
        var mobileCode = $("#code").val();
        var mobile = $("#mobile").val();
        $.get(ctx + "/mc/check", {"mc":mobileCode,"mobile":mobile},
                function(data){
                    if(data.flag){
                        document.getElementById('mobileForm').submit();
                    }else{
                        tipsmsg(data.err,"mobileCodeTips");
                    }
                });
    }

    var isVc = false;       //是否需要图形验证码
    function send_mobile_code(){
        var sendResult = false; //是否发送手机验证码成功
        var mobile = $("input[name='mobile']").val();
        var vCode = "";
        if(mobile == ""){
            tipsmsg("请填入手机号","mobileTips");
            return false;
        }else{
            var bol = checkMobile(mobile);//手机是否有效
            if(bol){
                if(isVc){
                    //校验四位验证码是否正确
                    if($('#second-code').length>0){
                        var second = $('#second-code').val();
                        if(second.length!=4){
                            tipsmsg('请输入四位验证码','secondcodeTips');
                            return false;
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
                    success: function(result) {
                        if(result.flag){
                            //发送成功
                            sendResult = true;
                        }else if(result.vc){
                            sendResult = false;
                            //发送不成功，且要输入图形验证码
                            tipsmsg(result.err,'secondcodeTips');
                            isVc = true;

                            //启动二次校验
                            $('#second-code').show();

                            var html = '<div class="col-md-7"><input class="form-control" name=""  placeholder="验证码"  id="second-code" onkeyup="clearErrMsg()"/><small class="help-block tips-error" id="secondcodeTips"></small></div><div class="col-md-5">';
                            html += '<span class="code-img"><img id="imgValidateCode" src="' + ctx + '/vc/get?dt='+ new Date() +'" onclick="changeImgCode()"></span></div>';

                            $('#second-codeblock').html(html);

                        }else{
                            sendResult = false;
                            tipsmsg(result.err,'mobileCodeTips');
                        }
                    }
                });
            }
            return sendResult;
        }
    }

    function checkMobile(mobile){
        if(regMobile(mobile) == false){
            tipsmsg("无效手机号码","mobileTips");
            return false;
        }
        var bol = false;
        $.ajax({
            type: "get",
            url: ctx + "/forget/check_mobile",
            data: { mobile:mobile},
            async: false,
            dataType: "json",
            success: function(result) {
                if(result.flag){
                    bol = true;
                }else{
                    //显示showtips
                    tipsmsg(result.err,"mobileTips");
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