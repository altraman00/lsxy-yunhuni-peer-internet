
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
});

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
