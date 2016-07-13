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
                            <section class="panel panel-default pos-rlt clearfix ">
                                <div class="row ">
                                    <div class="col-md-1 remove-padding width-130">
                                        应用名称：
                                    </div>
                                    <div class="col-md-10 ">
                                        <p>${app.name}</p>
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
                                    <div class="col-md-3 " >
                                        <span href="">13971068693</span>
                                        <span href="">13971068693</span>
                                        <span href="">13971068693</span>
                                        <span href="">13971068693</span>
                                        <span href="">13971068693</span>
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
                                            <c:if test="${result.status==1}"><span class="success">已上线</span></c:if>
                                            <c:if test="${result.status==2}"><span class="nosuccess">未上线</span></c:if>
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
                                            <div class="col-md-3 remove-padding"><input type="text" class="form-control" placeholder="文件名" /></div>
                                            <div class="col-md-1"><button class="btn btn-primary">查询</button></div>
                                            <div class="col-md-3 sizebox ">
                                                共计500M，已占用100M
                                            </div>
                                        </div>

                                        <table class="table table-striped cost-table-history tablelist" id="playtable">
                                            <thead>
                                            <tr>
                                                <th width="20%">标题</th>
                                                <th width="10%">状态</th>
                                                <th width="10%">大小</th>
                                                <th width="45%">备注</th>
                                                <th width="10%">操作</th>
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
                                            <div class="col-md-3 remove-padding line32 font14">
                                                录音文件总计占用：100M
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
            <form action="" method="">
                <div class="row mt-20">
                    <div class="col-md-4">  <input type="checkbox" class="check-box" />13971068693</div>
                    <div class="col-md-4">  <input type="checkbox" class="check-box" />13971068693</div>
                    <div class="col-md-4">  <input type="checkbox" class="check-box" />13971068693</div>
                </div>
                <div class="row mt-20">
                    <div class="col-md-4">  <input type="checkbox" class="check-box" />13971068693</div>
                    <div class="col-md-4">  <input type="checkbox" class="check-box" />13971068693</div>
                    <div class="col-md-4">  <input type="checkbox" class="check-box" />13971068693</div>
                </div>
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
                <p>--扫描文件</p>
                <p>--共计  200  个文件   200M</p>
                <p>--开始压缩打包</p>
                <p>--压缩打包完成 20160601-20160603-001.zip</p>
                <p>--压缩包生成完成：<a href="#" target="_blank">点击下载</a></p>
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
    <div class="title">批量删除<a class="close_a modalCancel" data-id="three"></a></div>
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
        <a class="cancel modalCancel" data-id="three">返回</a>
        <a class="sure modalSureThree" data-id="three">确认</a>
    </div>
</div>

<!---上传文件--->
<div class="modal-box application-detail-box" id="modalfour" style="display:none ">
    <div class="title">文件上传<a class="close_a modalCancel" data-id="four"></a></div>
    <div class="content">
        <p class="info">只支持 .wav 格式的文件，请将其他格式转换成wav格式（编码为 8k、16位）后再上传；单条语音最大支持 5M；文件名称只允许含英文、数字，其他字符将会造成上传失败。  </p>
        <form action="" method="post" id="">
            <div class="input-box ">
                <div class="row  mt-10">
                    <div class="col-md-6">
                        <button type="button" class="btn btn-primary singlefile" >单文件上传</button>
                    </div>
                    <input type="file" value="" class="input-text form-control"  id="singlefile" style="display: none" />
                </div>
                <div class="row text-left mt-10">
                    <div class="col-md-12">
                        <button type="button" class="btn btn-primary batchfile">批量上传</button>
                    </div>
                    <input type="file" value="" class="input-text form-control" id="batchfile" style="display: none" multiple="multiple" />
                    <p>允许一次选择20个文件，并且建议在网络环境好的情况下使用，以防止上传错误文件</p>
                </div>
            </div>
        </form>
    </div>
    <div class="footer">
        <a class="cancel modalCancel" data-id="four">返回</a>
        <a class="sure modalSureFour" data-id="four">确认</a>
    </div>
</div>


<div class="tips-toast"></div>
<%@include file="/inc/footer.jsp"%>
<script type="text/javascript" src='${resPrefixUrl }/js/bootstrap-datepicker/js/bootstrap-datepicker.js'> </script>
<script type="text/javascript" src='${resPrefixUrl }/js/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js'> </script>
<script type="text/javascript" src='${resPrefixUrl }/js/application/detail.js'> </script>
<!--syncpage-->
<script type="text/javascript" src='${resPrefixUrl }/js/page.js'></script>

