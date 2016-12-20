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
                                            <div class="aside-li-a ">
                                                <a href="${ctx}/console/account/index">基本资料</a>
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
                        <div class="head-box"><a href="#subNav" data-toggle="class:hide">
                            <i class=" iconfont icon-menu-left text"></i>
                            <i class=" iconfont icon-menu-right text-active"></i>
                        </a></div>
                        <section class=" w-f personal-auth">
                            <div class="wrapper header">
                                <span class="border-left">&nbsp;实名认证</span>
                            </div>
                            <div class="row m-l-none m-r-none bg-light lter">
                                <div class="row">
                                    <form:form role="form" action="${ctx}/console/account/auth/edit?${_csrf.parameterName}=${_csrf.token}&upgrade=${upgrade}" enctype="multipart/form-data"  method="post" class="register-form" id="personalAuthForm" >
                                        <div class="form-group">
                                            <lable class="col-md-3 text-right">应用行业：</lable>
                                            <div class="auth_select col-md-4 ">
                                                <c:choose>
                                                    <c:when test="${upgrade == true}">
                                                        <input type="radio" name="type" value="0"  disabled/> 个人
                                                        <input type="radio" name="type" value="1" checked='checked'> 公司
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input type="radio" name="type" value="0" checked='checked' /> 个人
                                                        <input type="radio" name="type" value="1"> 公司
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>
                                        <div class='personal'>
                                            <div class="form-group">
                                                <input type="hidden"  name="privateId">
                                                <lable class="col-md-3 text-right">真实姓名：</lable>
                                                <div class="col-md-4 ">
                                                    <input type="text" name="privateName" placeholder="" class="form-control input-form max30"/>
                                                    <p class="tips">请与提交的证件姓名保持一致</p>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right">证件类型：</lable>
                                                <div class="col-md-4">
                                                    <select class="form-control " name="idType" >
                                                        <option value="0">身份证</option>
                                                        <%--<option value="1">护照</option>--%>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right">证件号码：</lable>
                                                <div class="col-md-4" >
                                                    <input type="text" name="idNumber" placeholder="" class="form-control input-form idcard"/>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right">持证照片：</lable>
                                                <div class="col-md-4">
                                                    <input type="file" id="file"  name="file"  placeholder="" class="form-control-file input-form limitImageFile"   />
                                                    <p class="tips">证件图片大小2m以内，图片格式要求是jpg、jpeg、gif、png、bmp </p>
                                                    <div class="row">
                                                        <div class="">
                                                            <div class="thumbnail">
                                                                <img src="${resPrefixUrl }/images/personal/id.png" alt="...">
                                                                <div class="caption">
                                                                    <p>请手持真实有效的相关证件拍照，需要拍摄证件带有信息的一面，照片要求证件信息和人物形象完整清晰，人物与证件上的身份相符。</p>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <!-- <span class="btn btn&#45;default btn&#45;file"> 浏览 <input type="file"> </span> -->
                                                <div class="form-group">

                                                    <div class="col-md-9">
                                                        <button  type="submit"   class="validateBtnNormal btn btn-primary  btn-form">保存</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="company">
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right">公司名称：</lable>
                                                <div class="col-md-4 ">
                                                    <input type="text" name="corpName" data-fv-notempty="true" placeholder="" class="form-control input-form companyname" id="form-username"/>
                                                    <p class="tips">请与提交的资料保持一致</p>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right">办公地址：</lable>
                                                <div class="col-md-4">
                                                    <input type="text" name="addr" placeholder="" class="form-control input-form max100 notEmpty"/>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right">所属行业：</lable>
                                                <div class="col-md-4">
                                                    <select name="industry" id="" class="form-control">
                                                        <%@ include file="/inc/industry.jsp"%>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right">申请人：</lable>
                                                <div class="col-md-4 ">
                                                    <input type="text" name="proposer" placeholder="" class="form-control input-form max30 notEmpty" id="form-proposer"/>
                                                </div>
                                            </div>
                                            <div class="noticeInfo form-group">
                                                <br>
                                                <p class="text-success">请提供有效的营业执照、税务登记证、营业执照号、统一社会信用代码、税务登记号或注册号等信息</p>
                                            </div>
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right extend_label">证件类型：</lable>
                                                <div class="radio-form col-md-8">
                                                    <input class="card_select_0" type="radio" name="authType" checked value="0"> 三证合一（一照一码）
                                                    <input class="card_select_1" type="radio" name="authType" value="1"> 三证合一
                                                    <input class="card_select_2" type="radio" name="authType" value="2"> 三证分离
                                                </div>
                                            </div>
                                            <div class="card_type01">
                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right extend_label lineheight-24">统一社会信用代码：</lable>
                                                    <div class="col-md-4">
                                                        <input type="text" name="type01Prop02" placeholder="" class="form-control input-form limit18"/>
                                                        <p class="tips">18位数字或者大写英文字母</p>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right extend_label">营业执照：</lable>
                                                    <div class="col-md-4">
                                                        <input type="file" placeholder="" name="file" class="form-control-file input-form limitImageFile"   />
                                                        <p class="tips">将原件或盖章的复印件扫描、拍照后上传，文件支持2M以内的jpg、jpeg、gif、png、bmp。</p>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="card_type02">
                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right extend_label">注册号：</lable>
                                                    <div class="col-md-4">
                                                        <input type="text" name="type02Prop01" placeholder="" class="form-control input-form limit15"/>
                                                        <p class="tips">15位数字或者大写英文字母</p>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right extend_label">税务登记号：</lable>
                                                    <div class="col-md-4">
                                                        <input type="text" name="type02Prop02" placeholder="" class="form-control input-form limit15"/>
                                                        <p class="tips">15位数字或者大写英文字母</p>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right extend_label">营业执照：</lable>
                                                    <div class="col-md-4">
                                                        <input type="file" placeholder="" name="file" class="form-control-file input-form limitImageFile"   />
                                                        <p class="tips">将原件或盖章的复印件扫描、拍照后上传，文件支持2M以内的jpg、jpeg、gif、png、bmp。</p>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="card_type03">
                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right extend_label">税务登记号：</lable>
                                                    <div class="col-md-4">
                                                        <input type="text" name="type03Prop01" placeholder="" class="form-control input-form limit15"/>
                                                        <p class="tips">15位数字或者大写英文字母</p>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right extend_label">税务登记证：</lable>
                                                    <div class="col-md-4">
                                                        <input type="file" placeholder="" name="file" class="form-control-file input-form limitImageFile"/>
                                                        <p class="tips">将原件或盖章的复印件扫描、拍照后上传，文件支持2M以内的jpg、jpeg、gif、png、bmp。</p>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right extend_label">营业执照号：</lable>
                                                    <div class="col-md-4">
                                                        <input type="text" name="type03Prop03" placeholder="" class="form-control input-form limit15"/>
                                                        <p class="tips">15位数字或者大写英文字母</p>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right extend_label">营业执照：</lable>
                                                    <div class="col-md-4">
                                                        <input type="file" placeholder="" name="file" class="form-control-file input-form limitImageFile"/>
                                                        <p class="tips">将原件或盖章的复印件扫描、拍照后上传，文件支持2M以内的jpg、jpeg、gif、png、bmp。</p>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <div class="col-md-9">
                                                    <button type="submit" class="validateBtnExtend btn btn-primary  btn-form">保存</button>
                                                </div>
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
<script type="text/javascript" src='${resPrefixUrl }/js/personal/auth.js'></script>
<script type="text/javascript">
    function id_photoFileUp(){
        var form = document.getElementById("personalAuthForm");
        form.submit();
    }
    var msg = '${msg}';
    if(msg){
        showtoast(msg);
    }
</script>
<c:if test="${upgrade == true}">
    <script>
        $(function(){
            $('.company').css('display', 'block')
            $('.personal').css('display', 'none')
            $('.card_type01').css('display', 'block')
            $('.card_type02').css('display', 'none')
            $('.card_type03').css('display', 'none')
        });
    </script>
</c:if>
</body>
</html>
