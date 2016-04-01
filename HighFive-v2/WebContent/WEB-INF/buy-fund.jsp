<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="customer-top.jsp" />

<div id="wrapper">
	<div class="row">
		<div class="col-lg-12">
			<h1 class="page-header">Buy Fund | Customer: ${customer.firstName}
				${customer.lastName } ( ID :
		${customer.customerId} )</h1>
		</div>
		<jsp:include page="error-list.jsp" />
		<!-- /.col-lg-12 -->
	</div>

<h5
	style="margin-left: 15px; font-size: 16px; margin-bottom: 25px; margin-top: 25px">
	Available Balance: $
	<fmt:formatNumber type="number" minFractionDigits="2"
		maxFractionDigits="2" value="${customer.pendingCash/100}" />
</h5>

<form action="by_fund.do" class="form-inline" method="post">
	<div
		style="margin-left: 15px; margin-top: 35px; margin-bottom: 15px; font-size: 19px">
		<strong>Purchase Fund</strong>
	</div>	
	<div class="table-responsive">
		<table class="table table-striped"
			style="width: 980px; height: 30px; border: hidden; margin-left: 15px; font-size: 14px">
			<tbody>
				<tr style="vertical-align: middle; font-size: 15px">
					<th style="width: 80px; text-align: center">Fund ID</th>

					<th style="width: 240px; text-align: center">Purchase Amount</th>
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
							<label class="sr-only" for="exampleInputAmount">Amount</label>
							<div class="input-group">
								<input type="hidden" name="fundIds" value="${ fund.fundId }">
								<input type="text" class="form-control"
									style="border: 1px solid"
									placeholder="$0.01 to $10,0000" name="amount">
							</div>
						</div>
					</td>
				</tr>

			</tbody>
		</table>
	</div>
	
	
	
	
	<div>
		<input name="action" class="btn btn-default pull-left"
			style="width: 185px" type="submit" value="buy">
	</div>
</form>

<br>
<br>
<br>


					<div class="col-lg-6">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            Fund Information
                        </div>
                        <!-- /.panel-heading -->
<!--                         <div class="panel-body"> -->
                            <div class="table-responsive">
                                <table class="table">
                                    <thead>
                                        <tr>
                                            <th>Fund ID</th>
                                            <th>Name</th>
                                            <th>Ticker</th>
                                            <th>Closing	Price</th>
                                        </tr>
                                    </thead>
							<c:forEach var="fund" items="${fundList}">
                                    <tbody>
                                        <tr class="warning">
                                            <td><strong>${ fund.fundId }</strong></td>
                                            <td>${fund.name}</td>
                                            <td>${ fund.symbol }</td>
                                            <td>$<fmt:formatNumber type="number" value="${fund.price/100}"
							minFractionDigits="2" maxFractionDigits="2" /></td>
                                        </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            <!-- /.table-responsive -->
                        </div>
                        <!-- /.panel-body -->
                    </div>
                    <!-- /.panel -->
                </div>	
<!-- /body container -->
</div>
</div>
</div>
</div>
<!-- /body container -->
<br>
<br>
<br>
<br>
<br>
<jsp:include page="buttom.jsp" />
