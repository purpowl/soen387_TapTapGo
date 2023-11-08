<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
  <%@include file="includes/header.jsp" %>
  <title>Staff Login</title>
</head>
<body>
<%@include file="includes/navbar.jsp" %>

<div class="container">
  <!-- Change username and password -->
  <div class="card w-100 mx-auto my-5 border-0">
    <div class="card-body p-5">
      <h2 class="text-center mb-3">Modify username and password</h2>
      <form method="post">
        
        <!-- Old username -->
        <div class="form-group mb-3">
          <label class="form-label" for="oldUsername">Current Username</label>
          <input type="text" id="oldUsername" name="oldUsername" class="form-control" placeholder="Enter current username">
        </div>

        <!-- New username -->
        <div class="form-group mb-3">
          <label class="form-label" for="newUsername">New Username</label>
          <input type="text" id="newUsername" name="newUsername" class="form-control" placeholder="Enter new username">
        </div>

        <!-- Old Password-->
        <div class="form-group mb-3">
          <label class="form-label" for="oldpwd">Password</label>
          <input type="password" id="oldpwd" name="oldpwd" class="form-control" placeholder="Enter current password">
        </div>

        <!-- New Password-->
        <div class="form-group mb-3">
          <label class="form-label" for="newpwd">Password</label>
          <input type="password" id="newpwd" name="oldpwd" class="form-control" placeholder="Enter new password">
        </div>

        <!-- Buttons -->
        <div class="text-center mb-3">
          <button style=" background: hsl(221, 100%, 33%);color: hsl(221, 100%, 95%);" type="submit" class="btn">Submit</button>
          <a href="<%=request.getContextPath()%>/your-account.jsp">
            <button type="reset" class="btn btn-outline-secondary">Cancel</button>
          </a>
        </div>
      </form>
    </div>
  </div>
</div>

<%@include file="includes/footer.jsp" %>
</body>
</html>