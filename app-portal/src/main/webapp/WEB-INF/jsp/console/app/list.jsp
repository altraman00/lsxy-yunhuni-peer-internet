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
                        <div class="wrapper header"><span class="margin_lr"></span><span class="margin_lr border-left">&nbsp;应用管理</span>
                        </div>
                        <section class="scrollable">
                            <div class="slim-scroll">
                                <!-- nav -->
                                <nav class="hidden-xs">
                                    <ul class="nav">
                                        <li>
                                            <div class="aside-li-a active">
                                                <a href="${ctx}/console/app/list">应用列表</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a">
                                                <a href="${ctx}/console/app/index">创建应用</a>
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
                        <div class="wrapper header">
                            <span class="border-left">&nbsp;应用列表</span>
                        </div>
                        <section class="scrollable wrapper w-f">
                            <section class="panel panel-default yunhuni-personal">
                                <div class="row m-l-none m-r-none bg-light lter">
                                    <div class="col-md-12 padder-v fix-padding">
                                        <a href="${ctx}/console/app/index" class="btn btn-primary query">创建应用</a>
                                    </div>
                                </div>
                            </section>
                            <section class="panel panel-default pos-rlt clearfix ">
                                <table class="table table-striped cost-table-history tablelist" id="tableModal">
                                    <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>应用名称</th>
                                        <th>APPID</th>
                                        <th>应用状态</th>
                                        <th>应用创建时间</th>
                                        <th>操作</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${pageObj.result}" var="result" varStatus="s">
                                        <tr id="app-${result.id}">
                                            <td scope="row">${s.index+1}</td>
                                            <td>${result.name}</td>
                                            <td>${result.id}</td>
                                            <c:if test="${result.status==1}"><td class="success"  id="statusapp-${s.index+1}">已上线</td></c:if>
                                            <c:if test="${result.status==2}"><td  class="nosuccess" id="statusapp-${s.index+1}">未上线</td></c:if>
                                            <td><fmt:formatDate value="${result.createTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate> </td>
                                            <td class="operation">
                                                <a href="${ctx}/console/app/detail?id=${result.id}">详情</a> <span ></span>
                                                <a onclick="delapp('${result.id}','${result.status}')" >删除</a> <span ></span>
                                                <c:if test="${result.status==2}"> <a onclick="tabtarget('${result.id}','${result.isIvrService==1?1:0}')" >申请上线</a></c:if>
                                                <c:if test="${result.status==1}"> <span class="apply" id="trb-${result.id}"><a onclick="lineapp('${result.id}')">下线</a></span></c:if>
                                            </td>

                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </section>
                            <c:set var="pageUrl" value="${ctx}/console/app/list"></c:set>
                            <%@include file="/inc/pagefooter.jsp" %>

                        </section>
                    </section>
                </aside>
            </section>
            <a href="#" class="hide nav-off-screen-block" data-toggle="class:nav-off-screen" data-target="#nav"></a>
        </section>
    </section>
</section>



<input type="hidden" value="" id="modal-appid" />

