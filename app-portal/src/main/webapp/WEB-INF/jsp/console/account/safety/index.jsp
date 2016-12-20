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
        <%@include file="/inc/leftMenu.jsp"%>
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
                        <div class="head-box"><a href="#subNav" data-toggle="class:hide">
                            <i class=" iconfont icon-menu-left text"></i>
                            <i class=" iconfont icon-menu-right text-active"></i>
                        </a></div>
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
                                                            <span><img src="${resPrefixUrl }/images/index/ok_green.png"/><a  href="${ctx}/console/account/auth/index">已认证</a></span>
                                                        </c:if>
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
                                                    <span class="help-block"><font style="color: #428bca;">登录密码</font><br/>
                                                        <small class="help-small">安全性高的密码可以使账号更安全，设置一个由字母、符号或数字组成的密码</small>
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
                                                    <span class="help-block"><font style="color: #428bca;">手机绑定</font><br/>
                                                        <c:if test="${ safetyVo.isMobile==1}" > <small class="help-small" id="mobileOld">您已经绑定了手机  ${ safetyVo.mobile}</small></c:if>
                                                    </span>
                                            </div>
                                            <div class="col-md-2 right">

                                                <c:if test="${ safetyVo.isMobile==1}" ><span><img src="${resPrefixUrl }/images/index/ok_green.png"/>已设置</span>  </c:if>
                                            <c:if test="${ safetyVo.isMobile==-1}" > <span class="tips-color"><img src="${resPrefixUrl }/images/index/tip.png"/> 未设置</span>  </c:if>
                                                <a class="personal-edit showMobilebox" data-type="2">修改</a>
                                            </div>
                                        </div>
                                        <div class="row dotted-line personal-box ">
                                            <div class="col-md-1 account-icon "><i class="personal-email-icon"></i>
                                            </div>
                                            <div class="col-md-9 ">
                                                    <span class="help-block"><font style="color: #428bca;">邮箱绑定</font> <c:if test="${ safetyVo.isEmail==0}" ><i class="fa fa-exclamation-triangle"></i>变更邮箱[${modifyEmail}]的过程中，只有新邮箱被重新认证才能启用，未验证之前请使用旧邮箱登录</span></c:if><br/>
                                                        <c:if test="${ safetyVo.isEmail!=-1}" ><small class="help-small">您已经绑定了邮箱  ${ safetyVo.email}</small></c:if>
                                                        <c:if test="${ safetyVo.isEmail==-1}" ><small class="help-small">您可以绑定邮箱地址，绑定邮箱地址后，可使用邮箱地址登陆，并可接受来自壹耘相关邮件通知</small></c:if>
                                                    </span>
                                            </div>
                                            <div class="col-md-2 right">
                                                <c:if test="${ safetyVo.isEmail!=-1}" ><span><img src="${resPrefixUrl }/images/index/ok_green.png"/>已设置</span> </c:if>
                                                <c:if test="${ safetyVo.isEmail==-1}" ><span class="tips-color"><img src="${resPrefixUrl }/images/index/tip.png"/> 未设置</span> </c:if>
                                                <a  class="personal-edit showMobilebox" data-type="3">修改</a>
                                            </div>
                                        </div>
                                        <%--<div class="row  personal-box">--%>
                                            <%--<div class="col-md-1 account-icon "><i class="personal-protect-icon"></i>--%>
                                            <%--</div>--%>
                                            <%--<div class="col-md-9 ">--%>
                                                    <%--<span class="help-block "><a href="#">密码保护</a><br/>--%>
                                                        <%--<small class="help-small">设置3个容易记住且最不容易被他人获取的问题及答案，可用于密码找回</small>--%>
                                                    <%--</span>--%>
                                            <%--</div>--%>
                                            <%--<div class="col-md-2 right">--%>
                                                <%--<c:if test="${ safetyVo.isPrivate==1}" ><span><img src="${resPrefixUrl }/images/index/ok_green.png"/>已设置</span></c:if>--%>
                                                <%--<c:if test="${ safetyVo.isPrivate==-1}" ><span class="tips-color"><img src="${resPrefixUrl }/images/index/tip.png"/> 未设置</span></c:if>--%>
                                                <%--<a href="" class="personal-edit" >修改</a>--%>
                                            <%--</div>--%>
                                        <%--</div>--%>
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
    <div class="addmobile1" style="">
        <div class="title">操作确认<a class="close_a modalCancel cancelclear"></a></div>
        <div class="content" >
            <div class="tips-box">
                确认是本账号操作，请再次输入用户名密码
            </div>
            <div class="input">
                <input class=" form-control password" type="password" name="" id="password" placeholder="请输入当前登录用户密码   " />
            </div>
            <div class="input">
                <div class="tips-error moadltips1" id="moadltips1" style="display: none">密码有误</div>
            </div>
            <input type="hidden" value="3" id="modaltype" />
        </div>
        <div class="footer">
            <a  class="cancel modalCancel cancelclear">返回</a>
            <a  class="sure modalSuer1">确认</a>
        </div>
    </div>


    <div class="addmobile2" style="display: none">
        <div class="title">重新绑定手机号<a class="close_a modalCancel cancelclear"></a></div>
        <div class="content">
            <div class="margintop30"></div>
            <div class="input ">
                <input class=" form-control modalMobile " type="text" id="mobile" placeholder="输入手机号码" maxlength="11" />
            </div>

            <div id="second-codeblock" ></div>

            <div class="input-inline-block">
                <input class="code form-control modalCode " type="text" name="" id="yzm1"placeholder="手机验证码" />
                <button class="code-button" id="send-code"data-type="2" >获取验证码</button>
            </div>

            <p class="tips-error moadltips2 text-center" style="display: none">请先填写手机号码</p>
        </div>
        <div class="footer">
            <a class="cancel modalCancel cancelclear">返回</a>
            <a class="sure modalSuer2 ">确认</a>
        </div>
    </div>
    <div class="addmobile3" style="display:none ">
        <div class="title">重新邮箱地址<a class="close_a modalCancel cancelclear"></a></div>
        <div class="content">
            <div class="margintop30"></div>
            <div class="input ">
                <input class=" form-control modalEmail" type="text" name="" id="email"  placeholder="输入新的邮箱地址"  />
            </div>
            <div class="input">
                <a  id="send-code-email" data-type="3">发送邮箱验证链接</a>
            </div>
            <div class="in-block" id="second-codeblock-email" ></div>

            <p class="tips-error moadltips3 text-center" style="display: none">请输入正确的邮箱地址</p>

        </div>
        <div class="footer">
            <a class="cancel modalCancel cancelclear">返回</a>
            <a class="sure modalSuer3">确认</a>
        </div>
    </div>

