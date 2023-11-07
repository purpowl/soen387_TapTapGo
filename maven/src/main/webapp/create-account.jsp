<%@ page contentType="text/html;charset=UTF-8"%>

<html>
<head>
    <%@include file="includes/header.jsp" %>
    <title>Create User Account</title>
    <link
      rel="stylesheet"
      type="text/css"
      href="<%=request.getContextPath()%>/lib/css/create-account.css"
    />
</head>
<body>
<%@include file="includes/navbar.jsp" %>

<div class="container">
    <div class="card w-70 mx-auto my-5">
        <div class="card-body p-5">
            <h2 class="text-center mb-3">Join TapTapGo Community</h2>
            <form>
                <div class="form-outline mb-3">
                    <label for="inputName" class="form-label">Your Name</label>
                    <input type="text" class="form-control" id="inputName">
                </div>
                <div class="form-outline mb-3">
                    <label for="inputEmail" class="form-label">Your Email Address</label>
                    <input type="email" class="form-control" id="inputEmail">
                </div>
                <div class="form-outline mb-3">
                    <label for="inputPassword" class="form-label">Password</label>
                    <input type="password" class="form-control" id="inputPassword" aria-describedby="passwordCondition">
                    <div id="passwordCondition" class="form-text">Must contain 6 or more characters and at least 1 number</div>
                </div>
                <div class="form-outline mb-3">
                    <label for="inputRepeatPassword" class="form-label">Repeat your Password</label>
                    <input type="password" class="form-control" id="inputRepeatPassword">
                </div>
                <div class="form-outline mb-3 form-check">
                    <input type="checkbox" class="form-check-input" id="termsCheck">
                    <label class="form-check-label" for="termsCheck">I agree to all statements in <span style="text-decoration: underline">Terms of service</span></label>
                </div>
                <div class="text-center mb-3">
                    <button 
                        style=" background: hsl(221, 100%, 33%);color: hsl(221, 100%, 95%);"
                        type="submit" 
                        class="btn btn-block btn-lg"
                        >Create Account
                    </button>
                </div>

            </form>
        </div>
    </div>
</div>

<%@include file="includes/footer.jsp" %>
</body>
</html>