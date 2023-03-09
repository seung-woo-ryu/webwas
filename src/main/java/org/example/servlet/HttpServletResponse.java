package org.example.servlet;

import java.util.Map;
import java.util.Optional;

public class HttpServletResponse {
    private String httpVersion;
    private String statusCode;
    private String statusMsg;
    private Map<String, Object> headers;
    private String responseBody;
    private static final String START_LINE_DELIMITER = " ";
    private static final String NEW_LINE = "\n";
    private static final String HEADER_DELIMITER = ": ";


    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public String toResponseMsg() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
                .append(httpVersion)
                .append(START_LINE_DELIMITER)
                .append(statusCode)
                .append(START_LINE_DELIMITER)
                .append(statusMsg)
                .append(NEW_LINE);

        if(existHeaders(headers)){
            headers.forEach((k,v)->{
                stringBuilder
                        .append(k)
                        .append(HEADER_DELIMITER)
                        .append(v)
                        .append(NEW_LINE);
            });
        }

        Optional.ofNullable(responseBody).ifPresent(body->{
            stringBuilder
                    .append(NEW_LINE)
                    .append(responseBody)
                    .append(NEW_LINE);
        });

        return stringBuilder.toString();
    }

    private boolean existHeaders(Map<String, Object> headers) {
        return !(headers == null) && !headers.isEmpty();
    }


    @Override
    public String toString() {
        return "HttpServletResponse{" +
                "httpVersion='" + httpVersion + '\'' +
                ", StatusCode='" + statusCode + '\'' +
                ", statusMsg='" + statusMsg + '\'' +
                ", headers=" + headers +
                ", responseBody='" + responseBody + '\'' +
                '}';
    }
}
