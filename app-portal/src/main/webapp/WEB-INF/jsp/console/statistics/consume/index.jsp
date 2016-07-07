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
            <aside class="bg-Green lter aside hidden-print" data-include="aside" id="nav"><%@include file="/inc/leftMenu.jsp"%></aside>
            <!-- /.aside -->

            <section id="content">
            <section class="hbox stretch">
                <aside>
                    <section class="vbox xbox">
                        <div class="wrapper header">
                            <span class="border-left">&nbsp;消费统计</span>
                        </div>
                        <form:form role="form" action="${ctx}/console/statistics/consume/index" method="post"   id="mainForm">
                        <section class="scrollable wrapper w-f">
                            <section class="panel panel-default yunhuni-personal">
                                <div class="row m-l-none m-r-none bg-light lter">
                                    <div class="col-md-2 col-md-offset-9 padder-v fix-padding">
                                        <select   class="form-control myselectapp" name="appId">
                                            <option value="0" >全部应用</option>
                                            <c:forEach items="${appList}" var="app">
                                                <option value="${app.id}" <c:if test="${app.id==consumeStatisticsVo.appId}">selected="selected"</c:if> >${app.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </section>



                            <section class="panel panel-default pos-rlt clearfix ">
                                <div class="row">
                                    <div class="col-md-12">
                                        <input type="radio" name="type" value="month" class="selectdata"  <c:if test="${consumeStatisticsVo.type=='month'}">checked</c:if> />日统计
                                        <input type="radio" name="type" value="year" class="selectdata"   <c:if test="${consumeStatisticsVo.type=='year'}">checked</c:if> />月统计
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-12 text-center">
                                        <p class="font18">消费额统计</p>
                                    </div>
                                </div>
                                <!--日统计-->
                                <div class="row monthform" >
                                    <div class="col-md-12">
                                        <input type="text" class="datepicker currentMonth form-control date_block monthstart" name="startTime" value="${consumeStatisticsVo.startTime}" />
                                        <span class="monthcontrast"><span>对比</span><input type="text" class="datepicker currentMonth form-control date_block monthend" name="endTime" value="${consumeStatisticsVo.endTime}"/></span>
                                        <button class="btn btn-primary finddatebtn" type="button"  data-id="month" >查询</button>
                                        <button class="btn btn-default compassbtn monthcbtn" type="button"   data-id="month"><c:if test="${consumeStatisticsVo.endTime!=''}">取消</c:if>对比</button>
                                        <span class="tips-error monthtips"></span>
                                    </div>
                                </div>
                                <!--月统计-->
                                <div class="row yearform" >
                                    <div class="col-md-12">
                                        <input type="text" class="datepicker  form-control currentYear date_block yearstart" name="startTimeYear" value="${consumeStatisticsVo.startTimeYear}"  />
                                        <span class="yearcontrast"><span>对比</span><input type="text" class="datepicker currentYear form-control date_block yearend" name="endTimeYear" value="${consumeStatisticsVo.endTimeYear}" /></span>
                                        <button class="btn btn-primary finddatebtn " type="button"  data-id="year" >查询</button>
                                        <button class="btn btn-default compassbtn yearcbtn" type="button" data-id="year"><c:if test="${consumeStatisticsVo.endTimeYear!=''}">取消</c:if>对比</button>
                                        <span class="tips-error yeartips"></span>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-12 scanvas" >
                                        <!--统计列表-->
                                        <div class="ecpanel" id="ecpanel" style=" height: 500px;  "></div>
                                    </div>
                                </div>
                            </section>
                            <section class="panel panel-default pos-rlt clearfix ">
                                <div class="form-group">
                                    <span class="hr text-label"><strong>详情数据</strong></span>
                                </div>
                                <table class="table table-striped ">
                                    <thead>
                                    <tr>
                                        <th>日期</th>
                                        <th>消费（元）</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${pageObj.result}" var="result">
                                        <tr>
                                            <td><fmt:formatDate value="${result.dt}" pattern="yyyy-MM-dd"></fmt:formatDate> </td>
                                            <td><fmt:formatNumber value="${result.sumAmount}" pattern="0.00"></fmt:formatNumber> </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </section>
                            <c:set var="extraParam" value="&startTimeYear=${consumeStatisticsVo.startTimeYear}&endTimeYear=${consumeStatisticsVo.endTimeYear}&startTime=${consumeStatisticsVo.startTime}&endTime=${consumeStatisticsVo.endTime}&type=${consumeStatisticsVo.type}&appId=${consumeStatisticsVo.appId}"></c:set>
                            <c:set var="pageUrl" value="${ctx}/console/statistics/consume/index"></c:set>
                            <%@include file="/inc/pagefooter.jsp" %>
                        </section>
                        </form:form>
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
    $('input[name="type"]').click(function(){
        //参数
//        var v = $(this).val();
//        if(v=='year'){
//            $('.yearform').show();
//            $('.monthform').hide();
//
//        }else{
//            alert(1);
//            $('.monthform').show();
//            $('.yearform').hide();s
//        }
        initForm();
    });

    //查询
    $('.finddatebtn').click(function(){
        var id = $(this).attr('data-id');
        var tipsclass  = '.'+id+'tips';
        $(tipsclass).html('');
        var starttime =  $('.'+id+'start').val();
        var endtime = '';
        if(starttime==''){
            tips(tipsclass,'请先填写时间'); return;
        }
        if($('.'+id+'cbtn').html()=='取消对比'){
            endtime =  $('.'+id+'end').val();
            if(endtime==''){
                tips(tipsclass,'请填写对比时间'); return;
            }
        }
        initForm();
    });

    function tips(tipsclass,tips){
        $(tipsclass).html(tips);
    }

    //对比
    $('.compassbtn').click(function(){
        var id = $(this).attr('data-id');
        var val = $(this).html();
        $('.'+id+'tips').html('');
        if(val=='对比'){
            $('.'+id+'contrast').show();
            //$('.'+id+'end').val('');
            $(this).html('取消对比');
        }else{
            $('.'+id+'contrast').hide();
            $(this).html('对比');
            if(id=='year'){
               $('input[name="endTimeYear"]').val("");
            }else{
                $('input[name="endTime"]').val("");
            }
            initForm();
        }
    });

    //应用
    $('.myselectapp').change(function(){
        initForm();
    });


    //初始时间
    function initialStartTime(id){
        var starttime =  $('.'+id+'start').val();
        var endtime = '';
        if(starttime==''){
            starttime = $('.'+id+'start').attr('data-time');
        }
        if($('.'+id+'cbtn').html()=='取消对比'){
            endtime =  $('.'+id+'end').val();
            if(endtime==''){

            }
        }
        return starttime;
    }


    //初始结束时间
    function initialEndTime(id){
        var endtime = '';
        if($('.'+id+'cbtn').html()=='取消对比'){
            endtime =  $('.'+id+'end').val();
        }
        return endtime;
    }

    function initForm(){
        var form = document.getElementById("mainForm");
        form.submit();
    }

    //触发函数
    /**
     * starttime 时间
     * endtime 对比时间
     */
    function initchart(){
        var type = $('input[name="type"]:checked').val();
        var appId = $('.myselectapp').val();
        var startTime = initialStartTime(type);
        var endTime = initialEndTime(type);
        var tdata = new Array();
        var seriesjson  ="";
        var resultData="";
        $.ajax({
            url : "${ctx}/console/statistics/consume/list",
            type : 'post',
            async: false,//使用同步的方式,true为异步方式
            data : {'type':type,'appId':appId,'startTime':startTime,'endTime':endTime, '${_csrf.parameterName}':'${_csrf.token}'},//这里使用json对象
            dataType: "json",
            success : function(data){
                resultData = data;
            },
            fail:function(){
                alert('密码验证失败，请重试')
            }
        });
        for(var i=0;i<resultData.length;i++){
            tdata[i]=resultData[i].name;
        }
        seriesjson=JSON.stringify(resultData);
        //alert(seriesjson);
        //异步查询 返回json 数据
        if(type=='year'){
            $('.yearform').show();
            $('.monthform').hide();
            if('${consumeStatisticsVo.endTimeYear}'!=''){
                $('.'+type+'contrast').show();
            }
            //注意 tdata 等于 seriesjson[name]
            //var tdata = new Array('2016年','2017年');
            //var seriesjson  ="[{name:'2016年',type:'line',data:[120, 132, 101, 134, 90, 230, 210,120, 132, 101, 134, 90]},{name:'2017年',type:'line',data:[220, 182, 191, 234, 290, 330, 310]}]";
            //var seriesjson = eval('('+seriesjson+')');
        }else{
            $('.monthform').show();
            $('.yearform').hide();
            if('${consumeStatisticsVo.endTime}'!=''){
                $('.'+type+'contrast').show();
            }
            //注意 tdata 等于 seriesjson[name]
            //var tdata = new Array('5月','6月');
            //var seriesjson  ="[{name:'5月',type:'line',data:[120, 132, 101, 134, 90, 230, 210,120, 132, 101, 134, 90, 230, 210,120, 132, 101, 134, 90, 230, 210,120, 132, 101,4000]},{name:'6月',type:'line',data:[220, 182, 191, 234, 290, 330, 310]}]";
            //var seriesjson = eval('('+seriesjson+')');
            //alert(seriesjson);
        }
        seriesjson = eval('('+seriesjson+')');
        charts(tdata,seriesjson,type);
    }


    var timeData  = ['一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月']
    var monthData = ['1日','2日','3日','4日','5日','6日','7日','8日','9日','10日','11日','12日','13日','14日','15日','16日','17日','18日','19日','20日','21日','22日','23日','24日','25日','26日','27日','28日','29日','30日','31日'];

    /**
     * 回调数据
     * @param tdata 标题项
     * @param tdata 标题项
     * @param tdata 标题项
     */
    function charts(tdata,series,type){
        var Xdata = monthData;
        if(type=='year'){
            Xdata = timeData;
        }
        var myChart = echarts.init(document.getElementById('ecpanel'),'wonderland');
        option = {
            title: {
                text: '',
                subtext:'金额（元）'
            },
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                data:tdata
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            toolbox: {
                feature: {
                    saveAsImage: {}
                }
            },
            xAxis: {
                type: 'category',
                boundaryGap: true,
                data: Xdata
            },
            yAxis: {
                type: 'value'
            },
            series: series
        };

        myChart.setOption(option);
    }
</script>
<!--must-->
<script type="text/javascript" src='${resPrefixUrl }/js/statistics/consume.js'> </script>
</body>
</html>

