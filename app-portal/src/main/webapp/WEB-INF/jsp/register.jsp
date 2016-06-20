<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@include file="/inc/import.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en-US" lang="en-US">
<head>
    <%@include file="/inc/meta.jsp" %>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <meta http-equiv="X-UA-Compatible" content="edge"/>
    <!-- CSS -->
    <link href="${resPrefixUrl }/portal/login/img/favicon.ico" type="image/x-icon" rel="shortcut icon">
    <link rel="stylesheet" href="${resPrefixUrl}/portal/login/css/supersized.css">
    <link rel="stylesheet" href="${resPrefixUrl}/portal/login/css/style.css">
    <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
    <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <style>
        @media screen and(max-width:1000px){
            .container{width:220px}
        }
        #errorMsg{
            margin-top: 12px;
            display: block;
            margin-left: 100px;
            color: red;
        }
    </style>


    <style type="text/css">

        <c:if test="${empty param.er}">
        #errorMsg{
            visibility:hidden}
        </c:if>



        #imgValidateCode{position: relative;top: 0px;left: 0px;}
        #checkedIcon{display:none}
        .lotusLogin .lotusLoginForm input{
            font-size:11pt;
        }
    </style>

    <script type="text/javascript">
        function changeCode(){
            document.getElementById("imgValidateCode").src="${ctx}/vc/get?dt="+(new Date().getTime());
        }
        function showAA(){

        $('#myModal').modal('show');
        }
    </script>
</head>

<body>
<div class="container">
    <h1>云呼你 注册</h1>
    <div class="row">
        <div class="col-lg-6">
            <div style="display:inline-block;padding-top: 255px;">
                <span >已有账号？</span>
                <br>
                <a class="btn btn-default" href="${ctx}/login" role="button">登录</a>
            </div>
        </div>

        <div class="col-lg-6">
            <form  id="registerForm" method="post" action="${ctx}/login">
                <input type="text"  placeholder="会员名称" id="username" name="username"  onkeydown="if(event.keyCode == 13){document.getElementById('password').focus();}">
                <input type="text"  placeholder="手机号" id="phone" name="phone" >
                <input type="text"  placeholder="绑定邮箱地址" id="email" name="email" >
                <span id="errorMsg">
				</span>
                <button type="button" class="submit_button" onclick="bfSubmitForm();" >注册</button>
            </form>
        </div>
    </div>

    <form method="post" action="${ctx}/checkPhoneCode">
    <div class="modal fade" id="checkPhone" tabindex="-1" role="dialog" aria-hidden="true" style="color: #0e0e0e">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" style="width: auto"
                            data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 >验证手机号</h4>
                </div>
                <div class="modal-body">
                    <input type="text" id="checkPhoneCode" name="checkPhoneCode">
                    <button type="button" style="width: auto" class="btn btn-primary">
                        获取验证码
                    </button>
                </div>
                <div class="modal-footer">
                    <button type="button" style="width: auto" class="btn btn-primary">
                       确定
                    </button>
                </div>
            </div>
        </div>
    </div>
    </form>

    <p class="rights">Copy Rights © 2013-2014 &nbsp;版权所有 All Rights Reserved 京ICP备&nbsp;14034791号</p>
</div>

</body>
<!-- Javascript -->
<script src="${resPrefixUrl}/portal/login/js/supersized.3.2.7.min.js" ></script>
<script src="${resPrefixUrl}/portal/login/js/supersized-init.js" ></script>
<script src="${resPrefixUrl}/portal/register/js/scripts.js" ></script>

</html>
