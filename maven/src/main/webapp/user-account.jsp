<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="com.taptapgo.User" %>
<%
    // only registered users can access this page, redirect to login page otherwise
    session = request.getSession(false);
    if (session == null || session.getAttribute("isRegisteredUser") == null) {
        response.sendRedirect("login.jsp");
    }
    // reference to customer and order list
    User user = (User) session.getAttribute("registered_user");
%>
<html>
<head>
  <%@include file="includes/header.jsp" %>
  <title>User Account</title>
</head>
<body>
<%@include file="includes/navbar.jsp" %>

<div style ="margin-bottom: 2rem;" class="container">
    <div class="card-header my-3">Hello <%=user.getFirstName() + " " + user.getLastName()%> </div>
    <div class="row mb-10">
        <!-- Your orders -->
        <div class="col-sm">
            <div class="card w-100">
                <div class="card-body">
                    <a href="<%=request.getContextPath()%>/orders">
                        <h5 style="color: hsl(221, 100%, 33%)" class="card-title">Your Orders</h5>
                        <p class="card-text">Track orders and view all your past orders</p>
                    </a>
                </div>
            </div>
        </div>

        <!-- Persomal information -->
        <div class="col-sm">
            <div class="card w-100">
                <div class="card-body">
                    <a href ="<%=request.getContextPath()%>/personal-information.jsp">
                        <h5 style="color: hsl(221, 100%, 33%)" class="card-title">Personal Information</h5>
                        <p class="card-text">View and modify your personal information</p>
                    </a>
                </div>
            </div>
        </div>

        <!-- Account information -->
        <div class="col-sm">
            <div class="card w-100">
                <div class="card-body">
                    <a href= "<%=request.getContextPath()%>/modify-username-pwd.jsp">
                        <h5 style="color: hsl(221, 100%, 33%)" class="card-title">Modify Username & Password</h5>
                        <p class="card-text">Manage passwords and email</p>
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<%@include file="includes/footer.jsp" %>
</body>
</html>