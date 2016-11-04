<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@include file="/inc/import.jsp" %>
<!DOCTYPE html>
<html>

<!-- header -->
<head>
    <%@include file="/inc/meta.jsp" %>
    <style>
        textarea {
            resize: none;
        }
        #text1::-webkit-input-placeholder::after {
            display:block;
            content:"\A";
        }
    </style>
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
                                            非常感谢您提供宝贵的意见和建议，我们将全力为您提供最优质的服务，如有任何疑问，可联系我们的客服人员，客服号码：400-002-0048
                                        </p>
                                        <div class="form-group">
                                            <lable class="col-md-3 text-right">反馈意见：</lable>
                                        </div>
                                        <div class="form-group height-220" >
                                            <div class=" remove-padding textarea-box " >
                                                <textarea name="content" id="text1" cols="30" rows="10" class="form-control input-form customcontent "></textarea>
                                                <span class="textarea-box-tip">500字以内</span>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <div class="col-md-3 text-left remove-padding">
                                                <button type="button" id="validateBtn" class="btn btn-primary btn-form">提交</button>
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
<script type="text/javascript" src='${resPrefixUrl }/js/custom/index.js'></script>
<script type="text/javascript">
    $('#validateBtn').click(function(){
        $('#customForm').bootstrapValidator('validate');
        $('#validateBtn').attr('disabled',true);
        var result = $('#customForm').data('bootstrapValidator').isValid();
        var text = $('#text1').val();
        if(result&&text.length>0){
            ajaxsync(ctx+"/console/customer/edit",getFormJson("#customForm"),function(data){
                if(data.success){
                    showtoast("提交成功",ctx+"/console/customer/index",1000);
                }else{
                    showtoast(data.errorMsg);
                    $('#validateBtn').removeAttr('disabled');
                }
            },"post");
        }else{
            $('#validateBtn').removeAttr('disabled');
        }
    });
</script>
</body>
</html>
