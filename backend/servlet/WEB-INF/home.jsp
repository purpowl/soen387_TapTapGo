<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link href="/resources/bootstrap.min.css" rel="stylesheet" />
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet"/>
        <title>TapTapGo</title>
    </head>
    <body>
        <!--Navigation Bar-->
        <nav class="custom-navbar navbar navbar navbar-expand-md navbar-dark bg-dark" arial-label="Taptapgo navigation bar">
            <div class="container">
                <a class="navbar-brand" href="/">TapTapGo</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarsTaptapgo" aria-controls="navbarsTaptapgo" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarsTaptapgo">
                    <ul class="custom-navbar-nav navbar-nav ms-auto mb-2 mb-md-0">
                        <li class="nav-item active">
                            <a class="nav-link" href="/">Home</a>
                        </li>
                        <li><a class="nav-link" href="/products">Shop</a></li>
                    </ul>
                    <ul class="custom-navbar-cta navbar-nav mb-2 mb-md-0 ms-5">
                        <li><a class="nav-link" href=""><img src="/resources/user.svg"></a></li>
                        <li><a class="nav-link" href="/cart"><img src="/resources/cart.svg"></a></li>
                    </ul>
                </div>
            </div>
        </nav>

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
    </body>
</html>
