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
        <div class="container-fluid mx-3 my-3">
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
                <div class="col-12">
                    <% 
                        for (Map.Entry<Product, Integer> product_entry : cart.entrySet()) { 
                            Product product = product_entry.getKey();
                            int amount = product_entry.getValue();
                    %>
                    <div class="card">
                        <div class="card-body">
                            <h4 class="card-title mt-2 mb-1"><%=product.getName()%></h4>
                            <% 
                                if (!product.getDescription().isEmpty()) {
                            %>
                            <p class="card-text font-weight-light" style="font-size: 70%;"><%=product.getDescription()%></p>
                            <%
                                }
                            %>
                            <div class="container-fluid">
                                <div class="row">
                                    <div class="col-2">
                                        <p class="text-center py-1">
                                            <span>
                                                <form action="<%=request.getContextPath()%>/cart/remove" method="post">
                                                    <input type="hidden" name="slug" value="<%=product.getSlug()%>">
                                                    <button type="submit" class="btn btn-dark">-</button>
                                                </form>
                                            </span>
                                            <%=amount%>
                                            <span>
                                                <form action="<%=request.getContextPath()%>/cart/add" method="post">
                                                    <input type="hidden" name="slug" value="<%=product.getSlug()%>">
                                                    <button type="submit" class="btn btn-dark">+</button>
                                                </form>
                                            </span>
                                        </p>
                                    </div>
                                    <div class="col-8"></div>
                                    <div class="col-2">
                                        <p class="font-weight-bold" style="font-size: 80%;"><%=product.getPrice()%></p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <% 
                        } 
                    %>
                </div>
            </div>
        </div>
        <%@include file="includes/footer.jsp" %>
    </body>
</html>
