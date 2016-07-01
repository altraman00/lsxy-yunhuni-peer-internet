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
            <aside class="bg-Green lter aside hidden-print" id="nav"><%@include file="/inc/leftMenu.jsp"%></aside>
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
                                                <div class="aside-li-a ">
                                                    <a href="${ctx}/console/number/call/index">呼入号码管理</a>
                                                </div>
                                            </li>
                                            <li>
                                                <div class="aside-li-a active">
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
                            <section class=" w-f application_create">
                                <div class="wrapper header">
                                    <span class="border-left">&nbsp;号码绑定</span>
                                    <a class="border-right">如何使用测试号码绑定</a>
                                </div>

                                <div class="row m-l-none m-r-none bg-light lter">
                                    <div class="row">
                                        <form role="form" action="./index.html" method="post" class="register-form" id="application_create">
                                            <p class="number_info ">
                                                测试号码用于在应用上线前用于外呼、会议或其他业务调试，在应用未上线阶段，所有语音、会议业务仅限于已经绑定的测试号码测试号码允许绑定5个
                                            </p>
                                            <div class="form-group">
                                                <lable class="col-md-3 text-right">号码1：</lable>
                                                <div class="col-md-4">
                                                    <input type="text" name="" placeholder="" value="13611460985" class="form-control input-form limit20"/>
                                                </div>
                                                <div class="col-md-4">
                                                    <a  class="showMobilebox line32"  data-mobile="13611460985" >解除绑定</a>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <lable class="col-md-3 text-right">号码2：</lable>
                                                <div class="col-md-4">
                                                    <input type="text" name="" placeholder=""  class="form-control input-form limit20"/>
                                                </div>
                                                <div class="col-md-4">
                                                    <a class="showMobilebox line32" data-mobile="13611460985"  >验证</a>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <lable class="col-md-3 text-right">号码3：</lable>
                                                <div class="col-md-4">
                                                    <input type="text" name="" placeholder="" class="form-control input-form limit20"/>
                                                </div>
                                                <div class="col-md-4">
                                                    <a data-mobile="13611460983"  class="showMobilebox line32">验证</a>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <lable class="col-md-3 text-right">号码4：</lable>
                                                <div class="col-md-4">
                                                    <input type="text" name="" placeholder="" class="form-control input-form limit20"/>
                                                </div>
                                                <div class="col-md-4">
                                                    <a data-mobile="13611460984"  class="showMobilebox line32">验证</a>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <lable class="col-md-3 text-right">号码5：</lable>
                                                <div class="col-md-4">
                                                    <input type="text" name="" placeholder="" class="form-control input-form limit20"/>
                                                </div>
                                                <div class="col-md-4">
                                                    <a data-mobile="13611460982"  class="showMobilebox line32">解除绑定</a>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <span class="hr text-label" ><strong>测试呼入号码</strong></span>
                                            </div>

                                            <p>应用上线前测试阶段呼入使用同一的测试号码进行IVR或呼入功能调试</p>

                                            <div class="number_on">
                                                <a>0898-77887748858</a>
                                            </div>

                                        </form>
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




<!---mobilebox-->
<div class="shadow-bg" id="show-bg"></div>
<div id="mobilebox" class="modal-box" style="display:none ;">
    <div class="title">验证手机号<a class="close_a modalCancel"></a></div>
    <div class="content">
        <div class="margintop30"></div>
        <div class="input ">
            手机号：<span id="modalmobile">13611460986</span>
        </div>
        <div class="input">
            <input class="code form-control modalCode" type="text" name="mobile" placeholder="验证码" />
            <button class="code-button" id="send-code" >发送验证码</button>
        </div>
        <div class="input">
            <div class="tips-error moadltips" style="display: none">请先填写手机号码</div>
        </div>
    </div>
    <div class="footer">
        <a class="cancel modalCancel">返回</a>
        <a class="sure" onclick="sureCode()">确认</a>
    </div>
</div>


<script >
    /**
     * 发送验证码
     */
    function sendCode(){
        //showmsg('提示','moadltips');
    }
    /**
     * 发送成功
     */
    function sureCode(){

        $('.modalCancel').click();
    }



</script>


<%@include file="/inc/footer.jsp"%>
<script type="text/javascript" src='${resPrefixUrl }/js/number/band.js'></script>
</body>
</html>
