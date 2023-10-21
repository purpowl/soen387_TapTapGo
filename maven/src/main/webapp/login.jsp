
<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
  <%@include file="includes/header.jsp" %>
  <title>Staff Login</title>
</head>
<body>
<%@include file="includes/navbar.jsp" %>

<div class="container">
  <div class="card w-50 mx-auto my-5">
    <div class="card-header text-center">Staff Login</div>
    <div class="card-body">
      <!-- form for staff login, calls LoginServlet doPost -->
      <form action="login" method="post">
        <div class="form-group mb-3">
          <label for="staff-pwd">Password</label>
          <input type="password" id="staff-pwd" name="login-password" class="form-control" placeholder="Password">
          <!-- pass the previous page to servlet to redirect once login is successful -->
          <input type="hidden" name="from" value="${param.from}">
        </div>
        <div class="text-center mb-3">
          <button type="submit" class="btn btn-primary">Login</button>
        </div>
      </form>
    </div>
  </div>
</div>

<%@include file="includes/footer.jsp" %>
</body>
</html>