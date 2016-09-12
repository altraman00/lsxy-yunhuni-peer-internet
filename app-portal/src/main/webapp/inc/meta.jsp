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
<meta charset="utf-8" />
<meta name="renderer" content="webkit">
<meta name="description" content="app, web app, responsive, admin dashboard, admin, flat, flat ui, ui kit, off screen nav" />
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
<!-- Custom CSS -->
<link href="${resPrefixUrl }/css/style.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="${resPrefixUrl }/css/app.v2.css" type="text/css" />
<link rel="stylesheet" href="${resPrefixUrl }/css/own.css" type="text/css"/>
<link rel="stylesheet" href="${resPrefixUrl }/stylesheets/screen.css" type="text/css" media="screen" charset="utf-8">
<!--bootstrap css-->
<link rel="stylesheet" href="${resPrefixUrl }/bower_components/bootstrapvalidator/dist/css/bootstrapValidator.css">

<!--[if lt IE 9]> <script src="${resPrefixUrl }/js/ie/html5shiv.js" cache="false"></script> <script src="${resPrefixUrl }/js/ie/respond.min.js" cache="false"></script> <script src="${resPrefixUrl }/js/ie/excanvas.js" cache="false"></script> <![endif]-->
