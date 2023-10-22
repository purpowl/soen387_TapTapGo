<%@ page import="com.taptapgo.Product" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <%@include file="includes/header.jsp" %>
        <title>Product</title>
    </head>
    <body>
        <%@include file="includes/navbar.jsp" %>

        <div class="container-fluid mx-4 my-5">
            <div class="row">
                <div class="col">
                    <img src="images/epomaker_alice.jpg" alt="keyboard">
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
                                    <button type="submit" class="btn btn-dark">Add To Cart</button>
                                </form>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </body>
</html>