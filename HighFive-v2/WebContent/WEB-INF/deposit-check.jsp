<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="employee-top.jsp"/>



<!--         <div id="page-wrapper"> -->
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">Deposit Check</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-lg-6">
                                <jsp:include page="error-list.jsp" />
                                    <form role="form" action="deposit_check.do" method="POST">
                                        <input type="hidden" name="id" value="${customer.customerId}">
                                        <div class="form-group input-group">
                                            <span class="input-group-addon">$</span>
                                            <input type="text" class="form-control" placeholder="Enter Amount (more than $0.01 and no more than $1,000,000)" name="amount">
                                        </div>
                                        <div>
                                            <p class="help-block">Customer: ${customer.firstName} ${customer.lastName}
        (ID: ${customer.customerId}).</p>
                                        </div>
                                        <input type="submit" name="action" class="btn btn-default" value="Deposit Check">
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
            <br>
            <!-- /.row -->
<!--         </div> -->
        <!-- /#page-wrapper -->
<br><br><br><br><br><br><br><br><br><br>
<div></div>
</div>
</div>
</div>
<jsp:include page="buttom.jsp" />
