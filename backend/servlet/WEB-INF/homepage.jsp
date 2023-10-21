<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8" />
    <meta
      name="viewport"
      content="width=device-width, initial-scale=1, shrink-to-fit=no"
    />
    <meta name="author" content="Untree.co" />
    <link rel="shortcut icon" href="/resources/TapTapGo_logo_blue.png" />

    <meta name="description" content="" />
    <meta name="keywords" content="bootstrap, bootstrap4" />

    <!-- Bootstrap CSS -->
    <link href="resources/bootstrap.min.css" rel="stylesheet" />
    <link
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"
      rel="stylesheet"
    />
    <link href="resources/tiny-slider.css" rel="stylesheet" />
    <link href="resources/style.css" rel="stylesheet" />
    <title>TapTapGo</title>
  </head>

  <body>
    <!-- Start Header/Navigation -->
    <nav
      class="custom-navbar navbar navbar navbar-expand-md navbar-dark bg-dark"
      arial-label="Furni navigation bar"
    >
      <div class="container">
        <a class="navbar-brand" href="/">Furni<span>.</span></a>

        <button
          class="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarsFurni"
          aria-controls="navbarsFurni"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarsFurni">
          <ul class="custom-navbar-nav navbar-nav ms-auto mb-2 mb-md-0">
            <li class="nav-item active">
              <a class="nav-link" href="/">Home</a>
            </li>
            <li><a class="nav-link" href="/products">Shop</a></li>
          </ul>

          <ul class="custom-navbar-cta navbar-nav mb-2 mb-md-0 ms-5">
            <li>
              <a class="nav-link" href="#"><img src="/resources/user.svg" /></a>
            </li>
            <li>
              <a class="nav-link" href="/cart"
                ><img src="/resources/cart.svg"
              /></a>
            </li>
          </ul>
        </div>
      </div>
    </nav>
    <!-- End Header/Navigation -->

    <!--Hero Section-->
    <div class="hero">
        <div class="container">
            <article className="content">
                <h1>The best mechanical<br />keyboards for you.</h1>
                <p>We were founded to support Concordians with our favourite obsession.
                    No import fees, long wait times for shipping, or foreign-currency
                    exchange rates. We offer simple pricing and free shipping in Montreal.
                </p>
                <p><a href="/products" class="btn btn-secondary me-2">Shop Now</a></p>
            </article>
            <article className="img-container">
                <img src="/resources/hero-bcg.jpeg" alt="green keyboard" className="main-img" />
            </article>
        </div>
    </div>
    <!-- End Hero Section -->

    <!-- Start Product Section -->
    <div class="product-section">
      <div class="container">
        <div class="row">
          <!-- Start Column 1 -->
          <div class="col-md-12 col-lg-3 mb-5 mb-lg-0">
            <h2 class="mb-4 section-title">Crafted with excellent material.</h2>
            <p class="mb-4">
              Donec vitae odio quis nisl dapibus malesuada. Nullam ac aliquet
              velit. Aliquam vulputate velit imperdiet dolor tempor tristique.
            </p>
            <p><a href="/products" class="btn">Explore</a></p>
          </div>
          <!-- End Column 1 -->

          <!-- Start Column 2 -->
          <div class="col-12 col-md-4 col-lg-3 mb-5 mb-md-0">
            <a class="product-item" href="cart.html">
              <img
                src="images/product-1.png"
                class="img-fluid product-thumbnail"
              />
              <h3 class="product-title">Nordic Chair</h3>
              <strong class="product-price">$50.00</strong>

              <span class="icon-cross">
                <img src="images/cross.svg" class="img-fluid" />
              </span>
            </a>
          </div>
          <!-- End Column 2 -->

          <!-- Start Column 3 -->
          <div class="col-12 col-md-4 col-lg-3 mb-5 mb-md-0">
            <a class="product-item" href="cart.html">
              <img
                src="images/product-2.png"
                class="img-fluid product-thumbnail"
              />
              <h3 class="product-title">Kruzo Aero Chair</h3>
              <strong class="product-price">$78.00</strong>

              <span class="icon-cross">
                <img src="images/cross.svg" class="img-fluid" />
              </span>
            </a>
          </div>
          <!-- End Column 3 -->

          <!-- Start Column 4 -->
          <div class="col-12 col-md-4 col-lg-3 mb-5 mb-md-0">
            <a class="product-item" href="cart.html">
              <img
                src="images/product-3.png"
                class="img-fluid product-thumbnail"
              />
              <h3 class="product-title">Ergonomic Chair</h3>
              <strong class="product-price">$43.00</strong>

              <span class="icon-cross">
                <img src="images/cross.svg" class="img-fluid" />
              </span>
            </a>
          </div>
          <!-- End Column 4 -->
        </div>
      </div>
    </div>
    <!-- End Product Section -->

    <!-- <script src="js/bootstrap.bundle.min.js"></script>
    <script src="js/tiny-slider.js"></script>
    <script src="js/custom.js"></script> -->
  </body>
</html>
