<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:choose>
    <c:when test="${ isCustomer == true }">
        <jsp:include page="customer-top.jsp"/>
    </c:when>
    <c:otherwise>
        <jsp:include page="employee-top.jsp"/>
    </c:otherwise>
</c:choose>


<div id="wrapper">
	<div class="row">
		<div class="col-lg-12">
			<h1 class="page-header">Price
	History of ${ fund.name } | Ticker : ${fund.symbol} ( Fund ID: ${fund.fundId} )</h1>
		</div>
		<jsp:include page="error-list.jsp" />
		<!-- /.col-lg-12 -->
	</div>

	
<jsp:include page="error-list.jsp" />

<c:choose>
    <c:when test="${!empty history}">
        <div id="curve_chart" style="width: 900px; height: 500px"></div>
    </c:when>
</c:choose> 

<div id="ex1"></div> <br>


					<div class="col-lg-6">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            Price History
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                            <div class="table-responsive">
                                <table class="table">
                                    <thead>
                                        <tr>
                                            <th>Price Date</th>
                                            <th>Closing Price</th>
                                        </tr>
                                    </thead>
 			<c:forEach var="priceRecord" items="${ history }">
                                    <tbody>
                                        <tr class="warning">
                                            <td><fmt:formatDate value='${priceRecord.priceDate}' var='pricedate'
						type='date' pattern='yyyy-MM-dd' /> ${ pricedate }</td>
                                            <td>$<fmt:formatNumber value='${priceRecord.price/100}' type='number' minFractionDigits='2' maxFractionDigits='2'/></td>
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


<%-- <div class="table-responsive">
	<table class="table table-striped" style="width: 1000px">
	    <tbody>
			<tr style="vertical-align: middle; font-size: 15px">
				<th style="width: 80px; text-align: center">Price Date</th>
				<th style="width: 350px; text-align: center">Closing Price</th>
			</tr>
			
 			<c:forEach var="priceRecord" items="${ history }">
				<tr>
					<td style="vertical-align: middle; text-align: center"><fmt:formatDate value='${priceRecord.priceDate}' var='pricedate'
						type='date' pattern='yyyy-MM-dd' /> ${ pricedate }</td>
					<td style="vertical-align: middle; text-align: center">$<fmt:formatNumber value='${priceRecord.price/100}' type='number' minFractionDigits='2' maxFractionDigits='2'/></td>
				</tr>
			</c:forEach> 
		</tbody>
	</table>
</div>  --%>

<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
<script type="text/javascript">
  google.charts.load('current', {'packages':['corechart']});
  google.charts.setOnLoadCallback(drawChart);
  function drawChart() {
    var data = google.visualization.arrayToDataTable([
      ['Date', 'Closing Price', ],        
      <c:forEach var="priceRecord" items="${ history }">
	  <fmt:formatDate value='${priceRecord.priceDate}' var='year' type='date' pattern='yyyy'/>
	  <fmt:formatDate value='${priceRecord.priceDate}' var='month' type='date' pattern='MM'/>
	  <fmt:formatDate value='${priceRecord.priceDate}' var='day' type='date' pattern='dd'/>
	  <fmt:formatNumber value='${priceRecord.price/100}' var='price' type='number' minFractionDigits='2' maxFractionDigits='2'/>
	  [new Date(${year}, ${month - 1}, ${day}), ${price}],
	  </c:forEach>
    ]);
    var options = {
      title: 'Fund Stats',
      legend: { position: 'bottom' }
    };
    var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));
    chart.draw(data, options);
  }
  
</script>

<jsp:include page="buttom.jsp" />