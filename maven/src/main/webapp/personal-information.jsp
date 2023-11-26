<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="com.taptapgo.User" %>
<%
    // only registered users can access this page, redirect to login page otherwise
    session = request.getSession(false);
    if (session == null || session.getAttribute("isRegisteredUser") == null) {
        response.sendRedirect("login.jsp");
    }
    // reference to user
    User user = (User) session.getAttribute("registered_user");
%>

<html>
<head>
  <%@include file="includes/header.jsp" %>
  <title>Personal Information</title>
</head>
<body>
<%@include file="includes/navbar.jsp" %>

<div class="container mb-4">
    <div class="row">
        <div class="col-sm-12 mt-3 ">
            <h4>Hello <span><%=user.getFirstName() + " " + user.getLastName()%></span></h4> <!-- TODO -->
        </div>
        <%
            String failureMessage = (String) request.getParameter("failureMessage");
            String successMessage = (String) request.getParameter("successMessage");
            if (failureMessage != null) {
                if (failureMessage.equals("fieldEmpty")) {
        %>
        <div class="col-sm-12">
            <p style="color:red">Please leave the fields at its original value if you don't want to change the field!</p>
        </div>
        <%
                } else if (failureMessage.equals("dbFail")) {
        %>
        <div class="col-sm-12">
            <p style="color:red">There's a problem with our server at the moment. Please try again later!</p>
        </div>
        <%
                }
            } else if(successMessage != null) {
        %>
        <div class="col-sm-12">
            <p style="color:green">Account information successfully changed!</p>
        </div>
        <%
            }
        %>
        <div class="col-sm-4">
            <!-- Profile picture card-->
            <div class="card w-100 mx-auto mt-3 mb-5">
                <div class="card-header">Profile Picture</div>
                <div class="card-body text-center">
                    <!-- Profile picture image -->
                    <img class="img-account-profile rounded-circle mb-2" src="" alt="">
                    <!-- Profile picture help block -->
                    <div class="small font-italic text-muted mb-4">JPG or PNG no larger than 5 MB</div>
                    <!-- Profile picture upload button -->
                    <button style="background: hsl(221, 100%, 33%);color: hsl(221, 100%, 95%)" class="btn" type="button">Upload new image</button>
                </div>
            </div>
        </div>
        <div class="col-sm-8">
            <!-- Account details card-->
            <div class=" card w-100 mx-auto mt-3 mb-5">
                <div class="card-header">Personal Information</div> <!-- TODO -->
                <div class="card-body">
                    <form action="<%=request.getContextPath()%>/account" method="post">
                        <!-- Form Row-->
                        <input type="hidden" name="userID" value="<%=user.getUserID()%>">
                        <div class="row mb-3">
                            <!-- Form Group (first name)-->
                            <div class="col-md-6">
                                <label class="small mb-1" for="inputFirstName">First name</label>
                                <input class="form-control" id="inputFirstName" name="inputFirstName" type="text" placeholder="Enter your first name" value="<%=user.getFirstName()%>">
                            </div>
                            <!-- Form Group (last name)-->
                            <div class="col-md-6">
                                <label class="small mb-1" for="inputLastName">Last name</label>
                                <input class="form-control" id="inputLastName" name="inputLastName" type="text" placeholder="Enter your last name" value="<%=user.getLastName()%>">
                            </div>
                        </div>
                        <!-- Form Group (email address)-->
                        <div class="mb-3">
                            <label class="small mb-1" for="inputEmailAddress">Email address</label>
                            <input class="form-control" id="inputEmailAddress" name="inputEmailAddress" type="email" placeholder="Enter your email address" value="<%=user.getEmail()%>">
                        </div>
                        <!-- Form Group (telephone)-->
                        <div class="mb-3">
                            <label class="small mb-1" for="inputPhone">Phone number</label>
                            <input class="form-control" id="inputPhone" name="inputPhone" type="tel" placeholder="Enter your phone number" value="<%=user.getPhone()%>" pattern="[0-9]{10}">
                        </div>
                        <!-- Save changes button-->
                        <button style="background: hsl(221, 100%, 33%);color: hsl(221, 100%, 95%);" class="btn" type="submit">Save changes</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<%@include file="includes/footer.jsp" %>
</body>
</html>