package org.example;

import org.example.config.CustomConfig;
import org.example.servlet.HttpServlet;
import org.example.servlet.HttpServletRequest;
import org.example.servlet.HttpServletResponse;
import org.example.util.HtmlUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ServletContainer {
    private Map<String, HttpServlet> servletMap;
    private static final String PACKAGE_PATH = "org.example.servletImpl.";
    public ServletContainer(CustomConfig servletConfig) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        this.servletMap = new HashMap<>();

        init(servletConfig);
    }

    private void init(CustomConfig servletConfig) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Properties properties = servletConfig.getProperties();

        Enumeration<Object> ServletNames = properties.keys();

        while (ServletNames.hasMoreElements()) {
            String ServletName =(String) ServletNames.nextElement();
            String url = properties.getProperty(ServletName);

            System.out.println("load: " + PACKAGE_PATH + ServletName);
            Class<?> klass = Class.forName(PACKAGE_PATH + ServletName);
            HttpServlet httpServlet = (HttpServlet) klass.getDeclaredConstructor().newInstance();

            servletMap.put(url, httpServlet);
        }
    }

    public void delegateRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try{
            HttpServlet httpServlet = getHttpServlet(httpServletRequest);
            httpServlet.service(httpServletRequest,httpServletResponse);
        } catch(RuntimeException e){
            sendErrPage(httpServletRequest, httpServletResponse, e);
        }
    }

    private static void sendErrPage(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, RuntimeException e) {
        httpServletResponse.setHttpVersion(httpServletRequest.getHttpVersion());
        httpServletResponse.setStatusCode("40x");
        httpServletResponse.setStatusCode("error");
        httpServletResponse.setResponseBody(HtmlUtil.getHtmlWithBody("<h1>"+ e.toString()+" </h1>"));
    }

    private HttpServlet getHttpServlet(HttpServletRequest httpServletRequest) throws RuntimeException{
        try {
            String requestTarget = httpServletRequest.getRequestTarget();
            return servletMap.get(requestTarget);
        } catch (Exception e) {
            throw new RuntimeException("does not find servlet");
        }

    }
}
