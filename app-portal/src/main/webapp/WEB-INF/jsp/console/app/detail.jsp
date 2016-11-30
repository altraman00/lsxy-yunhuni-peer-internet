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
                                                            ${app.name}
                                                            <c:if test="${app.status==1}"><span style="color:#9dc940;">已上线</span></c:if>
                                                            <c:if test="${app.status==2}"><span class="text-danger">未上线</span></c:if>
                                                            <%--<input type="hidden" id="appStatus" value="${app.status}">--%>
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
                                                            <p><c:if test="${app.serviceType=='call_center'}">呼叫中心</c:if>
                                                                <c:if test="${app.serviceType=='voice'}">语音</c:if></p>
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
                                                    <div class="row ">
                                                        <div class="col-md-1 dev">
                                                            监听通知：
                                                        </div>
                                                        <div class="col-md-6">
                                                            <p><c:if test="${app.isAuth=='1'}">启用监听    启用了鉴权</c:if>
                                                                <c:if test="${app.isAuth!='1'}">没有启动监听</c:if></p>
                                                        </div>
                                                    </div>
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
                                                        <div class="col-md-8"> <span id="testNumBind"
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
                                                            <p class="text-danger">（上线后平台会重新分配分机接入信息）</p>
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
                                    <li class="active" data-id="play">
                                        <a href="#play" data-toggle="tab">
                                            放音媒体库
                                        </a>
                                    </li>
                                    <li data-id="voice"><a href="#voice" data-toggle="tab">录音文件</a></li>
                                    <li class="right" id="uploadButton" hidden><a href="#" id="uploadButtonA" class="btn btn-primary defind modalShow" data-id="four" >上传放音文件</a></li>
                                </ul>
                                <div id="myTabContent" class="tab-content" style="">
                                    <div class="tab-pane fade in active" id="play">
                                        <p class="application_info">
                                            当您的应用需要开通语音通知、自定义IVR或者云呼叫中心时，请上传语音文件至放音媒体库，语音文件均需要审核
                                        </p>
                                        <div class="form-group">
                                            <div class="col-md-3 remove-padding"><input type="text" class="form-control" placeholder="文件名" id="name"/></div>
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
                                                <th width="35%">备注</th>
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
                                    <div class="tab-pane fade" id="voice">
                                        <p class="application_info">
                                            1.每个账号默认允许免费存储7天内的录音文件，超过7天的录音文件系统自动删除。<br/>
                                            2.会员可以自行配置存储周期，修改存储周期后，文件存储按1元/G/月收费，每个月底进行扣费。<br/>
                                            3.录音文件可在详单查询内下载。
                                        </p>
                                        <div class="row margin-bottom-20">
                                            <div class="col-md-2 font14 max-width-120">
                                                配置存储周期
                                            </div>
                                            <div class="col-md-10 font14">
                                                <input type="radio" name="cycle" value="0" checked/>7天免费存储
                                                &nbsp;<input type="radio" name="cycle" value="1" />1个月
                                                &nbsp;<input type="radio" name="cycle" value="2" />2个月
                                                &nbsp;<input type="radio" name="cycle" value="3" />3个月
                                            </div>
                                        </div>
                                        <div class="row margin-bottom-20">
                                            <div class="col-md-2 font14 max-width-120"></div>
                                            <div class=" col-md-10">
                                                <a href="" class="btn btn-primary">保存</a>
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

