<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%-- <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> --%>
<!-- Team 5 -->


<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title>Customer Login -- Carnegie Financial Services</title>

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
	
<!-- Customized login page -->
<link href="css/login.css"
	rel="stylesheet" type="text/css">

<script src="js/prefixfree.min.js"></script>


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
				<span class="navbar-brand" href="#">Carnegie Financial
					Services</span>
			</div>
			<!-- /.navbar-header -->
			<ul class="nav navbar-top-links navbar-right">
				<li class="dropdown"><!-- <a class="dropdown-toggle"
					data-toggle="dropdown" href="#"> <i class="fa fa-user fa-fw"></i>
						<i class="fa fa-caret-down"></i>
				</a>
					<ul class="dropdown-menu dropdown-user"> -->
						<c:choose>
							<c:when test="${ employee == null && customer == null }">
								<li><a href="login_employee.do" 
									type="submit">  Employee Login</a></li>
								<li><a href="login_customer.do" 
									type="submit">  Customer Login</a></li>

							</c:when>
							<c:otherwise>
								<li>
									<a>Hi, ${employee.firstName}
										${employee.lastName}</a>
								</li>
								<li class="divider"></li>
								<li><a href="logout.do" 
									type="submit">  Logout</a></li>
								<li><a href="change_pwd.do" 
									type="submit">  Change Password</a></li>

							</c:otherwise>
						</c:choose>

					</ul> <!-- /.dropdown-user --></li>

				<!-- /.dropdown -->

			</ul>
		</nav>
			<!-- <li class="active">Login</li> -->
			</ol>
	</div>
	
	<jsp:include page="error-list.jsp" />

	<div class="container">

		<div class="body"></div>
		<div class="grad"></div>
		<div class="head">
		</div>
		<br>
		<br>
		<br>
		<br><br><br>
		<br><br><br><br>
		<br><br>
		<br><br><br>
				<div class="header-content">
            <div class="header-content-inner">
                <h1 style="font-size: 65px; text-align: center"> Carnegie Financial Services</h1>
                <hr>
                <p style="text-align: center">Please refer to the right upper corner to choose your identity</p>
            </div>
        </div>
		<script
			src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>
	</div>

	<!-- /body container -->


	<jsp:include page="buttom.jsp" />