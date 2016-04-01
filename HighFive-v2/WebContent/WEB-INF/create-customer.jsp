<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<jsp:include page="employee-top.jsp" />

<li class="active">Create Customer Account</li>
<jsp:include page="error-list.jsp" />


 <p class="content" style="font-size:medium;color:#44C881; margin-top:8px;margin-left:15px">
                    ${customermessage}
                   </p>


<!-- <div class="container"> -->
<br>
	<div class="row">
		<form role="form" action="create_customer.do" method="post">
			<div class="col-lg-6">
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
					<label for="InputName">Username</label>
					<div class="input-group">
						<input type="text" class="form-control" name="userName"
							value="${form.userName}" required> <span
							class="input-group-addon"></span>
					</div>
				</div>
				<div class="form-group">
					<label for="InputName">Password</label>
					<div class="input-group">
						<input type="password" class="form-control" name="password"
							value="" required> <span class="input-group-addon"></span>
					</div>
				</div>
				<div class="form-group">
					<label for="InputName">Confirm Password</label>
					<div class="input-group">
						<input type="password" class="form-control" name="confirm"
							value="" required> <span class="input-group-addon"></span>
					</div>
				</div>
				<div class="form-group">
					<label for="InputName">Address Line1</label>
					<div class="input-group">
						<input type="text" class="form-control" name="addrLine1"
							value="${form.addrLine1}" required> <span
							class="input-group-addon"></span>
					</div>
				</div>
				<div class="form-group">
					<label for="InputName">Address Line2</label>
					<div class="input-group">
						<input type="text" class="form-control" name="addrLine2"
							value="${form.addrLine2}" required> <span
							class="input-group-addon"></span>
					</div>
				</div>
				<div class="form-group">
					<label for="InputName">City</label>
					<div class="input-group">
						<input type="text" class="form-control" name="city"
							value="${form.city}" required> <span
							class="input-group-addon"></span>
					</div>
				</div>
				<div class="form-group">
					<label for="InputName">State</label>
					<div class="input-group">
						<input type="text" class="form-control" type="text"
							class="form-control" name="state" value="${form.state}" required>
						<span class="input-group-addon"></span>
					</div>
				</div>
				<div class="form-group">
					<label for="InputName">Zip Code</label>
					<div class="input-group">
						<input type="text" class="form-control" name="zip"
							value="${form.zip}" required> <span
							class="input-group-addon"></span>
					</div>
				<br>
				</div>
				<input type="submit" name="action" class="btn btn-info pull-right"
					value="Create Customer Account">
			</div>
		</form>
	</div>
<!-- </div> -->
<br>
<br>
<br>
<br>
<br>
<br><!-- /body container -->

<br>
<br>

<!-- /body container -->

<jsp:include page="buttom.jsp" />
