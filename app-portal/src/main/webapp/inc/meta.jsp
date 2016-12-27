<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@page import="com.lsxy.framework.core.utils.DateUtils"%>
<%@page import="java.util.Date"%>
<%@page import="com.lsxy.framework.core.security.SecurityUser"%>
<%@page import="com.lsxy.framework.config.SystemConfig"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%
	Date date = new Date();
	long currentTime = date.getTime();
	pageContext.setAttribute("currentTime",currentTime);
	String sToday = DateUtils.formatDate(date, "yyyy-MM-dd HH:mm:ss");
	request.setAttribute("today", sToday);
	String userAgent = request.getHeader("User-Agent");
	String resPrefixUrl = SystemConfig.getProperty("global.resPrefixUrl",request.getContextPath());
	String globalWebsite = SystemConfig.getProperty("global.official.website.url","https://www.oneyun.com");
	pageContext.setAttribute("resPrefixUrl",resPrefixUrl);

	String vt = SystemConfig.getVersionStamp();

	String rootUrl = SystemConfig.getProperty("portal.system.root.url");
	String devModel = SystemConfig.getProperty("system.developModel","true");
	SecurityUser currentUser = (SecurityUser)request.getSession().getAttribute("currentUser");
	String userName = null;
	if(currentUser != null){
		userName = currentUser.getUserName();
	}
%>
<script language="Javascript"  type="text/javascript">
	//服务器端时间
	var todayDt = new Date(${currentTime});
	var ctx = "${ctx}";
	var resPrefixUrl = "${resPrefixUrl}";
	var csrfParameterName = "${_csrf.parameterName}";
	var csrfToken = "${_csrf.token}";
</script>


<title><%=SystemConfig.getProperty("portal.system.title") %></title>
<script>
	var _hmt = _hmt || [];
	(function() {
		var hm = document.createElement("script");
		hm.src = "//hm.baidu.com/hm.js?0c61ac734b9dde2e0b1841e2750ec7bd";
		var s = document.getElementsByTagName("script")[0];
		s.parentNode.insertBefore(hm, s);
	})();
</script>
<script language="JavaScript" type="text/javascript">
	var flag = true;
	if(window.navigator.userAgent.indexOf("MSIE")>0) { if(window.navigator.userAgent.indexOf("MSIE 6.0")>0 || window.navigator.userAgent.indexOf("MSIE 7.0")>0 || window.navigator.userAgent.indexOf("MSIE 8.0")>0) {flag = false;} } if(!flag){
		document.write('<div style="width:100%; height: 100%; z-index: 99999;background-color: #FFFFFF; position: fixed;top: 0; "><div style="width:100%;height:50px;text-align: center; position: fixed;top: 0; background-color: #fffecb;color:#000; font-size: 14px; line-height: 50px; ">非常抱歉，网站暂时还不支持您的浏览器版本，我们的程序猿正在全力修复中~</div></div>');
	}
</script>

<meta charset="utf-8" />
<meta name="renderer" content="webkit">
<meta name="description" content="app, web app, responsive, admin dashboard, admin, flat, flat ui, ui kit, off screen nav" />
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
<!-- Custom CSS -->
<link rel="shortcut icon" href="${resPrefixUrl }/favicon.ico"/>
<link rel="bookmark" href="${resPrefixUrl }/favicon.ico"/>
<link href="${resPrefixUrl }/css/style.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="${resPrefixUrl }/css/app.v2.css" type="text/css" />
<link rel="stylesheet" href="${resPrefixUrl }/css/own.css" type="text/css"/>
<link rel="stylesheet" href="${resPrefixUrl }/stylesheets/screen.css" type="text/css" media="screen" charset="utf-8">
<!--bootstrap css-->
<link rel="stylesheet" href="${resPrefixUrl }/bower_components/bootstrapvalidator/dist/css/bootstrapValidator.css">

<!--[if lt IE 9]> <script src="${resPrefixUrl }/js/ie/html5shiv.js" cache="false"></script> <script src="${resPrefixUrl }/js/ie/respond.min.js" cache="false"></script> <script src="${resPrefixUrl }/js/ie/excanvas.js" cache="false"></script> <![endif]-->
