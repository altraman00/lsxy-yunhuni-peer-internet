var hesyun_wo_contextPath = "";
var hesyun_wo_resourceContextPath = "";
var hesyun_current_sessionid = "";
var hesyun_wo_div_id = "";

function initForm(id, title, text, wo_id){
	console.log("initForm");
  var _form = $.easyform('hesyun_wo_form_template', {  
    formId : "wo_form_default",
    context : "http://www.hesyun.net/portal",
    //formAction : ctx + "/tenant/user/customer/saveCustomer",
    formLoadSuccess : function() {
    	$("#" + id + " #title").val(title); 
		$("#" + id + " #content").val(text); 

		$('#'+hesyun_wo_div_id+' #attachment').uploadifive({
	        uploadScript     : '/workorder/uploadAttachment/' + wo_id,
	        buttonClass : 'uploadifive-button',
			buttonText : '选择附件',
			removeCompleted : true,
			onError : function(error, fileObj) {
				alert("上传附件失败");
			},
			onAddQueueItem : function(file) {
				console.log("Uploading file: "+ file.name);
			},
	        onUploadComplete : function(file, data) {
	            console.log("File uploaded.");
	            var dataObj = JSON.parse(data);
	            alert(dataObj.errmsg);
	        }
    	});

    	
    }
    //saveCallback : saveCustCall
  });
}

/**
 * 查询客户工单记录
 */
function getCustomerWo(div_id, customerid){
	console.log("load history list");
	var colModel = [
	                // {name:'num',title:'编号',type:'string',width:120},
	                {name:'title',title:'标题',type:'string',width:120},
	                // {name:'text',title:'内容',type:'string',width:260,sortable:false},
	                {name:'createDt', title:'时间',type:'date',width:150,sortable:false,render:function(value,item){
	                	return  new Date(value.time).format("yyyy-MM-dd HH:mm:ss");
	                }},
	                {name:'status',title:'状态',type:'string',width:60,sortable:false,render:function(value,item){
	                	if(value == '0'){
	                		return "<span style='color:red'>未保存</span>";
	                	}else if (value =='1'){
	                		return "<span style='color:red'>未发送</span>";
	                	}else if (value =='2'){
	                		return "<span style='color:red'>待处理</span>";
	                	}else if (value =='3'){
	                		return "<span style='color:green'>已处理</span>";
	                	}
	                }}];

	var opBtns = [{caption:'详情',onclick:function(id,item){
				  //getMyWoDetail(id);
	    		}}];
	
	var _woGrid = $.easygrid('hesyun_history_wo', {
		url : ctx + '/customer/getCustomerWo/',
		model : colModel,
		isPagination : false
		//innerBtn : opBtns
	});
	
	_woGrid.setRequestParam({customerid : customerid});
	_woGrid.loadData();
	//$(".right-table").css("margin","20px 0px");
}


function saveWo(div_id, wo_id){
	var skillName = $('#' + div_id + ' .hesyun_skill_group').text();
	var skillCode = $('#' + div_id + ' .hesyun_skill_group').val();
	var priority = $('#' + div_id + ' input:radio[name=priority]:checked').val();
	console.log("priority: " + priority + " name: " + skillName + " code: " + skillCode);	
	var title = $('#' + div_id + ' #title').val();
	var text = $('#' + div_id + ' #content').val();

	var save_url = hesyun_wo_contextPath + "/portal/tenant/user/wo/save";
	$.ajax({
	  type: 'GET',
	  url: save_url,
	  data: {wo_id: wo_id, title: title, text: text, priority: priority},
	  dataType: 'jsonp',
	  success: function(result){
	  	alert(result.errmsg);
	  }
	});
}

function sendWo(div_id, wo_id){
	var skillName = $('#' + div_id + ' .hesyun_skill_group').text();
	var skillCode = $('#' + div_id + ' .hesyun_skill_group').val();
	var priority = $('#' + div_id + ' input:radio[name=priority]:checked').val();
	var sentTo = $('#' + div_id + ' #hesyun_wo_persons').val();

	var title = $('#' + div_id + ' #title').val();
	if (title == "") {alert("请填写工单标题");return;};
	var text = $('#' + div_id + ' #content').val();
	if (text == "") {alert("请填写工单内容");return;};

	var save_url = hesyun_wo_contextPath + "/portal/tenant/user/wo/send";
	$.ajax({
	  type: 'GET',
	  url: save_url,
	  data: {wo_id: wo_id, title: title, text: text, priority: priority, skill: skillCode, sendto:sentTo},
	  dataType: 'jsonp',
	  success: function(result){
		  $('.order-btn').show();
		  alert(result.errmsg);
		  $("#" + div_id + " .hesyun_new_default_wo_div").html("");
	  },
	  error: function(result){
	  	alert("Server is dead.");
	  }
	});
}

