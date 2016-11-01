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
                        <div class="wrapper header"><span class="margin_lr"></span><span class="margin_lr border-left">&nbsp;号码管理</span>
                        </div>
                        <section class="scrollable">
                            <div class="slim-scroll">
                                <!-- nav -->
                                <nav class="hidden-xs">
                                    <ul class="nav">
                                        <li>
                                            <div class="aside-li-a active">
                                                <a href="${ctx}/console/telenum/callnum/index?pageNo=1&pageSize=20">呼入号码管理</a>
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
                            <span class="border-left">&nbsp;呼入号码</span>
                        </div>
                        <section class="scrollable wrapper w-f">
                            <section>
                                <div class="col-md-12 ">
                                    <div class="number_info">
                                        <p>1、呼入号码作为应用使用IVR功能的拨入号使用，测试阶段可使用统一的测试呼入号码测试IVR功能，应用上线后可租用固定独立的呼入号码实现交互功能。</p>
                                        <p>2、余额不足以支付每月扣除的100元月租费时，相关联的号码将过期。</p>
                                        <p>3、下线应用后呼入号码自动与应用解除关联。</p>
                                        <p>4、超过有效期7天的号码自动移除。</p>
                                    </div>
                                </div>
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
                                        <th>操作</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${pageObj.result}" var="result" varStatus="s">
                                        <tr id="app-${result.id}">
                                            <td scope="row">${s.index+1}</td>
                                            <td>${result.resourceTelenum.telNumber}</td>
                                            <td>
                                                <c:if test="${result.rentExpire.time<time.time}">
                                                    <div style="color: red" >过期</div>
                                                </c:if>
                                                <c:if test="${result.rentExpire.time>time.time}">正常</c:if>
                                            </td>
                                            <td>
                                                <c:if test="${result.app==null}">无</c:if>
                                                <c:if test="${result.app!=null}">
                                                    <a href="${ctx}/console/app/detail?id=${result.app.id}">${result.app.name}</a>
                                                </c:if>
                                            </td>
                                            <td>
                                                <c:if test="${result.rentExpire.time<time.time}">
                                                    <div style="color: red" ><fmt:formatDate value="${result.rentExpire}" pattern="yyyy-MM-dd"/></div>
                                                </c:if>
                                                <c:if test="${result.rentExpire.time>time.time}">
                                                    <fmt:formatDate value="${result.rentExpire}" pattern="yyyy-MM-dd"/>
                                                </c:if>
                                            </td>
                                            <td>
                                                <c:if test="${result.app==null}"><a  onclick="release('${result.id}')">释放</a></c:if>
                                            </td>
                                        </>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </section>
                            <c:set var="pageUrl" value="${ctx}/console/telenum/callnum/index"></c:set>
                            <%@include file="/inc/pagefooter.jsp" %>
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
<script type="text/javascript">
function release(id){
    bootbox.setLocale("zh_CN");
    bootbox.confirm("您是否需要释放当前号码，如需再次使用需要重新缴纳资源租用费", function(result) {
        if(result){
            var params = {'id':id,'${_csrf.parameterName}':'${_csrf.token}'};
            ajaxsync("${ctx}/console/telenum/callnum/release",params,function(data){
                if(data.success){
                    showtoast("释放成功");
                    $('#app-'+id).remove();
                }else{
                    showtoast(data.errorMsg);
                }
            },"post");
        }else{
            showtoast("取消");
        }
    });
}
</script>
</body>
</html>