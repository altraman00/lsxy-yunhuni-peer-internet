<%@ taglib prefix="s" uri="http://java.sun.com/jsp/jstl/core" %>
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
                        <div class="wrapper header"><span class="margin_lr"></span><span class="margin_lr border-left">&nbsp;应用管理</span>
                        </div>
                        <section class="scrollable">
                            <div class="slim-scroll">
                                <!-- nav -->
                                <nav class="hidden-xs">
                                    <ul class="nav">
                                        <li>
                                            <div class="aside-li-a <c:if test="${app.id!=null}">active</c:if> ">
                                                <a href="${ctx}/console/app/list">应用列表</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a <c:if test="${app.id==null}">active</c:if> ">
                                                <a href="${ctx}/console/app/index">创建应用</a>
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
                        <section class=" w-f application_create">
                            <div class="wrapper header">
                                <span class="border-left">&nbsp;
                                    <c:if test="${app.id==null}">创建应用</c:if>
                                    <c:if test="${app.id!=null}">编辑应用</c:if>
                                </span></div>
                            <div class="row m-l-none m-r-none bg-light lter">
                                <div class="row">

                                    <form:form role="form" action="./index.html" method="post" class="register-form" id="application_create">
                                        <c:if test="${app.id!=null}">
                                            <input type="hidden" name="id" value="${app.id}">
                                            <input type="hidden" name="status" value="${app.status}">
                                            <c:if test="${app.status==1}">
                                                <p class="app-tips ">编辑应用的选择服务项目需要下线应用，才能编辑</p>
                                            </c:if>
                                        </c:if>
                                        <div class="form-group">
                                            <lable class="col-md-3 text-right">应用名称：</lable>
                                            <div class="col-md-4">
                                                <input type="text" name="name"  value="${app.name}" placeholder="" class="form-control input-form limit20" onchange="countName(this)" id="checkNameByCountNameInput"/>
                                                <small class="help-block"data-bv-for="limit20" style="color: red" id="checkNameByCountName"></small>
                                            </div>
                                            <span class="span-required">*</span>
                                        </div>
                                        <p class="tips">20字符内，符合<a href="${globalWebsite}/develop/book/platform?page=application" target="_blank">应用审核规范要求</a></p>

                                        <div class="form-group">
                                            <lable class="col-md-3 text-right">应用描述：</lable>
                                            <div class="col-md-4">
                                                <input type="text" name="description" value="${app.description}" placeholder="" class="form-control input-form limit300"/>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <lable class="col-md-3 text-right">应用类型：</lable>
                                            <div class="col-md-4 ">
                                                <select name="type" class="form-control notEmpty">
                                                    <option value="">请选择应用类型</option>
                                                    <option value="网页" <c:if test="${app.type=='网页'}">selected</c:if>>网页</option>
                                                    <option value="PC" <c:if test="${app.type=='PC'}">selected</c:if>>PC</option>
                                                    <option value="移动" <c:if test="${app.type=='移动'}">selected</c:if>>移动</option>
                                                    <option value="其他" <c:if test="${app.type=='其他'}">selected</c:if>>其他</option>
                                                </select>
                                            </div>
                                            <span class="span-required">*</span>
                                        </div>
                                        <div class="form-group">
                                            <lable class="col-md-3 text-right">所属行业：</lable>
                                            <div class="col-md-4 ">
                                                <select name="industry" class="form-control notEmpty" >
                                                    <option value="">请选择所属行业</option>
                                                    <c:set var="industry" value="${app.industry}"></c:set>
                                                    <%@ include file="/inc/industry.jsp"%>
                                                </select>
                                            </div>
                                            <span class="span-required">*</span>
                                        </div>
                                        <div class="form-group">
                                            <lable class="col-md-3 text-right">服务器白名单：</lable>
                                            <div class="col-md-4">
                                                <input type="text" name="whiteList" value="${app.whiteList}" placeholder="" class="form-control input-form whiteList"/>
                                            </div>
                                        </div>
                                        <p class="tips">
                                            允许IP地址，以英文输入法分号分隔，例如：8.8.8.8; 8.8.8.8。设定白名单地址后，壹耘服务器在识别该应用请求时将只接收白名单内服务器发送的请求，能有效提升账号安全性。 如未设置默认不生效
                                        </p>
                                        <div class="form-group">
                                            <lable class="col-md-3 text-right">回调URL：</lable>
                                            <div class="col-md-4">
                                                <input type="text" name="url" value="${app.url}" placeholder="" class="form-control input-form url" maxlength="300"/>
                                            </div>
                                            <span class="span-required">*</span>
                                        </div>
                                        <%--<p class="tips"><a href="">回调说明文档</a></p>--%>
                                        <%--<p class="tips">--%>
                                            <%--<input type="checkbox" name="isAuth" value="1" <c:if test="${app.isAuth=='1'}">checked='checked'</c:if>> 鉴权 (网络直拨,回拨,互联网语音,视频通话会涉及鉴权流程，勾选但未实现会呼叫失败)--%>
                                        <%--</p>--%>

                                        <div class="form-group height-10">
                                            <span class="hr"></span>
                                        </div>

                                        <p class="number_info">
                                            壹耘平台提供多种通信服务,其中：<br/>
                                            1、语音服务包含语音通知、语音回拨、语音会议、语音验证码和自定义IVR产品适用于快递发件提醒，房屋销售咨询，号码防泄漏，获取验证码等场景，提高企业办公效率，提高用户信息安全，提高验证码到达率；<br/>
                                            2、呼叫中心服务包含呼叫中心产品适用于企业为售前售后，业务咨询，业务订购，客户反馈投诉受理等而搭建的客服中心或销售中心。
                                        </p>
                                        <div class="form-group app-createbox" id="app-tag">
                                            <input type="hidden" name="serviceType" id="serviceType" value="msg">
                                            <div class="col-md-3 text-right"><span class="text-label" ><strong>选择服务:</strong></span></div>
                                            <div class="col-md-9" >
                                                <div class="btn app-tag active" data-app="msg"><i class="iconfont icon-xiaoxi"></i><span>消息</span></div>
                                                <div class="btn app-tag" data-app="voice"><i class="iconfont icon-call-voice"></i><span>语音</span></div>
                                                <div class="btn app-tag" data-app="call_center"><i class="iconfont icon-call-center" ></i><span>呼叫中心</span></div>
                                            </div>
                                        </div>
                                        <!--语音-->
                                        <div class="form-group app-createbox none app-tap-child" data-child="voice" >
                                            <lable class="col-md-3 text-right"></lable>
                                            <div class="col-md-9" >
                                                <p><input type="checkbox" name="isVoiceDirectly" value="1" <c:if test="${app.status=='1'}"> disabled="disabled" </c:if> <c:if test="${app.isVoiceDirectly=='1'}">checked='checked'</c:if>> 启用 &nbsp;&nbsp;<font style="color: #428bca;">语音通知</font>（自动拨打用户电话，并播报自定义的通知内容）</p>
                                                <p><input type="checkbox" name="isVoiceCallback" value="1" <c:if test="${app.status=='1'}"> disabled="disabled" </c:if> <c:if test="${app.isVoiceCallback=='1'}">checked='checked'</c:if>> 启用 &nbsp;&nbsp;<font style="color: #428bca;">语音回拨</font>（以不同的通话方式实现匿名通话功能,保护双方号码隐私）</p>
                                                <p><input type="checkbox" name="isSessionService" value="1" <c:if test="${app.status=='1'}"> disabled="disabled" </c:if> <c:if test="${app.isSessionService=='1'}">checked</c:if>> 启用 &nbsp;&nbsp;<font style="color: #428bca;">语音会议</font>（可与互联网会议、视频会议融合参会，提供丰富的会议管理功能）</p>
                                                    <%--<p><input type="checkbox" name="isRecording" value="1" <c:if test="${app.status=='1'}"> disabled="disabled" </c:if>  <c:if test="${app.isRecording=='1'}">checked</c:if>> 启用 &nbsp;&nbsp;<font style="color: #428bca;">通话录音</font>（提供通话录音、录音存储管理等功能）</p>--%>
                                                <p><input type="checkbox" name="isVoiceValidate" value="1" <c:if test="${app.status=='1'}"> disabled="disabled" </c:if> <c:if test="${app.isVoiceValidate=='1'}">checked</c:if>> 启用 &nbsp;&nbsp;<font style="color: #428bca;">语音验证码</font>（通过电话直呼到用户手机并语音播报验证码）</p>
                                                <p><input type="checkbox" name="isIvrService" value="1" <c:if test="${app.status=='1'}"> disabled="disabled" </c:if> <c:if test="${app.isIvrService=='1'}">checked</c:if>> 启用 &nbsp;&nbsp;<font style="color: #428bca;">自定义IVR</font>（即互动式语音应答，可根据IVR交互协议完成更加复杂的通讯业务功能）</p>
                                                <%--<div class="tips ml-36">--%>
                                                    <%--<p class="app-tips ">该选项开启后，将产生1000元的号码租用费以及100元/月的功能费，应用上线时开始收取，多个应用开启并上线会叠加收费</p>--%>
                                                <%--</div>--%>
                                            </div>
                                        </div>
                                        <!--消息-->
                                        <div class="form-group app-createbox app-tap-child" data-child="msg" >
                                            <lable class="col-md-3 text-right"></lable>
                                            <div class="col-md-9" >
                                                <p><input type="checkbox" name="isUssd" value="1" <c:if test="${app.status=='1'}"> disabled="disabled" </c:if> <c:if test="${app.isUssd=='1'}">checked</c:if>> 启用 &nbsp;&nbsp;<font style="color: #428bca;">闪印</font>（不惧拦截，批量发送，迅速抢占手机屏幕）</p>
                                                <p><input type="checkbox" name="isSms" value="1" <c:if test="${app.status=='1'}"> disabled="disabled" </c:if> <c:if test="${app.isSms=='1'}">checked</c:if>> 启用 &nbsp;&nbsp;<font style="color: #428bca;">短信</font>（免开发的短信发送平台，接入即用）</p>
                                            </div>
                                        </div>
                                        <!--呼叫中心-->
                                        <div class="form-group app-createbox none app-tap-child" data-child="call_center" >
                                            <lable class="col-md-3 text-right"></lable>
                                            <div class="col-md-9" >
                                                <p><input type="checkbox" name="isCallCenter" value="1" <c:if test="${app.status=='1'}"> disabled="disabled" </c:if> <c:if test="${app.isCallCenter=='1'}">checked</c:if>> 启用 &nbsp;&nbsp;<font style="color: #428bca;">呼叫中心</font>(专业的销售和客服中心，具备IVR导航、坐席管理、呼叫转移等全方位功能)</p>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="col-md-4 remove-padding">
                                                <a id="validateBtn" class="validateBtnNormal btn btn-primary  btn-form">
                                                    <c:if test="${app.id==null}">创建</c:if>
                                                    <c:if test="${app.id!=null}">修改</c:if>
                                                </a>
                                                <c:if test="${app.id!=null}">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a id="validateBtn2"  class="btn btn-primary  btn-form">取消</a></c:if>
                                            </div>
                                        </div>
                                    </form:form>
                                </div>
                            </div>
                        </section>
                    </section>
                </aside>
            </section>
        </section>
    </section>
