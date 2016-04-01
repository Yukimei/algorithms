<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<jsp:include page="employee-top.jsp" />

<li class="active">Create Employee Account</li>

<jsp:include page="error-list.jsp" />
<p class="content" style="font-size:medium;color:#44C881; margin-top:8px;margin-left:15px">
                    ${emessage}
</p>

<br>
<div class="row">
	<form role="form" action="create_employee.do" method="post">
			<div class="col-lg-6">
		<div class="form-group">
			<label for="InputName">Username</label>
			<div class="input-group">
				<input type="text" class="form-control" name="userName"
					value="${form.userName}" required> <span
					class="input-group-addon"></span>
			</div>
		</div>
		<div class="form-group">
			<label for="InputName">First Name</label>
			<div class="input-group">

				<input type="text" class="form-control" name="firstName"
					value="${form.firstName}" required> <span
					class="input-group-addon"></span>
			</div>
		</div>
		<div class="form-group">
			<label for="InputName">Last Name</label>
			<div class="input-group">
				<input type="text" class="form-control" name="lastName"
					value="${form.lastName}" required> <span
					class="input-group-addon"></span>
			</div>
		</div>
		<div class="form-group">
			<label for="InputName">Password</label>
			<div class="input-group">
				<input type="password" class="form-control" name="password" value=""
					required> <span class="input-group-addon"></span>
			</div>
		</div>
		<div class="form-group">
			<label for="InputName">Confirm Password</label>
			<div class="input-group">
				<input type="password" class="form-control" name="confirm" value=""
					required> <span class="input-group-addon"></span>
			</div>
		</div>
		<br>

				<input type="submit" name="action" class="btn btn-info pull-right"
					value="Create Employee Account">
	</form>
</div>

<br>
<br>
<br>
<br>
<br>
</div>
</div>
</div>
</div>
<!-- /body container -->

<jsp:include page="buttom.jsp" />

