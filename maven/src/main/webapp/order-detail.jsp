<%@ page contentType="text/html;charset=UTF-8"%>
<%
    // only staff can access this page, redirect to login page otherwise
    session = request.getSession(false);
    if (session == null || session.getAttribute("isStaff") == null) {
        response.sendRedirect("login.jsp");
    }

%>
<html>
<head>
    <%@include file="includes/header.jsp" %>
    <title>Modify Product</title>
</head>
<body>
<%@include file="includes/navbar.jsp" %>

<div class="container mb-4">
    <div class="card-header my-3">View Order Detail</div>
    <div class="row">
        <input type="hidden" name="method" value="post">
        <input type="hidden" name="slug" value="<%=slug%>">
        <div class="col-sm-12">
        <!-- Account details card-->
          <div class=" card w-100 mx-auto mt-3 mb-5">
              <div class="card-body">
                  <form>
                    <fieldset disabled>
                      <!-- Form Row-->
                      <h4 class="mb-3">Order</h4>
                      <div class="row mb-3">
                          <div class="col-md-6">
                              <label class="small mb-1" for="OrderID">Order #</label>
                              <input class="form-control" id="OrderID" type="text" value="TODO">
                          </div>

                          <div class="col-md-6">
                              <label class="small mb-1" for="ShippingStatus">Order Status</label>
                              <input class="form-control" id="ShippingStatus" type="text" value="TODO">
                          </div>
                      </div>

                      <div class="row mb-3">
                          <div class="col-md-6">
                              <label class="small mb-1" for="TotalAmt">Total Amount</label>
                              <input class="form-control" id="TotalAmt" type="number" value="TODO">
                          </div>
                          <div class="col-md-6">
                              <label class="small mb-1" for="OrderPayDate">Pay Date</label>
                              <input class="form-control" id="OrderPayDate" type="date" value="TODO">
                          </div>
                      </div>

                      <div class="mb-3">
                          <label class="small mb-1" for="CustomerID">Customer #</label>
                          <input class="form-control" id="CustomerID" type="text" value="TODO">
                      </div>

                      <hr class="mb-4">
                      <h4 class="mb-3">Payment</h4>
                      <div class="row mb-3">
                          <div class="col-md-6">
                              <label class="small mb-1" for="PaymentMethod">Payment Method</label>
                              <input class="form-control" id="PaymentMethod" type="text" value="TODO">
                          </div>
                          <div class="col-md-6">
                              <label class="small mb-1" for="4CreditDigits">Last 4 digits</label>
                              <input class="form-control" id="4CreditDigits" type="number" value="TODO">
                          </div>
                      </div>

                      <hr class="mb-4">
                      <h4 class="mb-3">Billing</h4>
                      <div class="mb-3">
                          <label class="small mb-1" for="BillingAddress">Billing Address</label>
                          <input class="form-control" id="BillingAddress" type="text" value="TODO">
                      </div>
                      <div class="row mb-3">
                          <div class="col-md-4">
                              <label class="small mb-1" for="BillCity">City</label>
                              <input class="form-control" id="BillCity" type="text" value="TODO">
                          </div>
                          <div class="col-md-4">
                              <label class="small mb-1" for="BillCountry">Country</label>
                              <input class="form-control" id="BillCountry" type="text" value="TODO">
                          </div>
                          <div class="col-md-4">
                              <label class="small mb-1" for="BillPostalCode">Postal Code</label>
                              <input class="form-control" id="BillPostalCode" type="text" value="TODO">
                          </div>
                      </div>

                      <hr class="mb-4">
                      <h4 class="mb-3">Shipping</h4>
                      <div class="mb-3">
                          <label class="small mb-1" for="ShipAddress">Shipping Address</label>
                          <input class="form-control" id="ShipAddress" type="text" value="TODO">
                      </div>
                      <div class="row mb-3">
                          <div class="col-md-4">
                              <label class="small mb-1" for="ShipCity">City</label>
                              <input class="form-control" id="ShipCity" type="text" value="TODO">
                          </div>
                          <div class="col-md-4">
                              <label class="small mb-1" for="ShipCountry">Country</label>
                              <input class="form-control" id="ShipCountry" type="text" value="TODO">
                          </div>
                          <div class="col-md-4">
                              <label class="small mb-1" for="ShipPostalCode">Postal Code</label>
                              <input class="form-control" id="ShipPostalCode" type="text" value="TODO">
                          </div>
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
                    </fieldset>
                    <!-- Exit button-->
                    <a 
                        style="background: hsl(221, 100%, 33%);color: hsl(221, 100%, 95%);"
                        href="<%=request.getContextPath()%>/manage-order.jsp"
                        class="btn">Exit
                    </a>
                  </form>
              </div>
          </div>
      </div>
</div>

<%@include file="includes/footer.jsp" %>
</body>
</html>