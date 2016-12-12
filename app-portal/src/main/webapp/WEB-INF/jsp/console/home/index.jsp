<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@include file="/inc/import.jsp" %>

<!DOCTYPE html>
<html>
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
                    <aside>
                        <script language="JavaScript" type="text/javascript">
                            var marqueeContent = new Array();
                            window.onload = function() {
                                var param = {};
                                ajaxsync(ctx + "/console/message/account_message/list", param, function (result) {
                                    for (var i = 0; i < result.data.length; i++) {
                                        if(result.data[i].message.type=='0'&&result.data[i].message.title=='系统通知'){
                                            var sr = new String(result.data[i].message.content).substring(0,40);
                                            marqueeContent[i]="<span >"+sr+"</span>";
                                        }else{
                                            marqueeContent[i]="<span >"+result.data[i].message.title+"</span>";
                                        }
                                    }
                                    if(result.data.length>0){
                                        initMarquee();
                                    }
                                });
                            }
                            //                        marqueeContent[0]="<span >壹云有新的讯息1</span>";
                            //                        marqueeContent[1]="<span >壹云有新的讯息2</span>";
                            //                        marqueeContent[2]="<span >壹云有新的讯息3壹云有新的讯息3</span>";
                            //                        marqueeContent[3]="<span >壹云有新的讯息4</span>";
                            var marqueeInterval=new Array();
                            var marqueeId=0;
                            var marqueeDelay=2000;
                            var marqueeHeight=20;
                            //初始化
                            function initMarquee() {
                                var str = '';
                                if(marqueeContent.length>0){
                                    str=marqueeContent[0];
                                }


                                var html = '<div class="common-info" ><div id="marqueeBox" style="overflow:hidden;float:left;height:'+marqueeHeight+'px;width:80%;" onmouseover="clearInterval(marqueeInterval[0])" onmouseout="marqueeInterval[0]=setInterval(\'startMarquee()\',marqueeDelay)"><div>'+str+'</div></div><span class="close" id="common-close"></span><a class="detail" href="'+ctx+'/console/message/account_message/index">查看详情</a></div>';
                                document.getElementById("commonMsg").innerHTML= html;
                                marqueeId++;
                                marqueeInterval[0]=setInterval("startMarquee()",marqueeDelay);
                                $('#common-close').click(function(){
                                    $('.common-info').fadeOut()
                                });
                            }
                            //开始滚动
                            function startMarquee() {
                                var str=marqueeContent[marqueeId];
                                marqueeId++;
                                if(marqueeId>=marqueeContent.length) marqueeId=0;
                                if(document.getElementById("marqueeBox").childNodes.length==1) {
                                    var nextLine=document.createElement('DIV');
                                    nextLine.innerHTML=str;
                                    document.getElementById("marqueeBox").appendChild(nextLine);
                                }
                                else {
                                    document.getElementById("marqueeBox").childNodes[0].innerHTML=str;
                                    document.getElementById("marqueeBox").appendChild(document.getElementById("marqueeBox").childNodes[0]);
                                    document.getElementById("marqueeBox").scrollTop=0;
                                }
                                clearInterval(marqueeInterval[1]);
                                marqueeInterval[1]=setInterval("scrollMarquee()",20);
                            }

                            //
                            function scrollMarquee() {
                                document.getElementById("marqueeBox").scrollTop++;
                                if(document.getElementById("marqueeBox").scrollTop%marqueeHeight==(marqueeHeight-1)){
                                    clearInterval(marqueeInterval[1]);
                                }
                            }



                        </script>
                        <div id="commonMsg">
                        </div>
                        <section class="vbox xbox">
                            <!-- 如果没有三级导航 这段代码注释-->
                            <!-- <div class="head&#45;box"> <a href="#subNav" data&#45;toggle="class:hide"> <i class="fa fa&#45;angle&#45;left text"></i> <i class="fa fa&#45;angle&#45;right text&#45;active"></i> </a> </div> -->
                            <section class="scrollable wrapper w-f">
                                <section class="panel panel-default yunhuni-personal">
                                    <div class="row m-l-none m-r-none bg-light lter">

                                        <div class="col-md-3 padder-v fix-padding">
                                            <div class='wrappernewBox green-border-top'>
                                                <span class="pull-left m-r-sm">
                                                  <img src="${resPrefixUrl}/images/index/avatar-middle.png" width="80"/>
                                                </span>
                                                <span class="h5 block m-t-xs">
                                                  <i class="iconfont icon-yue middleicon"></i>
                                                  <span class="inline-block">余额(元)</span>
                                                </span>
                                                <span class="block m-t-25">
                                                  ${homeVO.arrearage}<small class="text-muted text-uc account-money green">￥${homeVO.balanceInt}</small>
                                                  <small class="account-number-decimal green">.${homeVO.balanceDec}</small>
                                                  元
                                                </span>
                                                <div class="box-footer">
                                                    <button class="btn btn-primary"  onclick="window.location.href='${ctx}/console/cost/recharge'">充值</button>
                                                    <button class="btn btn-default" onclick="window.location.href='${ctx}/console/cost/consume'">消费情况</button>
                                                </div>
                                            </div>
                                        </div>
                                        <c:set var="appSize" value="0"></c:set>
                                        <c:if test="${homeVO.appSize != null && homeVO.appSize > 0}">
                                            <c:set var="appSize" value="${homeVO.appSize}"></c:set>
                                        </c:if>
                                        <div class="col-md-3 padder-v fix-padding">
                                            <div class='wrappernewBox blue-border-top'>
                                                <span class="h5 block m-t-xs"> <i class="iconfont icon-oc-appnum middleicon m-r-5"></i><span class="inline-block">应用数（上线数/总数)</span></span>
                                                <a class="h5 block m-t-25 text-center" href="${ctx}/console/app/list"><span class="green bigger-font-size">${homeVO.onLineApp}/${appSize}</span>个</a>
                                                <div class="box-footer">
                                                    <a class="btn btn-primary" href="${ctx}/console/app/index" >创建应用</a>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="col-md-3 padder-v fix-padding">
                                            <div class='wrappernewBox yellow-border-top'>
                                                <span class="h5 block m-t-xs"> <i class="iconfont icon-huawuliang middleicon m-r-5"></i><span class="inline-block">平均通话时长(分钟)</span></span>
                                                <span class="h5 block m-t-25 text-center ">
                                                  <span class="green bigger-font-size">
                                                      <c:if test="${homeVO.lineAverageCallTime <= 0}">--</c:if>
                                                        <c:if test="${homeVO.lineAverageCallTime > 0}">
                                                            ${homeVO.lineAverageCallTime}
                                                        </c:if>
                                                  </span>
                                                </span>
                                            </div>
                                        </div>

                                        <div class="col-md-3 padder-v fix-padding">
                                            <div class='wrappernewBox pink-border-top'>
                                                <span class="h5 block m-t-xs"> <i class="iconfont icon-oc-callrate middleicon m-r-5"></i><span class="inline-block">接通率(%)</span></span>
                                                <span class="h5 block m-t-25 text-center ">
                                                    <span class="green bigger-font-size">
                                                        <c:if test="${homeVO.lineLinkRate <= 0.0}">--</c:if>
                                                        <c:if test="${homeVO.lineLinkRate > 0.0}">
                                                            ${homeVO.lineLinkRate}
                                                        </c:if>
                                                    </span>
                                                  </span>

                                            </div>
                                        </div>



                                    </div>
                                </section>
                                <section class="panel panel-default pos-rlt clearfix ">
                                    <div class="sectionnewWrap">
                                        <header class="panel-heading">
                                            <div class="h5 ">开发者账号</div>
                                        </header>
                                        <div class="panel-body clearfix border-top-none">
                                            <p>
                                                <span class="iconfont icon-oc-api smallicon inline-block develop-icon"></span>
                                                <span class="index-key"><strong>接口API:</strong></span>
                                                <span class="index-api restapi" id="restapi">${homeVO.restApi}</span>
                                            <span>
                                              <%--<a href="#"><i class="iconfont icon-oc-small-copy inline-block develop-icon"></i>API文档</a>--%>
                                              <a id="copyapi" ><i class="iconfont icon-oc-small-copy inline-block develop-icon"></i>复制</a>
                                            </span>
                                            </p>
                                            <p>
                                                <span class="iconfont icon-oc-secrekey smallicon inline-block develop-icon"></span>
                                                <span class="index-key"><strong>密钥:</strong></span>
                                                <span id="secretkey" class="index-api secretkey" >${homeVO.secretKey}</span>
                                            <span>
                                              <a class='reset_confirm'><i class="iconfont icon-resert smallicon inline-block develop-icon"></i>重新生成</a>
                                              <a id="copysecrekey"  ><i class="iconfont icon-oc-small-copy"></i>复制</a>
                                            </span>
                                            <span>
                                              <a class='tips-error tips-key'></a>
                                            </span>
                                            </p>
                                            <p>
                                                <span class="iconfont icon-oc-certid smallicon inline-block develop-icon"></span>
                                                <span class="index-key"><strong>鉴权账号:</strong></span>
                                                <span class="index-api certid" id="certid">${homeVO.certId}</span>
                                            <span>
                                              <a id="copycertid" ><i class="iconfont icon-oc-small-copy inline-block develop-icon"></i>复制</a>
                                            </span>
                                            </p>
                                        </div>
                                    </div>
                                </section>
                                <c:if test="${appSize== 0}">
                                    <section class="panel panel-default pos-rlt clearfix ">
                                        <div class="sectionWrap">
                                            <div class="panel-body clearfix">
                                                <div class="none-newapp">
                                                    <img src="${resPrefixUrl}/images/index/avatar-big.png" alt="">
                                            <span class="">
                                              <span class="app">您还没有创建属于自己的应用，快来创建应用吧</span>
                                              <br/><br/>
                                              <a class="btn btn-primary fl" href="${ctx}/console/app/index">创建应用</a>
                                            </span>
                                                </div>
                                            </div>
                                        </div>
                                    </section>
                                </c:if>

                                <c:if test="${appSize > 0}">
                                    <section class="panel panel-default pos-rlt clearfix ">
                                        <div class="sectionnewWrap">
                                            <header class="panel-heading">
                                                <div class="h5">
                                                    <a class="tab-data cursor tab-active" data-id="today">今日数据</a>
                                                    <a class="tab-data cursor" data-id="month">本月数据</a>
                                                    <a href="${ctx}/console/statistics/consume/index" class="tab-data">更多数据</a>
                                                </div>
                                            </header>
                                            <div class="panel-body clearfix border-top-none remove-padding">
                                                <div class="row m-l-none m-r-none bg-light lter">
                                                    <div class="col-md-2 padder-v fix-padding width-twenty-percent" >
                                                        <div class="warpbox">
                                                            <div class="">
                                                                <i class="icon iconfont icon-oc-wallet bigicon"></i>
                                                                <span class="green money datatoday" id="today_cost1"></span>
                                                                <span class="green money datamonth" id="month_cost1"></span>
                                                            </div>
                                                            <div class="middle-font-size middle-font-color" >
                                                                消费额(元)
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-2 padder-v fix-padding width-twenty-percent">
                                                        <div class="warpbox">
                                                            <div class="">
                                                                <i class="icon iconfont icon-oc-msg1 bigicon"></i>
                                                                <span class="green money datatoday" id="today_session">0</span>
                                                                <span class="green money datamonth" id="month_session">0</span>
                                                            </div>
                                                            <div class="middle-font-size middle-font-color" >
                                                                会话量(次)
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-2 padder-v fix-padding width-twenty-percent" >
                                                        <div class="warpbox">
                                                            <div class="">
                                                                <i class="icon iconfont icon-oc-mobile bigicon"></i>
                                                                <span class="green money datatoday" id="today_costTime">0</span>
                                                                <span class="green money datamonth"  id="month_costTime">0</span>
                                                            </div>
                                                            <div class="middle-font-size middle-font-color" >
                                                                话务量(分钟)
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-2 padder-v fix-padding width-twenty-percent">
                                                        <div class="warpbox">
                                                            <div class="">
                                                                <i class="icon iconfont icon-oc-talktime bigicon"></i>
                                                                <span class="green money datatoday" id="today_avgCostTime">0</span>
                                                                <span class="green money datamonth" id="month_avgCostTime">0</span>
                                                            </div>
                                                            <div class="middle-font-size middle-font-color" >
                                                                平均通话时长(分钟)
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-2 padder-v fix-padding width-twenty-percent" >
                                                        <div class="warpbox remove-border">
                                                            <div class="">
                                                                <i class="icon iconfont icon-oc-callrate bigicon"></i>
                                                                <span class="green money datatoday" id="today_avgCall">0</span>
                                                                <span class="green money datamonth" id="month_avgCall">0</span>
                                                            </div>
                                                            <div class="middle-font-size middle-font-color" >
                                                                接通率(%)
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                            </div>
                                        </div>
                                    </section>
                                    <!--话务量消费额-->
                                    <section class="panel panel-default pos-rlt clearfix ">
                                        <div class="sectionWrap">
                                            <div class="panel-body clearfix border-top-none">
                                                <div class="row monthform" >
                                                    <div class="col-md-12">
                                                        <input type="text" class="datepicker currentMonth form-control date_block monthstart" id="monthstart" value="${homeVO.time}" data-time="${homeVO.time}" />
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-md-12 scanvas" >
                                                        <!--统计列表-->
                                                        <div class="ecpanel" id="ecpanel" style=" height: 450px;  "></div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>


                                    </section>
                                    <!--话务量消费额end-->
                                    <section class="panel panel-default pos-rlt clearfix ">
                                        <div class="row ">
                                            <div class="col-md-6 remove-left-padding" >
                                                <div class="sectionWrap">
                                                    <div class="panel-body clearfix border-top-none">
                                                        <div class="row">
                                                            <div class="col-md-12">
                                                                <input type="text" class="datepicker currentMonth form-control date_block sessionstart" id="sessionstart" value="${homeVO.time}" data-time="${homeVO.time}" />
                                                            </div>
                                                        </div>
                                                        <div class="row" >
                                                            <div class="col-md-12">
                                                                <div class="ecpanel" id="sessionecpanel" style=" height: 450px;"></div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-6 remove-right-padding">
                                                <div class="sectionWrap">
                                                    <div class="panel-body clearfix border-top-none">
                                                        <div class="row">
                                                            <div class="col-md-12">
                                                                <input type="text" class="datepicker currentMonth form-control date_block apistart"  id="apistart" value="${homeVO.time}" data-time="${homeVO.time}" />
                                                            </div>
                                                        </div>
                                                        <div class="row">
                                                            <div class="ecpanel" id="apiecpanel" style="height: 450px;"></div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                    </section>
                                </c:if>
                                <!--end-->
                            </section>
                        </section>
                    </aside>
                </section>
                <a href="#" class="hide nav-off-screen-block" data-toggle="class:nav-off-screen" data-target="#nav"></a>
            </section>
        </section>
    </section>
