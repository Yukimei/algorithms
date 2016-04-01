<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="customer-top.jsp" />
<div id="wrapper">
	<div class="row">
		<div class="col-lg-12">
			<h1 class="page-header">Request Check</h1>
		</div>
		<!-- /.col-lg-12 -->
	</div>
	<!-- /.row -->
	<div class="row">
		<jsp:include page="error-list.jsp" />
		<div class="col-lg-12">
			<div class="panel panel-default">
				<div class="panel-heading">Check Request Form</div>
				<div class="panel-body">
					<div class="row">
						<div class="col-lg-6">

							<div class="form-group">
								<label>Cash Balance</label>
								<p class="form-control-static">USD &nbsp;${customer.cash / 100}</p>
							</div>
							<div class="form-group">
								<label>Cash Available For Check</label>
								<p class="form-control-static">USD
									&nbsp;${(customer.pendingCash) / 100}</p>
							</div>


							<div>
								<form role="form" action="request_check.do" method="POST">

									<div class="form-group">
										<label>Enter Cash Amount For Check</label> <input type="text"
											class="form-control" name="amount">
										<p class="help-block">Cash Amount need to be larger than $0.01, and equal or
											smaller than cash available for check.</p>
									</div>
									<!--                                         <div class="form-group">
                                            <label>Select Bank Account</label>
                                            <select class="form-control">
                                                <option>PNC 3400 **** **** 3984</option>
                                                <option>Chase & Co. 4718 ****5678</option>
                                            </select>
                                        </div> -->

									<input type="submit" name="action" class="btn btn-default" value="Submit"/>
									<!-- <button type="reset" class="btn btn-default">Reset</button> -->

								</form>
							</div>
							<!-- /.col-lg-6 (nested) -->
						</div>
						<!-- /.row (nested) -->
					</div>
					<!-- /.panel-body -->
				</div>
				<!-- /.panel -->
			</div>
			<!-- /.col-lg-12 -->
		</div>
		<!-- /.row -->
	</div>
	<!-- /#page-wrapper -->


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
<!-- FOOTER -->
<jsp:include page="buttom.jsp" />
</div>
<!-- /.container -->

<!-- /#wrapper -->

<!-- jQuery -->
<script src="../bower_components/jquery/dist/jquery.min.js"></script>

<!-- Bootstrap Core JavaScript -->
<script src="../bower_components/bootstrap/dist/js/bootstrap.min.js"></script>

<!-- Metis Menu Plugin JavaScript -->
<script src="../bower_components/metisMenu/dist/metisMenu.min.js"></script>

<!-- Custom Theme JavaScript -->
<script src="../dist/js/sb-admin-2.js"></script>

</body>

</html>

