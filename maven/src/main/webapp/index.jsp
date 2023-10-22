<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <%@include file="includes/header.jsp" %>
    <title>TapTapGo</title>
</head>
<body>
<%@include file="includes/navbar.jsp" %>

<!--Hero Section-->
<div class="hero">
    <div class="container">
        <article className="content">
            <h1>The best mechanical<br />keyboards for you.</h1>
            <p>We were founded to support Concordians with our favourite obsession.
                No import fees, long wait times for shipping, or foreign-currency
                exchange rates. We offer simple pricing and free shipping in Montreal.</p>
            <p><a href="products.jsp" class="btn btn-secondary me-2">Shop Now</a></p>
        </article>
        <article className="img-container">
            <img src="/taptapgo/images/hero-bcg.jpeg" alt="green keyboard" className="main-img" />
        </article>
    </div>
</div>

<%@include file="includes/footer.jsp" %>
</body>
</html>