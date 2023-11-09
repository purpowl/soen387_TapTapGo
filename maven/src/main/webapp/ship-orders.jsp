<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
  <%@include file="includes/header.jsp" %>
  <title>Staff Login</title>
</head>
<body>
<%@include file="includes/navbar.jsp" %>

<div class="container mb-5">

  <!-- Search box -->
  <div class="d-flex justify-content-end">
    <form class="form-inline">
      <input class="form-control mr-sm-2" type="search" placeholder="Enter OrderID to search" aria-label="Search">
      <button class="btn btn-outline-dark my-2 my-sm-0" type="submit">Search</button>
    </form>
  </div>

  <!-- Page Indicator -->
  <div class="card-header my-3 ">Order Details</div>
  
  <!-- Container Wrapper -->
  <div class="row">
    <div class="col-lg-12">
      <div class="card mt-4">
        <div class="card-body p-0 table-responsive">
          <!-- Order Info table -->
          <table class="table mb-0">
            <thead class="table-dark">
              <tr>
                <th scope="col">ORDER ID</th>
                <th scope="col">ORDER PLACED</th>
                <th scope="col">TOTAL</th>
                <th scope="col">SHIP TO</th>
                <th scope="col">ORDER STATUS</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>123456</td> <!-- TODO -->
                <td>2023-11-08</td> <!-- TODO-->
                <td>$129.00</td> <!-- TODO-->
                <td>1234 Main St</td> <!-- TODO: Address without City, Country, Postal Code-->
                <td>Shipped</td><!-- TODO-->
              </tr>
              <tr>
              <!-- For each product in the cart, display the image and the description -->
              <tr>
                  <th><img src="<%=request.getContextPath()%>/images/epomaker_alice.jpg" alt="product" class="" width="150"></th>  <!-- TODO -->
                  <td style="vertical-align: middle;">Tsunami</td> <!-- TODO -->
                  <td colspan="3" style="vertical-align: middle;">Lorem ipsum dolor, sit amet consectetur adipisicing elit. Dolorem, facilis.</td> <!-- TODO -->
              </tr>
            </tbody>
          </table>
          <!-- Order Info table -->
        </div>
      </div>
    </div>
  </div>
</div>

<%@include file="includes/footer.jsp" %>
</body>
</html>