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
                                            <div class="aside-li-a active">
                                                <a href="${ctx}/console/app/list">应用列表</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a">
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
                        <div class="wrapper header">
                            <span class="border-left">&nbsp;应用详情</span>
                        </div>
                        <section class="scrollable wrapper w-f">
                            <section class="panel panel-default yunhuni-personal">
                                <div class="row m-l-none m-r-none bg-light lter">
                                    <div class="col-md-12 padder-v fix-padding">
                                        <a href="${ctx}/console/app/index?id=${app.id}" class="btn btn-primary query">应用编辑</a>
                                        <c:if test="${app.serviceType=='call_center'}">
                                            <%--<a href="" class="btn btn-call margin_left-20">呼叫中心管理</a>--%>
                                        </c:if>
                                    </div>
                                </div>
                            </section>
                                <!--新的应用详情-->
                                <section class="panel panel-default pos-rlt clearfix appliaction-detail">
                                    <div class="row">
                                        <div class="col-md-6 remove-padding-left">
                                            <div class="panel panel-default">
                                                <div class="panel-heading"><i class="iconfont icon-call-base"></i><span class="title">基础信息</span></div>
                                                <div class="panel-body basebox">
                                                    <div class="row ">
                                                        <div class="col-md-1 base">
                                                            应用名称：
                                                        </div>
                                                        <div class="col-md-6">
                                                            <p>
                                                                ${app.name}
                                                                <c:if test="${app.status==1}"><span style="color:#9dc940;">已上线</span></c:if>
                                                                <c:if test="${app.status==2}"><span class="text-danger">未上线</span></c:if>
                                                                <%--<input type="hidden" id="appStatus" value="${app.status}">--%>
                                                            </p>
                                                        </div>
                                                    </div>
                                                    <div class="row ">
                                                        <div class="col-md-1 base">
                                                            应用描述：
                                                        </div>
                                                        <div class="col-md-9">
                                                            <p><c:if test="${app.description==null||app.description==''}">无</c:if><c:if test="${app.description!=null}">${app.description}</c:if></p>
                                                        </div>
                                                    </div>
                                                    <div class="row ">
                                                        <div class="col-md-1 base">
                                                            应用类型：
                                                        </div>
                                                        <div class="col-md-6">
                                                            <p>${app.type}</p>
                                                        </div>
                                                    </div>
                                                    <div class="row ">
                                                        <div class="col-md-1 base">
                                                            所属行业：
                                                        </div>
                                                        <div class="col-md-6">
                                                            <p>${app.industry}</p>
                                                        </div>
                                                    </div>
                                                    <div class="row ">
                                                        <div class="col-md-1 base">
                                                            选择服务：
                                                        </div>
                                                        <div class="col-md-6">
                                                            <p>
                                                                <c:if test="${app.isVoiceDirectly=='1'}">语音通知&nbsp;</c:if>
                                                                <c:if test="${app.isVoiceCallback=='1'}">语音回拨&nbsp;</c:if>
                                                                <c:if test="${app.isSessionService=='1'}">语音会议&nbsp;</c:if>
                                                                <c:if test="${app.isVoiceValidate=='1'}">语音验证码&nbsp;</c:if>
                                                                <c:if test="${app.isIvrService=='1'}">自定义IVR&nbsp;</c:if>
                                                                <c:if test="${app.isCallCenter==1}">呼叫中心&nbsp;</c:if>
                                                                <c:if test="${app.isSms=='1'}">短信&nbsp;</c:if>
                                                                <c:if test="${app.isUssd==1}">闪印&nbsp;</c:if>
                                                            </p>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-6 remove-padding-left">
                                            <div class="panel panel-default">
                                                <div class="panel-heading"><i class="iconfont icon-call-developer"></i><span class="title">开发者信息</span></div>
                                                <div class="panel-body devbox">
                                                    <div class="row ">
                                                        <div class="col-md-1 dev">
                                                            应用标识：
                                                        </div>
                                                        <div class="col-md-6">
                                                            <p>${app.id}</p>
                                                        </div>
                                                    </div>
                                                    <div class="row ">
                                                        <div class="col-md-1 dev">
                                                            服务器白名单：
                                                        </div>
                                                        <div class="col-md-6">
                                                            <p><c:if test="${app.whiteList==null||app.whiteList==''}">无</c:if><c:if test="${app.whiteList!=null}">${app.whiteList}</c:if></p>
                                                        </div>
                                                    </div>
                                                    <%--<div class="row ">--%>
                                                        <%--<div class="col-md-1 dev">--%>
                                                            <%--监听通知：--%>
                                                        <%--</div>--%>
                                                        <%--<div class="col-md-6">--%>
                                                            <%--<p><c:if test="${app.isAuth=='1'}">启用监听    启用了鉴权</c:if>--%>
                                                                <%--<c:if test="${app.isAuth!='1'}">没有启动监听</c:if></p>--%>
                                                        <%--</div>--%>
                                                    <%--</div>--%>
                                                    <div class="row ">
                                                        <div class="col-md-1 dev">
                                                            回调URL：
                                                        </div>
                                                        <div class="col-md-8 ">
                                                            <p><span style="color:#428bca;">${app.url}</span></p>
                                                        </div>
                                                    </div>
                                                    <div class="row ">
                                                        <div class="col-md-1 dev">
                                                            绑定测试号：
                                                        </div>
                                                        <div class="col-md-8">
                                                            <p>
                                                                <span id="testNumBind"
                                                                    <c:if test="${testNumBindList==null || fn:length(testNumBindList)==0 || tempTestNum==0}">
                                                                        hidden
                                                                    </c:if>>
                                                                <c:set var="tempTestNum" value="0"></c:set>
                                                                <c:forEach items="${testNumBindList}" var="testNumBind">
                                                                    <c:if test="${testNumBind.app.id==app.id}">
                                                                        <c:set var="tempTestNum" value="${tempTestNum+1}"></c:set>
                                                                    </c:if>
                                                                </c:forEach>
                                                                    <c:forEach items="${testNumBindList}" var="testNumBind">
                                                                        <c:if test="${testNumBind.app.id==app.id}">
                                                                            <span name="testNum">${testNumBind.number} </span>
                                                                        </c:if>
                                                                    </c:forEach>
                                                                </span>
                                                                    <a
                                                                            <c:if test="${testNumBindList!=null && fn:length(testNumBindList)> 0}">
                                                                                class="modalShow" data-id="one"
                                                                            </c:if>
                                                                            <c:if test="${testNumBindList==null || fn:length(testNumBindList)== 0}">
                                                                                onclick="showTestNumBind()"
                                                                            </c:if>
                                                                    >绑定交互测试号</a>
                                                            </p>
                                                        </div>
                                                    </div>
                                                    <c:if test="${app.serviceType=='call_center'}">
                                                        <div class="row">
                                                            <div class="col-md-1 dev">
                                                                分机接入信息：
                                                            </div>
                                                            <div class="col-md-9">
                                                                <p>${sipRegistrar}</p>
                                                                <%--<c:forEach items="${appExtensionList}" var="appExtension">--%>
                                                                    <%--<c:if test="${appExtension.telenum==app.id}">--%>
                                                                        <%--<p>${appExtension.telenum} </p>--%>
                                                                    <%--</c:if>--%>
                                                                <%--</c:forEach>--%>
                                                                <%--<p class="text-danger">（上线后平台会重新分配分机接入信息）</p>--%>
                                                            </div>
                                                        </div>
                                                        <div class="row">
                                                            <div class="col-md-1 dev">
                                                                呼叫中心应用编号：
                                                            </div>
                                                            <div class="col-md-9">
                                                                <p>${app.callCenterNum}</p>
                                                                    <%--<c:forEach items="${appExtensionList}" var="appExtension">--%>
                                                                    <%--<c:if test="${appExtension.telenum==app.id}">--%>
                                                                    <%--<p>${appExtension.telenum} </p>--%>
                                                                    <%--</c:if>--%>
                                                                    <%--</c:forEach>--%>
                                                                    <%--<p class="text-danger">（上线后平台会重新分配分机接入信息）</p>--%>
                                                            </div>
                                                        </div>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </section>
                                <!--新的应用详情结束-->
                            <section class="panel panel-default pos-rlt clearfix application-tab">
                                <ul id="myTab" class="nav nav-tabs">
                                    <c:if test="${app.serviceType == 'voice'}"><%--语音--%>
                                        <li class="active" data-id="play"><a href="#play" data-toggle="tab">放音媒体库</a></li>
                                        <li data-id="voice"><a href="#voice" data-toggle="tab">录音文件</a></li>
                                        <li data-id="number"><a href="#number" data-toggle="tab">号码绑定</a></li>
                                        <li data-id="subAccount"><a href="#subAccount" data-toggle="tab">子账号</a></li>
                                    </c:if>
                                    <c:if test="${app.serviceType == 'msg'}"><%--消息--%>
                                        <li data-id="template" class="active"><a href="#template" data-toggle="tab">模板列表</a></li>
                                        <li data-id="subAccount" ><a href="#subAccount" data-toggle="tab">子账号</a></li>
                                    </c:if>
                                    <c:if test="${app.serviceType == 'call_center'}"><%--呼叫中心--%>
                                        <li data-id="extension" class="active"><a href="#extension" data-toggle="tab">分机列表</a></li>
                                        <li data-id="agent"><a href="#agent" data-toggle="tab">坐席列表</a></li>
                                        <li data-id="queue"><a href="#queue" data-toggle="tab">排队条件</a></li>
                                        <li data-id="play"><a href="#play" data-toggle="tab">放音媒体库</a></li>
                                        <li data-id="voice"><a href="#voice" data-toggle="tab">录音文件</a></li>
                                        <li data-id="number"><a href="#number" data-toggle="tab">号码绑定</a></li>
                                        <li data-id="subAccount"><a href="#subAccount" data-toggle="tab">子账号</a></li>
                                    </c:if>
                                    <li class="right" id="uploadButton" hidden><a href="#" id="uploadButtonA" class="btn btn-primary defind modalShow" data-id="four" >上传放音文件</a></li>
                                </ul>
                                <div id="myTabContent" class="tab-content" style="">
                                    <!-- 放音文件-->
                                    <div class="tab-pane fade<c:if test="${app.serviceType=='voice'}"> in active</c:if>" id="play">
                                        <p class="application_info">
                                            当您的应用需要开通语音通知、自定义IVR或者云呼叫中心时，请上传语音文件至放音媒体库，语音文件均需要审核
                                        </p>
                                        <div class="form-group">
                                            <div class="col-md-3 remove-padding"><input type="text" class="form-control" placeholder="文件名" id="name"/></div>
                                            <div class="col-md-3 remove-padding" style="padding-left:15px;">
                                                <input type="text" class="form-control" placeholder="关联子账号" id="voice_file_subId"/>
                                            </div>
                                            <div class="col-md-1"><button class="btn btn-primary" type="button" onclick="upplay()">查询</button></div>
                                            <div class="col-md-8 sizebox  remove-padding " id="voiceFilePlay">
                                            </div>
                                        </div>

                                        <table class="table table-striped cost-table-history tablelist" id="playtable">
                                            <thead>
                                            <tr>
                                                <th width="20%">文件名</th>
                                                <th width="10%">状态</th>
                                                <th width="10%">大小</th>
                                                <th width="10%">关联子账号</th>
                                                <th width="25%">备注</th>
                                                <th width="20%">操作</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            </tbody>
                                        </table>
                                        <section class="panel panel-default yunhuni-personal">
                                            <div id="playpage"></div>
                                        </section>

                                    </div>
                                    <!-- 录音文件-->
                                    <div class="tab-pane fade" id="voice">
                                        <p class="application_info">
                                            1.每个账号默认允许免费存储7天内的录音文件，超过指定配置周期的录音文件系统将自动删除。<br/>
                                            2.会员可以自行配置存储周期，修改存储周期后，文件存储按1元/G/月收费，每个月底进行扣费。<br/>
                                            3.录音文件可在详单查询内下载。
                                        </p>
                                        <div class="row margin-bottom-20">
                                            <div class="col-md-2 font14 max-width-120">
                                                配置存储周期
                                            </div>
                                            <div class="col-md-10 font14">
                                                <input type="radio" name="cycle" value="7" <c:if test="${cycle==7}">checked</c:if> />&nbsp;7天免费存储
                                                &nbsp;<input type="radio" name="cycle" value="30"<c:if test="${cycle==30}">checked</c:if> />&nbsp;1个月
                                                &nbsp;<input type="radio" name="cycle" value="60"<c:if test="${cycle==60}">checked</c:if> />&nbsp;2个月
                                                &nbsp;<input type="radio" name="cycle" value="90"<c:if test="${cycle==90}">checked</c:if> />&nbsp;3个月
                                                &nbsp;<input type="radio" name="cycle" value="180"<c:if test="${cycle==180}">checked</c:if> />&nbsp;6个月
                                            </div>
                                        </div>
                                        <div class="row margin-bottom-20">
                                            <div class="col-md-2 font14 max-width-120"></div>
                                            <div class=" col-md-10">
                                                <a href="#" class="btn btn-primary" onclick="saveCycle()">保存</a>
                                            </div>
                                        </div>
                                        <%--<p class="application_info">--%>
                                            <%--1、默认录音文件免费存储7天，超过7天平台自动删除该文件，如有需要请提前下载保留<br/>--%>
                                            <%--2、可配置录音文件存储周期，超过7天按相应的存储资费计费，10元/天。--%>
                                        <%--</p>--%>
                                        <%--<div class="form-group">--%>
                                            <%--<div class="col-md-3 remove-padding line32 font14" id="voiceFileRecord">--%>
                                            <%--</div>--%>
                                            <%--<div class="col-md-9 text-right">--%>
                                                <%--<a class="btn modalShow right" data-id="three">批量删除</a> <a class="btn modalShow right" data-id="two" >批量下载</a>--%>
                                            <%--</div>--%>
                                        <%--</div>--%>

                                        <%--<table class="table table-striped cost-table-history tablelist" id="voicetable">--%>
                                            <%--<thead>--%>
                                            <%--<tr>--%>
                                                <%--<th>标题</th>--%>
                                                <%--<th>大小</th>--%>
                                                <%--<th>时长</th>--%>
                                                <%--<th>操作</th>--%>
                                            <%--</tr>--%>
                                            <%--</thead>--%>
                                            <%--<tbody>--%>

                                            <%--</tbody>--%>
                                        <%--</table>--%>
                                        <%--<section class="panel panel-default yunhuni-personal">--%>
                                            <%--<div id="voicepage"></div>--%>
                                        <%--</section>--%>
                                    </div>
                                    <!--号码绑定-->
                                    <div class="tab-pane fade" id="number">
                                        <p class="margin-bottom-20"></p>
                                        <div class="row margin-bottom-20">
                                            <div class="col-md-12">
                                                <a class="btn btn-primary" onclick="unallband()">全部解除绑定</a>
                                                <c:if test="${app.status==1}">
                                                    <a class="btn btn-primary" id="call-number">绑定号码</a>
                                                </c:if>
                                            </div>
                                        </div>

                                        <table class="table table-striped cost-table-history tablelist" id="number-table">
                                            <thead>
                                            <tr>
                                                <th class="text-center">号码</th>
                                                <th class="text-center">状态</th>
                                                <th class="text-center">可呼入</th>
                                                <th class="text-center">可呼出</th>
                                                <th class="text-center"><span class="text-center-l-fixed">归属地</span></th>
                                                <th class="text-center">有效期</th>
                                                <th class="text-center">操作</th>
                                            </tr>
                                            </thead>
                                            <tbody id="band-table">

                                            </tbody>
                                        </table>
                                        <section class="panel panel-default yunhuni-personal">
                                            <div id="bandpage"></div>
                                        </section>
                                    </div>
                                    <!--号码绑定end-->
                                    <!--分机列表-->
                                    <div class="tab-pane fade<c:if test="${app.serviceType=='call_center'}"> in active</c:if>" id="extension">
                                        <div class="form-group">
                                            <div class="col-md-3 remove-padding"><input type="text" class="form-control" placeholder="分机号" id="extension_num" /></div>
                                            <div class="col-md-3 remove-padding" style="padding-left:15px;">
                                                <%--<select id="extension_subId" class="form-control show-tick sublist"  data-live-search="true" title="关联子账号"  >--%>
                                                <%--</select>--%>
                                                <input type="text" class="form-control" placeholder="关联子账号" id="extension_subId" /></div>
                                            <div class="col-md-2">
                                                <button class="btn btn-primary" type="button" onclick="extensionList()">查询</button>
                                                <button href="#"  class="btn btn-primary defind modalShow" data-id="six" >创建分机</button>
                                            </div>
                                        </div>
                                        <table class="table table-striped cost-table-history tablelist" id="extension-table">
                                            <thead>
                                            <tr>
                                                <th class="">ID</th>
                                                <th class="">分机号</th>
                                                <th class="">密码</th>
                                                <th>关联子账号</th>
                                                <th class="">状态</th>
                                                <th class="">鉴权方式</th>
                                                <th class="">操作</th>
                                            </tr>
                                            </thead>
                                            <tbody id="extension-list">

                                            </tbody>
                                        </table>
                                        <section class="panel panel-default yunhuni-personal">
                                            <div id="extension-page"></div>
                                        </section>
                                    </div>
                                    <!--分机列表end-->
                                    <!--模板列表-->
                                    <div class="tab-pane fade<c:if test="${app.serviceType=='msg'}"> in active</c:if>" id="template">
                                        <p class="application_info">
                                            1.提交模板申请前，请仔细阅读文档：<a href="#" onclick="window.open('${globalWebsite}')">模板使用说明</a></br>
                                            2.短信模板只能在所属应用下使用，不允许跨应用使用</br>
                                            3.会员需进行公司认证，并审核通过后才能新增模板</br>
                                            4.会员在进行闪印和短信的测试时，可以使用平台提供测试模板
                                        </p>
                                        <div id="template_home">
                                            <div class="form-group">
                                                <div class="col-md-3 remove-padding"><input type="text" class="form-control" placeholder="模板名称" id="template_num" /></div>
                                                <div class="col-md-3 remove-padding" style="padding-left:15px;">
                                                    <%--<select id="extension_subId" class="form-control show-tick sublist"  data-live-search="true" title="关联子账号"  >--%>
                                                    <%--</select>--%>
                                                    <input type="text" class="form-control" placeholder="关联子账号" id="template_subId" /></div>
                                                <div class="col-md-2">
                                                    <button class="btn btn-primary" type="button" onclick="templateList()">查询</button>
                                                    <div hidden>
                                                    <button href="#"  class="btn btn-primary defind modalShow" data-id="seven" >新增模板</button></div>
                                                    <button href="#"  class="btn btn-primary " onclick="isnewTemplate()" >新增模板</button>
                                                </div>
                                            </div>
                                            <table class="table table-striped cost-table-history tablelist" id="template-table">
                                                <thead>
                                                <tr>
                                                    <th class="">模板编号</th>
                                                    <th class="">模板类型</th>
                                                    <th class="">模板名称</th>
                                                    <th>内容</th>
                                                    <th>关联子账号</th>
                                                    <th class="">状态</th>
                                                    <th class="">操作</th>
                                                </tr>
                                                </thead>
                                                <tbody id="template-list">

                                                </tbody>
                                            </table>
                                            <section class="panel panel-default yunhuni-personal">
                                                <div id="template-page"></div>
                                            </section>
                                        </div>
                                        <div id="template_detail" hidden>
                                            <div class="panel-body devbox remove-padding-left">
                                                <h4 class="margin-bottom-20">模板详情</h4>
                                                <div class="row margin-bottom-10">
                                                    <div class="col-md-1 dev">
                                                        模板编号：
                                                    </div>
                                                    <div class="col-md-6">
                                                        <p>
                                                            <input type="text" class="form-control"  disabled value="{{tempId}}">
                                                        </p>
                                                    </div>
                                                </div>
                                                <div class="row margin-bottom-10">
                                                    <div class="col-md-1 dev">
                                                        模板类型：
                                                    </div>
                                                    <div class="col-md-9">
                                                        <p>
                                                            <input type="radio" id="ussd2" disabled value="ussd" v-model="type" name="type">
                                                            <label for="ussd2">闪印</label>
                                                            <input type="radio" id="sms2" disabled value="sms" v-model="type" name="type">
                                                            <label for="sms2">短信</label>
                                                        </p>
                                                    </div>
                                                </div>
                                                <div class="row margin-bottom-10">
                                                    <div class="col-md-1 dev line-height-32">
                                                        模板名称：
                                                    </div>
                                                    <div class="col-md-6">
                                                        <p>
                                                            <input v-if="isShow(1,0)" type="text" class="form-control" disabled value="{{name}}" placeholder=""/>
                                                            <input v-if="isShow(-1)" type="text" class="form-control" v-model="name" value="{{name}}" placeholder=""/>
                                                        </p>
                                                        <span>仅供识别</span>
                                                    </div>
                                                </div>
                                                <div class="row margin-bottom-10">
                                                    <div class="col-md-1 dev line-height-32">
                                                        模板内容：
                                                    </div>
                                                    <div class="col-md-6" v-if="isShow(-1)">
                                                        <div style="height: 70px;position:relative;margin:0;padding:0;width:620px">
                                                        <textarea class="form-control" v-model="content" placeholder="" style="height:70px;position:absolute;margin:0;padding:2px "
                                                                  id="template_content2" onkeydown="checkMaxInput(this,60)" onkeyup="checkMaxInput(this,60)" onfocus="checkMaxInput(this,60)" onblur="checkMaxInput(this,60);"
                                                        >{{content}}</textarea>
                                                            <div id="template_content2msg" class="note" style="position:absolute;line-height:117px;padding:3px 555px;height: 0px"><font color="#777">{{contentlength}}/60</font></div>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6" v-if="isShow(1,0)" >
                                                        <textarea  type="text"  class="form-control"  readonly placeholder="">{{content}}</textarea>
                                                    </div>
                                                </div>
                                                <div class="row margin-bottom-10">
                                                    <div class="col-md-1 dev line-height-32">
                                                        备注：
                                                    </div>
                                                    <div class="col-md-6" v-if="isShow(-1)">
                                                        <div style="height: 70px;position:relative;margin:0;padding:0;width:620px">
                                                        <textarea class="form-control" v-model="remark" placeholder="" style="height:70px;position:absolute;margin:0;padding:2px "
                                                                  id="template_remark2" onkeydown="checkMaxInput(this,100)" onkeyup="checkMaxInput(this,100)" onfocus="checkMaxInput(this,100)" onblur="checkMaxInput(this,100);"
                                                        >{{remark}}</textarea>
                                                            <div id="template_remark2msg" class="note" style="position:absolute;line-height:117px;padding:3px 555px;height: 0px"><font color="#777">{{remarklength}}/100</font></div>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6" v-if="isShow(1,0)" style="width:620px">
                                                        <textarea  type="text"  class="form-control"  readonly placeholder="" >{{remark}}</textarea>
                                                    </div>
                                                </div>
                                                <div class="row margin-bottom-10">
                                                    <div class="col-md-1 dev">
                                                        状态：
                                                    </div>
                                                    <div class="col-md-6">
                                                        <p v-if="isShow(1)">审核已通过</p>
                                                        <p v-if="isShow(-1)">审核不通过！</p>
                                                        <p v-if="isShow(0)">待审核</p>
                                                    </div>
                                                </div>
                                                <div class="row margin-bottom-10" v-if="isShow(-1)">
                                                    <div class="col-md-1 dev line-height-32">
                                                        原因：
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div style="height: 70px;position:relative;margin:0;padding:0;width:620px">
                                                        <textarea type="text"  class="form-control" readonly placeholder="">{{reason}}</textarea>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row margin-bottom-20">
                                                <div class="col-md-6">
                                                    <button v-if="state==-1" class="btn btn-primary" type="button" onclick="editSevenPost()">重新提交</button>
                                                    <button class="btn btn-primary" type="button" onclick="totemplateHome()">返回</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <!--分机列表end-->
                                    <!--坐席列表-->
                                    <div class="tab-pane fade" id="agent">
                                        <div class="form-group">
                                            <div class="col-md-3 remove-padding">
                                                <input type="text" class="form-control" placeholder="坐席名称（ID）" id="agent_num" /></div>
                                            <div class="col-md-3 remove-padding" style="padding-left:15px;">
                                                <%--<select id="agent_subId" class="form-control show-tick sublist"  data-live-search="true" title="关联子账号"  >--%>
                                                <%--</select>--%>
                                                    <input type="text" class="form-control" placeholder="关联子账号" id="agent_subId" /></div>
                                                <%--<input type="text" class="form-control " placeholder="子账号鉴权账号" id="subId"/>--%>
                                            <div class="col-md-2">
                                                <button class="btn btn-primary" type="button" onclick="agentList()">查询</button>
                                            </div>
                                        </div>
                                        <table class="table table-striped cost-table-history tablelist" id="agent-table">
                                            <thead>
                                            <tr>
                                                <th class="text-center">坐席名称（ID）</th>
                                                <th class="text-center">技能组</th>
                                                <th class="text-center">绑定分机</th>
                                                <th class="text-center">关联子账号</th>
                                                <th class="text-center">状态</th>
                                                <th class="text-center">操作</th>
                                            </tr>
                                            </thead>
                                            <tbody id="agent-list">

                                            </tbody>
                                        </table>
                                        <section class="panel panel-default yunhuni-personal">
                                            <div id="agent-page"></div>
                                        </section>
                                    </div>
                                    <!--坐席列表end-->
                                    <!--排队条件列表-->
                                    <div class="tab-pane fade" id="queue">
                                        <div class="form-group">
                                            <div class="col-md-3 remove-padding">
                                                <input type="text" class="form-control" placeholder="关联子账号" id="queue_subId" />
                                                <%--<input type="text" class="form-control" placeholder="ID" id="queue_num" />--%>
                                            </div>
                                            <%--<div class="col-md-3 remove-padding" style="padding-left:15px;">--%>
                                                <%--<select id="queue_subId" class="form-control show-tick sublist"  data-live-search="true" title="关联子账号"  >--%>
                                                <%--</select>--%>
                                                    <%--<input type="text" class="form-control" placeholder="关联子账号" id="queue_subId" />--%>
                                                    <%--<input type="text" class="form-control " placeholder="子账号鉴权账号" id="subId"/>--%>
                                        <%--</div>--%>
                                            <div class="col-md-2">
                                                <button class="btn btn-primary" type="button" onclick="queueList()">查询</button>
                                            </div>
                                        </div>
                                        <table class="table table-striped cost-table-history tablelist" id="queue-table">
                                            <thead>
                                            <tr>
                                                <th class="text-center" >条件选择表达式</th>
                                                <th class="text-center">排序表达式</th>
                                                <th class="text-center" style="width: 20px">优先级</th>
                                                <th class="text-center" style="width: 20px">等待超时时间（秒）</th>
                                                <th class="text-center" style="width: 20px">接听超时时间（秒）</th>
                                                <th class="text-center">关联子账号</th>
                                                <th class="text-center">备注</th>
                                            </tr>
                                            </thead>
                                            <tbody id="queue-list">

                                            </tbody>
                                        </table>
                                        <section class="panel panel-default yunhuni-personal">
                                            <div id="queue-page"></div>
                                        </section>
                                    </div>
                                    <!--排队条件列表end-->
                                    <!--子账号-->
                                    <div class="tab-pane fade" id="subAccount">
                                        <div  id="subAccount_home">
                                        <%--<p class="margin-bottom-20"></p>--%>
                                        <p class="application_info">
                                            1.子账号是壹耘平台提供给会员管理其客户的账号，会员可以使用子账号的各个功能，轻松地管理每一个独立的客户<br/>
                                            2.子账号与指定的应用关联，请勿跨应用使用
                                        </p>
                                        <div class="form-group">
                                            <div class="col-md-4 remove-padding"><input id="five_certId" type="text" class="form-control" placeholder="鉴权账号" /></div>
                                            <div class="col-md-8">
                                                <button class="btn btn-primary" type="button" onclick="subAccountList()">查询</button>
                                                <button href="#"  class="btn btn-primary defind modalShow" data-id="five" >创建子账号</button>
                                            </div>
                                        </div>
                                        <table class="table table-striped cost-table-history tablelist" id="subAccount-table">
                                            <thead>
                                            <tr>
                                                <th class="text-center">鉴权账号</th>
                                                <th class="text-center">密钥</th>
                                                <c:if test="${app.serviceType == 'voice'}">
                                                    <th class="text-center">语音用量 /总量（分钟）</th>
                                                </c:if>
                                                <c:if test="${app.serviceType == 'call_center'}">
                                                    <th class="text-center">语音用量 /总量（分钟）</th>
                                                    <%--<th class="text-center">坐席用量 /总量（个）</th>--%>
                                               </c:if>
                                               <c:if test="${app.serviceType == 'msg'}">
                                                   <th class="text-center">闪印用量 /配额（条）</th>
                                                   <th class="text-center">短信用量/配额（条）</th>
                                               </c:if>
                                                <th class="text-center">状态</th>
                                                <th class="text-center">备注</th>
                                                <th class="text-center">操作</th>
                                            </tr>
                                            </thead>
                                            <tbody >

                                            </tbody>
                                        </table>
                                        <section class="panel panel-default yunhuni-personal">
                                            <div id="subAccountpage"></div>
                                        </section>
                                        </div>
                                        <div  id="subAccount_datail" hidden>
                                            <div class="panel-body devbox remove-padding-left">
                                                <h4 class="margin-bottom-20">子账号详情</h4>
                                                <div class="row margin-bottom-10">
                                                    <div class="col-md-1 dev">
                                                        鉴权账号：
                                                    </div>
                                                    <div class="col-md-6">
                                                        <p>
                                                            {{certId}}
                                                        </p>
                                                    </div>
                                                </div>
                                                <div class="row margin-bottom-10">
                                                    <div class="col-md-1 dev">
                                                        密钥：
                                                    </div>
                                                    <div class="col-md-9">
                                                        <p>
                                                            {{secretKey}}
                                                        </p>
                                                    </div>
                                                </div>
                                                <div class="row margin-bottom-10">
                                                    <div class="col-md-1 dev line-height-32">
                                                        回调地址：
                                                    </div>
                                                    <div class="col-md-6">
                                                        <p>
                                                            <input type="text" class="form-control" v-model="url" value="{{url}}" placeholder=""/>
                                                        </p>
                                                        <span>（子账号的回调地址优先级高于应用的回调地址）</span>
                                                    </div>
                                                </div>
                                                <div class="row margin-bottom-10">
                                                    <div class="col-md-1 dev">
                                                        配置额度：
                                                    </div>
                                                    <div class="col-md-6">
                                                        <p>（负数为额度不作限制，0则表示额度为0，正数为具体的使用额度限制）</p>
                                                    </div>
                                                </div>
                                                <c:if test="${app.serviceType == 'voice'}">
                                                    <div class="row margin-bottom-10">
                                                        <div class="col-md-1 dev line-height-32">语音(分钟)：</div>
                                                        <div class="col-md-6">
                                                            <p>
                                                                <input type="text"  class="form-control"  v-model="voiceNum" value="{{voiceNum}}" placeholder=""/>
                                                            </p>
                                                        </div>
                                                        <span class="col-md-1 line-height-32 text-left text-danger">*</span>
                                                    </div>
                                                </c:if>
                                                <c:if test="${app.serviceType == 'call_center'}">
                                                    <div class="row margin-bottom-10">
                                                        <div class="col-md-1 dev line-height-32">语音(分钟)：</div>
                                                        <div class="col-md-6">
                                                            <p>
                                                                <input type="text"  class="form-control"  v-model="voiceNum" value="{{voiceNum}}" placeholder=""/>
                                                            </p>
                                                        </div>
                                                        <span class="col-md-1 line-height-32 text-left text-danger">*</span>
                                                    </div>
                                                    <%--<div class="row margin-bottom-10">--%>
                                                        <%--<div class="col-md-1 dev line-height-32">坐席（个）：</div>--%>
                                                        <%--<div class="col-md-6">--%>
                                                            <%--<p>--%>
                                                                <%--<input type="text"  class="form-control"  v-model="seatNum" value="{{seatNum}}" placeholder=""/>--%>
                                                            <%--</p>--%>
                                                        <%--</div>--%>
                                                        <%--<span class="col-md-1 line-height-32 text-left text-danger">*</span>--%>
                                                    <%--</div>--%>
                                                </c:if>
                                                <c:if test="${app.serviceType == 'msg'}">
                                                    <div class="row margin-bottom-10">
                                                        <div class="col-md-1 dev line-height-32">闪印（条）：</div>
                                                        <div class="col-md-6">
                                                            <p>
                                                                <input type="text"  class="form-control"  v-model="ussdNum" value="{{ussdNum}}" placeholder=""/>
                                                            </p>
                                                        </div>
                                                        <span class="col-md-1 line-height-32 text-left text-danger">*</span>
                                                    </div>
                                                    <div class="row margin-bottom-10">
                                                        <div class="col-md-1 dev line-height-32">短信（条）：</div>
                                                        <div class="col-md-6">
                                                            <p>
                                                                <input type="text"  class="form-control"  v-model="smsNum" value="{{smsNum}}" placeholder=""/>
                                                            </p>
                                                        </div>
                                                        <span class="col-md-1 line-height-32 text-left text-danger">*</span>
                                                    </div>
                                                </c:if>
                                                <div class="row margin-bottom-10">
                                                    <div class="col-md-1 dev line-height-32">
                                                        备注：
                                                    </div>
                                                    <div class="col-md-6">
                                                        <input type="text"  class="form-control"  v-model="remark" value="{{remark}}" placeholder=""/>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row margin-bottom-20">
                                                <div class="col-md-6">
                                                    <button class="btn btn-primary" type="button" onclick="editSubAccount()">保存</button>
                                                    <button class="btn btn-primary" type="button" onclick="tosubAccountHome1()">返回</button>
                                                </div>
                                            </div>
                                            <c:if test="${app.serviceType == 'voice' ||app.serviceType == 'call_center' }">
                                                <p class="noticeInfo hr text-success"></p>
                                                <p class="margin-bottom-20"></p>
                                                <div class="row margin-bottom-20">
                                                    <div class="col-md-12">
                                                        <a class="btn btn-primary" onclick="unallband2()" >全部解除绑定</a>
                                                        <c:if test="${app.status==1}">
                                                            <a class="btn btn-primary" id="call-number2" data-num-bind="">绑定号码</a>
                                                        </c:if>
                                                    </div>
                                                </div>

                                                <table class="table table-striped cost-table-history tablelist" id="number-table2">
                                                    <thead>
                                                    <tr>
                                                        <th class="text-center">号码</th>
                                                        <th class="text-center">状态</th>
                                                        <th class="text-center">可呼入</th>
                                                        <th class="text-center">可呼出</th>
                                                        <th class="text-center"><span class="text-center-l-fixed">归属地</span></th>
                                                        <th class="text-center">有效期</th>
                                                        <th class="text-center">操作</th>
                                                    </tr>
                                                    </thead>
                                                    <tbody id="band-table2">

                                                    </tbody>
                                                </table>
                                                <section class="panel panel-default yunhuni-personal">
                                                    <div id="bandpage2"></div>
                                                </section>
                                            </c:if>
                                        </div>
                                    </div>
                                    <!--子账号end-->
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


