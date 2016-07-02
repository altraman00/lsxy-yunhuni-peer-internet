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
                                                <a href="${ctx}/console/telenum/callnum/index">呼入号码管理</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a">
                                                <a href="${ctx}/console/telenum/bind/index">测试号码绑定</a>
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
                                    <c:forEach items="${pageList.result}" var="result" varStatus="s">
                                        <tr>
                                            <td scope="row">${s.index+1}</td>
                                            <td>${result.resourceTelenum.telNumber}</td>
                                            <td>${result.rentStatus}</td>
                                            <td><a href="#">${result.app.name}</a></td>
                                            <td><div name="rentExpireTime">${result.rentExpire}</div></td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </section>
                            <section class="panel panel-default yunhuni-personal">
                                <nav class='pageWrap'>
                                    <ul class="pagination">
                                        <!--设置显示的页数-->
                                        <c:set var="startPageNo" value="1"></c:set>
                                        <c:set var="endPageNo" value="5"></c:set>
                                        <c:if test="${ pageList.currentPageNo + 2 >5}">
                                            <c:set var="startPageNo" value="${ pageList.currentPageNo-2}"></c:set>
                                            <c:set var="endPageNo" value="${ pageList.currentPageNo+2}"></c:set>
                                        </c:if>
                                        <c:if test="${pageList.totalPageCount <= endPageNo}">
                                            <c:set  var="endPageNo" value="${pageList.totalPageCount}"></c:set>
                                        </c:if>


                                        <c:if test="${startPageNo > 1}">
                                            <li>
                                                <a href="${ctx}/console/telenum/callnum/index?pageNo=${startPageNo-1}" aria-label="Previous">
                                                    <span aria-hidden="true">&laquo;</span>
                                                </a>
                                            </li>
                                        </c:if>

                                        <c:forEach end="${endPageNo}" begin="${startPageNo}" varStatus="s" >
                                            <li
                                                <c:if test="${pageList.currentPageNo == s.index}">
                                                    class="active"
                                                </c:if>
                                            >
                                                <a href="${ctx}/console/telenum/callnum/index?pageNo=${s.index}">${s.index}</a>
                                            </li>
                                        </c:forEach>

                                        <c:if test="${pageList.totalPageCount>endPageNo}">
                                            <li>
                                                <a href="${ctx}/console/telenum/callnum/index?pageNo${endPageNo+1}" aria-label="Next">
                                                    <span aria-hidden="true">&raquo;</span>
                                                </a>
                                            </li>
                                        </c:if>
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



<%@include file="/inc/footer.jsp"%>
<script type="text/javascript" src='${resPrefixUrl }/js/bootstrap-datepicker/js/bootstrap-datepicker.js'> </script>
<script type="text/javascript" src='${resPrefixUrl }/js/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js'> </script>
<script type="text/javascript" src='${resPrefixUrl }/js/cost/order.js'> </script>
 <script type="text/javascript" >
     /*处理日期方法*/
     function getLocalTime(nS) {
         var date = new Date(parseInt(nS) ).toLocaleString();
         var dates = date.split(" ");
         var ymd = dates[0].split("/");
         return ymd[0]+"年"+ymd[1]+"月"+ymd[2]+"日";
     }
     /*翻译日期*/
     $(function(){
         var times =  $('div[name="rentExpireTime"]');
         for(var i = 0;i<times.length; i++){
             times[i].innerHTML = getLocalTime(times[i].innerHTML);
         }
     })
 </script>
</body>
</html>