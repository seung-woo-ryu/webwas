package org.example.servlet;

public class HttpServlet implements Servlet {
    private static final String METHOD_GET = "GET";
    private static final String METHOD_OPTIONS = "OPTIONS";
    private static final String METHOD_POST = "POST";
    public void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        //todo: 에러 페이지 반환
        httpServletResponse.setStatusCode("404");
        httpServletResponse.setStatusMsg("resource not found");
    };
    public void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        //todo: 에러 페이지 반환
        httpServletResponse.setStatusCode("404");
        httpServletResponse.setStatusMsg("resource not found");
    };
    public void doOptions(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        //todo: 에러 페이지 반환
        httpServletResponse.setStatusCode("404");
        httpServletResponse.setStatusMsg("resource not found");
    };
    @Override
    public void init(){};
    @Override
    public void destroy(){};

    @Override
    public void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws RuntimeException{
        String method = httpServletRequest.getMethod();
        httpServletResponse.setHttpVersion(httpServletRequest.getHttpVersion());
        try {
            if (METHOD_GET.equals(method)) {
                doGet(httpServletRequest, httpServletResponse);
            } else if (METHOD_POST.equals(method)) {
                doPost(httpServletRequest, httpServletResponse);
            } else if (METHOD_OPTIONS.equals(method)) {
                doOptions(httpServletRequest, httpServletResponse);
            } else {
                throw new IllegalArgumentException("non-existent method");
            }

            httpServletResponse.setStatusCode("200");
            httpServletResponse.setStatusMsg("OK");
        } catch (Exception e) {
            throw new RuntimeException("should handle that exception");
        }

    }
}
