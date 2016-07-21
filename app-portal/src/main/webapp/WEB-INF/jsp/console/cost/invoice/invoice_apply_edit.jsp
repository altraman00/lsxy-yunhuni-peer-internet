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
                                    <span class="border-left">&nbsp;开票申请</span>
                                </div>
                                <div class="row m-l-none m-r-none bg-light lter">
                                    <div class="row">
                                        <form:form role="form" action="${ctx}/console/cost/invoice_apply/save" method="post" class="invoice-form"
                                              id="costInvoiceForm">
                                            <!-- 防止表单重复提交要加这个隐藏变量 -->
                                            <input type="hidden" name="submission_token" value="${submission_token}" />
                                            <input type="hidden" name="id" value="${apply.id}"/>
                                            <div class="form-group">
                                                <span class=" text-label"><strong>开票信息:</strong></span>
                                            </div>
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right">开具发票金额：</lable>
                                                <lable class="col-md-9 line34"><fmt:formatNumber value="${ apply.amount}" pattern="#0.00" />元</lable>
                                            </div>
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right">开票时间：</lable>
                                                <lable class="col-md-9 line34"><fmt:formatDate value="${apply.start}" pattern="yyyy-MM"/> 至 <fmt:formatDate value="${apply.end}" pattern="yyyy-MM"/></lable>
                                                <input type="hidden" name="start" value="<fmt:formatDate value="${apply.start}" pattern="yyyy-MM"/>">
                                                <input type="hidden" name="end" value="<fmt:formatDate value="${apply.end}" pattern="yyyy-MM"/>">
                                            </div>
                                            <div class="form-group">
                                                <span class="hr text-label"><strong>发票信息:</strong> <a id="invoice-url">(查看详情)</a></span>
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
                                                <input type="hidden" name="type" value="${apply.type}">
                                            </div>
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right ">发票抬头：</lable>
                                                <lable class="col-md-4"><input name="title" placeholder="请填写发票抬头" value="${apply.title}" class="form-control input-form notEmpty" data-bv-field="notEmpty"/></lable>
                                            </div>
                                            <c:if test="${apply.type == 3}">
                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right ">纳税人识别号：</lable>
                                                    <lable class="col-md-4"> <input name="taxpayerNum" placeholder="请填写纳税人识别号" value="${apply.taxpayerNum}" class="form-control input-form notEmpty" data-bv-field="notEmpty"/></lable>
                                                </div>
                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right ">银行账户：</lable>
                                                    <lable class="col-md-4"> <input name="bankAccount" placeholder="请填写银行账户" value="${apply.bankAccount}" class="form-control input-form notEmpty" data-bv-field="notEmpty"/></lable>
                                                </div>
                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right ">开户行：</lable>
                                                    <lable class="col-md-4"><input name="bank" placeholder="请填写开户行" value="${apply.bank}" class="form-control input-form notEmpty" data-bv-field="notEmpty"/></lable>
                                                </div>
                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right ">注册地址：</lable>
                                                    <lable class="col-md-4"><input name="bank" placeholder="请填写注册地址" value="${apply.bank}" class="form-control input-form notEmpty" data-bv-field="notEmpty"/></lable>
                                                </div>
                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right ">企业电话：</lable>
                                                    <lable class="col-md-4"><input name="phone" placeholder="请填写企业电话" value="${apply.phone}" class="form-control input-form notEmpty" data-bv-field="notEmpty"/></lable>
                                                </div>
                                            </c:if>
                                            <div class="form-group">
                                                <span class="hr text-label"><strong>邮寄信息:</strong> &nbsp;<span class="grey">(临时修改不改变已保存的邮寄信息)</span></span>
                                            </div>

                                            <div class="form-group">
                                                <lable class="col-md-3 text-right ">收取地址：</lable>
                                                <div class="col-md-4">
                                                    <input type="text" name="receiveAddress" placeholder="请填写收取地址" value="${apply.receiveAddress}" class="form-control input-form notEmpty" data-bv-field="notEmpty" />
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right ">收件人：</lable>
                                                <div class="col-md-4">
                                                    <input type="text" name="receivePeople" placeholder="请填写收件人" value="${apply.receivePeople}" class="form-control input-form notEmpty" data-bv-field="notEmpty" />
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right ">手机号码：</lable>
                                                <div class="col-md-4">
                                                    <input type="text" name="receiveMobile" placeholder="请填写手机号码" value="${apply.receiveMobile}" class="form-control input-form notEmpty" data-bv-field="notEmpty" />
                                                </div>
                                            </div>


                                            <div class="form-group">
                                                <div class="col-md-3 text-right">
                                                    <button class="btn btn-primary  btn-form ">提交申请</button>
                                                </div>
                                                <div class="col-md-4 ">
                                                    <button class=" btn btn-default  btn-form">取消</button>
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
    <!-- /.modal -->
</div>

<div class="tips-toast"></div>
<%@include file="/inc/footer.jsp"%>
<script type="text/javascript" src='${resPrefixUrl }/js/cost/invoice.js'></script>
<!--syncpage-->
<script>

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


</script>


</body>

</html>

