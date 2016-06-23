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
	String _tenantName = null;
	if(currentUser != null){
		_tenantName = currentUser.getTenantName();
	}
%>
<script language="Javascript"  type="text/javascript">
	//服务器端时间
	var todayDt = new Date(${currentTime});
	var ctx = "${ctx}";
	var resPrefixUrl = "${resPrefixUrl}";
</script>

<title><%=SystemConfig.getProperty("portal.system.title") %></title>
