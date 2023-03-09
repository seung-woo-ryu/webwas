package org.example;

import org.example.config.CustomConfig;
import org.example.servlet.HttpServletRequest;
import org.example.servlet.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Tomcat {
    private CustomConfig serverConfig;
    private static final String SERVER_PROPERTIES = "server.properties";
    private static final String SERVLET_PROPERTIES = "servlet.properties";
    private static final String PORT = "port";
    private static final String BUFFER_SIZE = "bufferSize";
    private int port;
    private int bufferSize;
    private final ServletContainer servletContainer;
    private final Map<String, Object> serverConfigMaps;
    public Tomcat() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        this.servletContainer = new ServletContainer(new CustomConfig(SERVLET_PROPERTIES));
        this.serverConfig = new CustomConfig(SERVER_PROPERTIES);
        this.serverConfigMaps = new HashMap<>();

        init();
    }

    private void init() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        initProperties();
        initOption();
    }

    private void initProperties() {
        Properties properties = serverConfig.getProperties();
        Enumeration<Object> keys = properties.keys();

        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            setPropertyToMap(key, (String) properties.get(key));
        }
    }

    private void setPropertyToMap(String key, String value) {
        try {
            this.serverConfigMaps.put(key, Integer.parseInt(value));
        } catch (NumberFormatException e) {
            this.serverConfigMaps.put(key, value);
        }
    }

    private void initOption() {
        this.port = (int) this.serverConfigMaps.get(PORT);
        this.bufferSize = (int) this.serverConfigMaps.get(BUFFER_SIZE);
    }


    public void start() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(true); // 블로킹 방식
        serverSocketChannel.bind(new InetSocketAddress(this.port));

        int n = 1;
        while (true) {
            SocketChannel socketChannel = serverSocketChannel.accept(); // 이 부분에서 연결이 될때딱지 블로킹
            InetSocketAddress inetSocketAddress = (InetSocketAddress) socketChannel.getRemoteAddress();
            StringBuilder sb = new StringBuilder();

            System.out.println("!!!!!!!!!!!!!!");
            System.out.println(n++);
            System.out.println("Connected : " + inetSocketAddress.getHostName());

            // Client로부터 글자 받기
            // todo: buffer 사이즈 초과하는 request message 받기
            ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);
            while(socketChannel.read(byteBuffer)>0){
                sb.append(StandardCharsets.UTF_8.decode(byteBuffer).toString());
            }

            byteBuffer.flip();

            String httpRequest = StandardCharsets.UTF_8.decode(byteBuffer).toString();
            System.out.println("Received Data \n" + httpRequest);

            HttpServletResponse httpServletResponse = new HttpServletResponse();
            HttpServletRequest httpServletRequest = HttpServletRequest.of(httpRequest);

            servletContainer.delegateRequest(httpServletRequest, httpServletResponse);
            System.out.println("send data \n" + httpServletResponse.toResponseMsg());

            byteBuffer = StandardCharsets.UTF_8.encode(httpServletResponse.toResponseMsg());
            socketChannel.write(byteBuffer);

            System.out.println("Sending Success");
            socketChannel.close();
        }
    }
}
