package org.example;

import org.example.servlet.HttpServletRequest;
import org.example.servlet.HttpServletResponse;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class ThreadWorker implements Runnable{
    private final SocketChannel socketChannel;
    private final ServletContainer servletContainer;
    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;

    public ThreadWorker(SocketChannel socketChannel, ServletContainer servletContainer, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        this.socketChannel = socketChannel;
        this.servletContainer = servletContainer;
        this.httpServletRequest = httpServletRequest;
        this.httpServletResponse = httpServletResponse;
    }

    @Override
    public void run() {
        try {
            servletContainer.delegateRequest(httpServletRequest, httpServletResponse);

            Logger.info("send data \n" + httpServletResponse.toResponseMsg());

            socketChannel.write(StandardCharsets.UTF_8.encode(httpServletResponse.toResponseMsg()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socketChannel.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
