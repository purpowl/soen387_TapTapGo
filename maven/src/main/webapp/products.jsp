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

<div class="container">
    <div class="card-header my-3">All Products</div>
    <div class="row">
        <%
            // if there are products in warehouse, create cards for them
            if (!products.isEmpty()) {
                for (Product p : products) {
        %>
        <div class="col-md-3 my-3">
            <div class="card w-100">
                <img class="card-img-top" src="images/epomaker_alice.jpg"
                     alt="Card image cap">
                <div class="card-body">
                    <h5 class="card-title"><%=p.getName() %></h5>
                    <h6 class="price">Price: $<%=p.getPrice() %></h6>
                    <h6 class="category">Description: <%=p.getDescription() %></h6>
                    <div class="mt-3 d-flex justify-content-between">
                        <form action="<%=request.getContextPath()%>/cart/add" method="post">
                            <input type="hidden" name="slug" value="<%=p.getSlug()%>">
                            <button type="submit" class="btn btn-dark">Add To Cart</button>
                        </form>
                        <a class="btn btn-primary" href="<%=request.getContextPath()%>/product/<%=p.getSlug()%>">View</a>
                    </div>
                </div>
            </div>
        </div>
        <%}
            }%>
    </div>
    <!-- staff members can see a button to download product catalog -->
    <% if (session.getAttribute("isStaff") != null) { %>
        <div class="row text-center container">
            <form action="download-catalog" method="post">
                <button class="btn btn-primary btn-sm" type="submit"><i class="fa fa-download"></i>Download Product Catalog</button>
            </form>
        </div>
    <% } %>
</div>

<%@include file="includes/footer.jsp" %>
</body>
</html>
