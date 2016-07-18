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
            <aside class="bg-Green lter aside hidden-print"  id="nav"><%@include file="/inc/leftMenu.jsp"%></aside>
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
                                                <a href="${ctx}/console/statistics/specifications/call">语音呼叫</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a ">
                                                <a href="${ctx}/console/statistics/specifications/callback">语音回拨</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a">
                                                <a href="${ctx}/console/statistics/specifications/metting">会议服务</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a">
                                                <a href="${ctx}/console/statistics/specifications/ivr">IVR定制服务</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a active">
                                                <a href="${ctx}/console/statistics/specifications/code">语音验证码</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a">
                                                <a href="${ctx}/console/statistics/specifications/recording">录音服务</a>
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
                            <span class="border-left">&nbsp;语音回拨</span>
                        </div>
                        <section class="scrollable wrapper w-f">
                            <!--大图标 添加样子 application-tab -->
                            <section class="panel panel-default pos-rlt clearfix ">
                                <ul id="myTab" class="nav nav-tabs">
                                    <li class="active"><a href="#play" data-toggle="tab">语音应用1</a></li>
                                    <li><a href="#voice" data-toggle="tab">语音应用2</a></li>
                                </ul>
                                <div id="myTabContent" class="tab-content" style="">
                                    <form  method="get">
                                        <div class="row statistics_row" >
                                            <div class="col-md-1">
                                                日期
                                            </div>
                                            <div class="col-md-2">
                                                <input type="text" name="" class="form-control currentDay " value="2016-06-27" />
                                            </div>
                                            <div class="col-md-2">
                                                <button class="btn btn-primary" type="submit"> 查询</button>
                                            </div>
                                        </div>
                                    </form>
                                    <div class="tab-pane fade in active" id="play">
                                        <table class="table table-striped cost-table-history">
                                            <thead>
                                            <tr>
                                                <th colspan="6"><span class="p-money">总条数：224条</span></th>
                                            </tr>
                                            <tr>
                                                <th>发送时间</th>
                                                <th>主叫</th>
                                                <th>被叫</th>
                                                <th>状态</th>
                                                <th>挂机时间</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <tr>
                                                <td>2015-07-12 12:20:00</td>
                                                <td>13611460986</td>
                                                <td>02065612354</td>
                                                <td>正常</td>
                                                <td>2015-07-12 12:20:00</td>
                                            </tr>
                                            <tr>
                                                <td>2015-07-12 12:20:00</td>
                                                <td>13611460986</td>
                                                <td>02065612354</td>
                                                <td>正常</td>
                                                <td>2015-07-12 12:20:00</td>
                                            </tr>
                                            <tr>
                                                <td>2015-07-12 12:20:00</td>
                                                <td>13611460986</td>
                                                <td>02065612354</td>
                                                <td>正常</td>
                                                <td>2015-07-12 12:20:00</td>
                                            </tr>
                                            <tr>
                                                <td>2015-07-12 12:20:00</td>
                                                <td>13611460986</td>
                                                <td>02065612354</td>
                                                <td>正常</td>
                                                <td>2015-07-12 12:20:00</td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                    <div class="tab-pane fade" id="voice">
                                        <table class="table table-striped cost-table-history">
                                            <thead>
                                            <tr>
                                                <th colspan="6"><span class="p-money">总条数：225条</span></th>
                                            </tr>
                                            <tr>
                                                <th>发送时间</th>
                                                <th>主叫</th>
                                                <th>被叫</th>
                                                <th>状态</th>
                                                <th>挂机时间</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <tr>
                                                <td>2015-07-12 12:20:00</td>
                                                <td>13611460986</td>
                                                <td>02065612354</td>
                                                <td>正常</td>
                                                <td>2015-07-12 12:20:00</td>
                                            </tr>
                                            <tr>
                                                <td>2015-07-12 12:20:00</td>
                                                <td>13611460986</td>
                                                <td>02065612354</td>
                                                <td>正常</td>
                                                <td>2015-07-12 12:20:00</td>
                                            </tr>
                                            <tr>
                                                <td>2015-07-12 12:20:00</td>
                                                <td>13611460986</td>
                                                <td>02065612354</td>
                                                <td>正常</td>
                                                <td>2015-07-12 12:20:00</td>
                                            </tr>
                                            <tr>
                                                <td>2015-07-12 12:20:00</td>
                                                <td>13611460986</td>
                                                <td>02065612354</td>
                                                <td>正常</td>
                                                <td>2015-07-12 12:20:00</td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                    <section class="panel panel-default yunhuni-personal">
                                        <nav class="pageWrap">
                                            <ul class="pagination">
                                                <li>
                                                    <a href="#" aria-label="Previous">
                                                        <span aria-hidden="true">«</span>
                                                    </a>
                                                </li>
                                                <li class="active"><a href="#">1</a></li>
                                                <li><a href="#">2</a></li>
                                                <li><a href="#">3</a></li>
                                                <li><a href="#">4</a></li>
                                                <li><a href="#">5</a></li>
                                                <li>
                                                    <a href="#" aria-label="Next">
                                                        <span aria-hidden="true">»</span>
                                                    </a>
                                                </li>
                                            </ul>
                                        </nav>
                                    </section>
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
<script type="text/javascript" src='${resPrefixUrl }//js/statistics/find.js'> </script>
</body>
</html>

