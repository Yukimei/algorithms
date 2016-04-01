<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:choose>
    <c:when test="${ employee == null }">
        <jsp:include page="customer-top.jsp"/>
    </c:when>
    <c:otherwise>
        <jsp:include page="employee-top.jsp"/>
    </c:otherwise>
</c:choose>
<div id="wrapper">
	<div class="row">
		<div class="col-lg-12">
			<h1 class="page-header">Change Your Password</h1>
		</div>
		<jsp:include page="error-list.jsp" />
		<!-- /.col-lg-12 -->
	</div>

<div class="col-md-4">
	<form role="form" method="POST" action="change_pwd.do">
        <table >
        <tbody>
            <tr>
                <td ><strong>New Password</strong> </td>
            </tr>
            <tr>
                <td ><input type="password"  name="newPassword" value=""/></td>
            </tr>
            <tr >
                <td > <strong>Confirm New Password</strong> </td>
            </tr>
            <tr>
                <td><input type="password" name="confirmPassword" value=""/></td>
            </tr>
			<td>&nbsp;</td>

            <tr>
                <td colspan="2" align="center">
                    <input type="submit" class="btn btn-primary btn-block" name="action" value="Change Password"/>
                </td>
            </tr>
        </tbody>
        </table>
    </form>
</div>
<br><br><br><br><br>
			</div>			
		</div>
	</div>
</div>

<jsp:include page="buttom.jsp" />