<!--号码绑定-->
<div id="vue-application">
    <div class="modal fade call-detail-modal" id="call-modal" tabindex="100" role="dialog"
         aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title">绑定号码</h4>
                </div>
                <div class="modal-body">
                    <table class="table">
                        <thead>
                        <tr>
                            <th><input type="checkbox" v-model="shopCheck" @click="isCheck" />全选</th>
                            <th>号码</th>
                            <th class="text-center">可呼入</th>
                            <th class="text-center">可呼出</th>
                            <th class="text-center"><span class="text-center-l-fixed">归属地</span></th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr v-for="item in phonelist">
                            <td scope="row"><input type="checkbox" v-model="shop" value="{{ item.num }}"/></td>
                            <td>{{ item.num }}</td>
                            <td class="text-center">{{ isCall[item.isCalled] }}</td>
                            <td class="text-center">{{ isCall[item.isDialing]}}</td>
                            <td class="text-center"><span class="text-center-l-fixed">{{ item.areaCode}}</span></td>
                        </tr>
                        </tbody>
                    </table>
                    <!--分页-->
                    <div id="datatablepage"></div>
                </div>
                <div class="modal-footer">
                    <a class="btn btn-primary" @click="band" v-if="shop.length > 0">立即绑定</a>
                    <button type="button" class="btn btn-default"
                            data-dismiss="modal">关闭
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
<!--号码绑定end-->

