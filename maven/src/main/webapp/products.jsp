<%@ page import="com.taptapgo.Product" %>
<%@ page import="com.taptapgo.Warehouse" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    // reference to warehouse and product list
    Warehouse warehouse = (Warehouse) application.getAttribute("warehouse");
    List<Product> products = new ArrayList<Product>(warehouse.getProductList().keySet());
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
    <!-- Sort button -->
    <div class="d-flex justify-content-end">
        <div class="dropdown">
        <button class="btn btn-sm dropdown-toggle" type="button" data-toggle="dropdown" aria-expanded="false">
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-sort-up" viewBox="0 0 16 16">
            <path d="M3.5 12.5a.5.5 0 0 1-1 0V3.707L1.354 4.854a.5.5 0 1 1-.708-.708l2-1.999.007-.007a.498.498 0 0 1 .7.006l2 2a.5.5 0 1 1-.707.708L3.5 3.707zm3.5-9a.5.5 0 0 1 .5-.5h7a.5.5 0 0 1 0 1h-7a.5.5 0 0 1-.5-.5M7.5 6a.5.5 0 0 0 0 1h5a.5.5 0 0 0 0-1zm0 3a.5.5 0 0 0 0 1h3a.5.5 0 0 0 0-1zm0 3a.5.5 0 0 0 0 1h1a.5.5 0 0 0 0-1z"/>
            </svg>
            Sort
            <span class="caret"></span>
        </button>
        <div class="dropdown-menu">
            <a class="dropdown-item" href="<%=request.getContextPath()%>/products.jsp?sort=price_descending">Highest Price</a>
            <a class="dropdown-item" href="<%=request.getContextPath()%>/products.jsp?sort=price_ascending">Lowest Price</a>
            <a class="dropdown-item" href="<%=request.getContextPath()%>/products.jsp?sort=name_ascending">Product name ascending</a>
            <a class="dropdown-item" href="<%=request.getContextPath()%>/products.jsp?sort=name_descending">Product name descending</a>
        </div>
        </div>
    </div>
    <!-- Display Product -->
    <div class="row">
        <%
            // if there are products in warehouse, create cards for them
            if (!products.isEmpty()) {
                String sortParam = (String) request.getParameter("sort");

                if(sortParam == null) {
                    products = Product.sortProductsBy(products, "Name", "ascending");
                } else if (sortParam.equals("name_descending")){
                    products = Product.sortProductsBy(products, "Name", "descending");
                } else if (sortParam.equals("price_ascending")) {
                    products = Product.sortProductsBy(products, "Price", "ascending");
                } else if (sortParam.equals("price_descending")) {
                    products = Product.sortProductsBy(products, "Price", "descending");
                } else {
                    products = Product.sortProductsBy(products, "Name", "ascending");
                }
                for (Product p : products) {
                    System.out.println("Product: " + p.getName());
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
