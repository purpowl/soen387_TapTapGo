<%@ page import="com.taptapgo.User" %>
<%@ page import="com.taptapgo.repository.UserIdentityMap" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%
  // only staff can access this page, redirect to login page otherwise
  session = request.getSession(false);
  if (session == null || session.getAttribute("isStaff") == null) {
    response.sendRedirect(request.getContextPath() + "/login.jsp");
  }

  HashMap<String, User> allUsers = UserIdentityMap.loadAllUsers();
  String currentUserID = (String) session.getAttribute("userID");
%>
<html>
<head>
  <%@include file="includes/header.jsp" %>
  <title>Ship Order</title>
</head>
<body>
<%@include file="includes/navbar.jsp" %>

<div class="container mb-5">

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
                <th scope="col">NAME</th>
                <th scope="col">ROLE</th>
                <th scope="col">EMAIL</th>
                <th scope="col">PHONE</th>
                <th scope="col"></th>
              </tr>
            </thead>
            <tbody>
              <%
                for (Map.Entry<String, User> user_entry : allUsers.entrySet()) {
                  User user = user_entry.getValue();
              %>
              <tr>
                <td><%=user.getFirstName() + " " + user.getLastName()%></td>
                <%
                  if (user.isStaff()) {
                %>
                    <td>Staff</td>
                <% } else { %>
                    <td>Customer</td>
                <% } %>
                <td><%=user.getEmail()%></td>
                <td><%=user.getPhone()%></td>
                <!-- Remove Staff/Make Staff button-->
                <% if (user.getUserID().equals(currentUserID)) { %>
                  <td></td>
                <% } else if (user.isStaff()) { %>
                <td>
                  <form class="mb-0" action="<%=request.getContextPath()%>/staff" method="post">
                    <input type="hidden" name="userID" value="<%=user.getUserID()%>">
                    <input type="hidden" name="action" value="revoke">
                    <button class="btn btn-sm btn-outline-danger" type="submit">Remove Staff</button>
                  </form>
                </td>
                <% } else { %>
                <td>
                  <form class="mb-0" action="<%=request.getContextPath()%>/staff" method="post">
                    <input type="hidden" name="userID" value="<%=user.getUserID()%>">
                    <input type="hidden" name="action" value="promote">
                    <button class="btn btn-sm btn-outline-secondary" type="submit">Make Staff</button>
                  </form>
                </td>
                <% } %>
              </tr>
              <% } %>
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