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
                        <div class="wrapper header"><span class="margin_lr"></span><span class="margin_lr border-left">&nbsp;呼叫中心统计</span>
                        </div>
                        <section class="scrollable">
                            <div class="slim-scroll">
                                <nav class="hidden-xs">
                                    <ul class="nav">
                                        <li>
                                            <div class="aside-li-a">
                                                <a href="${ctx}/console/statistics/callcenter/index">综合统计分析</a>
                                            </div>
                                            <div class="aside-li-a active">
                                                <a href="${ctx}/console/statistics/callcenter/detail">明细查询</a>
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
                        <div class="head-box"><a href="#subNav" data-toggle="class:hide">
                            <i class=" iconfont icon-menu-left text"></i>
                            <i class=" iconfont icon-menu-right text-active"></i>
                        </a></div>
                        <div class="wrapper header">
                            <span class="border-left">&nbsp;明细查询</span>
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
                                    <form:form action="${ctx}/console/statistics/callcenter/detail" method="post" id="mainForm">
                                        <div class="row statistics_row" >
                                            <div class="col-md-12 search-box remove-padding">
                                                <input type="hidden" id="appId" name="appId" value="${appId}">
                                                <span class="search-small-title inline-block">日期</span>
                                                <input type="text" name="startTime" class="form-control search-input inline-block currentDay "  value="${startTime}"  />
                                                <span class="search-small-title inline-block text-center">至</span>
                                                <input type="text" name="endTime" class="form-control  search-input inline-block currentDay "  value="${endTime}"  />

                                                <span class="search-title inline-block text-center">坐席</span>
                                                <input type="text" name="agent" class="form-control inline-block search-input"  value="${agent}"   placeholder="请输入坐席" />

                                                <span class="search-bigger-title inline-block text-center ">呼叫号码</span>
                                                <input type="text" name="callnum" class="form-control inline-block search-input"  value="${callnum}"  placeholder="请输入呼叫号码" />

                                                <span class="search-bigger-title inline-block text-center ">呼叫类型</span>
                                                <select name="type" class="form-control inline-block search-input">
                                                    <option  value="">全部</option>
                                                    <option <c:if test="${type == '1'}"> selected</c:if> value="1">呼入</option>
                                                    <option <c:if test="${type == '2'}"> selected</c:if> value="2">呼出</option>
                                                </select>

                                                <button class="btn btn-primary margin-left-20" type="submit"> 查询</button>
                                                <button class="btn btn-primary" type="button" onclick="download()"> 导出</button>
                                            </div>


                                        </div>
                                    </form:form>
                                    <div>
                                        <table class="table table-striped cost-table-history">
                                            <thead>
                                            <tr>
                                                <c:set var="sum_cost" value="0.000"></c:set>
                                                <c:if test="${sum!=null && sum.cost!=null}">
                                                    <c:set value="${sum.cost}" var="sum_cost"></c:set>
                                                </c:if>
                                                <c:set var="sum_num" value="0"></c:set>
                                                <c:if test="${sum!=null && sum.num!=null}">
                                                    <c:set value="${sum.num}" var="sum_num"></c:set>
                                                </c:if>
                                                <th colspan="11" style="text-align: right;"><span class="p-money">呼叫个数：${sum_num}个&nbsp;&nbsp;总消费金额：<fmt:formatNumber value="${sum_cost}" pattern="0.000"></fmt:formatNumber> 元</span></th>
                                            </tr>
                                            <tr>
                                                <th>呼叫时间</th>
                                                <th>呼叫类型</th>
                                                <th>主叫</th>
                                                <th>被叫</th>
                                                <th>坐席</th>
                                                <th>转接结果</th>
                                                <th>通话结束原因</th>
                                                <th>转人工时间</th>
                                                <th>接听时间</th>
                                                <th>通话结束时间</th>
                                                <th><span style="float:left;width: 80px" ><span style="float:right;" >消费金额</span></span></th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:forEach items="${pageObj.result}" var="result" varStatus="s">
                                                <tr>
                                                    <td><fmt:formatDate value="${result.startTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                                                    <td>
                                                        <c:if test="${result.type==1}">呼入</c:if>
                                                        <c:if test="${result.type==2}">呼出</c:if>
                                                    </td>
                                                    <td>${result.fromNum}</td>
                                                    <td>${result.toNum}</td>
                                                    <td>${result.agent}</td>
                                                    <td>
                                                        <c:if test="${result.toManualResult==1}">接听</c:if>
                                                        <c:if test="${result.toManualResult==2}">呼叫坐席失败</c:if>
                                                        <c:if test="${result.toManualResult==3}">主动放弃</c:if>
                                                        <c:if test="${result.toManualResult==4}">超时</c:if>
                                                        <c:if test="${result.toManualResult==5}">转接失败</c:if>
                                                    </td>
                                                    <td>${result.overReason}</td>
                                                    <td><fmt:formatDate value="${result.toManualTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                                                    <td><fmt:formatDate value="${result.answerTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                                                    <td><fmt:formatDate value="${result.endTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                                                    <td><span style="float:left;width: 80px" ><span style="float:right;" >￥<fmt:formatNumber value="${result.cost}" pattern="0.000"></fmt:formatNumber></span></span></td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                    <c:set var="extraParam" value="&startTime=${startTime}&endTime=${endTime}&appId=${appId}&agent=${agent}&callnum=${callnum}&type=${type}"></c:set>
                                    <c:set var="pageUrl" value="${ctx}/console/statistics/callcenter/detail"></c:set>
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
        $('#mainForm').attr('action',ctx+"/console/statistics/callcenter/download");
        $('#mainForm').submit();
        $('#mainForm').attr('action',ctx+"/console/statistics/callcenter/detail");
    }
</script>
</body>
</html>

