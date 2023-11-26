<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="com.taptapgo.Product" %>
<%@ page import="com.taptapgo.Warehouse" %>
<%
    // only staff can access this page, redirect to login page otherwise
    session = request.getSession(false);
    if (session == null || session.getAttribute("isStaff") == null) {
        response.sendRedirect("login.jsp");
    }

    String slug = request.getParameter("slug");
    // reference to warehouse
    Warehouse warehouse = (Warehouse) application.getAttribute("warehouse");

    // reference to product
    Product product = (Product) warehouse.findProductBySlug(slug);
    int inventory = warehouse.getProductInventoryBySlug(slug);

%>
<html>
<head>
    <%@include file="includes/header.jsp" %>
    <title>Modify Product</title>
</head>
<body>
<%@include file="includes/navbar.jsp" %>

<div class="container">
    <div class="card-header my-3">Modify Product</div>
    <div class="card w-100 mx-auto my-5">
        <div class="card-header text-center">Product Details</div>
        <div class="card-body">
            <!-- form for staff to modify product details -->
            <form action="<%=request.getContextPath()%>/products/<%=slug%>" method="post" >
                <input type="hidden" name="method" value="post">
                <input type="hidden" name="slug" value="<%=slug%>">
                <div class="form-group mb-3">
                    <div class="mb-3">
                        <label for="name">Product Name</label>
                        <input type="text" class="form-control" id="name" name="name" placeholder="<%=product.getName()%>">
                    </div>
                    
                    <div class="mb-3">
                        <label for="desc">Description</label>
                        <input type="text" class="form-control" id="desc" name="desc" placeholder="<%=product.getDescription()%>">
                    </div>
                    
                    <div class="mb-3">
                        <label for="vendor">Vendor</label>
                        <input type="text" class="form-control" id="vendor" name="vendor" placeholder="<%=product.getVendor()%>">
                    </div>
      
                    <div class="mb-3">
                        <label for="price">Price</label>
                        <input type="text" class="form-control" id="price" name="price" placeholder="<%=product.getPrice()%>">
                    </div>

                    <div class="mb-4">
                        <label for="amount">Amount</label>
                        <input type="text" class="form-control" id="amount" name="amount" placeholder="<%=inventory%>">
                    </div>
                    <div class="d-flex justify-content-between mb-3">
                        <a href="<%=request.getContextPath()%>/manage-product.jsp" class="btn btn-outline-secondary">Cancel</a>
                        <button style=" background: hsl(221, 100%, 33%);color: hsl(221, 100%, 95%);" type="submit" class="btn">Modify</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<%@include file="includes/footer.jsp" %>
</body>
</html>