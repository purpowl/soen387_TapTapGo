<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.taptapgo.Order" %>
<%@ page import="com.taptapgo.Product" %>
<%@ page import="com.taptapgo.repository.OrderIdentityMap" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="com.taptapgo.User" %>
<%@ page import="com.taptapgo.exceptions.InvalidParameterException" %>

<%
  HttpSession currentSession = request.getSession();

  // Redirect to ship order if this is staff
  if (currentSession.getAttribute("staff") != null) {
    response.sendRedirect(request.getContextPath() + "/ship-orders.jsp");
  }

  // Redirect to anonymous order retrieval if this is not registered user
  if (currentSession.getAttribute("registered_user") == null) {
    response.sendRedirect(request.getContextPath() + "/orders.jsp");
  }

  Object user_object = currentSession.getAttribute("registered_user");
  User user = (User) user_object;
  HashMap<Integer, Order> orderList;
  try {
    orderList = OrderIdentityMap.getOrdersByUser(user);
  } catch (InvalidParameterException e) {
    e.printStackTrace();
    return;
  }
  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
%>
<html>
<head>
  <%@include file="includes/header.jsp" %>
  <title>Orders</title>
</head>
<body>
<%@include file="includes/navbar.jsp" %>

<div class="container mb-5">

  <!-- Search box -->
  <div class="d-flex justify-content-end">
    <form class="form-inline">
      <input class="form-control mr-sm-2" type="search" placeholder="Enter OrderID to search" aria-label="Search">
      <button class="btn btn-outline-dark my-2 my-sm-0" type="submit">Search</button>
    </form>
  </div>

  <!-- Page Indicator -->
  <div class="card-header my-3 ">Order Details</div>
  
  <!-- Container Wrapper -->
  <div class="row">
    <div class="col-lg-12">
      <div class="card mt-4">
        <div class="card-body p-0 table-responsive">
          <!-- Order Info table -->
          <table class="table mb-0">
            <thead class="table-dark">
              <tr>
                <th scope="col">ORDER ID</th>
                <th scope="col">ORDER PLACED</th>
                <th scope="col">TOTAL</th>
                <th scope="col">SHIP TO</th>
                <th scope="col">ORDER STATUS</th>
              </tr>
            </thead>
            <tbody>
              <% 
                for (Map.Entry<Integer, Order> orderEntry : orderList.entrySet()) {
                  Order order = orderEntry.getValue();
              %>
              <tr>
                <td><%=order.getOrderID()%></td> <!-- TODO -->
                <td><%=formatter.format(order.getPayDate())%></td> <!-- TODO-->
                <td>$<%=Product.roundPrice(order.getTotalPrice())%></td> <!-- TODO-->
                <td><%=order.getShippingAddress()%></td> <!-- TODO: Address without City, Country, Postal Code-->
                <td><%=order.shipStatusToString()%></td><!-- TODO-->
              </tr>
              <!-- For each product in the cart, display the image and the description -->
              <% 
                  for (Map.Entry<Product, Integer> productEntry : order.getOrderProducts().entrySet()) {
                    Product product = productEntry.getKey();
              %>
              <tr>
                  <th><img src="<%=request.getContextPath()%>/images/epomaker_alice.jpg" alt="product" class="" width="150"></th>  <!-- TODO -->
                  <td style="vertical-align: middle;"><%=product.getName()%></td> <!-- TODO -->
                  <td colspan="3" style="vertical-align: middle;"><%=product.getDescription()%></td> <!-- TODO -->
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