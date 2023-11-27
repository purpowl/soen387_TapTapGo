<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="java.lang.NumberFormatException" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.taptapgo.Product" %>
<%@ page import="com.taptapgo.Order" %>
<%@ page import="com.taptapgo.repository.OrderIdentityMap" %>
<%@ page import="java.util.Map" %>
<%
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    // HttpSession session = request.getSession(false);

    session = request.getSession(false);
    if (session == null) {
        response.sendRedirect("/taptapgo");
    }

    // reference to order
    String orderID_str = (String) request.getParameter("orderID");

    if (orderID_str == null && session.getAttribute("isStaff") != null) {
        response.sendRedirect(request.getContextPath() + "/manage-order.jsp");
    } else if (orderID_str == null) {
        response.sendRedirect(request.getContextPath() + "/orders.jsp");
    }

    Integer orderID = null;
    try{
        orderID = Integer.parseInt(orderID_str);
    } catch(NumberFormatException e) {
        if (session.getAttribute("isStaff") != null) {
            response.sendRedirect(request.getContextPath() + "/manage-order.jsp");
        } else {
            response.sendRedirect(request.getContextPath() + "/orders.jsp");
        }
    }

    Order order = OrderIdentityMap.getOrderByID(orderID);
    if (order == null) {
        if (session.getAttribute("isStaff") != null) {
            response.sendRedirect(request.getContextPath() + "/manage-order.jsp");
        } else {
            response.sendRedirect(request.getContextPath() + "/orders.jsp");
        }
    }
%>

<html>
<head>
    <%@include file="includes/header.jsp" %>
    <title>Order Details</title>
</head>
<body>
<%@include file="includes/navbar.jsp" %>

