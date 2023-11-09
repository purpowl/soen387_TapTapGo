<%@ page contentType="text/html;charset=UTF-8"%>
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
        <h4>Hello <span> [Username]</span></h4> <!-- TODO -->
      </div>
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
                  <form>
                      <!-- Form Row-->
                      <div class="row mb-3">
                          <!-- Form Group (first name)-->
                          <div class="col-md-6">
                              <label class="small mb-1" for="inputFirstName">First name</label>
                              <input class="form-control" id="inputFirstName" type="text" placeholder="Enter your first name" value="Valerie">
                          </div>
                          <!-- Form Group (last name)-->
                          <div class="col-md-6">
                              <label class="small mb-1" for="inputLastName">Last name</label>
                              <input class="form-control" id="inputLastName" type="text" placeholder="Enter your last name" value="Luna">
                          </div>
                      </div>
                      <!-- Form Group (email address)-->
                      <div class="mb-3">
                          <label class="small mb-1" for="inputEmailAddress">Email address</label>
                          <input class="form-control" id="inputEmailAddress" type="email" placeholder="Enter your email address" value="name@example.com">
                      </div>
                      <!-- Form Group (telephone)-->
                      <div class="mb-3">
                          <label class="small mb-1" for="inputPhone">Phone number</label>
                          <input class="form-control" id="inputPhone" type="email" placeholder="Enter your phone number" value="555-123-4567">
                      </div>
                      <!-- Save changes button-->
                      <button style="background: hsl(221, 100%, 33%);color: hsl(221, 100%, 95%);" class="btn" type="button">Save changes</button>
                  </form>
              </div>
          </div>
      </div>
    </div>
</div>

<%@include file="includes/footer.jsp" %>
</body>
</html>