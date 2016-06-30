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
                                            <div class="aside-li-a ">
                                                <a href="${ctx}/console/account/information/index">基本资料</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a active">
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
                        <div class="head-box"> <a href="#subNav" data-toggle="class:hide"> <i class="fa fa-angle-left text"></i> <i class="fa fa-angle-right text-active"></i> </a> </div>
                        <section class=" w-f personal-auth">
                            <div class="wrapper header">
                                <span class="border-left">&nbsp;实名认证</span>
                            </div>
                            <div class="row m-l-none m-r-none bg-light lter">
                                <div class="row">
                                    <form:form role="form" action="${ctx}/console/account/auth/edit?${_csrf.parameterName}=${_csrf.token}" enctype="multipart/form-data"  method="post" class="register-form" id="personalAuthForm" >
                                        ${msg}
                                        <div class="form-group">
                                            <lable class="col-md-3 text-right">应用行业：</lable>
                                            <div class="auth_select col-md-4 ">
                                                <input type="radio" name="type" value="0" checked='checked' /> 个人
                                                <input type="radio" name="type" value="1"> 公司
                                            </div>
                                        </div>
                                        <div class='personal'>
                                            <div class="form-group">
                                                <input type="hidden"  name="privateId">
                                                <lable class="col-md-3 text-right">真实姓名：</lable>
                                                <div class="col-md-4 ">
                                                    <input type="text" name="privatename" placeholder="" class="form-control input-form"   />
                                                    <p class="tips">与所使用认证的证件一致的姓名名称</p>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right">证件类型：</lable>
                                                <div class="col-md-4">
                                                    <select class="form-control" name="id_number">
                                                        <option value="0">身份证</option>
                                                        <option value="1">身份证</option>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right">证件号码：</lable>
                                                <div class="col-md-4" >
                                                    <input type="text"  placeholder="" class="form-control input-form"  />
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right">持证照片：</lable>
                                                <div class="col-md-4">
                                                    <input type="file" id="file"  name="file"  placeholder="" class="form-control input-form limitImageFile"   />
                                                    <p class="tips">证件图片大小2m以内，图片格式要求是jpg、jpeg、gif、png、bmp </p>
                                                    <div class="row">
                                                        <div class="">
                                                            <div class="thumbnail">
                                                                <img src="${resPrefixUrl }/images/personal/id.png" alt="...">
                                                                <div class="caption">
                                                                    <p>请手持真实有效的中华人民共和国二代身份证拍照，需要拍摄身份证的正反面照片，照片要求身份证证件完整清晰，人头形象完整清晰且和身份证证件照同属一个。</p>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <!-- <span class="btn btn&#45;default btn&#45;file"> 浏览 <input type="file"> </span> -->
                                                <div class="form-group">

                                                    <div class="col-md-9">
                                                        <a id="validateBtnPersonal"  onclick="id_photoFileUp()" class="validateBtnNormal btn btn-primary  btn-form">保存</a>

                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="company">
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right">公司名称：</lable>
                                                <div class="col-md-4 ">
                                                    <input type="text" name="corpName" data-fv-notempty="true" placeholder="" class="form-control input-form notEmpty" id="form-username" />
                                                    <p class="tips">与所使用认证的证件一致的姓名名称</p>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right">办公地址：</lable>
                                                <div class="col-md-4">
                                                    <input type="text" name="addr" placeholder="" class="form-control input-form notEmpty"  />
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right">所属行业：</lable>
                                                <div class="col-md-4">
                                                    <select class="form-control" name="field_code">
                                                        <option value="0">通讯</option>
                                                        <option value="1">身份证</option>
                                                    </select>
                                                </div>
                                            </div>

                                            <div class="noticeInfo form-group">
                                                <p class="text-success"> 请提供真实有效的营业执照和组织机构代码证，或三证合一的的营业执照（营业执照，组织机构代码证，税务登记证合为一个证件）一证一码的营业执照照片/扫描件（目前进行企业认证，请进入买家中心操作） </p>

                                            </div>

                                            <div class="form-group">
                                                <lable class="col-md-3 text-right extend_label" name="auth_type">证件类型：</lable>
                                                <div class="radio-form col-md-4 ">
                                                    <input type="radio" name="san" checked value="0"> 三证合一（一照一码）
                                                    <input type="radio" name="san" value="1"> 三证合一
                                                    <input type="radio" name="san" value="2"> 三证分离
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right extend_label">统一社会信用代码：</lable>
                                                <div class="col-md-4">
                                                    <input type="text" name="type01_prop02" placeholder="" class="form-control input-form notEmpty"  />
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right extend_label">营业执照：</lable>
                                                <div class="col-md-4">
                                                    <input type="file"placeholder="" name="file" class="form-control input-form limitImageFile"   />
                                                    <p class="tips">将原件或盖章的复印件扫描、拍照后上传，图片格式要求是jpg、jpeg、gif、png、bmp </p>
                                                </div>
                                                <!-- <span class="btn btn&#45;default btn&#45;file"> 浏览 <input type="file"> </span> -->
                                            </div>

                                            <div class="form-group">
                                                <lable class="col-md-3 text-right extend_label">注册号：</lable>
                                                <div class="col-md-4">
                                                    <input type="text" name="type02_prop01" placeholder="" class="form-control input-form limit15"   />
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right extend_label">税务登记号：</lable>
                                                <div class="col-md-4">
                                                    <input type="text" name="type02_prop02" placeholder="" class="form-control input-form limit15"  />
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right extend_label">税务登记：</lable>
                                                <div class="col-md-4">
                                                    <input type="file" placeholder="" name="file" class="form-control input-form limitImageFile"   />
                                                    <p class="tips">证件图片大小2m以内，图片格式要求是jpg、jpeg、gif、png、bmp </p>
                                                </div>
                                                <!-- <span class="btn btn&#45;default btn&#45;file"> 浏览 <input type="file"> </span> -->
                                            </div>
                                            <div class="form-group">

                                                <div class="col-md-9">
                                                    <a  onclick="id_photoFileUp()" id="validateBtn" class="validateBtnExtend btn btn-primary  btn-form">保存</a>
                                                </div>
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
<script src="${resPrefixUrl }/js/app.v2.js"></script> <!-- Bootstrap --> <!-- App -->
<script src="${resPrefixUrl }/js/charts/flot/jquery.flot.min.js" cache="false"></script>
<script src="${resPrefixUrl }/js/bootbox.min.js"></script>
<script src="${resPrefixUrl }/js/charts/flot/demo.js" cache="false"></script>
<script src="${resPrefixUrl }/bower_components/bootstrapvalidator/dist/js/bootstrapValidator.min.js"></script>
<script src="${resPrefixUrl }/js/include.js"></script>
<script type="text/javascript" src='${resPrefixUrl }/js/personal/auth.js'></script>
<script type="text/javascript">
    function id_photoFileUp(){
        var form = document.getElementById("personalAuthForm");
        form.submit();

    }
</script>
</body>
</html>
