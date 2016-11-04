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
                    <aside>
                        <section class="vbox xbox">
                            <div class="wrapper header">
                                <span class="border-left">&nbsp;会话统计</span>
                            </div>
                            <section class="scrollable wrapper w-f">
                                <section class="panel panel-default pos-rlt clearfix ">
                                    <ul id="myTab" class="nav nav-tabs">
                                        <input type="hidden" value="-1" id="defaultapp"/>
                                        <li class="active"><a  data-toggle="tab" data-app="-1">全部应用</a></li>
                                        <c:forEach items="${appList}" var="app">
                                            <li ><a  data-toggle="tab" data-app="${app.id}">${app.name}</a></li>
                                        </c:forEach>
                                    </ul>
                                </section>
                                <section class="panel panel-default pos-rlt clearfix ">
                                    <div class="row margin-bottom-20">
                                        <div class="col-md-9">
                                            <input type="radio" name="stime" value="month" class="selectdata" checked/>日统计
                                            <input type="radio" name="stime" value="year" class="selectdata ml-15" />月统计
                                        </div>
                                    </div>

                                    <!--日统计-->
                                    <div class="row monthform" >
                                        <div class="col-md-12">
                                            <input type="text" class="datepicker currentMonth form-control date_block monthstart" value="${startTimeMonth}" data-time="${startTimeMonth}" />
                                            <button class="btn btn-primary finddatebtn" data-id="month" >查询</button>
                                            <span class="tips-error monthtips"></span>

                                        </div>
                                    </div>

                                    <!--月统计-->
                                    <div class="row yearform" >
                                        <div class="col-md-12">
                                            <input type="text" class="datepicker  form-control currentYear date_block yearstart" value="${startTimeYear}" data-time="${startTimeYear}" />
                                            <button class="btn btn-primary finddatebtn " data-id="year" >查询</button>
                                            <span class="tips-error yeartips"></span>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-12 text-center">
                                            <p class="font18">话务量/消费额统计</p>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-12 scanvas" >
                                            <!--统计列表-->
                                            <div class="ecpanel" id="ecpanel" style=" height: 450px;  "></div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-12 text-center">
                                            <p class="font18">会话量统计</p>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-12 scanvas" >
                                            <!--统计列表-->
                                            <div class="ecpanel" id="ecpane2" style=" height: 450px;  "></div>
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
    <script type="text/javascript" src='${resPrefixUrl }/js/bootstrap-datepicker/js/bootstrap-datepicker.js'> </script>
    <script type="text/javascript" src='${resPrefixUrl }/js/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js'> </script>
    <!--统计插件-->
    <script src="${resPrefixUrl }/js/echarts.min.js" ></script>
    <!--统计插件主题-->
    <script src="${resPrefixUrl }/js/echartstheme/wonderland.js" ></script>


    <script>


        $('input[name="stime"]').click(function(){
            var v = $(this).val();
            if(v=='year'){
                $('.yearform').show();
                $('.monthform').hide();
            }else{
                $('.monthform').show();
                $('.yearform').hide();
            }
            initchart();
        });

        //查询
        $('.finddatebtn').click(function(){
            var id = $(this).attr('data-id');
            var tipsclass  = '.'+id+'tips';
            $(tipsclass).html('');
            var starttime =  $('.'+id+'start').val();
            if(starttime==''){
                tips(tipsclass,'请先填写时间'); return;
            }
            initchart();
        });



        function tips(tipsclass,tips){
            $(tipsclass).html(tips);
        }

        //应用
        $('#myTab li a').click(function(){
            var app = $(this).attr('data-app');
            $('#defaultapp').val(app);
            initchart();
        });


        //获取当前选中应用
        function getapp(){
            var appname = '';
            $('#myTab li').each(function(){
                var res = $(this).hasClass('active');
                if(res){
                    appname = $(this).find('a').attr('data-app');
                }
            });
            return appname;
        }


        //初始时间
        function initialStartTime(id){
            var starttime =  $('.'+id+'start').val();
            if(starttime==''){
                starttime = $('.'+id+'start').attr('data-time');
            }
            return starttime;
        }


        //触发函数
        /**
         * starttime 时间
         * endtime 对比时间
         */
        var xdAll="";
        var xdAll2="";
        var ydAll="";
        function initchart(){
            var type = $('input[name="stime"]:checked').val();
            var type1 = 'day';
            if(type=='year'){type1='month'}
            var app = $('#defaultapp').val();
            var starttime = initialStartTime(type);
            //异步查询 返回json 数据
            var param ={'type':type1,'appId':app,'startTime':starttime, csrfParameterName:csrfToken};
            ajaxsync(ctx+"/console/statistics/session/list",param,function(result){
                var resultData = result.data;
                xdAll = JSON.stringify(resultData[0]);
                Array.prototype.max = function(){
                    return Math.max.apply({},this)
                }
                xdAll = eval('('+xdAll+')');
                ydAll = JSON.stringify(resultData[1]);
                ydAll = eval('('+ydAll+')');
                xdAll2 = JSON.stringify(resultData[2]);
                xdAll2 = eval('('+xdAll2+')');
//            xdAll2 = JSON.stringify(resultData[0]);
//            xdAll2 = eval('('+xdAll2+')');
                charts(xdAll,ydAll,resultData[0].max(),resultData[1].max(),type);
                charts2(xdAll2,resultData[2].max(),type);
            });
        }

        var t = ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'];
        var timeData  = ['一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月'];
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
         * @param tdata 标题项
         * @param tdata 标题项
         * @param tdata 标题项
         */
        function charts(xd,yd,xdMax,xyMax,type){
//        var type = $('input[name="stime"]:checked').val();
            var Xdata = monthData;
            var Ydata = monthData;
            var textData = '日期';
            if(type=='year'){
                Xdata = timeData;
                Ydata = timeData;
                textData = '月份';
//            var xd = moneyDataYear();
//            var yd = meetDataYear();
            }
//            var xd = moneyDataMonth();
//            var yd = meetDataMonth();
//        }

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
                    left: '3%',
                    right: '3%',
                    bottom: '3%',
                    containLabel: true
                },
                toolbox: {
                    show: true,
                    feature: {
                    }
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
//                {
//                    type: 'category',
//                    boundaryGap: true,
//                    data: Ydata,
//                }
                ],
                yAxis: [
                    {
                        type: 'value',
                        scale: true,
                        name: '话务量(分钟)',
                        max: xyMax,
                        min: 0,
                        boundaryGap: [0.2, 0.2],
                        nameGap:32,
                        nameTextStyle:{
                            color:'#999',
                        }
                    },
                    {
                        type: 'value',
                        scale: true,
                        name: '消费额(元)',
                        max: xdMax,
                        min: 0,
                        boundaryGap: [0.2, 0.2],
                        nameGap:32,
                        nameTextStyle:{
                            color:'#999',
                        }
                    }
                ],
                series: [
                    {
                        name:'消费额',
                        type:'bar',
//                    xAxisIndex: 1,
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
         * 回调数据
         * @param tdata 标题项
         * @param tdata 标题项
         * @param tdata 标题项
         */
        function charts2(xd2,xyMax2,type){
//        var type = $('input[name="stime"]:checked').val();
            var Xdata = monthData;
            var Ydata = monthData;
            var textData = '日期';
            if(type=='year'){
                Xdata = timeData;
                Ydata = timeData;
                textData = '月份';
//            var xd = moneyDataYear();
//            var yd = meetDataYear();
            }
//            var xd = moneyDataMonth();
//            var yd = meetDataMonth();
//        }

            var myChart2 = echarts.init(document.getElementById('ecpane2'),'wonderland');
            option = {
                title: {
                    text: '',
                    subtext: ''
                },
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data:['会话量']
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                toolbox: {
                    show: true,
                    feature: {
                    }
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

                    }
                ],
                yAxis: [
                    {
                        type: 'value',
                        scale: true,
                        name: '会话量(次)',
                        max: xyMax2,
                        min: 0,
                        boundaryGap: [0.2, 0.2],
                        nameGap:32,
                        nameTextStyle:{
                            color:'#999',
                        }
                    }
                ],
                series: [
                    {
                        name:'会话量',
                        type:'line',
                        data:xd2,
                        areaStyle: {normal: {}}
                    }
                ]
            };

            myChart2.setOption(option);
        }





        //当浏览器大小变化时
        $(window).resize(function () {
            initchart()
        });




    </script>


    <!--must-->
    <script type="text/javascript" src='${resPrefixUrl }/js/statistics/conversation.js'> </script>
</body>
</html>

