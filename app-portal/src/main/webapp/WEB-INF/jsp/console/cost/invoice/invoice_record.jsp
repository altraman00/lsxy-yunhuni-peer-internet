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
                            <div class="head-box"><a href="#subNav" data-toggle="class:hide">
                                <i class=" iconfont icon-menu-left text"></i>
                                <i class=" iconfont icon-menu-right text-active"></i>
                            </a></div>
                            <div class="wrapper header">
                                <span class="border-left">&nbsp;发票说明</span>
                            </div>
                            <section class="scrollable wrapper w-f">
                                <section class="panel panel-default yunhuni-personal">
                                    <div class="row m-l-none m-r-none bg-light lter">
                                        <div class="col-md-12 remove-padding">
                                            <div class="number_info">
                                                <p>1、开发票类型分为：个人增值税普通发票(100元起)和企业增值税普通发票(100元起)两种，个人增值税普通发票需要会员进行个人认证后才能开具，企业增值税普通发票则需要会员进行公司认证后才能开具</p>
                                                <p>2、官方活动赠送金额不计算在开票金额内</p>
                                                <p>3、如果是由于您的开票信息、邮寄信息填写错误导致的发票开具、邮寄错误，将不能退票重开。请您填写发票信息时仔细确认</p>
                                                <p>4、因账务结算原因，每月25号期前提交的开票申请当月受理，之后申请延期至下月受理</p>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <span class="hr text-label"><strong>开票信息:</strong></span>
                                        </div>
                                        <section class="panel panel-default yunhuni-personal">
                                            <div class="fix-padding">
                                                <p class="bg-success price_info price-box invoiceapply text-l">
                                                    可开具发票金额： <span class="price">${amountInt}.</span><span class="point">${amountDec}</span> 元
                                                </p>
                                            </div>
                                        </section>

                                        <section class="col-md-12 padder-v fix-padding">
                                            <div class='wrapperBox cost_month'>
                                                <div class="panel-body clearfix border-top-none ">
                                                    <form action="${ctx}/console/cost/invoice_apply/to_apply" method="get">
                                                        <div class="row">
                                                            选择开票时间：
                                                            <!--默认第一条消费记录的时间-->
                                                            <input type="text" class="form-control" readonly="readonly" name="start"
                                                                   <c:if test="${empty start}">placeholder="暂无消费信息"</c:if>
                                                                   value='${start}' id="datestart"/>到
                                                            <input type="text" class="datepicker form-control" name="end" readonly="readonly"
                                                                   data-date-end-date="0m" value=''  id="dateend"/>
                                                            <%--<a class="btn btn-primary query">查询</a>--%>
                                                            <span class="tips-error querytips"></span>
                                                        </div>
                                                        <div class="row invoiceapply">
                                                            发票类型：
                                                            <c:choose>
                                                                <c:when test='${invoiceType eq "1"}'>
                                                                    <span class="invoice-type"  data-type="1">个人增值税普通发票</span>
                                                                </c:when>
                                                                <c:when test='${invoiceType eq "2"}'>
                                                                    <span class="invoice-type"  data-type="2">企业增值税普通发票</span>
                                                                </c:when>
                                                                <%--<c:when test='${invoiceType eq "3"}'>--%>
                                                                    <%--<span class="invoice-type"  data-type="3">企业增值税专用发票</span>--%>
                                                                <%--</c:when>--%>
                                                                <c:otherwise>
                                                                    <a class="invoice-type" href="${ctx}/console/cost/invoice_info" data-type="0">您还未填写发票信息，请先填写完成</a>
                                                                </c:otherwise>
                                                            </c:choose>
                                                            <!--<span class="invoice-type"
                                                                  data-type="2">企业增值税普通发票</span>-->
                                                            <!--<span class="invoice-type" data-type="3">企业增值税专用发票</span>-->
                                                        </div>
                                                        <div class="row invoiceapply"  >
                                                            <div>开票时间：
                                                                <span id="ininvoicetime" data-start="" data-end="" ></span>
                                                            </div>
                                                        </div>
                                                        <div class="row invoiceapply">
                                                            <input type="hidden" id="invoicePrice" value="0.00"/>
                                                            <div>已选开具发票金额：
                                                            <span class="price" id="invoice-price" data-money="0">0.</span><span class="point" id="invoice-point">00</span>
                                                                元
                                                                <a id="invoice-url" ></a>
                                                                <button class="btn btn-primary float-right" id="sendinvoice"
                                                                        type="submit"
                                                                        disabled>开具发票
                                                                </button>
                                                            </div>
                                                        </div>
                                                    </form>
                                                </div>
                                            </div>
                                        </section>
                                    </div>
                                </section>
                                <!--<section class="panel panel-default yunhuni-personal">
                                    <div class="fix-padding">
                                        <p class="bg-success price_info">
                                            2016-05 消费总额  <span class="text-warning"> 2000.00</span> 元
                                        </p>
                                    </div>
                                </section>-->

                                <section class="panel panel-default pos-rlt clearfix ">
                                    <div class="form-group">
                                        <span class="hr text-label"><strong>发票申请列表:</strong></span>
                                    </div>
                                    <table class="cost-table table table-striped ">
                                        <!--<caption>消费项目</caption>-->
                                        <thead>
                                        <th>申请时间</th>
                                        <th>开票金额（元）</th>
                                        <th>发票类型</th>
                                        <th>状态</th>
                                        <th>开票抬头</th>
                                        <th>操作</th>
                                        </thead>
                                        <c:forEach items="${pageObj.result}" var="result" varStatus="s">
                                            <tr>
                                                <td>
                                                    <fmt:formatDate value="${result.applyTime}" pattern="yyyy-MM-dd HH:mm"/>
                                                </td>
                                                <td><fmt:formatNumber value="${ result.amount}" pattern="#0.00" /> </td>
                                                <td>
                                                    <c:if test="${result.type == 1}">
                                                        <span>个人增值税普通发票</span>
                                                    </c:if>
                                                    <c:if test="${result.type == 2}">
                                                        <span>企业增值税普通发票</span>
                                                    </c:if>
                                                    <%--<c:if test="${result.type == 3}">--%>
                                                        <%--<span>企业增值税专用发票</span>--%>
                                                    <%--</c:if>--%>
                                                </td>
                                                <td>
                                                    <c:if test="${result.status == 0}">
                                                        <span>申请已提交</span>
                                                    </c:if>
                                                    <c:if test="${result.status == 1}">
                                                        <c:if test="${result.expressNo != null}">
                                                            <span class="success">处理完成，发票已寄出</span>
                                                        </c:if>
                                                        <c:if test="${result.expressNo == null}">
                                                            <span>申请已提交</span>
                                                        </c:if>
                                                    </c:if>
                                                    <c:if test="${result.status == 2}">
                                                        <span class="nosuccess" data-toggle="tooltip" title="审核不通过原因:${result.reason}">
                                                            异常<i class="iconfont icon-uc_no_pass"></i>
                                                        </span>
                                                    </c:if>
                                                </td>
                                                <td>${result.title}</td>
                                                <td>
                                                    <c:if test="${result.status == 0 || result.status == 1}">
                                                        <a href="${ctx}/console/cost/invoice_apply/detail/${result.id}">查看详情</a>
                                                    </c:if>
                                                    <c:if test="${result.status == 2}">
                                                        <a class="nosuccess" href="${ctx}/console/cost/invoice_apply/edit/${result.id}">重新申请</a>
                                                    </c:if>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </table>
                                </section>
                                <c:set var="pageUrl" value="${ctx}/console/cost/invoice_apply/page"></c:set>
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

