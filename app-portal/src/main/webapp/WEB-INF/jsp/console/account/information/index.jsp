<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@include file="/inc/import.jsp" %>
<!DOCTYPE html>
<html>

<!-- header -->
<head>
    <%@include file="/inc/meta.jsp" %>

</head>
<body>
<%@include file="/inc/headerNav.jsp"%>
<section class='aside-section'>
    <section class="hbox stretch">
        <!-- .aside -->
        <aside class="bg-Green lter aside hidden-print include" data-include="aside" id="nav"><%@include file="/inc/leftMenu.jsp"%></aside>
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
                                            <div class="aside-li-a active">
                                                <a href="${ctx}/console/account/information/index">基本资料</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a ">
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
                        <div class="head-box"><a href="#subNav" data-toggle="class:hide"> <i
                                class="fa fa-angle-left text"></i> <i class="fa fa-angle-right text-active"></i> </a>
                        </div>
                        <section class=" w-f personal-auth">
                            <div class="wrapper header">
                                <span class="border-left">&nbsp;基本资料</span></div>
                            <div class="row m-l-none m-r-none bg-light lter">
                                <div class="row">
                                    <form:form role="form" action="${ctx}/console/account/information/edit" method="post" class="register-form"
                                          id="personalAuthForm">
                                        <p class="noticeInfo hr text-success"><strong>请完善以下信息，方便我们更好的为您服务</strong> </p>
                                        <div class="form-group">
                                            <span class="hr text-label" ><strong>业务信息</strong></span>
                                        </div>

                                        <div class="form-group">
                                            <lable class="col-md-3 text-right">应用行业：</lable>
                                            <div class="col-md-4 ">
                                                <select name="industry" id="" class="form-control">
                                                    <option value="互联网" <c:if test="${informationVo.industry == '互联网'}"> selected</c:if> >互联网</option>
                                                    <option value="餐饮" <c:if test="${informationVo.industry == '餐饮'}"> selected</c:if> >餐饮</option>
                                                    <option value="金融" <c:if test="${informationVo.industry == '金融'}"> selected</c:if> >金融</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <lable class="col-md-3 text-right">主要业务：</lable>
                                            <div class="col-md-4">
                                                <input type="text" name="business" placeholder="" value="${informationVo.business}  "
                                                       class="form-control input-form notEmpty"/>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <lable class="col-md-3 text-right">网站：</lable>
                                            <div class="col-md-4">
                                                <input type="text" name="url" placeholder="" value="${informationVo.url}"
                                                       class="form-control input-form notEmpty"/>
                                            </div>
                                        </div>
                                        <div class="form-group" >
                                            <span class="hr text-label" ><strong>联系信息</strong></span>
                                        </div>
                                        <div class="form-group">
                                            <lable class="col-md-3 text-right">所在地区：</lable>
                                            <div class="col-md-2 ">
                                                <select name="province" class="form-control province notEmpty">
                                                </select>
                                            </div>
                                            <div class="col-md-2 ">
                                                <select name="city" class="form-control city notEmpty">
                                                </select>
                                            </div>
                                            <div class="col-md-2 " style="display: none;">
                                                <select name="area" class="form-control city notEmpty"></select>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <lable class="col-md-3 text-right">通讯地址：</lable>
                                            <div class="col-md-4">
                                                <input type="text" name="address" placeholder="" value="${informationVo.address}"
                                                       class="form-control input-form notEmpty" />
                                            </div>
                                        </div>


                                        <div class="form-group">
                                            <lable class="col-md-3 text-right">联系电话：</lable>
                                            <div class="col-md-4">
                                                <input type="text" placeholder="例如:20-88888888" value="${informationVo.mobile}" name="mobile"
                                                       class="form-control input-form notEmpty"/>
                                            </div>
                                        </div>


                                        <div class="form-group">
                                            <div class="col-md-9">
                                                <button id="validateBtn" type="submit" class="validateBtnNormal btn btn-primary  btn-form">保存</button>
                                                <%--<a id="validateBtn" onclick="" class="validateBtnNormal btn btn-primary  btn-form">保存</a>--%>
                                            </div>
                                        </div>
                                    </form:form>
                                </div>
                        </section>
                    </section>
                </aside>
            </section>
        </section>
    </section>
</section>
</section>
<script src="${resPrefixUrl }/js/province.js"></script>



<script src="${resPrefixUrl }/js/app.v2.js"></script> <!-- Bootstrap --> <!-- App -->
<script src="${resPrefixUrl }/js/charts/flot/jquery.flot.min.js" cache="false"></script>
<script src="${resPrefixUrl }/js/bootbox.min.js"></script>
<script src="${resPrefixUrl }/js/charts/flot/demo.js" cache="false"></script>
<script src="${resPrefixUrl }/bower_components/bootstrapvalidator/dist/js/bootstrapValidator.min.js"></script>
<script src="${resPrefixUrl }/js/include.js"></script>
<script type="text/javascript" src='${resPrefixUrl }/js/personal/auth.js'></script>
<script>
    function initPCAS()
    {
        new PCAS("province","city","${informationVo.province}","${informationVo.city}");
        var myselect = $(".province");
        // myselect.selectmenu("refresh");
        myselect = $(".city");
        // myselect.selectmenu("refresh");
        // myselect = $(".area");
    }
    var msg = '${msg}';
    if(msg==''){}else{alert(msg);}
</script>
</body>
</html>

