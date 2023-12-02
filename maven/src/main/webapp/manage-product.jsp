<%@ page import="com.taptapgo.Product" %>
<%@ page import="com.taptapgo.Warehouse" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    // only staff can access this page, redirect to login page otherwise
  session = request.getSession(false);
  if (session == null || session.getAttribute("isStaff") == null) {
    response.sendRedirect("login.jsp");
  }

    // reference to warehouse and product list
    Warehouse warehouse = (Warehouse) application.getAttribute("warehouse");
    List<Product> products = new ArrayList<Product>(warehouse.getProductList().keySet());
%>
<html>
<head>
    <%@include file="includes/header.jsp" %>
    <title>Manage Product</title>
</head>
<body>
<%@include file="includes/navbar.jsp" %>

<div class="container mb-5">
    
    <div class="mt-3 d-flex justify-content-end">
        <!-- Create Product button -->
        <a style=" background: hsl(221, 100%, 33%);color: hsl(221, 100%, 95%);" 
           class="btn mb-3" 
           href="<%=request.getContextPath()%>/create-product.jsp">Create Product
        </a>
        <!-- Download Product Catalog -->
        <form action="download-catalog" method="get">
            <button class="ml-3 btn btn-secondary" type="submit"><i class="fa fa-download"></i> Download Product Catalog</button>
        </form>
    </div>

    <!-- Page Indicator -->
    <div class="card-header my-3 ">Manage Product</div>

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
            <a class="dropdown-item" href="<%=request.getContextPath()%>/manage-product.jsp?sort=price_descending">Highest Price</a>
            <a class="dropdown-item" href="<%=request.getContextPath()%>/manage-product.jsp?sort=price_ascending">Lowest Price</a>
            <a class="dropdown-item" href="<%=request.getContextPath()%>/manage-product.jsp?sort=name_ascending">Product name ascending</a>
            <a class="dropdown-item" href="<%=request.getContextPath()%>/manage-product.jsp?sort=name_descending">Product name descending</a>
        </div>
        </div>
    </div>

    <!-- Error Handling -->
    <%
        String createStatus = request.getParameter("create");

        if (createStatus != null) {
            if (createStatus.equals("success")) {
    %>
    <div class="row">
        <div class="col-12">
            <p style="color: #379237;">Product successfully created!</p>
        </div>
    </div>
    <%
    } else {
    %>
    <div class="row">
        <div class="col-12">
            <p style="color: red;">Fail to create product! Please recheck your parameters.</p>
        </div>
    </div>
    <%
            }
        }
    %>
    <%
        String modifyStatus = request.getParameter("modify");

        if (modifyStatus != null) {
            if (modifyStatus.equals("success")) {
    %>
    <div class="row">
        <div class="col-12">
            <p style="color: #379237;">Product successfully modified!</p>
        </div>
    </div>
    <%  
            } else {   
    %>
    <div class="row">
        <div class="col-12">
            <p style="color: red;">Fail to modify product! Please recheck your parameters.</p>
        </div>
    </div>
    <% 
            }
        }
    %>
    <%
        String deleteStatus = request.getParameter("delete");

        if (deleteStatus != null) {
            if (deleteStatus.equals("success")) {
    %>
    <div class="row">
        <div class="col-12">
            <p style="color: #379237;">Product successfully deleted!</p>
        </div>
    </div>
    <%  
            } else {
    %>
    <div class="row">
        <div class="col-12">
            <p style="color: red;">Fail to delete product!</p>
        </div>
    </div>
    <%
            }
        }   
    %>
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
        %>
        <div class="col-md-3 my-3">
            <div class="card w-100">
                <% if (p.getImagePath() != null) { %>
                    <img class="card-img-top" src="<%=request.getContextPath()%>/images/products/<%=p.getImagePath()%>" width="200" height="150" style="object-fit: cover" alt="Card image cap">
                <% } else { %>
                    <img class="card-img-top" src="<%=request.getContextPath()%>/images/epomaker_alice.jpg" width="200" height="150" style="object-fit: cover" alt="Card image cap">
                <% } %>
                <div class="card-body">
                    <h5 class="card-title"><%=p.getName() %></h5>
                    <h6 class="price">Price: $<%=p.getPrice() %></h6>
                    <p class="category">Description: <%=p.getDescription() %></p>
                    <!-- Show different types of button for staff/user -->
                    <div class="mt-3 d-flex justify-content-between">
                        <form action="<%=request.getContextPath()%>/products/<%=p.getSlug()%>" method="post">
                            <input type="hidden" name="slug" value="<%=p.getSlug()%>">
                            <input type="hidden" name="method" value="delete">
                            <button type="submit" class="btn btn-outline-danger btn-sm">Delete</button>
                        </form>
                        <a style=" background: hsl(221, 100%, 33%);color: hsl(221, 100%, 95%);" 
                           class="btn btn-sm mb-3" 
                           href="<%=request.getContextPath()%>/modify-product.jsp?slug=<%=p.getSlug()%>">Modify
                        </a>
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