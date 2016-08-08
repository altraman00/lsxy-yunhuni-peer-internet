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
                        <div class="head-box"><a href="#subNav" data-toggle="class:hide"> <i
                                class="fa fa-angle-left text"></i> <i class="fa fa-angle-right text-active"></i> </a>
                        </div>
                        <div class="wrapper header">
                            <span class="border-left">&nbsp;应用详情</span>
                        </div>
                        <section class="scrollable wrapper w-f">
                            <section class="panel panel-default yunhuni-personal">
                                <div class="row m-l-none m-r-none bg-light lter">
                                    <div class="col-md-12 padder-v fix-padding">
                                        <a href="${ctx}/console/app/index?id=${app.id}" class="btn btn-primary query">应用编辑</a>
                                    </div>
                                </div>
                            </section>
                            <section class="panel panel-default pos-rlt clearfix appliaction-detail">
                                <div class="row ">
                                    <div class="col-md-1 remove-padding width-">
                                        应用名称：
                                    </div>
                                    <div class="col-md-10 ">
                                        <p>${app.name}</p>
                                    </div>
                                </div>
                                <div class="row ">
                                    <div class="col-md-1 remove-padding width-130">
                                        应用描述：
                                    </div>
                                    <div class="col-md-10 ">
                                        <p>${app.description}</p>
                                    </div>
                                </div>
                                <div class="row ">
                                    <div class="col-md-1 remove-padding width-130">
                                        应用类型：
                                    </div>
                                    <div class="col-md-10 ">
                                        <p>${app.type}</p>
                                    </div>
                                </div>
                                <div class="row ">
                                    <div class="col-md-1 remove-padding width-130">
                                        所属行业：
                                    </div>
                                    <div class="col-md-10 ">
                                        <p>${app.industry}</p>
                                    </div>
                                </div>
                                <div class="row ">
                                    <div class="col-md-1 remove-padding width-130">服务器白名单：</div>
                                    <div class="col-md-10 ">
                                        <p>${app.whiteList}</p>
                                    </div>
                                </div>
                                <div class="row ">
                                    <div class="col-md-1 remove-padding width-130">
                                        APPID：
                                    </div>
                                    <div class="col-md-10 ">
                                        <p>${app.id}</p>
                                    </div>
                                </div>

                                <div class="row ">
                                    <div class="col-md-1 remove-padding width-130">
                                        监听通知：
                                    </div>
                                    <div class="col-md-10 ">
                                        <p>
                                            <c:if test="${app.isAuth=='1'}">启用监听    启用了鉴权</c:if>
                                            <c:if test="${app.isAuth!='1'}">没有启动监听</c:if>
                                        </p>
                                    </div>
                                </div>
                                <div class="row ">
                                    <div class="col-md-1 remove-padding width-130">
                                        回调URL：
                                    </div>
                                    <div class="col-md-10 ">
                                        <p><a href="#" target="_top" >${app.url}</a></p>
                                    </div>
                                </div>
                                <div class="row ">
                                    <div class="col-md-1 remove-padding width-130">
                                        绑定测试号：
                                    </div>
                                    <div class="col-md-3 " id="testNumBind">
                                        <c:forEach items="${testNumBindList}" var="testNumBind">
                                            <c:if test="${testNumBind.app.id==app.id}">
                                                <span name="testNum">${testNumBind.number} </span>
                                            </c:if>
                                        </c:forEach>
                                    </div>
                                    <div class="col-md-4 " >
                                        <a class="modalShow" data-id="one">绑定交互测试号</a>
                                    </div>
                                </div>
                                <div class="row ">
                                    <div class="col-md-1 remove-padding width-130">
                                        应用状态：
                                    </div>
                                    <div class="col-md-10 ">
                                        <p>
                                            <c:if test="${app.status==1}"><span class="success">已上线</span></c:if>
                                            <c:if test="${app.status==2}"><span class="nosuccess">未上线</span></c:if>
                                        </p>
                                    </div>
                                </div>


                                <div class="row border-block"></div>
                            </section>
                            <section class="panel panel-default pos-rlt clearfix application-tab">
                                <ul id="myTab" class="nav nav-tabs">
                                    <li class="active" data-id="play">
                                        <a href="#play" data-toggle="tab">
                                            放音媒体库
                                        </a>
                                    </li>
                                    <li data-id="voice"><a href="#voice" data-toggle="tab">录音文件</a></li>
                                    <li class="right"><a href="#" class="btn btn-primary defind modalShow" data-id="four" >上传放音文件</a></li>
                                </ul>
                                <div id="myTabContent" class="tab-content" style="">
                                    <div class="tab-pane fade in active" id="play">
                                        <p class="application_info">
                                            1、当您的应用需使用IVR服务时，请上传语音文件至放音媒体库，语音文件均需要审核<br/>
                                            2、每个帐号默认拥有200M的存储空间，多个应用共享，若有特殊需要增加容量请联系客户经理
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
                                                <th width="20%">标题</th>
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
                                            1、默认录音文件免费存储7天，超过7天平台自动删除该文件，如有需要请提前下载保留<br/>
                                            2、可配置录音文件存储周期，超过7天按相应的存储资费计费，10元/天。
                                        </p>
                                        <div class="form-group">
                                            <div class="col-md-3 remove-padding line32 font14" id="voiceFileRecord">
                                            </div>
                                            <div class="col-md-9 text-right">
                                                <a class="btn modalShow right" data-id="three">批量删除</a> <a class="btn modalShow right" data-id="two" >批量下载</a>
                                            </div>
                                        </div>

                                        <table class="table table-striped cost-table-history tablelist" id="voicetable">
                                            <thead>
                                            <tr>
                                                <th>标题</th>
                                                <th>大小</th>
                                                <th>时长</th>
                                                <th>操作</th>
                                            </tr>
                                            </thead>
                                            <tbody>

                                            </tbody>
                                        </table>
                                        <section class="panel panel-default yunhuni-personal">
                                            <div id="voicepage"></div>
                                        </section>
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
    <div class="title">文件上传<a class="close_a modalCancel-app-up" data-id="four" id="uploadCancelX"></a></div>
    <div class="content">
        <p class="info">只支持 .wav 格式的文件，请将其他格式转换成wav格式（编码为 8k、16位）后再上传；单条语音最大支持 5M；文件名称只允许含英文、数字，其他字符将会造成上传失败。  </p>
        <form:form action="${ctx}/console/app/file/play/upload" method="post" id="uploadMianForm" enctype="multipart/form-data" target="hidden_frame">
            <div class="input-box ">
                <div class="row  mt-10">
                    <input type="hidden" name="appId" value="${app.id}">
                    <div class="col-md-2">
                        文件 :
                    </div>
                    <div class="col-md-10">
                        <input type="file" value="" class="input-text form-control" name="file" id="singlefile" multiple="multiple" />
                        <div class="progress h10 mt-10">
                            <div class="progress-bar progress-bar-info progress-bar-striped" role="progressbar"  aria-valuemin="0" aria-valuemax="100" style="width: 0%" id="uploadLength">
                            </div>
                        </div>

                    </div>
                </div>
                <div class="row text-left mt-10">
                    <p>允许一次选择20个文件，并且建议在网络环境好的情况下使用，以防止上传错误文件</p>
                </div>
            </div>
            <iframe name='hidden_frame' id="hidden_frame" style='display:none'></iframe>
            <input type="reset" id="resetForm" hidden/>
        </form:form>
    </div>
    <div class="footer">
        <a class="cancel modalCancel-app-up " data-id="four" id="uploadCancel">返回</a>
        <a class="sure modalSureFour" data-id="four" id="uploadOk">确认</a>
    </div>
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

