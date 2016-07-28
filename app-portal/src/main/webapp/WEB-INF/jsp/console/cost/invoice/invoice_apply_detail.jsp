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
                            <div class="wrapper header"><span class="margin_lr"></span><span class="margin_lr border-left">&nbsp;费用管理</span>
                            </div>
                            <section class="scrollable">
                                <div class="slim-scroll">
                                    <!-- nav -->
                                    <nav class="hidden-xs">
                                        <ul class="nav">
                                            <li>
                                                <div class="aside-li-a">
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
                                                    <div class="aside-li-a active">
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
                            <section class=" w-f cost_invoice">
                                <div class="wrapper header">
                                    <span class="border-left">&nbsp;发票详情</span>
                                </div>
                                <div class="row m-l-none m-r-none bg-light lter">
                                    <div class="row">
                                        <form role="form" action="" method="post" class="register-form"
                                              id="costInvoiceForm">
                                            <!--个人普通-->
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right">开具发票金额：</lable>
                                                <lable class="col-md-9 line34"><fmt:formatNumber value="${ apply.amount}" pattern="#0.00" />元</lable>
                                            </div>
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right ">发票类型：</lable>
                                                <lable class="col-md-6  line34">
                                                    <c:if test="${apply.type == 1}">
                                                        个人增值税普通发票
                                                    </c:if>
                                                    <c:if test="${apply.type == 2}">
                                                        企业增值税普通票
                                                    </c:if>
                                                    <c:if test="${apply.type == 3}">
                                                        企业增值税专用票
                                                    </c:if>
                                                </lable>
                                            </div>
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right ">状态：</lable>
                                                    <c:if test="${apply.status == 0}">
                                                        <lable class="col-md-9 line34 success">申请已提交</lable>
                                                    </c:if>
                                                    <c:if test="${apply.status == 1}">
                                                        <lable class="col-md-9 line34 success">已受理完成，发票已寄出</lable>
                                                    </c:if>
                                                    <c:if test="${apply.status == 2}">
                                                        <lable class="col-md-9 line34 nosuccess">异常</lable>
                                                    </c:if>
                                                <!--状态说明
                                                    申请已提交
                                                    异常
                                                    已受理完成
                                                -->
                                                <!--<lable class="col-md-9 line34 success">申请已提交</lable>-->
                                                <!--<lable class="col-md-9 line34 nosuccess">异常</lable>-->
                                            </div>
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right ">发票抬头：</lable>
                                                <lable class="col-md-9 line34 "> ${apply.title}</lable>
                                            </div>
                                            <c:if test="${apply.type == 3}">
                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right ">纳税人识别号：</lable>
                                                    <lable class="col-md-9 line34 "> ${apply.taxpayerNum}</lable>
                                                </div>

                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right ">银行账户：</lable>
                                                    <lable class="col-md-9 line34 ">${apply.bankAccount}</lable>
                                                </div>

                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right ">开户行：</lable>
                                                    <lable class="col-md-9 line34 ">${apply.bank}</lable>
                                                </div>

                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right ">注册地址：</lable>
                                                    <lable class="col-md-9 line34 ">${apply.regAddress}</lable>
                                                </div>
                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right ">企业电话：</lable>
                                                    <lable class="col-md-9 line34 ">${apply.phone}</lable>
                                                </div>
                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right lineheight-24">一般纳税人认证资格证书：</lable>
                                                    <div class="col-md-9 line34 ">
                                                        <c:set var="defaultImgUrl" value="${(empty apply.qualificationUrl)? (resPrefixUrl.concat('/images/index/l6.png')): (ctx.concat('/console/oss/img?uri=').concat(apply.qualificationUrl))}" />
                                                        <img src="${defaultImgUrl}" alt="" id="imgPrev" width="100" height="80" class="recordimg" />
                                                    </div>
                                                </div>
                                            </c:if>
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right ">收取地址：</lable>
                                                <lable class="col-md-9 line34 ">${apply.receiveAddress}</lable>
                                            </div>

                                            <div class="form-group">
                                                <lable class="col-md-3 text-right ">收件人：</lable>
                                                <lable class="col-md-9 line34 ">${apply.receivePeople}</lable>
                                            </div>

                                            <div class="form-group">
                                                <lable class="col-md-3 text-right ">手机 ：</lable>
                                                <lable class="col-md-9 line34 ">${apply.receiveMobile}</lable>
                                            </div>
                                            <c:if test="${apply.status == 1}">
                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right ">快递公司：</lable>
                                                    <lable class="col-md-9 line34 ">${apply.expressCom}</lable>
                                                </div>
                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right ">快递单号：</lable>
                                                    <lable class="col-md-9 line34 ">${apply.expressNo}</lable>
                                                </div>

                                            </c:if>

                                            <!--申请已提交的状态可以编辑
                                            <div class="form-group">
                                                <div class="col-md-9">
                                                    <a id="validateBtnPersonal"
                                                       class="validateBtnNormal btn btn-primary  btn-form">编辑</a>
                                                </div>
                                            </div>
                                            -->
                                        </form>
                                    </div>
                                </div>
                            </section>
                        </section>
                    </aside>
                </section>
            </section>
        </section>
    </section>
</section>

<div class="tips-toast"></div>
<%@include file="/inc/footer.jsp"%>
<script type="text/javascript" src='${resPrefixUrl }/js/cost/invoice.js'></script>
<!--syncpage-->
<script>

</script>

</body>

</html>

