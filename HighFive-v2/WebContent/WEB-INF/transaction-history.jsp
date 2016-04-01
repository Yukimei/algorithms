<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:choose>
	<c:when test="${ isCustomer == true }">
		<jsp:include page="customer-top.jsp" />
		<li class="active">Customer Transaction History</li>

		<div id="wrapper">
			<div class="row">
				<div class="col-lg-12">
					<h1 class="page-header">Transaction History | Customer:
						${customer.firstName} ${customer.lastName } ( ID :
						${customer.customerId} )</h1>
				</div>
				<jsp:include page="error-list.jsp" />
			</div>
		</div>
	</c:when>
	<c:otherwise>
		<jsp:include page="employee-top.jsp" />
		<li><a href="customer_list.do">Manage Customer Account</a></li>
		<li class="active">View Transaction History</li>
		<div id="wrapper">
			<div class="row">
				<div class="col-lg-12">
					<h1 class="page-header">Transaction History | Customer:
						${customer.firstName} ${customer.lastName } ( ID :
						${customer.customerId} )</h1>
				</div>
				<jsp:include page="error-list.jsp" />
			</div>
		</div>
	</c:otherwise>
</c:choose>


<div class="col-md-12" style="margin-top: 15px">
	<!-- <div style="float:right; margin-right:5px; margin-bottom:10px"> -->
	<form role="form" action="view_account.do" method="POST">
		<input type="hidden" name="id" value="${customer.customerId}">
		<input class="btn btn-sm btn-primary" value="View Account"
			type="submit"
			style="font-size: 15px; float: right; margin-bottom: 10px; width: 210px">
	</form>
</div>
<div>
	<form role="form" action="trans_his.do" method="POST">
		<table>
			<tr>
				<th style="width: 80px; text-align: center"></th>
				<th style="width: 800px; text-align: center"><input
					class="form-control" name="search"
					placeholder="Please enter the name of the fund whose history you want to view."
					value="${form.search}"></th>
				<th style="width: 80px; text-align: center"><input
					class="btn btn-default" type="submit" name="action" value="Search"></th>
			</tr>
		</table>
	</form>
</div>
<br />
<div>
	<table>
		<tr>
			<c:choose>
				<c:when test="${!empty form.search}">
					<th style="width: 500px; text-align: left">The latest (up to 10)  transactions for fund
						name: ${form.search}</th>
				</c:when>
			</c:choose>
		</tr>
	</table>
	<table>
		<tr>
			<c:choose>
				<c:when test="${param.page>1}">
					<th style="width: 40px; text-align: center"><a
						href="ViewTransactionTurnPage.do?page=${param.page-1}">&#60&#60&#60&#60&#60&#60</a></th>
				</c:when>
				<c:otherwise>
					<th style="width: 40px; text-align: center">&#60&#60&#60&#60&#60&#60</th>
				</c:otherwise>
			</c:choose>

			<th style="width: 80px; text-align: center">Page <fmt:parseNumber
					type="number" value="${param.page}" integerOnly="true" /></th>

			<c:choose>
				<c:when test="${param.page+1<=(size+9)/10}">
					<th style="width: 40px; text-align: center"><a
						href="ViewTransactionTurnPage.do?page=${param.page+1}">&#62&#62&#62&#62&#62&#62</a></th>
				</c:when>
				<c:otherwise>
					<th style="width: 40px; text-align: center">&#62&#62&#62&#62&#62&#62</th>
				</c:otherwise>
			</c:choose>


			<th style="width: 160px; text-align: center">${size} records in
				total</th>
		</tr>
	</table>
</div>
<br />
<div class="panel-body">
	<div class="dataTable_wrapper">
		<table class="table table-striped table-bordered table-hover"
			id="dataTables-example">
			<thead>
				<tr>
					<th style="vertical-align: middle; text-align: center">#</th>
					<th style="vertical-align: middle; text-align: center">Date</th>
					<th style="vertical-align: middle; text-align: center">Action</th>


					<th style="vertical-align: middle; text-align: center">Fund
						Name</th>
					<th style="vertical-align: middle; text-align: center">Ticker</th>
					<th style="vertical-align: middle; text-align: center">shares of transaction</th>
					<th style="vertical-align: middle; text-align: center">Latest
						Price</th>
					<th style="vertical-align: middle; text-align: center">Total
						Amount</th>

				</tr>
			</thead>

			<tbody>


				<c:forEach var="trans" items="${allTrans}" varStatus="count"
					begin="${(param.page-1)*10}" end="${(param.page-1)*10+9}">
					<tr class="warning">
						<td
							style="text-align: center; vertical-align: middle; font-size: 15px">${count.index+1}
						</td>
						<td>
							<c:choose>
								<c:when test="${ trans.executeDate == null }">Pending</c:when>
								<c:otherwise>
									<fmt:formatDate pattern="yyyy-MM-dd"
										value="${ trans.executeDate }" />
								</c:otherwise>
							</c:choose>
						</td>
						<td>${trans.transactionType}
						</td>
						<td>
							${trans.name}
						</td>
						<td>${trans.symbol}</td>
						<td>
							<c:choose>
								<c:when test="${ trans.shares == 0 }">
								</c:when>
								<c:otherwise>
									<fmt:formatNumber type="number" 
									value="${trans.shares / 1000}"/>
								</c:otherwise>
							</c:choose>
						</td>
						<td>
							<c:choose>
								<c:when test="${ trans.price == 0 }">
								</c:when>
								<c:otherwise>
                                   $<fmt:formatNumber type="number"
										value="${trans.price / 100}" />
								</c:otherwise>
							</c:choose>
						</td>
						<td>
							<c:choose>
								<c:when test="${ trans.amount == 0 }">
								</c:when>
								<c:otherwise>
                                   $<fmt:formatNumber type="number"
										value="${trans.amount / 100}"/>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<br> <br> <br>
</div>
</div>
</div>
</div>
<!-- /body container -->

<jsp:include page="buttom.jsp" />
