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
                        <div class="wrapper header"><span class="margin_lr"></span><span class="margin_lr border-left">&nbsp;费用管理</span>
                        </div>
                        <section class="scrollable">
                            <div class="slim-scroll">
                                <!-- nav -->
                                <nav class="hidden-xs">
                                    <ul class="nav">
                                        <li>
                                            <div class="aside-li-a active">
                                                <a href="${ctx}/console/cost/consume">消费记录</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a">
                                                <a href="${ctx}/console/cost/recharge">充值</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a">
                                                <a href="${ctx}/console/cost/recharge/list">充值订单</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a">
                                                <a href="${ctx}/console/cost/bill_month/get">月结账单</a>
                                            </div>
                                        </li>
                                    </ul>
                                </nav>
                            </div>

                            <div class="wrapper header"><span class="margin_lr"></span><span
                                    class="margin_lr border-left">&nbsp;发票管理</span>
                            </div>
                            <section class="scrollable">
                                <div class="slim-scroll">
                                    <!-- nav -->
                                    <nav class="hidden-xs">
                                        <ul class="nav">
                                            <li>
                                                <div class="aside-li-a">
                                                    <a href="${ctx}/console/cost/invoice_info">发票信息</a>
                                                </div>
                                            </li>
                                            <li>
                                                <div class="aside-li-a">
                                                    <a href="${ctx}/console/cost/invoice_apply/page">发票申请</a>
                                                </div>
                                            </li>
                                        </ul>
                                    </nav>
                                </div>
                            </section>
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
                            <span class="border-left">&nbsp;消费记录</span>
                        </div>
                        <form:form role="form" action="${ctx}/console/cost/consume" method="post" id="mainForm">
                        <section class="scrollable wrapper w-f">
                            <section class="panel panel-default pos-rlt clearfix ">
                                <ul id="myTab" class="nav nav-tabs">
                                    <li <c:if test="${appId==null||appId==''}">class="active"</c:if> ><a  data-toggle="tab" data-app="-1">全部应用</a></li>
                                    <c:forEach items="${appList}" var="app">
                                        <li <c:if test="${appId==app.id}">class="active"</c:if> ><a  data-toggle="tab" data-app="${app.id}">${app.name}</a></li>
                                    </c:forEach>
                                </ul>
                            </section>
                            <section class="panel panel-default yunhuni-personal">
                                <div class="row m-l-none m-r-none bg-light lter">
                                    <div class="col-md-12 padder-v fix-padding">
                                        <div class='wrapperBox cost_month cost_month_select'>
                                            <div class="panel-body clearfix border-top-none personal-base">
                                                <div class="row">
                                                    <a class="current_month">本月</a>
                                                    <a class="last_month">上月</a>
                                                    从
                                                    <input type="text" class="datepicker currentMonth form-control" value='${startTime}' name="startTime" id="startTime" data-date-end-date="0m" readonly="readonly"/>
                                                    到
                                                    <input type="text" class="datepicker lastMonth form-control" value='${endTime}' name="endTime" id="endTime" data-date-end-date="0m" readonly="readonly"/>
                                                    <input type="text" name="appId" id="appId" value="${appId}" hidden>
                                                    <button class="btn btn-primary query" type="button" id="findform" >查询</button>
                                                    <button class="btn btn-primary query" type="button" onclick="download()" >导出</button>
                                                    <span class="tips-error" ></span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </section>
                            </form:form>
                            <section class="panel panel-default pos-rlt clearfix ">
                                <table class="table table-striped cost-table-history">
                                    <thead>
                                    <tr>
                                        <th width="10">#</th>
                                        <th>消费时间</th>
                                        <th><span style="float:left;width: 80px" ><span style="float:right;" >消费金额</span></span></th>
                                        <th>消费类型</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${pageObj.result}" var="result" varStatus="s">
                                        <tr>
                                            <td scope="row">${s.index+1}</td>
                                            <td><fmt:formatDate value="${result.dt}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                                            <td><span style="float:left;width: 80px" ><span style="float:right;" >
                                                    ￥<fmt:formatNumber value="${result.amount}" pattern="0.000"></fmt:formatNumber>
                                            </span></span></td>
                                            <td>${result.type}
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </section>
                            <c:set var="pageUrl" value="${ctx}/console/cost/consume"></c:set>
                            <c:set var="extraParam" value="&appId=${appId}&startTime=${startTime}&endTime=${endTime}"></c:set>
                            <%@include file="/inc/pagefooter.jsp" %>
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
<script type="text/javascript" src='${resPrefixUrl }/js/bootstrap-datepicker/js/bootstrap-datepicker.js'> </script>
<script type="text/javascript" src='${resPrefixUrl }/js/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js'> </script>
<script type="text/javascript" src='${resPrefixUrl }/js/cost/history.js'> </script>
<script type="text/javascript">
    //应用
    $('#myTab li a').click(function(){
        var app = $(this).attr('data-app');
        if(app==-1){
            app=null;
        }
        $('#appId').val(app);
        $('#mainForm').submit();
    });
    function download(){
        $('#mainForm').attr('action',ctx+"/console/cost/consume/download");
        $('#mainForm').submit();
        $('#mainForm').attr('action',ctx+"/console/cost/consume");
    }
    function compareTime(starttime,endtime){
        if(!starttime){
            return '请填写开始时间';
        }
        if(!endtime){
            return '请填写结束时间';
        }
        var d1 = new Date(starttime.replace(/\-/g, "\/"));
        var d2 = new Date(endtime.replace(/\-/g, "\/"));
        if(d1>d2){
            return '时间范围填写有误';
        }
        $('#mainForm').submit();
    }
</script>
</body>
</html>

