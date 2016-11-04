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
                <aside>
                    <section class="vbox xbox">
                        <!-- 如果没有三级导航 这段代码注释-->
                        <!--<div class="head-box"><a href="#subNav" data-toggle="class:hide"> <i
                                class="fa fa-angle-left text"></i> <i class="fa fa-angle-right text-active"></i> </a>
                        </div>-->
                        <div class="wrapper header">
                            <span class="border-left">&nbsp;消息列表</span>
                        </div>
                        <section class="scrollable wrapper w-f">

                            <section class="panel panel-default pos-rlt clearfix ">
                                <table class="table table-striped cost-table-history news-table">
                                    <thead>
                                    <tr>
                                        <th width="150">消息时间</th>
                                        <th >消息内容</th>
                                        <th width="120">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${pageObj.result}" var="result" varStatus="s">
                                        <tr>
                                            <td <c:if test="${result.status==0}">class="bg"</c:if>><fmt:formatDate value="${result.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                            <td>
                                                <c:if test="${result.message.type==0 && result.message.title=='系统通知'}">
                                                    ${result.message.content}
                                                </c:if>
                                                <c:if test="${!(result.message.type==0 && result.message.title=='系统通知')}">
                                                    ${ result.message.title}
                                                </c:if>
                                            </td>
                                            <td><a href="${ctx}/console/message/account_message/detail?id=${result.id}">详情</a>
                                                <a href="${ctx}/console/message/account_message/delete?id=${result.id}&pageNo=${ pageObj.currentPageNo}">删除</a></td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </section>
                            <c:set var="pageSize" value="1"></c:set>
                            <c:set var="pageUrl" value="${ctx}/console/message/account_message/index"></c:set>
                            <%@include file="/inc/pagefooter.jsp" %>
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
<script type="text/javascript" src='${resPrefixUrl }/js/cost/history.js'> </script>
<script type="text/javascript">
    var msg = '${msg}';
    if(msg==''){}else{alert(msg);}
</script>
</body>
</html>

