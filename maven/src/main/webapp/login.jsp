<%@ page contentType="text/html;charset=UTF-8"%>
<%
  // only guest users can access this page, redirect to account page otherwise
  session = request.getSession(false);
  if (session.getAttribute("isRegisteredUser") != null || session.getAttribute("isStaff") != null) {
    response.sendRedirect("user-account.jsp");
  }
%>
<html>
<head>
  <%@include file="includes/header.jsp" %>
  <title>Login</title>
</head>
<body>
<%@include file="includes/navbar.jsp" %>

<div class="container">
  <!-- Login -->
  <div class="card w-100 mx-auto my-5 border-0">
    <!-- Error Handling -->
    <%
      String createStatus = request.getParameter("create");

      if (createStatus != null) {
        if (createStatus.equals("fail")) {
    %>
    <div class="row">
      <div class="col-12">
        <p style="color: red;">Login failed. Wrong password.</p>
      </div>
    </div>
    <%
        }
      }
    %>
    <div class="card-body p-5">
      <h2 class="text-center mb-3">Login</h2>
      <!-- form for staff login, calls LoginServlet doPost -->
      <form action="login" method="post">

        <!-- Password -->
        <div class="form-group mb-3">
          <label for="passcode">Passcode</label>
            <input type="password" id="passcode" name="login-password" class="form-control" placeholder="Passcode">
            <!-- pass the previous page to servlet to redirect once login is successful -->
            <input type="hidden" name="from" value="${param.from}">
        </div>

        <!-- Button -->
        <div class="text-center mb-3">
          <button style=" background: hsl(221, 100%, 33%);color: hsl(221, 100%, 95%);" type="submit" class="btn btn-lg btn-block">Login</button>
        </div>
        <div class="text-center mb-3">
          <a style="color: hsl(221, 100%, 33%)" href="<%=request.getContextPath()%>/register.jsp">Don't have an account? Register here</a>
        </div>
      </form>
    </div>
  </div>
</div>
    
<%@include file="includes/footer.jsp" %>
</body>
</html>