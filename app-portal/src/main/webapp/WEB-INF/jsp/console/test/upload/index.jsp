<%@page import="org.springframework.context.annotation.Import"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@include file="/inc/import.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/inc/meta.jsp" %>

    <style>
        .t-status {
            position: relative;
            line-height: 100%;
        }

        .t-status h4 {
            margin-left: 55px;
        }
        .t-status p {
            margin-bottom: 0px;
            margin-left: 55px;
            font-size: 12px;
        }

        .c-status {
            position: relative;
            line-height: 100%;
        }

        .c-status h4 {
            margin-left: 45px;
            font-size: 16px;
        }
        .c-status p {
            margin-bottom: 0px;
            margin-left: 45px;
            font-size: 9px;
        }

        .type {
            position: absolute;
            top: 0;
            padding: 0px 10px 0px 0px;
            border-right: 1px solid #ededed;
            margin-left: 5px;
            width: 40px;
            height: 30px;
            font-size: 30px;
        }

        .min-type {
            position: absolute;
            top: 0;
            padding: 0px 10px 0px 0px;
            margin-left: 5px;
            width: 30px;
            height: 30px;
            font-size: 28px;
        }
    </style>


    <style>
        .prog-border {
            height: 15px;
            width: 205px;
            background: #fff;
            border: 1px solid #000;
            margin: 0;
            padding: 0;
        }
        .prog-bar {
            height: 11px;
            margin: 2px;
            padding: 0px;
            background: #178399;
            font-size: 10pt;
        }

    </style>
    <script language="javascript" type="text/javascript">
        <!--
        //var userName=document.getElementById("userName").value;
        //创建跨浏览器的XMLHttpRequest对象
        var timer;
        function startListener(){
            var xmlhttp;
            try{
                //IE 5.0
                xmlhttp = new ActiveXObject('Msxm12.XMLHTTP');
            }catch(e){
                try{
                    //IE 5.5 及更高版本
                    xmlhttp = new ActiveXObject('Microsoft.XMLHTTP');
                }catch(e){
                    try{
                        //其他浏览器
                        xmlhttp = new XMLHttpRequest();
                    }catch(e){}
                }
            }
            var progressStatusText = document.getElementById("progressBar");
            xmlhttp.open("get","${ctx}/test/upload/status",true);
            /**此处Header设置非常重要，必须设置Content-type类型，负责会报错误
             */
            xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
            xmlhttp.onreadystatechange = function(){
                if(xmlhttp.readyState == 4){
                    if(xmlhttp.status == 200){
                        progressStatusText.innerHTML = "";
                        progressStatusText.innerHTML = xmlhttp.responseText;
                        var temp = xmlhttp.responseText.indexOf("success");
                        if (  temp > 0 ){
                            window.clearTimeout(timer);
                        }else{
                            timer = window.setTimeout(startListener,1000);
                        }
                    }
                }
            }
            xmlhttp.send(null);
        }
        function startUpload(){
            timer = window.setTimeout(startListener,1000);
            return true;
        }
        function cancelUpload(){
            var xmlhttp;
            try{
                //IE 5.0
                xmlhttp = new ActiveXObject('Msxm12.XMLHTTP');
            }catch(e){
                try{
                    //IE 5.5 及更高版本
                    xmlhttp = new ActiveXObject('Microsoft.XMLHTTP');
                }catch(e){
                    try{
                        //其他浏览器
                        xmlhttp = new XMLHttpRequest();
                    }catch(e){}
                }
            }
            var progressStatusText = document.getElementById("progressBar");
            xmlhttp.open("get","${ctx}/test/upload/cancel",true);
            xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
            //xmlhttp.setRequestHeader("Content-type", "multipart/form-data");
            xmlhttp.onreadystatechange = function(){
                if(xmlhttp.readyState == 4){
                    if(xmlhttp.status == 200){
                        progressStatusText.innerHTML = "";
                        progressStatusText.innerHTML = xmlhttp.responseText;
                    }
                }
            }
            xmlhttp.send(null);
            return false;
        }
        //-->
    </script>

</head>

<body>
<%@include file="/inc/headerNav.jsp" %>
<!-- 主界面 -->

<div class="container-fluid new_main">
    <div class="col_side">
        <%@include file="/inc/leftMenu.jsp" %>
    </div>
    <div class="col_main">



        <div> <h2>上传文件</h2></div>
       <form  action="${ctx}/test/upload/upload" enctype="multipart/form-data" method="post" onsubmit="return startUpload();" >
           <input type="file" multiple name="file" id="file" size="40"/><br>
           <input type="submit" name="uploadButton" id="uploadButton" value="开始上传"/>
           <input type="button" name="cancelUploadButton" id="cancelUploadButton" value="取消上传" onclick="return cancelUpload();"/><br>
       </form>
        <div id="progressBar">
        </div>

    </div>
</div>

<%@include file="/inc/footer.jsp" %>
<script src="${resPrefixUrl}/common/scripts/ichart/ichart.1.2.min.js"></script>
<script src="${resPrefixUrl}/portal/console/account/scripts/index.js"></script>
<script>
    setCookie('hsy_last_page', 'admin', 10);
    function changeType(v){
        if(v.value==1){
            document.getElementById("op1").removeAttribute("hidden");
            document.getElementById("op2").setAttribute("hidden","true");
        }else if(v.value==2){
            document.getElementById("op1").setAttribute("hidden","true");
            document.getElementById("op2").removeAttribute("hidden");
        }
    }
</script>
</body>
</html>