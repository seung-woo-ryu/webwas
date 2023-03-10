package org.example.servletImpl;

import org.example.servlet.HttpServlet;
import org.example.servlet.HttpServletRequest;
import org.example.servlet.HttpServletResponse;
import org.example.util.HtmlUtil;

import java.util.List;
import java.util.Map;

public class MainServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        StringBuilder builder = getContentBuilder(httpServletRequest);

        httpServletResponse.setResponseBody(HtmlUtil.getHtml("GET\n"+builder.toString()));
    }

    @Override
    public void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        httpServletResponse.setResponseBody(HtmlUtil.getHtml("POST\n"+httpServletRequest.getBody()));
    }

    private StringBuilder getContentBuilder(HttpServletRequest httpServletRequest) {
        StringBuilder builder = new StringBuilder();

        Map<String, List<String>> params = httpServletRequest.getParams();
        if (!params.isEmpty()) {
            for (String key: params.keySet() ) {
                for (String value : params.get(key)) {
                    builder.append(key).append("=").append(value).append("\n");
                }
            }
        }
        return builder;
    }
}
