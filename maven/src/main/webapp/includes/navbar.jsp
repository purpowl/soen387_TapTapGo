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
        <!-- Home -->
        <li class="nav-item">
          <a class="nav-link" href="<%=request.getContextPath()%>/index.jsp">Home</a>
        </li>

        <!-- Shop-->
        <li class="nav-item">
          <a class="nav-link" href="<%=request.getContextPath()%>/products.jsp">Shop</a>
        </li>

        <!-- Search order - all users can see this link to search for their order-->
        <li class="nav-item">
          <a class="nav-link" href="<%=request.getContextPath()%>/orders.jsp">Search Guest Order</a>
        </li>

        <!-- Create Product page - staff members can see a link to add new product -->
        <% if (session.getAttribute("isStaff") != null) { %>
        <li class="nav-item">
          <a class="nav-link" href="<%=request.getContextPath()%>/manage-product.jsp">Manage Product</a>
        </li>
        <% } %>

        <!-- Ship order - staff member can see this link to ship order -->
        <% if (session.getAttribute("isStaff") != null) { %>
        <li class="nav-item">
          <a class="nav-link" href="<%=request.getContextPath()%>/manage-order.jsp">Manage Order</a>
        </li>
        <% } %>


        <!-- Revoke/Promote user - staff member can see this link to promote customer to staff and revoke staff permission -->
        <% if (session.getAttribute("isStaff") != null) { %>
        <li><a class="nav-link" href="<%=request.getContextPath()%>/manage-user.jsp">Manage User</a></li>
        <% } %>
        
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
          <% if (session.getAttribute("isRegisteredUser") == null) { %>
            href="${pageContext.request.contextPath}/login.jsp?"
          <% } else { %>
            href="${pageContext.request.contextPath}/user-account.jsp"
          <% } %>
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
        <!-- <button
          class="navbar-toggler"
          type="button"
          data-mdb-toggle="collapse"
          data-mdb-target="#navbarSupportedContent"
          aria-controls="navbarSupportedContent"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span class="navbar-toggler-icon"></span>
        </button> -->
        <!-- Toggle button -->

      </div> 
      <!-- Right elements --> 
    </div>
  <!-- Container wrapper -->
</nav>
<!-- Navbar -->
