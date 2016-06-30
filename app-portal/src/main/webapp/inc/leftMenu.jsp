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
							<li>
								<a data-toggle="tooltip" data-placement='right' class="active" title='账号管理' href="${ctx}/console/account/safety/index">
									<i class="fa fa-user icon"></i> <span>账号管理</span>
								</a>
							</li>
							<li>
								<a data-toggle="tooltip" data-placement='right' title='费用管理' href="no_service.html">
									<i class="fa fa-database icon" aria-hidden="true"> </i>
									<span>费用管理</span> </a>
							</li>
						</ul>
					</li>
					<li class="">
						<a href="#" class="side-menu-link">
							<i class="fa fa-caret-down icon"> </i><span>开发者中心</span> </a>
						<ul class="nav lt list">
							<li>
								<a data-toggle="tooltip" data-placement='right' title='应用管理' href="fee.html"> <i class="fa fa-desktop icon"></i> <span>应用管理</span> </a>
							</li>
							<li>
								<a data-toggle="tooltip" data-placement='right' title='号码管理' href="${ctx}/console/number/call/index"><i class="fa fa-clone icon" aria-hidden="true"> </i> <span>号码管理</span> </a>
							</li>
						</ul>
					</li>
					<li class="border-top">
						<a  data-toggle="tooltip" data-placement='right' title='消息中心' href="#" class="canbehover" >
							<i class="fa fa-envelope" icon"> </i>
							<span>消息中心</span>
						</a>
					</li>
					<li class="border-top" >
						<a data-toggle="tooltip" data-placement='right' title='客服中心' href="#" class="canbehover" >
							<i class="fa fa-comment-o icon"> </i>
							<span>客服中心</span> </a>
					</li>
					<li class="border-top" >
						<a data-toggle="tooltip" data-placement='right' title='统计查询' href="#" class="canbehover" >
							<i class="fa fa-clock-o icon"> </i>
							<span>统计查询</span> </a>
					</li>
				</ul>
			</nav>
			<!-- / nav --> </div>
	</section>
</section>