<div class="container mb-5">
    <div class="card-header my-3">Order #<span><%=order.getOrderID()%></span> Details </div>
    <!-- Show order details for staff/customer -->
    
    <!-- Order ID Table -->
    <div class="row mb-3">
        <div class="col-sm-12 my-3">
            <div class="card">
                <div class="card-body p-0 table-responsive">
                    <!-- Order Item table -->
                    <table class="table mb-0">
                        <thead class="thead-dark">
                        <tr>
                            <th scope="col">Product</th>
                            <th scope="col"></th>
                            <th scope="col"></th>
                            <th scope="col">Price</th>
                            <th scope="col">Quantity</th>
                            <th scope="col">Description</th>
                        </tr>
                        </thead>
                        <tbody>
                            <!-- For each product in the order, display the image and the description -->
                            <% 
                                float total = 0;
                                for (Map.Entry<Product, Integer> productEntry : order.getOrderProducts().entrySet()) {
                                    Product product = productEntry.getKey();
                                    int amount = productEntry.getValue();
                                    total += product.getPrice() * amount;
                            %>
                            <tr>
                                <td colspan="2"><img src="<%=request.getContextPath()%>/images/epomaker_alice.jpg" alt="product" class="" width="150"></td>
                                <td style="vertical-align: middle;"><%=product.getName()%></td>
                                <td style="vertical-align: middle;">$<%=Product.roundPrice((total * 1.14975))%></td>
                                <td style="vertical-align: middle;">x<span><%=amount%></span></td>
                                <td style="vertical-align: middle;"><%=product.getDescription()%></td>
                            </tr>
                            <%
                                }
                            %>
                            <tr class="table-active">
                                <td colspan="3">Subtotal</td>
                                <td colspan="3">$<%=Product.roundPrice(total)%></td>
                            </tr>
                            <tr class="table-active">
                                <td colspan="3">GST/HST</td>
                                <td colspan="3">$<%=Product.roundPrice((total * 0.05))%></td>
                            </tr>
                            <tr class="table-active">
                                <td colspan="3">PST/QST</td>
                                <td colspan="3">$<%=Product.roundPrice((total * 0.09975))%></td>
                            </tr>
                            <tr class="table-active font-weight-bold">
                                <td colspan="3">Total</td>
                                <td colspan="3">$<%=Product.roundPrice((total * 1.14975))%></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <!-- Show Customer information on the order for staff -->
    <%  if (session.getAttribute("isStaff") != null) { %>
    <div class="row mb-3">
        <input type="hidden" name="method" value="post">
        <div class="col-sm-12">
            <!-- Account details card-->
            <div class=" card w-100 mx-auto mt-3 mb-3">
                <div class="card-body">
                    <form>
                    <fieldset disabled>
                        <!-- Form Row-->
                        <h4 class="mb-3">Order</h4>
                        <div class="row mb-4">
                            <div class="col-md-6">
                                <label class="small mb-1" for="OrderID">Order #</label>
                                <input class="form-control" id="OrderID" type="text" value="<%=order.getOrderID()%>">
                            </div>

                            <div class="col-md-6">
                                <label class="small mb-1" for="ShippingStatus">Order Status</label>
                                <input class="form-control" id="ShippingStatus" type="text" value="<%=order.getShippingStatus()%>">
                            </div>
                        </div>

                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label class="small mb-1" for="TotalAmt">Total Amount</label>
                                <input class="form-control" id="TotalAmt" type="number" value="<%=Product.roundPrice((total * 1.14975))%>">
                            </div>
                            <div class="col-md-6">
                                <label class="small mb-1" for="OrderPayDate">Pay Date</label>
                                <input class="form-control" id="OrderPayDate" type="date" value="<%=formatter.format(order.getPayDate())%>">
                            </div>
                        </div>

                        <div class="mb-4">
                            <label class="small mb-1" for="CustomerID">Customer #</label>
                            <input class="form-control" id="CustomerID" type="text" value="<%=order.getCustomerID()%>">
                        </div>

                        <h4 class="mb-3">Payment</h4>
                        <div class="row mb-4">
                            <div class="col-md-6">
                                <label class="small mb-1" for="PaymentMethod">Payment Method</label>
                                <input class="form-control" id="PaymentMethod" type="text" value="<%=order.getPaymentMethod()%>">
                            </div>
                            <div class="col-md-6">
                                <label class="small mb-1" for="4CreditDigits">Last 4 digits</label>
                                <input class="form-control" id="4CreditDigits" type="number" value="<%=order.getCardNum()%>">
                            </div>
                        </div>

                        <h4 class="mb-3">Billing</h4>
                        <div class="mb-3">
                            <label class="small mb-1" for="BillingAddress">Billing Address</label>
                            <input class="form-control" id="BillingAddress" type="text" value="<%=order.getBillAddress()%>">
                        </div>
                        <div class="row mb-4">
                            <div class="col-md-4">
                                <label class="small mb-1" for="BillCity">City</label>
                                <input class="form-control" id="BillCity" type="text" value="<%=order.getBillCity()%>">
                            </div>
                            <div class="col-md-4">
                                <label class="small mb-1" for="BillCountry">Country</label>
                                <input class="form-control" id="BillCountry" type="text" value="<%=order.getBillCountry()%>">
                            </div>
                            <div class="col-md-4">
                                <label class="small mb-1" for="BillPostalCode">Postal Code</label>
                                <input class="form-control" id="BillPostalCode" type="text" value="<%=order.getBillPostalCode()%>">
                            </div>
                        </div>

                        <h4 class="mb-3">Shipping</h4>
                        <div class="mb-3">
                            <label class="small mb-1" for="ShipAddress">Shipping Address</label>
                            <input class="form-control" id="ShipAddress" type="text" value="<%=order.getShippingAddress()%>">
                        </div>
                        <div class="row mb-3">
                            <div class="col-md-4">
                                <label class="small mb-1" for="ShipCity">City</label>
                                <input class="form-control" id="ShipCity" type="text" value="<%=order.getShippingCity()%>">
                            </div>
                            <div class="col-md-4">
                                <label class="small mb-1" for="ShipCountry">Country</label>
                                <input class="form-control" id="ShipCountry" type="text" value="<%=order.getShippingCountry()%>">
                            </div>
                            <div class="col-md-4">
                                <label class="small mb-1" for="ShipPostalCode">Postal Code</label>
                                <input class="form-control" id="ShipPostalCode" type="text" value="<%=order.getShippingPostalCode()%>">
                            </div>
                        </div>
                        
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label class="small mb-1" for="ShipDate">ShipDate</label>
                                <input class="form-control" id="ShipDate" type="date" value="<%=order.getShipDate()%>">
                            </div>
                            <div class="col-md-6">
                                <label class="small mb-1" for="TrackingNumber">Tracking Number</label>
                                <input class="form-control" id="TrackingNumber" type="text" value="<%=order.getTrackingNumber()%>">
                            </div>
                        </div>
                    </fieldset>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <%  } %>

    <!-- Exit button-->
    <div class="row mb-3">
        <div class="col-sm-12">
            <% if (session.getAttribute("isStaff") != null) { %>
            <a href="<%=request.getContextPath()%>/manage-order.jsp" class="btn btn-secondary">Back</a>
            <% } else if (session.getAttribute("isRegisteredUser") != null) { %>
            <a href="<%=request.getContextPath()%>/user-orders.jsp" class="btn btn-secondary">Back</a>
            <% } else { %>
            <a href="<%=request.getContextPath()%>/orders.jsp" class="btn btn-secondary">Back</a>
            <% } %>
        </div>     
    </div>

</div>

<%@include file="includes/footer.jsp" %>
</body>
</html>