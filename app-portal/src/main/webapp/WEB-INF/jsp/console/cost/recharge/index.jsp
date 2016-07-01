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
                                            <div class="aside-li-a ">
                                                <a href="${ctx}/console/cost/consume">消费记录</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a active">
                                                <a href="${ctx}/console/cost/recharge">充值</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a">
                                                <a href="./cost_order.html">充值订单</a>
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
                                                <div class="aside-li-a">
                                                    <a href="./cost_invoice.html">发票信息</a>
                                                </div>
                                            </li>
                                            <li>
                                                <div class="aside-li-a">
                                                    <a href="./cost_invoice_record.html">发票记录</a>
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
                            <span class="border-left">&nbsp;充值</span></div>

                        <section class="scrollable wrapper w-f">
                            <section class="panel panel-default yunhuni-personal">
                                <div class="row m-l-none m-r-none bg-light lter">
                                    <div class="col-md-12 padder-v fix-padding">
                                        <div class='wrapperBox'>
                                            <div class="panel-body clearfix border-top-none personal-base">
                                                <div class="row ">
                                                    <div class="col-md-12 remove-padding width-130">
                                                        <strong>余额</strong>
                                                    </div>
                                                </div>
                                                <div class="row ">
                                                    <div class="col-md-12 remove-padding ">
                                                        <div class="cost-money">998.<span class="font24">00</span><span class="unit">元</span></div>
                                                    </div>
                                                </div>

                                            </div>
                                        </div>
                                    </div>

                                </div>
                            </section>

                            <section class="panel panel-default pos-rlt clearfix ">
                                <div class="sectionWrap">
                                    <div class="cost-rechage cost-box">
                                        <form:form action="${ctx }/console/cost/recharge/sure" id="costDefulat" method="post" >
                                            <!-- 防止表单重复提交要加这个隐藏变量 -->
                                            <input type="hidden" name="submission_token" value="${submission_token}" />

                                            <div class="form-group">
                                                <lable class="col-md-3 text-right line30">充值方式：</lable>
                                                <div class="col-md-2 input-box">
                                                    <input type="radio" name="modepay" class="radiovalid" value="zhifubao" />
                                                    <img src="${resPrefixUrl }/images/zhifubao.png"/>
                                                </div>
                                                <div class="col-md-2 input-box">
                                                    <input type="radio" name="modepay" class="radiovalid" value="yinlian"/>
                                                    <img src="${resPrefixUrl }/images/yinlian.png"/>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right line30">充值金额：</lable>
                                                <div class="col-md-2">
                                                    <input type="text" name="" placeholder=""
                                                           class="form-control input-form costprice " />
                                                </div>
                                                <div class="col-md-1">
                                                    <span class="line30">元</span>
                                                </div>
                                            </div>
                                            <div class="form-group cost-tips-box">
                                                <div class="col-md-12">
                                                    <p><strong>温馨提示：</strong></p>
                                                    <p>1、如您有欠费账单，充值后会优先补扣欠费账单。</p>
                                                    <p>2、充值后请及时对支付订单进行结算，以免影响正常服务。</p>
                                                </div>
                                            </div>
                                            <div class="form-group ">
                                                <div class="col-md-1 remove-padding">
                                                    <button type="submit" class="btn btn-primary  btn-form ">充值</button>
                                                </div>
                                            </div>
                                        </form:form>
                                    </div>
                                </div>
                            </section>
                            <section class="panel panel-default pos-rlt clearfix ">
                                <div class="sectionWrap">
                                    <div class="row">
                                        <div class="cost-box">
                                            <p><strong>银行对公汇款</strong></p>
                                            <p>开户名称：广州流水行云科技有限公司</p>
                                            <p>收款账号：62260548778786548</p>
                                            <p>开户银行：招商银行</p>
                                        </div>
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
</section>
<%@include file="/inc/footer.jsp"%>
<script src="${resPrefixUrl }/js/cost/recharge.js"></script><!--must-->
</body>
</html>