<!---上传文件--->
<div class="modal-box application-detail-box application-file-box" id="modalfour" style="display:none ">
    <div class="modal-loadding loadding"></div>
    <div class="title">文件上传<a class="close_a modalCancel-app-up" data-id="four" ></a></div>
    <div class="content">
        <p class="info">只支持 .wav 格式的文件，请将其他格式转换成wav格式（编码为 8k、16位）后再上传,单条语音最大支持 5M。</p>
        <form:form action="${ctx}/console/app/file/play/upload" method="post" id="uploadMianForm" enctype="multipart/form-data" target="hidden_frame">
            <div class="input-box ">
                <div class="row  mt-10">
                    <input type="hidden" name="appId" value="${app.id}">
                    <div class="col-md-2">
                        文件 :
                    </div>
                    <div class="col-md-10">
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
<script src="${resPrefixUrl }/bower_components/blueimp-file-upload/js/vendor/jquery.ui.widget.js"></script>
<script src="${resPrefixUrl }/bower_components/blueimp-file-upload/js/jquery.fileupload.js"></script>
<script src="${resPrefixUrl }/bower_components/blueimp-file-upload/js/jquery.fileupload-ui.js"></script>
<script src="${resPrefixUrl }/bower_components/blueimp-file-upload/js/jquery.fileupload-process.js"></script>
<script src="${resPrefixUrl }/bower_components/blueimp-file-upload/js/jquery.iframe-transport.js"></script>
<script src="${resPrefixUrl }/bower_components/blueimp-file-upload/js/jquery.fileupload-validate.js"></script>


        <script>
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
//                var appStatus = $("#appStatus").val();
//                if(appStatus == 2){
//                    $('#uploadButtonA').unbind("click").bind("click",function(){
//                        showtoast("请先上线应用");
//                    })
//                }
            }
            // 上传多个文件
            var cancelCancel=false;
            $(function(){
                var Allfile = [],allFileLength = 0, errorFileArray = [];
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
                        var  re = /^.+(\.wav)$/i;
                        var result=  re.test(filename);
                        if(result){
                            if(data.files[0].size <= (5* 1024 * 1024)) {
//                                ajaxsync(ctx + "/console/app/file/play/total",{csrfParameterName:csrfToken},function(response){
//                                    if((response.data.fileTotalSize-response.data.fileRemainSize)>=data.files[0].size){
                                        ajaxsync(ctx + "/console/app/file/play/verify/name",{csrfParameterName:csrfToken,'appId':appId,'name':filename},function(response1){
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
//                        fileTotalSoze();
                        $('#play-'+id).remove();
                        if(pagePlay){
                            pagePlay.count--;
                            pagePlay.initTotalPage();
                            if(pagePlay.nowPage>pagePlay.totalPage){
                                pagePlay.nowPage=pagePlay.totalPage;
                            }
                            if(pagePlay.nowPage<=0){
                                pagePlay.nowPage=1;
                            }
                            $('#page'+pagePlay.nowPage+pagePlay.obj).click();
                        }
                    }else{
                        showtoast("删除失败");
                    }
                },"post");

            }
        });
    }




    //默认加载放音文件分页
    $(function () {
        upplay();
    });
    /**
     *触发放音文件分页
     */
    var pagePlay ;
    function upplay(){
        $('#uploadButton').show();
        //获取数据总数
        var count = 0;
        var name = $('#name').val();

        ajaxsync(ctx + "/console/app/file/play/list",{'name':name,'appId':appId,'pageNo':1,'pageSize':20,csrfParameterName:csrfToken},function(response){
            count=response.data.totalCount;
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
        ajaxsync(ctx + "/console/app/file/play/list",{'name':name,'appId':appId,'pageNo':nowPage,'pageSize':listRows,csrfParameterName:csrfToken},function(response){
            var data =[];
            for(var j=0;j<response.data.result.length;j++){
                var tempFile = response.data.result[j];
                var temp = [tempFile.id,tempFile.name,tempFile.status,resultFileSize(tempFile.size),tempFile.remark,tempFile.reason?tempFile.reason:'',tempFile.sync];
                data[j]=temp;
            }
            var html ='';
            //数据列表
            for(var i = 0 ; i<data.length; i++){
                html +='<tr class="playtr" id="play-'+data[i][5]+'"><td class="voice-format">'+data[i][1]+'</td>';
                if(data[i][2]==-1){
                    html+='<td  title="审核不通过原因：'+data[i][5]+'"><span class="nosuccess">审核不通过</span><i class="fa fa-exclamation-triangle"></i></td>';
                }else if(data[i][2]==1&&data[i][6]==1){
                    html+='<td ><span class="success">已审核</span></td>';
                }else{
                    html+='<td>待审核</td>';
                }
                html+='<td>'+data[i][3]+'</td>';
                html+='<td id="remark-a-'+data[i][0]+'">'+data[i][4]+'</td>';
                html+='<td class="operation"> <a onclick="delplay(this)" id="delete-'+data[i][0]+'" >删除</a> <span ></span> <a onclick="editremark(this)" id="remark-b-'+data[i][0]+'">修改备注</a> </td></tr>';
            }
            $('#playtable').find(".playtr").remove();
            $('#playtable').append(html);
        },"post");

    }



    $('#myTab li').click(function(){
        var type = $(this).attr('data-id');
        if(type=='voice'){
            upvoice();
        }
        if(type=='play'){
            upplay();
        }
    });

//    fileTotalSoze();
</script>

</body>
</html>

