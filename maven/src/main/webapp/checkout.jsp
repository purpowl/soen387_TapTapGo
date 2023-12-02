<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.taptapgo.Product" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page contentType="text/html;charset=UTF-8"%>

<% 
    HttpSession currentSession = request.getSession();
    boolean isRegistered = currentSession.getAttribute("registered_user") != null;

    Object cart_object = currentSession.getAttribute("cart");
    Object cart_modified_object = currentSession.getAttribute("cart_modified");
    HashMap<Product, Integer> cart;

    if (cart_object == null) {
        System.out.println("CART IS NULL!!!!");
        cart = new HashMap<>();
    } else {
        cart = (HashMap<Product, Integer>) cart_object;
    }
    currentSession.setAttribute("cart", cart);
%>
<html>
<head>
  <%@include file="includes/header.jsp" %>
  <title>Checkout</title>
</head>
<body>
<%@include file="includes/navbar.jsp" %>

<div class="container mb-4">
    <div class="card-header my-5 mb-3">Checkout</div>
    <div class="row">
    <!-- Error Handling -->
    <div class="col-12">
    <%
        String failureMessage = (String) request.getParameter("checkout");

        if(failureMessage != null) {
            if (failureMessage.equals("dborderfail") || failureMessage.equals("dbcustfail")) {
    %>
                <p style="color: red">There's currently some error with our database. Please try again later!</p>
    <%
            } else if (failureMessage.equals("ccinputfail")) {
    %>
                <p style="color: red">Please enter the correct credit card number!</p>
    <%
            }
        }
    %>
    </div>
    </div>
    <!-- Container Wrapper -->
    <div class="row">
        <!-- Right Section - Your Cart -->
        <div class="col-md-4 order-md-2 mb-4">
            <h4 class="d-flex justify-content-between align-items-center mb-3">
                <span class="text-muted">Your cart</span>
            </h4>
            <ul class="list-group mb-3 sticky-top">
                <%
                    double total = 0;
                    for (Map.Entry<Product, Integer> product_entry : cart.entrySet()) { 
                        Product product = product_entry.getKey();
                        int amount = product_entry.getValue();
                        total += product.getPrice() * amount;
                %>
                <li class="list-group-item d-flex justify-content-between lh-condensed">
                    <div>
                        <h6 class="my-0"><%=product.getName()%></h6>
                        <% 
                            if (!product.getDescription().isEmpty()) {
                        %>
                        <small class="text-muted"><%=product.getDescription()%></small>
                        <%
                            }
                        %>
                    </div>
                    <span class="text-muted">$<%=product.getPrice()%></span>
                </li>
                <% 
                    } 
                %>
                <li class="list-group-item d-flex justify-content-between">
                    <span>Total (CAD)</span>
                    <strong>$<%=Product.roundPrice(total)%></strong>
                </li>
            </ul>
            <a class="btn btn-outline-secondary btn-block" href="<%=request.getContextPath()%>/products.jsp">Continue Shopping</a>
        </div>
        <!-- Left Session - Billing Address -->
        <div class="col-md-8 order-md-1">
            <h4 class="mb-3">Billing</h4>
            <form class="needs-validation" novalidate="" action="<%=request.getContextPath()%>/checkout" method="post">
                <% 
                    if (!isRegistered) {
                %>
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="firstName">First name</label>
                        <input type="text" class="form-control" id="firstName" name="firstName" placeholder="" value="" required="">
                        <div class="invalid-feedback"> Valid first name is required. </div>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="lastName">Last name</label>
                        <input type="text" class="form-control" id="lastName" name="lastName" placeholder="" value="" required="">
                        <div class="invalid-feedback"> Valid last name is required. </div>
                    </div>
                </div>
                <div class="mb-3">
                    <label for="phone">Email <span class="text-muted">(Optional)</span></label>
                    <input type="text" class="form-control" id="phone" name="phone" placeholder="123-456-7890">
                    <div class="invalid-feedback"> Please enter a valid phone number for carrier's contact.</div>
                </div>
                <div class="mb-3">
                    <label for="email">Email <span class="text-muted">(Optional)</span></label>
                    <input type="email" class="form-control" id="email" name="email" placeholder="you@example.com">
                    <div class="invalid-feedback"> Please enter a valid email address for shipping updates. </div>
                </div>
                <% 
                    }
                %>
                <div class="mb-3">
                    <label for="billAddress">Address</label>
                    <input type="text" class="form-control" id="billAddress" name="billAddress" placeholder="1234 Main St" required="">
                    <div class="invalid-feedback"> Please enter your shipping address. </div>
                </div>
                <div class="row">
                    <div class="col-md-5 mb-3">
                        <label for="billCity">Province</label>
                        <select class="custom-select d-block w-100" id="billCity" name="billCity" required="">
                            <option value="">Choose...</option>
                            <option value="AB">Alberta</option>
                            <option value="BC">British Columbia</option>
                            <option value="MB">Manitoba</option>
                            <option value="NB">New Brunswick</option>
                            <option value="NL">Newfoundland and Labrador</option>
                            <option value="NS">Nova Scotia</option>
                            <option value="ON">Ontario</option>
                            <option value="PE">Prince Edward Island</option>
                            <option value="QC">Quebec</option>
                            <option value="SK">Saskatchewan</option>
                            <option value="NT">Northwest Territories</option>
                            <option value="NU">Nunavut</option>
                            <option value="YT">Yukon</option>
                        </select>
                        <div class="invalid-feedback"> Please select a valid city. </div>
                    </div>
                    <div class="col-md-4 mb-3">
                        <label for="billCountry">Country</label>
                        <select class="custom-select d-block w-100" id="billCountry" name="billCountry" required="">
                            <option value="">Choose...</option>
                            <option value="CA">Canada</option>
                        </select>
                        <div class="invalid-feedback"> Please provide a valid country. </div>
                    </div>
                    <div class="col-md-3 mb-4">
                        <label for="billPostalCode">Postal Code</label>
                        <input type="text" class="form-control" id="billPostalCode" name="billPostalCode" placeholder="" required="">
                        <div class="invalid-feedback"> Postal Code code required. </div>
                    </div>
                </div>
                <!-- Shipping same as billing address checkbox -->
                <div class="form-group mb-4">
                    <div class="checkbox ">
                        <input data-toggle="collapse" data-target="#collapseShipAddress" aria-expanded="true" aria-controls="collapseShipAddress" id="sameShippingAddress" name="sameShippingAddress" value="checked" type="checkbox" checked/> Shipping address is the same as billing address.
                    </div>
                </div>
                <!-- Collapsible section -->
                <div id="collapseShipAddress" class="collapse">
                    <h4 class="mb-3">Shipping Address</h4>
                    <div class="mb-3">
                        <label for="shipAddress">Address</label>
                        <input type="text" class="form-control" id="shipAddress" name="shipAddress" placeholder="1234 Main St" required="">
                        <div class="invalid-feedback"> Please enter your shipping address. </div>
                    </div>  
                    <div class="row">
                        <div class="col-md-5 mb-3">
                            <label for="shipCity">Province</label>
                            <select class="custom-select d-block w-100" id="shipCity" name="shipCity" required="">
                                <option value="">Choose...</option>
                                <option value="AB">Alberta</option>
                                <option value="BC">British Columbia</option>
                                <option value="MB">Manitoba</option>
                                <option value="NB">New Brunswick</option>
                                <option value="NL">Newfoundland and Labrador</option>
                                <option value="NS">Nova Scotia</option>
                                <option value="ON">Ontario</option>
                                <option value="PE">Prince Edward Island</option>
                                <option value="QC">Quebec</option>
                                <option value="SK">Saskatchewan</option>
                                <option value="NT">Northwest Territories</option>
                                <option value="NU">Nunavut</option>
                                <option value="YT">Yukon</option>
                            </select>
                            <div class="invalid-feedback"> Please select a valid city. </div>
                        </div>
                        <div class="col-md-4 mb-3">
                            <label for="shipCountry">Country</label>
                            <select class="custom-select d-block w-100" id="shipCountry" name="shipCountry" required="">
                                <option value="">Choose...</option>
                                <option value="CA">Canada</option>
                            </select>
                            <div class="invalid-feedback"> Please provide a valid country. </div>
                        </div>
                        <div class="col-md-3 mb-3">
                            <label for="shipPostalCode">Postal Code</label>
                            <input type="text" class="form-control" id="shipPostalCode" name="shipPostalCode" placeholder="" required="">
                            <div class="invalid-feedback"> Postal Code code required. </div>
                        </div>
                    </div>
                </div>

                <!-- Payment -->
                <hr class="mb-4">
                <h4 class="mb-3">Payment</h4>
                <div class="d-block my-3">
                    <div class="custom-control custom-radio">
                        <input id="credit" name="paymentMethod" type="radio" class="custom-control-input" value="credit" checked="" required="">
                        <label class="custom-control-label" for="credit">Credit card</label>
                    </div>
                    <div class="custom-control custom-radio">
                        <input id="debit" name="paymentMethod" type="radio" class="custom-control-input" value="debit" required="">
                        <label class="custom-control-label" for="debit">Debit card</label>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="cc-name">Name on card</label>
                        <input type="text" class="form-control" id="cc-name" placeholder="" required="">
                        <small class="text-muted">Full name as displayed on card</small>
                        <div class="invalid-feedback"> Name on card is required </div>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="cc-number">Credit card number</label>
                        <input type="text" class="form-control" id="cc-number" name="cc-number" placeholder="" required="">
                        <div class="invalid-feedback"> Credit card number is required </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-3 mb-3">
                        <label for="cc-expiration">Expiration</label>
                        <input type="text" class="form-control" id="cc-expiration" placeholder="" required="">
                        <div class="invalid-feedback"> Expiration date required </div>
                    </div>
                    <div class="col-md-3 mb-3">
                        <label for="cc-cvv">CVV</label>
                        <input type="text" class="form-control" id="cc-cvv" placeholder="" required="">
                        <div class="invalid-feedback"> Security code required </div>
                    </div>
                </div>
                <hr class="mb-4">
                <button style=" background: hsl(221, 100%, 33%);color: hsl(221, 100%, 95%);" class="btn btn-lg btn-block" type="submit">Place Order</button>
            </form>
        </div>
    </div>
</div>

<%@include file="includes/footer.jsp" %>
</body>
</html>