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
                                    <span class="border-left">&nbsp;开票申请</span>
                                </div>
                                <div class="row m-l-none m-r-none bg-light lter">
                                    <div class="row">
                                        <form:form role="form" action="${ctx}/console/cost/invoice_apply/save" method="post" class="invoice-form" enctype="multipart/form-data"
                                              id="invoiceForm">
                                            <!-- 防止表单重复提交要加这个隐藏变量 -->
                                            <input type="hidden" name="submission_token" value="${submission_token}" />
                                            <input type="hidden" name="id" value="${apply.id}"/>
                                            <div class="form-group">
                                                <span class=" text-label"><strong>开票信息:</strong><a id="invoice-url">(消费详情)</a></span>
                                            </div>
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right">开具发票金额：</lable>
                                                <lable class="col-md-9 line34"><span id="invoice-price" data-money="<fmt:formatNumber value="${ apply.amount}" pattern="#0.00" />"><fmt:formatNumber value="${ apply.amount}" pattern="#0.00" />元</span></lable>
                                            </div>
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right">开票时间：</lable>
                                                <lable class="col-md-9 line34"><span id="ininvoicetime" data-start="<fmt:formatDate value="${apply.start}" pattern="yyyy-MM"/>" data-end="<fmt:formatDate value="${apply.end}" pattern="yyyy-MM"/>" >
                                                    <fmt:formatDate value="${apply.start}" pattern="yyyy-MM"/> 至 <fmt:formatDate value="${apply.end}" pattern="yyyy-MM"/>
                                                </span></lable>
                                                <input type="hidden" name="start" value="<fmt:formatDate value="${apply.start}" pattern="yyyy-MM"/>">
                                                <input type="hidden" name="end" value="<fmt:formatDate value="${apply.end}" pattern="yyyy-MM"/>">
                                            </div>
                                            <div class="form-group">
                                                <span class="hr text-label"><strong>发票信息:</strong> &nbsp;<span class="grey">(临时修改不改变已保存的邮寄信息)</span></span>
                                            </div>


                                            <div class="form-group">
                                                <lable class="col-md-3 text-right ">发票类型：</lable>
                                                <lable class="col-md-6  line34">
                                                    <c:if test="${apply.type == 1}">
                                                        个人增值税普通发票
                                                    </c:if>
                                                    <c:if test="${apply.type == 2}">
                                                        企业增值税普通发票
                                                    </c:if>
                                                    <%--<c:if test="${apply.type == 3}">--%>
                                                        <%--企业增值税专用发票--%>
                                                    <%--</c:if>--%>
                                                </lable>
                                                <input id="type" type="hidden" name="type" value="${apply.type}">
                                            </div>
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right ">发票抬头：</lable>
                                                <lable class="col-md-4"><input name="title" placeholder="请填写发票抬头" value="${apply.title}" class="form-control input-form limit50" data-bv-field="notEmpty"/></lable>
                                            </div>
                                            <c:if test="${apply.type == 3}">
                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right ">纳税人识别号：</lable>
                                                    <lable class="col-md-4"> <input name="taxpayerNum" placeholder="请填写纳税人识别号" value="${apply.taxpayerNum}" class="form-control input-form limit32" data-bv-field="notEmpty"/></lable>
                                                </div>
                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right ">银行账户：</lable>
                                                    <lable class="col-md-4"> <input name="bankAccount" placeholder="请填写银行账户" value="${apply.bankAccount}" class="form-control input-form limit32" data-bv-field="notEmpty"/></lable>
                                                </div>
                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right ">开户行：</lable>
                                                    <lable class="col-md-4"><input name="bank" placeholder="请填写开户行" value="${apply.bank}" class="form-control input-form limit32" data-bv-field="notEmpty"/></lable>
                                                </div>
                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right ">注册地址：</lable>
                                                    <lable class="col-md-4"><input name="regAddress" placeholder="请填写注册地址" value="${apply.regAddress}" class="form-control input-form limit100" data-bv-field="notEmpty"/></lable>
                                                </div>
                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right ">企业电话：</lable>
                                                    <lable class="col-md-4"><input name="phone" placeholder="请填写企业电话" value="${apply.phone}" class="form-control input-form limit32" data-bv-field="notEmpty"/></lable>
                                                </div>
                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right lineheight-24">一般纳税人认证资格证书：</lable>
                                                    <div class="col-md-4">
                                                        <c:set var="defaultImgUrl" value="${(empty apply.qualificationUrl)? (resPrefixUrl.concat('/images/index/l6.png')): (ctx.concat('/console/oss/img?uri=').concat(apply.qualificationUrl))}" />
                                                        <input type="hidden" id="qualificationUrl"  name="qualificationUrl" value="${apply.qualificationUrl}" data-url="${defaultImgUrl}">
                                                        <input type="file" class="form-control-file input-form  limitImageFile"  id="uploadfile"  name="uploadfile">将原件或盖章的复印件扫描、拍照后上传，文件支持2M以内的jpg、jpeg、gif、png、bmp。
                                                        <img src="${defaultImgUrl}" alt="" id="imgPre" width="100" height="80" class="recordimg" />
                                                    </div>
                                                </div>
                                            </c:if>
                                            <div class="form-group">
                                                <span class="hr text-label"><strong>邮寄信息:</strong> &nbsp;<span class="grey">(临时修改不改变已保存的邮寄信息)</span></span>
                                            </div>

                                            <div class="form-group">
                                                <lable class="col-md-3 text-right ">收取地址：</lable>
                                                <div class="col-md-4">
                                                    <input type="text" name="receiveAddress" placeholder="请填写收取地址" value="${apply.receiveAddress}" class="form-control input-form limit100" data-bv-field="notEmpty" />
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right ">收件人：</lable>
                                                <div class="col-md-4">
                                                    <input type="text" name="receivePeople" placeholder="请填写收件人" value="${apply.receivePeople}" class="form-control input-form limit32" data-bv-field="notEmpty" />
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right ">手机号码：</lable>
                                                <div class="col-md-4">
                                                    <input type="text" name="receiveMobile" placeholder="请填写手机号码" value="${apply.receiveMobile}" class="form-control input-form mobile" data-bv-field="notEmpty" />
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <div class="col-md-3 text-right">
                                                    <button class="btn btn-primary  btn-form " id="validateBtn">提交申请</button>
                                                </div>
                                                <div class="col-md-4 ">
                                                    <a href="${ctx}/console/cost/invoice_apply/page" class=" btn btn-default  btn-form">取消</a>
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
    <!-- /.modal -->