</section>
<%@include file="/inc/footer.jsp"%>


<!--统计插件-->
<script src="${resPrefixUrl}/js/echarts.min.js" ></script>
<!--统计插件主题-->
<script src="${resPrefixUrl}/js/echartstheme/wonderland.js" ></script>
<script>
    function getAvg(id){
        var param = {};
        ajaxsync(ctx + "/console/home/get_avg_der/"+id, param, function (result) {
            $('#'+id+"_avgCall").html(result.data.avgCall);
            $('#'+id+"_avgCostTime").html(result.data.avgCostTime);
            var cost = new String(result.data.cost).split(".");
            var cost2 = "";
            if(cost.length==1){
                cost2="000";
            }else{
                cost2 = cost[1];
                if(cost[1].length>3){
                    cost2 = cost[1].substring(0,3);
                }else if(cost[1].length<3){
                    var tleng =3 - cost[1].length;
                    for(var i=0;i<tleng;i++){
                        cost2+="0";
                    }
                }
            }

            $('#'+id+"_cost1").html(cost[0]+'.<span class="small-font-size" >'+cost2+'</span>');
            $('#'+id+"_costTime").html(result.data.costTime);
            $('#'+id+"_session").html(result.data.session);
            if(id=='today'){
                $('.datatoday').show();$('.datamonth').hide();
            }else{
                $('.datatoday').hide();$('.datamonth').show();
            }
        },"get");
    }
    $('.tab-data').click(function(){
        var id = $(this).attr('data-id');
        $('.tab-data').removeClass('tab-active');
        $(this).addClass('tab-active');
        getAvg(id);
    })
    getAvg("today");
    //查询
    $('#monthstart').change(function(){
        var id = $(this).attr('data-id');
        var tipsclass  = '.'+id+'tips';
        $(tipsclass).html('');
        var starttime =  $('.'+id+'start').val();
        if(starttime==''){
            tips(tipsclass,'请先填写时间'); return;
        }
        initchart(starttime);
    });

    //查询
    $('#sessionstart').change(function(){
        var id = $(this).attr('data-id');
        var tipsclass  = '.'+id+'tips';
        $(tipsclass).html('');
        var starttime =  $('.'+id+'start').val();
        if(starttime==''){
            tips(tipsclass,'请先填写时间'); return;
        }
        initsessionchart(starttime);
    });

    //查询
    $('#apistart').change(function(){
        var id = $(this).attr('data-id');
        var tipsclass  = '.'+id+'tips';
        $(tipsclass).html('');
        var starttime =  $('.'+id+'start').val();
        if(starttime==''){
            tips(tipsclass,'请先填写时间'); return;
        }
        initapichart(starttime);
    });




    //初始时间
    function initialStartTime(){
//        var starttime =  $('.'+id+'start').val();
//        if(starttime==''){
//            starttime = $('.'+id+'start').attr('data-time');
//        }
        return '${homeVO.time}';
    }


    //触发函数
    /**
     * @param xd 消费数据
     * @param yd 会话数据
     * starttime 时间
     * endtime 对比时间
     */

    function initchart(starttime){
        if(!starttime){
            starttime = $('#monthstart').val();
        }
        var type='day';
        var param ={'type':type,'appId':-1,'startTime':starttime, csrfParameterName:csrfToken};
        ajaxsync(ctx+"/console/statistics/session/list",param,function(result){
            var resultData = result.data;
            xdAll = JSON.stringify(resultData[0]);
            Array.prototype.max = function(){
                return Math.max.apply({},this)
            }
            xdAll = eval('('+xdAll+')');
            ydAll = JSON.stringify(resultData[1]);
            ydAll = eval('('+ydAll+')');
//            xdAll2 = JSON.stringify(resultData[2]);
//            xdAll2 = eval('('+xdAll2+')');
            charts(xdAll,ydAll,resultData[0].max(),resultData[1].max(),type);
//            sessioncharts(xdAll2,resultData[2].max(),type);
        });



        //注意 tdata 等于 seriesjson[name]
//        var tdata = new Array('5月','6月');
//        var seriesjson  ="[{name:'5月',type:'line',data:[120, 132, 101, 134, 90, 230, 210,120, 132, 101, 134, 90, 230, 210,120, 132, 101, 134, 90, 230, 210,120, 132, 101,4000]},{name:'6月',type:'line',data:[220, 182, 191, 234, 290, 330, 310]}]";
//        var seriesjson = eval('('+seriesjson+')');
//
//        charts(tdata,seriesjson);
    }
    var xdAll="";
    var xdAll2="";
    var ydAll="";
    var xdAll3="";
    /**
     * 会话量
     * */
    function initsessionchart(starttime){
        if(!starttime){
            starttime = $('#sessionstart').val();
        }
        var type='day';
        //异步查询 返回json 数据
        //消费额最大值
        //会话次数最大值
        //数据处理
        var param ={'type':type,'appId':-1,'startTime':starttime, csrfParameterName:csrfToken};
        ajaxsync(ctx+"/console/statistics/session/list/session",param,function(result){
            var resultData = result.data;
            xdAll2 = JSON.stringify(resultData[0]);
            xdAll2 = eval('('+xdAll2+')');
            sessioncharts(xdAll2,resultData[0].max(),type);
        });
    }
    /**api调用
     * **/
    function initapichart(starttime){
        if(!starttime){
            starttime = $('#apistart').val();
        }
        var type='day';
        //异步查询 返回json 数据
        //消费额最大值
        //会话次数最大值
        //数据处理
        var param ={'type':type,'appId':-1,'startTime':starttime, csrfParameterName:csrfToken};
        ajaxsync(ctx+"/console/statistics/session/list/api",param,function(result){
            var resultData = result.data;
            xdAll3 = JSON.stringify(resultData[0]);
            xdAll3 = eval('('+xdAll3+')');
            apicharts(xdAll3,resultData[0].max(),type);
        });
    }


    var monthData = ['1日','2日','3日','4日','5日','6日','7日','8日','9日','10日','11日','12日','13日','14日','15日','16日','17日','18日','19日','20日','21日','22日','23日','24日','25日','26日','27日','28日','29日','30日','31日'];

    //测试随机生成数据
    function moneyDataMonth(){
        var res = [];
        var len = 31;
        while (len--) {
            res.push(Math.round(Math.random() * 1000));
        }
        return res;
    }

    //测试随机生成数据
    function moneyDataYear(){
        var res = [];
        var len = 12;
        while (len--) {
            res.push(Math.round(Math.random() * 1000));
        }
        return res;
    }

    //测试随机生成数据
    function meetDataMonth(){
        var res = [];
        var len = 0;
        while (len < 31) {
            res.push((Math.random()*10 + 5).toFixed(1) - 0);
            len++;
        }
        return res;
    }

    //测试随机生成数据
    function meetDataYear(){
        var res = [];
        var len = 0;
        while (len < 12) {
            res.push((Math.random()*10 + 5).toFixed(1) - 0);
            len++;
        }
        return res;
    }



    /**
     * 回调数据
     * @param xd 消费数据
     * @param yd 会话数据
     *
     *
     * @param type 年月
     */
    function charts(xd,yd,xdMax,xyMax,type){
        var Xdata = monthData;
        var Ydata = monthData;
        var textData = '日期';
        var myChart = echarts.init(document.getElementById('ecpanel'),'wonderland');
        option = {
            title: {
                text: '',
                subtext: ''
            },
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                data:['话务量', '消费额']
            },
            grid: {
                left: '0%',
                right: '0%',
                bottom: '3%',
                containLabel: true
            },
            toolbox: {
                show: true,
            },
            dataZoom: {
                show: false,
                start: 0,
                end: 100
            },
            xAxis: [
                {
                    type: 'category',
                    boundaryGap: true,
                    data : Xdata,
                    name: textData,

                },

            ],
            yAxis: [
                {
                    type: 'value',
                    scale: true,
                    name: '     话务量(分钟)',
                    max: xyMax,
                    min: 0,
                    nameGap:32,
                    nameTextStyle:{
                        color:'#999',
                    },
                    boundaryGap: [0.2,0.2]
                },
                {
                    type: 'value',
                    scale: true,
                    name: '消费额(元)    ',
                    max: xdMax,
                    min: 0,
                    nameGap:32,
                    nameTextStyle:{
                        color:'#999',
                    },
                    boundaryGap: [0.2, 0.2]
                }
            ],
            series: [
                {
                    name:'消费额',
                    type:'bar',
                    yAxisIndex: 1,
                    data:xd
                },
                {
                    name:'话务量',
                    type:'line',
                    data:yd
                }
            ]
        };
        myChart.setOption(option);
    }


    /**
     * 会话量 回调数据
     *
     */
    function sessioncharts(xd2,xyMax2,type){
        var Xdata = monthData;
        var xd = xd2;
        var yd = meetDataMonth();
        var sessionChart = echarts.init(document.getElementById('sessionecpanel'),'wonderland');

        option = {
            title: {
                text: ''
            },
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                data:['会话量']
            },
            grid: {
                left: '0%',
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
                data : Xdata
            },
            yAxis: {
                type: 'value',
                name: '  会话量(次)',
            },
            series: [
                {
                    name:'会话量',
                    type:'line',
                    stack: '总量',
                    data:xd,
                    areaStyle: {normal: {}}
                },
            ]
        };
        sessionChart.setOption(option);
    }

    /**
     * api调用次数 回调数据
     */
    function apicharts(xd3,xyMax3,type){
        var Xdata = monthData;
        var xd = xd3;
        var yd = meetDataMonth();
        var sessionChart = echarts.init(document.getElementById('apiecpanel'),'wonderland');

        option = {
            title: {
                text: ''
            },
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                data:['api调用次数']
            },
            grid: {
                left: '0%',
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
                name: '次数(次)'
            },
            series: [
                {
                    name:'api调用次数',
                    type:'line',
                    stack: '',
                    data:xd,
                    areaStyle: {normal: {}}
                },
            ]
        };
        sessionChart.setOption(option);
    }
    var appSize = '${appSize}';
    //当浏览器大小变化时
    $(window).resize(function () {
        if(appSize>0) {
            initchart();
            initsessionchart();
            initapichart();
        }
    });
    //    if(appSize>0){
    //        initchart();
    //        initsessionchart();
    //        initapichart();
    //    }
