<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@include file="/inc/import.jsp" %>
<div id="myTabContent2" hidden class="tab-content" >
        <small style="font-size:20px;">消息群发详情</small>

        <div class="row statistics_row" >
            <div class="col-md-2 mywidth">
                手机号码
            </div>
            <div class="col-md-2">
                <input type="text" name="mobile1" class="form-control"  value="${mobile1}"  />
            </div>
            <div class="col-md-2">
                <button class="btn btn-primary" type="submit"> 查询</button>
                <button class="btn btn-primary" type="button" onclick="tohome()"> 返回</button>
            </div>
        </div>
    <div>
        <table class="table table-striped cost-table-history">
            <thead>
                <tr>
                    <th>序号</th>
                    <th>手机号码</th>
                    <th>发送结果</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${pageObj1.result}" var="result" varStatus="s">
                    <tr>
                        <td>${s.index + 1}</td>
                        <td>${result.mobile}</td>
                        <td>
                            <c:if test="${result.state==1}">任务完成</c:if>
                            <c:if test="${result.state==0}">待处理</c:if>
                            <c:if test="${result.state==-1}">任务失败</c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    <c:set var="extraParam1" value="&start=${start}&end=${end}&&start1=${start1}&end1=${end1}&appId=${appId}&isMass=${isMass}&taskName=${taskName}&mobile=${mobile}&msgKey=${msgKey}&mobile1=${mobile1}&state=${state}"></c:set>
    <c:set var="pageUrl1" value="${ctx}/console/statistics/billdetail/${sendType}"></c:set>
    <%@include file="/inc/pagefooter1.jsp" %>
</div>
<script>
    function toDetail(id){
        $('#msgKey').val("id");
        $('#mainForm').submit();
    }
    function tohome(){
        $('#msgKey').val("");
        $('#mobile1').val("");
        $('#state').val('');
        $('#myTabContent').show();
        $('#myTabContent2').hide();
    }
</script>