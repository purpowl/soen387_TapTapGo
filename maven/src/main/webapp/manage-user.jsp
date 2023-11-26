<%@ page import="com.taptapgo.User" %>
<%@ page import="com.taptapgo.Staff" %>
<%@ page import="com.taptapgo.Customer" %>
<%@ page import="com.taptapgo.repository.UserIdentityMap" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%
  // only staff can access this page, redirect to login page otherwise
  session = request.getSession(false);
  if (session == null || session.getAttribute("isStaff") == null) {
    response.sendRedirect("login.jsp");
  }
%>
<html>
<head>
  <%@include file="includes/header.jsp" %>
  <title>Ship Order</title>
</head>
<body>
<%@include file="includes/navbar.jsp" %>

<div class="container mb-5">

  <!-- Search box -->
  <div class="d-flex justify-content-end">
    <form class="form-inline">
      <input class="form-control mr-sm-2" type="search" placeholder="Enter User ID to search" aria-label="Search">
      <button class="btn btn-outline-dark my-2 my-sm-0" type="submit">Search</button>
    </form>
  </div>

  <!-- Page Indicator -->
  <div class="card-header my-3 ">All Users</div>
  
  <!-- Container Wrapper -->
  <div class="row">
    <div class="col-lg-12 my-3">
      <div class="card">
        <div class="card-body p-0 table-responsive">
          <!-- Order Info table -->
          <table class="table mb-0">
            <thead class="table-dark">
              <tr>
                <th scope="col">USER ID</th>
                <th scope="col">NAME</th>
                <th scope="col">ROLE</th>
                <th scope="col">EMAIL</th>
                <th scope="col">PHONE</th>
                <th scope="col"></th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td><span class="badge badge-success"></span>User ID</td>
                <td>First Name + Last Name</td>
                <td>Role</td>
                <td>Email</td>
                <td>Phone</td>
                <!-- Remove Staff/Make Staff button-->
                <%  if (session.getAttribute("isStaff") != null) { %>
                <td>
                  <button class="btn btn-sm btn-outline-danger">Remove Staff</button>
                </td>
                <% } else { %>
                <td>
                  <button class="btn btn-sm btn-outline-secondary">Make Staff</button>
                </td>
                <% } %>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</div>

<%@include file="includes/footer.jsp" %>
</body>
</html>