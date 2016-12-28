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
                        <div class="head-box"><a href="#subNav" data-toggle="class:hide">
                            <i class=" iconfont icon-menu-left text"></i>
                            <i class=" iconfont icon-menu-right text-active"></i>
                        </a></div>
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
                                        <th>应用标识</th>
                                        <th>选择服务</th>
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
                                            <td><c:if test="${result.serviceType=='call_center'}">呼叫中心</c:if>
                                                <c:if test="${result.serviceType=='voice'}">语音服务</c:if></td>
                                            <td ><c:if test="${result.status==1}"><span style="color:#9dc940;"  id="statusapp-${result.id}">已上线</span></c:if>
                                            <c:if test="${result.status==2}"><span style="color:#ff0000;" id="statusapp-${result.id}">未上线</span></c:if>
                                            </td>
                                            <td><fmt:formatDate value="${result.createTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate> </td>
                                            <td class="operation">
                                                <a href="${ctx}/console/app/detail?id=${result.id}">详情</a> <span ></span>
                                                <a onclick="delapp('${result.id}')" >删除</a> <span ></span>
                                                <c:if test="${result.status==2}"> <a onclick="tabtarget('${result.id}','1')" >申请上线</a></c:if>
                                                <c:if test="${result.status==1}"> <span class="apply" id="trb-${result.id}"><a onclick="offline('${result.id}','${result.isIvrService}')">下线</a></span></c:if>
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
<div id="mobilebox-1" class="appliation-modal-box" style="display:none ;">
    <div class="addmobile1" >
        <div class="title">应用上线流程<a class="close_a modalCancel" data-type="1"></a></div>
        <div class="content" >
            <!--nav-->
            <div class="nav-modal-box">
                <ul class="nav-modal">
                    <li><a class="spot" data-action="1"></a><span class="lines"></span> </li>
                    <li><a class="spot" data-action="2"></a><span class="lines"></span></li>
                    <li><a class="spot" data-action="3"></a></li>
                </ul>
                <ul class="nav-modal-text navw-270">
                    <li><span class="text">实名认证</span> </li>
                    <li><span class="text">号码绑定</span> </li>
                    <li class="ml-20 mr-0"><span class="text">上线</span>  </li>
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
                    <div class="input text-center" >
                        <a type="button"  class="btn btn-primary btn-box tabModalBtn"  data-id="2" data-fun="selectNums()">下一步</a>
                    </div>
                </div>
                <!---end--->
            </div>

            <div class="contentModal" style="display: none" data-action="2">

                <!--没有可选号码-->
                <div id="selectOwnIvr" style="display: none" >
                    <div class="input text-center"  style="width: 89%;
                        color: #619B5A;
                        margin: 0 auto;
                        font-size: 14px;
                        background-color: #dff0d8;
                    ">
                        我的号码列表没有可选号码，如需租用号码，请前往“号码管理”中的“我的号码”
                    </div>
                    <div class="input text-center" >
                        <a type="button"  class="btn btn-primary btn-box tabModalBtn" href="${ctx}/console/telenum/callnum/index">立即前往</a>
                    </div>
                </div>

                <!--可选号码-->
                <div id="selectNewIvr" style="display: none">
                    <div class="input text-center">
                        <div  style="width: 89%;
                            color: #619B5A;
                            margin: 0 auto;
                            font-size: 14px;
                            background-color: #dff0d8;
                        ">
                            若您选择开通自定义IVR或者呼叫中心功能，则需要从我的号码列表中选择一个可呼入的号码与该应用绑定</div>
                    </div>
                    <div class="phone-table">
                        <table class="table table-striped">
                            <thead>
                            <tr>
                                <th></th>
                                <th>手机号码</th>
                                <th class="text-center">可呼入</th>
                                <th class="text-center">可呼出</th>
                                <th class="text-left-fixed"><span class="">归属地</span></th>
                            </tr>
                            </thead>
                            <tbody id="phonelist">
                            <tr>
                                <td><input type="checkbox" name="" /></td>
                                <td>13611460866</td>
                                <td class="text-center" >✔</td>
                                <td class="text-center">✘</td>
                                <td class="text-left-fixed">0757</td>
                            </tr>

                            </tbody>
                        </table>
                    </div>
                    <div class="input text-center" >
                        <a type="button"  class="btn btn-primary btn-box tabModalBtn" data-id="3" data-fun="goOnline()" >下一步</a>
                    </div>
                </div>

            </div>

            <div class="contentModal" style="display:none " data-action="3">
                <div class="input text-center" >
                    <img src="${resPrefixUrl }/images/index/l1.png" alt="" class="sre" />
                    <p>上线成功</p>
                </div>
                <div class="input text-center" >
                    <a href="" type="button"  class="btn btn-primary btn-box tabModalBtn" data-fun="refreshPage()" >上线成功</a>
                </div>
            </div>
            <div class="input">
                <div class="tips-error moadltips1 text-center" style="display: none">错误提示信息</div>
            </div>

        </div>
    </div>

