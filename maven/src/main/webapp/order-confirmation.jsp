<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="com.taptapgo.Order" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.taptapgo.Product" %>

<%
  HttpSession currentSession = request.getSession();
  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

  if(currentSession.getAttribute("order") == null) {
    response.sendRedirect(request.getContextPath() + "/orders");
  }

  Order order = (Order) currentSession.getAttribute("order");
%>

<html>
<head>
  <%@include file="includes/header.jsp" %>
  <title>Order Confirmation</title>
</head>
<body>
<%@include file="includes/navbar.jsp" %>

<div class="container mb-5">
    <!-- Container Wrapper -->
  <div class="row">
    <!-- Order Confirmation message -->
    <div class="col-lg-12 mt-3">
        <h2>Order confirmed!</h2>
        <p class="fw-bold">Your order has been confirmed. You will receive your tracking number via email within the next few days!</p>
    </div>
    <!-- Order Details -->
    <div class="col-lg-12">
      <div class="card mt-3">
        <div class="card-body p-0 table-responsive">
          <!-- Order Info table -->
          <table class="table mb-0">
            <thead class="table-dark">
              <tr>
                <th scope="col">ORDER #</th>
                <th scope="col">ORDER PLACED</th>
                <th scope="col">TOTAL</th>
                <th scope="col">SHIP TO</th>
                <th scope="col">ORDER STATUS</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td><span class="badge badge-success"><%=order.getOrderID()%></span></td> <!-- TODO -->
                <td><%=formatter.format(order.getPayDate())%></td> <!-- TODO-->
                <td>$<%=Product.roundPrice(order.getTotalPrice())%></td> <!-- TODO-->
                <td><%=order.getShippingAddress()%></td> <!-- TODO: Address without City, Country, Postal Code-->
                <td>Order Received</td>
              </tr>
              <!-- For each product in the cart, display the image and the description -->
              <% 
                  for (Map.Entry<Product, Integer> productEntry : order.getOrderProducts().entrySet()) {
                    Product product = productEntry.getKey();
                    int amount = productEntry.getValue();
              %>
              <tr>
                  <td colspan="2">
                    <% if (product.getImagePath() != null) { %>
                      <img src="<%=request.getContextPath()%>/images/products<%=product.getImagePath()%>" alt="product" class="" width="150">
                    <% } else { %>
                      <img src="<%=request.getContextPath()%>/images/epomaker_alice.jpg" alt="product" class="" width="150">
                    <% } %>
                  </td>
                  <td colspan="1" style="vertical-align: middle;"><%=product.getName()%></td> <!-- TODO -->
                  <td colspan="1" style="vertical-align: middle;">x<span><%=amount%></span></td> <!-- TODO -->
                  <td colspan="2" style="vertical-align: middle;"><%=product.getDescription()%></td> <!-- TODO -->
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