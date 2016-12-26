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
                            <span class="border-left">&nbsp;充值</span></div>

                        <section class="scrollable wrapper w-f">
                            <section class="panel panel-default yunhuni-personal">
                                <div class="row m-l-none m-r-none bg-light lter">
                                    <div class="form-group cost-tips-box">
                                        <div class="col-md-12">
                                            <p><strong>温馨提示：</strong></p>
                                            <p>1、如您有欠费账单，充值后会优先补扣欠费账单。</p>
                                            <p>2、充值后请及时对支付订单进行结算，以免影响正常服务。</p>
                                            <p>3、若使用银行对公汇款方式充值，会员在汇款时，请在备注中注明您在壹耘平台使用的认证名称，方便对账。</p>
                                            <p>4、到账时间主要由汇款行和收款行之间的结算方式决定，正常为1个工作日，款项到账后，壹耘平台会第一时间为您充值，如有疑问请联系客服人员。</p>
                                        </div>
                                    </div>
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
                                                        <div class="cost-money"><span class="unit">${arrearage}</span>${balanceInt}.<span class="font24">${balanceDec}</span><span class="unit">元</span></div>
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
                                        <form:form action="${ctx }/console/cost/recharge/create" id="costDefulat" method="post" >
                                            <!-- 防止表单重复提交要加这个隐藏变量 -->
                                            <input type="hidden" name="submission_token" value="${submission_token}" />
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right line30">充值方式：</lable>
                                                <div class="col-md-2 input-box">
                                                    <input type="radio" name="type" class="radiovalid" value="ALIPAY" />
                                                    <img src="${resPrefixUrl }/images/zhifubao.png"/>
                                                </div>
                                                <div class="col-md-2 input-box">
                                                    <input type="radio" name="type" class="radiovalid" value="UNIONPAY"/>
                                                    <img src="${resPrefixUrl }/images/yinlian.png"/>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right line30">充值金额：</lable>
                                                <div class="col-md-2">
                                                    <input type="text" name="amount" placeholder=""
                                                           class="form-control input-form costprice " />
                                                </div>
                                                <div class="col-md-1">
                                                    <span class="line30">元</span>
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
                                            <p>收款账号：1209 1067 9910 101</p>
                                            <p>开户银行：招商银行广州分行科技园支行</p>
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

