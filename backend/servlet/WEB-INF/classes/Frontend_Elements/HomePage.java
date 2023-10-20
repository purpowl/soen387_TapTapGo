package Frontend_Elements;

public class HomePage {
    Header header;
    NavBar nav;
    Hero hero;
    String content;

    public HomePage() {
        header = new Header();
        nav = new NavBar();
        hero = new Hero();

        this.content = "<html>\n"
                            + header.getHeader()
                            + "<body>\n"
                                + nav.getNavBar()
                                + hero.getHero()
                            + "</body>\n"
                        + "</html>";
    }


    public String getHomePage() {
        return this.content;
    }
}
