<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/inc/import.jsp" %>

 <%
 response.getWriter().println("hello world");
 %>

<%
 response.sendRedirect( request.getContextPath() +"/console/index");
%>