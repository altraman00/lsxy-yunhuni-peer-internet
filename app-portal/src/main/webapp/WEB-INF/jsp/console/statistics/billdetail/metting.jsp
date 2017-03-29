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
                        <div class="wrapper header"><span class="margin_lr"></span><span class="margin_lr border-left">&nbsp;详单查询</span>
                        </div>
                        <section class="scrollable">
                            <div class="slim-scroll">
                                <nav class="hidden-xs">
                                    <ul class="nav">
                                        <li>
                                            <div class="aside-li-a ">
                                                <a href="${ctx}/console/statistics/billdetail/notify">语音通知</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a">
                                                <a href="${ctx}/console/statistics/billdetail/callback">语音回拨</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a active">
                                                <a href="${ctx}/console/statistics/billdetail/metting">语音会议</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a">
                                                <a href="${ctx}/console/statistics/billdetail/code">语音验证码</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a">
                                                <a href="${ctx}/console/statistics/billdetail/ivr">自定义IVR</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a">
                                                <a href="${ctx}/console/statistics/billdetail/ussd">闪印</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a">
                                                <a href="${ctx}/console/statistics/billdetail/sms">短信</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a">
                                                <a href="${ctx}/console/statistics/billdetail/callcenter">呼叫中心</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a">
                                                <a href="${ctx}/console/statistics/billdetail/recording">通话录音</a>
                                            </div>
                                        </li>
                                    </ul>
                                </nav>
                            </div>
                        </section>
                    </section>
                </aside>
                <aside>
                    <section class="vbox xbox">
                        <!-- 如果没有三级导航 这段代码注释-->
                        <!--<div class="head-box"><a href="#subNav" data-toggle="class:hide"> <i
                                class="fa fa-angle-left text"></i> <i class="fa fa-angle-right text-active"></i> </a>
                        </div>-->
                        <div class="wrapper header">
                            <span class="border-left">&nbsp;语音会议</span>
                        </div>
                        <section class="scrollable wrapper w-f">
                            <!--大图标 添加样子 application-tab -->
                            <section class="panel panel-default pos-rlt clearfix ">
                                <ul id="myTab" class="nav nav-tabs" name="appId">
                                    <li <c:if test="${appId=='all'}"> class="active"</c:if> >
                                        <a href="" data-toggle="tab" onclick="appSubmit('all')">全部</a>
                                    </li>
                                    <c:forEach items="${appList}" var="app" varStatus="s">
                                        <li
                                                <c:if test="${app.id==appId}"> class="active"</c:if>
                                        >
                                            <a href="" data-toggle="tab" onclick="appSubmit('${app.id}')">${app.name}</a>
                                        </li>
                                    </c:forEach>
                                </ul>
                                <div id="myTabContent" class="tab-content" style="">
                                    <form:form action="${ctx}/console/statistics/billdetail/metting" method="post" id="mainForm">
                                        <div class="row statistics_row" >
                                            <input type="hidden" id="appId" name="appId" value="${appId}">
                                            <div class="col-md-1 remove-padding-right">
                                                日期
                                            </div>
                                            <div class="col-md-2 remove-padding" style="width: 180px">
                                                <input type="text" name="start" class="form-control currentDay "  value="${start}"  />
                                            </div>
                                            <div class="col-md-1 remove-padding-right" style="width: 47px">
                                                到
                                            </div>
                                            <div class="col-md-2 remove-padding" style="width: 180px">
                                                <input type="text" name="end" class="form-control currentDay "  value="${end}"  />
                                            </div>
                                            <div class="col-md-2">
                                                <button class="btn btn-primary" type="submit"> 查询</button>
                                                <%--<button class="btn btn-primary" type="button" onclick="download()"> 导出</button>--%>
                                            </div>
                                        </div>
                                    </form:form>
                                    <div >
                                        <table class="table table-striped cost-table-history">
                                            <thead>
                                            <tr>
                                                <c:set var="sum_cost" value="0.00"></c:set>
                                                <c:if test="${sum!=null && sum.cost!=null}">
                                                    <c:set value="${sum.cost}" var="sum_cost"></c:set>
                                                </c:if>
                                                <th colspan="6"><span class="p-money">总消费金额(元)：<fmt:formatNumber value="${sum_cost}" pattern="0.000"></fmt:formatNumber> 元</span></th>
                                            </tr>
                                            <tr>
                                                <th>会议标识</th>
                                                <th>呼叫时间</th>
                                                <th>参与者</th>
                                                <th>参与类型</th>
                                                <th>时长（秒）</th>
                                                <th><span style="float:left;width: 80px" ><span style="float:right;" >消费金额</span></span></th>
                                                <th>操作</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:forEach items="${pageObj.result}" var="result" varStatus="s">
                                                <tr>
                                                    <td>${result.sessionId}</td>
                                                    <td><fmt:formatDate value="${result.callStartDt}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate> </td>
                                                    <td>
                                                        <c:if test="${result.joinType == 0}">${result.fromNum}</c:if>
                                                        <c:if test="${result.joinType == 1}">${result.toNum}</c:if>
                                                        <c:if test="${result.joinType == 2}">${result.fromNum}</c:if>
                                                    </td>
                                                    <td>
                                                        <c:if test="${result.joinType == 0}">创建</c:if>
                                                        <c:if test="${result.joinType == 1}">邀请加入</c:if>
                                                        <c:if test="${result.joinType == 2}">呼入加入</c:if>
                                                    </td>
                                                    <td>${result.costTimeLong}</td>
                                                    <td>
                                                        <span style="float:left;width: 80px" ><span style="float:right;" >
                                                        ￥<fmt:formatNumber value="${result.cost}" pattern="0.000"></fmt:formatNumber>
                                                        </span></span>
                                                    </td>
                                                    <td>
                                                        <c:if test="${result.costTimeLong != 0 && result.recording != 0}">
                                                            <a id="downVoid${result.id}" onclick="downVoid('${result.id}')" data-statu="1">录音下载</a>
                                                        </c:if>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                    <c:set var="extraParam" value="&start=${start}&end=${end}&appId=${appId}"></c:set>
                                    <c:set var="pageUrl" value="${ctx}/console/statistics/billdetail/metting"></c:set>
                                    <%@include file="/inc/pagefooter.jsp" %>
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
<%@include file="/inc/footer.jsp"%>
<script type="text/javascript" src='${resPrefixUrl }/js/bootstrap-datepicker/js/bootstrap-datepicker.js'> </script>
<script type="text/javascript" src='${resPrefixUrl }/js/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js'> </script>
<!--must-->
<script type="text/javascript" src='${resPrefixUrl }/js/statistics/find.js'> </script>
<script type="text/javascript" >
    function appSubmit(appId){
        $('#appId').val(appId);
        $('#mainForm').submit();
    }
    function download(){
        $('#mainForm').attr('action',ctx+"/console/statistics/billdetail/metting/download");
        $('#mainForm').submit();
        $('#mainForm').attr('action',ctx+"/console/statistics/billdetail/metting");
    }
    function downVoid(id) {
        var tag  = $('#downVoid'+id);
        var ststus = tag.attr('data-statu');
        if(ststus==1){
            //查询录音是否下载到oss,是下载到本地，否下载到oss显示 正在下载 ,下载oss失败，显示重试
            var params = {'${_csrf.parameterName}':'${_csrf.token}'};
            tag.html('正在下载<span class="download"></span>').attr("data-statu","2");
            ajaxsubmit("${ctx}/console/app/file/record/cdr/download/"+id,params,function(result) {
                if(result.success){
                    window.open(result.data);
                    tag.html('录音下载').attr("data-statu","1");
                }else{
                    showtoast(result.errorMsg);
                    if(result.errorCode == "0401"){
                        tag.remove();
                    }else{
                        tag.html('下载失败,请重试').attr("data-statu","1");
                    }
                }
            });
        }else if(ststus==2){
            tag.html('下载失败,请重试').attr("data-statu","1");
        }else{
            tag.html('录音下载').attr("data-statu","1");
        }
    }
</script>
</body>
</html>

