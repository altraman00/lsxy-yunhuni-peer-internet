<!--设置显示的页数-->
<c:set var="startPageNo" value="1"></c:set>
<c:set var="endPageNo" value="5"></c:set>
<c:if test="${ pageList.currentPageNo + 2 >5}">
    <c:set var="startPageNo" value="${ pageList.currentPageNo-2}"></c:set>
    <c:set var="endPageNo" value="${ pageList.currentPageNo+2}"></c:set>
</c:if>
<c:if test="${pageList.totalPageCount <= endPageNo}">
    <c:set  var="endPageNo" value="${pageList.totalPageCount}"></c:set>
    <c:set  var="startPageNo" value="${pageList.totalPageCount-4}"></c:set>
</c:if>
<c:if test="${startPageNo > 1}">
    <li>
        <a href="${ctx}${pageUrl}?pageNo=${startPageNo-1}" aria-label="Previous">
            <span aria-hidden="true">&laquo;</span>
        </a>
    </li>
</c:if>
<c:forEach end="${endPageNo}" begin="${startPageNo}" varStatus="s" >
    <li
            <c:if test="${pageList.currentPageNo == s.index+1}">
                class="active"
            </c:if>
    >
        <a href="${ctx}${pageUrl}?pageNo=${s.index}">${s.index}</a>
    </li>
</c:forEach>
<c:if test="${pageList.totalPageCount>endPageNo}">
    <li>
        <a href="${ctx}${pageUrl}?pageNo=${endPageNo+1}" aria-label="Next">
            <span aria-hidden="true">&raquo;</span>
        </a>
    </li>
</c:if>