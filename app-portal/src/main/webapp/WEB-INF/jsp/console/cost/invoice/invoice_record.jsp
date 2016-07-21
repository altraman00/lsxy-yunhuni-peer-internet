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
                            <div class="head-box"><a href="#subNav" data-toggle="class:hide"> <i class="fa fa-angle-left text"></i> <i class="fa fa-angle-right text-active"></i> </a>
                            </div>
                            <div class="wrapper header">
                                <span class="border-left">&nbsp;发票说明</span>
                            </div>
                            <section class="scrollable wrapper w-f">
                                <section class="panel panel-default yunhuni-personal">
                                    <div class="row m-l-none m-r-none bg-light lter">
                                        <div class="col-md-12 remove-padding">
                                            <div class="number_info">
                                                <p>*开发票类型分为：个人增值税普通发票(100元起)，企业增值税普通发票(100元起)，企业增值税专用发票(1000元起)，共三种个人增值税普通发票与企业增值税普通发票的发票抬头修改后可直接保存，企业增值税专用票则需要用户进行企业认证后才能开具</p>
                                                <p>*官方活动赠送金额不计算在开票金额内</p>
                                                <p>*如果是由于您的开票信息、邮寄信息填写错误导致的发票开具、邮寄错误，将不能退票重开。请您填写发票信息时仔细</p>
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
                                                    <form action="" method="post">
                                                        <div class="row">
                                                            选择开票时间：
                                                            <!--默认第一条消费记录的时间-->
                                                            <input type="text" class="form-control" readonly="readonly"
                                                                   value='${start}' />到
                                                            <input type="text" class="datepicker form-control"
                                                                   data-date-end-date="0m" value=''  id="dateend"/>
                                                            <a class="btn btn-primary query">查询</a>
                                                            <span class="tips-error querytips"></span>
                                                        </div>
                                                        <div class="row invoiceapply" style="display: none">
                                                            发票类型：
                                                            <!--<a class="invoice-type" href="cost_invoice.html" data-type="0">您还未填写发票信息，请先填写完成</a>-->
                                                            <!--<span class="invoice-type" href="cost_invoice.html"
                                                                  data-type="2">企业增值税普通票</span>-->
                                                        <span class="invoice-type" href="cost_invoice.html"
                                                              data-type="1">个人增值税普通发票</span>
                                                            <!--<span class="invoice-type" href="cost_invoice.html" data-type="3">企业增值税专用票</span>-->
                                                        </div>
                                                        <div class="row invoiceapply"  >
                                                            <div>开票时间：
                                                                <span id="ininvoicetime"> </span>
                                                            </div>
                                                        </div>
                                                        <div class="row invoiceapply">
                                                            <div>已选开具发票金额：
                                                            <span class="price" id="invoice-price"
                                                                  data-money="0">0.</span><span class="point"
                                                                                                id="invoice-point">00</span>
                                                                元
                                                                <a id="invoice-url"></a>
                                                                <button class="btn btn-primary float-right" id="sendinvoice"
                                                                        type="submit"
                                                                        disabled>开发发票
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
                                                    <fmt:formatDate value="${result.createTime}" pattern="yyyy-MM-dd HH:mm"/>
                                                </td>
                                                <td><fmt:formatNumber value="${ result.amount}" pattern="#0.00" /> </td>
                                                <td>
                                                    <c:if test="${result.type == 1}">
                                                        <span>个人增值税普通发票</span>
                                                    </c:if>
                                                    <c:if test="${result.type == 2}">
                                                        <span>企业增值税普通票</span>
                                                    </c:if>
                                                    <c:if test="${result.type == 3}">
                                                        <span>企业增值税专用票</span>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <c:if test="${result.status == 0}">
                                                        <span>申请已提交</span>
                                                    </c:if>
                                                    <c:if test="${result.status == 1}">
                                                        <span class="success">处理完成，发票已寄出</span>
                                                    </c:if>
                                                    <c:if test="${result.status == 2}">
                                                        <span class="nosuccess" data-toggle="tooltip" title="${result.remark}">
                                                            异常
                                                        </span>
                                                    </c:if>
                                                </td>
                                                <td>${result.title}</td>
                                                <td><a href="cost_invoice_detail.html">查看详情</a></td>
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
                    开票详情
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


    $('.query').click(function () {
        $('.querytips').html('');
        //获取时间
        var starttime = $('#datestart').val();
        var endtime = $('#dateend').val();
        var tips = compareTime(starttime, endtime);
        if (tips) {
            $('.querytips').html(tips);
            return false;
        }
        //清空数据
        $('#invoice-price').html('0.').attr('data-money', '0');
        $('#invoice-point').html('00');
        $('#invoice-url').html('');


        //显示时间
        $('#ininvoicetime').html(starttime + ' 至 ' + endtime);

        //异步获取开局发票金额
        var price = "100.01";


        var priceInt = parseInt(price);
        var priceFloat = parseFloat(price).toFixed(2);
        var strs = new Array(); //定义一数组
        strs = price.split("."); //字符分割
        for (i = 0; i < strs.length; i++) {
            strs[i];
        }
        $('#invoice-price').html(priceInt + '.').attr('data-money', priceInt);
        if (strs[1] != '') {
            priceFloat = strs[1];
        }

        $('#invoice-point').html(priceFloat);

        $('#invoice-url').html('查看详情');

        var con = condition();
        if (con) {
            $('#sendinvoice').removeAttr('disabled');
        }


    });


    $('#invoice-url').click(function(){
        //列表加载数据
        var html ='';
        //标题时间段
        var starttime = $('#datestart').val();
        var endtime   = $('#dateend').val();
        var money =  100;
        //时间
        $('#cost-detail-time').html(starttime+' 至 '+ endtime);
        //消费金额
        $('#cost-detail-money').html(100);

        //获取数据总数
        var count = 11;
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
    var searchTable = function(nowPage,listRows)
    {

        var data = [
            ['2016-06-07', '1000.00'],
            ['2016-06-06', '1000.00'],
            ['2016-06-05', '1000.00']
        ];
        var html ='';
        //数据列表

        for(var i = 0 ; i<data.length; i++){
            html +='<div class="row c-title"><div class="col-md-3">'+data[i][0]+'</div><div class="col-md-6">'+data[i][1]+'</div><div class="col-md-3"><a onclick="showModalDetail(this)"  data-id="collapse-'+i+'">展开</a><span data-toggle="collapse" href="#collapse-'+i+'" id="collapse-'+i+'-show" ></span></div></div><div id="collapse-'+i+'" class="content accordion-body collapse" style="height: 0px; "><div class="accordion-inner"><div class="row" id="collapse-'+i+'-content"></div></div></div>';
        }
        $('#modal-content').html('');
        $('#modal-content').html(html);
    }


    function showModalDetail(obj){
        var id = obj.getAttribute('data-id');
        var title = obj.innerHTML;
        if(title=='展开'){
            //ajax

            //组装数据
            var d = [
                { title : '单项外呼', price :'120元' },
                { title : '双向呼叫', price :'120元' },
                { title : '电话会议', price :'120元' },
                { title : '电话接入IVR', price :'120元' },
                { title : 'IVR外呼放音', price :'120元' },
                { title : '短信', price :'120元' },
                { title : '电话通知', price :'120元' },
                { title : '通话录音', price :'120元' },
                { title : 'IVR功能费', price :'120元' },
                { title : 'IVR号码租用费', price :'120元' },
                { title : '录音文件存储', price :'120元' }
            ];
            var html ='';
            for(var i=0 ; i<d.length; i++){
                html+='<div class="col-md-6"><span class="col-md-6">'+d[i]['title']+'：</span><div class="col-md-6">'+d[i]['price']+'</div></div>';
            }

            document.getElementById(id+'-content').innerHTML=html;
            //显示
            document.getElementById(id+'-show').click();
            obj.innerHTML='收起';
        }else{

            document.getElementById(id+'-show').click();
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

