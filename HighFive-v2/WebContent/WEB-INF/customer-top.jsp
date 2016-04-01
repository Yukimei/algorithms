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

<title>Customer Account - - Carnegie Financial Services</title>

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
				<li class="dropdown">
					<ul class="dropdown-menu dropdown-messages">

					</ul> <!-- /.dropdown-messages -->
				</li>
				<!-- /.dropdown -->


						<c:choose>
							<c:when test="${ employee == null && customer == null }">
								<li class="dropdown-toggle"><a href="login_employee.do" class="fa fa-user fa-fw"
									type="submit">Employee Login</a></li>
								<li class="dropdown-toggle"><a href="login_customer.do" class="fa fa-user fa-fw"
									type="submit">Customer Login</a></li>

							</c:when>
							<c:otherwise>
								<li ><a>Hi, ${customer.firstName} ${customer.lastName}</a></li>
								<li class="divider"></li>
								<li class="dropdown"><a href="logout.do" 
									type="submit"> Logout</a></li>
								<li class="dropdown-toggle"><a href="change_pwd.do" 
									type="submit"> Change Password</a></li>

							</c:otherwise>
						</c:choose>

					</ul> <!-- /.dropdown-user --></li>
					
				<!-- /.dropdown -->
			</ul>
			<!-- /.navbar-top-links -->
			<div class="navbar-default sidebar" role="navigation">
				<div class="sidebar-nav navbar-collapse">
					<ul class="nav" id="side-menu">
						<br>
						<li></li>
						<li><a href="view_account.do"><i
								class="fa fa-dashboard fa-fw"></i> View Account</a></li>
						
						<li><a href="by_fund.do"> <i class="fa fa-long-arrow-left fa-fw"></i> Buy
								Fund
						</a></li>
						<li><a href="sell_fund.do"><i class="fa fa-long-arrow-right fa-fw"></i> Sell
								Fund</a></li>
						<li><a href="request_check.do"><i
								class="fa fa-edit fa-fw"></i> Request Check</a></li>
						<li><a href="trans_his.do"><i
								class="fa fa-wrench fa-fw"></i> Transaction History</a></li>

						<li class="active"><a href="research_fund.do"><i
								class="fa fa-files-o fa-fw"></i> Research Fund</span></a></li>
					</ul>
				</div>
			</div>
	</div>
	</nav>
	<div class="col-sm-8 col-sm-offset-3 col-md-10 col-md-offset-2 main">
		<div class="row">
			<div>
				<ol class="breadcrumb">