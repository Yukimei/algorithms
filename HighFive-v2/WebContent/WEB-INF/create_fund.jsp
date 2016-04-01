<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="employee-top.jsp"/>
<jsp:include page="error-list.jsp"/>

<!-- <div id="page-wrapper"> -->
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">Create Fund</h1>
                </div>
                <!-- /.col-lg-12 -->
            
                <!-- /.row -->
                <div class="row">
                    <div class="col-lg-12">
                        <div class="panel panel-default">
                            <div class="panel-body">
                                <div class="row">
                                    <div class="col-lg-6">
                                        <form role="form" action="create_fund.do" method="POST">
                                            <div class="form-group">
                                                <label>Fund Name</label>
                                                <input class="form-control" placeholder="Enter text" name="name" value="${form.name}">
                                                <p class="help-block">Please input the name of the fund here.</p>
                                            </div>
                                            <div class="form-group">
                                                <label>Ticker</label>
                                                <input class="form-control" placeholder="Enter text" name="symbol" value="${form.symbol}">
                                                <p class="help-block">Please input an one-to-five-character identifier.</p>
                                            </div>
                                            <input type="submit" name="action" class="btn btn-default" value="Create New Fund">
                                        </form>
                                    </div>
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
        <br>
        <br>
        <!-- /#page-wrapper -->
        <!-- /body container -->
</div>
</div>
</div>
</div>
<!-- /body container -->
<br>
<br>

<jsp:include page="buttom.jsp" />