function createNewWo(id, sessionid, contextPath) {
	console.log(id + "  " + sessionid + "  " + contextPath);
	var url = contextPath + "/portal/tenant/user/wo/new";//?sessionid=" + default_params.customer_session_id + "&callback=?";
	console.log("url: " + url);
	$.ajax({
		  type: 'GET',
		  url: url,
		  data: {sessionid: sessionid},
		  dataType: 'jsonp',
		  error: function(result){
		  	alert("Server is dead.");
		  },
		  success: function(result){
	    	if (result.errmsg) {
	    		alert(result.errmsg);
				return;
			} else {
				console.log("workorder: " + result.workorder);
				console.log("Persons: " + result.persons);
				
				$('.order-btn').hide();
				
				var wo_div = $("#" + id + " .hesyun_new_default_wo_div");
				var temp_url = hesyun_wo_resourceContextPath + "/portal/wo/template/new_workorder.html";
				wo_div.load(temp_url, function(){
					var skillDiv = "";
					var skillCodes = result.skillCodes;
					for (var name in skillCodes) {
						skillDiv += "<option value=" + skillCodes[name] + ">" + name + "</option>";
					}
					var skillGroup = $("#" + id + " .hesyun_skill_group");
					skillGroup.append(skillDiv);

    				$('#hesyun_wo_persons').typeahead({
				      	source: function(query, process) {
				        	return result.persons;
				     	}
				   	});

				    var wo_title = result.workorder.title?result.workorder.title:"";
				    var wo_text = result.workorder.text?result.workorder.text:"";

		            $("#" + id + " .hesyun_wo_number").html(result.workorder.num); 
		            // $("#" + id + " #title").val(wo_title); 
		            // $("#" + id + " #content").val(wo_text); 

		            var saveButton = $("#" + id + " .hesyun_wo_save_button");
					saveButton.attr("onclick","saveWo(\"" + id + "\", \"" + result.workorder.id + "\")");  
					var sendButton = $("#" + id + " .hesyun_wo_send_button");
					sendButton.attr("onclick","sendWo(\"" + id + "\", \"" + result.workorder.id + "\")");  

					initForm(id, wo_title, wo_text, result.workorder.id);
				});
				// wo_div.loadTemplate($("#new_workorder"), {});

	        } 
	    }
	}); 
}

(function($) {     
$.fn.defaultWo = function(params) {
	var default_params = {
		div_id: "hesyun_default_wo_template",
		path_context : "http://www.hesyun.net",
		resource_path_context : "http://zxpc.hesyun.net/res",
		customer_session_id:"8a2bd17b4ae2cbd1014ae5fa8b960003",
		customer_id:""
	};

	for ( var k in params) {
		params[k] = params[k] ? params[k] : default_params[k];
		console.log(k + " is " + params[k]);
	}

	hesyun_wo_contextPath = params.path_context;
	hesyun_wo_resourceContextPath = params.resource_path_context;
	hesyun_current_sessionid = params.customer_session_id;
	hesyun_wo_div_id = params.div_id;

	var default_wo = $("#" + params.div_id);
	// default_wo.loadTemplate($("#hesyun_workorder"), {});
	// default_wo.loadTemplate($("#new_workorder"), {});
	var temp_url = params.resource_path_context + "/portal/wo/template/hesyun_workorder.html";
	default_wo.load(temp_url, function(){
		console.log("hesyun_workorder loaded");
		var button = $("#" + params.div_id + " .hesyun_create_wo");
		button.attr("onclick","createNewWo(\"" + params.div_id + "\", \"" + params.customer_session_id + "\", \"" + params.path_context + "\")");  
		getCustomerWo(params.div_id, params.customer_id);
	});

	
};   


})(jQuery); 


