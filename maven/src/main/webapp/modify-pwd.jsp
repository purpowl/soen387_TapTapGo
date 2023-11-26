<%@ page contentType="text/html;charset=UTF-8"%>
<%
  // only registered users can access this page, redirect to login page otherwise
  session = request.getSession(false);
  if (session == null || session.getAttribute("isRegisteredUser") == null) {
    response.sendRedirect("login.jsp");
  }
%>
<html>
<head>
  <%@include file="includes/header.jsp" %>
  <title>Modify username and password</title>
</head>
<body>
<%@include file="includes/navbar.jsp" %>

<div class="container">
  <!-- Change password -->
  <div class="card w-100 mx-auto my-5 border-0">
    <%
      String modifyStatus = request.getParameter("modify");

      if (modifyStatus != null) {
        if (modifyStatus.equals("success")) {
    %>
    <div class="row">
      <div class="col-12">
        <p style="color: #379237;">Passcode successfully modified!</p>
      </div>
    </div>
    <%
    } else {
    %>
    <div class="row">
      <div class="col-12">
        <p style="color: red;">Fail to modify passcode! Please try again.</p>
      </div>
    </div>
    <%
        }
      }
    %>
    <div class="card-body p-5">
      <h2 class="text-center mb-3">Modify passcode</h2>
      <form action="<%=request.getContextPath()%>/change-passcode" method="post">

        <!-- Old Password-->
        <div class="form-group mb-3">
          <label class="form-label" for="oldpwd">Current Passcode</label>
          <input type="password" id="oldpwd" name="oldpwd" class="form-control" placeholder="Enter current passcode" required="">
        </div>

        <!-- New Password-->
        <div class="form-group mb-3">
          <label class="form-label" for="newpwd">Passcode</label>
          <input type="password" id="newpwd" name="newpwd" class="form-control" placeholder="Enter new passcode" required="">
        </div>

        <!-- Buttons -->
        <div class="text-center mb-3">
          <a href="<%=request.getContextPath()%>/user-account.jsp" class="btn btn-outline-secondary">Cancel</a>
          <button style=" background: hsl(221, 100%, 33%);color: hsl(221, 100%, 95%);" type="submit" class="btn">Submit</button>
        </div>
      </form>
    </div>
  </div>
</div>

<%@include file="includes/footer.jsp" %>
</body>
</html>