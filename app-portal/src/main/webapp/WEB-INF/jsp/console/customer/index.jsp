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
            <aside class="bg-Green lter aside hidden-print" data-include="aside" id="nav"><%@include file="/inc/leftMenu.jsp"%></aside>
            <!-- /.aside -->

        <section id="content">
            <section class="hbox stretch">
                <aside>
                    <section class="vbox xbox">
                        <!-- 如果没有三级导航 这段代码注释-->
                        <!--<div class="head-box"> <a href="#subNav" data-toggle="class:hide"> <i class="fa fa-angle-left text"></i> <i class="fa fa-angle-right text-active"></i> </a> </div>-->
                        <section class=" w-f personal-auth">
                            <div class="wrapper header">
                                <span class="border-left">&nbsp;意见反馈</span>
                            </div>
                            <div class="row m-l-none m-r-none bg-light lter">
                                <div class="row">
                                    <form:form role="form" action="${ctx}/console/customer/edit" method="post" class="register-form" id="customForm" >
                                        <p class="number_info ">
                                            非常感谢您提供宝贵的意见和建议，我们将全力为您提供最优质的服务
                                        </p>
                                        <div class="form-group">
                                            <lable class="col-md-3 text-right">反馈意见：</lable>
                                        </div>
                                        <div class="form-group height-220" >
                                            <div class="col-md-5 remove-padding" >
                                                <textarea name="content" id="" cols="30" rows="10" class="form-control input-form customcontent"></textarea>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="col-md-3 text-left remove-padding">
                                                <button type="submit" class=" btn btn-primary  btn-form">提交</button>
                                            </div>
                                        </div>
                                    </form:form>
                                </div>
                            </div>
                        </section>
                    </section>
                </aside>
            </section>
        </section>
    </section>
</section>
</section>
<script src="${resPrefixUrl }/js/app.v2.js"></script> <!-- Bootstrap --> <!-- App -->
<script src="${resPrefixUrl }/js/charts/flot/jquery.flot.min.js" cache="false"></script>
<script src="${resPrefixUrl }/js/bootbox.min.js"></script>
<script src="${resPrefixUrl }/js/charts/flot/demo.js" cache="false"></script>
<script src="${resPrefixUrl }/bower_components/bootstrapvalidator/dist/js/bootstrapValidator.min.js"></script>
<script type="text/javascript" src='${resPrefixUrl }/js/custom/index.js'></script>
<%@include file="/inc/footer.jsp"%>
<script type="text/javascript">
    var msg = '${msg}';
    if(msg==''){}else{alert(msg);}
</script>
</body>
</html>
