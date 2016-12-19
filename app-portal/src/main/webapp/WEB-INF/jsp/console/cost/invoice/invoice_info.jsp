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
                                                    <div class="aside-li-a active">
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
                            <section class=" w-f cost_invoice">
                                <div class="wrapper header">
                                    <span class="border-left">&nbsp;发票说明</span>
                                </div>
                                <div class="col-md-12 ">
                                    <div class="number_info">
                                            <p>1、开发票类型分为：个人增值税普通发票(100元起)和企业增值税普通发票(100元起)两种，个人增值税普通发票需要会员进行个人认证后才能开具，企业增值税普通发票则需要会员进行公司认证后才能开具</p>
                                            <p>2、官方活动赠送金额不计算在开票金额内</p>
                                            <p>3、如果是由于您的开票信息、邮寄信息填写错误导致的发票开具、邮寄错误，将不能退票重开。请您填写发票信息时仔细确认</p>
                                            <p>4、因账务结算原因，每月25号期前提交的开票申请当月受理，之后申请延期至下月受理</p>
                                    </div>
                                </div>
                                <div class="wrapper header">
                                    <span class="border-left">&nbsp;发票信息</span>
                                </div>
                                <div class="row m-l-none m-r-none bg-light lter">
                                    <div class="row">
                                        <form role="form" action="" method="post" class="register-form"
                                              id="costInvoiceForm">

                                            <div class="form-group">
                                                <lable class="col-md-3 text-right ">发票类型：</lable>
                                                <lable class="col-md-6  line34">
                                                    <c:if test="${invoiceInfo.type == 1}">个人增值税普通发票</c:if>
                                                    <c:if test="${invoiceInfo.type == 2}">企业增值税普通发票</c:if>
                                                    <%--<c:if test="${invoiceInfo.type == 3}">企业增值税专用发票</c:if>--%>
                                                </lable>
                                            </div>

                                            <div class="form-group">
                                                <lable class="col-md-3 text-right ">发票抬头：</lable>
                                                <lable class="col-md-9 line34 "> ${invoiceInfo.title}</lable>
                                            </div>
                                            <!--企业增值税专用显示-->
                                            <c:if test="${invoiceInfo.type == 3}">
                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right ">纳税人识别号：</lable>
                                                    <lable class="col-md-9 line34 "> ${invoiceInfo.taxpayerNum}</lable>
                                                </div>

                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right ">银行账户：</lable>
                                                    <lable class="col-md-9 line34 "> ${invoiceInfo.bankAccount}</lable>
                                                </div>

                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right ">开户行：</lable>
                                                    <lable class="col-md-9 line34 "> ${invoiceInfo.bank}</lable>
                                                </div>

                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right ">注册地址：</lable>
                                                    <lable class="col-md-9 line34 "> ${invoiceInfo.regAddress}</lable>
                                                </div>

                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right ">企业电话：</lable>
                                                    <lable class="col-md-9 line34 "> ${invoiceInfo.phone}</lable>
                                                </div>
                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right lineheight-24">一般纳税人认证资格证书：</lable>
                                                    <div class="col-md-9 line34">
                                                        <c:set var="defaultImgUrl" value="${(empty invoiceInfo.qualificationUrl)? (resPrefixUrl.concat('/images/index/l6.png')): (ctx.concat('/console/oss/img?uri=').concat(invoiceInfo.qualificationUrl))}" />
                                                        <img src="${defaultImgUrl}" alt="" id="imgPrev" width="100" height="80" class="recordimg" />
                                                    </div>
                                                </div>
                                            </c:if>
                                            <!--企业增值税专用显示-->

                                            <div class="form-group">
                                                <span class="hr text-label"><strong>邮寄信息:</strong></span>
                                            </div>

                                            <div class="form-group">
                                                <lable class="col-md-3 text-right ">收取地址：</lable>
                                                <lable class="col-md-9 line34 "> ${invoiceInfo.receiveAddress}</lable>

                                            </div>
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right ">收件人：</lable>
                                                <lable class="col-md-9 line34 "> ${invoiceInfo.receivePeople}</lable>
                                            </div>
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right ">手机号：</lable>
                                                <lable class="col-md-9 line34 "> ${invoiceInfo.receiveMobile}</lable>
                                            </div>

                                            <div class="form-group">
                                                <div class="col-md-3 text-right">
                                                    <a class=" btn btn-primary  btn-form" href="${ctx}/console/cost/invoice_info/edit">编辑</a>
                                                </div>
                                            </div>

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
<%@include file="/inc/footer.jsp"%>
</body>

</html>

