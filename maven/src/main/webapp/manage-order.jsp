<%@ page import="com.taptapgo.Order" %>
<%@ page import="com.taptapgo.repository.OrderIdentityMap" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
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
  <!-- Sort button -->
  <div class="d-flex justify-content-end mb-3">
    <div class="dropdown">
      <button class="btn btn-sm dropdown-toggle" type="button" data-toggle="dropdown" aria-expanded="false">
        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-sort-up" viewBox="0 0 16 16">
          <path d="M3.5 12.5a.5.5 0 0 1-1 0V3.707L1.354 4.854a.5.5 0 1 1-.708-.708l2-1.999.007-.007a.498.498 0 0 1 .7.006l2 2a.5.5 0 1 1-.707.708L3.5 3.707zm3.5-9a.5.5 0 0 1 .5-.5h7a.5.5 0 0 1 0 1h-7a.5.5 0 0 1-.5-.5M7.5 6a.5.5 0 0 0 0 1h5a.5.5 0 0 0 0-1zm0 3a.5.5 0 0 0 0 1h3a.5.5 0 0 0 0-1zm0 3a.5.5 0 0 0 0 1h1a.5.5 0 0 0 0-1z"/>
        </svg>
        Sort
        <span class="caret"></span>
      </button>
      <div class="dropdown-menu">
        <a class="dropdown-item" href="<%=request.getContextPath()%>/manage-order.jsp?sort=id_descending">Order ID descending</a>
        <a class="dropdown-item" href="<%=request.getContextPath()%>/manage-order.jsp?sort=id_ascending">Order ID ascending</a>
        <a class="dropdown-item" href="<%=request.getContextPath()%>/manage-order.jsp?sort=pay_ascending">PayDate ascending</a>
        <a class="dropdown-item" href="<%=request.getContextPath()%>/manage-order.jsp?sort=pay_descending">PayDate descending</a>
        <a class="dropdown-item" href="<%=request.getContextPath()%>/manage-order.jsp?sort=ship_ascending">Shipped order first</a>
        <a class="dropdown-item" href="<%=request.getContextPath()%>/manage-order.jsp?sort=ship_descending">Unshipped order first</a>
      </div>
    </div>
  </div>
  <!-- Container Wrapper -->
  <div class="row">
    <div class="col-lg-12">
      <div class="card">
        <div class="card-body p-0 table-responsive">
          <!-- Order Info table -->
          <table class="table mb-0">
            <thead class="table-dark">
              <tr>
                <th data-sortable="true" data-sorter="alphanum" scope="col">ORDER #</th>
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
              List<Order> orderList = new ArrayList<Order>(allOrders.values());
              String sortParam = (String) request.getParameter("sort");

              if(sortParam == null){
                orderList = Order.sortOrdersBy(orderList, "ShipDate", "ascending");
              } else if (sortParam.equals("id_ascending")) {
                orderList = Order.sortOrdersBy(orderList, "ID", "ascending");
              } else if (sortParam.equals("id_descending")) {
                orderList = Order.sortOrdersBy(orderList, "ID", "descending");
              } else if (sortParam.equals("pay_ascending")) {
                orderList = Order.sortOrdersBy(orderList, "PayDate", "ascending");
              } else if (sortParam.equals("pay_descending")){
                orderList = Order.sortOrdersBy(orderList, "PayDate", "descending");
              } else if (sortParam.equals("ship_descending")){
                orderList = Order.sortOrdersBy(orderList, "ShipDate", "descending");
              } else {
                orderList = Order.sortOrdersBy(orderList, "ShipDate", "ascending");
              }
              
              for (Order order : orderList) {
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