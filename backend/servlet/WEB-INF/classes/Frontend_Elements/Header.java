package Frontend_Elements;

/**
 * Header content for HTML returned
 */
public class Header {
    private String content;

    public Header(){
        this.content = "<head>\n"
                            + "<meta charset=\"UTF-8\">\n"
                            + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                            + "<title>TapTapGo</title>\n"
                        + "</head>\n";
    }

    public String getHeader() {
        return this.content;
    }
}
