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
                            <div class="head-box"><a href="#subNav" data-toggle="class:hide"> <i
                                    class="fa fa-angle-left text"></i> <i class="fa fa-angle-right text-active"></i> </a>
                            </div>
                            <section class=" w-f cost_invoice">
                                <div class="wrapper header">
                                    <span class="border-left">&nbsp;编辑发票信息</span>
                                </div>
                                <div class="row m-l-none m-r-none bg-light lter">
                                    <div class="row">
                                        <form:form role="form" action="${ctx}/console/cost/invoice_info/save" method="post" class="register-form"
                                                   id="invoiceForm">
                                            <!-- 防止表单重复提交要加这个隐藏变量 -->
                                            <input type="hidden" name="submission_token" value="${submission_token}" />

                                            <div class="form-group">
                                                <span class="hr text-label" ><strong>发票信息:</strong></span>
                                            </div>
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right">发票类型：</lable>
                                                <div class="invoice_select col-md-6 ">
                                                    <input type="radio" name="type" class="invoice_radio" value="1"
                                                           <c:if test="${invoiceInfo == null || invoiceInfo.type == 1}">checked="checked"</c:if>
                                                           data-val="1"/>个人增值税普通发票
                                                    <input type="radio" name="type" class="invoice_radio" value="2"
                                                           <c:if test="${invoiceInfo.type == 2}">checked="checked"</c:if>
                                                           data-val="2">
                                                    企业增值税普通票
                                                    <input type="radio" name="type" class="invoice_radio" value="3"
                                                           <c:if test="${invoiceInfo.type == 3}">checked="checked"</c:if>
                                                           data-val="3">
                                                    企业增值税专用票
                                                </div>
                                            </div>

                                            <div class="radiotap" >
                                                <input type="hidden" name="id" value="${invoiceInfo.id}"/>
                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right ">发票抬头：</lable>
                                                    <div class="col-md-4 ">
                                                        <input type="text" data-fv-notempty="true" placeholder=""  name="title"  value="${invoiceInfo.title}"
                                                               class="form-control input-form notEmpty"/>
                                                    </div>
                                                </div>
                                                <!--企业专用票start-->
                                                <div id="comSpecial" class="invoice-type" data-val="3">
                                                    <div class="form-group">
                                                        <lable class="col-md-3 text-right ">纳税人识别号：</lable>
                                                        <div class="col-md-4">
                                                            <input type="text" placeholder="" name="taxpayerNum"  value="${invoiceInfo.taxpayerNum}"
                                                                   class="form-control input-form notEmpty"/>
                                                        </div>
                                                    </div>

                                                    <div class="form-group">
                                                        <lable class="col-md-3 text-right ">银行账户：</lable>
                                                        <div class="col-md-4">
                                                            <input type="text" placeholder="" name="bankAccount"  value="${invoiceInfo.bankAccount}"
                                                                   class="form-control input-form notEmpty"/>
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <lable class="col-md-3 text-right ">开户行：</lable>
                                                        <div class="col-md-4">
                                                            <input type="text" placeholder="" name="bank"  value="${invoiceInfo.bank}"
                                                                   class="form-control input-form notEmpty"/>
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <lable class="col-md-3 text-right ">注册地址：</lable>
                                                        <div class="col-md-4">
                                                            <input type="text" placeholder="" name="regAddress"  value="${invoiceInfo.regAddress}"
                                                                   class="form-control input-form notEmpty"/>
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <lable class="col-md-3 text-right ">企业电话：</lable>
                                                        <div class="col-md-4">
                                                            <input type="text" placeholder="" name="phone"  value="${invoiceInfo.phone}"
                                                                   class="form-control input-form notEmpty"/>
                                                        </div>
                                                    </div>
                                                </div>
                                                <!--企业专用票end-->
                                                <div class="form-group">
                                                    <span class="hr text-label"><strong>邮寄信息:</strong></span>
                                                </div>

                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right ">收取地址：</lable>
                                                    <div class="col-md-4">
                                                        <input type="text" placeholder="" name="receiveAddress" value="${invoiceInfo.receiveAddress}"
                                                               class="form-control input-form notEmpty"/>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right ">收件人：</lable>
                                                    <div class="col-md-4">
                                                        <input type="text" placeholder="" name="receivePeople"  value="${invoiceInfo.receivePeople}"
                                                               class="form-control input-form notEmpty"/>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right">手机号：</lable>
                                                    <div class="col-md-4">
                                                        <input type="text" placeholder="" name="receiveMobile"  value="${invoiceInfo.receiveMobile}"
                                                               class="form-control input-form mobile"/>
                                                    </div>
                                                </div>

                                                <div class="form-group">
                                                    <div class="col-md-3 text-right">
                                                        <a id="validateBtn"
                                                           class=" btn btn-primary  btn-form">保存</a>
                                                    </div>
                                                </div>
                                            </div>
                                        </form:form>
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
<script>



</script>
</body>

</html>

