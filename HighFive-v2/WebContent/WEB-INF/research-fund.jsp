<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="customer-top.jsp"/>
<jsp:include page="error-list.jsp" />

<div>
	<table>	
		<tr style="vertical-align: middle; font-size: 15px">
			<form role="form" action="research_fund.do" method="POST">
				<th style="width: 80px; text-align: center"></th>
				<th style="width: 830px; text-align: center"><input class="form-control" name="search" placeholder="Please enter the name of the fund you want to find." value="${form.search}"></th>
				<th style="width: 80px; text-align: center"><input class="btn btn-default" type="submit" name="action" value="Search"></th>
			</form>
		</tr>	
    </table>
</div>
<br/>
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
		   		<th style="width: 40px; text-align: center"><a href="FundListTurnPage.do?page=${param.page-1}">&#60&#60&#60&#60&#60&#60</a></th>
		   </c:when>  
		   <c:otherwise>
		         <th style="width: 40px; text-align: center">&#60&#60&#60&#60&#60&#60</th>
		   </c:otherwise>
		   </c:choose>
		   
		   <th style="width: 80px; text-align: center">Page <fmt:parseNumber type="number" value="${param.page}" integerOnly="true" /></th>
		    
		   <c:choose>
		   <c:when test="${param.page+1<=(size+9)/10}" >
		   		<th style="width: 40px; text-align: center"><a href="FundListTurnPage.do?page=${param.page+1}">&#62&#62&#62&#62&#62&#62</a></th>
		   </c:when>  
		   <c:otherwise>
		         <th style="width: 40px; text-align: center">&#62&#62&#62&#62&#62&#62</th>
		   </c:otherwise>
		   </c:choose>
    

          <th style="width: 160px; text-align: center"> ${size} records in total</th>
       </tr>
    </table>
</div>
<br/>


					<div class="col-lg-6">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            Research Fund
                        </div>
                        <!-- /.panel-heading -->
<!--                         <div class="panel-body"> -->
                            <div class="table-responsive">
                                <table class="table">
                                    <thead>
                                        <tr>
                                            <th>Fund ID</th>
                                            <th>Name</th>
                                            <th></th>
                                            <th>Previous Closing Price</th>
                                        </tr>
                                    </thead>
			<c:forEach var="fund" items="${fundList}" begin="${(param.page-1)*10}" end="${(param.page-1)*10+9}">
                                    <tbody>
                                        <tr class="warning">
                                            <td><strong>${ fund.fundId }</strong></td>
                                            
                                            <td><%-- <a href="stats.do?fundId=${fund.fundId}"> --%>${fund.name} <!-- </a> --></td>
                                            <td><form action="stats.do" method="post">
								    <input type="hidden" name="id" value="${fund.fundId}">
								    <input type="submit" class="btn btn-sm btn-primary" value="Fund Stats">
								  </form></td>
                                           <%--  <td>${ fund.symbol }</td> --%>
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


</div>
</div>
</div>
</div>
<!-- /body container -->
<br>
<br>
<jsp:include page="buttom.jsp" />
