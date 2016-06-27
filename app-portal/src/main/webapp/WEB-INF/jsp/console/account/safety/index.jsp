<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@include file="/inc/import.jsp" %>
<!DOCTYPE html>
<html>

<!-- header -->
<head>
    <%@include file="/inc/meta.jsp" %>

</head>
<body>
<%@include file="/inc/headerNav.jsp"%>
<section class='aside-section'>
    <section class="hbox stretch">
        <!-- .aside -->
        <aside class="bg-Green lter aside hidden-print include" data-include="aside" id="nav"><%@include file="/inc/leftMenu.jsp"%></aside>
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
                                                <a href="${ctx}/console/account/information/index">基本资料</a>
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
                                                        登陆账号(会员名称)：
                                                    </div>
                                                    <div class="col-md-10 ">
                                                        <span>流水行云科技</span>
                                                        <span><img src="${resPrefixUrl }/images/index/renzheng.png"/><a
                                                                href="${ctx}/console/account/auth/index">点击进行实名认证</a></span>
                                                        <span><img src="${resPrefixUrl }/images/index/ok_green.png"/> 已认证</span>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-md-1 remove-padding width-130">
                                                        账号ID：
                                                    </div>
                                                    <div class="col-md-10 ">
                                                        <span>GZ005001</span>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-md-1 remove-padding width-130">
                                                        注册时间：
                                                    </div>
                                                    <div class="col-md-10 ">
                                                        <span>2016-10-10 22:30:00</span>
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
                                                <span><img src="${resPrefixUrl }/images/index/ok_green.png"/>已设置</span>
                                                <span class="tips-color"><img src="${resPrefixUrl }/images/index/tip.png"/> 未设置</span>
                                                <a class="personal-edit" href="${ctx}/console/account/safety/index_psw">修改</a>
                                            </div>
                                        </div>
                                        <div class="row dotted-line personal-box">
                                            <div class="col-md-1 account-icon "><i class="personal-phone-icon"></i>
                                            </div>
                                            <div class="col-md-9 ">
                                                    <span class="help-block"><a href="#">手机绑定</a><br/>
                                                        <small class="help-small">您已经绑定了手机  139*****8693</small>
                                                    </span>
                                            </div>
                                            <div class="col-md-2 right">
                                                <span><img src="${resPrefixUrl }/images/index/ok_green.png"/>已设置</span>
                                                <span class="tips-color"><img src="${resPrefixUrl }/images/index/tip.png"/> 未设置</span>
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
                                                <span><img src="${resPrefixUrl }/images/index/ok_green.png"/>已设置</span>
                                                <span class="tips-color"><img src="${resPrefixUrl }/images/index/tip.png"/> 未设置</span>
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
                                                <span><img src="${resPrefixUrl }/images/index/ok_green.png"/>已设置</span>
                                                <span class="tips-color"><img src="${resPrefixUrl }/images/index/tip.png"/> 未设置</span>
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




<!---mobilebox-->
<div class="shadow-bg" id="show-bg"  style="display: none"></div>
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
                <input class=" form-control modalMobile" type="text" name="" placeholder="输入手机号码" maxlength="11" />
            </div>
            <div class="input">
                <input class="code form-control modalCode" type="text" name="mobile" placeholder="验证码" />
                <button class="code-button" id="send-code" >发送验证码</button>
            </div>
            <div class="input">
                <div class="tips-error moadltips2" style="display: none">请先填写手机号码</div>
            </div>
        </div>
        <div class="footer">
            <a class="cancel modalCancel">返回</a>
            <a class="sure modalSuer2">确认</a>
        </div>
    </div>

</div>





<script src="${resPrefixUrl }/js/app.v2.js"></script> <!-- Bootstrap --> <!-- App -->
<script src="${resPrefixUrl }/js/charts/flot/jquery.flot.min.js" cache="false"></script>
<script src="${resPrefixUrl }/js/bootbox.min.js"></script>
<script src="${resPrefixUrl }/js/charts/flot/demo.js" cache="false"></script>
<script src="${resPrefixUrl }/js/include.js"></script>
<script src="${resPrefixUrl }/bower_components/bootstrapvalidator/dist/js/bootstrapValidator.min.js"></script>
<script src="${resPrefixUrl }/s/personal/account.js"></script><!--must-->
<!--must-->
<script>
    function sendCode(){
        //todo
        alert("发送验证码");
    }
<<<<<<< Updated upstream

=======
    function  safetyPost(url,param) {
        alert(param);

        alert(2);
    }
>>>>>>> Stashed changes
    $('.modalSuer1').click(function(){
        var psw = $('.password').val();
        if(psw.length<6 || psw.length>18){
            showmsg('密码必须大于6，小于18个字符','moadltips1');return;
        }
<<<<<<< Updated upstream
        alert('密码正确');
        $('.addmobile1').hide();
        $('.addmobile2').show();
=======
        var param = {};
        //验证密码
        $.ajax({
            url : "${ctx}/console/account/safety/validation_psw",
            type : 'post',
            async: false,//使用同步的方式,true为异步方式
            data : {'oldPws':psw,'${_csrf.parameterName}':'${_csrf.token}'},//这里使用json对象
            dataType: "json",
            success : function(data){
                alert(data.msg);
                alert(data.sucess)
                if(data.sucess==2) {
                    $('.addmobile1').hide();
                    $('.addmobile2').show();
                }
            },
            fail:function(){
                alert('密码验证失败，请重试')
            }
        });
        //alert('密码正确');

>>>>>>> Stashed changes
    });

    $('.modalSuer2').click(function(){
        if(!regMobile()){
            showmsg('请输入正确的手机格式','moadltips2'); return false;
        }
        var code = $('.modalCode').val();
        if(code.length!=4){
            showmsg('请输入四位数的验证码','moadltips2'); return false;
        }
<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
        alert('检验验证码是否正确 , 绑定手机');
        $('.cancel').click();
    });


</script>


</body>
</html>