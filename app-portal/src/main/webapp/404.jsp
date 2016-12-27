<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@include file="/inc/import.jsp"%>
<html>
	<head>
		<%@include file="/inc/meta.jsp"%>
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
		<%--<%@include file="/inc/mobile/meta_bootstrap.jsp"%>--%>
	</head>
	<body>
	<script >
		window.location.href='${globalWebsite}/404';
	</script>
		<%--<div class="container" style="margin-top:20px;">--%>
			<%--<div class="jumbotron">--%>
				<%--<h1>温馨提示</h1>--%>
				<%--<p>亲，您看到这个页面有可能是以下原因造成的：<br/>--%>
				<%--1.访问的页面我们正在努力开发中，尽情期待<br/>--%>
				<%--2.该页面已被移除<br/><br/>--%>
				<%--给您造成的不便我们深感抱歉，您可以发邮件到<%=SystemConfig.getProperty("global.mail.reception.email") %>与我们取得联系，我们会在24小时之内给您回复，谢谢您的支持与谅解!!--%>
			<%--</div> --%>
		<%--</div>--%>
	</body>
</html>