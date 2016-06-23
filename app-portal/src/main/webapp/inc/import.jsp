<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>

<%@page import="com.lsxy.framework.config.SystemConfig"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%
 String resPrefixUrl = SystemConfig.getProperty("global.resPrefixUrl",request.getContextPath());
 pageContext.setAttribute("resPrefixUrl",resPrefixUrl);
%>