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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final String SERVER_PROPERTIES = "server.properties";
    private static final String SERVLET_PROPERTIES = "servlet.properties";
    private static final String PORT = "port";
    private static final String BUFFER_SIZE = "buffer_size";
    private static final String POOL_SIZE = "pool_size";
    private int port;
    private int bufferSize;
    private int poolSize;
    private final ServletContainer servletContainer;
    private final Map<String, Object> serverConfigMaps;
    private CustomConfig serverConfig;
    public Server() throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
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
        this.poolSize = (int) this.serverConfigMaps.get(POOL_SIZE);
    }


    public void start() throws IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(this.poolSize);

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(true);
        serverSocketChannel.bind(new InetSocketAddress(this.port));

        try {
            run(executorService,serverSocketChannel);
        } catch (IOException e) {
            Logger.info("Error Msg: " + e.toString());
            Logger.info("Server is down" + e.toString());
        }
    }

    private void run(ExecutorService executorService, ServerSocketChannel serverSocketChannel) throws IOException{
        while (!executorService.isShutdown()) {
            Logger.info("소켓 연결 대기 중입니다------------------------------");
            SocketChannel socketChannel = serverSocketChannel.accept();
            Logger.info("소켓 연결 되었습니다------------------------------");
            InetSocketAddress inetSocketAddress = (InetSocketAddress) socketChannel.getRemoteAddress();

            Logger.info("Connected : " + inetSocketAddress.getHostName());

            ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);
            socketChannel.read(byteBuffer);

            byteBuffer.flip();

            String httpRequest = StandardCharsets.UTF_8.decode(byteBuffer).toString();

            Logger.info("Received Data \n" + httpRequest);

            try {
                executorService.execute(new ThreadWorker(socketChannel,servletContainer,HttpServletRequest.of(httpRequest),new HttpServletResponse()));
            } catch (Exception e) {
                Logger.info("Server.run error");
                e.printStackTrace();
                executorService.shutdown();
            }
        }
    }
}



