<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<div class="tips-toast"></div>
<script src="${resPrefixUrl }/js/app.v2.js"></script> <!-- Bootstrap --> <!-- App -->
<script src="${resPrefixUrl }/js/charts/flot/jquery.flot.min.js" cache="false"></script>
<script src="${resPrefixUrl }/js/bootbox.min.js"></script>
<script src="${resPrefixUrl }/js/charts/flot/demo.js" cache="false"></script>
<script src="${resPrefixUrl }/bower_components/bootstrapvalidator/dist/js/bootstrapValidator.min.js"></script>
<script src="${resPrefixUrl }/js/jquery.cookie.min.js" ></script>
<script src="${resPrefixUrl }/js/yunhuni.js" ></script>


<script>
	$(function(){
		var node = "";
		var pathSplit = location.pathname.split("/");
		if(pathSplit.length > 2){
			node = pathSplit[2]
		}
		if(node=='statistics'){
			node=pathSplit[2]+'/'+pathSplit[3];
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
	var showtoastNum='';
	function showtoast(tips,url,times) {
		if(showtoastNum!=''){
			clearInterval(showtoastNum);
		}
		$('.tips-toast').hide().html('');
		$('.tips-toast').css('display','block').html(tips);
		if(!times){
			times = 3000;
		}
		showtoastNum = setTimeout(function(){hidetoast(url);},times);
	}
	function hidetoast(url){
		$('.tips-toast').hide();
		if(url!=undefined && url!='' && url!='undefined'){
			window.location.href=url;
		}
	}
	function getFormJson(form) {
		var o = {};
		var a = $(form).serializeArray();
		$.each(a, function () {
			if (o[this.name] !== undefined) {
				if (!o[this.name].push) {
					o[this.name] = [o[this.name]];
				}
				o[this.name].push(this.value || '');
			} else {
				o[this.name] = this.value || '';
			}
		});
		//input checkbox单独处理
		var t1 = document.getElementById(form.substring(1)).getElementsByTagName("input");
		for(var i=0;i<t1.length;i++) {
			if (t1[i].type == "checkbox") {
				if (!(t1[i].checked)) {
					o[t1[i].name]="0";
				}else{
					o[t1[i].name]="1";
				}
			}
		}
		return o;
	}
	function getAmont(amont){
		var cost = new String(amont).split(".");
		var cost2 = "";
		if(cost.length==1){
			cost2="000";
		}else{
			cost2 = cost[1];
			if(cost[1].length>3){
				cost2 = cost[1].substring(0,3);
			}else if(cost[1].length<3){
				var tleng =3 - cost[1].length;
				for(var i=0;i<tleng;i++){
					cost2+="0";
				}
			}
		}
		return cost[0]+"."+cost2;
	}
</script>
