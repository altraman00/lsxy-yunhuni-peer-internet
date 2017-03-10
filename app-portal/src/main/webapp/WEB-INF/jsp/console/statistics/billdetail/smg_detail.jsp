<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@include file="/inc/import.jsp" %>
<div id="myTabContent2" hidden class="tab-content" >
        <small style="font-size:20px;">消息群发详情</small>

        <div class="row statistics_row" >
            <div class="col-md-2 mywidth">
                手机号码
            </div>
            <div class="col-md-2">
                <input type="text" id="mobile2" class="form-control"  value="${mobile1}"  />
            </div>
            <div class="col-md-2 mywidth">
                发送结果
            </div>
            <div class="col-md-2">
                <select id="state2">
                    <option value="-100" <c:if test="${state==-100}">selected </c:if>>全部</option>
                    <option value="1" <c:if test="${state==1}"> selected</c:if> >成功</option>
                    <option value="-1" <c:if test="${state==-1}">selected </c:if> >失败</option>
                    <option value="0" <c:if test="${state==0}"> selected</c:if> >未发送</option>
                </select>
            </div>
            <div class="col-md-2">
                <button class="btn btn-primary" type="button" onclick="submitDetail()"> 查询</button>
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
                    <th>原因</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${pageObj1.result}" var="result" varStatus="s">
                    <tr>
                        <td>${s.index + 1}</td>
                        <td>${result.mobile}</td>
                        <td>
                            <c:if test="${result.state==1}">发送成功</c:if>
                            <c:if test="${result.state==0}">未发送</c:if>
                            <c:if test="${result.state==-1}">发送失败</c:if>
                        </td>
                        <td>${result.reason}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    <c:set var="extraParam1" value="&pageSize=${pageSize0}&pageNo=${pageno0}&start=${start}&end=${end}&appId=${appId}&isMass=${isMass}&taskName=${taskName}&mobile=${mobile}&state=${state}&mobile1=${mobile1}&msgKey=${msgKey}"></c:set>
    <c:set var="pageUrl" value="${ctx}/console/statistics/billdetail/${sendType}"></c:set>
    <%@include file="/inc/pagefooter1.jsp" %>
</div>
<script>
    function submitDetail(){
        $('#mobile1').val($('#mobile2').val());
        $('#state').val( $('#state2 option:selected').val() );
        $('#mainForm').submit();
    }
    function toDetail(id){
        $('#msgKey').val(id);
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