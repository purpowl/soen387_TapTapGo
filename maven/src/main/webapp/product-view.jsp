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
                    <!-- staff members can see a button to download product catalog -->
                    <% if (session.getAttribute("isStaff") != null) { %>
                        <a style=" background: hsl(221, 100%, 33%);color: hsl(221, 100%, 95%);"
                           class="btn btn-sm mb-3" href="<%=request.getContextPath()%>/modify-product.jsp?slug=${product.getSlug()}"
                        >Modify
                        </a>
                    <% } else { %>
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
        <!-- <div class="container" style="min-height: 1000px;">
            <div class="card-header my-3">Product Details</div>
            <div class="row">
                <div class="col-8">
                    <img class="main" src="<%=request.getContextPath()%>/images/epomaker_alice.jpg" alt="keyboard">
                </div>
                <div class="col">
                    <div class="container-fluid">
                        <div class="row">
                            <div class="col-12">
                                <p class="font-weight-bold font-size-2">${product.getName()}</p>
                            </div>
                        </div>
                        <div class="row mt-3">
                            <div class="col-12">
                                <p class="font-weight-light">&#36;${product.getPrice()}</p>
                            </div>
                        </div>
                        <div class="row mt-3">
                            <div class="col-12">
                                <p>Vendor: ${product.getVendor()}</p>
                                <p>Description:</p>
                                <p>${product.getDescription()}</p>
                            </div>
                        </div>
                        <div class="row mt-3">
                            <div class="col-12">
                                <form action="<%=request.getContextPath()%>/cart/add" method="POST">
                                    <input type="hidden" name="slug" value="${product.getSlug()}">
                                    <button style=" background: hsl(221, 100%, 33%);color: hsl(221, 100%, 95%);"
                                            type="submit" class="btn btn-dark"
                                        >Add To Cart
                                    </button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div> -->
    </body>
</html>