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
                                                    <a href="./cost_month.html">月结账单</a>
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
                                                        <a href="./cost_invoice.html">发票信息</a>
                                                    </div>
                                                </li>
                                                <li>
                                                    <div class="aside-li-a">
                                                        <a href="./cost_invoice_record.html">发票申请</a>
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
                                    <span class="border-left">&nbsp;发票信息</span>
                                </div>
                                <div class="row m-l-none m-r-none bg-light lter">
                                    <div class="row">
                                        <form role="form" action="./index.html" method="post" class="register-form"
                                              id="costInvoiceForm">

                                            <div class="form-group">
                                                <lable class="col-md-3 text-right ">发票类型：</lable>
                                                <lable class="col-md-6  line34">企业增值税专用票</lable>
                                            </div>

                                            <div class="form-group">
                                                <lable class="col-md-3 text-right ">发票抬头：</lable>
                                                <lable class="col-md-9 line34 "> 流水行云</lable>
                                            </div>
                                            <!--企业增值税专用显示-->
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right ">纳税人识别号：</lable>
                                                <lable class="col-md-9 line34 "> 509175918471494</lable>
                                            </div>

                                            <div class="form-group">
                                                <lable class="col-md-3 text-right ">银行账号：</lable>
                                                <lable class="col-md-9 line34 ">4412345646541165165</lable>
                                            </div>


                                            <div class="form-group">
                                                <lable class="col-md-3 text-right ">开户行：</lable>
                                                <lable class="col-md-9 line34 ">广东省广州市天河区建设银行龙口东分行</lable>
                                            </div>

                                            <div class="form-group">
                                                <lable class="col-md-3 text-right ">注册地址：</lable>
                                                <lable class="col-md-9 line34 ">广东省广州市天河区羊城创意园311号B座24</lable>
                                            </div>


                                            <div class="form-group">
                                                <lable class="col-md-3 text-right ">企业电话：</lable>
                                                <lable class="col-md-9 line34 ">0202200220</lable>
                                            </div>
                                            <!--企业增值税专用显示-->

                                            <div class="form-group">
                                                <span class="hr text-label"><strong>邮寄信息:</strong></span>
                                            </div>

                                            <div class="form-group">
                                                <lable class="col-md-3 text-right ">收取地址：</lable>
                                                <lable class="col-md-9 line34 ">广东省广州市天河区羊城创意园311号B座24</lable>

                                            </div>
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right ">收件人：</lable>
                                                <lable class="col-md-9 line34 ">谭老师</lable>
                                            </div>
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right ">手机号：</lable>
                                                <lable class="col-md-9 line34 ">13611460986</lable>
                                            </div>

                                            <div class="form-group">
                                                <div class="col-md-3 text-right">
                                                    <a class=" btn btn-primary  btn-form" href="cost_invoice_edit.html">编辑</a>
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

