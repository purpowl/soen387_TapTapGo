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

  <!-- Page Indicator -->
  <div class="card-header my-3 ">Order Details</div>
  
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
                <th scope="col"></th>
              </tr>
            </thead>
            <tbody>
              <% 
                for (Map.Entry<Integer, Order> orderEntry : orderList.entrySet()) {
                  Order order = orderEntry.getValue();
              %>
              <tr>
                <td><span class="badge badge-success"><%=order.getOrderID()%></span></td>
                <td><%=formatter.format(order.getPayDate())%></td>
                <td>$<%=Product.roundPrice(order.getTotalPrice() * 1.14975)%></td>
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
                <td> <a href="<%=request.getContextPath()%>/order-detail.jsp?from=${pageContext.request.requestURI}&orderID=<%=order.getOrderID()%>" class="btn btn-sm btn-outline-secondary">View order</a></td>
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