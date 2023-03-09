package org.example.servlet;

public interface Servlet {
    public abstract void init();

    public abstract void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

    public abstract void destroy();

}
