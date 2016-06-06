<%@page import="java.util.Iterator"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Map"%>
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
	<% 
	boolean usecheckcode = true;
	String errorFlag = request.getParameter("er");
	String errorMsg = "密码错误,或账号被锁定"; 
	if(errorFlag == null || errorFlag.equals("")){
		%> 
		#errorMsg{
		visibility:hidden} 
		<% 
	}else{  
		if(errorFlag.equals("vcer")){ 
			errorMsg = "验证码错误！";
		}
	}
	
	%>
	#imgValidateCode{position: relative;top: 0px;left: 0px;}
	#checkedIcon{display:none}
	.lotusLogin .lotusLoginForm input{
		font-size:11pt;
	}
	</style> 

	<script type="text/javascript">
		function changeCode(){
			document.getElementById("imgValidateCode").src="${ctx}/common/getValidateCode?dt="+(new Date().getTime());
		}
		function submitForm(){
			if($("#j_username").val() == ""){
				showError("请输入用户名！");
				$("#j_username").focus();
				return;
			} 
			if($("#j_password").val() == ""){
				showError("请输入密码！");
				$("#j_password").focus();
				return;
			}
			<%
			if(usecheckcode){
				%>
					if($("validateCode").val() == ""){
						showError("请输入验证码！");
						$("validateCode").focus();
						return;
					}
				<%
			}
			%>
			loginForm.submit();
			//checkCode();
		}
		function showError(msg){
			$("#errorMsg")[0].style.visibility='visible';
			errorMsg.innerHTML=msg;
		}	

	</script>
</head>

<body>
	<div class="container">
        <h1>欢迎使用和声云客服平台</h1>   
        <div class="qrcode" id="qrcode">
        	<div id="qrcodex"></div>
        </div>
        <form  id="loginForm" method="post" action="${ctx}/j_spring_security_check" >
            <input styletype="text"  class="username" placeholder="邮箱地址" id="j_username" name="j_username" value="${param.j_username }" onkeydown="if(event.keyCode == 13){document.getElementById('j_password').focus();}">
            <p class="tips">&nbsp;&nbsp;亲，请检查用户名或密码!</p>
            <input type="password" class="password" placeholder="密码" id="j_password" name="j_password" type="password"  value="${param.j_password }" >
            <input type="Captcha" class="Captcha"  placeholder="验证码" maxlength="4"  id="validateCode" name="validateCode"  value="${param.validateCode }" onkeydown="if(event.keyCode == 13){submitForm();}" >
            <img alt="" src="${ctx}/common/getValidateCode" id="imgValidateCode" style="cursor: pointer;width: 80px;padding-top: 0px;margin:28px 0px 0px 15px;" onclick="javascript:changeCode();" title="看不清？请点击图片更换验证码">
            <a class="forgot" href="${ctx}/common/forgetPassword">忘记密码？</a>
            <input class="remember" type="checkbox"> 
            <p class="rember">记住我</p>  
            <span id="errorMsg"><%=errorMsg %></span>
            <button type="button" class="submit_button" onclick="submitForm();" >登录</button>            
        </form>
        <p class="rights">Copy Rights © 2013-2014 &nbsp;版权所有 All Rights Reserved 京ICP备&nbsp;14034791号</p>
    </div>	
    
</body>
 <!-- Javascript -->
    <script src="${resPrefixUrl}/portal/login/js/jquery-1.8.2.min.js" ></script>
    <script src="${resPrefixUrl}/portal/login/js/supersized.3.2.7.min.js" ></script>
    <script src="${resPrefixUrl}/portal/login/js/supersized-init.js" ></script>
    <script src="${resPrefixUrl}/portal/login/js/scripts.js" ></script>
    
	<script src="http://res.wx.qq.com/connect/zh_CN/htmledition/js/wxLogin.js"> 
	</script>
	<script>
	var obj = new WxLogin({
        id:"qrcodex", 
        appid: "<%=SystemConfig.getProperty("wxqrlogin.appid")%>", 
        scope: "snsapi_login", 
        redirect_uri: "<%=SystemConfig.getProperty("hscloud.url")%>/open/wxqrlogin",
        state: "<%=request.getSession().getId()%>",
        style: "white",
        href: ""
	});  
	</script>
</html>
