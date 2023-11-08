<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
  <%@include file="includes/header.jsp" %>
  <title>Staff Login</title>
</head>
<body>
<%@include file="includes/navbar.jsp" %>

<div class="container">
  <!-- Login -->
  <div class="card w-100 mx-auto my-5 border-0">
    <div class="card-body p-5">
      <h2 class="text-center mb-3">Login</h2>
      <!-- form for staff login, calls LoginServlet doPost -->
      <form action="login" method="post">
        <!-- Username -->
        <div class="form-group mb-3">
          <label for="username">Username</label>
            <input type="username" id="pwd" name="login-username" class="form-control" placeholder="Username">
        </div>

        <!-- Password -->
        <div class="form-group mb-3">
          <label for="password">Password</label>
            <input type="password" id="pwd" name="login-password" class="form-control" placeholder="Password">
            <!-- pass the previous page to servlet to redirect once login is successful -->
            <input type="hidden" name="from" value="${param.from}">
        </div>

        <!-- Button -->
        <div class="text-center mb-3">
          <button style=" background: hsl(221, 100%, 33%);color: hsl(221, 100%, 95%);" type="submit" class="btn btn-lg btn-block">Login</button>
        </div>
        <div class="text-center mb-3">
          <a style="color: hsl(221, 100%, 33%)"href="<%=request.getContextPath()%>/register.jsp">Don't have an account? Register here</a>
        </div>
      </form>
    </div>
  </div>
</div>
    
<%@include file="includes/footer.jsp" %>
</body>
</html>