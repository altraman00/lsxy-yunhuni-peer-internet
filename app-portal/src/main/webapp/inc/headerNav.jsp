<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>

<%@ page import="java.util.*" %>
<%
String _tenantUn = currentUser.getTenantUn();

ApplicationContext _ctx = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());

String userName = currentUser.getName();
%>
<form method="post" action="${ctx }/logout" id="logoutForm" name="logout" style="display:none">
	<input type="hidden" name="${_csrf.parameterName}"
		   value="${_csrf.token}" />
</form>

<script>
function logout(){
	document.getElementById("logoutForm").submit();
}
</script>


<nav class="navbar navbar-white" role="navigation">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>

		<a class="navbar-brand nav-logo" href="#">
			<img style="display: inline;" alt="" src="${resPrefixUrl}/common/images/navgo.png">
			<span style="font-weight: bold; margin-left:10px;">云呼你-用户控制台</span>
		</a>
    </div>

    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse">
      <ul class="nav navbar-nav navbar-right">
        <li class="lotusFirst">
			<span class="navbar-text" style="font-style: italic;"><%=userName %></span>
		</li> 

		<li>
			<a href="javascript:logout();">退出</a>
		</li>
      </ul>
    </div><!-- /.navbar-collapse -->
</nav>