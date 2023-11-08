<html>
    <head>
        <%@include file="includes/header.jsp" %>
        <title>Cart</title>
    </head>
    <body>
        <%@include file="includes/navbar.jsp" %>
        <div class="container" style="min-height: 1000px;">
            <div class="card-header my-3">Error</div>
            <section style="justify-content: center; align-items: center; text-align: center">
                <h1 style="font-size: 7rem; margin-bottom: 2rem">404</h1>
                <h3 style="margin-bottom: 3rem">Sorry, the page you tried cannot be found</h3>
                <a 
                    style="text-transform: uppercase; background: hsl(221, 100%, 33%); color: hsl(221, 100%, 95%)"
                    href="<%=request.getContextPath()%>/index.jsp" 
                    class="btn"
                    >BACK TO HOMEPAGE
                </a>
            </section>
        </div>
    </body>
</html>
