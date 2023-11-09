<%@ page import="com.taptapgo.Staff" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%
    // only staff can access this page, redirect to login page otherwise
    session = request.getSession(false);
    if (session == null || session.getAttribute("isStaff") == null) {
        response.sendRedirect("login.jsp");
    }
    Staff staff = (Staff) session.getAttribute("staff");
    String staffID = staff.getUserID();
%>
<html>
<head>
    <%@include file="includes/header.jsp" %>
    <title>Ship Order</title>
</head>
<body>
<%@include file="includes/navbar.jsp" %>

<div class="container mb-4">
    <div class="card-header my-3">Ship Order</div>
    <div class="row">
        <input type="hidden" name="method" value="post">
        <input type="hidden" name="slug" value="<%=slug%>">
        <div class="col-sm-12">
        <!-- Account details card-->
          <div class=" card w-100 mx-auto mt-3 mb-5">
              <div class="card-header">Update Ship Date, Tracking Number & Order Status</div> <!-- TODO -->
              <div class="card-body">
                  <form action="<%=request.getContextPath()%>/shipOrder/<%=staffID%>" method="post">
                      <!-- Form Row-->
                      <div class="mb-3">
                          <label class="small mb-1" for="ShippingStatus">Order Status</label>
                          <select class="custom-select d-block w-100" id="ShippingStatus" required="">
                            <option value="">Choose...</option>
                            <option value="P">Packing</option>
                            <option value="S">Shipped</option>
                            <option value="D">Delivered</option>
                            <option value="C">Canceled</option>
                        </select>
                      </div>

                      <div class="row mb-3">
                          <div class="col-md-6">
                              <label class="small mb-1" for="ShipDate">ShipDate</label>
                              <input class="form-control" id="ShipDate" type="date" value="TODO">
                          </div>
                          <div class="col-md-6">
                              <label class="small mb-1" for="TrackingNumber">Tracking Number</label>
                              <input class="form-control" id="TrackingNumber" type="text" value="TODO">
                          </div>
                      </div>
                    <!-- Ship Order button-->
                    <button type="submit" style="background: hsl(221, 100%, 33%);color: hsl(221, 100%, 95%)" class="btn">Ship Order</button>
                    <!-- Exit button-->
                    <a href="<%=request.getContextPath()%>/manage-order.jsp" class="btn btn-secondary">Cancel</a>
                  </form>
              </div>
          </div>
      </div>
</div>

<%@include file="includes/footer.jsp" %>
</body>
</html>