<!---mobilebox-->
<div class="shadow-bg" id="show-bg" ></div>
<div id="mobilebox-1" class="appliation-modal-box" style="display: none;">
    <div class="addmobile1" >
        <div class="title">应用上线流程<a class="close_a modalCancel" data-type="1"></a></div>
        <div class="content" >
            <!--nav-->
            <div class="nav-modal-box">
                <ul class="nav-modal">
                    <li><a class="spot" data-action="1"></a><span class="lines"></span> </li>
                    <li><a class="spot" data-action="2"></a><span class="lines"></span></li>
                    <li><a class="spot" data-action="3"></a><span class="lines"></span></li>
                    <li><a class="spot" data-action="4"></a> </li>
                </ul>
                <ul class="nav-modal-text">
                    <li><span class="text">实名认证</span> </li>
                    <li><span class="text">IVR号码绑定</span> </li>
                    <li class="ml-3"><span class="text">支付</span> </li>
                    <li class="ml-30 mr-0"><span class="text">上线</span>  </li>
                </ul>
            </div>

            <div class="contentModal" data-action="1">
                <!--未认证显示-->
                <div class="not-real-auth" style="display: none">
                    <div class="input text-center " >
                        <img src="${resPrefixUrl }/images/index/l6.png" alt="" class="sre" />
                        <p>您还没有经过实名认证，请进行实名认证！</p>
                    </div>
                    <div class="input text-center" >
                        <a href="${ctx}/console/account/auth/index" type="button"  class="btn btn-primary btn-box">实名认证</a>
                    </div>
                </div>
                <!---end--->
                <!--认证显示-->
                <div class="real-auth">
                    <div class="input text-center" >
                        <img src="${resPrefixUrl }/images/index/l6.png" alt="" class="sre" />
                        <p>您已成功进行实名认证，点击进入下一步!</p>
                    </div>
                </div>
                <div class="input text-center" >
                    <a type="button"  class="btn btn-primary btn-box tabModalBtn"  data-id="2" data-fun="creatIVR()">下一步</a>
                </div>
                <!---end--->
            </div>

            <div class="contentModal" style="display: none" data-action="2">
                <div class="input text-center">
                    <p>您选择开通IVR功能，我们给您分配了一个IVR号码供应用使用IVR功能</p>
                </div>
                <div class="input text-center">
                    <div class="defulatTips" id="creatIVR" >
                        0898-77887748858
                    </div>
                    <a onclick="nolike()" class="font14">不喜欢 换一个?</a>
                </div>
                <div class="hideIVR">
                </div>
                <div class="input text-center" >
                    <a type="button"  class="btn btn-primary btn-box tabModalBtn" data-id="3" data-fun="createorder()" >下一步</a>
                </div>
            </div>

            <div class="contentModal" style="display: none" data-action="3">
                <div class="input text-center mt-0">
                    <p>您需要支付：<span class="money">998.00</span> 元&nbsp;&nbsp;&nbsp; 账号余额：0.00 元 &nbsp;&nbsp;&nbsp; <span class="nomoney">!!余额不足</span>&nbsp;&nbsp;&nbsp;<a href="cost_recharge_sure.html">充值</a> </p>
                </div>
                <div class="input text-center mb-0 mt-0">
                    <div class="defulatTips">IVR号码：121212121</div>
                    <div class="defulatTips">IVR号码租用费：1000元</div>
                    <div class="defulatTips">IVR功能使用费：100元/月</div>
                </div>
                <div class="ivrserver"><input type="checkbox" name="readcheckbox" id="readbook" />已阅读<a target="_blank" href="ivragreement.html" >IVR服务协议</a></div>
                <div class="input text-center mt-0" >
                    <a type="button"  class="btn btn-primary btn-box tabModalBtn" data-id="4" data-fun="pay()">确认支付</a>
                    <a type="button"  class="btn btn-primary btn-box" onclick="resetIVR()">重新选择</a>
                </div>
            </div>

            <div class="contentModal" style="display:none " data-action="4">
                <div class="input text-center" >
                    <img src="${resPrefixUrl }/images/index/l1.png" alt="" class="sre" />
                    <p>上线成功</p>
                </div>
                <div class="input text-center" >
                    <a href="" type="button"  class="btn btn-primary btn-box tabModalBtn">上线成功</a>
                </div>
            </div>
            <div class="input">
                <div class="tips-error moadltips1 text-center" style="display: none">错误提示信息</div>
            </div>

        </div>
    </div>

</div>


<div id="mobilebox-0" class="appliation-modal-box" style="display: none" >
    <div class="addmobile1" >
        <div class="title">应用上线流程<a class="close_a modalCancel" data-type="0"></a></div>
        <div class="content" >
            <!--nav-->
            <div class="nav-modal-box">
                <ul class="nav-modal navw-150">
                    <li><a class="spot" data-action="1"></a><span class="lines"></span> </li>
                    <li><a class="spot" data-action="2"></a></li>
                </ul>
                <ul class="nav-modal-text navw-150">
                    <li><span class="text">实名认证</span> </li>
                    <li class=" mr-0"><span class="text">上线</span>  </li>
                </ul>
            </div>

            <div class="contentModal" data-action="1">
                <!--未认证显示-->
                <div class="not-real-auth" style="display: none">
                    <div class="input text-center" >
                        <img src="${resPrefixUrl }/images/index/l6.png" alt="" class="sre" />
                        <p>您还没有经过实名认证，请进行实名认证！</p>
                    </div>
                    <div class="input text-center" >
                        <a href="${ctx}/console/account/auth/index" type="button"  class="btn btn-primary btn-box">实名认证</a>
                    </div>
                </div>
                <!---end--->
                <!--认证显示-->
                <div class="real-auth">
                    <div class="input text-center">
                        <img src="${resPrefixUrl }/images/index/l6.png" alt="" class="sre" />
                        <p>您已成功进行实名认证，点击进入下一步!</p>
                    </div>
                </div>
                <div class="input text-center" >
                    <a type="button"  class="btn btn-primary btn-box tabModalBtn" data-id="2" >下一步</a>
                </div>
                <!---end--->
            </div>

            <div class="contentModal" style="display: none" data-action="2">
                <div class="input text-center" >
                    <img src="${resPrefixUrl }/images/index/l1.png" alt="" class="sre" />
                    <p>上线成功</p>
                </div>
                <div class="input text-center" >
                    <a href="" class="btn btn-primary btn-box tabModalBtn"  data-fun="" >上线成功</a>
                </div>
            </div>
            <div class="input">
                <div class="tips-error moadltips1 text-center" style="display: none">错误提示信息</div>
            </div>

        </div>
    </div>

