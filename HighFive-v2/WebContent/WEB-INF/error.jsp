<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:choose>
    <c:when test="${ isCustomer == true }">
        <jsp:include page="customer-top.jsp"/>
    </c:when>
    <c:otherwise>
        <jsp:include page="employee-top.jsp"/>
    </c:otherwise>
</c:choose>

<jsp:include page="error-list.jsp" />

<p style="font-size: medium; color: green">&nbsp; &nbsp; &nbsp;>
    ${message}
</p>

<br> <br> <br>
</div>
</div>
</div>
</div>

<jsp:include page="buttom.jsp" />
