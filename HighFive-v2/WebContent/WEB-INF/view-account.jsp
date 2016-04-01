<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:choose>
	<c:when test="${ isCustomer == true }">
		<jsp:include page="customer-top.jsp" />
	</c:when>
	<c:otherwise>
		<jsp:include page="employee-top.jsp" />
	</c:otherwise>
</c:choose>

<div id="wrapper">
	<div class="row">
		<div class="col-lg-12">
			<h1 class="page-header">View ${customer.firstName}
				${customer.lastName } Account</h1>
		</div>
		<jsp:include page="error-list.jsp" />
		<!-- /.col-lg-12 -->
	</div>

	<div class="row">
		<div class="col-lg-3 col-md-6" style="width: 300px">
			<div class="panel panel-primary">
				<div class="panel-heading">
					<div class="row">
						<div class="col-xs-3">
							<i class="fa fa-comments fa-5x"></i>
						</div>
						<div class="col-xs-9 text-right">
							<div class="huge">${customer.firstName}
								${customer.lastName}</div>
							<div>${customer.addrLine1},${customer.addrLine2 },
								${customer.city }, ${customer.zip }, ${customer.state }</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="col-lg-3 col-md-6" style="width: 300px">
			<div class="panel panel-green">
				<div class="panel-heading">
					<div class="row">
						<div class="col-xs-3">
							<i class="fa fa-tasks fa-5x"></i>
						</div>
						<div class="col-xs-9 text-right">
							<div class="huge">
								<fmt:formatDate type="date" value="${date}" />
							</div>
							<div>Last Trading Day</div>

						</div>
					</div>
				</div>
				<a href="trans_his.do">
					<div class="panel-footer">
						<span class="pull-left">View Transaction History</span> <span
							class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
						<div class="clearfix"></div>
					</div>
				</a>
			</div>
		</div>

		<div class="col-lg-3 col-md-6" style="width: 300px">
			<div class="panel panel-red">
				<div class="panel-heading">
					<div class="row">
						<div class="col-xs-3">
							<i class="fa fa-support fa-5x"></i>
						</div>
						<div class="col-xs-9 text-right">
							<div>USD ${customer.cash / 100}</div>
							<div>Cash Balance</div>
							<div>USD ${customer.pendingCash / 100}</div>
							<div>Available Cash Balance</div>
						</div>
					</div>
				</div>
				<c:choose>
					<c:when test="${ isCustomer == true }">
						<a href="request_check.do">
							<div class="panel-footer">
								<span class="pull-left">Cash For Check</span> <span
									class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
								<div class="clearfix"></div>
							</div>
						</a>
					</c:when>
					<c:otherwise>
						<a href="deposit_check.do">
							<div class="panel-footer">
								<span class="pull-left">Deposit a Check</span> <span
									class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
								<div class="clearfix"></div>
							</div>
						</a>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>


	<div class="col-lg-6">
		<div class="panel panel-default">
			<div class="panel-heading">
				<i class="fa fa-bar-chart-o fa-fw"></i> Current Account Portfolio
			</div>
			<!-- /.panel-heading -->
			<!--                         <div class="panel-body"> -->
			<div class="table-responsive">
				<table class="table">
					<thead>
						<tr>
							<th>Fund ID</th>
							<th>Fund Name</th>
							<th>Fund Symbol</th>
							<th>Last Transaction Day Price</th>
							<th>Shares</th>
							<th>Value</th>
						</tr>
					</thead>

					<c:forEach var="position" items="${positionList}">
						<tbody>
							<tr class="warning">
								<td><strong>${ position.fundId }</strong></td>
								<td>${ position.fundName }</td>
								<td>${ position.symbol }</td>
								<td>$<fmt:formatNumber type="number"
										value="${position.price / 100}" minFractionDigits="2"
										maxFractionDigits="2" /></td>
								<td><fmt:formatNumber type="number"
										value="${position.shares / 1000}" minFractionDigits="3"
										maxFractionDigits="3" /></td>
								<td><fmt:formatNumber type="number"
										value="${position.value / 100000}" minFractionDigits="3"
										maxFractionDigits="3" /></td>
							</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
			<!-- /.table-responsive -->
		</div>
		<!-- /.panel-body -->
	</div>
	<!-- /.panel-body -->
</div>
<!-- /.panel -->

<!-- /body container -->
</div>
</div>
</div>
</div>
<!-- /body container -->
<br>
<br>
</div>
<!-- /.container-fluid -->
</div>
<br>
<br>
<br>
<jsp:include page="buttom.jsp" />
</body>
</html>

