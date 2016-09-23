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
        <aside class="bg-Green lter aside hidden-print"  id="nav"><%@include file="/inc/leftMenu.jsp"%></aside>
        <!-- /.aside -->
        <section id="content">
            <section class="hbox stretch">
                <aside>
                    <script language="JavaScript" type="text/javascript">
                        var marqueeContent = new Array();
                        window.onload = function() {
                            var param = {};
                            ajaxsync(ctx + "/console/message/account_message/list", param, function (result) {
                                for (var i = 0; i < result.data.length; i++) {
                                    console.info(result.data[i].message.title);
                                    if(result.data[i].message.type=='0'&&result.data[i].message.title=='系统通知'){
                                        var sr = new String(result.data[i].message.content).substring(0,40);
                                        marqueeContent[i]="<span >"+sr+"</span>";
                                    }else{
                                        marqueeContent[i]="<span >"+result.data[i].message.title+"</span>";
                                    }
                                }
                                if(result.data.length>0){
                                    initMarquee();
                                }
                            });
                        }
//                        marqueeContent[0]="<span >云呼你有新的讯息1</span>";
//                        marqueeContent[1]="<span >云呼你有新的讯息2</span>";
//                        marqueeContent[2]="<span >云呼你有新的讯息3云呼你有新的讯息3</span>";
//                        marqueeContent[3]="<span >云呼你有新的讯息4</span>";
                        var marqueeInterval=new Array();
                        var marqueeId=0;
                        var marqueeDelay=2000;
                        var marqueeHeight=20;
                        //初始化
                        function initMarquee() {
                            var str = '';
                            if(marqueeContent.length>0){
                                 str=marqueeContent[0];
                            }


                            var html = '<div class="common-info" ><div id="marqueeBox" style="overflow:hidden;float:left;height:'+marqueeHeight+'px" onmouseover="clearInterval(marqueeInterval[0])" onmouseout="marqueeInterval[0]=setInterval(\'startMarquee()\',marqueeDelay)"><div>'+str+'</div></div><a href="'+ctx+'/console/message/account_message/index">查看详情</a><span class="close" id="common-close"></span></div>';
                            document.getElementById("commonMsg").innerHTML= html;
                            marqueeId++;
                            marqueeInterval[0]=setInterval("startMarquee()",marqueeDelay);
                            $('#common-close').click(function(){
                                $('.common-info').fadeOut()
                            });
                        }
                        //开始滚动
                        function startMarquee() {
                            var str=marqueeContent[marqueeId];
                            marqueeId++;
                            if(marqueeId>=marqueeContent.length) marqueeId=0;
                            if(document.getElementById("marqueeBox").childNodes.length==1) {
                                var nextLine=document.createElement('DIV');
                                nextLine.innerHTML=str;
                                document.getElementById("marqueeBox").appendChild(nextLine);
                            }
                            else {
                                document.getElementById("marqueeBox").childNodes[0].innerHTML=str;
                                document.getElementById("marqueeBox").appendChild(document.getElementById("marqueeBox").childNodes[0]);
                                document.getElementById("marqueeBox").scrollTop=0;
                            }
                            clearInterval(marqueeInterval[1]);
                            marqueeInterval[1]=setInterval("scrollMarquee()",20);
                        }

                        //
                        function scrollMarquee() {
                            document.getElementById("marqueeBox").scrollTop++;
                            if(document.getElementById("marqueeBox").scrollTop%marqueeHeight==(marqueeHeight-1)){
                                clearInterval(marqueeInterval[1]);
                            }
                        }



                    </script>
                    <div id="commonMsg">

                    </div>

                    <section class="vbox xbox">
                        <!-- 如果没有三级导航 这段代码注释-->
                        <!-- <div class="head&#45;box"> <a href="#subNav" data&#45;toggle="class:hide"> <i class="fa fa&#45;angle&#45;left text"></i> <i class="fa fa&#45;angle&#45;right text&#45;active"></i> </a> </div> -->
                        <section class="scrollable wrapper w-f">
                            <section class="panel panel-default yunhuni-personal">
                                <div class="row m-l-none m-r-none bg-light lter">
                                    <div class="col-md-6 padder-v fix-padding">
                                        <div class='wrapperBox'>
                                            <span class="pull-left m-r-sm">
                                              <img src="${resPrefixUrl}/images/photo.png" width="50"/>
                                            </span>
                                            <span class="h5 block m-t-xs">
                                              <strong>余额</strong>
                                            </span>
                                            <span>
                                                ${homeVO.arrearage}<small class="text-muted text-uc account-number">${homeVO.balanceInt}</small>
                                              <small class="account-number-decimal">.${homeVO.balanceDec}</small>
                                              元
                                            </span>
                                            <div class="box-footer">
                                                <a class="btn btn-primary" href="${ctx}/console/cost/recharge">充值</a>
                                                <a class="btn btn-default" href="${ctx}/console/cost/consume">消费情况</a>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-6 padder-v fix-padding">
                                        <div class='wrapperBox'>
                                            <div class="row">
                                                    <!--
                                                <div class="col-md-4 remove-padding">
                                                 <span class="h5 block m-t-xs">
                                                   <strong>线路状况</strong>
                                                 </span>
                                                 <span>
                                                  当前
                                                  <small class="text-muted text-uc account-number">${homeVO.lineNum}</small>
                                                     线
                                                 </span>
                                                </div>
                                                     -->
                                                <div class="col-md-4 remove-padding">
                                                 <span class="h5 block m-t-xs">
                                                   <strong>平均通话时长</strong>
                                                 </span>
                                                <span>
                                                    <c:if test="${homeVO.lineAverageCallTime <= 0}">--</c:if>
                                                    <c:if test="${homeVO.lineAverageCallTime > 0}">
                                                        <small class="text-muted text-uc account-number">${homeVO.lineAverageCallTime}</small>分钟
                                                    </c:if>
                                                 </span>
                                                </div>
                                                <div class="col-md-4 remove-padding">
                                                 <span class="h5 block m-t-xs">
                                                   <strong>接通率</strong>
                                                 </span>
                                                <span>
                                                    <c:if test="${homeVO.lineLinkRate <= 0.0}">--</c:if>
                                                    <c:if test="${homeVO.lineLinkRate > 0.0}">
                                                        <small class="text-muted text-uc account-number">${homeVO.lineLinkRate}</small>%
                                                    </c:if>
                                                 </span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <%--<div class="col-md-4 padder-v fix-padding">--%>
                                        <%--<div class='wrapperBox'>--%>
                                            <%--<span class="h5 block m-t-xs">--%>
                                              <%--<strong>套餐剩余量</strong>--%>
                                            <%--</span>--%>
                                            <%--<div class='account-left'>--%>
                                              <%--<span class="w-half">--%>
                                                <%--<img src="${resPrefixUrl}/images/index/voice.png" alt="">--%>
                                                <%--语音剩余:  <small class="account-number-small">${homeVO.voiceRemain}</small> 分钟--%>
                                              <%--</span>--%>
                                              <%--<span class="w-half">--%>
                                                <%--<img src="${resPrefixUrl}/images/index/meeting.png" alt="">--%>
                                                <%--会议剩余:  <small class="account-number-small">${homeVO.conferenceRemain}</small> 分钟--%>
                                              <%--</span>--%>
                                              <%--<span class="">--%>
                                                <%--<img  src="${resPrefixUrl}/images/index/message.png" alt="">--%>
                                                <%--短信剩余:  <small class="account-number-small">${homeVO.smsRemain}</small> 条--%>
                                              <%--</span>--%>
                                              <%--<span class="w-half">--%>
                                                <%--<img  src="${resPrefixUrl}/images/index/status_1.png" alt="">--%>
                                                <%--存储用量：<small class="account-number-small">${homeVO.fileUsedSize}M/${homeVO.fileTotalSize}M</small>--%>
                                              <%--</span>--%>
                                            <%--</div>--%>
                                            <%--<div class="box-footer">--%>
                                                <%--<a href="#" class="btn btn-default fr" >购买流量包</a>--%>
                                            <%--</div>--%>
                                        <%--</div>--%>
                                    <%--</div>--%>
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
                                        <%--<span>--%>
                                          <%--<a href="#">API文档</a>--%>
                                        <%--</span>--%>
                                        </p>
                                        <p>
                                            SecretKey: <span id="secretKey">${homeVO.secretKey}</span>
                                        <span>
                                          <a  class='reset_sk_confirm' >重新生成</a>
                                        </span>
                                        <span>
                                            <a class="tips-error tips-key"></a>
                                        </span>
                                        </p>
                                        <p>
                                            鉴权ID: <span >${homeVO.certId}</span>
                                        </p>
                                    </div>
                                </div>
                            </section>
                            <c:if test="${homeVO.appStateVOs == null || fn:length(homeVO.appStateVOs) == 0}">
                                <section class="panel panel-default pos-rlt clearfix ">
                                    <div class="sectionWrap">
                                        <div class="panel-body clearfix">
                                            <div class="none-app">
                                                <img src="${resPrefixUrl}/images/index/chicken.png" alt="">
                                            <span>
                                              还没创建应用，创建应用HAPPY一下吧<br/></br>
                                                <a href="${ctx}/console/app/index">创建应用</a>
                                            </span>
                                            </div>
                                        </div>
                                    </div>
                                </section>
                            </c:if>
                            <c:if test="${homeVO.appStateVOs != null && fn:length(homeVO.appStateVOs) > 0}">
                                <c:forEach var="app" items="${homeVO.appStateVOs}" >
                                    <c:if test="${app.status == 1}" >
                                        <section class="panel panel-default pos-rlt clearfix  app-list">
                                            <div class="sectionWrap">
                                                <header class="panel-heading">
                                                    <div class="h5 border-left">${app.name}</div>
                                                </header>
                                                <div class="panel-body clearfix border-top-none">
                                                    <div class="app-status-right dropdown fl">
                                                        <span class="app-icon pull-left m-r-sm">
                                                            <img src="${resPrefixUrl}/images/index/cp.png" width="50px"/>
                                                        </span>
                                                        <span class="h5 block m-t-xs text-muted">APPID :
                                                            <span>${app.id}</span>
                                                            <small class="text-success">已上线</small>
                                                        </span>
                                                        <c:if test="${not empty app.ivr}">
                                                            <span class="h5 block m-t-xs text-muted">IVR呼入号码：
                                                                <span>${app.ivr}</span>
                                                                <c:if test="${app.ivrExpire}">
                                                                    <a><i class="fa fa-exclamation-triangle tips-warning"></i> <small class="text-danger ">号码已过期，请及时续费</small></a>
                                                                </c:if>
                                                            </span>
                                                        </c:if>
                                                        <small class="text-muted m-t-xs yhn-description">${app.description}</small>
                                                    </div>
                                                    <div class="app-status-left fr">
                                                        <ul class="app-status-list">
                                                            <li><img src="${resPrefixUrl}/images/index/status_1.png" alt=""> 1小时内呼叫量: <span class="number">${app.callOfHour}</span> </li>
                                                            <li><img src="${resPrefixUrl}/images/index/status_2.png" alt=""> 1天内呼叫量: <span class="number">${app.callOfDay}</span> </li>
                                                            <li><img src="${resPrefixUrl}/images/index/status_3.png" alt=""> 当前呼叫并发: <span class="number">${app.currentCall}</span> </li>
                                                        </ul>
                                                        <a href="${ctx}/console/app/detail?id=${app.id}" class="fr">详情</a>
                                                    </div>
                                                </div>
                                            </div>
                                        </section>
                                    </c:if>
                                    <c:if test="${app.status == 2}" >
                                        <section class="panel panel-default pos-rlt clearfix  app-list">
                                            <div class="sectionWrap">
                                                <header class="panel-heading">
                                                    <div class="h5 border-left">${app.name}</div>
                                                </header>
                                                <div class="panel-body clearfix border-top-none">
                                                    <div class="app-status-center dropdown fl">
                                                        <span class="app-icon pull-left m-r-sm">
                                                            <img src="${resPrefixUrl}/images/index/cp.png" width="50px"/>
                                                        </span>
                                                        <span class="h5 block m-t-xs text-muted">APPID :
                                                            <span>${app.id}</span>
                                                            <small class="text-danger">未上线</small>
                                                        </span>
                                                        <small class="text-muted m-t-xs yhn-description">${app.description}</small>
                                                        <a href="${ctx}/console/app/detail?id=${app.id}" class="fr">详情</a>
                                                    </div>
                                                </div>
                                            </div>
                                        </section>
                                    </c:if>
                                </c:forEach>
                            </c:if>
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

<script type="text/javascript">

    // restfult api 重新生成confirm
    $('.reset_sk_confirm').on('click',function(e){
        bootbox.setLocale("zh_CN");
        bootbox.confirm("确定重新生成secretKey么", function(result) {
            if(result){
                ajaxsync(ctx + "/console/home/change_sk",null,function(response){
                    if(response.data != null){
                        $("#secretKey").html(response.data);
                    }else{
                        //errorCode errorMsg
                        $(".tips-key").html("生成失败!" + response.errorMsg)
                    }
                },"get");
            }
        });
        return false
    })

</script>



</body>
</html>
