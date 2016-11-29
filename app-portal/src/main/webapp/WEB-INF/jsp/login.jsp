<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@include file="/inc/import.jsp" %>
<!DOCTYPE html>
<html lang="en" class="no-js">
<head>
	<%@include file="/inc/metaLogin.jsp" %>
	<!--index.css-->
	<link href="${resPrefixUrl }/stylesheets/login.css" rel="stylesheet"  type="text/css" />
	<title>登陆</title>
</head>
<body class="login-bg">
<!--  container -->
<div class="container">
	<div class="row">
		<div class="col-sm-12 text-center">
			<div class="login-logo">
				<a a href="#" onclick="window.open('<%=SystemConfig.getProperty("global.official.website.url")%>')">
				<img src="${resPrefixUrl }/images/index/logo_plus.png" />
				</a>
			</div>
		</div>
	</div>
	<div class="login-box">
		<div class="row">
			<div class="col-sm-12 text-center">
				<h3 class="login-text">登录壹耘</h3>
			</div>
		</div>
		<div class="row">
			<form role="form" action="${ctx}/login" method="post" class="login-form" id="defaultForm">
				<div class="form-group">
					<input type="text" id="username" name="username" placeholder="请输入会员名称、手机号或邮箱" class="form-username form-control" id="form-username">
				</div>
				<div class="form-group">
					<input type="password" id="password" name="password" placeholder="请输入密码" class="form-password form-control" id="form-password" />
				</div>
				<div class="form-group form-block"   >
					<div class="col-md-6 remove-padding">
						<input type="text" name="validateCode" placeholder="验证码" class="form-control" id="form-code">
					</div>
					<div class="col-md-6 remove-padding border">
						<span class="code-img"><img src="${ctx}/vc/get?dt=${currentTime}" id="imgValidateCode"/></span>
					</div>
				</div>

				<div class="form-group form-block" >
					<div class="col-md-6 remove-padding remember" >
						<input type="checkbox" class="remember-check" id="rememberMe"/>   记住用户名
					</div>
					<div class="col-md-6 remove-padding border text-right">
						<a href="${ctx}/forget/index">忘记密码?</a>
					</div>
				</div>

				<button id="validateBtn" type="submit" class="btn btn-primary  btn-form">登录</button>
			</form>

			<div class="row margin-block"  >
				<div class="col-sm-12 text-center" >
					<a href="${ctx}/reg/index" class="register">还没注册账号，注册新账号</a>
				</div>
			</div>
			<div class="row margin-block" >
				<div class="col-sm-12 text-center " >
					<c:if test="${not empty param.er}">
						<c:if test="${param.er eq 'true'}"><a class="tips-error" >${SPRING_SECURITY_LAST_EXCEPTION.message}</a></c:if>
						<c:if test="${param.er eq 'vcer'}"><a class="tips-error" >验证码错误</a></c:if>

					</c:if>

				</div>
			</div>
		</div>
	</div>
	<footer>
		<div class="row">
			<div class="col-sm-12 text-center login-footer">
				Copyright 2016 壹耘 粤ICP备16048993号 All Rights Reserved 广州流水行云科技有限公司
			</div>
		</div>
	</footer>
</div>
<!-- /container -->


<!--jquery-->
<script src="${resPrefixUrl }/bower_components/jquery/dist/jquery.min.js"></script>

<!--bootstrapvalidator-->
<script src="${resPrefixUrl }/bower_components/bootstrapvalidator/dist/js/bootstrapValidator.min.js"></script>

<script src="${resPrefixUrl }/js/register/login.js" ></script>
<script src="${resPrefixUrl }/js/jquery.cookie.min.js" ></script>

<script type="text/javascript">
	$(document).ready(function() {

		$('#imgValidateCode').click(function() {
			$("#imgValidateCode").prop("src",ctx +  "/vc/get?dt="+(new Date().getTime()));
		});

		//判断之前是否有设置cookie，如果有，则设置【记住我】选择框
		if($.cookie('yunhuni_username')!=undefined){
			$("#rememberMe").attr("checked", true);
		}else{
			$("#rememberMe").attr("checked", false);
		}
		//读取cookie
		if($('#rememberMe:checked').length>0){
			$('#username').val($.cookie('yunhuni_username'));
		}

	});

	$("#defaultForm").submit(function(){
		beforeSubmit();
	});
	function beforeSubmit(){
		//监听【记住我】事件
		if($('#rememberMe:checked').length>0){//设置cookie
			$.cookie('yunhuni_username', $('#username').val(), {expires: 30});
		}else{//清除cookie
			$.removeCookie('yunhuni_username');
		}
	}

</script>

</body>
</html>