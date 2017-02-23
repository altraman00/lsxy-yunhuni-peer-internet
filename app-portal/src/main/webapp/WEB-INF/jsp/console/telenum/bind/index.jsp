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
                                                <div class="aside-li-a ">
                                                    <a href="${ctx}/console/telenum/callnum/index">我的号码</a>
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
                            <div class="head-box"><a href="#subNav" data-toggle="class:hide">
                                <i class=" iconfont icon-menu-left text"></i>
                                <i class=" iconfont icon-menu-right text-active"></i>
                            </a></div>
                            <section class=" w-f application_create">
                                <div class="wrapper header">
                                    <span class="border-left">&nbsp;测试号码绑定</span>
                                    <%--<a class="border-right">如何使用测试号码绑定</a>--%>
                                </div>

                                <div class="row m-l-none m-r-none bg-light lter">
                                    <div class="row">
                                        <form role="form" action="./index.html" method="post" class="register-form" id="application_create">
                                            <p class="number_info ">
                                                1.测试号码用于在应用未上线阶段，租户进行外呼、会议或其他业务调试时使用，租户绑定号码后，才能接听系统应用的呼出。<br/>
                                                2.测试号码只允许绑定5个。
                                            </p>

                                            <c:forEach items="${testNumBindList}"  var="testNumBind" varStatus="s" >
                                                <div class="form-group">
                                                    <lable class="col-md-3 text-right">号码${s.index+1}：</lable>
                                                    <div class="col-md-4">
                                                        <input type="text" name="" placeholder="" value="${testNumBind.number}" class="form-control input-form limit20" id="voild-${s.index+1}" disabled/>
                                                    </div>
                                                    <div class="col-md-4">
                                                        <a  class="showMobilebox line32"  data-id="${s.index+1}" id="btn-${s.index+1}" >解除绑定</a>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                            <c:forEach begin="${fn:length(testNumBindList) +1}" end="5" varStatus="s" >
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

                                            <p>应用上线前测试阶段呼入使用统一的测试号码进行IVR或呼入功能调试</p>

                                            <div class="number_on">
                                                <a>${testCallNumber}</a>
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
    <div class="title">验证手机号<a class="close_a modalCancel cancelclear"></a></div>
    <div class="content">
        <div class="margintop30"></div>
        <div class="input ">
            手机号：<span id="modalmobile" ></span>
        </div>
        <div id="second-codeblock"></div>
        <div class="input-inline-block">
            <input class="code form-control modalCode" type="text" name="mobile" id="mobileV" placeholder="手机验证码" />
            <button class="code-button" id="send-code" >获取验证码</button>
        </div>



        <p class="tips-error moadltips text-center msgcode" style="display:none ">请先填写手机号码</p>

    </div>
    <div class="footer">
        <a class="cancel modalCancel cancelclear">返回</a>
        <a class="sure" onclick="sureCode()">确认</a>
    </div>
</div>

<%@include file="/inc/footer.jsp"%>
<script type="text/javascript" src='${resPrefixUrl }/js/number/band.js'></script>
<script >
    $(".cancelclear").click(function(){
        $("#mobileV").val('');
    })
    /**
     * 发送验证码
     */
    var isVc = false;//是否需要图形验证码
    var sendCodeResult=false;
    function sendCode(){
        var mobile = $('#modalmobile').html();
        if(regMobile(mobile)==false){
            showmsg('请输入正确的手机格式','msgcode'); return false;
        }
        var sendResult = false; //是否发送手机验证码成功
        var vCode = "";
        if(isVc){
            //校验四位验证码是否正确
            if($('#second-code').length>0){
                var second = $('#second-code').val();
                if(second.length!=4){
                    showmsg('请输入四位验证码','msgcode'); return false;
                }
            }
            vCode = $("#second-code").val();
        }


        ajaxsync(ctx+"/mc/send",{'mobile':mobile,'validateCode':vCode,'${_csrf.parameterName}':'${_csrf.token}'},function(response){
            if(response.data.flag){
                showmsg('发送短信验证码成功','msgcode');
                $('#second-codeblock').html('');
                sendResult = true;
            }else if(response.data.vc){
                sendResult = false;
                //发送不成功，且要输入图形验证码
                showmsg(response.data.err,'msgcode');
                isVc = true;
                //启动二次校验
                var html = '<div class="input-inline-block"><input class="code form-control " type="text" name="" id="second-code" placeholder="图形验证码"/>';
                html += '&nbsp;&nbsp;<a class="code-img"><img src="${ctx}/vc/get?dt='+ new Date() +'" onclick="changeImgCode()"  id="imgValidateCode" ></a></div>';
                $('#second-codeblock').html(html);

            }else{
                sendResult = false;
                showmsg(response.data.err,'msgcode');
            }
        },"get");

        sendCodeResult =  sendResult;
        return sendResult;
    }
    /**
     * 发送成功
     */
    function sureCode(){
        var id =  $('#modalmobile').attr('data-id');
        var pro = $('#voild-'+id).prop('disabled');
        if(!sendCodeResult){
            showmsg('尚未发送短信验证码','msgcode'); return false;
        }
        var code = $('.modalCode').val();
        if(code.length!=4){
            showmsg('请输入四位数的验证码','msgcode'); return false;
        }
        var mobile = $('#modalmobile').html();

        ajaxsync(ctx+"/mc/check",{'mc':code,"mobile":mobile},function(response){
            if(response.data.flag){
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
                    success : function(result){
                        if(result.sucess==2) {
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
                            $('.modalCancel').click();
                            showtoast(result.msg);
                        }else{
                            showmsg(result.msg,'msgcode');
                        }
                    },
                    fail:function(){
                        showmsg('网络异常，请稍后重试','msgcode');
                    }

                });
            }else{
                showmsg(response.data.err,'msgcode');
            }
        },"get");

        return;
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
        $('.modalCancel').click();
    }
    function changeImgCode(){
        $("#imgValidateCode").prop("src",  '${ctx}'+"/vc/get?dt="+new Date());
    }
</script>
</body>
</html>