</div>


<div class="tips-toast"></div>
<%@include file="/inc/footer.jsp"%>
<script type="text/javascript" src='${resPrefixUrl }/js/bootstrap-datepicker/js/bootstrap-datepicker.js'> </script>
<script type="text/javascript" src='${resPrefixUrl }/js/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js'> </script>
<script type="text/javascript" src='${resPrefixUrl }/js/application/list.js'> </script>
<script>

    function isRealAuth(){
        var realAuth = null;
        //获取用户实名认证状态
        $.ajax({
            url : "${ctx}/console/account/auth/is_real_auth",
            type : 'get',
            async: false,//使用同步的方式,true为异步方式
            dataType: "json",
            success : function(data){
                isRealAuth = data;
            },
            fail:function(){
                showtoast('网络异常，请稍后重试');
            }
        });
        return realAuth;
    }

    /**
     * @param id 应用id
     * @param type 类型
     * type 1 ivr  2no ivr
     */
    function tabtarget(id,type){
        //赋值appid
        $('#modal-appid').val(id);
        //步骤
        var index = 0;
        var flag = true;//是否能上线
        $.ajax({
            url : "${ctx}/console/app_action/get/"+ id ,
            type : 'get',
            async: false,//使用同步的方式,true为异步方式
            dataType: "json",
            success : function(data){
                if(data && data.action != null){
                    switch (data.action){
                        case 11: index = 2;break;   //选号
                        case 12: index = 3;break;   //支付
                        case 13: index = 2;break;   //支付返回选号
                        case 14: showtoast('应用已上线');flag = false;break;   //上线完成
                        case 21: index = 0;break;   //下线
                        default: index = 0;break;
                    }
                }
            },
            fail:function(){
                flag = false;
                showtoast('网络异常，请稍后重试');
            }
        });
        var realAuth = isRealAuth();
        if(flag && realAuth != null){
            if(index==0){
                if(realAuth){
                    $("div.real-auth").show();
                }else{
                    $("div.not-real-auth").show();
                }
                //初始化
                cleanModal(type);
            }
            modalAction(index + 1);
            showBox(type);
        }
    }



    var ivrnumber = 1;

    //生成IVR
    function creatIVR(){
        $('.hideIVR').html('');
        //异步生成
        var ivr = ['02000100','02000200','02000300','0200400','0200500'];
        for (var i = 0; i < ivr.length; i++) {
            $('.hideIVR').append('<sapn class="hideIVR-p-'+(i+1)+'">'+ivr[i]+'</sapn>');
        }
        //赋值第一个
        $('#creatIVR').html(ivr[0]);
        ivrnumber = 1;
        return true;
    }

    function nolike(){
        ivrnumber++;
        if(ivrnumber==6){
            ivrnumber = 1;
        }
        var ivr = $('.hideIVR-p-'+ivrnumber).html();
        $('#creatIVR').html(ivr);

    }


    /**
     * 应用id
     * status 该应用状态
     */
    function delapp(id,status){
        bootbox.setLocale("zh_CN");
        if(status==1){
            bootbox.alert("当前应用正在运营中，请将起下线后进行删除", function(result) {}); return;
        }
        if(status==2){
            bootbox.confirm("删除应用：将会使该操作即时生效，除非您非常清楚该操作带来的后续影响", function(result) {
                if(result){
                    $.ajax({
                        url : "${ctx}/console/app/delete",
                        type : 'post',
                        async: false,//使用同步的方式,true为异步方式
                        data : {'id':id,'${_csrf.parameterName}':'${_csrf.token}'},//这里使用json对象
                        dataType: "json",
                        success : function(data){
                            showtoast(data.msg);
                        },
                        fail:function(){
                            showtoast('网络异常，请稍后重试');
                        }
                    });
                    $('#app-'+id).remove();
                }else{
                    //showtoast('取消');
                }
            });
        }
    }


    //应用下线
    function lineapp(id){
        bootbox.setLocale("zh_CN");
        bootbox.confirm("下线应用：将会使该操作即时生效，除非您非常清楚该操作带来的后续影响", function(result) {
            if(result){
                $('#trb-'+id).html('');
                $('#statusapp-'+id).html('未上线').removeClass('success').addClass('nosuccess');
                $('#trb-'+id).html('<a onclick="tabtarget('+id+')">申请上线</a>');
                showtoast('下线成功');
            }else{
                showtoast('取消');
            }

        });
    }




    //监听支付状态 返回true success
    function syncpay(){
        return true;
    }


    function createorder(){
        return true;
    }

    function pay(){
        if(!$('#readbook').is(':checked')) {
            showtoast('请先阅读IVR协议');
            return false;
        }
        return true;
    }

    //重选择事件
    function resetIVR(){
        tabModalBtn(2,'creatIVR()');
    }
</script>

</body>
</html>

