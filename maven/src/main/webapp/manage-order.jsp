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

  <!-- Search box -->
  <div class="d-flex justify-content-end">
    <form class="form-inline">
      <input class="form-control mr-sm-2" type="search" placeholder="Enter Order # to search" aria-label="Search">
      <button class="btn btn-outline-dark my-2 my-sm-0" type="submit">Search</button>
    </form>
  </div>

  <!-- Page Indicator -->
  <div class="card-header my-3 ">All Orders</div>
  
  <!-- Container Wrapper -->
  <div class="row">
    <div class="col-lg-12">
      <div class="card mt-4">
        <div class="card-body p-0 table-responsive">
          <!-- Order Info table -->
          <table class="table mb-0">
            <thead class="table-dark">
              <tr>
                <th scope="col">ORDER #</th>
                <th scope="col">ORDER PLACED</th>
                <th scope="col">SHIP TO</th>
                <th scope="col">ORDER STATUS</th>
                <th scope="col">SHIP DATE</th>
                <th scope="col">TRACKING #</th>
              </tr>
            </thead>
            <tbody>
            <%
              for (Map.Entry<Integer, Order> orderEntry : allOrders.entrySet()) {
                Order order = orderEntry.getValue();
            %>
              <tr>
                <td><span class="badge badge-success"><%=order.getOrderID()%></span></td> <!-- TODO -->
                <td><%=formatter.format(order.getPayDate())%></td> <!-- TODO-->
                <td>$<%=Product.roundPrice(order.getTotalPrice())%></td> <!-- TODO-->
                <td><%=order.getShippingAddress()%></td> <!-- TODO: Address without City, Country, Postal Code-->
                <td><%=order.shipStatusToString()%></td><!-- TODO-->
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
              </tr>
              <tr>
              <!-- For each product in the order, display the image and the description -->
              <% 
                  for (Map.Entry<Product, Integer> productEntry : order.getOrderProducts().entrySet()) {
                    Product product = productEntry.getKey();
                    int amount = productEntry.getValue();
              %>
              <tr>
                  <th colspan="2"><img src="<%=request.getContextPath()%>/images/epomaker_alice.jpg" alt="product" class="" width="150"></th>  <!-- TODO -->
                  <td colspan="1" style="vertical-align: middle;"><%=product.getName()%></td> <!-- TODO -->
                  <td colspan="1" style="vertical-align: middle;">x<span><%=amount%></span></td> <!-- TODO -->
                  <td colspan="2" style="vertical-align: middle;"><%=product.getDescription()%></td> <!-- TODO -->
              </tr>
              <tr>
                <%
                  if (order.getTrackingNumber() == null) {
                %>
                  <td>
                      <a href="<%=request.getContextPath()%>/shipOrder/<%=order.getOrderID()%>">
                          <button style=" background: hsl(221, 100%, 33%);color: hsl(221, 100%, 95%)" class="btn btn-block">Ship order</button>
                      </a>
                  </td>
                <%
                  } else {
                %>
                  <td>
                      <button class="btn btn-secondary btn-block">Shipped</button>
                  </td>
                <%
                  }
                %>
                <td>
                    <a href="<%=request.getContextPath()%>/order-detail.jsp">
                        <button class="btn btn-secondary btn-block">View order detail</button>
                    </a>
                </td> 
                <td colspan="4"></td> 
              </tr>
              <%
                  }
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