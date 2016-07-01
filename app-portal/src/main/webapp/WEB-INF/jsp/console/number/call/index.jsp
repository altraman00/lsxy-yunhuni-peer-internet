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
                        <div class="wrapper header"><span class="margin_lr"></span><span class="margin_lr border-left">&nbsp;号码管理</span>
                        </div>
                        <section class="scrollable">
                            <div class="slim-scroll">
                                <!-- nav -->
                                <nav class="hidden-xs">
                                    <ul class="nav">
                                        <li>
                                            <div class="aside-li-a active">
                                                <a href="${ctx}/console/number/call/index">呼入号码管理</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a">
                                                <a href="${ctx}/console/number/bind/index">测试号码绑定</a>
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
                        <div class="head-box"><a href="#subNav" data-toggle="class:hide"> <i
                                class="fa fa-angle-left text"></i> <i class="fa fa-angle-right text-active"></i> </a>
                        </div>
                        <div class="wrapper header">
                            <span class="border-left">&nbsp;充值订单记录</span>
                        </div>
                        <section class="scrollable wrapper w-f">
                            <section>
                                <p class="number_info">呼入号码作为应用使用IVR功能的拨入号使用，测试阶段可使用统一的测试呼入号码测试IVR功能，应用上线后可租用固定独立的呼入号码实现交互功能</p>
                            </section>

                            <section class="panel panel-default pos-rlt clearfix ">
                                <table class="table table-striped cost-table-history">
                                    <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>号码</th>
                                        <th>状态</th>
                                        <th>关联应用</th>
                                        <th>有效期</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td scope="row">1</td>
                                        <td>13971068693</td>
                                        <td>正常</td>
                                        <td><a href="#">demo001</a></td>
                                        <td>2016-06-40</td>
                                    </tr>
                                    <tr>
                                        <td scope="row">2</td>
                                        <td>13971068693</td>
                                        <td>正常</td>
                                        <td><a href="#">demo001</a></td>
                                        <td>2016-06-40</td>
                                    </tr>
                                    <tr>
                                        <td scope="row">3</td>
                                        <td>13971068693</td>
                                        <td>正常</td>
                                        <td><a href="#">demo001</a></td>
                                        <td>2016-06-40</td>
                                    </tr>
                                    <tr>
                                        <td scope="row">4</td>
                                        <td>13971068693</td>
                                        <td>正常</td>
                                        <td><a href="#">demo001</a></td>
                                        <td>2016-06-40</td>
                                    </tr>
                                    </tbody>
                                </table>
                            </section>
                            <section class="panel panel-default yunhuni-personal">
                                <nav class='pageWrap'>
                                    <ul class="pagination">
                                        <li>
                                            <a href="#" aria-label="Previous">
                                                <span aria-hidden="true">&laquo;</span>
                                            </a>
                                        </li>
                                        <li class="active"><a href="#">1</a></li>
                                        <li><a href="#">2</a></li>
                                        <li><a href="#">3</a></li>
                                        <li><a href="#">4</a></li>
                                        <li><a href="#">5</a></li>
                                        <li>
                                            <a href="#" aria-label="Next">
                                                <span aria-hidden="true">&raquo;</span>
                                            </a>
                                        </li>
                                    </ul>
                                </nav>
                            </section>

                        </section>
                    </section>
                </aside>
            </section>
            <a href="#" class="hide nav-off-screen-block" data-toggle="class:nav-off-screen" data-target="#nav"></a>
        </section>
    </section>
</section>



<script src="${resPrefixUrl }/js/app.v2.js"></script> <!-- Bootstrap --> <!-- App -->
<script src="${resPrefixUrl }/js/charts/flot/jquery.flot.min.js" cache="false"></script>
<script src="${resPrefixUrl }/js/bootbox.min.js"></script>
<script src="${resPrefixUrl }/js/charts/flot/demo.js" cache="false"></script>
<script src="${resPrefixUrl }/bower_components/bootstrapvalidator/dist/js/bootstrapValidator.min.js"></script>
<script type="text/javascript" src='${resPrefixUrl }/js/bootstrap-datepicker/js/bootstrap-datepicker.js'> </script>
<script type="text/javascript" src='${resPrefixUrl }/js/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js'> </script>
<script type="text/javascript" src='${resPrefixUrl }/js/cost/order.js'> </script>
</body>
</html>