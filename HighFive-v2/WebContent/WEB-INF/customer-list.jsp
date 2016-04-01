<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<jsp:include page="employee-top.jsp" />

                    <li class="active">Manage Customer Account</li>        

					<h3 class="page-header" style="margin-left: 15px;font-size:21px">Manage Customer Account</h3>
<jsp:include page="error-list.jsp" />				
					
				  <div>
				  <form role="form" action="customer_list.do" method="POST">
					<table>
						<tr>
							<th style="width: 80px; text-align: center"></th>
							<th style="width: 800px; text-align: center"><input class="form-control" name="search" placeholder="Please enter the Username of the customer whose record you want to view." value="${form.search}"></th>
							<th style="width: 80px; text-align: center"><input class="btn btn-default" type="submit" name="action" value="Search"></th>
						</tr>
					</table>
				  </form>
				  </div>
					<div>
					    <table>
					       <tr>
					       	  <c:choose>
							  <c:when test="${!empty form.search}" >
							   		<th style="width: 200px; text-align: center">Results for fund name: ${form.search}</th>
							  </c:when>  
							  </c:choose>
					       </tr>
					    </table>
						<table>
					  	   <tr>
							   <c:choose>
							   <c:when test="${param.page>1}" >
							   		<th style="width: 40px; text-align: center"><a href="CustomerListTurnPage.do?page=${param.page-1}">&#60&#60&#60&#60&#60&#60</a></th>
							   </c:when>  
							   <c:otherwise>
							         <th style="width: 40px; text-align: center">&#60&#60&#60&#60&#60&#60</th>
							   </c:otherwise>
							   </c:choose>
							   
							   <th style="width: 80px; text-align: center">Page <fmt:parseNumber type="number" value="${param.page}" integerOnly="true" /></th>
							    
							   <c:choose>
							   <c:when test="${param.page+1<=(size+9)/10}" >
							   		<th style="width: 40px; text-align: center"><a href="CustomerListTurnPage.do?page=${param.page+1}">&#62&#62&#62&#62&#62&#62</a></th>
							   </c:when>  
							   <c:otherwise>
							         <th style="width: 40px; text-align: center">&#62&#62&#62&#62&#62&#62</th>
							   </c:otherwise>
							   </c:choose>
					    

					          <th style="width: 160px; text-align: center"> ${size} records in total</th>
					       </tr>
					    </table>
					</div>
<!-- 					<div class="col-md-12">
						<div style="font-size: 20px; margin-top: 10px;margin-bottom:10px;">
							<strong>Customer List</strong>
						</div>
					</div> -->
					<br>
					<div class="col-lg-6">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            Customer List
                        </div>
                        <!-- /.panel-heading -->
<!--                         <div class="panel-body"> -->
                            <div class="table-responsive">
                                <table class="table">
                                    <thead>
                                        <tr>
                                            <th>Name</th>
                                            <th>Username</th>
                                            <th>Customer ID</th>
                                            <th>View Account</th>
                                            <th>Reset Password</th>
                                        </tr>
                                    </thead>
							<c:forEach var="customer" items="${ customerList }" begin="${(param.page-1)*10}" end="${(param.page-1)*10+9}">
                                    <tbody>
                                        <tr class="warning">
                                            <td><strong>${customer.firstName} ${customer.lastName}</strong></td>
                                            <td>${customer.userName}</td>
                                            <td>${customer.customerId}</td>
                                            <td><form action="view_account.do" method="post">
								    <input type="hidden" name="id" value="${customer.customerId}">
								    <input type="submit" class="btn btn-sm btn-primary" value="View Account">
								  </form></td>
								  			<td><form action="reset_pwd.do" method="post">
								    <input type="hidden" name="id" value="${customer.customerId}">
								    <input type="submit" class="btn btn-sm btn-primary" value="Reset Password">
								  </form></td>
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
					
					<br>
					<div></div>
					<p>
</div>
</div>
</div>
</div>
<!-- /body container -->
<br>
<br>
	<!-- /body container -->

<jsp:include page="buttom.jsp" />
