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
                        <div class="wrapper header"><span class="margin_lr"></span><span class="margin_lr border-left">&nbsp;基本资料</span>
                        </div>
                        <section class="scrollable">
                            <div class="slim-scroll">
                                <!-- nav -->
                                <nav class="hidden-xs">
                                    <ul class="nav">
                                        <li>
                                            <div class="aside-li-a active">
                                                <a href="${ctx}/console/account/safety/index">安全设置</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a ">
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
                                <span class="border-left">&nbsp;修改密码</span></div>
                            <div class="row m-l-none m-r-none bg-light lter">

                                <div class="row" >
                                    <div class="tips-box-padding">
                                        <div class="tips-box">
                                            <p>修改密码时需要输入当前密码；新密码长度要大于6位，建议您定期更换密码，确保帐号安全。</p>
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <form:form role="form" action="${ctx}/console/account/safety/edit_psw" method="post" class="register-form"
                                          id="personalAuthForm">
                                        <div class="form-group">
                                            <div class="col-md-4 remove-padding">
                                                <input type="password" name="oldPassword" placeholder="原密码"
                                                       class="form-control input-form password" />
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="col-md-4 remove-padding ">
                                                <input type="password" name="newPassword" placeholder="新密码"
                                                       class="form-control input-form newpassword" />
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="col-md-4 remove-padding">
                                                <input type="password" name="" placeholder="确认新的密码"
                                                       class="form-control input-form repassword" />
                                            </div>
                                        </div>
                                        <!--提示-->
                                        <div class="form-group">
                                            <div class="col-md-4 remove-padding">
                                                <div class="tips-error" style="display: none;">原密码输入不正确</div>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <div class="col-md-9 remove-padding">
                                                <button type="submit"  class="btn btn-primary  btn-form">保存</button>
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
<script type="text/javascript" src='${resPrefixUrl }/js/personal/password.js'></script>
<script type="text/javascript">
    var msg = '${msg}';
    if(msg==''){}else{alert(msg);}
</script>
</body>
</html>