<!---mobilebox-->
<div class="shadow-bg" id="show-bg"></div>
<div class="modal-box application-detail-box" id="modalone" style="display: none">
    <div class="title">绑定测试电话号码<a class="close_a modalCancel" data-id="one"></a></div>
    <div class="content">
        <p class="info">注意：一个测试号码同一时间只能绑定到一个应用，绑定完成后，其之前的绑定关系将会被解除</p>
        <div class="input-box ">
            <form role="form" action="${ctx}/console/account/information/edit" method="post" class="register-form" id="testNumBindForm">
                <c:forEach items="${testNumBindList}" var="testNumBind" varStatus="s">
                    <c:if test="${s.index+1/3==0}"><div class="row mt-20"></c:if>
                    <div class="col-md-4">  <input type="checkbox" name="testName-${testNumBind.number}" class="check-box" <c:if test="${testNumBind.app.id==app.id}" >checked </c:if> value="${testNumBind.id}" />${testNumBind.number}</div>
                    <c:if test="${s.index+1/3==0}"></div></c:if>
                </c:forEach>
            </form>
        </div>
    </div>
    <div class="footer">
        <a class="cancel modalCancel" data-id="one">返回</a>
        <a class="sure modalSureOne" data-id="one">确认</a>
    </div>
</div>
<!---批量下载--->
<div class="modal-box application-detail-box" id="modaltwo" style="display:none ">
    <div class="title">批量下载<a class="close_a modalCancel" data-id="two"></a></div>
    <div class="content">
        <p class="text-center mt-20">批量下载允许下载3天范围内的录音文件 <span class="tips-error moadltipstwo text-center"></span></p>
        <div class="input-box ">
            <div class="row text-center">
                <div class="col-md-4 remove-padding">
                    <input type="text" value="" class="input-text datepicker form-control date-input" id="datestarttwo"  />
                </div>
                <div class="col-md-1 line32">到</div>
                <div class="col-md-4 remove-padding">
                    <input type="text" value="" class="input-text datepicker form-control date-input " id="dateendtwo" />
                </div>
                <button class="btn btn-primary findfile" data-id="two">查询</button>
            </div>

            <div class="row scrolldiv" id="scrolldivtwo">
            </div>
        </div>
    </div>
    <div class="footer">
        <a class="cancel modalCancel" data-id="two">返回</a>
        <a class="sure modalSureTwo" data-id="two">确认</a>
    </div>
</div>

<!---批量删除--->
<div class="modal-box application-detail-box" id="modalthree" style="display:none ">
    <div class="title">批量删除<a class="close_a modalCancel cancelthree" data-id="three"></a></div>
    <div class="content">
        <p class="text-center mt-20">批量删除录音文件 <span class="tips-error moadltipsthree text-center"></span></p>

        <div class="input-box ">
            <div class="row text-center">
                <div class="col-md-4 remove-padding">
                    <input type="text" value="" class="input-text datepicker form-control" id="datestartthree" />
                </div>
                <div class="col-md-1 line32">到</div>
                <div class="col-md-4 remove-padding">
                    <input type="text" value="" class="input-text datepicker form-control" id="dateendthree" />
                </div>
                <button class="btn btn-primary findfile" data-id="three">查询</button>
            </div>

            <div class="row scrolldiv" id="scrolldivthree" >

            </div>

        </div>
    </div>
    <div class="footer">
        <a class="cancel modalCancel cancelthree" data-id="three">返回</a>
        <a class="sure modalSureThree" data-id="three">确认</a>
    </div>
