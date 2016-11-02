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
                                                <div class="aside-li-a ">
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
                            <div class="head-box"><a href="#subNav" data-toggle="class:hide">
                                <i class=" iconfont icon-menu-left text"></i>
                                <i class=" iconfont icon-menu-right text-active"></i>
                            </a></div>
                            <div class="wrapper header">
                                <span class="border-left">&nbsp;充值</span></div>

                            <section class="scrollable wrapper w-f">
                                <section class="panel panel-default pos-rlt clearfix ">
                                    <div class="sectionWrap">
                                        <div class="row">
                                            <div class="cost-box" >
                                                <p class="surecolor"><strong>充值订单确认</strong></p>
                                                <p><span>充值金额：</span>
                                                    <span class="cost-money">${balanceInt}<span class="floatmoney">.${balanceDec}</span> <span class="unit">元</span></span> </p>
                                                <p>订单号：${recharge.orderId}</p>
                                                <p>充值方式：${recharge.typeName}</p>
                                                <p>订单状态：${recharge.statusName}</p>
                                                <br/>
                                                <form:form action="${ctx}/console/cost/recharge/to_pay" method="post" target="_blank">
                                                    <input type="hidden" name="orderId" value="${recharge.orderId}" />
                                                    <button id="paysubmit" type="submit" class="btn btn-primary  btn-form ">付款</button>
                                                </form:form>
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
</body>
<script>
    $(document).ready(function () {
        $('#paysubmit').click(function(){
            bootbox.dialog({
                message: "将在新窗口为您打开付款界面，请按提示进行操作。",
                title: "付款提示",
                buttons: {
                    danger: {
                        label: "我已付款!",
                        className: "btn-primary",
                        callback: function() {
                            window.location.href = ctx + '/console/cost/recharge/list';
                        }
                    },
                    success: {
                        label: "查看订单",
                        className: "btn-success",
                        callback: function() {
                            window.location.href = ctx + '/console/cost/recharge/list';
                        }
                    },
                }
            });
        })
    });

</script>
</html>

