<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>

<script>
	$(function(){
		var node = "";
		var pathSplit = location.pathname.split("/");
		if(pathSplit.length > 2){
			node = pathSplit[2]
		}
		if(node != null && node != ""){
			$('.nav-router').each(function(){
				var nav = $(this).attr('data-router');
				if(nav==node){
					$(this).addClass('active')
				}
			});
		}
	})
</script>
