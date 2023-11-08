
<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
  <%@include file="includes/header.jsp" %>
  <title>Staff Login</title>
</head>
<body>
<%@include file="includes/navbar.jsp" %>
<div class="container">
    <!-- Register -->
    <div class="card w-100 mx-auto my-5 border-0">
        <div class="card-body p-5">
            <!-- Form Title -->
            <h2 class="text-center mb-3">Register</h2>
            <!-- Form -->
            <form action="register" method="post">
                <!-- First Name & Last Name Row -->
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-outline mb-3">
                            <label class="form-label" for="firstName">First Name</label>
                            <input type="text" id="firstName" class="form-control" />
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-outline mb-3">
                            <label class="form-label" for="lastName">Last Name</label>
                            <input type="text" id="lastName" class="form-control"/>    
                        </div>
                    </div>
                </div>
            
                <!-- Email Row -->
                <div class="form-outline mb-3">
                    <label class="form-label" for="emailAddress">Email Address</label>
                    <input type="email" id="emailAddress" class="form-control" >
                </div>
                
                <!-- Phone Number Row -->
                <div class="form-outline mb-3">
                    <label for="phoneNumber" class="form-label">Telephone</label>
                    <input type="tel" id="phoneNumber" class="form-control">
                </div>

                <!-- Username Row -->
                <div class="form-outline mb-3">
                    <label for="username" class="form-label">Username</label>
                    <input type="text" id="register-username" class="form-control">
                </div>

                <!-- Password Row -->
                <div class="form-outline mb-3">
                    <label for="inputPassword" class="form-label">Password</label>
                    <input type="password" id="register-pwd" class="form-control">
                </div>

                <!-- Term Agreement-->
                <div class="form-outline mb-3 form-check">
                    <input type="checkbox" class="form-check-input" id="termsCheck">
                    <label class="form-check-label" for="termsCheck">I agree to all statements in <span style="text-decoration: underline">Terms of service</span></label>
                </div>

                <!-- Register button-->
                <div class="text-center mb-3">
                    <button style=" background: hsl(221, 100%, 33%);color: hsl(221, 100%, 95%);" type="submit" class="btn btn-block btn-lg">Register</button> 
                </div>
            </form>
        </div>
    </div>
    <!-- Register -->
</div>

<%@include file="includes/footer.jsp" %>
</body>
</html>
