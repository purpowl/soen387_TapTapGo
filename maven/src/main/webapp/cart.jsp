<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.taptapgo.Product" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page contentType="text/html;charset=UTF-8"%>

<% 
    HttpSession currentSession = request.getSession();
    // redirect to index if staff is accessing customer cart
    if (currentSession.getAttribute("isStaff") != null) {
        response.sendRedirect("index.jsp");
    }
    Object cart_object = currentSession.getAttribute("cart"); 
    Object cart_modified_object = currentSession.getAttribute("cart_modified");
    HashMap<Product, Integer> cart;

    if (cart_object == null) {
        System.out.println("CART IS NULL!!!!");
        cart = new HashMap<Product, Integer>();
    } else {
        cart = (HashMap<Product, Integer>) cart_object;
    }
%>
<html>
    <head>
        <%@include file="includes/header.jsp" %>
        <title>Cart</title>
    </head>
    <body>
        <%@include file="includes/navbar.jsp" %>
        <div class="container mb-5">
            <!-- Page Location Indicator -->
            <div class="card-header my-3 ">Cart</div>
            <!-- Container Wrapper -->
            <div class="row">
                <!-- Error Handler -->
                <div class="col-12">
                    <div class="container m-3">
                        <% 
                            if (cart_modified_object != null) {
                                boolean cart_modified_success = (Boolean) cart_modified_object;
                                if (!cart_modified_success) {
                        %>
                        <div class="row">
                            <div class="col-12 text-center">
                                <h5 style="color: red;">Failed to add product to cart!</>
                            </div>
                        </div>
                        <%
                                }
            
                                // Remove the failure flag
                                currentSession.removeAttribute("cart-modified");
                            }
                        %>
                    </div>
                </div>
            </div>
            <!-- Display All products (in grid of 3) -->
            <div class="row mt-1">
                <div class="col-12">
                    <%
                        double total = 0;
                        for (Map.Entry<Product, Integer> product_entry : cart.entrySet()) { 
                            Product product = product_entry.getKey();
                            int amount = product_entry.getValue();
                            total += product.getPrice() * amount;
                    %>
                    <!-- Display a product -->
                    <div class="card mt-2">
                        <div class="card-body px-5">
                            <h4 class="card-title mt-2 mb-2"><%=product.getName()%></h4>
                            <% 
                                if (!product.getDescription().isEmpty()) {
                            %>
                            <p class="card-text font-weight-light"><%=product.getDescription()%></p>
                            <%
                                }
                            %>
                            <div class="container pl-0">
                                <div class="row">
                                    <div class="col-8 pl-0">
                                        <div class="container pl-3">
                                            <div class="row">
                                                <div class="col-1 ml-0 mr-1 d-flex align-items-right">
                                                    <form action="<%=request.getContextPath()%>/cart/remove" method="post">
                                                        <input type="hidden" name="slug" value="<%=product.getSlug()%>">
                                                        <button 
                                                            style=" background: hsl(221, 100%, 33%);color: hsl(221, 100%, 95%);" 
                                                            type="submit" 
                                                            class="btn"
                                                        >-
                                                        </button>
                                                    </form>
                                                </div>
                                                <div class="col-3 mx-1 pl-10 pr-0 d-flex align-items-center">
                                                    <p class="w-100 text-center border rounded"><%=amount%></p>
                                                </div>
                                                <div class="col-1 mx-0 d-flex align-items-left">
                                                    <span>
                                                        <form action="<%=request.getContextPath()%>/cart/add" method="post">
                                                            <input type="hidden" name="slug" value="<%=product.getSlug()%>">
                                                            <button
                                                                style=" background: hsl(221, 100%, 33%);color: hsl(221, 100%, 95%);"  
                                                                type="submit" 
                                                                class="btn"
                                                            >+
                                                            </button>
                                                        </form>
                                                    </span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-4">
                                        <p>$<%=product.getPrice()%></p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- Display a product -->
                    <% 
                        } 
                    %>
                </div>
            </div>
            <!-- Display All products (in grid of 3) -->

            <!-- Total Order Amount -->
            <div class="row mt-4">
                <div class="col-12">
                    <div class="card mt-2">
                        <div class="card-body px-5">
                            <h4 class="card-title mt-2 mb-4">Total Order</h4>
                            <div class="container pl-0">
                                <div class="row">
                                    <div class="col-8 pl-0">
                                        <div class="container pl-3">
                                            <p>Subtotal</p>
                                            <p>GST/HST</p>
                                            <p>PST/QST</p>
                                            <p class="font-weight-bold">Total</p>
                                        </div>
                                    </div>
                                    <div class="col-4 pl-0">
                                        <div class="container pl-3">
                                            <p>$<%=Product.roundPrice(total)%></p>
                                            <p>$<%=Product.roundPrice((total * 0.05))%></p>
                                            <p>$<%=Product.roundPrice((total * 0.09975))%></p>
                                            <p class="font-weight-bold">$<%=Product.roundPrice((total * 1.14975))%></p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- Total Order Amount -->
            <!-- Check Out button -->
            <div class="row mt-4">
                <div class="col-12">
                    <a href="<%=request.getContextPath()%>/checkout.jsp">
                        <button style=" background: hsl(221, 100%, 33%);color: hsl(221, 100%, 95%);" type="submit" class="btn btn-block btn-lg me-2">Check Out</button>                                                 
                    </a>
                </div>
            </div>
            <!-- Check Out button -->
        </div>
        <%@include file="includes/footer.jsp" %>
    </body>
</html>