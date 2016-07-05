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
                                                    <a href="${ctx}/console/telenum/callnum/index">呼入号码管理</a>
                                                </div>
                                            </li>
                                            <li>
                                                <div class="aside-li-a active">
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

                                            <c:forEach items="${testMobileBindList}"  var="testMobileBind" varStatus="s" >
                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right">号码${s.index+1}：</lable>
                                                    <div class="col-md-4">
                                                        <input type="text" name="" placeholder="" value="${testMobileBind.number}" class="form-control input-form limit20" id="voild-${s.index+1}" disabled/>
                                                    </div>
                                                    <div class="col-md-4">
                                                        <a  class="showMobilebox line32"  data-id="${s.index+1}" id="btn-${s.index+1}" >解除绑定</a>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                            <c:forEach begin="${fn:length(testMobileBindList) +1}" end="5" varStatus="s" >
                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right">号码${s.index}：</lable>
                                                    <div class="col-md-4">
                                                        <input type="text" name="" placeholder="" class="form-control input-form limit20" id="voild-${s.index}"/>
                                                    </div>
                                                    <div class="col-md-4">
                                                        <a class="showMobilebox line32"   data-id="${s.index}" id="btn-${s.index}">验证</a>
                                                    </div>
                                                </div>
                                            </c:forEach>


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
            手机号：<span id="modalmobile"></span>
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
        if(regMobile()==false){
            showmsg('手机格式有误','moadltips'); return false;
        }
        var mobile = $('#modalmobile').html();
        $.ajax({
            url : "${ctx}/mc/send",
            type : 'get',
            async: true,//使用同步的方式,true为异步方式
            data : {'mobile':mobile,'${_csrf.parameterName}':'${_csrf.token}'},//这里使用json对象
            //dataType: "json",
            success : function(data){
            },
            fail:function(){
            }
        });
        alert("发送短信成功!")
    }

    /**
     * 发送成功
     */
    function sureCode(){
        var mc = $('.modalCode').val();
        if(mc.length!=4){
            showmsg('请输入四位数的验证码','moadltips2'); return false;
        }
        //验证验证码
        var sType = {
            '0':'验证码错误',
            '1':'验证通过',
            '2':'验证达到最大次数',
            '3':'验证码过期',
            '5':'验证最大次数'
        }
        // alert('检验验证码是否正确 , 绑定手机');

        $.ajax({
            url : "${ctx}/mc/check",
            type : 'get',
            async: false,//使用同步的方式,true为异步方式
            data : {'mc':mc,'${_csrf.parameterName}':'${_csrf.token}'},//这里使用json对象
            dataType: "json",
            success : function(data) {
                var result = data;
                if (result == 1) {//验证通过
                    //开始绑定手机号码
                    var id =  $('#modalmobile').attr('data-id');
                    var pro = $('#voild-'+id).prop('disabled');
                    var number = $('#modalmobile').html();
                    var type = "save";
                    if(pro){
                        type = "disbind";
                    }
                    $.ajax({
                        url : "${ctx}/console/telenum/bind/"+type,
                        type : 'post',
                        async: false,//使用同步的方式,true为异步方式
                        data : {'number':number,'${_csrf.parameterName}':'${_csrf.token}'},//这里使用json对象
                        dataType: "json",
                        success : function(data){

                            if(data.sucess==2) {
                                alert(sType[result]+', '+data.msg);
                                if(pro){
                                    //解除绑定
                                    $('#voild-'+id).val('');
                                    $('#voild-'+id).removeAttr('disabled');
                                    $('#btn-'+id).html('验证');
                                }else{
                                    //验证成功
                                    $('#voild-'+id).attr('disabled',"true");
                                    $('#btn-'+id).html('解除绑定');
                                }
                            }else{
                                alert(data.msg);
                            }
                        },
                        fail:function(){
                            alert('网络异常，请稍后重试');
                        }

                    });

                }else{
                    alert(sType[result]);
                }
            },fail:function(){
            }
        })
        $('.modalCancel').click();
    }
</script>


<%@include file="/inc/footer.jsp"%>
<script type="text/javascript" src='${resPrefixUrl }/js/number/band.js'></script>
</body>
</html>
