<%@ page import="com.taptapgo.Order" %>
<%@ page import="com.taptapgo.repository.OrderIdentityMap" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.taptapgo.Product" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%
  // only staff can access this page, redirect to login page otherwise
  session = request.getSession(false);
  if (session == null || session.getAttribute("isStaff") == null) {
    response.sendRedirect("login.jsp");
  }
  HashMap<Integer, Order> allOrders = OrderIdentityMap.loadAllOrders();
  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
%>
<html>
<head>
  <%@include file="includes/header.jsp" %>
  <title>Ship Order</title>
</head>
<body>
<%@include file="includes/navbar.jsp" %>

<div class="container mb-5">
   <!-- Page Indicator -->
  <div class="card-header my-3 ">All Orders</div>
  
  <!-- Container Wrapper -->
  <div class="row">
    <div class="col-lg-12">
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
                <th scope="col">ORDER TYPE</th>
                <th scope="col"></th>
                <th scope="col"></th>
              </tr>
            </thead>
            <tbody>
            <%
              for (Map.Entry<Integer, Order> orderEntry : allOrders.entrySet()) {
                Order order = orderEntry.getValue();
            %>
              <tr>
                <td><span class="badge badge-success"><%=order.getOrderID()%></span></td>
                <td><%=formatter.format(order.getPayDate())%></td>
                <td>$<%=Product.roundPrice(order.getTotalPrice() * 1.14975)%></td>
                <td><%=order.getShippingAddress()%></td>
                <!-- Ship Status -->
                <td><%=order.shipStatusToString()%></td>
                <!-- Tracking Number -->
                <%
                  if (order.getTrackingNumber() == null) {
                %>
                  <td>N/A</td>
                <%
                  } else {
                %>
                <td><%=order.getTrackingNumber()%></td>
                <% } %>
                <!-- guest or registered user's order -->
                <%
                  if (order.getCustomerID().startsWith("rc")) {
                %>
                <td>Registered</td>
                <%
                } else {
                %>
                <td>Guest</td>
                <% } %>
                <!-- Ship button -->
                <%
                if (order.getTrackingNumber() == null) {
                %>
                <td>
                  <a href="<%=request.getContextPath()%>/shipOrder/<%=order.getOrderID()%>" 
                      style=" background: hsl(221, 100%, 33%);color: hsl(221, 100%, 95%)" 
                      class="btn btn-sm">Ship
                  </a>
                </td>
                <%
                  } else {
                %>
                <td></td>
                <%
                  }
                %>
                <!-- View Order Detail button -->
                <td> <a href="<%=request.getContextPath()%>/order-detail.jsp?from=${pageContext.request.requestURI}&orderID=<%=order.getOrderID()%>" class="btn btn-sm btn-outline-secondary">View</a></td>
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