var updateDialog;
var updateForm;

function startServer(){
    var btnStartServer = $(this);
    var host = btnStartServer.attr("host");
    var app = btnStartServer.attr("app");
    var url = ctx + "admin/server/start?host="+host+"&app="+app;

    $.get( url, function( data ) {
        if(data && data.success){
            alert('启动成功');
        }else{
            alert('启动失败:' + data.errorMsg);
        }
        location.reload();
    });

}

function updateServer(){
    var btnStartServer = $(this);
    var host = btnStartServer.attr("host");
    var app = btnStartServer.attr("app");
    var version = btnStartServer.attr("version");
    updateDialog.find("#txtUpdateVersion").val(version);
    updateDialog.find("#txtUpdateHost").val(host);
    updateDialog.find("#txtUpdateApp").val(app);
    updateDialog.dialog("open");
/*


    */

}

function stopServer(){
    var btnStartServer = $(this);
    var host = btnStartServer.attr("host");
    var app = btnStartServer.attr("app");
    var url = ctx + "admin/server/stop?host="+host+"&app="+app;

    $.get( url, function( data ) {
        if(data && data.success){
            alert('操作成功');
        }else{
            alert('操作失败');
        }
        location.reload();
    });
}

/**
 * 执行更新动作
 */
function doUpdate(){
    var version = updateDialog.find("#txtUpdateVersion").val();
    var host = updateDialog.find("#txtUpdateHost").val();
    var app = updateDialog.find("#txtUpdateApp").val();

    var url = ctx + "admin/server/update?host="+host+"&app="+app+"&version="+version;

    $.get( url, function( data ) {
        if(data && data.success){
            alert('更新成功');
        }else{
            alert('更新失败:'+data.errorMsg);
        }
        location.reload();
    });
}

$(function() {
    $(".btnStartServer").bind("click",startServer);
    $(".btnUpdateServer").bind("click",updateServer);
    $(".btnStopServer").bind("click",stopServer);
    updateDialog = $( "#update-dialog-form" ).dialog({
        autoOpen: false,
        height: 400,
        width: 350,
        modal: true,
        buttons: {
            "确定": doUpdate,
            "取消": function() {
                updateDialog.dialog( "close" );
            }
        },
        close: function() {
            document.forms[0].reset();
        }
    });
    // dialog.dialog( "open" );
    updateForm = updateDialog.find( "form" ).on( "submit", function( event ) {
        event.preventDefault();
    });

});