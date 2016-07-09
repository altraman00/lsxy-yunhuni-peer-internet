<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
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
					<li class=""> <a href="#" class="side-menu-link" > <i class="fa fa-caret-down icon"> </i><span>用户中心</span> </a>
						<ul class="nav lt list">
							<li class="nav-router " data-router="account">
								<a data-toggle="tooltip" data-placement='right' title='账号管理' href="${ctx}/console/account/safety/index">
									<i class="fa fa-user icon "></i> <span>账号管理</span>
								</a>
							</li>
							<li  class="nav-router " data-router="cost">
								<a data-toggle="tooltip" data-placement='right' title='费用管理' href="${ctx}/console/cost/consume">
									<i class="fa fa-database icon" aria-hidden="true"> </i>
									<span>费用管理</span> </a>
							</li>
						</ul>
					</li>
					<li class="">
						<a href="#" class="side-menu-link">
							<i class="fa fa-caret-down icon"> </i><span>开发者中心</span> </a>
						<ul class="nav lt list">
							<li class="nav-router " data-router="">
								<a data-toggle="tooltip" data-placement='right' title='应用管理' href="fee.html"> <i class="fa fa-desktop icon"></i> <span>应用管理</span> </a>
							</li>
							<li class="nav-router " data-router="telenum">
								<a data-toggle="tooltip" data-placement='right' title='号码管理' href="${ctx}/console/telenum/callnum/index"><i class="fa fa-clone icon" aria-hidden="true"> </i> <span>号码管理</span> </a>
							</li>
						</ul>
					</li>
					<li class="border-top">
						<a  data-toggle="tooltip" data-placement='right' title='消息中心' href="${ctx}/console/message/account_message/index" class="canbehover nav-router"  data-router="message" >
							<i class="fa fa-envelope icon"> </i>
							<span>消息中心</span>
						</a>
					</li>
					<li class="border-top ">
						<a data-toggle="tooltip" data-placement='right' title='客服中心' href="${ctx}/console/customer/index" class="canbehover nav-router" data-router="customer">
							<i class="fa fa-comment-o icon"> </i>
							<span>客服中心</span> </a>
					</li>
					<li class="border-top">
						<a href="#" class="side-menu-link">
							<i class="fa fa-caret-down icon"> </i><span>统计查询</span> </a>
						<ul class="nav lt list">
							<li>
								<a data-toggle="tooltip" data-placement='right' title='消费统计' href="${ctx}/console/statistics/consume/index" class="nav-router" data-router="statistics/consume"> <i class="fa fa-calendar icon"></i> <span>消费统计</span> </a>
							</li>
							<li>
								<a data-toggle="tooltip" data-placement='right' title='会话统计' href="${ctx}/console/statistics/session/index"  class="nav-router" data-router="statistics/session"><i class="fa fa-clock-o icon" aria-hidden="true"> </i> <span>会话统计</span> </a>
							</li>
							<li>
								<a data-toggle="tooltip" data-placement='right' title='详单查询' href="statistics_find.html"><i class="fa fa-clone icon" aria-hidden="true"> </i> <span>详单查询</span> </a>
							</li>
						</ul>
					</li>
				</ul>
			</nav>
			<!-- / nav --> </div>
	</section>
</section>
