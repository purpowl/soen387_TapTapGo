<%@ page contentType="text/html;charset=UTF-8"%>
<%
    // only staff can access this page, redirect to login page otherwise
    session = request.getSession(false);
    if (session == null || session.getAttribute("isStaff") == null) {
        response.sendRedirect("login.jsp");
    }

    String slug = request.getParameter("slug");
%>
<html>
<head>
    <%@include file="includes/header.jsp" %>
    <title>Modify Product</title>
</head>
<body>
<%@include file="includes/navbar.jsp" %>

<div class="container">
    <div class="card w-50 mx-auto my-5">
        <div class="card-header text-center">Product Details</div>
        <div class="card-body">
            <!-- form for staff to enter sku and name to create new product, calls CreateProductServlet doPost -->
            <form action="<%=request.getContextPath()%>/products/<%=slug%>" method="post" >
                <input type="hidden" name="method" value="post">
                <div class="form-group mb-3">
                    <label for="name">Product Name</label>
                    <input type="text" class="form-control" id="name" name="name" placeholder="Product Name">
                    <label for="desc">Description</label>
                    <input type="text" class="form-control" id="desc" name="desc" placeholder="Product Description">
                    <label for="vendor">Vendor</label>
                    <input type="text" class="form-control" id="vendor" name="vendor" placeholder="Product Vendor">
                    <label for="price">Price</label>
                    <input type="text" class="form-control" id="price" name="price" placeholder="Product Price">
                    <label for="amount">Amount</label>
                    <input type="text" class="form-control" id="amount" name="amount" placeholder="Product Amount">
                </div>
                <div class="text-center mb-3">
                    <button type="submit" class="btn btn-primary">Modify</button>
                </div>
            </form>
        </div>
    </div>
</div>

<%@include file="includes/footer.jsp" %>
</body>
</html>