<script>
    /**
     *绑定测试电话号码
     */
    $('.modalSureOne').click(function(){
        var id = $(this).attr('data-id');
        hideModal(id);
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
        //异步查询文件信息

        //获取时间
        var starttime = $('#datestart'+id).val();
        var endtime = $('#dateend'+id).val();
        var tips = compareTime(starttime,endtime);
        if(tips){
            $('.moadltips'+id).html(tips); return false;
        }

        //添加加载文件信息
        var html  = '  <p>--共计  200  个文件   200M</p>';
        html+='<p>--统计完成</p>';
        $('#scrolldiv'+id).append(html);
    });

    /**
     * 批量删除处理
     */
    $('.modalSureThree').click(function(){
        var id = $(this).attr('data-id');
        var r=confirm("确认删除所选文件")
        if (r==true)
        {
            //提交表单确认删除
        }
        else
        {
            hideModal(id)
        }
    });

    /**
     * 文件上传地址
     */
    $('.modalSureFour').click(function(){
        var id = $(this).attr('data-id');
        hideModal(id)
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
     *
     *
     * */
    function editremark(id,type){
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
                                //异步请求修改数据

                                //成功执行
                                if(type=='1'){
                                    $('#remark-b-'+id).html('修改备注');
                                }
                                $('#remark-a-'+id).html(remark);
                            }
                        }
                    }
                }
        );
    }




    /**
     * 删除操作
     */
    function delvoice(id){
        bootbox.setLocale("zh_CN");
        bootbox.confirm("确认删除文件", function(result) {
            if(result){
                $('#voice-'+id).remove();
            }
        });
    }


    /**
     * 删除操作
     */
    function delplay(id){
        bootbox.setLocale("zh_CN");
        bootbox.confirm("确认删除文件", function(result) {
            if(result){
                showtoast('删除成功');
                $('#play-'+id).remove();
            }
        });
    }



    /**
     *触发录音文件分页
     */
    function upvoice(){
        //获取数据总数
        var count = 55;
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
     *触发放音文件分页
     */
    function upplay(){
        //获取数据总数
        var count = 55;
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


    /**
     * 分页回调方法
     * @param nowPage 当前页数
     * @param listRows 每页显示多少条数据
     * */
    var playTable = function(nowPage,listRows)
    {

        var data = [
            ['1','1.wav','-1','1M','备注2'],
            ['2','12.wav','0','1M','备注3'],
            ['3','13.wav','1','1M','备注3']
        ];
        //-1 0 1
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
            html+='<td class="operation"> <a onclick="delplay('+data[i][0]+')" >删除</a> <span ></span> <a onclick="editremark('+data[i][0]+')" id="remark-b-'+data[i][0]+'">修改备注</a> </td></tr>';
        }
        $('#playtable').find(".playtr").remove();
        $('#playtable').append(html);
    }


    /**
     * 分页回调方法
     * @param nowPage 当前页数
     * @param listRows 每页显示多少条数据
     * */
    var voiceTable = function(nowPage,listRows)
    {
        var data = [
            ['1','20160606122333-[sessionid]','1M','123'],
            ['2','20160606122333-[sessionid]','1M','12'],
            ['3','20160606122333-[sessionid]','1M','123'],
            ['4','20160606122333-[sessionid]','1M','123']
        ];
        var html ='';
        for(var i = 0 ; i<data.length; i++){
            html +='<tr class="voicetr" id="voice-'+data[i][0]+'"><td class="voice-format">'+data[i][1]+'</td>';
            html+='<td>'+data[i][2]+'</td>';
            html+='<td>'+data[i][3]+'</td>';
            html+='<td class="operation"><a >下载</a> <span ></span><a onclick="delvoice('+data[i][0]+')" >删除</a></td></tr>';
        }
        $('#voicetable').find(".voicetr").remove();
        $('#voicetable').append(html);
    }



    function showtoast(tips) {
        $('.tips-toast').css('display','block').html(tips);
        setTimeout("hidetoast()",2000);
    }
    function hidetoast(){
        $('.tips-toast').fadeOut(1000);
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




</script>

</body>
</html>

