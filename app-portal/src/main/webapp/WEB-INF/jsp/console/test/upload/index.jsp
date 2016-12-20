<%@page import="org.springframework.context.annotation.Import"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@include file="/inc/import.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">

<!-- header -->
<head>
    <%@include file="/inc/meta.jsp" %>

</head>
<body>
<section class="vbox">
    <%@include file="/inc/headerNav.jsp"%>
    <section class='aside-section'>
        <section class="hbox stretch">
            <!-- .aside -->
             <%@include file="/inc/leftMenu.jsp"%>
            <!-- /.aside -->

            <section id="content">
                <section class="hbox stretch">
                    <!-- 如果没有三级导航 这段代码注释-->
                        <section class="vbox xbox">
                            <!-- 如果没有三级导航 这段代码注释-->
                            <div class="col_main">
                                <div> <h2>上传文件</h2></div>
                                <form  action="${ctx}/test/upload/upload" enctype="multipart/form-data" method="post"  >
                                   <input type="file" multiple name="file" id="file" size="40"/><br>
                                   <input type="submit" name="uploadButton" id="uploadButton" value="开始上传"/>
                                </form>
                            </div>
                            <div class="col_main">
                                <div> <h2>拷贝文件</h2></div>
                                <form  action="${ctx}/console/test/upload/copy"  method="post"  >
                                    文件from：<input type="text" name="file1" /><br>
                                    文件to：<input type="text" name="file2" /><br>
                                    <input type="submit"   value="开始拷贝"/>
                                </form>
                            </div>
                            <div>${msg}</div>
                        </section>
                        </section>
                </section>
            </section>
<%@include file="/inc/footer.jsp" %>
<script src="${resPrefixUrl}/common/scripts/ichart/ichart.1.2.min.js"></script>
<script src="${resPrefixUrl}/portal/console/account/scripts/index.js"></script>
</body>
</html>