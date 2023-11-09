
<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
  <%@include file="includes/header.jsp" %>
  <title>Register/title>
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
                            <input type="text" id="firstName" class="form-control" placeholder="" value="" required=""/>
                            <div class="invalid-feedback"> First Name cannot be empty. </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-outline mb-3">
                            <label class="form-label" for="lastName">Last Name</label>
                            <input type="text" id="lastName" class="form-control" placeholder="" value="" required=""/>    
                            <div class="invalid-feedback"> Last Name cannot be empty. </div>
                        </div>
                    </div>
                </div>
            
                <!-- Email Row -->
                <div class="form-outline mb-3">
                    <label class="form-label" for="emailAddress">Email Address</label>
                    <input type="email" id="emailAddress" class="form-control" placeholder="you@example.com" value="" required="">
                    <div class="invalid-feedback"> Please enter a valid email address for shipping updates. </div>
                </div>
                
                <!-- Phone Number Row -->
                <div class="form-outline mb-3">
                    <label class="form-label" for="phoneNumber">Telephone<span class="text-muted">(Optional)</span></label>
                    <input type="tel" id="phoneNumber" class="form-control">
                </div>

                <!-- Username Row -->
                <div class="form-outline mb-3">
                    <label class="form-label" for="registerUsername" >Username</label>
                    <input type="text" id="registerUsername" class="form-control" placeholder="" value="" required="">
                    <div class="invalid-feedback"> Username cannot be empty. </div>
                </div>

                <!-- Password Row -->
                <div class="form-outline mb-3">
                    <label class="form-label" for="registerPassword">Password</label>
                    <input type="password" id="registerPassword" class="form-control" placeholder="" value="" required="">
                    <div class="invalid-feedback"> Password cannot be empty. </div>
                </div>

                <!-- Term Agreement-->
                <div class="form-outline mb-3 form-check">
                    <input type="checkbox" class="form-check-input" id="termsCheck">
                    <label class="form-check-label" for="termsCheck">I agree to all statements in <span style="text-decoration: underline">Terms of service</span></label>
                </div>

                <!-- Register button-->
                <div class="text-center mb-3">
                    <button style=" background: hsl(221, 100%, 33%);color: hsl(221, 100%, 95%);" type="submit" class="btn btn-lg btn-block">Register</button> 
                </div>
            </form>
        </div>
    </div>
    <!-- Register -->
</div>

<%@include file="includes/footer.jsp" %>
</body>
</html>
