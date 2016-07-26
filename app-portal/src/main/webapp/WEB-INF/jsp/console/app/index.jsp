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
                        <div class="wrapper header"><span class="margin_lr"></span><span class="margin_lr border-left">&nbsp;应用管理</span>
                        </div>
                        <section class="scrollable">
                            <div class="slim-scroll">
                                <!-- nav -->
                                <nav class="hidden-xs">
                                    <ul class="nav">
                                        <li>
                                            <div class="aside-li-a">
                                                <a href="${ctx}/console/app/list">应用列表</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a active">
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
                        <div class="head-box"><a href="#subNav" data-toggle="class:hide"> <i
                                class="fa fa-angle-left text"></i> <i class="fa fa-angle-right text-active"></i> </a>
                        </div>
                        <section class=" w-f application_create">
                            <div class="wrapper header">
                                <span class="border-left">&nbsp;
                                    <c:if test="${app.id==null}">创建应用</c:if>
                                    <c:if test="${app.id!=null}">修改应用</c:if>
                                </span></div>
                            <div class="row m-l-none m-r-none bg-light lter">
                                <div class="row">

                                    <form:form role="form" action="./index.html" method="post" class="register-form" id="application_create">
                                        <c:if test="${app.id!=null}">
                                            <input type="hidden" name="id" value="${app.id}">
                                            <input type="hidden" name="status" value="${app.status}">
                                        </c:if>
                                        <div class="form-group">
                                            <lable class="col-md-3 text-right">应用名称：</lable>
                                            <div class="col-md-4">
                                                <input type="text" name="name" value="${app.name}" placeholder="" class="form-control input-form limit20"/>
                                            </div>
                                            <span class="span-required">*</span>
                                        </div>
                                        <p class="tips">20字符以内，符合<a href="">应用审核规范</a></p>

                                        <div class="form-group">
                                            <lable class="col-md-3 text-right">应用描述：</lable>
                                            <div class="col-md-4">
                                                <input type="text" name="description" value="${app.description}" placeholder="" class="form-control input-form "/>
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
                                                <select name="industry"  class="form-control notEmpty">
                                                    <option value="">请选择行业</option>
                                                    <c:set var="industry" value="${app.industry}"></c:set>
                                                    <%@ include file="/inc/industry.jsp"%>
                                                </select>
                                            </div>
                                            <span class="span-required">*</span>
                                        </div>
                                        <div class="form-group">
                                            <lable class="col-md-3 text-right">服务器白名单：</lable>
                                            <div class="col-md-4">
                                                <input type="text" name="whiteList" value="${app.whiteList}" placeholder="" class="form-control input-form "/>
                                            </div>
                                        </div>
                                        <p class="tips">
                                            允许IP地址，以英文输入法分号分隔，例如，8.8.8.8; 8.8.8.8 设定白名单地址后，云呼你服务器在识别该应用请求时将只接收白名单内服务器发送的请求，能有效提升账号安全性。如未设置默认不生效
                                        </p>
                                        <div class="form-group">
                                            <lable class="col-md-3 text-right">回调URL：</lable>
                                            <div class="col-md-4">
                                                <input type="text" name="url" value="${app.url}" placeholder="" class="form-control input-form"/>
                                            </div>
                                        </div>

                                        <p class="tips">
                                            <input type="checkbox" name="isAuth" <c:if test="${app.isAuth=='1'}">checked='checked'</c:if>> 鉴权 (网络直拨，回拨，互联网语音，视频通话会涉及鉴权流程，勾选但未实现会呼叫失效)
                                            <a href="">回调说明文档</a> </a>
                                        </p>
                                        <div class="form-group min-height20">
                                            <span class="hr text-label" ><strong>选择服务:</strong></span>
                                        </div>
                                        <div class="form-group app-createbox border-bottom">
                                            <lable class="col-md-3 text-right"></lable>
                                            <div class="col-md-9" >
                                                <p><strong>基础语音服务</strong></p>
                                                <p><input type="checkbox" name="isVoiceDirectly" <c:if test="${app.status=='1'}"> disabled="disabled" </c:if> <c:if test="${app.isVoiceDirectly=='1'}">checked='checked'</c:if>> 启用 &nbsp;&nbsp;语音外呼(嵌入CRM、OA、呼叫中心等产品中发起通话)</p>
                                                <p><input type="checkbox" name="isVoiceCallback" <c:if test="${app.status=='1'}"> disabled="disabled" </c:if> <c:if test="${app.isVoiceCallback=='1'}">checked='checked'</c:if>> 启用 &nbsp;&nbsp;双向回拨(以不同的通话方式实现茂名通话功能，保护双方号码隐私)</p>

                                            </div>
                                        </div>
                                        <div class="form-group app-createbox" >
                                            <lable class="col-md-3 text-right"></lable>
                                            <div class="col-md-9" >
                                                <p><strong>高级语音定制服务</strong></p>
                                                <p><input type="checkbox" name="isSessionService" <c:if test="${app.status=='1'}"> disabled="disabled" </c:if> <c:if test="${app.isSessionService=='1'}">checked</c:if>> 启用 &nbsp;&nbsp;<a href="">会议服务</a>(可与互联网会议、视频 会议融合参会，提供丰富的会议管理功能)</p>
                                                <p><input type="checkbox" name="isRecording" <c:if test="${app.status=='1'}"> disabled="disabled" </c:if>  <c:if test="${app.isRecording=='1'}">checked</c:if>> 启用 &nbsp;&nbsp;<a>录音服务</a>(以不同的通话方式实现茂名通话功能，保护双方号码隐私)</p>
                                                <p><input type="checkbox" name="isVoiceValidate" <c:if test="${app.status=='1'}"> disabled="disabled" </c:if> <c:if test="${app.isVoiceValidate=='1'}">checked</c:if>> 启用 &nbsp;&nbsp;<a>语音验证码</a>(嵌入CRM、OA、呼叫中心等产品中发起通话)</p>
                                                <p><input type="checkbox" name="isIvrService" <c:if test="${app.status=='1'}"> disabled="disabled" </c:if> <c:if test="${app.isIvrService=='1'}">checked</c:if>> 启用 &nbsp;&nbsp;<a>IVR定制服务</a>(以不同的通话方式实现茂名通话功能，保护双方号码隐私)</p>
                                                <div class="tips ml-36">
                                                    <p class="app-tips ">IVR定制服务开启后，该应用将产生100元/月的功能费，上线时开始收取，多个应用开启并上线会叠加收费</p>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <div class="col-md-9">
                                                <a id="validateBtn" class="validateBtnNormal btn btn-primary  btn-form">
                                                    <c:if test="${app.id==null}">创建</c:if>
                                                    <c:if test="${app.id!=null}">修改</c:if>
                                                </a>
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
<div class="tips-toast"></div>
<div class="tips-toast"></div>
<%@include file="/inc/footer.jsp"%>
<script type="text/javascript" src='${resPrefixUrl }/js/application/create.js'></script>

<script>
    $('#validateBtn').click(function(){
        $('#application_create').bootstrapValidator('validate');
        var result = $('#application_create').data('bootstrapValidator').isValid();
        if(result==true){
            $('#validateBtn').attr('disabled','disabled');
            var tempType = $('#validateBtn').html().trim()=='创建'?"create":"update";
            //提交表单
            $.ajax({
                url : "${ctx}/console/app/"+tempType,
                type : 'post',
                async: false,//使用同步的方式,true为异步方式
                data :getFormJson("#application_create"),
                dataType: "json",
                success : function(data){
                    var url = "";
                    if($('#validateBtn').html().trim()=='创建'){
                        url="${ctx}/console/app/list";
                    }else{
                        url="${ctx}/console/app/detail?id=${app.id}";
                    }
                    showtoast(data.msg,url);
                },
                fail:function(){
                    showtoast('网络异常，请稍后重试');
                    $('#validateBtn').removeAttr('disabled');
                }
            });
        }

    });
</script>

</body>
</html>