</div>

<%@include file="/inc/footer.jsp"%>
<script src="${resPrefixUrl }/js/personal/account.js"></script><!--must-->
<!--must-->
<script>
    function sendEmailCode(){
        var email = $('#email').val();
        var param = {'email':email,csrfParameterName:csrfToken};
        ajaxsync(ctx+"/console/account/safety/modify_email_bind",param,function(result){
          if(result.success){
              showmsg('已发送成功<span style="color:orange;font-size:20px">12小时</span>内有效','moadltips3');
              return true;
          }else{
              showmsg(result.errorMsg,'moadltips3'); return false;
          }
        });
    }

    $(".cancelclear").click(function(){
        $('#password').val("");
        $('#email').val("");
        $('#mobile').val("");
        $('#yzm1').val("");
        $('#moadltips1').html("");
        $('#second-code').val("");
    });
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
        var param = {'mobile':mobile,'validateCode':vCode,csrfParameterName:csrfToken};
        ajaxsync(ctx+"/mc/send",param,function(response){
            if(response.data.flag){
                showmsg('发送短信验证码成功','moadltips2');
                $('#second-codeblock').html('');
                sendResult = true;
            }else if(response.data.vc){
                sendResult = false;
                //发送不成功，且要输入图形验证码
                showmsg(response.data.err,'moadltips2');
                isVc = true;
                //启动二次校验
                var html = '<div class="input-inline-block"><input class="code form-control " type="text" name="" id="second-code" placeholder="图形验证码"/>';
                html += '&nbsp;&nbsp;<a class="code-img"><img src="${ctx}/vc/get?dt='+ new Date() +'" onclick="changeImgCode()"  id="imgValidateCode" ></a></div>';
                $('#second-codeblock').html(html);

            }else{
                sendResult = false;
                showmsg(response.data.err,'moadltips2');
            }
        },"get");
        sendCodeResult =  sendResult;
        return sendResult;
    }

    $('.modalSuer1').click(function(){
        var psw = $('.password').val();
        if(psw.length<6 || psw.length>18){
            showmsg('密码必须大于6，小于18个字符','moadltips1');return;
        }
        var type = $('#modaltype').val();
        //验证密码
        var param = {'oldPws':psw,csrfParameterName:csrfToken};
        ajaxsync(ctx+"/console/account/safety/validation_psw",param,function(data){
            if(data.sucess==2) {
                //showmsg(data.msg,'moadltips2');
                $('.addmobile1').hide();
                $('.addmobile'+type).show();
            }else{
                showmsg(data.msg,'moadltips1');
            }
        })
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
        var param = {'mc':code,"mobile":mobile};
        ajaxsync(ctx+"/mc/check",param,function(response){
            if(response.data.flag){
                //开始绑定手机号码
                param = {'mobile':mobile,csrfParameterName:csrfToken};
                ajaxsync(ctx+"/console/account/safety/edit_mobile",param,function(result){
                    if(result.success){
                        $('#mobileOld').html("您已经绑定了手机 "+mobile.substring(0,3)+"****"+mobile.substring(7,11));
                        $('.cancel').click();
                        showtoast("新手机绑定成功！");
                    }else{
                        showmsg(result.errorMsg,'moadltips2');
                    }
                })
            }else{
                showmsg(response.data.err,'moadltips2');
            }
        },"get");
    });

    function changeImgCode(){
        $("#imgValidateCode").prop("src",  '${ctx}'+"/vc/get?dt="+new Date());
    }
    $('.modalSuer3').click(function(){
        window.location.href=ctx+"/console/account/safety/index";
        $('.cancel').click();
    });
</script>


</body>
</html>