</div>
        <!---创建子账号--->
        <div class="modal-box application-detail-box" id="modalfive" style="display:none ">
            <div class="title">创建子账号<a class="close_a modalCancel cancelfive" data-id="five"></a></div>
            <div class="content" id="createSubAccountFive" >
                <p class="info info font14">
                    1.创建子账号后，系统将会自动生成一个鉴权账号和密钥<br/>
                    2.配额是会员提供给其客户使用服务的额度
                </p>
                <div class="row text-center">
                    <lable class="col-md-3 text-right line-height-32">回调地址：</lable>
                    <div class="col-md-8 remove-padding-right">
                        <input type="text" class="form-control" v-model="url"  value="{{url}}" placeholder=""/>
                    </div>

                </div>
                <div class="row margin-bottom-10">
                    <span class="col-lg-offset-3 col-md-9 ">（子账号的回调地址优先级高于应用的回调地址）</span>
                </div>
                <div class="row margin-bottom-10">
                    <lable class="col-md-3 text-right">配置额度：</lable>
                    <div class="col-md-8 remove-padding-right">
                        （负数为额度不作限制，0则表示额度为0，正数为具体的使用额度限制）
                    </div>
                </div>
                <c:if test="${app.serviceType == 'voice'}">
                    <div class="row margin-bottom-10">
                        <lable class="col-md-3 text-right line-height-32">语音（分钟）：</lable>
                        <div class="col-md-8 remove-padding-right">
                            <input type="text" class="form-control" v-model="voiceNum" value="{{voiceNum}}" placeholder=""/>
                        </div>
                        <span class="col-md-1 line-height-32 text-left text-danger padding-left-5" >*</span>
                    </div>
                </c:if>
                <c:if test="${app.serviceType == 'call_center'}">
                    <div class="row margin-bottom-10">
                        <lable class="col-md-3 text-right line-height-32">语音（分钟）：</lable>
                        <div class="col-md-8 remove-padding-right">
                            <input type="text" class="form-control" v-model="voiceNum" value="{{voiceNum}}" placeholder=""/>
                        </div>
                        <span class="col-md-1 line-height-32 text-left text-danger padding-left-5" >*</span>
                    </div>
                    <%--<div class="row margin-bottom-10">--%>
                        <%--<lable class="col-md-3 text-right line-height-32">坐席（个）：</lable>--%>
                        <%--<div class="col-md-8 remove-padding-right">--%>
                            <%--<input type="text" class="form-control" v-model="seatNum" value="{{seatNum}}" placeholder=""/>--%>
                        <%--</div>--%>
                        <%--<span class="col-md-1 line-height-32 text-left text-danger padding-left-5" >*</span>--%>
                    <%--</div>--%>
                </c:if>
                <c:if test="${app.serviceType == 'msg'}">
                    <div class="row margin-bottom-10">
                        <lable class="col-md-3 text-right line-height-32">闪印（条）：</lable>
                        <div class="col-md-8 remove-padding-right">
                            <input type="text" class="form-control" v-model="ussdNum" value="{{ussdNum}}" placeholder=""/>
                        </div>
                        <span class="col-md-1 line-height-32 text-left text-danger padding-left-5" >*</span>
                    </div>
                    <div class="row margin-bottom-10">
                        <lable class="col-md-3 text-right line-height-32">短信（条）：</lable>
                        <div class="col-md-8 remove-padding-right">
                            <input type="text" class="form-control" v-model="smsNum" value="{{smsNum}}" placeholder=""/>
                        </div>
                        <span class="col-md-1 line-height-32 text-left text-danger padding-left-5" >*</span>
                    </div>
                </c:if>
                <div class="row">
                    <lable class="col-md-3 text-right line-height-32">备注：</lable>
                    <div class="col-md-8 remove-padding-right">
                        <input type="text" class="form-control" v-model="remark" value="{{remark}}" placeholder=""/>
                    </div>
                </div>
            </div>
            <div class="footer">
                <a class="cancel modalCancel cancelfive" data-id="five">返回</a>
                <a class="sure modalSureFive" data-id="five">创建</a>
            </div>
        </div>
        <!---创建模板--->
        <div class="modal-box application-detail-box" id="modalseven" style="display:none;">
            <div class="title">创建模板<a class="close_a modalCancel cancelseven" data-id="seven"></a></div>
            <div class="content" id="createSeven" >
                <br>
                <div class="row margin-bottom-10">
                    <lable class="col-md-3 text-right line-height-32">模板类型：</lable>
                    <div class="col-md-8 remove-padding-right">
                        <input type="radio" id="ussd" checked value="ussd" v-model="type" name="type">
                        <label for="ussd">闪印</label>
                        <input type="radio" id="sms" value="sms" v-model="type" name="type">
                        <label for="sms">短信</label>
                        <br>
                    </div>
                    <%--<span class="col-md-1 line-height-32 text-left text-danger padding-left-5" >*</span>--%>
                </div>
                <div class="row margin-bottom-10">
                    <lable class="col-md-3 text-right line-height-32">模板名称：</lable>
                    <div class="col-md-8 remove-padding-right">
                        <input type="text" class="form-control" v-model="name" value="{{name}}" placeholder=""/>
                        <span>仅供识别</span>
                    </div>
                    <span class="col-md-1 line-height-32 text-left text-danger padding-left-5" >*</span>
                </div>
                <div class="row margin-bottom-10">
                    <lable class="col-md-3 text-right line-height-32">模板内容：</lable>
                    <div  class="col-md-8 remove-padding-right" style="height:115px;">
                        <div style="position:relative;margin:0;padding:0;">
                        <textarea class="form-control" v-model="content"  placeholder="" style="height:86px;position:absolute;margin:0;padding:2px "
                        id="template_content" onkeydown="checkMaxInput(this,62)" onkeyup="checkMaxInput(this,62)" onfocus="checkMaxInput(this,62)" onblur="checkMaxInput(this,62);"
                        >{{content}}</textarea>
                            <div id="template_contentmsg" class="note" style="position:absolute;line-height:147px;padding:3px 250px;height: 0px"><font color="#777">0/62</font></div>
                        </div>
                        <span style="position:absolute;line-height:184px;padding:3px 3px;height: 0px">
                            例：【壹耘】您的验证码是#*#,请在#*#分钟内完成输</span>
                        <span style="position:absolute;line-height:215px;padding:3px 3px;height: 0px">
                            入内容不允许超过62个字符（包括变量）</span>
                    </div>
                    <span class="col-md-1 line-height-32 text-left text-danger padding-left-5" >*</span>
                </div>
                <div class="row margin-bottom-10">
                    <lable class="col-md-3 text-right line-height-32">使用场景说明：</lable>
                    <div class="col-md-8 remove-padding-right" style="height:130px;">
                        <div style="position:relative;margin:0;padding:0">
                        <textarea class="form-control" v-model="remark" placeholder="" style="height:130px;position:absolute;margin:0;padding:2px "
                                  id="template_remark" onkeydown="checkMaxInput(this,100)" onkeyup="checkMaxInput(this,100)" onfocus="checkMaxInput(this,100)" onblur="checkMaxInput(this,100);"
                        >{{remark}}</textarea>
                        </div>
                        <div id="template_remarkmsg" class="note" style="position:absolute;line-height:235px;padding:3px 235px;height: 0px"><font color="#777">0/100</font></div>
                    </div>
                    <span class="col-md-1 line-height-32 text-left text-danger padding-left-5" >*</span>
                </div>
            </div>
            <div class="footer">
                <a class="cancel modalCancel cancelseven" data-id="seven">返回</a>
                <a class="sure modalSureSeven" data-id="seven">创建</a>
            </div>
        </div>
        <!---创建分机--->
        <div class="modal-box application-detail-box" id="modalsix" style="display:none;">
            <div class="title">创建分机<a class="close_a modalCancel cancelsix" data-id="six"></a></div>
            <div class="content" id="createSix" >
                <br>
                <div class="row margin-bottom-10">
                    <lable class="col-md-3 text-right line-height-32">分机号：</lable>
                    <div class="col-md-8 remove-padding-right">
                        <input type="text" class="form-control" v-model="user"  value="{{user}}" placeholder=""/>
                        <span>6-12的数字</span>
                    </div>
                    <span class="col-md-1 line-height-32 text-left text-danger padding-left-5" >*</span>
                </div>

                <div class="row margin-bottom-10">
                    <lable class="col-md-3 text-right line-height-32">分机密码：</lable>
                    <div class="col-md-8 remove-padding-right">
                        <input type="text" class="form-control" v-model="password" value="{{password}}" placeholder=""/>
                        <span>6-12的数字</span>
                    </div>
                    <span class="col-md-1 line-height-32 text-left text-danger padding-left-5" >*</span>
                </div>
                <%--<div class="row margin-bottom-10">--%>
                    <%--<lable class="col-md-3 text-right line-height-32">关联子账号：</lable>--%>
                    <%--<div class="col-md-8 remove-padding-right">--%>
                        <%--<input type="text" class="form-control" id="create_six_subId" value="" placeholder=""/>--%>
                    <%--</div>--%>
                    <%--<div class="col-md-8 remove-padding-right">--%>
                        <%--<select id="create_six_subId" class="sublist" name="subId" data-live-search="true" title="">--%>
                        <%--</select>--%>
                    <%--</div>--%>
                <%--</div>--%>
                <div class="text-center">
                    <p class="error_msg_six" style="color: red"></p>
                </div>
            </div>
            <div class="footer">
                <a class="cancel modalCancel cancelsix" data-id="six">返回</a>
                <a class="sure modalSureSix" data-id="six">创建</a>
            </div>
        </div>
        <!---创建分机--->
        <div hidden>
            <button href="#"  hidden class="btn btn-primary defind modalShow" data-id="playvoice" >试听</button>
        </div>
        <div class="modal-box application-detail-box"  id="modalplayvoice" style="display:none;">
            <div class="title" id="modalplayvoice2">试听放音文件<a class="close_a modalCancel cancelplayvoice" data-id="playvoice" style="right: 5px;top: 5px"></a></div>
            <div class="content" >
                <div class="row margin-bottom-10">
                    <div class="col-md-8 remove-padding-right" style="margin-top:10px;">
                    <audio id="playvoicesrc" controls="controls" preload="true" src="" style="width: 250px;"></audio>
                    </div>
                </div>
            </div>
        </div>
<!---上传文件--->
<div class="modal-box application-detail-box application-file-box" id="modalfour" style="display:none ">
    <div class="modal-loadding loadding"></div>
    <div class="title">文件上传<a class="close_a modalCancel-app-up" data-id="four" ></a></div>
    <div class="content">
        <p class="info">只支持 .wav 格式的文件，请将其他格式转换成wav格式（编码为 8k、8位）后再上传,单条语音最大支持 5M。</p>
        <form:form action="${ctx}/console/app/file/play/upload" method="post" id="uploadMianForm" enctype="multipart/form-data" target="hidden_frame">
            <div class="input-box ">
                <div class="row margin-bottom-10">
                    <div class="col-md-3">
                    <label class="control-label" for="file_sub_accout_num">关联子账号：</label>
                    </div>
                    <div class="col-md-9 remove-padding">
                    <%--<select id="file_sub_accout_num" class="sublist" name="subId" data-live-search="true" title="">--%>
                    <%--</select>--%>
                        <input id="file_sub_accout_num"  class="form-control" type="text" name="subId" >
                    </div>
                </div>
                <div class="row">
                    <input type="hidden" name="appId" value="${app.id}">
                    <div class="col-md-3">
                        <label>文件 :</label>
                    </div>
                    <div class="col-md-9 remove-padding" >
                        <span class="btn btn-success fileinput-button">
                        <i class="glyphicon glyphicon-plus"></i>
                        <span>选择文件</span>
                            <!-- The file input field used as target for the file upload widget -->
                        <input id="fileupload" type="file" name="file" >
                      </span>
                        <span id="fileName"></span>
                        <br>
                        <br>
                        <!-- The global progress bar -->
                        <div id="progress" class="progress" hidden>
                            <div class="progress-bar progress-bar-success"></div>
                        </div>
                        <div id="files" class="files"></div>
                    </span>
                    </div>
                    <div class="modal-loadding loadding"></div>
                </div>
                <div class="row text-left mt-10">
                    <p>一次允许选择1个文件，并且建议在网络环境好的情况下使用，以防止上传错误文件</p>
                </div>
            </div>
            <input type="reset" id="resetForm" hidden/>
    </div>
    <%--<div class="footer">--%>
        <%--<a class="cancel modalCancel-app-up " data-id="four" >返回</a>--%>
        <%--<a class="sure modalSureFour modalCancel-app-down" data-id="four" >确认</a>--%>
    <%--</div>--%>
    </form:form>

</div>


<%@include file="/inc/footer.jsp"%>

<script type="text/javascript">
    var appId = '${app.id}';
</script>
<script type="text/javascript" src='${resPrefixUrl }/js/bootstrap-datepicker/js/bootstrap-datepicker.js'> </script>
<script type="text/javascript" src='${resPrefixUrl }/js/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js'> </script>
<script type="text/javascript" src='${resPrefixUrl }/js/application/detail.js'> </script>
<!--syncpage-->
<script type="text/javascript" src='${resPrefixUrl }/js/page.js'></script>
<script type="text/javascript" src='${resPrefixUrl }/js/vue/vue.min.js'></script>
<script src="${resPrefixUrl }/bower_components/blueimp-file-upload/js/vendor/jquery.ui.widget.js"></script>
<script src="${resPrefixUrl }/bower_components/blueimp-file-upload/js/jquery.fileupload.js"></script>
<script src="${resPrefixUrl }/bower_components/blueimp-file-upload/js/jquery.fileupload-ui.js"></script>
<script src="${resPrefixUrl }/bower_components/blueimp-file-upload/js/jquery.fileupload-process.js"></script>
<script src="${resPrefixUrl }/bower_components/blueimp-file-upload/js/jquery.iframe-transport.js"></script>
<script src="${resPrefixUrl }/bower_components/blueimp-file-upload/js/jquery.fileupload-validate.js"></script>
<link rel="stylesheet" href="${resPrefixUrl }/js/dist/css/bootstrap-select.css">
<script src="${resPrefixUrl }/js/dist/js/bootstrap-select.js"></script>
        <script type="text/javascript">
            function isnewTemplate(){
                var state = '${authState}';
                if(state==2){
                    $('button[data-id=seven]').click();
                }else{
                    showtoast("你还没进行公司认证，快去认证吧！");
                }
            }
            //多行文本输入框剩余字数计算
            function checkMaxInput(obj, maxLen) {
                if (obj == null || obj == undefined || obj == "") {
                    return;
                }
                if (maxLen == null || maxLen == undefined || maxLen == "") {
                    maxLen = 100;
                }

                var strResult;
                var $obj = $(obj);
                var newid = $obj.attr("id") + 'msg';

                if (obj.value.length > maxLen) { //如果输入的字数超过了限制
                    obj.value = obj.value.substring(0, maxLen); //就去掉多余的字
                    $('#'+newid).html(( obj.value.length)+"/"+maxLen );//maxLen
//                    strResult = '<span id="' + newid + '" class=\'Max_msg\' ><br/>剩(' + (maxLen - obj.value.length) + ')字</span>'; //计算并显示剩余字数
                } else {
                    $('#'+newid).html(( obj.value.length)+"/"+maxLen );
//                    strResult = '<span id="' + newid + '" class=\'Max_msg\' ><br/>剩(' + (maxLen - obj.value.length) + ')字</span>'; //计算并显示剩余字数
                }

//                var $msg = $("#" + newid);
//                if ($msg.length == 0) {
//                    $obj.after(strResult);
//                }
//                else {
//                    $msg.html(strResult);
//                }
            }

            //清空剩除字数提醒信息
            function resetMaxmsg() {
//                $("span.Max_msg").remove();
            }
        </script>
        <script>
            $('.cancelfive').click(function(){
                createSubAccountFive.init();
            });
            $('.cancelsix').click(function(){
                createSix.init();
            });
            $('.cancelseven').click(function(){
                createSeven.init();
            });
            var appServiceType = '${app.serviceType}';
            $(function () {
                $('.modal-box .content input[type="text"]').css("height","30px");
                $('.modal-box .content input[type="number"]').css("height","30px");
                $('#modalfive').css("height","450px");
                $('#modalsix').css("height","330px");
                $('#modalplayvoice').css("height","93px").css("width","380px");
                $('#modalplayvoice2').css("padding","10px").css("margin-top","0px").css("height","36px")
                    .css("font-size:","14px").css("line-height","normal").css("text-align","left").css("background-color","#0099cc").css("font-size","14px");

                $('#modalseven').css("height","510px");
//                $('#modalseven textarea.form-control').css("height","75px");
                $('#template_detail div.col-md-6').css("width","650px");
            })
        </script>

        <script>
            function saveCycle(){
                var cycle = $('input[name=cycle]:checked').val();
                var cycleName = '';
                if(cycle==7){
                    cycleName = "7天";
                }else if(cycle%30==0&&cycle/30>0){
                    cycleName = cycle/30 +"个月";
                }
                bootbox.setLocale("zh_CN");
                var h1="配置存储周期是否更改为："+ cycleName;
                bootbox.confirm(h1, function(result) {
                    if(result){
                        ajaxsync(ctx + "/console/app/edit/recording/"+appId,{csrfParameterName:csrfToken,'cycle':cycle},function(response){
                            if(response.success){
                                showtoast(response.data);
                            }else{
                                showtoast(response.errorMsg);
                            }
                        },"post");
                    }
                });
            }
            function showTestNumBind(){
//                showtoast('当前没有测试号码')
                bootbox.setLocale("zh_CN");
                var h1="当前没有测试号码,是否前往号码管理进行测试号码绑定？";
                bootbox.confirm(h1, function(result) {
                    if(result){
                        window.location.href=ctx+'/console/telenum/bind/index';
                    }
                });
            }

            window.onload=function(){
                var flag = true;
                if(window.navigator.userAgent.indexOf("MSIE")>0) { if(window.navigator.userAgent.indexOf("MSIE 6.0")>0 || window.navigator.userAgent.indexOf("MSIE 7.0")>0 || window.navigator.userAgent.indexOf("MSIE 8.0")>0 || window.navigator.userAgent.indexOf("MSIE 9.0")>0) {flag = false;} } if(!flag){
                    $('#uploadButtonA').unbind("click").bind("click",function(){
                        showtoast("非常抱歉，本站的上传文件功能，暂时不支持IE9及以下的浏览器版本，请更换或者升级浏览器");
                    })
                }
            }
            // 上传多个文件
            var cancelCancel=false;
            $(function(){
                var Allfile = [],allFileLength = 0, errorFileArray = [];
                var file_sub_accout_num = $('#file_sub_accout_num').val();
                $('#fileupload').fileupload({
                    url: '${ctx}/console/app/file/play/upload',
                    maxFileSize: 5* 1024 * 1024,
                    dataType: 'json',
                    autoUpload: true,
                    acceptFileTypes: /(\.|\/)(wav)$/i,
                    add: function(e, data) {
                        $('#progress .progress-bar').css(
                                'width',
                                0 + '%'
                        );
                        var filename = data.files[0].name;
                        var subId = $('#file_sub_accout_num').val();
                        var  re = /^.+(\.wav)$/i;
                        var result=  re.test(filename);
                        if(result){
                            if(data.files[0].size <= (5* 1024 * 1024)) {
//                                ajaxsync(ctx + "/console/app/file/play/total",{csrfParameterName:csrfToken},function(response){
//                                    if((response.data.fileTotalSize-response.data.fileRemainSize)>=data.files[0].size){
                                        ajaxsync(ctx + "/console/app/file/play/verify/name",{csrfParameterName:csrfToken,'appId':appId,'name':filename,'subId':subId},function(response1){
                                            if(!response1.success){
                                                showtoast(response1.errorMsg);
                                                return ;
                                            }
                                            if(response1.data==0){
                                                $('#progress').show();
                                                $('#fileName').html(filename);
                                                data.submit();
                                                $('.modalCancel-app-up').bind("click", function () {
                                                    data.abort();
                                                });
                                                cancelCancel=false;
                                                $('#fileupload').attr('disabled',"disabled");
                                            }else{
                                                bootbox.setLocale("zh_CN");
                                                var h1="该文件名已存在，是否覆盖同名文件";
                                                bootbox.confirm(h1, function(result) {
                                                    if (result) {
                                                        $('#progress').show();
                                                        $('#fileName').html(filename);
                                                        data.submit();
                                                        $('.modalCancel-app-up').bind("click", function () {
                                                            data.abort();
                                                        });
                                                        cancelCancel = false;
                                                        $('#fileupload').attr('disabled', "disabled");
                                                    } else {
                                                        $('#progress').hide();
                                                    }
                                                });
                                            }
                                        },"post");
//                                    }else{
//                                        $('#progress').hide();
//                                        showtoast("存储空间不足，无法上传");
//                                    }
//                                },"post");
                            }else{
                                $('#progress').hide();
                                showtoast("上传文件超过5M");
                            }
                        }else{
                            $('#progress').hide();
                            showtoast("上传格式不正确");
                        }
                    },
                    done: function (e, data) {
                        var resultDate = data._response.jqXHR.responseJSON;
                        if(resultDate.success){
                            showtoast('上传成功');
                        }else {
                            showtoast(resultDate.errorMsg);
                        }
                        $('#progress .progress-bar').css(
                                'width',
                                0 + '%'
                        );
                        isCanner=false;
                        cancelCancel=true;
                        $('.modal-loadding').hide();
                        $('.modalCancel-app-up').click();
//                        fileTotalSoze();
                        upplay();
                    },
                    progressall: function (e, data) {
                        var progress = parseInt(data.loaded / data.total * 100, 10);
                        $('#progress .progress-bar').css(
                                'width',
                                progress + '%'
                        );
                        if(progress==100){
                            $('.modal-loadding').show();
                            isCanner=true;
                        }
                    },fail: function(e, data) {
                        var resultDate = data._response.jqXHR.responseJSON;
                        if(resultDate.success){
                            showtoast('上传失败');
                        }else {
                            showtoast(resultDate.errorMsg);
                        }
                        cancelCancel=true;
                        $('.modal-loadding').hide();
                        $('#fileupload').removeAttr('disabled');
                    }
                });
            })
        </script>