</div>


<%@include file="/inc/footer.jsp"%>
<script type="text/javascript" src='${resPrefixUrl }/js/bootstrap-datepicker/js/bootstrap-datepicker.js'> </script>
<script type="text/javascript" src='${resPrefixUrl }/js/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js'> </script>
<script type="text/javascript" src='${resPrefixUrl }/js/application/list.js'> </script>
<script type="text/javascript">
    $('.close_a').click(function () {
        window.location.href=ctx+'/console/app/list';
    });
    var pageObj = {
        currentPageNo : '${pageObj.currentPageNo}',
        totalCount : '${pageObj.totalCount}',
        pageSize : '${pageObj.pageSize}',
        pageUrl : '${pageUrl}',
        totalPageCount : '${pageObj.totalPageCount}',
        initToTalPageCount :function(){
            this.totalPageCount = this.totalCount%this.pageSize==0?parseInt(this.totalCount/ this.pageSize) : parseInt(this.totalCount/ this.pageSize+1);
        }
    }
</script>
<script>

    //判断是否实名认证
    function isRealAuth(){
        var realAuth = null;
        //获取用户实名认证状态
        ajaxsync(ctx + "/console/account/auth/is_real_auth",null,function(result){
            realAuth = (result.data == 1 || result.data == 2);
        },"get");
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
        var index = 1;
        var flag = true;//是否能显示上线框
        //获取应用所处的步骤

        ajaxsync(ctx + "/console/app_action/"+ id,null,function(response){
            if(response.success){
                switch (response.data){
                    case 11: index = 2;break;   //选号
                    case 14: showtoast('应用已上线');flag = false;break;   //上线完成
                    case 21: index = 1;break;   //下线
                    default: index = 1;break;
                }
            }else{
                flag = false;
                showtoast(response.errorMsg?response.errorMsg:'数据异常，请稍后重试！');
            }
        },"get");

        if(flag){
            //进入实名认证
            if(index == 1){
                var realAuth = isRealAuth();
                if(realAuth != null){
                    if(realAuth){
                        $("div.real-auth").show();
                        $("div.not-real-auth").hide();
                    }else{
                        $("div.real-auth").hide();
                        $("div.not-real-auth").show();
                    }
                    //初始化
                    cleanModal(type);
                }else{
                    flag = false;
                }
            //进入选号
            }else if(index == 2){
                if(!selectNums()){
                    flag = false;
                }
            }
            //是否最后显示上线框
            if(flag){
                modalAction(index);
                showBox(type);
            }
        }

    }

    //生成IVR
    function selectNums(){
        var result = false;
        var appId = $('#modal-appid').val();
        $('.hideIVR').html('');
        $('#phonelist').html('');
        var isCall =  ["✘","✔"] ;
        var ownIvr = [];
        var needCalledNum = false;
        ajaxsync(ctx + "/console/app_action/select_num/" + appId,null,function(response){
            if(response.success ){
                ownIvr = response.data.ownNums;
                needCalledNum = response.data.needCalledNum;
                result = true;
            }else{
                result = false;
                showtoast(response.errorMsg?response.errorMsg:'数据异常，请稍后重试！');
            }
        },"get");


        //特殊判断 <i class="fa fa-exclamation-triangle"></i>
        var html = '';
        if(ownIvr==null){
            $('#selectOwnIvr').show();
            return ;
        }

        if(needCalledNum){
            $('#selectOwnIvr').show();
        }else{
            $('#selectNewIvr').show();
            if(ownIvr.length>0){
                for (var i = 0 ; i< ownIvr.length ; i ++){
                    html += '<tr><td><input type="checkbox" name="phonelist" value="'+ownIvr[i].phone+'" /></td>'
                    //特殊判断 <i class="fa fa-exclamation-triangle"></i>
                    if(ownIvr[i].lastUsed){
                        html += '<td data-toggle="tooltip" data-placement="right"  title="此号码为该应用上一次进行号码绑定时选择的号码">'+ownIvr[i].phone+'<i class="fa fa-exclamation-triangle cursor orange" ></i> </td>'
                    }else{
                        //普通号码
                        html +='<td>'+ownIvr[i].phone+'</td>'
                    }
                    html +='<td class="text-center" >'+isCall[ownIvr[i].isCalled]+'</td><td class="text-center" >'+isCall[ownIvr[i].isDialing]+'</td><td class="text-left-fixed" >'+ownIvr[i].areaCode+'</td></tr>'
                }
                $('#phonelist').html(html);
            }
        }

        return result;
    }




    /**
     * 应用id
     * status 该应用状态
     */
    function delapp(id){
        bootbox.setLocale("zh_CN");
//        if(status==1){
//            bootbox.alert("当前应用正在运营中，请将其下线后进行删除", function(result) {}); return;
//        }
//        if(status==2){
            bootbox.confirm("删除应用：将会使该操作即时生效，除非您非常清楚该操作带来的后续影响", function(result) {
                if(result){
                    ajaxsync(ctx + "/console/app/delete",{'id':id,'${_csrf.parameterName}':'${_csrf.token}'},function(response){
                        if(response.success){
                            $('#app-'+id).remove();
                            pageObj.totalCount--;
                            pageObj.initToTalPageCount();
                            if(pageObj.currentPageNo>pageObj.totalPageCount){
                                pageObj.currentPageNo = pageObj.totalPageCount;
                            }
                            if(pageObj.currentPageNo <= 0){
                                pageObj.currentPageNo=1;
                            }
                            showtoast("删除成功！",pageObj.pageUrl+"?pageNo="+pageObj.currentPageNo+"&pageSize="+pageObj.pageSize,1000);
                        }else{
                            if(response.errorCode=='0011'){
                                bootbox.alert("当前应用正在运营中，请将其下线后进行删除", function(result) {}); return;
                            }else{
                                showtoast("删除成功！"+response.errorMsg);
                            }
                        }
                    },"post");

                }else{
                    //showtoast('取消');
                }
            });
//        }
    }


    //应用下线
    function offline(id,type){
        bootbox.setLocale("zh_CN");
        var h1="下线应用：将会使该操作即时生效，除非您非常清楚该操作带来的后续影响";
        if(type==1){
            h1 = "应用下线后，选择的功能服务将终止，IVR号码关联将解除，应用上线后需要重新选择绑定（应用下线不影响IVR号码的月租费的收取）";
        }
        bootbox.confirm(h1, function(result){
            if(result){
                ajaxsync(ctx + "/console/app_action/offline",{'appId':id,'${_csrf.parameterName}':'${_csrf.token}'},function(response){
                    if(response.success){
                        $('#trb-'+id).html('');
                        $('#statusapp-'+id).html('未上线').attr('style','color:#ff0000;');
                        $('#trb-'+id).html('<a onclick="tabtarget(\''+id+'\',\'1\')">申请上线</a>');
                        showtoast('下线成功');
                    }else{
                        result = false;
                        showtoast(response.errorMsg?response.errorMsg:'数据异常，请稍后重试！');
                    }
                },"post");
            }else{
                showtoast('取消');
            }

        });
    }

    //进入应用上线
    function goOnline(){
        var result = false;
        var appId = $('#modal-appid').val();

        var number  = ''
        $('input[name="phonelist"]:checked').each(function(){
            if(number==''){
                number += $(this).val();
            }else{
                number += ","+$(this).val();
            }
            //number.push($(this).val())
            console.log("选中"+$(this).val())
        })

        console.log(number);

        //异步请求
//        if(number.length<=0){
//            showtoast('请选择一个可呼入的号码')
//            return false
//        }

        //上线
        ajaxsync(ctx + "/console/app_action/online",{appId:appId,nums:number},function(response){
            if(!response.success){
                result = false;
                showtoast(response.errorMsg?response.errorMsg:'数据异常，请稍后重试！');
            }else{
                result = true;
            }
        },"get");
        return result;
    }


    function refreshPage(){
        location.reload();
    }


</script>

</body>
</html>

