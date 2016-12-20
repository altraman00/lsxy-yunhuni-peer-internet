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
                                <c:if test="${status==1||status==3||status==4||status==5}">
                                <h2>个人实名认证完成</h2>
                                <div class="auth authsuccess thumbnail">
                                    <img class="fl" src="${resPrefixUrl }/images/personal/pass.png" width="190px">
                                    <div class="caption fl">
                                        <ul>
                                            <li>认证类型：个人认证</li>
                                            <li>真实姓名：${realnamePrivate.name}</li>
                                            <li>证件类型：
                                                <c:if test="${realnamePrivate.idType == '0'}">身份证</c:if>
                                                <c:if test="${realnamePrivate.idType == '1'}">护照</c:if>
                                            </li>
                                            <li>证件号码：${realnamePrivate.idNumber}</li>
                                            <li>认证时间：${createTime1}</li>
                                        </ul>
                                    </div>
                                </div>
                                </c:if>
                                <c:if test="${status==2||status==4}">
                                <h2>公司实名认证完成</h2>
                                <div class="auth authsuccess thumbnail">
                                    <img class="fl" src="${resPrefixUrl }/images/personal/pass.png" width="190px">
                                    <div class="caption fl">
                                        <ul>
                                            <li>认证类型：公司认证</li>
                                            <li>公司名称：${realnameCorp.name}</li>
                                            <li>办公地址：${realnameCorp.addr}</li>
                                            <li>所属行业：${realnameCorp.industry}</li>
                                            <li>申请人：${realnameCorp.proposer}</li>
                                            <li>证件类型：
                                                <c:if test="${realnameCorp.authType == '0'}">三证合一（一照一码）</c:if>
                                                <c:if test="${realnameCorp.authType == '1'}">三证合一</c:if>
                                                <c:if test="${realnameCorp.authType == '2'}">三证分离</c:if>
                                            </li>
                                            <c:if test="${realnameCorp.authType == '0'}">
                                                <li>统一社会信用代码：${realnameCorp.type01Prop02}</li>
                                            </c:if>
                                            <c:if test="${realnameCorp.authType == '1'}">
                                                <li>注册号：${realnameCorp.type02Prop01}</li>
                                                <li>税务登记号：${realnameCorp.type02Prop02}</li>
                                            </c:if>
                                            <c:if test="${realnameCorp.authType == '2'}">
                                                <li>税务登记号：${realnameCorp.type03Prop01}</li>
                                                <li>营业执照号：${realnameCorp.type03Prop03}</li>
                                            </c:if>
                                            <li>认证时间：${createTime2} </li>
                                        </ul>
                                    </div>
                                </div>
                                </c:if>
                                <c:if test="${status==6||status==0||status==3}">
                                    <c:if test="${status==0||status==3}"><h2>公司实名认证审核中</h2></c:if>
                                    <c:if test="${status==6}"><h2>个人实名认证审核中</h2></c:if>
                                    <div class="authfail thumbnail">
                                        <img src="${resPrefixUrl }/images/personal/audit.png" alt="...">
                                        <div class="caption">
                                            <p><strong>您的资料提交成功，等待审核...</strong> </p>
                                            <p>当前审核状态: <span class="text-success">审核中</span></p>
                                            <p>正常情况审核周期为24小时内，您也可通过客服电话查询400-002-0048</p>
                                        </div>
                                    </div>
                                </c:if>
                                <c:if test="${status==-1||status==-2||status==5}">
                                    <c:if test="${status==-1}"><h2>个人实名认证失败</h2></c:if>
                                    <c:if test="${status==5||status==-2}"><h2>公司实名认证失败</h2></c:if>
                                    <div class="authfail thumbnail">
                                        <img src="${resPrefixUrl }/images/personal/smiling_face.png" alt="...">
                                        <div class="caption">
                                            <p><strong>审核结束</strong> </p>
                                            <p>当前审核状态: <span class="text-danger">未通过</span></p>
                                             <p>审核不通过原因:
                                            <c:if test="${status==-1}">${realnamePrivate.reason}</c:if>
                                            <c:if test="${status==5||status==-2}">${realnameCorp.reason}</c:if>
                                             </p>
                                        </div>
                                    </div>
                                </c:if>
                                <c:if test="${status==1||status==5}">
                                    <div class="row">
                                        <a href="/console/account/auth/index?type=upgrade" class="btn btn-primary btn-form">升级公司认证</a>
                                        <span class="lineheight-24">升级为公司认证后，平台将为开发者开放更多的权限</span>
                                    </div>
                                </c:if>
                                <c:if test="${status==-1||status==-2}">
                                <div class="row">
                                    <a href="/console/account/auth/index?type=fail" class="btn btn-primary btn-form">重新认证</a>
                                </div>
                                </c:if>
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