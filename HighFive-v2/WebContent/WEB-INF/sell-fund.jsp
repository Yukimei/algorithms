<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="customer-top.jsp" />

<div id="wrapper">
	<div class="row">
		<div class="col-lg-12">
			<h1 class="page-header">Sell Fund | Customer: ${customer.firstName}
				${customer.lastName } ( ID :
		${customer.customerId} )</h1>
		</div>
		<jsp:include page="error-list.jsp" />
		<!-- /.col-lg-12 -->
	</div>

<h5
	style="margin-left: 15px; font-size: 16px; margin-bottom: 25px; margin-top: 25px">
	Available Balance: $ <fmt:formatNumber type="number" minFractionDigits="2"
		maxFractionDigits="2" value="${customer.pendingCash/100}" />
</h5>

<form action="sell_fund.do" class="form-inline" method="post">
	<div
		style="margin-left: 15px; margin-top: 35px; margin-bottom: 15px; font-size: 19px">
		<strong>Sell Fund</strong>
	</div>
	<div class="table-responsive">
		<table class="table table-striped"
			style="width: 980px; height: 30px; border: hidden; margin-left: 15px; font-size: 14px">
			<tbody>
				<tr style="vertical-align: middle; font-size: 15px">
					<th style="width: 80px; text-align: center">Fund ID</th>

					<th style="width: 240px; text-align: center">Share Amount</th>
				</tr>
				<tr>
					<td style="padding-left: 50px; vertical-align: left">
						<div class="form-group">

							<div class="input-group">
								<input type="text" class="form-control"
									style="border: 1px solid" placeholder="" name="fundId">
							</div>
						</div>
					</td>

					<td style="padding-left: 50px; vertical-align: left">
						<div class="form-group">
							<div class="input-group">
								<input type="hidden" name="fundIds" value="${ fund.fundId }">
								<input type="text" class="form-control"
									style="border: 1px solid" placeholder="(0.001 to 100,000 shares)"
									name="shares">
							</div>
						</div>
					</td>
				</tr>

			</tbody>
		</table>
	</div>
	<div>
		<input name="action" class="btn btn-default pull-left"
			style="width: 185px" type="submit" value="sell">
	</div>
</form>

<br>
<br>
<br>
<div style="margin-left: 15px; margin-top: 30px; font-size: 19px">
</div>




					<div class="col-lg-6">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            Your Fund Information
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
                                            <th>Available Shares</th>
                                            <th>Shares</th>
                                        </tr>
                                    </thead>

			<c:forEach var="position" items="${positionList}">
                                    <tbody>
                                        <tr class="warning">
                                            <td><strong>${ position.fundId }</strong></td>
                                            <td>${ position.fundName }</td>
                                            <td>${ position.symbol }</td>
                                            <td>$<fmt:formatNumber type="number" value="${position.price / 100}"
							minFractionDigits="2" maxFractionDigits="2" /></td>
											<td><fmt:formatNumber type="number"
							value="${position.pendingShares / 1000}" minFractionDigits="3"
							maxFractionDigits="3" /></td>
											<td><fmt:formatNumber type="number" value="${position.shares / 1000}"/></td>
                                        </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            <!-- /.table-responsive -->
                        </div>
                        <!-- /.panel-body -->
                    </div>
</div>
</div>
</div>
<br>
<br>
</div>
</div>
</div>
</div>
<!-- /body container -->
<br>
<br>
<jsp:include page="buttom.jsp" />