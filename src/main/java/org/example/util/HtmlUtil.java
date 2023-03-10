package org.example.util;

public class HtmlUtil {
    private static final String NEW_LINE = "\n";
    private static final String TAP = "\t";
    private HtmlUtil() {
    }

    public static final String getHtml(String body) {
        StringBuilder builder = new StringBuilder();

        return builder
                .append("<html>"+NEW_LINE)
                .append(TAP+"<body>"+NEW_LINE)
                .append(TAP+TAP+"<div>"+NEW_LINE)
                .append(body)
                .append(NEW_LINE+TAP+TAP+"</div>"+NEW_LINE)
                .append(TAP+"</body>"+NEW_LINE)
                .append("</html>"+NEW_LINE)
                .toString();
    }
}
