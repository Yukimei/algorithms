<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<script type="text/javascript" src="js/epoch_classes.js"></script>
<link rel="stylesheet" type="text/css" href="css/epoch_styles.css" />

<jsp:include page="employee-top.jsp" />


</ol>
</div>
<h3 class="page-header" style="margin-left: 15px; font-size: 21px">Transition
	Day</h3>
<jsp:include page="error-list.jsp" />

<form action="transition_day.do" class="form-inline" method="POST">
	<div style="margin-left: 15px; margin-top: 30px; font-size: 19px">
	</div>
	<div style="margin-left: 15px; margin-top: 10px; font-size: 15px">
		<strong>Please input a date later than the last Transition
			Date.</strong>
	</div>

	<!--Datepicker container. The <input> text element is required, the button element is optional-->
	<div style="margin-left: 15px;">
		<!-- class="form-inline" -->
		<input type="text" id="date_field" name="date" class="form-control"
			style="width: 220" placeholder="mm/dd/yyyy" />
	</div>

	<h4 style="margin-left: 15px; margin-top: 25px; font-size: 18px">Last
		Transition Date : ${date}</h4>


	<br>
	<div class="col-lg-10">
		<div class="panel panel-default">
			<div class="panel-heading">Fund Information</div>
			<!-- /.panel-heading -->
			<!-- <div class="panel-body"> -->
			<div class="table-responsive">
				<table class="table">
					<thead>
						<tr>
							<th>Fund ID</th>
							<th>Name</th>
							<th>Ticker</th>
							<th>Previous Closing Price</th>
							<th>New Closing Price</th>
						</tr>
					</thead>
					<c:forEach var="fund" items="${fundList}">
						<tbody>
							<tr class="warning">
								<td><strong>${ fund.fundId }</strong></td>
								<td>${fund.name}</td>
								<td>${ fund.symbol }</td>
								<td>$<fmt:formatNumber type="number"
										value="${fund.price/100}" minFractionDigits="2"
										maxFractionDigits="2" /></td>
								<td>
									<div class="form-group">

										<div class="input-group">
											<input type="hidden" name="fundIds" value="${ fund.fundId }">
											<input type="text" class="form-control"
												style="border: 1px solid"
												placeholder="Dollar ( $1 to $999.99 )" name="prices">
										</div>
									</div>
								</td>
							</tr>
					</c:forEach>
					</tbody>
				</table>

			</div>
			<!-- /.table-responsive -->
		</div>
		<div>
			<input name="makeTransition" class="btn btn-default pull-left"
				style="width: 185px" type="submit" value="New Transition Begins">
		</div>
		<!-- /.panel-body -->
	</div>

</form>
</div>
</div>
</div>

<!-- /body container -->

<jsp:include page="buttom.jsp" />
