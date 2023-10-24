<nav
  class="navbar navbar-expand-md navbar-light bg-transparent"
  aria-label="Taptapgo navigation bar"
>
  <div class="container">
    <!-- <a class="navbar-brand" href="<%=request.getContextPath()%>/index.jsp">TapTapGo</a> -->
    <a class="navbar-brand" href="<%=request.getContextPath()%>/index.jsp">
      <img
        style="width: 175px; margin-right: 20px; border-radius: 0.25rem"
        src="<%=request.getContextPath()%>/images/TapTapGo-logo-blue.png"
        alt="TapTapGo"
      />
    </a>
    <button
      class="navbar-toggler"
      type="button"
      data-bs-toggle="collapse"
      data-bs-target="#navbarsTaptapgo"
      aria-controls="navbarsTaptapgo"
      aria-expanded="false"
      aria-label="Toggle navigation"
    >
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarsTaptapgo">
      <ul class="custom-navbar-nav navbar-nav ms-auto mb-2 mb-md-0">
        <li class="nav-item active">
          <a class="nav-link" href="<%=request.getContextPath()%>/index.jsp"
            >Home</a
          >
        </li>
        <li>
          <a class="nav-link" href="<%=request.getContextPath()%>/products.jsp"
            >Products</a
          >
        </li>
        <!-- staff members can see a link to Create Product page -->
        <% if (session.getAttribute("isStaff") != null) { %>
        <li>
          <a
            class="nav-link"
            href="<%=request.getContextPath()%>/create-product.jsp"
            >Create a Product</a
          >
        </li>
        <% } %>
      </ul>
      <ul class="custom-navbar-cta navbar-nav mb-2 mb-md-0 ms-5">
        <!-- staff members see logout button -->
        <% if (session.getAttribute("isStaff") != null) { %>
        <li><a class="nav-link" href="logout">Log Out</a></li>
        <% } else { %>
        <!-- customers see login button and cart -->
        <li>
          <a
            class="nav-link"
            href="${pageContext.request.contextPath}/login.jsp?from=${pageContext.request.requestURI}"
            ><img
              src="<%=request.getContextPath()%>/images/user.svg"
              alt="user icon"
          /></a>
        </li>
        <li>
          <a class="nav-link" href="<%=request.getContextPath()%>/cart.jsp"
            ><img
              src="<%=request.getContextPath()%>/images/cart.svg"
              alt="cart icon"
          /></a>
        </li>
        <% } %>
      </ul>
    </div>
  </div>
</nav>
