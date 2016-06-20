
jQuery(document).ready(function() {

    $('.container form').submit(function(){
        var username = $(this).find('.username').val();
        var password = $(this).find('.password').val();
        if(username == '') {
            $(this).find('.tips').fadeOut('fast', function(){
                $(this).css('top', '27px');
            });
            $(this).find('.tips').fadeIn('fast', function(){
                $(this).parent().find('.username').focus();
            });
            return false;
        }
		
        if(password == '') {
            $(this).find('.error').fadeOut('fast', function(){
                $(this).css('top', '96px');
            });
            $(this).find('.error').fadeIn('fast', function(){
                $(this).parent().find('.password').focus();
            });
            return false;
        }
    });

    $('.container form .username, .container form .password').keyup(function(){
        $(this).parent().find('.tips').fadeOut('fast');
    });


    if($.cookie('rememberMe')&&$.cookie('username')!=null&&$.cookie('password')!=null){


        var base64 = new Base64();
        $("#username").val($.cookie('username'));

        $("#password").val(base64.decode($.cookie('password')));

        $("#rememberMe").attr('checked','checked');

    }else if($.cookie('username')!=null){

        $("#username").val($.cookie('username'));

    }



});

function submitForm(){
    if($("#username").val() == ""){
        showError("请输入用户名！");
        $("#username").focus();
        return;
    }
    if($("#password").val() == ""){
        showError("请输入密码！");
        $("#password").focus();
        return;
    }
    if($("validateCode").val() == ""){
        showError("请输入验证码！");
        $("validateCode").focus();
        return;
    }

    showError("");

    var base64 = new Base64();
    var rememberMe = $("#rememberMe").prop('checked');
    var username = $.trim($("input[name='username']").val());
    var password = $.trim($("input[name='password']").val());
    if(rememberMe){  //选择“记住我”时，将用户的信息保存到本地cookie中
        $.cookie('username', username, { expires: 7 });
        $.cookie('password', base64.encode(password), { expires: 7 });
        $.cookie('rememberMe', rememberMe, { expires: 7 });
    }else{  //不“记住我”时，将之前保存的用户的信息从cookie中删除
        //$.cookie('userName', '', { expires: -1});
        $.cookie('password', '', { expires: -1 });
        $.cookie('rememberMe', '', { expires: -1 });
    }

    loginForm.submit();
}
function showError(msg){
    $("#errorMsg")[0].style.visibility='visible';
    errorMsg.innerHTML=msg;
}



function show(){
                var a=document.getElementById("qrcode");
                a.style.display="block";
                a.style.zIndex="3";
            }
            function hide(){
                var a=document.getElementById("qrcode");
                a.style.display="none";
                a.style.zIndex="-1";
            }
