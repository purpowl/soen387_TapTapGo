package servlet.Frontend_Elements;

public class Hero {
    private String content;

    public Hero() {
        this.content = "<div class=\"hero\">\n"
                            + "<div class=\"container\">\n"
                                + "<article className=\"content\">\n"
                                    + "<h1>The best mechanical<br />keyboards for you.</h1>\n"
                                    + "<p>We were founded to support Concordians with our favourite obsession.\n" + //
                                            "No import fees, long wait times for shipping, or foreign-currency\n" + //
                                            "exchange rates. We offer simple pricing and free shipping in Montreal.\n" + //
                                        "</p>\n"
                                    + "<p><a href=\"localhost:8080/taptapgo/products\" class=\"btn btn-secondary me-2\">Shop Now</a></p>\n"
                                + "</article>\n"
                                + "<article className=\"img-container\">\n"
                                    + "<img src=\"images/hero-bcg.jpeg\" alt=\"green keyboard\" className=\"main-img\" />\n"
                                + "</article>\n"
                            + "</div>\n"
                        + "</div>\n";
    }

    public String getHero() {
        return this.content;
    }
}
