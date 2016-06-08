<%@page import="org.springframework.context.annotation.Import"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@include file="/inc/import.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/inc/meta.jsp" %>

    <style>
        .t-status {
            position: relative;
            line-height: 100%;
        }

        .t-status h4 {
            margin-left: 55px;
        }
        .t-status p {
            margin-bottom: 0px;
            margin-left: 55px;
            font-size: 12px;
        }

        .c-status {
            position: relative;
            line-height: 100%;
        }

        .c-status h4 {
            margin-left: 45px;
            font-size: 16px;
        }
        .c-status p {
            margin-bottom: 0px;
            margin-left: 45px;
            font-size: 9px;
        }

        .type {
            position: absolute;
            top: 0;
            padding: 0px 10px 0px 0px;
            border-right: 1px solid #ededed;
            margin-left: 5px;
            width: 40px;
            height: 30px;
            font-size: 30px;
        }

        .min-type {
            position: absolute;
            top: 0;
            padding: 0px 10px 0px 0px;
            margin-left: 5px;
            width: 30px;
            height: 30px;
            font-size: 28px;
        }
    </style>
</head>

<body>
<%@include file="/inc/headerNav.jsp" %>
<!-- 主界面 -->

<div class="container-fluid new_main">
    <div class="col_side">
        <%@include file="/inc/leftMenu.jsp" %>
    </div>
    <div class="col_main">
        <div class="main_bd" style="padding:10px;">
            <div class="row" style="margin-top:10px;">
                <!-- 关注数 -->
                <div class="col-md-3 col-sm-6 col-xs-12" id="subscribeChart">
                    <div class="panel panel-default">
                        <div class="panel-body" style="padding:10px;">
                            <div class="t-status" style="color : #48B560;">
                                <a href="${ctx}/tenant/admin/custStatistic"><h4><span id="subCounts">0</span></h4></a>
                                <p class="help-block">今日xx量</p>
                                <div class="type">
                                    <span class="glyphicon glyphicon-eye-open"></span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- 取消关注数 -->
                <div class="col-md-3 col-sm-6 col-xs-12" id="unsubscribeChart">
                    <div class="panel panel-default">
                        <div class="panel-body" style="padding:10px;">
                            <div class="t-status" style="color : #d13736;">
                                <a href="${ctx}/tenant/admin/custStatistic"><h4><span id="unSubCounts">0</span></h4></a>
                                <p class="help-block">今日xx量</p>
                                <div class="type">
                                    <span class="glyphicon glyphicon-eye-close"></span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- 新增会话数 -->
                <div class="col-md-3 col-sm-6 col-xs-12" id="newSessionChart">
                    <div class="panel panel-default">
                        <div class="panel-body" style="padding:10px;">
                            <div class="t-status" style="color : #15295C;">
                                <h4><span id="sessionCounts">0</span></h4>
                                <p class="help-block">今日xx数</p>
                                <div class="type">
                                    <span class="glyphicon glyphicon-headphones"></span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- 新增留言数 -->
                <div class="col-md-3 col-sm-6 col-xs-12" id="newMsgChart">
                    <div class="panel panel-default">
                        <div class="panel-body" style="padding:10px;">
                            <div class="t-status" style="color : #F26645;">
                                <h4><span id="msgCounts">0</span></h4>
                                <p class="help-block">最新xx数</p>
                                <div class="type">
                                    <span class="glyphicon glyphicon-envelope"></span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row" style="margin-top:10px;">
                <!-- 会话数统计图 -->
                <div id="sessionDiv" class="col-md-12 col-sm-12 col-xs-12">
                    <div id="sessionChart">
                    </div>
                </div>
            </div>
            <div class="row" style="margin-top:30px;">
                <!-- 客户数统计图 -->
                <div id="customerDiv" class="col-md-12 col-sm-12 col-xs-12">
                    <div id="customerChart">
                    </div>
                </div>
            </div>
            <div class="row" style="margin-top:30px;">
                <div class="col-md-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">系统动态</div>
                        <div class="panel-body">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@include file="/inc/footer.jsp" %>
<script src="${resPrefixUrl}/common/scripts/ichart/ichart.1.2.min.js"></script>
<script src="${resPrefixUrl}/portal/console/index/scripts/index.js"></script>
<script>
    setCookie('hsy_last_page', 'admin', 10);
</script>
</body>
</html>