<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@include file="/inc/import.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <%@include file="/inc/meta.jsp" %>
</head>
<body>
<%@include file="/inc/headerNav.jsp"%>
<section class='aside-section'>
    <section class="hbox stretch">
        <!-- .aside -->
        <aside class="bg-Green  aside hidden-print include" data-include="aside" id="nav"><%@include file="/inc/leftMenu.jsp"%></aside>
        <!-- /.aside -->

        <section id="content">
            <section class="hbox stretch">
                <aside>
                    <section class="vbox xbox">
                        <!-- 如果没有三级导航 这段代码注释-->
                        <!-- <div class="head&#45;box"> <a href="#subNav" data&#45;toggle="class:hide"> <i class="fa fa&#45;angle&#45;left text"></i> <i class="fa fa&#45;angle&#45;right text&#45;active"></i> </a> </div> -->
                        <section class="scrollable wrapper w-f">
                            <section class="panel panel-default yunhuni-personal">
                                <div class="row m-l-none m-r-none bg-light lter">
                                    <div class="col-md-4 padder-v fix-padding">
                                        <div class='wrapperBox'>
                                            <span class="pull-left m-r-sm">
                                              <img src="${resPrefixUrl}/images/photo.png" width="50"/>
                                            </span>
                                            <span class="h5 block m-t-xs">
                                              <strong>余额</strong>
                                            </span>
                                            <span>
                                              <small class="text-muted text-uc account-number">${homeVO.balanceInt}</small>
                                              <small class="account-number-decimal">.${homeVO.balanceDec}</small>
                                              元
                                            </span>
                                            <div class="box-footer">
                                                <button class="btn btn-primary" >充值</button>
                                                <button class="btn btn-default" >消费情况</button>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-4 padder-v fix-padding">
                                        <div class='wrapperBox'>
                                            <span class="h5 block m-t-xs">
                                              <strong>线路状况</strong>
                                            </span>
                                            <span>
                                              当前
                                              <small class="text-muted text-uc account-number">${homeVO.lineNum}</small>
                                              线
                                            </span>
                                        </div>
                                    </div>
                                    <div class="col-md-4 padder-v fix-padding">
                                        <div class='wrapperBox'>
                                            <span class="h5 block m-t-xs">
                                              <strong>套餐剩余量</strong>
                                            </span>
                                            <div class='account-left'>
                                              <span class="w-half">
                                                <img src="${resPrefixUrl}/images/index/voice.png" alt="">
                                                语音剩余:  <small class="account-number-small">${homeVO.voiceRemain}</small> 分钟
                                              </span>
                                              <span class="w-half">
                                                <img src="${resPrefixUrl}/images/index/meeting.png" alt="">
                                                会议剩余:  <small class="account-number-small">${homeVO.conferenceRemain}</small> 分钟
                                              </span>
                                              <span class="">
                                                <img  src="${resPrefixUrl}/images/index/message.png" alt="">
                                                短信剩余:  <small class="account-number-small">${homeVO.smsRemain}</small> 条
                                              </span>
                                            </div>
                                            <div class="box-footer">
                                                <button class="btn btn-default fr" >购买流量包</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </section>
                            <section class="panel panel-default pos-rlt clearfix ">
                                <div class="sectionWrap">
                                    <header class="panel-heading">
                                        <div class="h5 border-left">开发者账号</div>
                                    </header>
                                    <div class="panel-body clearfix border-top-none">
                                        <p>
                                            REST API: ${homeVO.restApi}
                                        <span>
                                          <a href="#">API文档</a>
                                        </span>
                                        </p>
                                        <p>
                                            SecretKey ${homeVO.secretKey}
                                        <span>
                                          <a class='create_confirm' href="${ctx}/console/home/change_sk">重新生成</a>
                                        </span>
                                        </p>
                                    </div>
                                </div>
                            </section>
                            <section class="panel panel-default pos-rlt clearfix ">
                                <div class="sectionWrap">
                                    <div class="panel-body clearfix">
                                        <div class="none-app">
                                            <img src="${resPrefixUrl}/images/index/chicken.png" alt="">
                                        <span>
                                          还没创建应用，创建应用HAPPY一下吧<br/></br>
                                            <a href="">创建应用</a>
                                        </span>
                                        </div>
                                    </div>
                                </div>
                            </section>
                            <c:forEach var="app" items="${homeVO.appStateVOs}" >
                                <c:if test="${app.status eq 1}" >
                                    <section class="panel panel-default pos-rlt clearfix  app-list">
                                        <div class="sectionWrap">
                                            <header class="panel-heading">
                                                <div class="h5 border-left">${app.name}</div>
                                            </header>
                                            <div class="panel-body clearfix border-top-none">
                                                <div class="app-status-right dropdown fl">
                                          <span class="app-icon pull-left m-r-sm">
                                            <img src="${resPrefixUrl}/images/index/cp.png" width="50px"/></span>
                                          <span class="h5 block m-t-xs text-muted">APPID :
                                            <span>${app.id}</span>
                                            <small class="text-success">已上线</small>
                                          </span>
                                                    <small class="text-muted m-t-xs text-uc yhn-description">这里是描述适用商户：龙米、集美汇等3个</small>
                                                </div>
                                                <div class="app-status-left fr">
                                                    <ul class="app-status-list">
                                                        <li><img src="${resPrefixUrl}/images/index/status_1.png" alt=""> 1小时内呼叫量: <span class="number">${app.callOfHour}</span> </li>
                                                        <li><img src="${resPrefixUrl}/images/index/status_2.png" alt=""> 1天内呼叫量: <span class="number">${app.callOfDay}</span> </li>
                                                        <li><img src="${resPrefixUrl}/images/index/status_3.png" alt=""> 当天呼叫并发: <span class="number">${app.currentCall}</span> </li>
                                                    </ul>
                                                    <a href="" class="fr">详情</a>
                                                </div>
                                            </div>
                                        </div>
                                    </section>
                                </c:if>
                                <c:if test="${app.status eq 0}" >
                                    <section class="panel panel-default pos-rlt clearfix  app-list">
                                        <div class="sectionWrap">
                                            <header class="panel-heading">
                                                <div class="h5 border-left">${app.name}</div>
                                            </header>
                                            <div class="panel-body clearfix border-top-none">
                                                <div class="app-status-center dropdown fl">
                                          <span class="app-icon pull-left m-r-sm">
                                            <img src="${resPrefixUrl}/images/index/cp.png" width="50px"/></span>
                                          <span class="h5 block m-t-xs text-muted">APPID :
                                            <span>${app.id}</span>
                                            <small class="text-danger">未上线</small>
                                          </span>
                                                    <small class="text-muted m-t-xs text-uc yhn-description">这里是描述适用商户：龙米、集美汇等3个</small>
                                                </div>
                                            </div>
                                        </div>
                                    </section>
                                </c:if>
                            </c:forEach>
                        </section>
                    </section>
                </aside>
            </section>
            <a href="#" class="hide nav-off-screen-block" data-toggle="class:nav-off-screen" data-target="#nav"></a>

        </section>
    </section>
</section>
</section>
<script src="${resPrefixUrl }/js/app.v2.js"></script> <!-- Bootstrap --> <!-- App -->
<script src="${resPrefixUrl}/js/charts/flot/jquery.flot.min.js" cache="false"></script>
<script src="${resPrefixUrl}/js/bootbox.min.js"></script>
<script src="${resPrefixUrl}/js/charts/flot/demo.js" cache="false"></script>
<script src="${resPrefixUrl}/js/include.js" aysnc></script>
<script src="${resPrefixUrl}/js/include.js"></script>
<script type="text/javascript">
</script>
</body>
</html>