<!-- 开票详情（Modal） -->
<div class="modal fade cost-detail-modal" id="cost-detail-modal" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close"
                        data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="myModalLabel">
                    消费详情
                </h4>
            </div>
            <div class="modal-body">
                <!--内容-->
                <div class="accordion-group">
                    <div class="accordion-heading">
                        <div class="row a-title">
                            <div class="col-md-6" id="cost-detail-time"></div>
                            <div class="col-md-6 text-right">消费总金额：<span id="cost-detail-money"></span>元</div>
                        </div>

                        <div class="row title">
                            <div class="col-md-3">消费时间</div>
                            <div class="col-md-6">消费金额</div>
                            <div class="col-md-3">操作</div>
                        </div>
                        <!--列表-->
                        <div id="modal-content">
                            <div class="row c-title">
                                <div class="col-md-3">2016-01-01</div>
                                <div class="col-md-6">3000.00</div>
                                <div class="col-md-3">
                                    <a onclick="showModalDetail(this)"  data-id="collapse-1">展开</a>
                                    <span data-toggle="collapse"  href="#collapse-1" id="collapse-1-show" ></span>
                                </div>
                            </div>
                            <div id="collapse-1" class="content accordion-body collapse" style="height: 0px; ">
                                <div class="accordion-inner">
                                    <div class="row" id="collapse-1-content">
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="modal-loadding loadding"></div>
                    </div>
                </div>
                <!--内容-->

                <!--分页-->
                <div id="datatablepage"></div>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-default"
                        data-dismiss="modal">关闭
                </button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
