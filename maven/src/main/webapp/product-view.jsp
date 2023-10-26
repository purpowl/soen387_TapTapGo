<%@ page import="com.taptapgo.Product" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <%@include file="includes/header.jsp" %>
    <link
      rel="stylesheet"
      type="text/css"
      href="<%=request.getContextPath()%>/lib/css/product-view.css"
    />
        <title>Product</title>
    </head>
    <body>
        <%@include file="includes/navbar.jsp" %>
        <div class="container" style="min-height: 1000px;">
            <div class="card-header my-3">Product Details</div>
            <div class="product-center">
                <img class="main-image" src="<%=request.getContextPath()%>/images/epomaker_alice.jpg" alt="keyboard">
                <section class="content">
                    <h2>${product.getName()}</h2>
                    <h5 class="price">&#36;${product.getPrice()}</h5>
                    <p class="vendor"><span>Vendor: </span>${product.getVendor()}</p>
                    <p class="desc"><span>Description: </span>${product.getDescription()}</p>
                    <hr />
                    <!-- staff members can see a button to modify product -->
                    <% if (session.getAttribute("isStaff") != null) { %>
                        <a style=" background: hsl(221, 100%, 33%);color: hsl(221, 100%, 95%);"
                           class="btn btn-sm mb-3" href="<%=request.getContextPath()%>/modify-product.jsp?slug=${product.getSlug()}"
                        >Modify
                        </a>
                    <% } else { %>
                    <!-- customers see button to add to cart -->
                    <form action="<%=request.getContextPath()%>/cart/add" method="POST">
                        <input type="hidden" name="slug" value="${product.getSlug()}">
                            <button style=" background: hsl(221, 100%, 33%);color: hsl(221, 100%, 95%);"
                                    type="submit" class="btn btn-dark"
                                >Add To Cart
                            </button>
                    </form>
                    <% } %>
                </section>
            </div>
        </div>
    </body>
</html>