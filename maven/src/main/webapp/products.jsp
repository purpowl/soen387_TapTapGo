<%@ page import="com.taptapgo.Product" %>
<%@ page import="com.taptapgo.Warehouse" %>
<%@ page import="java.util.Set" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    // reference to warehouse and product list
    Warehouse warehouse = (Warehouse) application.getAttribute("warehouse");
    Set<Product> products = warehouse.getProductList().keySet();
%>
<html>
<head>
    <%@include file="includes/header.jsp" %>
    <title>Products</title>
</head>
<body>
<%@include file="includes/navbar.jsp" %>

<div class="container mb-5">
    <div class="card-header my-3">All Products</div>
    <!-- Display Product -->
    <div class="row">
        <%
            // if there are products in warehouse, create cards for them
            if (!products.isEmpty()) {
                for (Product p : products) {
        %>
        <div class="col-md-3 my-3">
            <div class="card w-100">
                <% if (p.getImagePath() != null) { %>
                    <img class="card-img-top" src="<%=request.getContextPath()%>/images/products/<%=p.getImagePath()%>" alt="Card image cap" width="200" height="150" style="object-fit: cover">
                <% } else { %>
                    <img class="card-img-top" src="<%=request.getContextPath()%>/images/epomaker_alice.jpg" alt="Card image cap" width="200" height="150" style="object-fit: cover">
                <% } %>
                <div class="card-body">
                    <h5 class="card-title"><%=p.getName() %></h5>
                    <h6 class="price">Price: $<%=p.getPrice() %></h6>
                    <p class="category">Description: <%=p.getDescription() %></p>
                    <!-- Show different types of button for staff/user -->
                    <div class="mt-3 d-flex justify-content-between">
                        <a class="btn btn-secondary btn-sm mb-3" href="<%=request.getContextPath()%>/products/<%=p.getSlug()%>">View</a>
                        <form action="<%=request.getContextPath()%>/cart/add" method="post">
                            <input type="hidden" name="slug" value="<%=p.getSlug()%>">
                            <button style=" background: hsl(221, 100%, 33%) ;color: hsl(221, 100%, 95%);"
                                    type="submit" 
                                    class="btn btn-sm"
                                >Add To Cart
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <%}
            }%>
    </div>
</div>

<%@include file="includes/footer.jsp" %>
</body>
</html>