<script>
    $('.modalCancel-app-up').click(function(){
        var v = $('#progress .progress-bar').css('width');
//        if(v=='0%'||v=='0px'||cancelCancel){
            $('#resetForm').click();
            $('#fileName').html("");
            var id = $(this).attr('data-id');
            $('#modal'+id).fadeOut();
            $('#show-bg').fadeOut();
            $('#progress').hide();
            $('#fileupload').removeAttr('disabled');
//        }
        $('#uploadButton').show();
    });
    /**
     *绑定测试电话号码
     */
    $('.modalSureOne').click(function(){
        var id = $(this).attr('data-id');
        var allNum = $('#testNumBindForm').serialize();
        var testNumBindHtml = "";//回显示内容
        var numbers = "";//重新绑定手机号码
        if(allNum.trim().length>0){
            var tempVal = allNum.split("&");
            for(var i=0;i<tempVal.length;i++){
                var temp = tempVal[i].split("=");
                testNumBindHtml += '<span>'+temp[0].split('-')[1]+' </span>';
                numbers+=temp[1]+",";
            }
            if(numbers.lastIndexOf(",")!=-1&&numbers.lastIndexOf(",")==(numbers.length-1)){
                numbers =  ","+numbers;
            }
        }
        ajaxsync(ctx + "/console/telenum/bind/update_app_number",{ 'numbers':numbers,'appId':appId,csrfParameterName:csrfToken},function(response){
            $('#testNumBind').html(testNumBindHtml);
            if(testNumBindHtml.length>0){
                $('#testNumBind').show();
            }else{
                $('#testNumBind').hide();
            }
            hideModal(id);
            showtoast("应用绑定号码更新成功");
        },"post");

    });
    /**修改子账号**/
    var editSubAccountFive = new Vue({
        el:'#subAccount_datail',
        data:{
            id:'',
            certId:'',
            secretKey:'',
            url:'',
            remark:'',
            voiceNum:'',
            seatNum:'',
            ussdNum:'',
            smsNum:'',
            ussdNum:''
        },methods:{
            initObj:function(obj){
                this.id=obj.id;
                this.certId=obj.certId;
                this.secretKey = obj.secretKey;
                this.url= obj.url;
                this.remark=obj.remark;
                this.voiceNum= obj.voiceNum;
                this.seatNum=obj.seatNum;
                this.smsNum = obj.smsNum;
                this.ussdNum = obj.ussdNum;
            },
            initObj2:function(obj){
                this.id=this.certId=this.secretKey = this.url= this.remark='';
                this.voiceNum= this.seatNum=this.ussdNum=this.smsNum=-1;
            }
        }
    });
    function editSubAccount(){
        if(isNaN(editSubAccountFive.voiceNum) ||editSubAccountFive.voiceNum==''||
            isNaN(editSubAccountFive.seatNum) ||editSubAccountFive.seatNum==''||
            isNaN(editSubAccountFive.ussdNum) ||editSubAccountFive.ussdNum==''||
            isNaN(editSubAccountFive.smsNum) ||editSubAccountFive.smsNum==''
        ){
            showtoast("配额参数必须为数字");
            return;
        }
        var re = /^http[s]?:\/\/.+$/;
        if(editSubAccountFive.url!=null&& editSubAccountFive.url.length >0 && !re.test(editSubAccountFive.url)){
            showtoast("回调地址格式错误");
            return;
        }
        if(editSubAccountFive.remark.length > 128){
            showtoast("备注信息长度不能超过128");
            return;
        }
        var params = {
            'id':editSubAccountFive.id,
            'url':editSubAccountFive.url,
            'voiceNum':new Number(editSubAccountFive.voiceNum),
            'seatNum':new Number(editSubAccountFive.seatNum),
            'ussdNum':new Number(editSubAccountFive.ussdNum),
            'smsNum':new Number(editSubAccountFive.smsNum),
            'remark':editSubAccountFive.remark,
            csrfParameterName:csrfToken
        }
        ajaxsync(ctx + "/console/sub_account/edit",params,function(response){
            if(response.success){
                showtoast("修改子账号成功");
            }else{
                showtoast(response.errorMsg);
            }
        },"post");
    };
    /*新增模板*/
    var createSeven = new Vue({
        el:'#createSeven',
        data:{
            type:'ussd',
            name:'',
            content:'',
            remark:''
        },
        methods:{
            init:function(){
                this.type='ussd';
                this.name=this.content=this.remark='';
            }
        }
    });
    /*修改模板*/
    var editSeven = new Vue({
        el:'#template_detail',
        data:{
            id:'',
            tempId:'',
            type:'',
            name:'',
            content:'',
            contentlength:'',
            remark:'',
            remarklength:'',
            state:'',
            reason:''
        },
        methods:{
            isShow:function(state1,state2){
                var flag ;
                if(state2 != undefined ){
                    flag = this.state == state1 ||this.state == state2 ?true:false;
                }else{
                    flag = this.state == state1 ?true:false;
                }
                return flag;
            },
            init:function(){
                this.id=this.tempId=this.type=this.name=this.content=this.remark=this.reason='';
                this.state=-1;
                this.remarklength=0;
                this.contentlength=0;
            },initObj:function(obj){
                this.id=obj.id;
                this.tempId=obj.tempId;
                this.type=obj.type;
                this.name=obj.name;
                this.content=obj.content;
                this.remark=obj.remark;
                this.state = obj.status;
                this.reason = obj.reason;
                this.remarklength=obj.remark!=null?( new Number(obj.remark.length)):100;
                this.contentlength=obj.content!=null?( new Number(obj.content.length)):60;
            }
        }
    });
    function editSevenPost(){
        if(editSeven.content ==''||editSeven.content.length <=0 || editSeven.content.length>62){
            showtoast('模板内容格式错误' );
            return;
        }
        if(editSeven.remark =='' ||  editSeven.remark.length <= 0 || editSeven.remark.length>100){
            showtoast("使用场景说明格式错误");
            return;
        }
        var params = {
            appId:appId,
            name:editSeven.name,
            content:editSeven.content,
            remark:editSeven.remark,
            csrfParameterName:csrfToken
        }
        ajaxsync(ctx + "/console/msg_template/edit/"+editSeven.id,params,function(response){
            if(response.success){
                showtoast("修改模板成功");
                editSeven.state=0;
            }else{
                var error = response.errorMsg;
                showtoast(error);
            }
        },"post");
    };
    $('.modalSureSeven').click(function(){
        var id = $(this).attr('data-id');
        if(createSeven.content ==''||createSeven.content.length <=0 || createSeven.content.length>62){
            showtoast('模板内容格式错误' );
            return;
        }
        if(createSeven.remark =='' ||  createSeven.remark.length <= 0 || createSeven.remark.length>100){
            showtoast("使用场景说明格式错误");
            return;
        }
        var subId = $('#create_six_subId') .val();//$('#create_six_subId option:selected') .val();
        var params = {
            appId:appId,
            type:createSeven.type,
            name:createSeven.name,
            content:createSeven.content,
            remark:createSeven.remark,
            csrfParameterName:csrfToken
        }
        ajaxsync(ctx + "/console/msg_template/new",params,function(response){
            console.info(response);
            if(response.success){
                showtoast("新增模板成功");
                createSeven.init();
                hideModal(id);
                templateList();
            }else{
                var error = response.errorMsg;
                showtoast(error);
            }
        },"post");
    });
    /*新增分机*/
    var createSix = new Vue({
        el:'#createSix',
        data:{
            user:'',
            password:''
        },
        methods:{
            init:function(){
                this.user=this.password='';
            }
        }
    });
    $('.modalSureSix').click(function(){
        var id = $(this).attr('data-id');
        var t1 = /^[0-9]{6,12}$/;
        if(createSix.user ==''||createSix.user.length <=0 || !t1.test(createSix.user)){
            showtoast('分机号格式错误' );
            return;
        }
//        var t2 = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,18}$/;
        if(createSix.password =='' ||  createSix.password.length <= 0 || !t1.test(createSix.password)){
            showtoast("密码格式错误");
            return;
        }
        var subId = $('#create_six_subId') .val();//$('#create_six_subId option:selected') .val();
        var params = {
            subId:subId,
            user:createSix.user,
            password:createSix.password,
            csrfParameterName:csrfToken
        }
        ajaxsync(ctx + "/console/app/"+appId+"/app_extension/new",params,function(response){
            if(response.success){
                showtoast("新增分机成功");
                createSix.init();
                hideModal(id);
                extensionList();
            }else{
                var error = response.errorMsg;
                var t1 ="message=";
                var index = response.errorMsg.indexOf(t1);
                if( index != -1){
                    var t2 = "]";
                    var t3 = response.errorMsg.substring( index + t1.length);
                    var index2 = t3.indexOf( t2 );
                    if(index2!=-1){
                        error = t3.substring(0 , index2 );
                    }else{
                        error = t3;
                    }
                }
                showtoast(error);
            }
        },"post");
    });
    /**
     *新增子账号
     */
    var createSubAccountFive = new Vue({
        el:'#createSubAccountFive',
        data:{
            url:'',
            voiceNum:-1,
            seatNum:-1,
            ussdNum:-1,
            smsNum:-1,
            remark:''
        },
        methods:{
            init:function(){
                this.url=this.remark='';
                this.voiceNum=this.seatNum=this.smsNum=this.ussdNum=-1;
            }
        }
    });
    $('.modalSureFive').click(function(){
        var id = $(this).attr('data-id');
        if(isNaN(createSubAccountFive.voiceNum) || createSubAccountFive.voiceNum==''||
            isNaN(createSubAccountFive.seatNum) ||createSubAccountFive.seatNum=='' ||
            isNaN(createSubAccountFive.smsNum) ||createSubAccountFive.smsNum=='' ||
            isNaN(createSubAccountFive.ussdNum) ||createSubAccountFive.ussdNum==''
        ){
            showtoast("配额参数必须为数字");
            return;
        }
        var re = /^http[s]?:\/\/.+$/;
        if(createSubAccountFive.url!=null&& createSubAccountFive.url.length >0 && !re.test(createSubAccountFive.url)){
            showtoast("回调地址格式错误");
            return;
        }
        if(createSubAccountFive.remark.length > 128){
            showtoast("备注信息长度不能超过128");
            return;
        }
        var params = {
            'appId':appId,
            'url':createSubAccountFive.url,
            'voiceNum':createSubAccountFive.voiceNum,
            'seatNum':createSubAccountFive.seatNum,
            'smsNum':createSubAccountFive.smsNum,
            'ussdNum':createSubAccountFive.ussdNum,
            'remark':createSubAccountFive.remark,
            csrfParameterName:csrfToken
        }
        ajaxsync(ctx + "/console/sub_account/new",params,function(response){
           if(response.success){
               showtoast("新增子账号成功");
               createSubAccountFive.init();
               hideModal(id);
               subAccountList();
           }else{
               showtoast(response.errorMsg);
           }
        },"post");
    });

    /**
     * 批量下载处理
     */
    $('.modalSureTwo').click(function(){
        var id = $(this).attr('data-id');
        //异步查询文件信息


        //hideModal(id)
        //添加回调数据
        var html  = '<p>--下载成功</p>';
        $('#scrolldiv'+id).append(html);
    });

    $('.cancelthree').click(function(){
        batchclear($(this).attr('data-id'));
    })
    function batchclear(id){
        $('#scrolldiv'+id).html('');
        $('#datestart'+id).val('');
        $('#dateend'+id).val('');
    }
    /**
     * 单文件
     */
    $('.singlefile').click(function(){
        $('#singlefile').click();
    });

    /**
     * 多文件
     */
    $('.batchfile').click(function(){
        $('#batchfile').click();
    });


    /**
     *生成修改备注文件
     *
     * */
    function editremark(thisT,type){
        $('#editmark-tips').html('');
        bootbox.setLocale("zh_CN");
        bootbox.dialog({
                    title: "修改备注",
                    message: '<div class="row">  ' +
                    '<div class="col-md-12"> ' +
                    '<form class="form-horizontal"> ' +
                    '<div class="form-group"> ' +
                    '<label class="col-md-2 control-label" for="name">备注: </label> ' +
                    '<div class="col-md-8"> ' +
                    '<input id="edit-remark-content" type="text" placeholder="" class="form-control input-md"> ' +
                    '<span class="tips-error" id="editmark-tips"></span> </div> ' +
                    '</div> ' +
                    '</div> </div>' +
                    '</form> </div>  </div>',
                    buttons: {
                        success: {
                            label: "保存",
                            className: "btn-success",
                            callback: function () {
                                var remark = $('#edit-remark-content').val();
                                if(!remark){
                                    $('#editmark-tips').html('请填写备注内容'); return false;
                                }
                                var id = new String(thisT.id).replace('remark-b-','');
                                //异步请求修改数据

                                ajaxsync(ctx + "/console/app/file/play/modify",{'id':id,'remark':remark,csrfParameterName:csrfToken},function(response){
                                    if(response.success){
                                        showtoast("修改成功");
                                        //成功执行
                                        if(type=='1'){
                                            $('#remark-b-'+id).html('修改备注');
                                        }
                                        $('#remark-a-'+id).html(remark);
                                    }else{
                                        showtoast("修改失败");
                                    }
                                },"post");

                            }
                        }
                    }
                }
        );
    }


    /**
     * 删除操作
     */
    function delplay(idType){
        bootbox.setLocale("zh_CN");
        bootbox.confirm("确认删除文件", function(result) {
            if(result){
                var id = new String(idType.id).replace("delete-","");
                ajaxsync(ctx + "/console/app/file/play/delete",{'id':id,csrfParameterName:csrfToken},function(response){
                    if(response.success){
                        showtoast("删除成功");
                        if(pagePlay){
                            var currentPage;
                            if(((pagePlay.nowPage - 1) * pagePlay.listRow +1) <= --pagePlay.count){
                                currentPage = pagePlay.nowPage;
                            }else {
                                currentPage = pagePlay.nowPage - 1;
                            }
                            if(currentPage> 0){
                                $('#page' + currentPage + pagePlay.obj).click();
                            }else{
                                $('#play-'+id).remove();
                                $('#page0'+pagePlay.obj).hide();
                            }
                        }
                    }else{
                        showtoast("删除失败");
                    }
                },"post");

            }
        });
    }
    function delAgent(id){
        bootbox.setLocale("zh_CN");
        bootbox.confirm("确认删除坐席", function(result) {
            if(result){
                ajaxsync(ctx + "/console/app/"+appId+"/agent/delete/"+id,{csrfParameterName:csrfToken},function(response){
                    if(response.success){
                        showtoast("删除成功");
                        if(extensionPage){
                            var currentPage;
                            if(((agentPage.nowPage - 1) * agentPage.listRow +1) <= --agentPage.count){
                                currentPage = agentPage.nowPage;
                            }else {
                                currentPage = agentPage.nowPage - 1;
                            }
                            if(currentPage>0){
                                $('#page' + currentPage + agentPage.obj).click();
                            }else{
                                $('#extension-'+id).remove();
                                $('#page0'+agentPage.obj).hide();
                            }
                        }
                    }else{
                        showtoast("删除失败");
                    }
                },"post");

            }
        });
    }
    function delTemplate(id){
        bootbox.setLocale("zh_CN");
        bootbox.confirm("确认删除模板", function(result) {
            if(result){
                ajaxsync(ctx + "/console/msg_template/delete/"+id,{csrfParameterName:csrfToken},function(response){
                    if(response.success){
                        showtoast("删除成功");
                        if(templatePage){
                            var currentPage;
                            if(((templatePage.nowPage - 1) * templatePage.listRow +1) <= --templatePage.count){
                                currentPage = templatePage.nowPage;
                            }else {
                                currentPage = templatePage.nowPage - 1;
                            }
                            if(currentPage>0){
                                $('#page' + currentPage + templatePage.obj).click();
                            }else{
                                $('#template-'+id).remove();
                                $('#page0'+templatePage.obj).hide();
                            }
                        }
                    }else{
                        showtoast("删除失败");
                    }
                },"post");

            }
        });
    }
    function delExtension(id){
        bootbox.setLocale("zh_CN");
        bootbox.confirm("确认删除分机", function(result) {
            if(result){
                ajaxsync(ctx + "/console/app/"+appId+"/app_extension/delete/"+id,{csrfParameterName:csrfToken},function(response){
                    if(response.success){
                        showtoast("删除成功");
                        if(extensionPage){
                            var currentPage;
                            if(((extensionPage.nowPage - 1) * extensionPage.listRow +1) <= --extensionPage.count){
                                currentPage = extensionPage.nowPage;
                            }else {
                                currentPage = extensionPage.nowPage - 1;
                            }
                            if(currentPage>0){
                                $('#page' + currentPage + extensionPage.obj).click();
                            }else{
                                $('#extension-'+id).remove();
                                $('#page0'+extensionPage.obj).hide();
                            }
                        }
                    }else{
                        showtoast("删除失败");
                    }
                },"post");

            }
        });
    }
    /**
     * 删除操作
     */
    function delSubAccount(id){
        bootbox.setLocale("zh_CN");
        bootbox.confirm("确认删除子账号", function(result) {
            if(result){
                ajaxsync(ctx + "/console/sub_account/delete/"+id,{csrfParameterName:csrfToken},function(response){
                    if(response.success){
                        showtoast("删除成功");
                        if(subAccountPage){
                            var currentPage;
                            if(((subAccountPage.nowPage - 1) * subAccountPage.listRow +1) <= --subAccountPage.count){
                                currentPage = subAccountPage.nowPage;
                            }else {
                                currentPage = subAccountPage.nowPage - 1;
                            }
                            if(currentPage>0){
                                $('#page' + currentPage + subAccountPage.obj).click();
                            }else{
                                $('#play-'+id).remove();
                                $('#page0'+subAccountPage.obj).hide();
                            }
                        }
                        console.info("进入-结束")
                    }else{
                        showtoast("删除失败");
                    }
                },"post");

            }
        });
    }

    /**
     *触发放音文件分页
     */
    var pagePlay ;

    function upplay(){
        $('#uploadButton').show();
        //获取数据总数
        var count = 0;
        var name = $('#name').val();
        var subId = $('#voice_file_subId') .val();//$('#voice_file_subId option:selected') .val();
        ajaxsync(ctx + "/console/app/file/play/list",{'name':name,'appId':appId,'pageNo':1,'pageSize':20,'subId':subId,csrfParameterName:csrfToken},function(response){
            if(response.success){
                count=response.data.totalCount;
            }else{
                showtoast(response.errorMsg?response.errorMsg:'数据异常');
            }
        },"post");

        //每页显示数量
        var listRow = 20;
        //显示多少个分页按钮
        var showPageCount = 5;
        //指定id，创建分页标签
        var pageId = 'playpage';
        //searchTable 为方法名
        pagePlay= new Page(count,listRow,showPageCount,pageId,playTable);
        pagePlay.show();
    }
    var fileTotalSoze = function(){

//        ajaxsync(ctx + "/console/app/file/play/total",{csrfParameterName:csrfToken},function(response){
//            $('#voiceFilePlay').html("共计" + resultFileSize(response.data.fileTotalSize) + ",已占用" + resultFileSize(response.data.fileRemainSize)+ "");
//        },"post");

    };
    var resultFileSize = function(temp){
        if(temp>(1024*1024)){
            temp = (temp/1024/1024).toFixed(2)+"MB";
        }else if(temp>1024){
            temp = (temp/1024).toFixed(2)+"KB";
        }else{
            temp = temp+"B";
        }
        return temp;
    }
    /**
     * 分页回调方法
     * @param nowPage 当前页数
     * @param listRows 每页显示多少条数据
     * */
    var playTable = function(nowPage,listRows)
    {
        var name = $('#name').val();
        var subId = $('#voice_file_subId') .val();//$('#voice_file_subId option:selected') .val();
        ajaxsync(ctx + "/console/app/file/play/list",{'name':name,'appId':appId,'pageNo':nowPage,'pageSize':listRows,'subId':subId,csrfParameterName:csrfToken},function(response){
            if(response.success){
                var data =[];
                for(var j=0;j<response.data.result.length;j++){
                    var tempFile = response.data.result[j];
                    var temp = [
                        tempFile.id,
                        tempFile.name,
                        tempFile.status,
                        resultFileSize(tempFile.size),
                        tempFile.remark,
                        tempFile.reason?tempFile.reason:'',
                        tempFile.sync,
                        tempFile.certId,
                        tempFile.fileKey
                    ];
                    data[j]=temp;
                }
                var html ='';
                //数据列表
                for(var i = 0 ; i<data.length; i++){
                    html +='<tr class="playtr" id="play-'+data[i][0]+'"><td class="voice-format">'+data[i][1]+'</td>';
                    if(data[i][2]==-1){
                        html+='<td  title="审核不通过原因：'+data[i][5]+'"><span class="nosuccess">审核不通过</span><i class="fa fa-exclamation-triangle"></i></td>';
                    }else if(data[i][2]==1&&data[i][6]==1){
                        html+='<td ><span class="success">已审核</span></td>';
                    }else{
                        html+='<td>待审核</td>';
                    }
                    html+='<td>'+data[i][3]+'</td>';
                    html +='<td>'+(data[i][7] == null?'':data[i][7] )+'</td>';
                    html+='<td id="remark-a-'+data[i][0]+'">'+data[i][4]+'</td>';
                    html+='<td class="operation"> <a onclick="playvoice(\''+data[i][8]+'\')" >试听</a><span ></span> <a onclick="delplay(this)" id="delete-'+data[i][0]+'" >删除</a> <span ></span> <a onclick="editremark(this)" id="remark-b-'+data[i][0]+'">修改备注</a> </td></tr>';
                }
                $('#playtable').find(".playtr").remove();
                $('#playtable').append(html);
            }else{
                showtoast(response.errorMsg?response.errorMsg:'数据异常');
            }
        },"post");

    }
    function playvoice(id){
        console.info(id);
        $('#playvoicesrc').attr("src",ctx + "/console/oss/voice?uri="+id);
        $('button[data-id=playvoice]').click();
    }
    $('.cancelplayvoice').click(function(){
        var audio = document.getElementById('playvoicesrc');
        if(audio!==null){
            //检测播放是否已暂停.audio.paused 在播放器播放时返回false.<span style="font-family: Arial, Helvetica, sans-serif;">在播放器暂停时返回true</span>
            if(!audio.paused)
            {
                audio.pause();// 这个就是暂停//audio.play();// 这个就是播放
            }
        }
    });
    //默认加载放音文件分页
    $(function () {
        var type = $('#myTab li[class=active]').attr('data-id');
        updateList(type);
    });
    $('#myTab li').click(function(){
        var type = $(this).attr('data-id');
        updateList(type);
    });
    function updateList(type){
        if(type=='play'){
            upplay();
            $('#uploadButton').show();
        }else{
            if(type=='voice'){
                //录音文件暂时没文件
            }
            if(type=='number'){
                upnumber();
            }
            if(type=='extension'){
                extensionList();
            }
            if(type=='agent'){
                agentList();
            }
            if(type=='subAccount' ){
                tosubAccountHome();
                subAccountList();
            }
            if(type=='queue'){
                queueList();
            }
            if(type=='template'){
                totemplateHome();
                templateList();
            }
            //除了放音文件标签外，其他都不显示上传放音文件按钮
            $('#uploadButton').hide();
        }
    }
    // 号码绑定
    var vue = new Vue({
        el: '#vue-application',
        data : {
            isSubAccount:'0',
            numUrl:'',
            isCall: ["✘", "✔"],
            phonelist:[],
            shop:[],
            shopCheck:false,
            page:[]
        },
        watch:{
            'shop':function () {
                this.intersect()
            }
        },
        methods:{
            clear:function () {
                this.phonelist = []
                this.shop = []
                this.shopCheck = false,
                        this.page = []
            },
            isCheck:function () {
                var p = this.phonelist
                var s = this.shop
                if(!this.shopCheck){
                    p.forEach(function (ex) {
                        if (!contains(s, ex.num))
                            s.push(ex.num);
                    })
                }else{
                    p.forEach(function (ex) {
                        if (contains(s, ex.num)){
                            console.log("remove" +ex.num);
                            s.remove(ex.num);
                        }

                    })
                }
                this.shop = s;
            },
            band:function () {
                var list =  this.shop;
                if(this.isSubAccount == 0){//应用下绑定
                    // 绑定号码
                    ajaxsync(ctx + "/console/telenum/callnum/num/bind/app/" + appId ,{nums:list.join(",")},function(response) {
                        if(response.success){
                            showtoast("绑定成功");
                        }else{
                            showtoast(response.errorMsg?response.errorMsg:'数据异常，请稍后重试！');
                        }
                    });
                    // 成功隐藏
                    $('#call-modal').modal('hide')
                    //刷新号码绑定分页列表
                    upnumber();
                }else if (this.isSubAccount == 1){//子账号绑定
                    // 绑定号码
                    ajaxsync(ctx +this.numUrl ,{nums:list.join(",")},function(response) {
                        if(response.success){
                            showtoast("绑定成功");
                        }else{
                            showtoast(response.errorMsg?response.errorMsg:'数据异常，请稍后重试！');
                        }
                    });
                    // 成功隐藏
                    $('#call-modal').modal('hide')
                    //刷新号码绑定分页列表
                    upnumber2();
                }

            },
            intersect:function () {
                var point = Array.intersect(this.shop,this.page);
                if(point.length >= this.page.length && this.page.length >0)
                    this.shopCheck = true
                else
                    this.shopCheck = false
            },
            currentPage:function () {
                var cp = []
                this.phonelist.forEach(function (ex) {
                    cp.push(ex.num)
                })
                this.page = cp
                this.intersect()
            },
            setPhoneList: function (nowPage, listRows) {
                this.shopCheck = false

                var data = [];
                ajaxsync(ctx + "/console/telenum/callnum/num/unused/app/" + appId ,{ 'type':this.isSubAccount,'pageNo':nowPage,'pageSize':listRows},function(response) {
                    if(response.success){
                        data = response.data.result;
                    }else{
                        showtoast(response.errorMsg?response.errorMsg:'数据异常，请稍后重试！');
                    }
                });

                this.phonelist = data //赋值
                this.currentPage()  // 初始化
            }
        }
    });

    //数组辅助 元素是否存在数组内
    function contains(a, obj) {
        for (var i = 0; i < a.length; i++) {
            if (a[i] === obj)
                return true;
        }
        return false;
    }

    ///数组辅助
    Array.prototype.indexOf = function(val) {
        for (var i = 0; i < this.length; i++) {
            if (this[i] == val) return i;
        }
        return -1;
    };

    ///数组辅助 删除指定元素
    Array.prototype.remove = function(val) {
        var index = this.indexOf(val);
        if (index > -1) {
            this.splice(index, 1);
        }
    };

    ///数组辅助 集合取交集
    Array.intersect = function () {
        var result = new Array();
        var obj = {};
        for (var i = 0; i < arguments.length; i++) {
            for (var j = 0; j < arguments[i].length; j++) {
                var str = arguments[i][j];
                if (!obj[str]) {
                    obj[str] = 1;
                }
                else {
                    obj[str]++;
                    if (obj[str] == arguments.length)
                    {
                        result.push(str);
                    }
                }
            }
        }
        return result;
    }

    $('#call-number').click(function () {
        vue.clear();  //重置数据
        vue.isSubAccount =0;
        modalPage(0);
        $('#call-modal').modal('show');
    });
    $('#call-number2').click(function () {
        vue.clear();  //重置数据
        vue.isSubAccount =1;
        var subId = $('#call-number2').attr("data-num-bind");
        vue.numUrl= "/console/sub_account/num/bind/app/"+appId+"/"+subId;
        modalPage(1);
        $('#call-modal').modal('show');
    });
    //分页
    function modalPage(type) {
        //获取数据总数
        var count = 0;
        var params = {"pageNo":1,"pageSize":10,"type":type};
        ajaxsync(ctx + "/console/telenum/callnum/num/unused/app/" + appId ,params,function(response) {
            if(response.success){
                count = response.data.totalCount;
            }else{
                showtoast(response.errorMsg?response.errorMsg:'数据异常，请稍后重试！');
            }
        });

        //每页显示数量
        var listRow = 10;
        //显示多少个分页按钮
        var showPageCount = 5;
        //指定id，创建分页标签
        var pageId = 'datatablepage';
        //searchTable 为方法名
        var page = new Page(count, listRow, showPageCount, pageId, searchTable);
        page.show();
    }

    /**
     * 分页回调方法
     * @param nowPage 当前页数
     * @param listRows 每页显示多少条数据
     * */
    var searchTable = function (nowPage, listRows) {
        vue.setPhoneList(nowPage, listRows);
    }

    var bindNumPage;
    /**
     *绑定号码分页
     */
    function upnumber(){
        //获取数据总数
        var count = 0;
        var params = {"pageNo":1,"pageSize":10};
        ajaxsync(ctx + "/console/telenum/callnum/list/app/" + appId ,params,function(response) {
            if(response.success){
                count = response.data.totalCount;
            }else{
                showtoast(response.errorMsg?response.errorMsg:'数据异常，请稍后重试！');
            }
        });
        //每页显示数量
        var listRow = 10;
        //显示多少个分页按钮
        var showPageCount = 5;
        //指定id，创建分页标签
        var pageId = 'bandpage';
        //searchTable 为方法名
        bindNumPage = new Page(count,listRow,showPageCount,pageId,numberTable);
        bindNumPage.show();
    }

    /**
     * 分页回调方法
     * @param nowPage 当前页数
     * @param listRows 每页显示多少条数据
     * */
    var numberTable = function(nowPage,listRows){
        var html = '';
        var data = [];
        ajaxsync(ctx + "/console/telenum/callnum/list/app/" + appId,{pageNo:nowPage,pageSize:listRows},function(response){
            if(response.success){
                data = response.data.result;
            }else{
                showtoast(response.errorMsg?response.errorMsg:'数据异常');
            }
        },"get");

        // $('#playtable').find(".playtr").remove();["✔", "✘"],
        for(var i =0 ; i<data.length; i++){
            html +='<tr id="rent-'+ data[i].rentId +'">' +
                    '<td class="text-center">'+nulltostr(data[i].num)+'</td>' +
                    '<td class="text-center">' + (data[i].status == 0 ? '过期': '正常') + '</td>' +
                    '<td class="text-center">'+ (data[i].isCalled == 0 ? '✘': '✔') +'</td>' +
                    '<td class="text-center">'+ (data[i].isDialing == 0 ? '✘': '✔') +'</td>' +
                    '<td class="text-center"><span class="text-center-l-fixed">'+data[i].areaCode+'</span></td>' +
                    '<td class="text-center"> ' + nulltostr(data[i].expireTime) + ' </td>' +
                    '<td class="text-center"><a onclick="unband(\''+data[i].rentId+'\')">解除绑定</a></td>' +
                    '</tr>'
        }
        $('#band-table').html(html);
    }
    /**
     *绑定号码分页
     */
    var bindNumPage2;
    function upnumber2(){
        //获取数据总数
        var count = 0;
        var subId = $('#call-number2').attr("data-num-bind");
        var params = {"pageNo":1,"pageSize":10,'appId':appId,"subId":subId};
        ajaxsync(ctx + "/console/sub_account/num/list"  ,params,function(response) {
            if(response.success){
                count = response.data.totalCount;
            }else{
                showtoast(response.errorMsg?response.errorMsg:'数据异常，请稍后重试！');
            }
        });
        //每页显示数量
        var listRow = 10;
        //显示多少个分页按钮
        var showPageCount = 5;
        //指定id，创建分页标签
        var pageId = 'bandpage';
        //searchTable 为方法名
        bindNumPage2 = new Page(count,listRow,showPageCount,pageId,numberTable2);
        bindNumPage2.show();
    }

    /**
     * 分页回调方法
     * @param nowPage 当前页数
     * @param listRows 每页显示多少条数据
     * */
    var numberTable2 = function(nowPage,listRows){
        var html = '';
        var data = [];
        var subId = $('#call-number2').attr("data-num-bind");
        ajaxsync(ctx + "/console/sub_account/num/list" ,{pageNo:nowPage,pageSize:listRows,'appId':appId,"subId":subId},function(response){
            if(response.success){
                data = response.data.result;
            }else{
                showtoast(response.errorMsg?response.errorMsg:'数据异常');
            }
        },"get");

        // $('#playtable').find(".playtr").remove();["✔", "✘"],
        for(var i =0 ; i<data.length; i++){
            html +='<tr id="rent-'+ data[i].rentId +'">' +
                '<td class="text-center">'+nulltostr(data[i].num)+'</td>' +
                '<td class="text-center">' + (data[i].status == 0 ? '过期': '正常') + '</td>' +
                '<td class="text-center">'+ (data[i].isCalled == 0 ? '✘': '✔') +'</td>' +
                '<td class="text-center">'+ (data[i].isDialing == 0 ? '✘': '✔') +'</td>' +
                '<td class="text-center"><span class="text-center-l-fixed">'+data[i].areaCode+'</span></td>' +
                '<td class="text-center"> ' + nulltostr(data[i].expireTime) + ' </td>' +
                '<td class="text-center"><a onclick="unband2(\''+data[i].rentId+'\')">解除绑定</a></td>' +
                '</tr>'
        }
        $('#band-table2').html(html);
    }
    /**
     * 解除绑定
     * */
    function unband2(id) {
        bootbox.setLocale("zh_CN");
        bootbox.dialog({
                title: "提示",
                message: '<div class="row">  ' +
                '<div class="col-md-12 text-center">号码解绑后保留与应用的关联，但不再与当前子账号关联，确定要解绑该号码吗？</div>  </div>',
                buttons: {
                    success: {
                        label: "确认",
                        className: "btn-primary",
                        callback: function () {
                            var subId = $('#call-number2').attr("data-num-bind");
                            ajaxsync(ctx + "/console/sub_account/unbind/one/"+ appId +"/"+subId+"/" + id ,null,function(response) {
                                if(response.success){
                                    showtoast("解除绑定成功")
                                    if(bindNumPage2){
                                        var currentPage;
                                        if(((bindNumPage2.nowPage - 1) * bindNumPage2.listRow +1) <= --bindNumPage2.count){
                                            currentPage = bindNumPage2.nowPage;
                                        }else {
                                            currentPage = bindNumPage2.nowPage - 1;
                                        }
                                        if(currentPage> 0){
                                            $('#page' + currentPage + bindNumPage2.obj).click();
                                        }else{
                                            $('#rent-'+id).remove();
                                            $('#page0'+bindNumPage2.obj).hide();
                                        }
                                    }
                                }else{
                                    showtoast(response.errorMsg?response.errorMsg:'数据异常，请稍后重试！');
                                }
                            });
                            //异步加载数据，释放成功
                        }
                    },
                    cancel:{
                        label: "关闭",
                        className: "btn-default",
                    }
                }
            }
        );
    }
    /**
     * 解除绑定
     * */
    function unband(id) {
        bootbox.setLocale("zh_CN");
        bootbox.dialog({
                    title: "提示",
                    message: '<div class="row">  ' +
                    '<div class="col-md-12 text-center">号码解绑后该号码可回到号码池中，不再与当前应用发生关联，确定要解绑该号码吗？</div>  </div>',
                    buttons: {
                        success: {
                            label: "确认",
                            className: "btn-primary",
                            callback: function () {
                                ajaxsync(ctx + "/console/telenum/callnum/app/" + appId + "/unbind/" + id ,null,function(response) {
                                    if(response.success){
                                        showtoast("解除绑定成功")
                                        if(bindNumPage){
                                            var currentPage;
                                            if(((bindNumPage.nowPage - 1) * bindNumPage.listRow +1) <= --bindNumPage.count){
                                                currentPage = bindNumPage.nowPage;
                                            }else {
                                                currentPage = bindNumPage.nowPage - 1;
                                            }
                                            if(currentPage> 0){
                                                $('#page' + currentPage + bindNumPage.obj).click();
                                            }else{
                                                $('#rent-'+id).remove();
                                                $('#page0'+bindNumPage.obj).hide();
                                            }
                                        }
                                    }else{
                                        showtoast(response.errorMsg?response.errorMsg:'数据异常，请稍后重试！');
                                    }
                                });
                                //异步加载数据，释放成功
                            }
                        },
                        cancel:{
                            label: "关闭",
                            className: "btn-default",
                        }
                    }
                }
        );
    }

    /**
     * 解除全部绑定
     * */
    function unallband() {
        bootbox.setLocale("zh_CN");
        bootbox.dialog({
                    title: "提示",
                    message: '<div class="row">  ' +
                    '<div class="col-md-12 text-center">你确认要执行这操作吗？</div>  </div>',
                    buttons: {
                        success: {
                            label: "确认",
                            className: "btn-primary",
                            callback: function () {
                                ajaxsync(ctx + "/console/telenum/callnum/app/" + appId + "/unbind_all" ,null,function(response) {
                                    if(response.success){
                                        showtoast("全部解除绑定成功");
                                        //异步加载数据，释放成功
                                        upnumber();
                                    }else{
                                        showtoast(response.errorMsg?response.errorMsg:'数据异常，请稍后重试！');
                                    }
                                });
                            }
                        },
                        cancel:{
                            label: "关闭",
                            className: "btn-default",
                        }
                    }
                }
        );
    }
    /**
     * 解除全部绑定
     * */
    function unallband2() {
        bootbox.setLocale("zh_CN");
        bootbox.dialog({
                title: "提示",
                message: '<div class="row">  ' +
                '<div class="col-md-12 text-center">你确认要执行这操作吗？</div>  </div>',
                buttons: {
                    success: {
                        label: "确认",
                        className: "btn-primary",
                        callback: function () {
                            var subId = $('#call-number2').attr("data-num-bind");
                            ajaxsync(ctx + "/console/sub_account/unbind/all/" + appId + "/"+subId ,null,function(response) {
                                if(response.success){
                                    showtoast("全部解除绑定成功");
                                    //异步加载数据，释放成功
                                    upnumber2();
                                }else{
                                    showtoast(response.errorMsg?response.errorMsg:'数据异常，请稍后重试！');
                                }
                            });
                        }
                    },
                    cancel:{
                        label: "关闭",
                        className: "btn-default",
                    }
                }
            }
        );
    }
    /**
     * 模板分页
     */
    var templatePage;
    function templateList(){

        //获取数据总数
        var count = 0;
        var templateNum = $('#template_num').val();
        var subId = $('#tempate_subId') .val();//$('#extension_subId option:selected') .val();
        var params = {"pageNo":1,"pageSize":10,"name":templateNum,'subId':subId,'appId':appId};
        ajaxsync(ctx + "/console/msg_template/plist" ,params,function(response) {
            if(response.success){
                count = response.data.totalCount;
            }else{
                showtoast(response.errorMsg?response.errorMsg:'数据异常，请稍后重试！');
            }
        },"get");
        //每页显示数量
        var listRow = 10;
        //显示多少个分页按钮
        var showPageCount = 5;
        //指定id，创建分页标签
        var pageId = 'template-page';
        //searchTable 为方法名
        templatePage = new Page(count,listRow,showPageCount,pageId,templateTable);
        templatePage.show();
    }
    var templateTable = function(nowPage,listRows){
        var html = '';
        var data = [];
        var templateNum = $('#template_num').val();
        var subId = $('#template_subId') .val();//$('#extension_subId option:selected') .val();
        var params = {"pageNo":nowPage,"pageSize":listRows,"name":templateNum,'subId':subId,'appId':appId};
        ajaxsync(ctx + "/console/msg_template/plist" ,params,function(response){
            if(response.success){
                data = response.data.result;
            }else{
                showtoast(response.errorMsg?response.errorMsg:'数据异常');
            }
        },"get");
        for(var i =0 ; i<data.length; i++){
            html +='<tr id="template-'+ data[i].id +'">' +
                '<td class="">' + data[i].tempId + '</td>' +
                '<td class="">'+ (data[i].type == 'sms'?'短信':( data[i].type=='ussd'?'闪印':'未知')) +'</td>' +
                '<td class="">'+ data[i].name +'</td>' +
                '<td class="">'+ (data[i].content) +'</td>' +
                '<td class="">'+ (data[i].certId) +'</td>';
            var state = data[i].state=='1'?'审核已通过':(data[i].state=='0'?'待审核':(data[i].state=='-1'?'审核不通过！':'未知')) ;
            var color = data[i].state == 1?"text-success":"text-danger";
            var reason = data[i].reason==null?'':data[i].reason;
            html+= '<td class="'+color+'" id="enable_'+data[i].id+'" title="审核不通过原因:'+reason+'">' + state+ '</td>' +
                '<td class=""><a href="javascript:totemplateDetail(\''+data[i].id+'\')" >详情</a>&nbsp;<a href="javascript:delTemplate(\''+data[i].id+'\')" >删除</a></td>' +
                '</tr>'
        }
        $('#template-list').html(html);
    }
    /**
     * 分机分页
     */
    var extensionPage;
    function extensionList(){

        //获取数据总数
        var count = 0;
        var extensionNum = $('#extension_num').val();
        var subId = $('#extension_subId') .val();//$('#extension_subId option:selected') .val();
        var params = {"pageNo":1,"pageSize":10,"extensionNum":extensionNum,'subId':subId};
        ajaxsync(ctx + "/console/app/" + appId + "/app_extension/page" ,params,function(response) {
            if(response.success){
                count = response.data.totalCount;
            }else{
                showtoast(response.errorMsg?response.errorMsg:'数据异常，请稍后重试！');
            }
        },"get");
        //每页显示数量
        var listRow = 10;
        //显示多少个分页按钮
        var showPageCount = 5;
        //指定id，创建分页标签
        var pageId = 'extension-page';
        //searchTable 为方法名
        extensionPage = new Page(count,listRow,showPageCount,pageId,extensionTable);
        extensionPage.show();
    }

    /**
     * 分机分页回调方法
     * @param nowPage 当前页数
     * @param listRows 每页显示多少条数据
     * */
    var extensionTable = function(nowPage,listRows){
        var html = '';
        var data = [];
        var extensionNum = $('#extension_num').val();
        var subId = $('#extension_subId') .val();//$('#extension_subId option:selected') .val();
        var params = {"pageNo":nowPage,"pageSize":listRows,"extensionNum":extensionNum,'subId':subId};
        ajaxsync(ctx + "/console/app/" + appId + "/app_extension/page" ,params,function(response){
            if(response.success){
                data = response.data.result;
            }else{
                showtoast(response.errorMsg?response.errorMsg:'数据异常');
            }
        },"get");

        // $('#playtable').find(".playtr").remove();["✔", "✘"],
        for(var i =0 ; i<data.length; i++){
            html +='<tr id="extension-'+ data[i].id +'">' +
                    '<td class="">'+ nulltostr(data[i].id) +'</td>' +
                    '<td class="">' + nulltostr(data[i].user) + '</td>' +
                    '<td class="">'+ nulltostr(data[i].password) +'</td>' +
                    '<td class="">'+ nulltostr(data[i].certId ) +'</td>' +
                    '<td class="">'+ (data[i].enable?'可用':'不可用') +'</td>' +
                    '<td class="">'+ (data[i].type==1?'SIP 终端':(data[i].type==2?'SIP 网关':(data[i].type==3?'普通电话':'未知类型'))) +'</td>' +
                    '<td class=""><a href="javascript:delExtension(\''+data[i].id+'\')" >删除</a></td>' +
                    '</tr>'
        }
        $('#extension-list').html(html);
    }
    var subAccountPage;
    function subAccountList(){
        var certId = $('#five_certId').val();
        //获取数据总数
        var count = 0;
        var params = {"pageNo":1,"pageSize":10,"appId":appId,"certId":  certId};
        ajaxsync(ctx + "/console/sub_account/list" ,params,function(response) {
            if(response.success){
                count = response.data.totalCount;
            }else{
                showtoast(response.errorMsg?response.errorMsg:'数据异常，请稍后重试！');
            }
        },"get");
        //每页显示数量
        var listRow = 20;
        //显示多少个分页按钮
        var showPageCount = 5;
        //指定id，创建分页标签
        var pageId = 'subAccountpage';
        //searchTable 为方法名
        subAccountPage = new Page(count,listRow,showPageCount,pageId,subAccountTable);
        subAccountPage.show();
    }
    /**
     * 坐席分页
     */
    var agentPage;
    function agentList(){
        //获取数据总数
        var count = 0;
        var agentNum = $('#agent_num').val();
        var subId = $('#agent_subId') .val();//$('#agent_subId option:selected') .val();
        var params = {"pageNo":1,"pageSize":10,"agentNum":agentNum,"subId":subId};
        ajaxsync(ctx + "/console/app/" + appId + "/agent/page" ,params,function(response) {
            if(response.success){
                count = response.data.totalCount;
            }else{
                showtoast(response.errorMsg?response.errorMsg:'数据异常，请稍后重试！');
            }
        },"get");
        //每页显示数量
        var listRow = 10;
        //显示多少个分页按钮
        var showPageCount = 5;
        //指定id，创建分页标签
        var pageId = 'agent-page';
        //searchTable 为方法名
        agentPage = new Page(count,listRow,showPageCount,pageId,agentTable);
        agentPage.show();
    }
    /**
     * 坐席分页
     */
    var queuePage;
    function queueList(){
        //获取数据总数
        var count = 0;
        var queueNum = $('#queue_num').val();
        var subId = $('#queue_subId') .val();//$('#queue_subId option:selected') .val();
        var params = {"pageNo":1,"pageSize":10,"queueNum":queueNum,"subId":subId};
        ajaxsync(ctx + "/console/app/" + appId + "/queue/page" ,params,function(response) {
            if(response.success){
                count = response.data.totalCount;
            }else{
                showtoast(response.errorMsg?response.errorMsg:'数据异常，请稍后重试！');
            }
        },"get");
        //每页显示数量
        var listRow = 10;
        //显示多少个分页按钮
        var showPageCount = 5;
        //指定id，创建分页标签
        var pageId = 'queue-page';
        //searchTable 为方法名
        queuePage = new Page(count,listRow,showPageCount,pageId,queueTable);
        queuePage.show();
    }
    var subAccountTable = function (nowPage,listRows) {
        var certId = $('#five_certId').val();
        var html = '';
        var data = [];
        ajaxsync(ctx + "/console/sub_account/list" ,{'pageNo':nowPage,'pageSize':listRows,'appId':appId,"certId":  certId},function(response){
            if(response.success){
                data = response.data.result;
            }else{
                showtoast(response.errorMsg?response.errorMsg:'数据异常');
            }
        },"get");
        for(var i =0 ; i<data.length; i++){
            html +='<tr class="playtr" id="play-'+data[i].id+'">' +
                '<td class="text-center">'+ nulltostr(data[i].certId) +'</td>' +
                '<td class="text-center">'+ nulltostr(data[i].secretKey) +'</td>';
            if(appServiceType=='msg'){
                html += '<td class="text-center">' + nulltostr(data[i].ussdNum) + '</td>' ;
                html += '<td class="text-center">' + nulltostr(data[i].smsNum) + '</td>' ;
            }else if(appServiceType=='call_center'){
                html += '<td class="text-center">' + nulltostr(data[i].voiceNum) + '</td>' ;
//                html += '<td class="text-center">' + data[i].seatNum + '</td>' ;
            }else if(appServiceType=='voice'){
                html += '<td class="text-center">' + nulltostr(data[i].voiceNum) + '</td>' ;
            }
            var state = data[i].enabled == 1?"启用":"禁用";
            var color = data[i].enabled == 1?"text-success":"text-danger";
            var stateEdit = data[i].enabled == 1?"禁用":"启用";
            var remark1 = data[i].remark !=null && data[i].remark.length>18 ? data[i].remark.substring(0,18)+"...": data[i].remark;
            html+= '<td class="text-center '+color+'" id="enable_'+data[i].id+'" >' + state+ '</td>' +
                '<td class="text-center">' + remark1 + '</td>' +
                '<td class="text-center"> <a href="javascript:toSubAccountEnable(\''+data[i].id+'\')" data-state="'+data[i].enabled+'" id="enable_edit_'+data[i].id+'" >'+stateEdit+'</a>&nbsp;<a href="javascript:tosubAccountDatail(\''+data[i].id+'\')" >详情</a>&nbsp;<a href="javascript:delSubAccount(\''+data[i].id+'\')" >删除</a></td>' +
                '</tr>'
        }

        $('#subAccount-table').find(".playtr").remove();
        $('#subAccount-table').append(html);
    }
    function toSubAccountEnable(id,enabled){
        var enabled = $('#enable_edit_'+id).attr("data-state");
        var state ;
        var h;
        var h2;
        var enabled2;
        var color ="text-center "
        if(enabled==1){//当前是启用，执行禁用
            state="disable/"+id;
            h="禁用";
            h2="启用";
            enabled2=0;
            color +="text-danger";
        }else{//当前是禁用，执行启用
            state="enabled/"+id;
            h="启用";
            h2="禁用";
            enabled2=1;
            color +="text-success";
        }
        ajaxsync(ctx + "/console/sub_account/"+state,{csrfParameterName:csrfToken},function(response){
            if(response.success){
                showtoast(h+"成功");
                $('#enable_'+id).html(h);
                $('#enable_edit_'+id).html(h2);
                $('#enable_edit_'+id).attr("data-state",enabled2);
                $("#enable_"+id).attr("class",color);
            }else{
                showtoast(h+"失败："+response.errorMsg);
            }
        },"post");
    }

    function totemplateDetail(id){
        ajaxsync(ctx + "/console/msg_template/get/"+id ,{},function(response){
            if(response.success){
                editSeven.initObj(response.data);
                $('#template_home').hide();
                $('#template_detail').show();
            }else{
                showtoast(response.errorMsg?response.errorMsg:'数据异常');
            }
        },"get");
    }
    function tosubAccountDatail(id){
        ajaxsync(ctx + "/console/sub_account/get/"+id ,{},function(response){
            if(response.success){
                editSubAccountFive.initObj(response.data);
                $('#subAccount_home').hide();
                $('#subAccount_datail').show();
                if(appServiceType == 'msg'|| '${app.status}'!=1){
                }else{
                    $('#call-number2').attr("data-num-bind",response.data.id);
                    upnumber2();
                }
            }else{
                showtoast(response.errorMsg?response.errorMsg:'数据异常');
            }
        },"get");
    }
    function totemplateHome(){
        $('#template_detail').hide();
        $('#template_home').show();
        editSeven.init();
        templateList();
    }
    function tosubAccountHome(){
        $('#subAccount_datail').hide();
        $('#subAccount_home').show();
        editSubAccountFive.initObj2();
    }
    function tosubAccountHome1(){
        $('#subAccount_datail').hide();
        $('#subAccount_home').show();
        editSubAccountFive.initObj2();
        subAccountList();
    }
    /**
     * 坐席分机分页回调方法
     * @param nowPage 当前页数
     * @param listRows 每页显示多少条数据
     * */
    var agentTable = function(nowPage,listRows){
        var html = '';
        var data = [];
        var agentNum = $('#agent_num').val();
        var subId = $('#agent_subId') .val();//$('#agent_subId option:selected') .val();
        var params = {"pageNo":nowPage,"pageSize":listRows,"agentNum":agentNum,"subId":subId};
        ajaxsync(ctx + "/console/app/" + appId + "/agent/page" ,params,function(response){
            if(response.success){
                data = response.data.result;
            }else{
                showtoast(response.errorMsg?response.errorMsg:'数据异常');
            }
        },"get");

        // $('#playtable').find(".playtr").remove();["✔", "✘"],
        for(var i =0 ; i<data.length; i++){
            var skillStr = '';
            var skills = data[i].skills;
            if(skills != null){
                for(var j =0 ; j<skills.length; j++){
                    skillStr += skills[j].name;
                    if(j != (skills.length-1)){
                        skillStr += ',';
                    }
                }
            }

            html +='<tr id="agent-'+ data[i].name +'">' +
                    '<td class="text-center">'+ nulltostr(data[i].name) +'</td>' +
                    '<td class="text-center">'+ nulltostr(skillStr) +'</td>' +
                    '<td class="text-center">'+ nulltostr(data[i].extension) +'</td>' +
                     '<td class="text-center">'+ nulltostr(data[i].certId) +'</td>' +
                    '<td class="text-center">' + nulltostr(data[i].state) + '</td>' +
                    '<td class="text-center"><a href="javascript:delAgent(\''+data[i].id+'\')" >删除</a></td>' +
                    '</tr>'
        }
        $('#agent-list').html(html);
    }
    /**
     * 坐席分机分页回调方法
     * @param nowPage 当前页数
     * @param listRows 每页显示多少条数据
     * */
    var queueTable = function(nowPage,listRows){
        var html = '';
        var data = [];
        var queueNum = $('#queue_num').val();
        var subId = $('#queue_subId') .val();//$('#queue_subId option:selected') .val();
        var params = {"pageNo":nowPage,"pageSize":listRows,"queueNum":queueNum,"subId":subId};
        ajaxsync(ctx + "/console/app/" + appId + "/queue/page" ,params,function(response){
            if(response.success){
                data = response.data.result;
            }else{
                showtoast(response.errorMsg?response.errorMsg:'数据异常');
            }
        },"get");

        // $('#playtable').find(".playtr").remove();["✔", "✘"],
        for(var i =0 ; i<data.length; i++){
            html +='<tr id="queue-'+ data[i].id +'">' +
                '<td class="text-center"><span style="float:left;" >'+ nulltostr(data[i].whereExpression) +'</span></td>' +
                '<td class="text-center"><span style="float:left;" >'+ nulltostr(data[i].sortExpression) +'</span></td>' +
                '<td class="text-center">' + nulltostr(data[i].priority) + '</td>' +
                '<td class="text-center">' + nulltostr(data[i].queueTimeout) + '</td>' +
                '<td class="text-center">' + nulltostr(data[i].fetchTimeout) + '</td>' +
                '<td class="text-center">'+ nulltostr(data[i].certId) +'</td>' +
                '<td class="text-center">' + nulltostr(data[i].remark) + '</td>' +
                '</tr>'
        }
        $('#queue-list').html(html);
    }
