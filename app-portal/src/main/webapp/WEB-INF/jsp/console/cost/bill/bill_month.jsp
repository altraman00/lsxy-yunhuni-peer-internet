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
                                                <div class="aside-li-a">
                                                    <a href="${ctx}/console/cost/recharge/list">充值订单</a>
                                                </div>
                                            </li>
                                            <li>
                                                <div class="aside-li-a active">
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
                                                    <div class="aside-li-a ">
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
                                <span class="border-left">&nbsp;月结账单</span>
                            </div>
                            <section class="scrollable wrapper w-f">
                                <section class="panel panel-default yunhuni-personal">
                                    <div class="row m-l-none m-r-none bg-light lter">
                                        <div class="col-md-12 padder-v fix-padding">
                                            <div class='wrapperBox cost_month'>
                                                <div class="panel-body clearfix border-top-none personal-base">
                                                    <form action="${ctx}/console/cost/bill_month/get" method="get">
                                                        <div class="row">
                                                            <span style="width: 20px;">
                                                            <lable class="">月份：</lable></span>
                                                            <input type="text" class="datepicker form-control" value='${month}' name="month" data-date-end-date="0m" />
                                                            <button class="btn btn-primary query">查询</button>
                                                        </div>
                                                        <div class="row">
                                                            <span style="width: 20px;">
                                                            <lable class="" >按应用：</lable></span>
                                                            <select class="form-control" name="appId" style="padding: 0;">
                                                                <option value="">所有应用</option>
                                                                <c:forEach var="app" items="${appList}">
                                                                    <option value="${app.id}"
                                                                    <c:if test="${app.id eq appId}">selected="selected"</c:if>
                                                                    >${app.name}</option>
                                                                </c:forEach>
                                                            </select>
                                                        </div>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </section>
                                <section class="panel panel-default yunhuni-personal">
                                    <div class="fix-padding">
                                        <p class="bg-success price_info">
                                            ${month} 消费总额  <span class="text-warning"> <fmt:formatNumber value="${sumAmount==null?0:sumAmount}" pattern="0.000"/></span> 元
                                        </p>
                                    </div>
                                </section>
                                <c:if test="${billMonths != null && fn:length(billMonths)>0}">
                                    <section class="panel panel-default pos-rlt clearfix ">

                                        <table class="cost-table table table-striped">
                                            <caption>消费项目</caption>
                                            <c:forEach items="${billMonths}" var="billMonth" varStatus="s">
                                                <c:if test="${s.index % 2 == 0}">
                                                <tr>
                                                </c:if>
                                                    <td class="extend_width">${billMonth.type} : </td>
                                                    <td> <fmt:formatNumber value="${billMonth.amongAmount==null?0:billMonth.amongAmount}" pattern="0.000"/> 元</td>
                                                <c:if test="${s.index % 2 == 1 || (s.index + 1) == fn:length(billMonths)}">
                                                    <c:if test="${s.index % 2 == 0}">
                                                        <td class="extend_width"></td><td></td>
                                                    </c:if>
                                                </tr>
                                                </c:if>
                                            </c:forEach>

                                        </table>
                                        <!--
                                        <button class="cost_history_download btn btn-default btn-sm">账单下载</button>
                                        -->
                                    </section>
                                </c:if>
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
<script type="text/javascript" src='${resPrefixUrl }/js/cost/month.js'> </script>
</body>
</html>

