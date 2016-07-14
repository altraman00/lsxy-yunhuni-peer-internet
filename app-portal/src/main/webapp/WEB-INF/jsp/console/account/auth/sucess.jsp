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
                        <div class="wrapper header"><span class="margin_lr"></span><span class="margin_lr border-left">&nbsp;基本资料</span>
                        </div>
                        <section class="scrollable">
                            <div class="slim-scroll">
                                <!-- nav -->
                                <nav class="hidden-xs">
                                    <ul class="nav">
                                        <li>
                                            <div class="aside-li-a">
                                                <a href="${ctx}/console/account/safety/index">安全设置</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a ">
                                                <a href="${ctx}/console/account/index">基本资料</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a active">
                                                <a href="${ctx}/console/account/auth/index">实名认证</a>
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
                        <div class="head-box"> <a href="#subNav" data-toggle="class:hide"> <i class="fa fa-angle-left text"></i> <i class="fa fa-angle-right text-active"></i> </a> </div>
                        <section class=" w-f personal-auth">
                            <div class="wrapper header">
                                <span class="border-left">&nbsp;实名认证</span>
                                <div class="auth authsuccess thumbnail">
                                    <img class="fl" src="${resPrefixUrl }/images/personal/pass.png" alt="...">
                                    <div class="caption fr">
                                        <h5>实名认证完成!</h5>
                                        <ul>
                                            <li>认证途径：云呼你平台认证</li>
                                            <li>认证账号：${userName}</li>
                                            <c:if test="${status == 1}">
                                                <li>真实姓名：${name}</li>
                                            </c:if>
                                            <c:if test="${status == 2}">
                                                <li>企业名称：${name}</li>
                                            </c:if>
                                            <li>认证时间：${time}</li>
                                        </ul>
                                    </div>
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
<%@include file="/inc/footer.jsp"%>

<script type="text/javascript" src='${resPrefixUrl }/js/personal/auth.js'></script>
</body>
</html>