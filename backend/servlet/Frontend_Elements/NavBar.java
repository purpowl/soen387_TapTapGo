package servlet.Frontend_Elements;

public class NavBar {
    private String content;

    public NavBar() {
        this.content = "<nav class=\"custom-navbar navbar navbar navbar-expand-md navbar-dark bg-dark\" arial-label=\"Taptapgo navigation bar\">\n"
                            + "<div class=\"container\">\n"
                                + "<a class=\"navbar-brand\" href=\"localhost:8080/taptapgo\">TapTapGo</a>\n"
                                + "<button class=\"navbar-toggler\" type=\"button\" data-bs-toggle=\"collapse\" data-bs-target=\"#navbarsTaptapgo\" aria-controls=\"navbarsTaptapgo\" aria-expanded=\"false\" aria-label=\"Toggle navigation\">\n"
                                    + "<span class=\"navbar-toggler-icon\"></span>\n"
                                + "</button>\n"
                                + "<div class=\"collapse navbar-collapse\" id=\"navbarsTaptapgo\">\n"
                                    + "<ul class=\"custom-navbar-nav navbar-nav ms-auto mb-2 mb-md-0\">\n"
                                        + "<li class=\"nav-item active\">\n"
                                            + "<a class=\"nav-link\" href=\"localhost:8080/taptapgo\">Home</a>\n"
                                        + "</li>\n"
                                        + "<li><a class=\"nav-link\" href=\"localhost:8080/taptapgo/products\">Shop</a></li>\n"
                                    + "</ul>\n"
                                    + "<ul class=\"custom-navbar-cta navbar-nav mb-2 mb-md-0 ms-5\">\n"
                                        + "<li><a class=\"nav-link\" href=\"\"><img src=\"images/user.svg\"></a></li>\n"
                                        + "<li><a class=\"nav-link\" href=\"localhost:8080/taptapgo/cart\"><img src=\"images/cart.svg\"></a></li>\n"
                                    + "</ul>\n"
                                + "</div>\n"
                            + "</div>\n"
                        + "</nav>\n";

    }

    public String getNavBar() {
        return this.content;
    }
}