</section>
</section>
<%@include file="/inc/footer.jsp"%>
<script type="text/javascript" src='${resPrefixUrl }/js/application/create.js'></script>

<script>
    var appId = '${app.id}';
    if(appId){
        var appServiceType = '${app.serviceType}';
        $('#serviceType').val(appServiceType);
        $('.app-tag').each(function () {
            if(appServiceType==$(this).attr('data-app')){
                $(this).addClass("active").siblings().removeClass("active");
            }else{
                $(this).hide();
            }
        })
        $('.app-tap-child').each(function(){
            if(appServiceType==$(this).attr('data-child'))
                $(this).fadeIn();
            else
                $(this).hide();
        });
    }
    $('#validateBtn2').click(function(){
        window.location.href="${ctx}/console/app/detail?id=${app.id}";
    });
    $('#validateBtn').click(function(){
        $('#application_create').bootstrapValidator('validate');
        var result = $('#application_create').data('bootstrapValidator').isValid();
        if(result==true){
            $('#validateBtn').attr('disabled','disabled');
            var tempType = $('#validateBtn').html().trim()=='创建'?"create":"update";
            //提交表单
            ajaxsync(ctx + "/console/app/"+tempType,getFormJson("#application_create"),function(response){
                if(response.success){
                    var url = "";
                    if($('#validateBtn').html().trim()=='创建'){
                        url="${ctx}/console/app/list";
                        showtoast("新建应用成功",url);
                    }else{
                        url="${ctx}/console/app/detail?id=${app.id}";
                        showtoast("应用修改成功",url);
                    }
                }else{
                    showtoast(response.errorMsg);
                    $('#validateBtn').removeAttr('disabled');
                }
            },"post").fail(function(){
                $('#validateBtn').removeAttr('disabled');
            });

        }

    });
    function countName(t){
        var value = t.value;
        if(value.trim().length>0){
            ajaxsync(ctx + "/console/app/count/name/"+value,{},function(response){
                if(response.success){
                    $('#checkNameByCountName').html('');
                    $('#checkNameByCountName').hide();
                    document.getElementById("checkNameByCountNameInput").style.borderColor="#468847";
                }else{
                    $('#checkNameByCountName').html('应用名称已存在');
                    $('#checkNameByCountName').show();
                    document.getElementById("checkNameByCountNameInput").style.borderColor="#b94a48";
                }
            },"post").fail(function(){
                $('#checkNameByCountName').html('');
                $('#checkNameByCountName').hide();
                document.getElementById("checkNameByCountNameInput").style.borderColor="#468847";
            });
        }
    }
    $(function () {
        $('#checkNameByCountName').html('');
        $('#checkNameByCountName').hide();
    })
</script>

</body>
</html>
