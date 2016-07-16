<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@include file="/inc/import.jsp" %>
<!DOCTYPE html>
<html>

<!-- header -->
<head>
    <%@include file="/inc/meta.jsp" %>

</head>
<body>
<section class="vbox">
<%@include file="/inc/headerNav.jsp"%>
<section class='aside-section'>
    <section class="hbox stretch">
        <!-- .aside -->
        <aside class="bg-Green lter aside hidden-print" id="nav"><%@include file="/inc/leftMenu.jsp"%></aside>
        <!-- /.aside -->

        <section id="content">
            <section class="hbox stretch">
                <!-- 如果没有三级导航 这段代码注释-->
                <aside class="bg-green lter aside-sm hidden-print ybox" id="subNav">
                    <section class="vbox">
                        <div class="wrapper header"><span class="margin_lr"></span><span class="margin_lr border-left">&nbsp;基本资料</span>
                        </div>
                        <section class="scrollable">
                            <div class="slim-scroll">
                                <!-- nav -->
                                <nav class="hidden-xs">
                                    <ul class="nav">
                                        <li>
                                            <div class="aside-li-a active">
                                                <a href="${ctx}/console/account/safety/index">安全设置</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a ">
                                                <a href="${ctx}/console/account/index">基本资料</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a ">
                                                <a href="${ctx}/console/account/auth/index">实名认证</a>
                                            </div>
                                        </li>
                                    </ul>
                                </nav>
                            </div>
                        </section>
                    </section>
                </aside>
                <aside>
                    <section class="vbox xbox">
                        <!-- 如果没有三级导航 这段代码注释-->
                        <div class="head-box"><a href="#subNav" data-toggle="class:hide"> <i
                                class="fa fa-angle-left text"></i> <i class="fa fa-angle-right text-active"></i> </a>
                        </div>
                        <div class="wrapper header"><span class="border-left">&nbsp;安全设置</span></div>
                        <section class=" wrapper w-f">
                            <section class="panel panel-default yunhuni-personal">
                                <div class="row m-l-none m-r-none bg-light lter">
                                    <div class="col-md-12 padder-v fix-padding">
                                        <div class='wrapperBox'>
                                            <div class="panel-body clearfix border-top-none personal-base">
                                                <div class="row ">
                                                    <div class="col-md-1 remove-padding width-130">
                                                        会员名称：
                                                    </div>
                                                    <div class="col-md-10 ">
                                                        <span>${safetyVo.username}</span>
                                                        <c:if test="${safetyVo.isReal !=1}">
                                                            <span> <img src="${resPrefixUrl }/images/index/renzheng.png"/>
                                                            <a  href="${ctx}/console/account/auth/index">点击进行实名认证</a></span>
                                                        </c:if>
                                                        <c:if test="${safetyVo.isReal ==1}">
                                                            <span><img src="${resPrefixUrl }/images/index/ok_green.png"/> 已认证</span>
                                                        </c:if>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-md-1 remove-padding width-130">
                                                        账号ID：
                                                    </div>
                                                    <div class="col-md-10 ">
                                                        <span>${safetyVo.userId}</span>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-md-1 remove-padding width-130">
                                                        注册时间：
                                                    </div>
                                                    <div class="col-md-10 ">
                                                        <span>${safetyVo.time}</span>
                                                    </div>
                                                </div>

                                            </div>
                                        </div>
                                    </div>

                                </div>
                            </section>

                            <section class="panel panel-default pos-rlt clearfix ">
                                <div class="sectionWrap">
                                    <div class="form-horizontal ">
                                        <div class="row dotted-line personal-box ">
                                            <div class="col-md-1 account-icon  "><i class="personal-password-icon"></i>
                                            </div>
                                            <div class="col-md-9 ">
                                                    <span class="help-block"><a href="#">登录密码</a><br/>
                                                        <small class="help-small">安全性高的密码可以使账号更安全，设置一个包含字母符号或数字中至少两项且长度超过6位的密码</small>
                                                    </span>
                                            </div>
                                            <div class="col-md-2 right">
                                                <span>
                                                    <img src="${resPrefixUrl }/images/index/ok_green.png"/>已设置
                                                </span>
                                                <%--<span class="tips-color">--%>
                                                    <%--<img src="${resPrefixUrl }/images/index/tip.png"/> 未设置--%>
                                                <%--</span>--%>
                                                <a class="personal-edit" href="${ctx}/console/account/safety/index_psw">修改</a>
                                            </div>
                                        </div>
                                        <div class="row dotted-line personal-box">
                                            <div class="col-md-1 account-icon "><i class="personal-phone-icon"></i>
                                            </div>
                                            <div class="col-md-9 ">
                                                    <span class="help-block"><a href="#">手机绑定</a><br/>
                                                        <c:if test="${ safetyVo.isMobile==1}" > <small class="help-small" id="mobileOld">您已经绑定了手机  ${ safetyVo.mobile}</small></c:if>
                                                    </span>
                                            </div>
                                            <div class="col-md-2 right">

                                                <c:if test="${ safetyVo.isMobile==1}" ><span><img src="${resPrefixUrl }/images/index/ok_green.png"/>已设置</span>  </c:if>
                                            <c:if test="${ safetyVo.isMobile==-1}" > <span class="tips-color"><img src="${resPrefixUrl }/images/index/tip.png"/> 未设置</span>  </c:if>
                                                <a class="personal-edit showMobilebox">修改</a>
                                            </div>
                                        </div>
                                        <div class="row dotted-line personal-box ">
                                            <div class="col-md-1 account-icon "><i class="personal-email-icon"></i>
                                            </div>
                                            <div class="col-md-9 ">
                                                    <span class="help-block"><a href="#">邮箱绑定</a><br/>
                                                        <small class="help-small">您可以绑定邮箱地址，绑定邮箱地址后，可使用邮箱地址登陆，并可接受来自云呼你相关邮件通知</small>
                                                    </span>
                                            </div>
                                            <div class="col-md-2 right">
                                                <c:if test="${ safetyVo.isEmail==1}" ><span><img src="${resPrefixUrl }/images/index/ok_green.png"/>已设置</span> </c:if>
                                                    <c:if test="${ safetyVo.isEmail==-1}" ><span class="tips-color"><img src="${resPrefixUrl }/images/index/tip.png"/> 未设置</span> </c:if>
                                                <a href="" class="personal-edit" data-toggle="modal"
                                                   data-target="#emailModal">修改</a>
                                            </div>
                                        </div>
                                        <div class="row  personal-box">
                                            <div class="col-md-1 account-icon "><i class="personal-protect-icon"></i>
                                            </div>
                                            <div class="col-md-9 ">
                                                    <span class="help-block "><a href="#">密码保护</a><br/>
                                                        <small class="help-small">设置3个容易记住且最不容易被他人获取的问题及答案，可用于密码找回</small>
                                                    </span>
                                            </div>
                                            <div class="col-md-2 right">
                                                <c:if test="${ safetyVo.isPrivate==1}" ><span><img src="${resPrefixUrl }/images/index/ok_green.png"/>已设置</span></c:if>
                                                <c:if test="${ safetyVo.isPrivate==-1}" ><span class="tips-color"><img src="${resPrefixUrl }/images/index/tip.png"/> 未设置</span></c:if>
                                                <a href="" class="personal-edit" >修改</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </section>
                        </section>
                    </section>
                </aside>
            </section>
            <a href="#" class="hide nav-off-screen-block" data-toggle="class:nav-off-screen" data-target="#nav"></a>
        </section>
    </section>
