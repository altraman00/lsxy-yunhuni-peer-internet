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
                    <aside class="bg-green lter aside-sm hidden-print ybox" id="subNav">
                        <section class="vbox">
                            <div class="wrapper header"><span class="margin_lr"></span><span class="margin_lr border-left">&nbsp;呼叫中心统计</span>
                            </div>
                            <section class="scrollable">
                                <div class="slim-scroll">
                                    <nav class="hidden-xs">
                                        <ul class="nav">
                                            <li>
                                                <div class="aside-li-a active">
                                                    <a href="${ctx}/console/statistics/callcenter/index">综合统计分析</a>
                                                </div>
                                                <div class="aside-li-a">
                                                    <a href="${ctx}/console/statistics/callcenter/detail">明细查询</a>
                                                </div>
                                            </li>
                                        </ul>
                                    </nav>
                                </div>
                            </section>
                        </section>
                    </aside>
            <section class="vbox xbox">
                <!-- 如果没有三级导航 这段代码注释-->
                <div class="head-box"><a href="#subNav" data-toggle="class:hide">
                    <i class=" iconfont icon-menu-left text"></i>
                    <i class=" iconfont icon-menu-right text-active"></i>
                </a></div>
                <div class="wrapper header">
                    <span class="border-left">呼叫中心统计</span>
                </div>
                <section class="scrollable wrapper w-f">

                    <section class="panel panel-default pos-rlt clearfix ">
                        <input type="hidden" value="${appId}" id="defaultapp" />
                        <ul id="myTab" class="nav nav-tabs">
                            <li <c:if test="${appId==null}"> class="active"</c:if>><a  data-toggle="tab" data-app="${appId}">全部应用</a></li>
                            <c:forEach items="${appList}" var="app" varStatus="s">
                                <li
                                        <c:if test="${app.id==appId}"> class="active"</c:if>
                                >
                                    <a href="" data-toggle="tab" data-app="${app.id}" >${app.name}</a>
                                </li>
                            </c:forEach>
                        </ul>
                    </section>

                    <section class="panel panel-default pos-rlt clearfix ">
                        <div class="sectionnewWrap">
                            <header class="panel-heading">
                                <div class="h5">
                                    <a class="tab-data cursor tab-active" data-id="today">本月数据</a>
                                </div>
                            </header>
                            <div class="panel-body clearfix border-top-none remove-padding">
                                <div class="row m-l-none m-r-none bg-light lter">
                                    <div class="col-md-2 padder-v fix-padding" >
                                        <div class="warpbox">
                                            <div class="">
                                                <i class="icon iconfont icon-oc-incoming bigicon"></i>
                                                <span class="green money datatoday" id="callIn"></span>
                                            </div>
                                            <div class="middle-font-size middle-font-color" >
                                                呼入量（次）
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-2 padder-v fix-padding">
                                        <div class="warpbox">
                                            <div class="">
                                                <i class="icon iconfont icon-oc-exhale bigicon"></i>
                                                <span class="green money datatoday" id="callOut"></span>
                                            </div>
                                            <div class="middle-font-size middle-font-color" >
                                                呼出量（次）
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-2 padder-v fix-padding" >
                                        <div class="warpbox">
                                            <div class="">
                                                <i class="icon iconfont icon-oc-transfer bigicon"></i>
                                                <span class="green money datatoday" id="transferSuccess"></span>
                                            </div>
                                            <div class="middle-font-size middle-font-color" >
                                                转接成功（次）
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-2 padder-v fix-padding">
                                        <div class="warpbox">
                                            <div class="">
                                                <i class="icon iconfont icon-oc-lineup bigicon"></i>
                                                <span class="green money datatoday" id="formTime"></span>
                                            </div>
                                            <div class="middle-font-size middle-font-color" >
                                                平均排队时长（秒）
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-2 padder-v fix-padding">
                                        <div class="warpbox">
                                            <div class="">
                                                <i class="icon iconfont icon-oc-talktime bigicon"></i>
                                                <span class="green money datatoday" id="callTime"></span>
                                            </div>
                                            <div class="middle-font-size middle-font-color" >
                                                平均通话时长（秒）
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-2 padder-v fix-padding" >
                                        <div class="warpbox remove-border">
                                            <div class="">
                                                <i class="icon iconfont icon-oc-wastage bigicon"></i>
                                                <span class="green money datatoday" id="callFail"></span>
                                            </div>
                                            <div class="middle-font-size middle-font-color" >
                                                呼入流失率（%）
                                            </div>
                                        </div>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </section>
                    <section class="panel panel-default pos-rlt clearfix ">
                        <div class="row margin-bottom-20" >
                            <div class="col-md-12">
                                <input type="radio" name="calltime"  data-id="call" value="day" class="selectdata" checked/>日统计
                                <input type="radio" name="calltime"  data-id="call" value="month" class="selectdata ml-15" />月统计
                            </div>
                        </div>


                        <div class="row" id="calldate" >
                            <div class="col-md-12">
                                <!--日统计-->
                                <input type="text" class="datepicker currentMonth form-control date_block monthstart monthform" id="calldateday" value="${day}" data-time="${day}"/>
                                <!--月统计-->
                                <input type="text" class="datepicker  form-control currentYear date_block yearstart yearform" id="calldatemonth" value="${month}"  data-time="${month}"/>
                                <button class="btn btn-primary finddatebtn" data-id="call" >查询</button>
                                <span class="tips-error monthtips"></span>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12 text-center">
                                <p class="font18">呼叫次数统计</p>
                            </div>
                        </div>

                        <!--统计-->
                        <div class="row">
                            <div class="col-md-12 scanvas">
                                <!--统计列表-->
                                <div class="ecpanel" id="ecpanel" style=" height: 450px;"></div>
                            </div>
                        </div>
                    </section>

                    <section class="panel panel-default pos-rlt clearfix">
                        <div class="row margin-bottom-20">
                            <div class="col-md-12">
                                <input type="radio" name="talktime" value="day" data-id="talk" class="selectdata" checked/>日统计
                                <input type="radio" name="talktime" value="month"  data-id="talk" class="selectdata ml-15" />月统计
                            </div>
                        </div>

                        <!--日统计-->
                        <div class="row" id="talkdate">
                            <div class="col-md-12">
                                <input type="text" class="datepicker currentMonth form-control date_block monthtalkstart monthform" id="talkdateday" value="${day}" data-time="${day}" />
                                <input type="text" class="datepicker  form-control currentYear date_block yeartalkstart yearform" id="talkdatemonth" value="${month}" data-time="${month}" />
                                <button class="btn btn-primary finddatebtn" data-id="talk" >查询</button>
                                <span class="tips-error"></span>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12 text-center">
                                <p class="font18">通话时长统计</p>
                            </div>
                        </div>
                        <!--统计-->
                        <div class="row">
                            <div class="col-md-12 scanvas" >
                                <!--统计列表-->
                                <div class="ecpanel" id="ecpaneltalk" style=" height: 450px;"></div>
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





