<%@ page import="com.taptapgo.Product" %>
<%@ page import="com.taptapgo.Warehouse" %>
<%@ page import="java.util.Set" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    // only staff can access this page, redirect to login page otherwise
  session = request.getSession(false);
  if (session == null || session.getAttribute("isStaff") == null) {
    response.sendRedirect("login.jsp");
  }

    // reference to warehouse and product list
    Warehouse warehouse = (Warehouse) application.getAttribute("warehouse");
    Set<Product> products = warehouse.getProductList().keySet();
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
        <a class="btn btn-outline-secondary mb-3" href="<%=request.getContextPath()%>/create-product.jsp">Create Product</a>
        <!-- Download Product Catalog -->
        <form action="download-catalog" method="get">
            <button class="ml-3 btn btn-secondary" type="submit"><i class="fa fa-download"></i> Download Product Catalog</button>
        </form>
    </div>

    <!-- Page Indicator -->
    <div class="card-header my-3 ">Manage Product</div>

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
                for (Product p : products) {
        %>
        <div class="col-md-3 my-3">
            <div class="card w-100">
                <img class="card-img-top" src="<%=request.getContextPath()%>/images/epomaker_alice.jpg"
                     alt="Card image cap">
                <div class="card-body">
                    <h5 class="card-title"><%=p.getName() %></h5>
                    <h6 class="price">Price: $<%=p.getPrice() %></h6>
                    <p class="category">Description: <%=p.getDescription() %></p>
                    <!-- Show different types of button for staff/user -->
                    <div class="mt-3 d-flex justify-content-between">
                        <form action="<%=request.getContextPath()%>/products/<%=p.getSlug()%>" method="post">
                            <input type="hidden" name="slug" value="<%=p.getSlug()%>">
                            <input type="hidden" name="method" value="delete">
                            <button type="submit" class="btn btn-secondary btn-sm">Delete</button>
                        </form>
                        <a style=" background: hsl(221, 100%, 33%);color: hsl(221, 100%, 95%);"
                           class="btn btn-sm mb-3" href="<%=request.getContextPath()%>/modify-product.jsp?slug=<%=p.getSlug()%>"
                           >Modify
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