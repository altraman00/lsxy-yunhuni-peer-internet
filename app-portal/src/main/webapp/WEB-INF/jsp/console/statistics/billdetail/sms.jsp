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
                        <div class="wrapper header"><span class="margin_lr"></span><span class="margin_lr border-left">&nbsp;详单查询</span>
                        </div>
                        <section class="scrollable">
                            <div class="slim-scroll">
                                <nav class="hidden-xs">
                                    <ul class="nav">
                                        <li>
                                            <div class="aside-li-a ">
                                                <a href="${ctx}/console/statistics/billdetail/notify">语音通知</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a">
                                                <a href="${ctx}/console/statistics/billdetail/callback">语音回拨</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a">
                                                <a href="${ctx}/console/statistics/billdetail/metting">语音会议</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a">
                                                <a href="${ctx}/console/statistics/billdetail/code">语音验证码</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a">
                                                <a href="${ctx}/console/statistics/billdetail/ivr">自定义IVR</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a">
                                                <a href="${ctx}/console/statistics/billdetail/ussd">闪印</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a active">
                                                <a href="${ctx}/console/statistics/billdetail/sms">短信</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a">
                                                <a href="${ctx}/console/statistics/billdetail/callcenter">呼叫中心</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a">
                                                <a href="${ctx}/console/statistics/billdetail/recording">通话录音</a>
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
                            <span class="border-left">&nbsp;短信</span>
                        </div>
                        <section class="scrollable wrapper w-f">
                            <!--大图标 添加样子 application-tab -->
                            <section class="panel panel-default pos-rlt clearfix ">
                                <ul id="myTab" class="nav nav-tabs" name="appId" style="margin-bottom: 10px;">
                                    <li <c:if test="${appId=='all'}"> class="active"</c:if> >
                                        <a href="" data-toggle="tab" onclick="appSubmit('all')">全部</a>
                                    </li>
                                    <c:forEach items="${appList}" var="app" varStatus="s">
                                        <li
                                            <c:if test="${app.id==appId}"> class="active"</c:if>
                                        >
                                            <a href="" data-toggle="tab" onclick="appSubmit('${app.id}')">${app.name}</a>
                                        </li>
                                    </c:forEach>
                                </ul>
                                <div id="myTabContent" class="tab-content" >
                                    <form:form action="${ctx}/console/statistics/billdetail/sms" method="post" id="mainForm">
                                        <div class="row margin-bottom-20">
                                            <div class="col-md-9">
                                                <input type="radio" name="isMass" value="0" class="selectdata isMassChange" <c:if test="${isMass == '0'}">checked </c:if>>单发消息
                                                <input type="radio" name="isMass" value="1" class="isMassChange" <c:if test="${isMass == '1'}">checked </c:if> class="selectdata ml-15">群发消息
                                            </div>
                                        </div>
                                        <div class="row statistics_row" >
                                            <input type="hidden" id="msgKey" name="msgKey" value="${msgKey}">
                                            <input type="hidden" id="state" name="state" value="${state}">
                                            <input type="hidden" id="mobile1" name="mobile1" value="${mobile1}">
                                            <input type="hidden" id="appId" name="appId" value="${appId}">
                                            <div class="col-md-1">
                                                日期
                                            </div>
                                            <div class="col-md-2">
                                                <input type="text" name="start" class="form-control currentDay "  value="${start}"  />
                                            </div>
                                            <div class="col-md-1">
                                                到
                                            </div>
                                            <div class="col-md-2">
                                                <input type="text" name="end" class="form-control currentDay "  value="${end}"  />
                                            </div>

                                                <div id="isMass0" <c:if test="${isMass == '1'}">hidden</c:if>>
                                                <div class="col-md-2 mywidth">
                                                    手机号码
                                                </div>
                                                <div class="col-md-2">
                                                    <input type="text" name="mobile" class="form-control"  value="${mobile}"  />
                                                </div>
                                                </div>


                                            <div id="isMass1" <c:if test="${isMass == '0'}">hidden </c:if>>
                                                <div class="col-md-2 mywidth">
                                                    任务名称
                                                </div>
                                                <div class="col-md-2">
                                                    <input type="text" name="taskName" class="form-control"  value="${taskName}"  />
                                                </div>
                                            </div>

                                            <div class="col-md-2">
                                                <button class="btn btn-primary" type="submit"> 查询</button>
                                                <%--<button class="btn btn-primary" type="button" onclick="download()"> 导出</button>--%>
                                            </div>
                                        </div>
                                    </form:form>
                                    <div>
                                        <table class="table table-striped cost-table-history">
                                            <thead>
                                            <c:if test="${isMass==1}">
                                                <tr>
                                                    <th>序号</th>
                                                    <th>任务名称</th>
                                                    <th>任务状态</th>
                                                    <th>创建时间</th>
                                                    <th>结束时间</th>
                                                    <th>发送结果</th>
                                                    <th>操作</th>
                                                </tr>
                                            </c:if>
                                            <c:if test="${isMass==0}">
                                                <tr>
                                                    <th>序号</th>
                                                    <th>创建时间</th>
                                                    <th>手机号码</th>
                                                    <th>发送内容</th>
                                                    <th>发送结果</th>
                                                    <th>备注</th>
                                                </tr>
                                            </c:if>

                                            </thead>
                                            <tbody>
                                            <c:if test="${isMass==1}">
                                                <c:forEach items="${pageObj.result}" var="result" varStatus="s">
                                                    <tr>
                                                        <td>${result.msgKey}</td>
                                                        <td>${result.taskName}</td>
                                                        <td>
                                                            <c:if test="${result.state==1}">任务完成</c:if>
                                                            <c:if test="${result.state==0}">待处理</c:if>
                                                            <c:if test="${result.state==-1}">任务失败</c:if>
                                                        </td>
                                                        <td><fmt:formatDate value="${result.sendTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate> </td>
                                                        <td>
                                                            <c:if test="${result.state==1}">
                                                                <fmt:formatDate value="${result.lastTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>
                                                            </c:if>
                                                        </td>
                                                        <td>成功数：${result.succNum}&nbsp;失败数：${result.failNum}&nbsp;待发数：${result.pendingNum}</td>
                                                        <td><a href="#" onclick="toDetail('${result.msgKey}')">详情</a>&nbsp;&nbsp;<a href="">下载</a> </td>
                                                    </tr>
                                                </c:forEach>
                                            </c:if>
                                            <c:if test="${isMass==0}">
                                                <c:forEach items="${pageObj.result}" var="result" varStatus="s">
                                                    <tr>
                                                        <td>${result.msgKey}</td>
                                                        <td><fmt:formatDate value="${result.sendTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate> </td>
                                                        <td>${result.mobile}</td>
                                                        <td>${result.msg}</td>
                                                        <td>
                                                            <c:if test="${result.state=='1'}">发送成功</c:if>
                                                            <c:if test="${result.state=='0'}">待处理</c:if>
                                                            <c:if test="${result.state=='-1'}">发送失败</c:if>
                                                        </td>
                                                        <td>${result.reason}</td>
                                                    </tr>
                                                </c:forEach>
                                            </c:if>
                                            </tbody>
                                        </table>
                                    </div>
                                    <c:set var="extraParam" value="&start=${start}&end=${end}&appId=${appId}&isMass=${isMass}&taskName=${taskName}&mobile=${mobile}"></c:set>
                                    <c:set var="pageUrl" value="${ctx}/console/statistics/billdetail/sms"></c:set>
                                    <%@include file="/inc/pagefooter.jsp" %>
                                </div>
                                <%@include file="smg_detail.jsp"%>
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
    $(function(){
        $('.mywidth').css("width","90px");
        $('.isMassChange').click(function(){
            $('#mainForm').submit();
//            var type = $(this).val();
//            if(type == 1){
//                $('#isMass1').show();
//                $('#isMass0').hide();
//            }else{
//                $('#isMass0').show();
//                $('#isMass1').hide();
//            }
        })
    });
    function appSubmit(appId){
        $('#appId').val(appId);
        $('#mainForm').submit();
    }
    function download(){
        $('#mainForm').attr('action',ctx+"/console/statistics/billdetail/callback/download");
        $('#mainForm').submit();
        $('#mainForm').attr('action',ctx+"/console/statistics/billdetail/callback");
    }
    function downVoid(id) {
        var tag  = $('#downVoid'+id);
        var ststus = tag.attr('data-statu');
        if(ststus==1){
            //查询录音是否下载到oss,是下载到本地，否下载到oss显示 正在下载 ,下载oss失败，显示重试
            var params = {'${_csrf.parameterName}':'${_csrf.token}'};
            tag.html('正在下载<span class="download"></span>').attr("data-statu","2");
            ajaxsubmit("${ctx}/console/app/file/record/cdr/download/"+id,params,function(result) {
                if(result.success){
                    window.open(result.data);
                    tag.html('录音下载').attr("data-statu","1");
                }else{
                    showtoast(result.errorMsg);
                    tag.html('下载失败,请重试').attr("data-statu","1");
                }
            });
        }else if(ststus==2){
            tag.html('下载失败,请重试').attr("data-statu","1");
        }else{
            tag.html('录音下载').attr("data-statu","1");
        }
    }
    $(function () {
        var msgKey = '${msgKey}';
        if(msgKey!=null && msgKey.length >0) {
            $('#myTabContent2').show();
            $('#myTabContent').hide();
        }
    })
</script>
</body>
</html>

