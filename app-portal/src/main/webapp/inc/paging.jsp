<section class="panel panel-default yunhuni-personal">
    <nav class='pageWrap'>
        <ul class="pagination">
            <!--设置显示的页数-->
            <c:set var="startPageNo" value="1"></c:set>
            <c:set var="endPageNo" value="5"></c:set>
            <c:if test="${pageList.totalPageCount >5 && pageList.currentPageNo + 2 >5}">
                <c:set var="endPageNo" value="${ pageList.currentPageNo+2}"></c:set>
                <c:if test="${endPageNo>pageList.totalPageCount}">
                    <c:set var="endPageNo" value="${pageList.totalPageCount}"></c:set>
                </c:if>
                <c:set var="startPageNo" value="${ endPageNo-4}"></c:set>
            </c:if>
            <c:if test="${pageList.totalPageCount <=5}">
                <c:set var="endPageNo" value="${pageList.totalPageCount}"></c:set>
            </c:if>
            <c:if test="${startPageNo > 1}">
                <li>
                    <a href="${ctx}${pageUrl}?pageNo=${startPageNo-1}" aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>
            </c:if>
            <c:forEach end="${endPageNo}" begin="${startPageNo}" varStatus="s" >
                <c:if test="${pageList.currentPageNo == s.index}">
                    <li class="active"> <a href="${ctx}${pageUrl}?pageNo=${s.index}">${s.index}</a> </li>
                </c:if>
                <c:if test="${pageList.currentPageNo != s.index}">
                    <li> <a href="${ctx}${pageUrl}?pageNo=${s.index}">${s.index}</a> </li>
                </c:if>
            </c:forEach>
            <c:if test="${pageList.totalPageCount>endPageNo}">
                <li>
                    <a href="${ctx}${pageUrl}?pageNo=${endPageNo+1}" aria-label="Next">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
            </c:if>
        </ul>
    </nav>
</section>