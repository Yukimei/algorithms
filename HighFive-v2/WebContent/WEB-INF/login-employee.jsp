<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!-- Team 5
 -->
 <!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title>Employee Login -- Carnegie Financial Services</title>

<!-- Bootstrap core CSS -->
<link href="bower_components/bootstrap/dist/css/bootstrap.min.css"
	rel="stylesheet">

<!-- MetisMenu CSS -->
<link href="bower_components/metisMenu/dist/metisMenu.min.css"
	rel="stylesheet">

<!-- Custom CSS -->
<link href="dist/css/sb-admin-2.css" rel="stylesheet">

<!-- Custom Fonts -->
<link href="bower_components/font-awesome/css/font-awesome.min.css"
	rel="stylesheet" type="text/css">

<link href="css/login.css" rel="stylesheet" type="text/css">


<!-- <script src="js/prefixfree.min.js"></script>
 -->

</head>

<body>
	<div id="wrapper">

		<!-- Navigation -->
		<nav class="navbar navbar-default navbar-static-top" role="navigation"
			style="margin-bottom: 0">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target=".navbar-collapse">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<span class="navbar-brand" href="index.do">Carnegie Financial
					Services</span>
			</div>
			<!-- /.navbar-header -->
			<ul class="nav navbar-top-links navbar-right">
				<li class="dropdown"><a class="dropdown-toggle"
					data-toggle="dropdown" href="#"> <i class="fa fa-user fa-fw"></i>
						<i class="fa fa-caret-down"></i>
				</a>
					<ul class="dropdown-menu dropdown-user">
						<c:choose>
							<c:when test="${ employee == null && customer == null }">					
								<li><a href="login_employee.do" class="fa fa-user fa-fw"
									type="submit">  Employee Login</a></li>
								<li><a href="login_customer.do" class="fa fa-user fa-fw"
									type="submit">  Customer Login</a></li>

							</c:when>
							<c:otherwise>
								<li>
									<a style="color: #ffffff">Hi, ${employee.firstName}
										${employee.lastName}</a>
								</li>
								<li class="divider"></li>
								<li><a href="logout.do" class="fa fa-user fa-fw"
									type="submit">  Logout</a></li>
								<li><a href="change_pwd.do" class="fa fa-user fa-fw"
									type="submit">  Change Password</a></li>

							</c:otherwise>
						</c:choose>

					</ul> <!-- /.dropdown-user --></li>

				<!-- /.dropdown -->

			</ul>

			<!-- <li class="active">Login</li> -->
			</ol>
	</div>
	
	<jsp:include page="error-list.jsp" />

	<div class="container">
		<!-- form role="form" action="login_customer.do" method="POST">
			<h5>User name</h5>
			<input class="form-control" style="border: 1px solid" type="text"
				name="username" value="${form.username}" />

			<h5>Password</h5>
			<input type="password" style="border: 1px solid" type="password"
				class="form-control" name="password" value="" /><br> <input
				name="action" class="btn btn-lg btn-primary btn-block" type="submit"
				value="Login">
		</form> -->
		<div class="body"></div>
		<div class="grad"></div>
		<div class="header">
			<div>Employee</div><br>
			<div><span>LogIn</span></div>
		</div>
		<br>
		<div class="login">
		<form role="form" action="login_employee.do" method="POST">
			<input type="text" placeholder="username" name="username" value="${form.username}"><br>
			<input type="password" placeholder="password" name="password" value=""><br><br>
			<input type="submit" name="action" class="btn btn-lg btn-primary btn-right" value="Login">
		</form>
		</div>
		<script
			src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>
	</div>

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