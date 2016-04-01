<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:forEach var="e" items="${errors}">
	<br/>
	<p style="font-size: medium; color: red">&nbsp; &nbsp; &nbsp;${e}
	</p>
</c:forEach>

<!-- <p class="content" style="font-size:medium;color:#44C881; margin-top:8px;margin-left:15px"> -->
<p style="font-size: medium; color: green">&nbsp; &nbsp; &nbsp; ${message}<br/>
	</p>
  