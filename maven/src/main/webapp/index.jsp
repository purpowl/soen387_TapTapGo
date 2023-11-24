<%@ page import="com.taptapgo.Product" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.taptapgo.User" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
  session = request.getSession(false);
  // if user is not registered
  if (session.getAttribute("isRegisteredUser") == null) {
    // create new cart if they don't have a cart yet
    if (session.getAttribute("cart") == null) {
      HashMap<Product, Integer> cart = new HashMap<>();
      session.setAttribute("cart", cart);
    }
    // create guest ID if they don't have one
    if (session.getAttribute("userID") == null){
      session.setAttribute("userID", "gc" + session.getId());
    }
    else {
      // else get their registered user ID
      session.setAttribute("userID", ((User) session.getAttribute("registered_user")).getUserID());
    }
  }
%>
<!DOCTYPE html>
<html>
  <head>
    <%@include file="includes/header.jsp" %>
    <link
      rel="stylesheet"
      type="text/css"
      href="<%=request.getContextPath()%>/lib/css/hero.css"
    />
    <title>TapTapGo</title>
  </head>
  <body>
    <%@include file="includes/navbar.jsp" %>

    <!--Hero Section-->
    <div class="container">
      <div class="hero-container">
        <article class="content">
          <h1>The best mechanical<br />keyboards for you.</h1>
          <p>
            We were founded to support Concordians with our favourite obsession.
            No import fees, long wait times for shipping, or foreign-currency
            exchange rates. We offer simple pricing and free shipping in
            Montreal.
          </p>
          <p>
            <a
              style="
                text-transform: uppercase;
                background: hsl(221, 100%, 33%);
                color: hsl(221, 100%, 95%);
              "
              href="<%=request.getContextPath()%>/products.jsp"
              class="btn btn-lg me-2"
              >Shop Now</a
            >
          </p>
        </article>
        <article class="img-container">
            <img src="<%=request.getContextPath()%>/images/hero-bcg.jpeg" alt="green keyboard" class="main-img" />
        </article>
      </div>
    </div>
    <%@include file="includes/footer.jsp" %>
  </body>
</html>
