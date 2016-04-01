<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title>Employer Account - Carnegie Financial Services</title>

<!-- Bootstrap Core CSS -->
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

<!-- <script src="js/ie-emulation-modes-warning.js"></script> -->

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
				<a class="navbar-brand" href="#">Carnegie Financial
					Services</a>
			</div>
			<!-- /.navbar-header -->

			<ul class="nav navbar-top-links navbar-right">
				<li class="dropdown">
					<ul class="dropdown-menu dropdown-messages">
						<li class="divider"></li>
					</ul> <!-- /.dropdown-alerts --></li>
						<c:choose>
							<c:when test="${ employee == null && customer == null }">
								<li><a href="login_employee.do" class="fa fa-user fa-fw" type="submit">  
									Employee Login</a></li>
								<li><a href="login_customer.do" class="fa fa-user fa-fw"
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
									type="submit"> Change Password</a></li>

							</c:otherwise>
						</c:choose>

					</ul> <!-- /.dropdown-user --></li>
				<!-- /.dropdown -->
			</ul>
			<!-- /.navbar-top-links -->
			<!-- /.navbar-top-links -->
			<div class="navbar-default sidebar" role="navigation">
				<div class="sidebar-nav navbar-collapse">
					<ul class="nav" id="side-menu">
						<br>
						<li></li>
						<li><a href="customer_list.do"><i
								class="fa fa-dashboard fa-fw"></i> Manage Customer Account</a></li>
						<li><a href="create_customer.do"><i
								class="fa fa-users fa-fw"></i> Create User</a></li>
						<li><a href="create_employee.do"><i
								class="fa fa-user-md fa-fw"></i> Create Employee</a></li>
						<li><a href="create_fund.do"><i
								class="fa fa-money fa-fw"></i> Create Fund</a></li>					
						<li><a href="transition_day.do"><i
								class="fa fa-exchange fa-fw"></i> Transition Day</a></li>					
					</ul>
				</div>
				<!-- /.sidebar-collapse -->
			</div>
	</div>
	<!-- /.navbar-static-side -->
	</nav>
	</div>
	</nav>

	<!-- main content starts -->
	<div class="col-sm-8 col-sm-offset-3 col-md-10 col-md-offset-2 main">
		<div class="row">
			<div>
				<ol class="breadcrumb">