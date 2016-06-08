<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>

<div class="menu_box" id="menuBar">
	<dl class="menu no_extra">
		<dt class="menu_title">
			<i class="icon_menu"
				style="background: url(${resPrefixUrl}/common/images/icon_menu_set.png) no-repeat;"></i>首页
		</dt>
		<dd class="menu_item">
			<a href="${ctx}/console/index">首页</a>
		</dd>
		<dd class="menu_item">
			<a href="${ctx}/console/xtdt">系统动态</a>
		</dd>

	</dl>
	<dl class="menu">
		<dt class="menu_title">
			<i class="icon_menu"
				style="background: url(${resPrefixUrl}/common/images/icon_menu_set.png) no-repeat;"></i>用户中心
		</dt>
		<dd class="menu_item">
			<a href="${ctx}/tenant/admin/customers">账号</a>
		</dd>
		<dd class="menu_item">
			<a href="${ctx}/tenant/admin/customer/customerlabels">费用中心</a>
		</dd>

	</dl>
	<dl class="menu ">
		<dt class="menu_title">
			<i class="icon_menu"
				style="background: url(${resPrefixUrl}/common/images/icon_menu_channel.png) no-repeat;"></i>开发者中心
		</dt>
		<dd class="menu_item">
			<a href="${ctx}/tenant/admin/channel/channellist">应用管理</a>
		</dd>
		<dd class="menu_item">
			<a href="${ctx}/tenant/admin/zx/zxset">放音文件管理</a>
		</dd>
		<dd class="menu_item">
			<a href="${ctx}/tenant/admin/zx/zxset">测试号码管理</a>
		</dd>
	</dl>
	<dl class="menu">
		<dt class="menu_title">
			<i class="icon_menu"
				style="background: url(${resPrefixUrl}/common/images/icon_menu_set.png) no-repeat;"></i>资源中心
		</dt>
		<dd class="menu_item">
			<a href="${ctx}/tenant/admin/systemset/basic?tab=basic">号码池列表</a>
		</dd>
		<dd class="menu_item">
			<a href="${ctx}/tenant/admin/systemset/basic?tab=basic">测试号码绑定</a>
		</dd>
	</dl>
	<dl class="menu">
		<dt class="menu_title">
			<i class="icon_menu"
			   style="background: url(${resPrefixUrl}/common/images/icon_menu_set.png) no-repeat;"></i>消息中心
		</dt>
		<dd class="menu_item">
			<a href="${ctx}/tenant/admin/systemset/basic?tab=basic">通知公告</a>
		</dd>
	</dl>
	<dl class="menu">
		<dt class="menu_title">
			<i class="icon_menu"
			   style="background: url(${resPrefixUrl}/common/images/icon_menu_set.png) no-repeat;"></i>客服中心
		</dt>
		<dd class="menu_item">
			<a href="${ctx}/tenant/admin/systemset/basic?tab=basic">问题反馈</a>
		</dd>
	</dl>
	<dl class="menu">
		<dt class="menu_title">
			<i class="icon_menu"
			   style="background: url(${resPrefixUrl}/common/images/icon_menu_set.png) no-repeat;"></i>统计查询
		</dt>
		<dd class="menu_item">
			<a href="${ctx}/tenant/admin/systemset/basic?tab=basic">近15天消费统计</a>
		</dd>
		<dd class="menu_item">
			<a href="${ctx}/tenant/admin/systemset/basic?tab=basic">应用消费情况</a>
		</dd>
		<dd class="menu_item">
			<a href="${ctx}/tenant/admin/systemset/basic?tab=basic">单向外呼详单查询</a>
		</dd>
		<dd class="menu_item">
			<a href="${ctx}/tenant/admin/systemset/basic?tab=basic">双向外呼详单查询</a>
		</dd>
		<dd class="menu_item">
			<a href="${ctx}/tenant/admin/systemset/basic?tab=basic">电话会议查询</a>
		</dd>
	</dl>
</div>
<script>
	(function() {
		$('#menuBar').find('.menu').each(function() {
			if ($(this).find('dd').length == 0) {
				$(this).remove();
			}
		});
		
		if (!$('#menuBar').find('.menu:first').hasClass('no_extra')) {
			$('#menuBar').find('.menu:first').addClass('no_extra');
		}
	})();

	$(document).ready(function(){
		$(".menu_item a[href='"+location.pathname+"']").parent().addClass("selected");
	});


	
</script>
