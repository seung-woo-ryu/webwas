package org.example.servlet;

import org.example.util.HttpParser;

import java.util.List;
import java.util.Map;

public class HttpServletRequest {
    private String method;
    private String requestTarget;
    private String httpVersion;
    private Map<String, Object> headers;
    private String body;
    private Map<String, List<String>> params;

    public HttpServletRequest() {
    }

    public static HttpServletRequest of(String req) {
        HttpServletRequest httpServletRequest = new HttpServletRequest();
        HttpParser.parse(req, httpServletRequest);
        return httpServletRequest;
    }


    public void setMethod(String method) {
        this.method = method;
    }

    public void setRequestTarget(String requestTarget) {
        this.requestTarget = requestTarget;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

    public void setBody(String fullBody) {
        this.body = fullBody;
    }

    public String getMethod() {
        return method;
    }

    public String getRequestTarget() {
        return requestTarget;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }
    @Override
    public String toString() {
        return "HttpServletRequest{" +
                "method='" + method + '\'' +
                ", requestTarget='" + requestTarget + '\'' +
                ", httpVersion='" + httpVersion + '\'' +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }

    public void setParams(Map<String, List<String>> params) {
        this.params = params;
    }

    public Map<String, List<String>> getParams() {
        return params;
    }
}
