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
            <aside class="bg-Green lter aside hidden-print"  id="nav"><%@include file="/inc/leftMenu.jsp"%></aside>
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
                                            <div class="aside-li-a active">
                                                <a href="${ctx}/console/statistics/specifications/call">语音呼叫</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a">
                                                <a href="${ctx}/console/statistics/specifications/callback">语音回拨</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a">
                                                <a href="${ctx}/console/statistics/specifications/metting">会议服务</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a">
                                                <a href="${ctx}/console/statistics/specifications/ivr">IVR定制服务</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a">
                                                <a href="${ctx}/console/statistics/specifications/code">语音验证码</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a">
                                                <a href="${ctx}/console/statistics/specifications/recording">录音服务</a>
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
                            <span class="border-left">&nbsp;语音呼叫</span>
                        </div>
                        <section class="scrollable wrapper w-f">
                            <!--大图标 添加样子 application-tab -->
                            <section class="panel panel-default pos-rlt clearfix ">

                                <ul id="myTab" class="nav nav-tabs" name="appId">
                                    <c:forEach items="${appList}" var="app" varStatus="s">
                                        <li
                                                <c:if test="${app.id==appId}"> class="active"</c:if>
                                                <c:if test="${appId==null&&s.index==0}"> class="active"</c:if>
                                        >
                                            <a href="" data-toggle="tab" onclick="appSubmit('${app.id}')">${app.name}</a>
                                        </li>
                                    </c:forEach>
                                </ul>
                                <div id="myTabContent" class="tab-content" style="">
                                    <form:form action="${ctx}/console/statistics/specifications/call" method="post" id="mainForm">
                                        <div class="row statistics_row" >
                                            <input type="hidden" id="appId" name="appId" value="">
                                            <div class="col-md-1">
                                                日期
                                            </div>
                                            <div class="col-md-2">
                                                <input type="text" name="time" class="form-control currentDay " value="${time}" />
                                            </div>
                                            <div class="col-md-2">
                                                <button class="btn btn-primary" type="submit"> 查询</button>
                                            </div>
                                        </div>
                                    </form:form>
                                    <div>
                                        <table class="table table-striped cost-table-history">
                                            <thead>
                                            <tr>
                                                <th colspan="6"><span class="p-money">总消费金额(元)：100元</span></th>
                                            </tr>
                                            <tr>
                                                <th>呼叫时间</th>
                                                <th>主叫</th>
                                                <th>被叫</th>
                                                <th>时长（秒）</th>
                                                <th>消费金额（元）</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:forEach items="${pageObj.result}" var="result" varStatus="s">
                                                <tr>
                                                    <td><fmt:formatDate value="${result.callStartDt}" pattern="yyyy-MM-dd"></fmt:formatDate> </td>
                                                    <td>${result.fromNum}</td>
                                                    <td>${result.toNum}</td>
                                                    <td>${result.callTimeLong}</td>
                                                    <td>${result.cost}</td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                    <c:set var="pageUrl" value="${ctx}/console/statistics/specifications/call"></c:set>
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
</script>
</body>
</html>