</script>


<script type="text/javascript">

    var copyapiBtn = document.querySelector('#copyapi')
    var copysecrekeyBtn = document.querySelector('#copysecrekey')
    var copycertidBtn = document.querySelector('#copycertid')

    // 点击的时候调用 copyTextToClipboard() 方法就好了.
    copyapiBtn.onclick = function() {
        copyTextToClipboard($("#restapi").html())
    }

    copysecrekeyBtn.onclick = function() {
        copyTextToClipboard($("#secretkey").html())
    }

    copycertidBtn.onclick = function() {
        copyTextToClipboard($("#certid").html())
    }


    function copyTextToClipboard(text) {
        var textArea = document.createElement("textarea")
        textArea.style.position = 'fixed'
        textArea.style.top = 0
        textArea.style.left = 0
        textArea.style.width = '2em'
        textArea.style.height = '2em'
        textArea.style.padding = 0
        textArea.style.border = 'none'
        textArea.style.outline = 'none'
        textArea.style.boxShadow = 'none'
        textArea.style.background = 'transparent'
        textArea.value = text
        document.body.appendChild(textArea)
        textArea.select()
        try {
            var msg = document.execCommand('copy') ? '成功' : '失败'
            showtoast('复制'+msg);
        } catch (err) {
            alert('不能使用这种方法复制内容');
        }
        document.body.removeChild(textArea)
    }



   /* if(window.clipboardData){
        //支持IE
        var restapi = document.getElementById("restapi");
        restapi.onclick = function(){
            window.clipboardData.setData('text',restapi.innerHTML);
            showtoast('复制成功');
        }

        var secretkey = document.getElementById("secretkey");
        secretkey.onclick = function(){
            window.clipboardData.setData('text',secretkey.innerHTML);
            showtoast('复制成功');
        }

        var certid = document.getElementById("certid");
        certid.onclick = function(){
            window.clipboardData.setData('text',certid.innerHTML);
            showtoast('复制成功');
        }


    }else{

        var restapi = new ZeroClipboard( document.getElementById("copyapi"), {
            moviePath: resPrefixUrl+"/js/zero/ZeroClipboard.swf"
        } );
        var secrekey = new ZeroClipboard( document.getElementById("copysecrekey"), {
            moviePath: resPrefixUrl+"/js/zero/ZeroClipboard.swf"
        } );
        var certid = new ZeroClipboard( document.getElementById("copycertid"), {
            moviePath: resPrefixUrl+"/js/zero/ZeroClipboard.swf"
        } );
        restapi.on( 'complete', function(client, args) {
            showtoast('复制成功');
        } );


    }
*/



</script>

<script src="${resPrefixUrl}/js/include.js" aysnc></script>
<script src="${resPrefixUrl}/js/include.js"></script>
<script type="text/javascript">
    $('.reset_confirm').click(function(){
        bootbox.setLocale("zh_CN");
        bootbox.confirm("确定重新生成密钥么", function(result) {
            if(result){
                ajaxsync(ctx + "/console/home/change_sk",null,function(response){
                    if(response.data != null){
                        $("#secretkey").html(response.data);
                        $(".tips-key").html("");
                        $('#copysecrekey').attr('data-clipboard-text',response.data);
                    }else{
                        //errorCode errorMsg
                        $(".tips-key").html("生成失败!" + response.errorMsg)
                    }
                },"get");
            }
        });
        return false
    });

    $('#common-close').click(function(){
        $('.common-info').fadeOut()

    })



</script>
<script type="text/javascript" src='${resPrefixUrl}/js/bootstrap-datepicker/js/bootstrap-datepicker.js'> </script>
<script type="text/javascript" src='${resPrefixUrl}/js/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js'> </script>
<script src="${resPrefixUrl}/js/index/index.js" ></script>
<script>

</script>


</body>
</html>