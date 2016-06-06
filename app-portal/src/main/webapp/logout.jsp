<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/inc/import.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en-US" lang="en-US">
<head>
	<%@include file="/inc/meta.jsp" %>
	<style type="text/css">
	</style>
	<script type="text/javascript">
	</script>
</head>    
<body class="lotusLogin lotusui" onload="">
	<div class="lotusLoginBox"> 
	<div class="lotusLoginContent">
	<div class="lotusLogo"><span class="lotusAltText">Lotus &lt;Product Name&gt;</span></div>
	<img src="${ctx}/resources/images/GenericApp_175x175.png" width="175" height="175" />
		<div class="lotusLoginForm">
		<input id="_usernum" name="_usernum" type="hidden"  value=""/>
			<form id="loginForm" method="post" action="j_security_check" aria-live="assertive">
				<div id="loginDiv" style="display:block">
				<div id="userNameDiv">
					<label for="username">用户名: 
					<span id="theSampleError" class="lotusFormError" >
						<img class="lotusIcon yourProductSprite yourProductSprite-msgError16" src="${ctx}/resources/css/images/blank.gif" alt="Error" />
					</span>		
					</label>
					<input class="lotusText" id="j_username" name="j_username" type="text"/>
				</div>								
				<div id="passwordDiv">
					<label for="password">密码:</label>
					<input class="lotusText" id="password" name="j_password" type="password" aria-required="true" />
				</div>		
				<div class="lotusBtnContainer">
					<input type="submit" class="lotusBtn lotusBtnSpecial lotusLeft" value="登陆" aria-required="true" />
				</div>
				</div>
			</form>
		</div><!-- end loginText -->
	
	<div class="lotusDescription">
</div>
</div><!-- end loginContent -->

<table class="lotusLegal" cellspacing="0" role="presentation">
<tr>
	<td><img class="" src="${ctx}/resources/css/images/blank.gif" alt="IBM" /></td>
	<td class="lotusLicense">
		联系我们 | 网站地图 | 企业邮箱 <br/>
			CopyRight © 2011-2012  版权所有 All Rights Reserved . <br/>
			
	</td>
</tr>
</table>

	</div>

<!-- START exampleBodyFooter.htm -->
<!-- This documentation was built on April 30 2010. (version: OneUI-2.1.0_149_20100430-1512) -->
<!--
Licensed Materials - Property of IBM.
(c) Copyright IBM Corporation 2001, 2009.  All Rights Reserved.
U.S. Government Users Restricted Rights:  Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<script type="text/javascript" src="${ctx}/resources/scripts/example-iframes-all.js"></script>
<script language="Javascript"  type="text/javascript">
	// Call a common script so updates can be kept to the .js files
	// This common processing script does stuff like browser and high contrast detection.
	// We create the function this way so we can pass in the proper path to an image to load
	var fn=function(x){lotusui_commonExamplePageFooterProcessing("${ctx}/resources/css/images/blank.gif")};
	lotusui_addEvent(window, "load", fn, true);
</script>
<!-- END exampleBodyFooter.htm -->



</body>
</html>