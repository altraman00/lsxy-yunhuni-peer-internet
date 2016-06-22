<%@page import="com.lsxy.framework.core.utils.DateUtils"%>
<%@page import="java.util.Date"%>
<%@page import="com.lsxy.framework.core.utils.UUIDGenerator"%>
<%@page import="com.lsxy.framework.core.security.SecurityUser"%>
<%@page import="com.lsxy.framework.config.SystemConfig"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
    
<%
	Date today = new Date();
	long todayTime = today.getTime();
	String sToday = DateUtils.formatDate(today, "yyyy-MM-dd HH:mm:ss");
	request.setAttribute("today", sToday);
	String userAgent = request.getHeader("User-Agent");
//	String resPrefixUrl = SystemConfig.getProperty("global.resPrefixUrl",request.getContextPath());
//	pageContext.setAttribute("resPrefixUrl",resPrefixUrl);

	String vt = SystemConfig.getVersionStamp();

	String rootUrl = SystemConfig.getProperty("portal.system.root.url");
	String devModel = SystemConfig.getProperty("system.developModel","true");

	SecurityUser currentUser = (SecurityUser)request.getSession().getAttribute("currentUser");
	String _tenantName = null;
	if(currentUser != null){
		_tenantName = currentUser.getTenantName();
	}

%>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">

<link href='${resPrefixUrl}/resources/images/favicon.ico' type="image/x-icon" rel="shortcut icon">
<link rel="shortcut icon" href="<%=rootUrl %>/favicon.ico" type="image/x-icon" />

<link href="${resPrefixUrl}/common/scripts/bootstrap-3.3.0-dist/css/bootstrap.css" rel="stylesheet">
<link href="${resPrefixUrl}/common/css/font-awesome/css/font-awesome.min.css" rel="stylesheet">
<link href="${resPrefixUrl}/common/css/main.css" rel="stylesheet">

<script src="${resPrefixUrl}/common/scripts/jquery/jquery-1.11.1.min.js"></script>
<script src="${resPrefixUrl}/common/scripts/jquery/jquery.json.min.js"></script>
<script src="${resPrefixUrl}/common/scripts/jquery/jquery.bootstrap.min.js"></script>
<script src="${resPrefixUrl}/common/scripts/bootstrap-validation/bootstrap3-validation.js"></script>
<script src="${resPrefixUrl}/common/scripts/bootstrap-3.3.0-dist/js/bootstrap.min.js"></script>
<script src="${resPrefixUrl}/common/scripts/jquery/jquery.form.js"></script>

<script src="${resPrefixUrl}/common/scripts/common.js?vt=<%=vt%>"  type="text/javascript" ></script>
<script src="${resPrefixUrl}/common/scripts/browser.js?vt=<%=vt%>"  type="text/javascript" ></script>

<%
if(userAgent.toLowerCase().indexOf("firefox")>=0){
	%>
	<%@include file="/inc/firefox.jsp" %>
	<%
}
String agent = request.getHeader("User-Agent");
%>

<script language="Javascript"  type="text/javascript">
	//服务器端时间
	var todayDt = new Date(<%=todayTime%>);
	var ctx = "${ctx}";
	var resPrefixUrl = "${resPrefixUrl}";
	if (checkMobile()) {
		window.location.href = ctx + "/mobile/open/mobileAccessDenied.jsp";
	}

	//检测浏览器版本，低于ie9.0给予提示
	checkBrowser(function(){
		window.location.href = ctx + "/open/browser";
	})
</script>


<title><%=SystemConfig.getProperty("portal.system.title") %></title>
<div id="alertDiv" style="width:100%;height:30px;margin:0 auto;background-color: #feffcf;z-index: 999;text-align: center;display:none;">
			<span style="padding-top:7px;display: inline-block;float:left;position:absolute;left:25%;font-size: 14px;">您当前浏览器版本过低，可能导致有些模块无法正常使用，建议您<A href="http://windows.microsoft.com/zh-cn/internet-explorer/download-ie" target=_blank>升级IE浏览器</A>到8.0以上，<SPAN>或者点击下载</SPAN><A href="http://www.google.cn/intl/zh-CN/chrome/browser/" target=_blank>chrome浏览器</A></span>
			<span style="float:right;margin-right:5px;padding-top:7px;cursor: pointer;"><img src="${resPrefixUrl}/common/images/close.png" width="16" height="16" onclick="document.getElementById('alertDiv').style.display='none';"></span>
</div>
<script language="Javascript"  type="text/javascript">
	//判断浏览器版本是否正常
	var _browser = getBrowserInfo();
	var browserType = (_browser+"").replace(/[0-9.]/ig,""); 
	var browserVersion = (_browser+"").replace(/[^0-9.]/ig,"");

	if(browserType == "msie " && browserVersion < 9.0){//判断IE浏览器版本 低于IE9.0给予提示
		document.getElementById('alertDiv').style.display='block';
	}
</script>