</div>

<%@include file="/inc/footer.jsp"%>
<script type="text/javascript" src='${resPrefixUrl }/js/cost/invoice.js'></script>
<script type="text/javascript" src='${resPrefixUrl }/js/page.js'></script>
<!--syncpage-->
<script>

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

//    function showImage()
//    {
//        // 获取文件路径
//        var path = document.getElementById('uploadfile').value;
//        // 显示文件路径
//        //document.getElementById('imgName').innerHTML = path;
//        // 创建 img
//        var img = document.createElement('img');
//        // 载入图像
//        img.src = path;
//        // 插入图像到页面中
//        document.getElementById('imgPrev').appendChild(img);
//    }

    function bfSubmit(){
        var flag = false;
        var type = $("#type").val();
        ajaxsync(ctx + "/console/account/auth/is_real_auth",null,function(response){
            if(response.success){
                if(response.data == 1){
                    if(type == response.data){
                        flag = true;
                    }else{
                        showtoast('个人实名认证的用户不能进行企业发票申请');
                    }
                }else if(response.data == 2){
                    flag = true;
                }else{
                    showtoast('请先进行实名认证');
                }
            }else{
                showtoast(response.errorMsg?response.errorMsg:'数据异常');
            }
        },"get");

        return flag;
    }
</script>


</body>

</html>

