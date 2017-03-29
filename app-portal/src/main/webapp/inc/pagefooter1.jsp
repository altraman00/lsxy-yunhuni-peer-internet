<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<section class="panel panel-default yunhuni-personal">
    <nav class='pageWrap'>
        <ul class="pagination">
            <!--设置显示的页数-->
            <c:if test="${pageObj1!=null}">
            <c:set var="startPageNo1" value="1"></c:set>
            <c:set var="endPageNo1" value="5"></c:set>
            <c:if test="${ pageObj1.currentPageNo + 2 >5}">
                <c:set var="startPageNo1" value="${ pageObj1.currentPageNo-2}"></c:set>
                <c:set var="endPageNo1" value="${ pageObj1.currentPageNo+2}"></c:set>
            </c:if>
            <c:if test="${pageObj1.totalPageCount <= endPageNo1}">
                <c:set  var="endPageNo1" value="${pageObj1.totalPageCount}"></c:set>
            </c:if>
            <c:if test="${endPageNo1 > 4}">
                <c:set  var="startPageNo1" value="${endPageNo1 - 4}"></c:set>
            </c:if>
            <c:if test="${endPageNo1 < 5}">
                <c:set  var="startPageNo1" value="1"></c:set>
            </c:if>
            <c:if test="${pageObj1.totalCount > 0}">
                <li>
                    <span style="border:0px; "  onmouseover="this.style.backgroundColor='#FFFFFF'" >共${pageObj1.totalCount}条</span>
                </li>
            </c:if>
            <c:if test="${startPageNo1 > 1}">
                <li>
                    <a href="${pageUrl1}?pageNo1=${startPageNo1-1}&pageSize1=${pageObj1.pageSize}${extraParam1}" aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>
            </c:if>
            <c:forEach end="${endPageNo1}" begin="${startPageNo1}" varStatus="s" >
                <c:if test="${endPageNo1!=1}">
                    <li
                            <c:if test="${pageObj1.currentPageNo == s.index}">
                                class="active"
                            </c:if>
                    >
                        <a href="${pageUrl1}?pageNo1=${s.index}&pageSize1=${pageObj1.pageSize}${extraParam1}">${s.index}</a>
                    </li>
                </c:if>
            </c:forEach>

            <c:if test="${pageObj1.totalPageCount>endPageNo1}">
                <li>
                    <a href="${pageUrl1}?pageNo1=${endPageNo1+1}&pageSize1=${pageObj1.pageSize}${extraParam1}" aria-label="Next">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
            </c:if>
            </c:if>
        </ul>
    </nav>
</section>