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
                                                <a href="${ctx}/console/account/index">基本资料</a>
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
                        <div class="head-box"><a href="#subNav" data-toggle="class:hide">
                            <i class=" iconfont icon-menu-left text"></i>
                            <i class=" iconfont icon-menu-right text-active"></i>
                        </a></div>

                        <section class=" w-f personal-auth">
                            <div class="wrapper header">
                                <span class="border-left">&nbsp;基本资料</span></div>
                            <div class="row m-l-none m-r-none bg-light lter">
                                <div class="row">
                                    <form:form role="form" action="" method="post" class="register-form"
                                          id="personalAuthForm">
                                        <input type="hidden" name="id" value="${account.id}"/>
                                        <p class="noticeInfo hr text-success"><strong>请完善以下信息，方便我们更好地为您服务</strong> </p>
                                        <div class="form-group">
                                            <span class="hr text-label" ><strong>业务信息</strong></span>
                                        </div>

                                        <div class="form-group">
                                            <lable class="col-md-3 text-right">应用行业：</lable>
                                            <div class="col-md-4 ">
                                                <select name="industry" id="" class="form-control">
                                                <c:set var="industry" value="${account.industry}"></c:set>
                                                <%@ include file="/inc/industry.jsp"%>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <lable class="col-md-3 text-right">主要业务：</lable>
                                            <div class="col-md-4">
                                                <input type="text" name="business" placeholder="" value="${account.business}"
                                                       class="form-control input-form "/>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <lable class="col-md-3 text-right">网站：</lable>
                                            <div class="col-md-4">
                                                <input type="text" name="url" placeholder="" value="${account.url}"
                                                       class="form-control input-form "/>
                                            </div>
                                        </div>
                                        <div class="form-group" >
                                            <span class="hr text-label" ><strong>联系信息</strong></span>
                                        </div>
                                        <div class="form-group">
                                            <lable class="col-md-3 text-right">所在地区：</lable>
                                            <div class="col-md-2 ">
                                                <select name="province" class="form-control province ">
                                                </select>
                                            </div>
                                            <div class="col-md-2 ">
                                                <select name="city" class="form-control city ">
                                                </select>
                                            </div>
                                            <div class="col-md-2 " style="display: none;">
                                                <select name="area" class="form-control city "></select>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <lable class="col-md-3 text-right">通讯地址：</lable>
                                            <div class="col-md-4">
                                                <input type="text" name="address" placeholder="" value="${account.address}"
                                                       class="form-control input-form " />
                                            </div>
                                        </div>


                                        <div class="form-group">
                                            <lable class="col-md-3 text-right">联系电话：</lable>
                                            <div class="col-md-4">
                                                <input type="text" placeholder="例如:020-88888888" value="${account.phone}" name="phone"
                                                       class="form-control input-form "/>
                                            </div>
                                        </div>


                                        <div class="form-group">
                                            <div class="col-md-9">
                                                <a id="validateBtn"  class="validateBtnNormal btn btn-primary  btn-form">保存</a>
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

<%@include file="/inc/footer.jsp"%>
<script src="${resPrefixUrl }/js/province.js"></script>
<script type="text/javascript" src='${resPrefixUrl }/js/personal/auth.js'></script>
<script>
    function initPCAS()
    {
        new PCAS("province","city","${account.province}","${account.city}");
        var myselect = $(".province");
        myselect = $(".city");
    }
    $('#validateBtn, #validateBtnPersonal').click(function(){
        $('#personalAuthForm').bootstrapValidator('validate');
        var result = $('#personalAuthForm').data('bootstrapValidator').isValid();
        if(result==true){
            //提交表单
            $.ajax({
                url : "${ctx}/console/account/update",
                type : 'post',
                async: false,//使用同步的方式,true为异步方式
                data :getFormJson("#personalAuthForm"),
                dataType: "json",
                success : function(data){
                    showtoast(data.msg);
                },
                fail:function(){
                    showtoast('网络异常，请稍后重试');
                }
            });
        }
    });
</script>
</body>
</html>