//    $(document).ready(function () {
//        $('.sublist').selectpicker('refresh');
//
//    });
    function getSubAccountByAppId(){
        if(appId == null || appId.length==0){
            showtoast("应用标识异常，获取子账号列表失败");
        }else{
            ajaxsync(ctx + "/console/sub_account/by/"+appId+"/list" ,{},function(response) {
                if(response.success){
                    var list =  response.data;
                    $("select.sublist[data-live-search=true] option").remove();
                    var opt = '<option  class="get-class" value=""></option>';
                    $("select.sublist[data-live-search=true]").append(opt);
                    for(var i=0;i< list.length ;i++){
                        var opt = '<option value="'+list[i].id+'" >'+list[i].certId+'</option>';
                        $("select.sublist[data-live-search=true]").append(opt);
                    }
                    $("select.sublist[data-live-search=true]").selectpicker('refresh');
//                    $("div.sublist option").remove();
                    $("div.open").css("position","absolute");
                    $("div.sublist button[data-id=file_sub_accout_num]").css("width","270px");
                    $('div.sublist').each(function (index) {
                        var len = $(this).children("button[data-id=file_sub_accout_num]").length;
                        if(len >0 ){
                            $(this).children("div.open").children("div.bs-searchbox").css("height","35px");
                            $(this).children("div.open").children("div.bs-searchbox").children("input").css("height","30px");
                        }
                        var len2 = $(this).children("button[data-id=create_six_subId]").length;
                        if(len2 >0){
                            $(this).css("width","293px");
                            $(this).children("div.open").children("div.bs-searchbox").css("height","35px");
                            $(this).children("div.open").children("div.bs-searchbox").children("input").css("height","30px");
                        }
                    });
                }else{
                    showtoast(response.errorMsg?response.errorMsg:'数据异常，请稍后重试！');
                }
            });
        }
    }
    function nulltostr(val){
        return val==null||val==undefined ? '':val;
    }
</script>

</body>
</html>