</div>
<!-- /.modal -->

<%@include file="/inc/footer.jsp"%>
<script type="text/javascript" src='${resPrefixUrl }/js/bootstrap-datepicker/js/bootstrap-datepicker.js'></script>
<script type="text/javascript" src='${resPrefixUrl }/js/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js'></script>
<script type="text/javascript" src='${resPrefixUrl }/js/cost/invoice_record.js'></script>
<!--syncpage-->
<script type="text/javascript" src='${resPrefixUrl }/js/page.js'></script>

<script>

    function clearData(){
        $('.querytips').html('');
        //清空数据
        $('#invoice-price').html('0.').attr('data-money', '0.00');
        $('#invoice-point').html('00');
        $('#invoice-url').html('');

        $('#ininvoicetime').html("").attr('data-start',"").attr('data-end',"");

        $('#sendinvoice').attr('disabled',true);
    }

    var onclangeFlag = false;
    $('#dateend').change(function () {
        if(!onclangeFlag){
            onclangeFlag = true;
            return false;
        }
        clearData()
        //获取时间
        var starttime = $('#datestart').val();
        var endtime = $('#dateend').val();
        var tips = compareTime(starttime, endtime);
        if (tips) {
            $('.querytips').html(tips);
            return false;
        }else{
            //1号7点前不能申请上一个月的发票（因为还没统计出来）
            var sdate = endtime.split('-');
            var endDate = new Date(sdate[0],sdate[1]);//此处月份不用减1
            if(new Date().getTime() - endDate.getTime() < 7 * 60 * 60 * 1000){
                $('.querytips').html("当月1号7点前不能申请上一个月的发票");
                return false;
            }
        }

        //显示时间
        $('#ininvoicetime').html(starttime + ' 至 ' + endtime).attr('data-start',starttime).attr('data-end',endtime);
        //异步获取开局发票金额
        var price = 0.00;
        var flag = false;

        //ajax
        ajaxsync(ctx + "/console/cost/invoice_apply/apply_info",{start:starttime,end:endtime},function(response){
            if(response.success){
                price = response.data;
                flag = true;
            }else{
                showtoast(response.errorMsg?response.errorMsg:'数据异常');
            }
        },"get");

        if(!flag){
            return;
        }
        var priceArr = price.toFixed(2).split("."); //字符分割
        var priceInt = priceArr[0];
        var priceFloat = priceArr[1];

        $('#invoice-price').html(priceInt + '.').attr('data-money', price.toFixed(2));
        $('#invoice-point').html(priceFloat);
        $('#invoice-url').html('消费详情');

        var con = condition();
        if (con) {
            $('#sendinvoice').removeAttr('disabled');
        }
    });

    $('#invoice-url').click(function(){
        var flag = false;
        //标题时间段
        var starttime = $('#ininvoicetime').attr('data-start');
        var endtime   = $('#ininvoicetime').attr('data-end');
        var money =  $('#invoice-price').attr('data-money');
        //时间
        $('#cost-detail-time').html(starttime+' 至 '+ endtime);
        //消费金额
        $('#cost-detail-money').html(money);
        //获取数据总数
        var count = 11;

        ajaxsync(ctx + "/console/cost/invoice_apply/count_day_consume",{start:starttime,end:endtime},function(response){
            if(response.success){
                flag = true;
                count = response.data;
            }else{
                showtoast(response.errorMsg?response.errorMsg:'数据异常');
            }
        },"get");

        if(!flag){
            return;
        }
        //每页显示数量
        var listRow = 3;
        //显示多少个分页按钮
        var showPageCount = 4;
        //指定id，创建分页标签
        var pageId = 'datatablepage';
        //searchTable 为方法名

        var page = new Page(count,listRow,showPageCount,pageId,searchTable);
        page.show();

        $('#cost-detail-modal').modal('show');
    });


    /**
     * 分页回调方法
     * @param nowPage 当前页数
     * @param listRows 每页显示多少条数据
     * */
    var searchTable = function(nowPage,listRows){
        var starttime = $('#ininvoicetime').attr('data-start');
        var endtime   = $('#ininvoicetime').attr('data-end');
        var result = [];

        ajaxsync(ctx + "/console/cost/invoice_apply/list_day_consume",{start:starttime,end:endtime,pageNo:nowPage,pageSize:listRows},function(response){
            if(response.success){
                result = response.data;
            }else{
                showtoast(response.errorMsg?response.errorMsg:'数据异常');
            }
        },"get");


        var html ='';
        //数据列表
        for(var i = 0 ; i<result.length; i++){
            var tempDate = new Date(result[i].dt);
            var tempDataStr = tempDate.getFullYear()+"-"+(tempDate.getMonth()+1)+"-"+tempDate.getDate();
            html +='<div class="row c-title"><div class="col-md-3">'+ tempDataStr +'</div><div class="col-md-6">￥'+result[i].amongAmount.toFixed(3)+'</div><div class="col-md-3"><a onclick="showModalDetail(this)"  data-id="'+tempDataStr+'">展开</a><span data-toggle="collapse" href="#collapse-'+tempDataStr+'" id="collapse-'+tempDataStr+'-show" ></span></div></div><div id="collapse-'+tempDataStr+'" class="content accordion-body collapse" style="height: 0px; "><div class="accordion-inner"><div class="row" id="collapse-'+tempDataStr+'-content"></div></div></div>';
        }
        $('#modal-content').html('');
        $('#modal-content').html(html);
    }

    function showModalDetail(obj){
        //组装数据
        var result = [];
        var id = obj.getAttribute('data-id');
        var title = obj.innerHTML;
        if(title=='展开'){
            //ajax

            //ajax
            ajaxsync(ctx + "/console/cost/bill_day/list",{day:id},function(response){
                if(response.success){
                    result = response.data;
                }else{
                    showtoast(response.errorMsg?response.errorMsg:'数据异常');
                }
            },"get");

            var html ='';
            for(var i=0 ; i<result.length; i++){
                html+='<div class="col-md-6"><span class="col-md-6">'+ result[i].type +'：</span><div class="col-md-6">￥'+ result[i].amongAmount.toFixed(3)+'</div></div>';
            }

            document.getElementById('collapse-'+id+'-content').innerHTML=html;
            //显示
            document.getElementById('collapse-'+id+'-show').click();
            obj.innerHTML='收起';
        }else{

            document.getElementById('collapse-'+id+'-show').click();
            obj.innerHTML='展开';
        }
    }

    //显示加载
    function showloadding(){
        $('.modal-loadding').show();
    }


    //隐藏加载
    function hideladding(){
        $('.modal-loadding').hide();
    }


    function sendsubmit() {
        alert('提交表单');
    }

</script>

</body>

</html>