</section>
</section>



<!---mobilebox-->
<div class="shadow-bg" id="show-bg"></div>
<div id="mobilebox" class="modal-box" style="display: none;">
    <div class="addmobile1" style="display: ">
        <div class="title">操作确认<a class="close_a modalCancel"></a></div>
        <div class="content" >
            <div class="tips-box">
                确认是本账号操作，请再次输入用户名密码
            </div>
            <div class="input">
                <input class=" form-control password" type="password" name="" placeholder="请输入当前登录用户密码   " />
            </div>
            <div class="input">
                <div class="tips-error moadltips1" style="display: none">密码有误</div>
            </div>
        </div>
        <div class="footer">
            <a  class="cancel modalCancel">返回</a>
            <a  class="sure modalSuer1">确认</a>
        </div>
    </div>


    <div class="addmobile2" style="display: none">
        <div class="title">重新绑定手机号<a class="close_a modalCancel"></a></div>
        <div class="content">
            <div class="margintop30"></div>
            <div class="input ">
                <input class=" form-control modalMobile " type="text" id="mobile" placeholder="输入手机号码" maxlength="11" />
            </div>
            <div class="input">
                <input class="code form-control modalCode " type="text" name="" placeholder="验证码" />
                <button class="code-button" id="send-code" >发送验证码</button>
            </div>
            <div class="in-block" id="second-codeblock" ></div>
            <p class="tips-error moadltips2 text-center" style="display: none">请先填写手机号码</p>

        </div>
        <div class="footer">
            <a class="cancel modalCancel ">返回</a>
            <a class="sure modalSuer2 ">确认</a>
        </div>
    </div>
</div>

