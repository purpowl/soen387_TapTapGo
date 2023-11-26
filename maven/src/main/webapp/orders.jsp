<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.taptapgo.Order" %>
<%@ page import="com.taptapgo.Product" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<html>
<head>
  <%@include file="includes/header.jsp" %>
  <title>Order Details</title>
</head>
<body>
<%@include file="includes/navbar.jsp" %>

<div class="container mb-5">

  <!-- Search box -->
  <div class="d-flex justify-content-end">
    <form class="form-inline" action="<%=request.getContextPath()%>/orders" method="post">
      <input class="form-control mr-sm-2" type="search" placeholder="Enter OrderID to search" aria-label="Search" id="orderID" name="orderID">
      <button class="btn btn-outline-dark my-2 my-sm-0" type="submit">Search</button>
    </form>
  </div>

  <!-- Page Indicator -->
  <div class="card-header my-3 ">Order Details</div>

  <!-- Check if a search was carried out -->
  <%
      String createStatus = request.getParameter("search");
      boolean searched = false;
      HttpSession currentSession = request.getSession();
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

      if (createStatus != null) {
          if (createStatus.equals("success")) {
            searched = true;
          } else {
  %>
  <div class="row">
      <div class="col-12">
          <p style="color: red;">Order not found!</p>
      </div>
  </div>
  <%
          }
      }
  %>
  
  <!-- Container Wrapper -->
  <div class="row">
    <div class="col-lg-12 my-3">
      <div class="card">
        <div class="card-body p-0 table-responsive">
          <!-- Order Info table -->
          <table class="table mb-0">
            <thead class="table-dark">
              <tr>
                <th scope="col">ORDER #</th>
                <th scope="col">PAY DATE</th>
                <th scope="col">TOTAL</th>
                <th scope="col">SHIP TO</th>
                <th scope="col">ORDER STATUS</th>
                <th scope="col">TRACKING #</th>
                <th scope="col"></th>
              </tr>
            </thead>
            <tbody>
              <%
                if (searched) {
                  Object order_object = currentSession.getAttribute("order");
                  currentSession.removeAttribute("order");

                  Order order = (Order) order_object;
              %>
              <tr>
                <td><span class="badge badge-success"><%=order.getOrderID()%></span></td>
                <td><%=formatter.format(order.getPayDate())%></td>
                <td>$<%=Product.roundPrice(order.getTotalPrice())%></td>
                <td><%=order.getShippingAddress()%></td>
                <td><%=order.shipStatusToString()%></td>
                <%
                  if (order.getTrackingNumber() == null) {
                %>
                  <td>N/A</td>
                <%
                  } else {
                %>
                  <td><%=order.getTrackingNumber()%></td>
                <%
                  }
                %>
                <!-- View Order Detail button -->
                <td> <a href="<%=request.getContextPath()%>/order-detail.jsp?orderID=<%=order.getOrderID()%>" class="btn btn-sm btn-outline-secondary">View order</a></td> 
              </tr>
              <%
                }
              %>
            </tbody>
          </table>
          <!-- Order Info table -->
        </div>
      </div>
    </div>
  </div>
</div>

<%@include file="includes/footer.jsp" %>
</body>
</html>