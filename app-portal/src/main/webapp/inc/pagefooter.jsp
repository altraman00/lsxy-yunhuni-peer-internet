<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<section class="panel panel-default yunhuni-personal">
    <nav class='pageWrap'>
        <ul class="pagination">
            <!--设置显示的页数-->
            <c:if test="${pageObj!=null}">
            <c:set var="startPageNo" value="1"></c:set>
            <c:set var="endPageNo" value="5"></c:set>
            <c:if test="${ pageObj.currentPageNo + 2 >5}">
                <c:set var="startPageNo" value="${ pageObj.currentPageNo-2}"></c:set>
                <c:set var="endPageNo" value="${ pageObj.currentPageNo+2}"></c:set>
            </c:if>
            <c:if test="${pageObj.totalPageCount <= endPageNo}">
                <c:set  var="endPageNo" value="${pageObj.totalPageCount}"></c:set>
            </c:if>
            <c:if test="${endPageNo > 4}">
                <c:set  var="startPageNo" value="${endPageNo - 4}"></c:set>
            </c:if>
            <c:if test="${endPageNo < 5}">
                <c:set  var="startPageNo" value="1"></c:set>
            </c:if>
            <c:if test="${pageObj.totalCount > 0}">
                <li>
                    <span style="border:0px; "  onmouseover="this.style.backgroundColor='#FFFFFF'" >共${pageObj.totalCount}条</span>
                </li>
            </c:if>
            <c:if test="${startPageNo > 1}">
                <li>
                    <a href="${pageUrl}?pageNo=${startPageNo-1}&pageSize=${pageObj.pageSize}${extraParam}" aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>
            </c:if>
            <c:forEach end="${endPageNo}" begin="${startPageNo}" varStatus="s" >
                <c:if test="${endPageNo!=1}">
                    <li
                            <c:if test="${pageObj.currentPageNo == s.index}">
                                class="active"
                            </c:if>
                    >
                        <a href="${pageUrl}?pageNo=${s.index}&pageSize=${pageObj.pageSize}${extraParam}">${s.index}</a>
                    </li>
                </c:if>
            </c:forEach>

            <c:if test="${pageObj.totalPageCount>endPageNo}">
                <li>
                    <a href="${pageUrl}?pageNo=${endPageNo+1}&pageSize=${pageObj.pageSize}${extraParam}" aria-label="Next">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
            </c:if>
            </c:if>
        </ul>
    </nav>
</section>