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
                                            <div class="aside-li-a active">
                                                <a href="${ctx}/console/account/safety/index">安全设置</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a ">
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
                                <span class="border-left">&nbsp;修改密码</span></div>
                            <div class="row m-l-none m-r-none bg-light lter">

                                <div class="row" >
                                    <div class="tips-box-padding">
                                        <div class="tips-box">
                                            <p>修改密码时需要输入当前密码；新密码长度要大于6位，建议您定期更换密码，确保账号安全。</p>
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <form:form role="form" action="${ctx}/console/account/safety/edit_psw" method="post" class="register-form"
                                          id="personalAuthForm">
                                        <div class="form-group">
                                            <div class="col-md-4 remove-padding moretips">
                                                <input type="password" name="oldPassword" placeholder="原密码" id="oldPassword" onkeyup="pwsKey2()"
                                                       class="form-control input-form password" />
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="col-md-4 remove-padding moretips ">
                                                <input type="password" name="newPassword" onkeydown="pwsKey()" id="newPassword" placeholder="新密码"
                                                       class="form-control input-form newpassword" />
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="col-md-4 remove-padding moretips">
                                                <input type="password" name="" placeholder="确认新的密码" id="qrnewPassword"
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
                                                <a id="validateBtn" class="btn btn-primary  btn-form" >保存</a>
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
    function pwsKey(){
        var t2 = $('#qrnewPassword').val();
        if(t2!='') {
            $('#qrnewPassword').val('');
        }
    }
    function pwsKey2(){
        var t0 = $('#oldPassword').val();
        var t1 = $('#newPassword').val();
        if(t1!=''&&t1==t0){
            showtoast('新密码不能与原密码相同');
            return false;
        }
        return true;
    }
    $('#validateBtn').click(function(){
        var result = $('#personalAuthForm').data('bootstrapValidator').isValid();
        var flag = pwsKey2();
        if(result==true&&flag==true){
            $('#validateBtn').attr('disabled','disabled');
            //提交表单
            $.ajax({
                url : "${ctx}/console/account/safety/edit_psw",
                type : 'post',
                async: false,//使用同步的方式,true为异步方式
                data :getFormJson("#personalAuthForm"),
                dataType: "json",
                success : function(data){
                    if(data.code=='0'){
                        showtoast(data.msg,'${ctx}/console/account/safety/index');
                    }else{
                        showtoast(data.msg);
                        $('#validateBtn').removeAttr('disabled');
                    }
                },
                fail:function(){
                    showtoast('网络异常，请稍后重试');
                    $('#validateBtn').removeAttr('disabled');
                }
            });
        }else{
            $('#personalAuthForm').bootstrapValidator('validate');
        }
    });
</script>
</body>
</html>