<script>
    $('.modalCancel-app-up').click(function(){
        clearTimeout(timer);
        $('#resetForm').click();
        $('#uploadLength').attr("style","width:"+0+"%");
        var id = $(this).attr('data-id');
        $('#modal'+id).fadeOut();
        $('#show-bg').fadeOut();
        if(uploadStatus==2){
            uploadStatus==0;
            showtoast("上传文件进入后台处理中...",window.location.href=ctx+"/console/app/detail?id="+appId);
        }else{
            showtoast("取消成功");
        }
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


    /**
     * 查询文件
     */
    $('.findfile').click(function(){
        var id = $(this).attr('data-id');
        $('#scrolldiv'+id).html('');
        $('.moadltips'+id).html('');

        //获取时间
        var starttime = $('#datestart'+id).val();
        var endtime = $('#dateend'+id).val();
        var tips = compareTime(starttime,endtime);
        if(tips){
            $('.moadltips'+id).html(tips); return false;
        }
        endtime+=" 23:59:59";
        var html  = "";
        //异步查询文件信息

        ajaxsync(ctx + "/console/app/file/record/sum",{'appId':appId,'startTime':starttime,'endTime':endtime,csrfParameterName:csrfToken},function(response){
            if(response.success){
                //添加加载文件信息
                html  = '  <p>--共计  '+  response.data.total+'  个文件   '+resultFileSize(response.data.size )+'</p>';
                html+='<p>--统计完成</p>';
                $('#scrolldiv'+id).append(html);
            }else{
                showtoast(response.errorMsg);
            }
        },"post");

        //two 表示的是批量下载
        if(id=='two'){
            html +='<p>--开始压缩打包</p>';
            $('#scrolldiv'+id).append(html);

            ajaxsync(ctx + "/console/app/file/record/zip",{'appId':appId,'startTime':starttime,'endTime':endtime,csrfParameterName:csrfToken},function(response){
                if(response.success){
                    var fileName = new String(response.data);
                    var index = fileName.lastIndexOf("/");
                    var name = fileName.substring(index,fileName.length);
                    //添加加载文件信息
                    html  = '<p>--压缩打包完成 '+name+'</p>';
                    html+='<p>--压缩包生成完成：<a href="#" target="_blank">点击下载</a></p>';
                    $('#scrolldiv'+id).append(html);
                }else{
                    html  = '<p>--压缩打包失败</p>';
                    html+='<p>--请稍后重试</p>';
                    $('#scrolldiv'+id).append(html);
                }
            },"post");

        }
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
     * 批量删除处理
     */
    $('.modalSureThree').click(function(){
        var id = $(this).attr('data-id');
        var starttime = $('#datestart'+id).val();
        var endtime = $('#dateend'+id).val();
        endtime+=" 23:59:59";
        bootbox.confirm("确认删除所选文件", function(result) {
            if(result){
                ajaxsync(ctx + "/console/app/file/record/batch_delete",{'id':id,'appId':appId,'startTime':starttime,'endTime':endtime,csrfParameterName:csrfToken},function(response){
                    if(response.success){
                        showtoast("批量删除成功");
                    }else{
                        showtoast("批量删除失败");
                    }
                },"post");

                upvoice();
                $('#voice-'+id).remove();
                hideModal(id);
            }else{
                hideModal(id);
            }
            batchclear(id);
        });
    });

    /**
     * 文件上传地址
     */
    var uploadStatus=0;
    $('.modalSureFour').click(function(){
        if(uploadStatus!=0){return false};
        var id = $(this).attr('data-id');
        //验证文件格式
        var file = $('#singlefile');
        var flag = true;
        if(file[0].files.length==0){
            showtoast("请上传文件");
            flag = false;
        }
        var allowtype =  ["WAV"];
        for(var i=0 ; i<file[0].files.length ;i++ ){
            var names =  getFiletype(file[0].files[i].name);
            if ($.inArray(names,allowtype) == -1)
            {
                showtoast("上传失败，只支持 .wav 格式的文件");
                flag = false;
                return flag;
            }
            if(file[0].files[i].size> 5* 1024 * 1024){
                showtoast("上传失败，单条语音最大支持 5M");
                flag = false;
                return flag;
            }
        }
        if(flag){
            $('#uploadLength').show();
            $('#uploadMianForm').submit();
            startUpload();
            uploadStatus=1;
        }
    });

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
    function delvoice(idType){
        bootbox.setLocale("zh_CN");
        bootbox.confirm("确认删除文件", function(result) {
            if(result){
                var id = new String(idType.id).replace("voice-record-","");

                ajaxsync(ctx + "/console/app/file/record/delete",{'id':id,csrfParameterName:csrfToken},function(response){
                    if(response.success){
                        showtoast("删除成功");
                        recordFileTotalSize();
                        $('#voice-'+id).remove();
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
    function delplay(idType){
        bootbox.setLocale("zh_CN");
        bootbox.confirm("确认删除文件", function(result) {
            if(result){
                var id = new String(idType.id).replace("delete-","");
                ajaxsync(ctx + "/console/app/file/play/delete",{'id':id,csrfParameterName:csrfToken},function(response){
                    if(response.success){
                        showtoast("删除成功");
                        fileTotalSoze();
                        $('#play-'+id).remove();
                    }else{
                        showtoast("删除失败");
                    }
                },"post");

            }
        });
    }
    /**
     * 查询录音文件占存储空间的大小
     */
    var recordFileTotalSize = function(){

        ajaxsync(ctx + "/console/app/file/record/sum",{'appId':appId,csrfParameterName:csrfToken},function(response){
            if(response.success){
                $('#voiceFileRecord').html("录音文件总计占用："+resultFileSize(response.data.size));
            }else{
                showtoast(response.errorMsg);
            }
        },"post");


    };



    //默认加载放音文件分页
    $(function () {
        upplay();
    });
    /**
     *触发放音文件分页
     */
    function upplay(){
        //获取数据总数
        var count = 0;
        var name = $('#name').val();

        ajaxsync(ctx + "/console/app/file/play/list",{'name':name,'appId':appId,'pageNo':1,'pageSize':20,csrfParameterName:csrfToken},function(response){
            count=response.data.totalCount;
        },"post");

        //每页显示数量
        var listRow = 3;
        //显示多少个分页按钮
        var showPageCount = 5;
        //指定id，创建分页标签
        var pageId = 'playpage';
        //searchTable 为方法名
        var page = new Page(count,listRow,showPageCount,pageId,playTable);
        page.show();
    }
    var fileTotalSoze = function(){

        ajaxsync(ctx + "/console/app/file/play/total",{csrfParameterName:csrfToken},function(response){
            $('#voiceFilePlay').html("共计" + resultFileSize(response.data.fileTotalSize) + ",已占用" + resultFileSize(response.data.fileRemainSize)+ "");
        },"post");

    };
    var resultFileSize = function(temp){
        if(temp>(1024*1024)){
            temp = (temp/1024/1024).toFixed(2)+"M";
        }else if(temp>1024){
            temp = (temp/1024).toFixed(2)+"K";
        }else{
            temp = temp+"b";
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
                var temp = [tempFile.id,tempFile.name,tempFile.status,resultFileSize(tempFile.size),tempFile.remark];
                data[j]=temp;
            }
            var html ='';
            //数据列表
            for(var i = 0 ; i<data.length; i++){
                html +='<tr class="playtr" id="play-'+data[i][0]+'"><td class="voice-format">'+data[i][1]+'</td>';
                if(data[i][2]==-1){
                    html+='<td class="nosuccess">审核不通过</td>';
                }else if(data[i][2]==1){
                    html+='<td class="success">已审核</td>';
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

    /**
     *触发录音文件分页
     */
    function upvoice(){
        recordFileTotalSize();
        //获取数据总数
        var count = 0;

        ajaxsync(ctx + "/console/app/file/record/list",{ 'appId':appId,'pageNo':1,'pageSize':20,csrfParameterName:csrfToken},function(response){
            count=response.data.totalCount;
        },"post");

        //每页显示数量
        var listRow = 3;
        //显示多少个分页按钮
        var showPageCount = 5;
        //指定id，创建分页标签
        var pageId = 'voicepage';
        //searchTable 为方法名
        var page = new Page(count,listRow,showPageCount,pageId,voiceTable);
        page.show();
    }
    /**
     * 分页回调方法
     * @param nowPage 当前页数
     * @param listRows 每页显示多少条数据
     * */
    var voiceTable = function(nowPage,listRows)
    {
        ajaxsync(ctx + "/console/app/file/record/list",{'appId':appId,'pageNo':nowPage,'pageSize':listRows,csrfParameterName:csrfToken},function(response){
            var data =[];
            for(var j=0;j< response.data.result.length;j++){
                var tempFile = response.data.result[j];
                var temp = [tempFile.id,tempFile.name,resultFileSize(tempFile.size),tempFile.duration];
                data[j]=temp;
            }
            var html ='';
            for(var i = 0 ; i<data.length; i++){
                html +='<tr class="voicetr" id="voice-'+data[i][0]+'"><td class="voice-format">'+data[i][1]+'</td>';
                html+='<td>'+data[i][2]+'</td>';
                html+='<td>'+data[i][3]+'</td>';
                html+='<td class="operation"><a >下载</a> <span ></span><a onclick="delvoice(this)" id="voice-record-'+data[i][0]+'" >删除</a></td></tr>';
            }
            $('#voicetable').find(".voicetr").remove();
            $('#voicetable').append(html);
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
    var timer = "";
    function startUpload(){
        timer = window.setTimeout(startListener,1000);
        return true;
    }
    function startListener(){
        ajaxsync(ctx + "/console/app/file/play/status",{csrfParameterName:csrfToken},function(response){
            $('#uploadLength').attr("style","width:"+response.data.percentComplete+"%");
            if(response.data.percentComplete==100&&response.data.flag==false){
                $('.modal-loadding').show();
                uploadStatus=2;
            }
            if(response.data.flag){
                $('.modal-loadding').hide();
                clearTimeout(timer);
                showtoast('上传成功');
                fileTotalSoze();
                upplay();
                var id = $('.modalSureFour').attr('data-id');
                hideModal(id);
                //清除内容
                $('#resetForm').click();
                $('#uploadLength').attr("style","width:"+0+"%");
                uploadStatus=0;
            }else{
                startUpload();
            }
        },"post");

    }
    fileTotalSoze();
</script>

</body>
</html>