<%@include file="/inc/footer.jsp"%>
<script src="${resPrefixUrl }/js/include.js"></script>
<script type="text/javascript" src='${resPrefixUrl }/js/bootstrap-datepicker/js/bootstrap-datepicker.js'> </script>
<script type="text/javascript" src='${resPrefixUrl }/js/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js'> </script>
<!--统计插件-->
<script src="${resPrefixUrl }/js/echarts.min.js" ></script>
<!--统计插件主题-->
<script src="${resPrefixUrl }/js/echartstheme/wonderland.js" ></script>
<!--syncpage-->
<script type="text/javascript" src='${resPrefixUrl }/js/page.js'></script>

<script>

    //呼叫次数统计
    $('input[name="calltime"]').click(function(){
        var v = $(this).val();
        if(v=='month'){
            $('#calldate .yearform').show();$('#calldate .monthform').hide();
        }else{
            $('#calldate .yearform').hide();$('#calldate .monthform').show();
        }
        initchart();
    });

    //呼通话时长统计
    $('input[name="talktime"]').click(function(){
        var v = $(this).val();
        if(v=='month'){
            $('#talkdate .yearform').show();$('#talkdate .monthform').hide();
        }else{
            $('#talkdate .yearform').hide();$('#talkdate .monthform').show();
        }
        inittalkchart();
    });


    //查询
    $('.finddatebtn').click(function(){
        var id = $(this).attr('data-id');
        if(id=='call')
            initchart();
        if(id=='talk')
            inittalkchart();
    });


    //初始时间
    function initialStartTime(id){
        var starttime =  $(id).val();
        if(starttime==''){
            starttime = $(id).attr('data-time');
        }
        return starttime;
    }


    //测试随机生成数据
    function meetDataMonth(){
        var res = [];
        var len = 31;
        while (len--) {
            res.push(Math.round(Math.random() * 1000));
        }
        return res;
    }

    //测试随机生成数据
    function meetDataYear(){
        var res = [];
        var len = 12;
        while (len--) {
            res.push(Math.round(Math.random() * 1000));
        }
        return res;
    }

    //呼叫触发函数
    /**
     * starttime 时间
     */
    function initchart(){
        var type = $('input[name="calltime"]:checked').val();
        var app = $('#defaultapp').val();
        var starttime = initialStartTime("#calldate"+type);

        var param ={'type':type,'appId':app,'startTime':starttime, csrfParameterName:csrfToken};
        ajaxsync(ctx+"/console/statistics/session/list/session",param,function(result){
            var resultData = result.data;
            callcharts(resultData[0],type);
        });
//        if(type=='month'){
//            var xd  = meetDataYear();
//        }else{
//            var xd  = meetDataMonth();
//        }
////        呼叫次数统计
//        callcharts(xd,type);
    }

    //通话时长触发函数
    function inittalkchart() {
        var type = $('input[name="talktime"]:checked').val();
        var app = $('#defaultapp').val();
        var starttime = initialStartTime("#talkdate"+type);
//        var param  = {starttime:starttime,type:type,app:app};
//        if(type=='month'){
//            var xd  = meetDataYear();
//        }else{
//            var xd  = meetDataMonth();
//        }
//        //呼叫次数统计
//        talkcharts(xd2,type);
        var param ={'type':type,'appId':app,'startTime':starttime, csrfParameterName:csrfToken};
        ajaxsync(ctx+"/console/statistics/session/list/call/time",param,function(result){
            var resultData = result.data;
            talkcharts(resultData[0],type);
        });

    }

    var timeData  = ['一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月']
    var monthData = ['1日','2日','3日','4日','5日','6日','7日','8日','9日','10日','11日','12日','13日','14日','15日','16日','17日','18日','19日','20日','21日','22日','23日','24日','25日','26日','27日','28日','29日','30日','31日'];

    /**
     * 呼叫次数回调
     * @param xd 呼叫数据
     * @param yd 时间类型
     *
     */
    function callcharts(xd,type){
        var Xdata = monthData;
        if(type=='month'){
            var Xdata = timeData;
        }
        var callChart = echarts.init(document.getElementById('ecpanel'),'wonderland');
        option = {
            title: {
                text: ''
            },
            tooltip: {
                trigger: 'axis'
            },
            grid: {
                left: '2%',
                right: '3%',
                bottom: '3%',
                containLabel: true
            },
            toolbox: {
                feature: {
                }
            },
            xAxis: {
                type: 'category',
                boundaryGap: false,
                data : Xdata,
                name: '',
            },
            yAxis: {
                type: 'value',
                name:'     呼叫次数（次）',
                nameGap:32,
                min: 0,
                axisLabel:{
                    margin:12
                }
            },
            series: [
                {
                    name:'呼叫次数',
                    type:'line',
                    data:xd,
                    areaStyle: {normal: {}}
                },
            ]
        };
        callChart.setOption(option);
    }


    /**
     * 通话时长数回调
     * @param xd 呼叫数据
     * @param yd 时间类型
     *
     */
    function talkcharts(xd,type){
        var Xdata = monthData;
        if(type=='month'){
            var Xdata = timeData;
        }
        var talkChart = echarts.init(document.getElementById('ecpaneltalk'),'wonderland');
        option = {
            title: {
                text: '',
            },
            tooltip: {
                trigger: 'axis'
            },
            grid: {
                left: '2%',
                right: '3%',
                bottom: '3%',
                containLabel: true
            },
            toolbox: {
                feature: {
                }
            },
            xAxis: {
                type: 'category',
                boundaryGap: false,
                data : Xdata,
            },
            yAxis: {
                type: 'value',
                name:'      通话时长（分钟）',
                nameGap:32,
                min: 0,
                nameTextStyle:{
                    color:'#999',
                    padding:[10,10,10,50],
                },
                boundaryGap: [0.2,0.2]
            },
            series: [
                {
                    name:'通话时长',
                    type:'line',
                    data:xd,
                    areaStyle: {normal: {}}
                },
            ]
        };
        talkChart.setOption(option);
    }




    function updatetable(){
        //获取数据总数
        var count = 55;
        //每页显示数量
        var listRow = 3;
        //显示多少个分页按钮
        var showPageCount = 5;
        //指定id，创建分页标签
        var pageId = 'datatablepage';
        //searchTable 为方法名
        var page = new Page(count,listRow,showPageCount,pageId,searchTable);
        page.show();

    }


    /**
     * 分页回调方法
     * @param nowPage 当前页数
     * @param listRows 每页显示多少条数据
     * */
    var searchTable = function(nowPage,listRows)
    {

        var data = [
            ['2016-06-06 16:00', '1000.00'],
            ['2016-06-06 16:00', '1000.00'],
            ['2016-06-06 16:00', '1000.00']
        ];
        i++;
        var html ='';
        //数据列表
        for(var i = 0 ; i<data.length; i++){
            html +='<tr><td>'+data[i][0]+'</td><td>'+data[i][1]+'</td></tr>';
        }
        $('#tableModal').find("tr").remove();
        $('#tableModal').append(html);
    }


    //当浏览器大小变化时
    $(window).resize(function () {
        initchart();
        inittalkchart();

    });
    function getCurrentMonth(){
        var app = $('#defaultapp').val();
        var params = {'appId':app};
        ajaxsync(ctx + "/console/statistics/callcenter/get_current_month", params, function (result) {
            if(result.success){
                //result.data
                $('#callIn').html(result.data.callIn);//呼入量
                $('#callOut').html(result.data.callOut);//呼出量
                $('#transferSuccess').html(result.data.transferSuccess);//转接成功
                $('#formTime').html(result.data.formTime);//排队时间
                $('#callTime').html(result.data.callTime);//平均通话时长
                $('#callFail').html(result.data.callFail);//呼入流失率
            }
        },"get");
    }
    window.onload = function(){
        getCurrentMonth();
    }
    //应用
    $('#myTab li a').click(function(){
        var app = $(this).attr('data-app');
        $('#defaultapp').val(app);
        //初始化本月数据
        getCurrentMonth();
        //初始化呼叫次数
        initchart();
        //初始化通话时长
        inittalkchart();
    });

</script>


<!--must-->
<script type="text/javascript" src='${resPrefixUrl }/js/statistics/call.js'> </script>
</body>
</html>

