<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@include file="/inc/import.jsp" %>

<!DOCTYPE html>
<html>
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
                        <div id="commonMsg">
                        </div>
                        <section class="vbox xbox">
                            <!-- 如果没有三级导航 这段代码注释-->
                            <!-- <div class="head&#45;box"> <a href="#subNav" data&#45;toggle="class:hide"> <i class="fa fa&#45;angle&#45;left text"></i> <i class="fa fa&#45;angle&#45;right text&#45;active"></i> </a> </div> -->
                            <section class="scrollable wrapper w-f">
                                <section class="panel panel-default yunhuni-personal">
                                    <div class="row m-l-none m-r-none bg-light lter">

                                        <div class="col-md-3 padder-v fix-padding">
                                            <div class='wrappernewBox green-border-top'>
                                                <span class="pull-left m-r-sm">
                                                  <img src="${resPrefixUrl}/images/index/avatar-middle.png" width="80"/>
                                                </span>
                                                <span class="h5 block m-t-xs">
                                                  <i class="iconfont icon-yue middleicon"></i>
                                                  <span class="inline-block">余额(元)</span>
                                                </span>
                                                <span class="block m-t-25">
                                                  ${homeVO.arrearage}<small class="text-muted text-uc account-money green">￥${homeVO.balanceInt}</small>
                                                  <small class="account-number-decimal green">.${homeVO.balanceDec}</small>
                                                  元
                                                </span>
                                                <div class="box-footer">
                                                    <button class="btn btn-primary"  onclick="window.location.href='${ctx}/console/cost/recharge'">充值</button>
                                                    <button class="btn btn-default" onclick="window.location.href='${ctx}/console/cost/consume'">消费情况</button>
                                                </div>
                                            </div>
                                        </div>
                                        <c:set var="appSize" value="0"></c:set>
                                        <c:if test="${homeVO.appSize != null && homeVO.appSize > 0}">
                                            <c:set var="appSize" value="${homeVO.appSize}"></c:set>
                                        </c:if>
                                        <div class="col-md-3 padder-v fix-padding">
                                            <div class='wrappernewBox blue-border-top'>
                                                <span class="h5 block m-t-xs"> <i class="iconfont icon-oc-appnum middleicon m-r-5"></i><span class="inline-block">应用数（上线数/总数)</span></span>
                                                <a class="h5 block m-t-25 text-center" href="${ctx}/console/app/list"><span class="green bigger-font-size">${homeVO.onLineApp}/${appSize}</span>个</a>
                                                <div class="box-footer">
                                                    <a class="btn btn-primary" href="${ctx}/console/app/index" >创建应用</a>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="col-md-3 padder-v fix-padding">
                                            <div class='wrappernewBox yellow-border-top'>
                                                <span class="h5 block m-t-xs"> <i class="iconfont icon-huawuliang middleicon m-r-5"></i><span class="inline-block">平均通话时长(分钟)</span></span>
                                                <span class="h5 block m-t-25 text-center ">
                                                  <span class="green bigger-font-size">
                                                      <c:if test="${homeVO.lineAverageCallTime <= 0}">--</c:if>
                                                        <c:if test="${homeVO.lineAverageCallTime > 0}">
                                                            ${homeVO.lineAverageCallTime}
                                                        </c:if>
                                                  </span>
                                                </span>
                                            </div>
                                        </div>

                                        <div class="col-md-3 padder-v fix-padding">
                                            <div class='wrappernewBox pink-border-top'>
                                                <span class="h5 block m-t-xs"> <i class="iconfont icon-oc-callrate middleicon m-r-5"></i><span class="inline-block">接通率(%)</span></span>
                                                <span class="h5 block m-t-25 text-center ">
                                                    <span class="green bigger-font-size">
                                                        <c:if test="${homeVO.lineLinkRate <= 0.0}">--</c:if>
                                                        <c:if test="${homeVO.lineLinkRate > 0.0}">
                                                            ${homeVO.lineLinkRate}
                                                        </c:if>
                                                    </span>
                                                  </span>

                                            </div>
                                        </div>



                                    </div>
                                </section>
                                <section class="panel panel-default pos-rlt clearfix ">
                                    <div class="sectionnewWrap">
                                        <header class="panel-heading">
                                            <div class="h5 ">开发者账号</div>
                                        </header>
                                        <div class="panel-body clearfix border-top-none">
                                            <p>
                                                <span class="iconfont icon-oc-api smallicon inline-block develop-icon"></span>
                                                <span class="index-key"><strong>接口API:</strong></span>
                                                <span class="index-api" id="restapi">${homeVO.restApi}</span>
                                            <span>
                                              <%--<a href="#"><i class="iconfont icon-oc-small-copy inline-block develop-icon"></i>API文档</a>--%>
                                              <a id="copyapi" data-clipboard-text="${homeVO.restApi}"><i class="iconfont icon-oc-small-copy inline-block develop-icon"></i>复制</a>
                                            </span>
                                            </p>
                                            <p>
                                                <span class="iconfont icon-oc-secrekey smallicon inline-block develop-icon"></span>
                                                <span class="index-key"><strong>密钥:</strong></span>
                                                <span class="index-api" id="secretkey">${homeVO.secretKey}</span>
                                            <span>
                                              <a class='reset_confirm'><i class="iconfont icon-resert smallicon inline-block develop-icon"></i>重新生成</a>
                                              <a id="copysecrekey" data-clipboard-text="${homeVO.secretKey}" ><i class="iconfont icon-oc-small-copy"></i>复制</a>
                                            </span>
                                            <span>
                                              <a class='tips-error tips-key'></a>
                                            </span>
                                            </p>
                                            <p>
                                                <span class="iconfont icon-oc-certid smallicon inline-block develop-icon"></span>
                                                <span class="index-key"><strong>鉴权账号:</strong></span>
                                                <span class="index-api" id="certid" >${homeVO.certId}</span>
                                            <span>
                                              <a id="copycertid" data-clipboard-text="${homeVO.certId}" ><i class="iconfont icon-oc-small-copy inline-block develop-icon"></i>复制</a>
                                            </span>
                                            </p>
                                        </div>
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
</section>
<%@include file="/inc/footer.jsp"%>
</body>
</html>