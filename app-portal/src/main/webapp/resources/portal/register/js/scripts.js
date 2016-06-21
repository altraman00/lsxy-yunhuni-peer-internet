/**
 * Created by liups on 2016/6/20.
 */

function bfSubmitForm(){
    if($("#username").val() == ""){
        showError("请输入用户名！");
        $("#username").focus();
        return;
    }
    if($("#phone").val() == ""){
        showError("请输入手机号码！");
        $("#phone").focus();
        return;
    }
    if($("#email").val() == ""){
        showError("请输入邮箱！");
        $("#email").focus();
        return;
    }
    showError("");

    $('#checkPhone').modal('show');
}


function submitForm(){
    registerForm.submit();
}


function showError(msg){
    $("#errorMsg")[0].style.visibility='visible';
    errorMsg.innerHTML=msg;
}