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
                        <div class="head-box"><a href="#subNav" data-toggle="class:hide">
                            <i class=" iconfont icon-menu-left text"></i>
                            <i class=" iconfont icon-menu-right text-active"></i>
                        </a></div>
                         <section class=" w-f personal-auth">
                            <div class="wrapper header">
                                <span class="border-left">&nbsp;实名认证</span>
                                <div class="authfail thumbnail">
                                    <img src="${resPrefixUrl }/images/personal/audit.png" alt="...">
                                    <div class="caption">
                                        <p><strong>您的资料提交成功，等待审核...</strong> </p>
                                        <p>当前审核状态: <span class="text-success">审核中</span></p>
                                        <p>正常情况审核周期为24小时内，您也可通过客服电话查询400-002-0048</p>
                                    </div>
                                </div>
                            </div>
                            <div class="row m-l-none m-r-none bg-light lter">
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

<script type="text/javascript" src='./js/personal/auth.js'></script>
</body>
</html>