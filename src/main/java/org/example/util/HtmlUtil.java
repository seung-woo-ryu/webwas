package org.example.util;

public class HtmlUtil {
    private HtmlUtil() {
    }

    public static final String getHtmlWithBody(String body) {
        StringBuilder builder = new StringBuilder();

        return builder
                .append("<html>")
                .append("<body>")
                .append("<div>")
                .append(body)
                .append("</div>")
                .append("</body>")
                .append("</html>")
                .toString();
    }
}
