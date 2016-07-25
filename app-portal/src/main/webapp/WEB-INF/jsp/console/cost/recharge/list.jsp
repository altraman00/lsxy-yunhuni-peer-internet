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
            <aside class="bg-Green lter aside hidden-print" id="nav"><%@include file="/inc/leftMenu.jsp"%></aside>
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
                                                <div class="aside-li-a ">
                                                    <a href="${ctx}/console/cost/consume">消费记录</a>
                                                </div>
                                            </li>
                                            <li>
                                                <div class="aside-li-a">
                                                    <a href="${ctx}/console/cost/recharge">充值</a>
                                                </div>
                                            </li>
                                            <li>
                                                <div class="aside-li-a active">
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
                            <div class="head-box"><a href="#subNav" data-toggle="class:hide"> <i
                                    class="fa fa-angle-left text"></i> <i class="fa fa-angle-right text-active"></i> </a>
                            </div>
                            <div class="wrapper header">
                                <span class="border-left">&nbsp;充值订单记录</span>
                            </div>
                            <section class="scrollable wrapper w-f">
                                <section class="panel panel-default yunhuni-personal">
                                    <div class="row m-l-none m-r-none bg-light lter">
                                        <div class="col-md-12 padder-v fix-padding">
                                            <div class='wrapperBox cost_month cost_month_select'>
                                                <div class="panel-body clearfix border-top-none personal-base">
                                                    <form action="${ctx}/console/cost/recharge/list" method="get">
                                                        <div class="row">
                                                            从
                                                            <input type="text" class="datepicker currentMonth form-control" name="startTime" value='${startTime}' data-date-end-date="0m" />
                                                            到
                                                            <input type="text" class="datepicker lastMonth form-control" name="endTime" value='${endTime}' data-date-end-date="0m" />
                                                            <button class="btn btn-primary query">查询</button>
                                                        </div>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </section>
                                <section class="panel panel-default pos-rlt clearfix ">
                                    <table class="table table-striped cost-table-history tablelist">
                                        <thead>
                                        <tr>
                                            <th>#</th>
                                            <th>订单号</th>
                                            <th>金额</th>
                                            <th>充值方式</th>
                                            <th>订单创建时间</th>
                                            <th>支付状态</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach items="${pageObj.result}" var="result" varStatus="s">
                                            <tr>
                                                <td scope="row">${s.index+1}</td>
                                                <td>${result.orderId}</td>
                                                <td><fmt:formatNumber value="${ result.amount}" pattern="#0.00" /> </td>
                                                <td>
                                                    ${result.typeName}
                                                </td>
                                                <td>
                                                    <fmt:formatDate value="${result.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                                </td>
                                                <td>
                                                    <c:if test="${'NOTPAID' eq result.status}">
                                                        <a href="${ctx}/console/cost/recharge/get?orderId=${result.orderId}" class="nosuccess text-underline" >${result.statusName}</a>
                                                    </c:if>
                                                    <c:if test="${'PAID' eq result.status}">
                                                        <a class="success">${result.statusName}</a>
                                                    </c:if>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </section>
                                <c:set var="pageUrl" value="${ctx}/console/cost/recharge/list"></c:set>
                                <c:set var="extraParam" value="&startTime=${startTime}&endTime=${endTime}"></c:set>
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
<script type="text/javascript" src="${resPrefixUrl }/js/cost/order.js"></script><!--must-->
</body>
</html>