<div class="tips-toast"></div>
<%@include file="/inc/footer.jsp"%>
<script src="${resPrefixUrl }/js/personal/account.js"></script><!--must-->
<!--must-->
<script>
    var isVc = false;//是否需要图形验证码
    var sendCodeResult=false;
    function sendCode(){
        var sendResult = false; //是否发送手机验证码成功
        var vCode = "";
        if(isVc){
            //校验四位验证码是否正确
            if($('#second-code').length>0){
                var second = $('#second-code').val();
                if(second.length!=4){
                    showmsg('请输入四位验证码','moadltips2'); return false;
                }
            }
            vCode = $("#second-code").val();
        }
        var mobile = $('#mobile').val();
        $.ajax({
            url : "${ctx}/mc/send",
            type : 'get',
            async: false,//使用同步的方式,true为异步方式
            data : {'mobile':mobile,'validateCode':vCode,'${_csrf.parameterName}':'${_csrf.token}'},//这里使用json对象
            dataType: "json",
            success : function(result){
                if(result.flag){
                    showmsg('发送短信验证码成功','moadltips2');
                    $('#second-codeblock').html('');
                    sendResult = true;
                }else if(result.vc){
                    sendResult = false;
                    //发送不成功，且要输入图形验证码
                    showmsg(result.err,'moadltips2');
                    isVc = true;
                    //启动二次校验
                    var html = '<div class="input mb-0 mt-0"><input class="code form-control " type="text" name="" id="second-code" placeholder="图形验证码"/>';
                    html += '&nbsp;&nbsp;<a class="code-img"><img src="${ctx}/vc/get?dt='+ new Date() +'" onclick="changeImgCode()"  id="imgValidateCode" ></a></div>';
                    $('#second-codeblock').html(html);

                }else{
                    sendResult = false;
                    showmsg(result.err,'moadltips2');
                }
            },
            fail:function(){
                showmsg("网络异常，请稍后重试",'moadltips2');
            }
        });
        sendCodeResult =  sendResult;
        return sendResult;
    }

    $('.modalSuer1').click(function(){
        var psw = $('.password').val();
        if(psw.length<6 || psw.length>18){
            showmsg('密码必须大于6，小于18个字符','moadltips1');return;
        }
        //验证密码
        $.ajax({
            url : "${ctx}/console/account/safety/validation_psw",
            type : 'post',
            async: false,//使用同步的方式,true为异步方式
            data : {'oldPws':psw,'${_csrf.parameterName}':'${_csrf.token}'},//这里使用json对象
            dataType: "json",
            success : function(data){
                if(data.sucess==2) {
                    showmsg(data.msg,'moadltips2');
                    $('.addmobile1').hide();
                    $('.addmobile2').show();
                }else{
                    showmsg(data.msg,'moadltips1');
                }
            },
            fail:function(){
                showmsg('密码验证失败，请重试','moadltips1');
            }
        });
    });

    $('.modalSuer2').click(function(){
        if(!regMobile()){
            showmsg('请输入正确的手机格式','moadltips2'); return false;
        }
        if(!sendCodeResult){
            showmsg('尚未发送短信验证码','moadltips2'); return false;
        }
        var code = $('.modalCode').val();
        if(code.length!=4){
            showmsg('请输入四位数的验证码','moadltips2'); return false;
        }
        var mobile = $('#mobile').val();
        $.ajax({
            url : "${ctx}/mc/check",
            type : 'get',
            async: false,//使用同步的方式,true为异步方式
            data : {'mc':code,"mobile":mobile,'${_csrf.parameterName}':'${_csrf.token}'},//这里使用json对象
            dataType: "json",
            success : function(data){
                if(data.flag){
                    //开始绑定手机号码
                    $.ajax({
                        url : "${ctx}/console/account/safety/edit_mobile",
                        type : 'post',
                        async: false,//使用同步的方式,true为异步方式
                        data : {'mobile':mobile,'${_csrf.parameterName}':'${_csrf.token}'},//这里使用json对象
                        dataType: "json",
                        success : function(result){
                            if(result.sucess==2) {
                                $('#mobileOld').html("您已经绑定了手机 "+mobile.substring(0,3)+"****"+mobile.substring(7,11));
                                $('.cancel').click();
                                showtoast(result.msg);
                            }else{
                                showmsg('修改手机号码失败','moadltips2');
                            }
                        },
                        fail:function(){
                            showmsg('网络异常，请稍后重试','moadltips2');
                        }
                    });
                }else{
                    showmsg(data.err,'moadltips2');
                }
            },
            fail:function(){
                showmsg('网络异常，请稍后重试','moadltips2');
            }
        });
    });

    function changeImgCode(){
        $("#imgValidateCode").prop("src",  '${ctx}'+"/vc/get?dt="+new Date());
    }

</script>


</body>
</html>