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
  <title>Manage User</title>
</head>
<body>
<%@include file="includes/navbar.jsp" %>


<%@include file="includes/footer.jsp" %>
</body>
</html>
