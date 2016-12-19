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
                                <span class="border-left">&nbsp;消息详情</span>
                            </div>
                            <div class="row m-l-none m-r-none bg-light lter">
                            <div class="row">
                                <div class="row">
                                    <lable class="col-md-2 text-right line34"><strong>消息标题：</strong></lable>
                                    <lable class="col-md-9 line34 "> ${message.message.title}</lable>
                                </div>
                                <div class="row">
                                <lable class="col-md-2 text-right line34"><strong>消息内容：</strong></lable>
                                <lable class="col-md-9 font16"> ${message.message.content}</lable>
                                </div>
                                <c:if test="${message.message.lineTime!=null}">
                                    <div class="row">
                                    <lable class="col-md-2 text-right line34"><strong>上线时间：</strong></lable>
                                    <lable class="col-md-9 line34 "><fmt:formatDate value="${message.message.lineTime}" pattern="yyyy-MM-dd HH:mm:ss"/></lable>
                                    </div>
                                </c:if>
                                <c:if test="${message.message.lineTime==null}">
                                    <div class="row">
                                        <lable class="col-md-2 text-right line34"><strong>通知时间：</strong></lable>
                                        <lable class="col-md-9 line34 "><fmt:formatDate value="${message.message.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></lable>
                                    </div>
                                </c:if>
                                <div class="row">
                                    <label class="col-md-2 col-md-offset-2  line34"><button class="btn btn-default" onclick="window.location.href='${ctx}/console/message/account_message/index'">返回</button></label>
                                </div>


                        </div>
                                </div>

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
</body>
</html>

