<!-- Navbar -->
<nav
  class="custom-navbar navbar navbar-expand-md navbar-light bg-transparent"
  aria-label="Taptapgo navigation bar"
>
  <!-- Container wrapper -->
  <div class="container">
    <a class="navbar-brand" href="<%=request.getContextPath()%>/index.jsp">
      <img
        style="width: 175px; margin-right: 20px; border-radius: 0.25rem"
        src="<%=request.getContextPath()%>/images/TapTapGo-logo-blue.png"
        alt="TapTapGo"
      />
    </a>

    <!-- Collapsible wrapper -->
    <div class="collapse navbar-collapse" id="navbarsTaptapgo">
      <!-- Left links -->
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
      <!-- <ul class="custom-navbar-nav navbar-nav ms-auto mb-2 mb-md-0"> -->
        <li class="nav-item">
          <a class="nav-link" href="<%=request.getContextPath()%>/index.jsp">Home</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="<%=request.getContextPath()%>/products.jsp">Shop</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="<%=request.getContextPath()%>/orders">Search Order</a>
        </li>
        <!-- staff members can see a link to Create Product page -->
        <% if (session.getAttribute("isStaff") != null) { %>
        <li class="nav-item">
          <a class="nav-link" href="<%=request.getContextPath()%>/create-product.jsp">Create Product</a>
        </li>
        <% } %>
        <!-- staff members see logout button -->
        <% if (session.getAttribute("isStaff") != null) { %>
        <li><a class="nav-link" href="logout">Log Out</a></li>
        <% } else { %>
        
      </ul>
      <!-- Left links -->
    </div>
    <!-- Collapsible wrapper -->

    <!-- customers see login button and cart -->
    <!-- Right elements -->
    <div class="d-flex align-items-center">
        <!-- Cart Icon -->
        <a class="nav-link" href="<%=request.getContextPath()%>/cart.jsp">
          <img src="<%=request.getContextPath()%>/images/cart.svg" alt="cart icon"/>
        </a>
        <!-- Avatar -->
        <div class="dropdown">
          <a
          class="dropdown-toggle d-flex align-items-center hidden-arrow"
          href="${pageContext.request.contextPath}/login.jsp?from=${pageContext.request.requestURI}"
          id="navbarDropdownMenuAvatar"
          role="button"
          data-mdb-toggle="dropdown"
          aria-expanded="false"
          > 
            <img src="<%=request.getContextPath()%>/images/user.svg" alt="user icon"/>
          </a>
          <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdownMenuAvatar">
            <li>
              <a class="dropdown-item" href="#">My profile</a>
            </li>
            <li>
              <a class="dropdown-item" href="#">Settings</a>
            </li>
            <li>
              <a class="dropdown-item" href="#">Logout</a>
            </li>
          </ul>
        </div>
        <!-- Avatar -->
        <!-- Toggle button -->
        <button
          class="navbar-toggler"
          type="button"
          data-mdb-toggle="collapse"
          data-mdb-target="#navbarSupportedContent"
          aria-controls="navbarSupportedContent"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span class="navbar-toggler-icon"></span>
        </button>
      </div> 
      <!-- Right elements --> 
      <% } %>
    </div>
  <!-- Container wrapper -->
</nav>
<!-- Navbar -->
