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

<div class="container" style="min-height: 1000px;">
    <div class="card-header my-3">All Products</div>
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
            <p style="color: red;">Product successfully modified!</p>
        </div>
    </div>
    <%
            }
        }   
    %>
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
                    <h6 class="category">Description: <%=p.getDescription() %></h6>
                    <!-- Show different types of button for staff/user -->
                    <%  if (session.getAttribute("isStaff") != null) { %>
                    <div class="mt-3 d-flex justify-content-between">
                        <a style=" background: hsl(221, 100%, 33%);color: hsl(221, 100%, 95%);"
                           class="btn btn-sm mb-3" href="<%=request.getContextPath()%>/modify-product.jsp?slug=<%=p.getSlug()%>"
                           >Modify
                        </a>
                        <form action="<%=request.getContextPath()%>/products/<%=p.getSlug()%>" method="post">
                            <input type="hidden" name="slug" value="<%=p.getSlug()%>">
                            <input type="hidden" name="method" value="delete">
                            <button type="submit" class="btn btn-secondary btn-sm">Delete</button>
                        </form>
                    </div>
                    <%  } else { %>
                    <div class="mt-3 d-flex justify-content-between">
                        <form action="<%=request.getContextPath()%>/cart/add" method="post">
                            <input type="hidden" name="slug" value="<%=p.getSlug()%>">
                            <button style=" background: hsl(221, 100%, 33%);color: hsl(221, 100%, 95%);"
                                    type="submit" 
                                    class="btn btn-sm"
                                >Add To Cart
                            </button>
                        </form>
                        <a class="btn btn-secondary btn-sm mb-3" href="<%=request.getContextPath()%>/products/<%=p.getSlug()%>">View</a>
                    </div>
                    <%  } %>
                </div>
            </div>
        </div>
        <%}
            }%>
    </div>
    <!-- staff members can see a button to download product catalog -->
    <% if (session.getAttribute("isStaff") != null) { %>
        <div class="row text-center container">
            <form action="download-catalog" method="get">
                <button class="btn btn-secondary btn-sm" type="submit"><i class="fa fa-download"></i> Download Product Catalog</button>
            </form>
        </div>
    <% } %>
</div>

<%@include file="includes/footer.jsp" %>
</body>
</html>
