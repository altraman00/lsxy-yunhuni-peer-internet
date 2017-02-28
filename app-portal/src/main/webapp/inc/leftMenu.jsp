<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ page import="java.net.*" %>
<%
	Cookie[] cookies = null;
	cookies = request.getCookies();
	String mini  = null;
	String name  = null;
	if( cookies != null ){
		for (int i = 0; i < cookies.length; i++){
			if(cookies[i] != null && "hasMini".equals(cookies[i].getName())){
				mini = URLDecoder.decode(cookies[i].getValue(), "utf-8");
			}
		}
	}
	if("false".equals(mini)){
		name = "aside-mini aside-transition";
	}
%>

<aside class="bg-Green lter aside hidden-print <%= name %> " id="nav" style="width:184px">
<section class="w-f">
	<header class="head bg_green lter text-center clearfix">
		<a id='togglerMiniSidebar' href="#nav" data-toggle="class:nav-xs" class="text-center btn btn_b">
			<img class="img-shrink" src="${resPrefixUrl }/images/index/shrink.png"/>
		</a>
	</header>
	<section class="w-f" id="top_h">
		<div class="slim-scroll" data-height="auto" data-disable-fade-out="true" data-distance="0" data-size="5px" data-color="#2a9a88"> <!-- nav -->
			<nav class="nav-primary nav_green hidden-xs">
				<ul class="list">
					<li class=""> <a href="#" class="side-menu-link" ><i class="iconfont icon-menu-down"></i><span>用户中心</span> </a>
						<ul class="nav lt list">
							<li class="nav-router " data-router="account">
								<a data-toggle="tooltip" data-placement='right' title='账号管理' href="${ctx}/console/account/safety/index">
									<i class="iconfont icon-menu-user"></i> <span>账号管理</span>
								</a>
							</li>
							<li  class="nav-router " data-router="cost">
								<a data-toggle="tooltip" data-placement='right' title='费用管理' href="${ctx}/console/cost/consume">
									<i class="iconfont icon-menu-cost"></i>
									<span>费用管理</span> </a>
							</li>
						</ul>
					</li>
					<li class="">
						<a href="#" class="side-menu-link">
							<i class="iconfont icon-menu-down"></i><span>开发者中心</span> </a>
						<ul class="nav lt list">
							<li class="nav-router " data-router="app">
								<a data-toggle="tooltip" data-placement='right' title='应用管理' href="${ctx}/console/app/list"> <i class="iconfont icon-menu-app"></i> <span>应用管理</span> </a>
							</li>
							<li class="nav-router " data-router="telenum">
								<a data-toggle="tooltip" data-placement='right' title='号码管理' href="${ctx}/console/telenum/callnum/index"><i class="iconfont icon-menu-number"></i> <span>号码管理</span> </a>
							</li>
						</ul>
					</li>
					<li class="border-top">
						<a href="#" class="side-menu-link">
							<i class="iconfont icon-menu-down"></i><span>统计查询</span> </a>
						<ul class="nav lt list">
							<li>
								<a data-toggle="tooltip" data-placement='right' title='消费统计' href="${ctx}/console/statistics/consume/index" class="nav-router" data-router="statistics/consume"><i class="iconfont icon-menu-consume"></i> <span>消费统计</span> </a>
							</li>
							<li>
								<a data-toggle="tooltip" data-placement='right' title='会话统计' href="${ctx}/console/statistics/session/index"  class="nav-router" data-router="statistics/session"> <i class="iconfont icon-menu-session"></i> <span> <span>会话统计</span> </a>
							</li>
							<li>
								<a data-toggle="tooltip" data-placement='right' title='呼叫中心统计' href="${ctx}/console/statistics/callcenter/index" class="nav-router" data-router="statistics/callcenter"><i class="iconfont icon-menu-call"></i> <span>呼叫中心统计</span> </a>
							</li>
							<li>
								<a data-toggle="tooltip" data-placement='right' title='子账号综合统计' href="${ctx}/console/statistics/subaccount/index" class="nav-router" data-router="statistics/subaccount"><i class="iconfont icon-child-account"></i> <span>子账号综合统计</span> </a>
							</li>
							<li>
								<a data-toggle="tooltip" data-placement='right' title='详单查询' href="${ctx}/console/statistics/billdetail/notify" class="nav-router" data-router="statistics/billdetail"><i class="iconfont icon-menu-detail"></i> <span>详单查询</span> </a>
							</li>
						</ul>
					</li>
					<li class="border-top">
						<a  data-toggle="tooltip" data-placement='right' title='消息中心' href="${ctx}/console/message/account_message/index" class="canbehover nav-router"  data-router="message" >
							<i class="iconfont icon-menu-msg"></i>
							<span>消息中心</span>
						</a>
					</li>
					<li class="border-top ">
						<a data-toggle="tooltip" data-placement='right' title='服务与支持' href="${ctx}/console/customer/index" class="canbehover nav-router" data-router="customer">
							<i class="iconfont icon-menu-kefu"></i>
							<span>服务与支持</span> </a>
					</li>

				</ul>
			</nav>
			<!-- / nav --> </div>
	</section>
</section>
</aside>