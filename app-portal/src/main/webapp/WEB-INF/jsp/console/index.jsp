<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@include file="/inc/import.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/inc/meta.jsp" %>
    <meta charset="utf-8" />
    <meta name="description" content="app, web app, responsive, admin dashboard, admin, flat, flat ui, ui kit, off screen nav" />
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
    <!-- Custom CSS -->
    <link href="${resPrefixUrl }/css/style.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="${resPrefixUrl }/css/app.v2.css" type="text/css" />
    <link rel="stylesheet" href="${resPrefixUrl }/css/own.css" type="text/css"/>
    <link rel="stylesheet" href="${resPrefixUrl }/stylesheets/screen.css" type="text/css" media="screen" charset="utf-8">
    <!--[if lt IE 9]> <script src="${resPrefixUrl }/js/ie/html5shiv.js" cache="false"></script> <script src="${resPrefixUrl }/js/ie/respond.min.js" cache="false"></script> <script src="${resPrefixUrl }/js/ie/excanvas.js" cache="false"></script> <![endif]-->
    <title>云分销</title>
</head>
<body>
<section class="vbox">
    <header class="bg-green bg header navbar navbar-fixed-top-xs bg-blue">
        <div class="navbar-header aside logo_b">
            <a href="#" class="navbar-brand logo_ab" data-toggle="fullscreen">
                <img src="${resPrefixUrl }/images/index/logo.png" class="m-r-sm logo_w2">
            </a>
        </div>
        <ul class="nav navbar-nav hidden-xs nav_b">
            <li class="dropdown"> <a href="${resPrefixUrl }/index.html" class="dropdown-toggle" data-toggle="dropdown"><span class="font-bold">商户管理控制台</span> </a> </li>
            <section class="dropdown-menu aside-xxl on animated fadeInLeft no-borders lt">
                <div class="row m-l-none m-r-none m-b-n-xs text-center">
                    <div class="col-xs-3">
                        <div class="padder_w"><a href="#" class="media list-group-item media_w border_r"><span class="block"><img src="${resPrefixUrl }/images/home.png"/></span> <small class="text-muted">首页</small></a></div>
                    </div>
                    <div class="col-xs-3">
                        <div class="padder_w"><a href="#" class="media list-group-item media_w"><span class="block"><img src="${resPrefixUrl }/images/distribution.png"/></span> <small class="text-muted">云分销</small></a></div>
                    </div>
                    <div class="col-xs-3">
                        <div class="padder_w"><a href="#" class="media list-group-item media_w"><span class="block"><img src="${resPrefixUrl }/images/cloud.png"/></span> <small class="text-muted">云众筹</small></a></div>
                    </div>
                    <div class="col-xs-3">
                        <div class="padder_w"><a href="#" class="media list-group-item media_w"><span class="block"> <img src="${resPrefixUrl }/images/User.png"/></span> <small class="text-muted">云客服</small></a></div>
                    </div>
                </div>
            </section>
            </li>
        </ul>
        <ul class="nav navbar-nav navbar-right hidden-xs nav-user nav_b">
            <li class="dropdown hidden-xs a-color"><a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="fa fa-fw fa-question"></i></a></li>
            <li class="hidden-xs"> <a href="#" class="dropdown-toggle" data-toggle="dropdown"> <i class="fa fa-bell"></i> <span class="badge badge-sm up bg-danger m-l-n-sm count">2</span> </a>
                <section class="dropdown-menu aside-xl">
                    <section class="panel bg-white">
                        <header class="panel-heading b-light bg-light"> <strong>您有<span class="count">2</span>通知</strong> </header>
                        <div class="list-group list-group-alt animated fadeInRight"><a href="#" class="media list-group-item"> <span class="pull-left thumb-sm"> <img src="${resPrefixUrl }/images/avatar.jpg" alt="John said" class="img-circle"> </span> <span class="media-body block m-b-none">提醒<br>
                            <small class="text-muted">10分钟以前</small></span></a><a href="#" class="media list-group-item"> <span class="media-body block m-b-none">待办<br>
                            <small class="text-muted">1小时以前</small></span></a></div>
                        <footer class="panel-footer text-sm"> <a href="#" class="pull-right"> <i class="fa fa-cog"></i> </a> <a href="#notes" data-toggle="class:show animated fadeInRight">查看所有通知</a> </footer>
                    </section>
                </section>
            </li>
            <li class="dropdown a-color"><a href="#" class="dropdown-toggle" data-toggle="dropdown"><span class="thumb-sm avatar pull-left"> <img src="${resPrefixUrl }/images/avatar.jpg"> </span>渠道商<b class="caret"></b> </a>
                <ul class="dropdown-menu animated fadeInRight">
                    <span class="arrow top"></span>
                    <li> <a href="#">设置</a> </li>
                    <li> <a href="${resPrefixUrl }/profile.html">简介</a> </li>
                    <li> <a href="${ctx}/logout" >退出</a> </li>
                </ul>
            </li>
        </ul>
    </header>
    <section>
        <section class="hbox stretch"> <!-- .aside -->
            <aside class="bg-Green lter aside hidden-print" id="nav">
                <section class="w-f scrollable">
                    <header class="head bg_green lter text-center clearfix"> <a href="#nav" data-toggle="class:nav-xs" class="text-center btn btn_b"> <img class="text" src="${resPrefixUrl }/images/left.png"/><img class="text-active" src="${resPrefixUrl }/images/right.png"/></a> </header>
                    <section class="w-f scrollable" id="top_h">
                        <div class="slim-scroll" data-height="auto" data-disable-fade-out="true" data-distance="0" data-size="5px" data-color="#2a9a88"> <!-- nav -->
                            <nav class="nav-primary nav_green hidden-xs">
                                <ul class="list">
                                    <li > <a href="#" > <i class="fa fa-caret-down icon"> </i><span>产品服务</span> </a>
                                        <ul class="nav lt list">
                                            <li> <a href="${resPrefixUrl }/no_distribution.html"> <i class="fa fa-dashboard icon"> </i> <span>云分销</span> </a> </li>
                                            <li> <a href="${resPrefixUrl }/no_service.html"> <i class="fa fa-headphones icon"> </i> <span>云客服</span> </a> </li>
                                        </ul>
                                    </li>
                                    <li > <a href="#"> <i class="fa fa-caret-down icon"> </i><span>个人中心</span> </a>
                                        <ul class="nav lt list">
                                            <li> <a href="${resPrefixUrl }/fee.html"> <i class="fa fa-money icon"></i> <span>费用管理</span> </a> </li>
                                            <li> <a href="${resPrefixUrl }/message.html"><i class="fa fa-envelope icon"> </i> <span>消息公告</span> </a> </li>
                                            <li> <a href="${resPrefixUrl }/Work_order.html"><i class="fa fa-edit icon"> </i> <span>工单处理</span> </a> </li>
                                            <li> <a href="${resPrefixUrl }/user.html"> <i class="fa fa-user icon"></i> <span>账户账号</span> </a> </li>
                                        </ul>
                                    </li>
                                    <li > <a href="#" > <i class="fa fa-caret-down icon"> </i><span>基础服务</span> </a>
                                        <ul class="nav lt list">
                                            <li> <a href="${resPrefixUrl }/wechat.html"> <i class="fa fa-comments icon"></i><span>微信服务</span> </a> </li>
                                            <li> <a href="${resPrefixUrl }/fans.html"> <i class="fa fa-users icon"></i><span>粉丝服务</span> </a> </li>
                                            <li> <a href="${resPrefixUrl }/content.html"> <i class="fa fa-floppy-o icon"></i> <span>内容服务</span> </a> </li>
                                        </ul>
                                    </li>
                                </ul>
                            </nav>
                            <!-- / nav --> </div>
                    </section>
                </section>
            </aside>
            <!-- /.aside -->
            <section id="content">
                <section class="hbox stretch">
                    <!-- .aside -->
                    <aside class="bg-green lter aside-sm hidden-print ybox" id="subNav">
                        <section class="vbox">
                            <div class="wrapper header"><span class="margin_lr"></span> <i class="fa fa-dashboard icon"> </i><span class="margin_lr"></span>云分销</div>
                            <section class="scrollable">
                                <div class="slim-scroll">
                                    <!-- nav -->
                                    <nav class="hidden-xs">
                                        <ul class="nav">
                                            <li>
                                                <div class="width_h"><span><i class="fa fa-angle-down text"></i> <i class="fa fa-angle-up text-active"></i></span><span class="margin_lr">粉丝管理</span></div>
                                                <ul class="nav lt">
                                                    <a href="#">
                                                        <li class="b-light">关系管理</li>
                                                    </a> <a href="#">
                                                    <li class="b-light">分组管理</li>
                                                </a> <a href="#">
                                                    <li class="b-light">店铺管理</li>
                                                </a>
                                                </ul>
                                            </li>
                                            <li>
                                                <div class="width_h"><span><i class="fa fa-angle-down text"></i> <i class="fa fa-angle-up text-active"></i></span><span class="margin_lr">商铺商品</span></div>
                                                <ul class="nav lt">
                                                    <a href="#">
                                                        <li class="b-light">商品列表</li>
                                                    </a> <a href="#">
                                                    <li class="b-light">商品分类</li>
                                                </a> <a href="#">
                                                    <li class="b-light">配送方式</li>
                                                </a>
                                                </ul>
                                            </li>
                                            <li>
                                                <div class="width_h"><span><i class="fa fa-angle-down text"></i> <i class="fa fa-angle-up text-active"></i></span><span class="margin_lr">订单管理</span></div>
                                                <ul class="nav lt">
                                                    <a href="#">
                                                        <li class="b-light">订单列表</li>
                                                    </a> <a href="#">
                                                    <li class="b-light">批量发货</li>
                                                </a>
                                                </ul>
                                            </li>
                                            <li>
                                                <div class="width_h"><span><i class="fa fa-angle-down text"></i> <i class="fa fa-angle-up text-active"></i></span><span class="margin_lr">佣金管理</span></div>
                                                <ul class="nav lt">
                                                    <a href="#">
                                                        <li class="b-light">佣金记录</li>
                                                    </a> <a href="#">
                                                    <li class="b-light">体现管理</li>
                                                </a> <a href="#">
                                                    <li class="b-light">流量变现</li>
                                                </a>
                                                </ul>
                                            </li>
                                            <li>
                                                <div class="width_h"><span><i class="fa fa-angle-down text"></i> <i class="fa fa-angle-up text-active"></i></span><span class="margin_lr">优惠营销</span></div>
                                                <ul class="nav lt">
                                                    <a href="#">
                                                        <li class="b-light">卡券列表</li>
                                                    </a> <a href="#">
                                                    <li class="b-light">赠券</li>
                                                </a> <a href="#">
                                                    <li class="b-light">购物得券</li>
                                                </a>
                                                </ul>
                                            </li>
                                            <li>
                                                <div class="width_h"><span><i class="fa fa-angle-down text"></i> <i class="fa fa-angle-up text-active"></i></span><span class="margin_lr">文章</span></div>
                                                <ul class="nav">
                                                    <a href="#">
                                                        <li class="b-light">文章列表</li>
                                                    </a>
                                                </ul>
                                            </li>
                                            <li>
                                                <div class="width_h"><span><i class="fa fa-angle-down text"></i> <i class="fa fa-angle-up text-active"></i></span><span class="margin_lr">店铺设置</span></div>
                                                <ul  class="nav lt">
                                                    <a href="#">
                                                        <li class="b-light">店铺规则</li>
                                                    </a> <a href="#">
                                                    <li class="b-light">嵌入代码</li>
                                                </a> <a href="#">
                                                    <li class="b-light">常见问答</li>
                                                </a>
                                                </ul>
                                            </li>
                                            <li>
                                                <div class="width_h"><span><i class="fa fa-angle-down text"></i> <i class="fa fa-angle-up text-active"></i></span><span class="margin_lr">魔法页面</span></div>
                                                <ul  class="nav lt">
                                                    <a href="#">
                                                        <li class="b-light">魔法列表</li>
                                                    </a>
                                                </ul>
                                            </li>
                                        </ul>
                                    </nav>
                                </div>
                            </section>
                        </section>
                    </aside>
                    <aside>
                        <section class="vbox xbox">
                            <div class="head-box"> <a href="#subNav" data-toggle="class:hide"> <i class="fa fa-angle-left text"></i> <i class="fa fa-angle-right text-active"></i> </a> </div>
                            <section class="scrollable wrapper w-f">
                                <section class="panel panel-default yunhuni-personal">
                                    <div class="row m-l-none m-r-none bg-light lter">
                                        <div class="col-md-4 padder-v">
                                            <div class='wrapperBox'>
                                            <span class="pull-left m-r-sm">
                                              <img src="${resPrefixUrl }/images/photo.png" width="50"/>
                                            </span>
                                            <span class="h5 block m-t-xs">
                                              <strong>余额</strong>
                                            </span>
                                            <span>
                                              <small class="text-muted text-uc account-number">998</small>
                                              <small class="account-number-decimal">.00</small>
                                              元
                                            </span>
                                                <div class="box-footer">
                                                    <button class="btn btn-primary" >充值</button>
                                                    <button class="btn btn-default" >消费情况</button>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-4 padder-v">
                                            <div class='wrapperBox'>
                                            <span class="h5 block m-t-xs">
                                              <strong>线路状况</strong>
                                            </span>
                                            <span>
                                              当前
                                              <small class="text-muted text-uc account-number">10</small>
                                              线
                                            </span>
                                            </div>
                                        </div>
                                        <div class="col-md-4 padder-v">
                                            <div class='wrapperBox'>
                                            <span class="h5 block m-t-xs">
                                              <strong>套餐剩余量</strong>
                                            </span>
                                                <div class='account-left'>
                                              <span class="w-half">
                                                <img src="${resPrefixUrl }/images/index/voice.png" alt="">
                                                语音剩余:  <small class="account-number-small">100</small> 分钟
                                              </span>
                                              <span class="w-half">
                                                <img src="${resPrefixUrl }/images/index/meeting.png" alt="">
                                                会议剩余:  <small class="account-number-small">198</small> 分钟
                                              </span>
                                              <span class="">
                                                <img  src="${resPrefixUrl }/images/index/message.png" alt="">
                                                短信剩余:  <small class="account-number-small">225</small> 分钟
                                              </span>
                                                </div>
                                                <div class="box-footer">
                                                    <button class="btn btn-default fr" >购买流量包</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </section>
                                <section class="panel panel-default pos-rlt clearfix admin-padding">
                                    <header class="panel-heading">
                                        <ul class="nav nav-pills pull-right">
                                            <li> <a href="#" class="panel-toggle text-muted"><i class="fa fa-caret-down text-active"></i><i class="fa fa-caret-up text"></i></a> </li>
                                        </ul>
                                        <div class="h5">开发者账号</div>
                                    </header>
                                    <div class="panel-body clearfix">
                                        <p>
                                            REST API: http://api.yunhuni.com/1234135312321414134/
                                        <span>
                                          <a href="">API文档</a>
                                        </span>
                                        </p>
                                        <p>
                                            SecretKey 1234135312321414134
                                        <span>
                                          <a href="">重新生成</a>
                                        </span>
                                        </p>
                                    </div>
                                </section>
                                <section class="panel panel-default pos-rlt clearfix admin-padding">
                                    <div class="panel-body clearfix">
                                        <div class="none-app">
                                            <img src="${resPrefixUrl }/images/index/chicken.png" alt="">
                                        <span>
                                          还没创建应用，创建应用HAPPY一下吧<br/></br>
                                            <a href="">创建应用</a>
                                        </span>
                                        </div>
                                    </div>
                                </section>
                                <section class="panel panel-default pos-rlt clearfix admin-padding app-list">
                                    <header class="panel-heading">
                                        <div class="h5">应用1</div>
                                    </header>
                                    <div class="panel-body clearfix">
                                        <div class="app-status-right dropdown fl">
                                          <span class="pull-left m-r-sm">
                                            <img src="${resPrefixUrl }/images/photo.png" width="50"/></span>
                                          <span class="h5 block m-t-xs">APPID：
                                            <strong>214132513515135132512334134</strong>
                                            <small>已上线</small>
                                          </span>
                                            <small class="text-muted m-t-xs text-uc">这里是描述适用商户：龙米、集美汇等3个</small>
                                        </div>
                                        <div class="app-status-left fr">
                                            <ul class="app-status-list">
                                                <li><img src="${resPrefixUrl }/images/index/status_1.png" alt=""> 1小时内呼叫量: <span>100</span> </li>
                                                <li><img src="${resPrefixUrl }/images/index/status_2.png" alt=""> 1天内呼叫量: <span>100</span> </li>
                                                <li><img src="${resPrefixUrl }/images/index/status_3.png" alt=""> 当天呼叫并发: <span>100</span> </li>
                                            </ul>
                                            <a href="" class="fr">详情</a>
                                        </div>
                                    </div>
                                </section>
                                <section class="panel panel-default pos-rlt clearfix admin-padding app-list">
                                    <header class="panel-heading">
                                        <div class="h5">应用2</div>
                                    </header>
                                    <div class="panel-body clearfix">
                                        <div class="app-status-center dropdown fl">
                                          <span class="pull-left m-r-sm">
                                            <img src="${resPrefixUrl }/images/photo.png" width="50"/></span>
                                          <span class="h5 block m-t-xs">APPID：
                                            <strong>214132513515135132512334134</strong>
                                            <small>未上线</small>
                                          </span>
                                            <small class="text-muted m-t-xs text-uc">这里是描述适用商户：龙米、集美汇等3个</small>
                                        </div>
                                    </div>
                                </section>
                            </section>
                        </section>
                    </aside>
                </section>
                <a href="#" class="hide nav-off-screen-block" data-toggle="class:nav-off-screen" data-target="#nav"></a>
            </section>
        </section>
    </section>
</section>
<script src="${resPrefixUrl }/js/app.v2.js"></script> <!-- Bootstrap --> <!-- App -->
<script src="${resPrefixUrl }/js/charts/flot/jquery.flot.min.js" cache="false"></script>
<script src="${resPrefixUrl }/js/charts/flot/demo.js" cache="false"></script>
<script>
    // slimScrollDiv
    setTimeout(function(){
        var slim = document.querySelector('.slimScrollDiv')
        slim.style.height = (window.innerHeight - 80) + 'px'
    }, 1000)

    $("a[href='#nav']").click(function(){
        $('#nav').toggleClass('aside-mini')

        var hasMini = $('#nav').hasClass('aside-mini')

        // 防止冒泡
        return false
    })
</script>
</body>
</html>
