<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.taptapgo.Product" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page contentType="text/html;charset=UTF-8"%>

<% 
    HttpSession currentSession = request.getSession();  
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
        <div class="container-fluid" style="min-height: 1000px;">
            <div class="row">
                <div class="col-12">
                    <div class="container-fluid m-3">
                        <div class="row">
                            <div class="col-12 text-left"><h3>Cart</h3></div>
                        </div>
                        <% 
                            if (cart_modified_object != null) {
                                boolean cart_modified_success = (Boolean) cart_modified_object;
                                if (!cart_modified_success) {
                        %>
                        <div class="row">
                            <div class="col-12 text-center">
                                <p style="color: red;">Failed to add product to cart!</p>
                            </div>
                        </div>
                        <%
                                }
            
                                // Remove the failure flag
                                currentSession.removeAttribute("cart-modified");
                            }
                        %>
                        <div class="row">
                            <div class="col-8">
                                <%
                                    double total = 0;
                                    for (Map.Entry<Product, Integer> product_entry : cart.entrySet()) { 
                                        Product product = product_entry.getKey();
                                        int amount = product_entry.getValue();
                                        total += product.getPrice();
                                %>
                                <div class="card mt-2">
                                    <div class="card-body px-5">
                                        <h4 class="card-title mt-2 mb-4"><%=product.getName()%></h4>
                                        <% 
                                            if (!product.getDescription().isEmpty()) {
                                        %>
                                        <p class="card-text font-weight-light" style="font-size: 70%;"><%=product.getDescription()%></p>
                                        <%
                                            }
                                        %>
                                        <div class="container-fluid pl-0">
                                            <div class="row">
                                                <div class="col-2 pl-0">
                                                    <div class="container-fluid pl-3">
                                                        <div class="row">
                                                            <div class="col-1 ml-0 mr-1 d-flex align-items-right">
                                                                <form action="<%=request.getContextPath()%>/cart/remove" method="post">
                                                                    <input type="hidden" name="slug" value="<%=product.getSlug()%>">
                                                                    <button type="submit" class="btn btn-dark">-</button>
                                                                </form>
                                                            </div>
                                                            <div class="col-3 mx-1 pl-10 pr-0 d-flex align-items-center">
                                                                <p class="w-100 text-center border rounded"><%=amount%></p>
                                                            </div>
                                                            <div class="col-1 mx-0 d-flex align-items-left">
                                                                <span>
                                                                    <form action="<%=request.getContextPath()%>/cart/add" method="post">
                                                                        <input type="hidden" name="slug" value="<%=product.getSlug()%>">
                                                                        <button type="submit" class="btn btn-dark">+</button>
                                                                    </form>
                                                                </span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-7"></div>
                                                <div class="col-2">
                                                    <p class="font-weight-bold">$<%=product.getPrice()%></p>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <% 
                                    } 
                                %>
                            </div>
                            <div class="col-4 p-2 mr-3">
                                <div class="card border rounded">
                                    <div class="card-body">
                                        <h4 class="card-title">Order Summary</h4>
                                        <div class="container mt-3">
                                            <div class="row my-1">
                                                <div class="col-3">
                                                    <p>Subtotal</p>
                                                </div>
                                                <div class="col-6"><p></p></div>
                                                <div class="col-3">
                                                    $<%=total%>
                                                </div>
                                            </div>
                                            <div class="row my-1">
                                                <div class="col-3">
                                                    <p>GST/HST</p>
                                                </div>
                                                <div class="col-6"><p></p></div>
                                                <div class="col-3">
                                                    $<%=Product.roundPrice((total * 0.05))%>
                                                </div>
                                            </div>
                                            <div class="row my-1">
                                                <div class="col-3">
                                                    <p>PST/QST</p>
                                                </div>
                                                <div class="col-6"><p></p></div>
                                                <div class="col-3">
                                                   $<%=Product.roundPrice((total * 0.09975))%>
                                                </div>
                                            </div>
                                            <div class="row mt-4">
                                                <div class="col-3">
                                                    <h5 class="font-weight-bold">Total</h5>
                                                </div>
                                                <div class="col-6"><p></p></div>
                                                <div class="col-3">
                                                    $<%=Product.roundPrice((total * 1.14975))%>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <%@include file="includes/footer.jsp" %>
    </body>
</html>
