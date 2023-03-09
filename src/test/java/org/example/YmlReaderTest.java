package org.example;

import org.example.util.HttpParser;
import org.example.servlet.HttpServlet;
import org.example.servlet.HttpServletRequest;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.Proxy;
import java.util.*;
import java.util.stream.Collectors;

class YmlReaderTest {
    @Test
    void httpMessage() {
        String msg = """
                POST /echo/post/json HTTP/1.1
                Host: reqbin.com
                Content-Type: application/json
                Content-Length: 80
                                
                {
                  "Id": 12345,
                  "Customer": "John Smith",
                  "Quantity": 1,
                  "Price": 10.00
                }
                """;
        String msg2 = """
                POST /echo/post/form HTTP/1.1
                Host: reqbin.com
                Content-Type: application/x-www-form-urlencoded
                Content-Length: 23
                     
                key1=value1&key2=value2
                """;
        String getMsg = """
                GET /echo/get/json HTTP/1.1
                Host: reqbin.com
                Accept: application/json
                """;
        List<String> ss = msg.lines().collect(Collectors.toList());

        HttpServletRequest httpServletRequest = HttpServletRequest.of(msg2);

        System.out.println(httpServletRequest);
    }

    @Test
    void test() throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String packagePath = "org.example.servletImpl.";

        String path = "C:\\Users\\Administrator\\IdeaProjects\\webwas\\src\\main\\resources\\servlet.properties";

        Properties properties = new Properties();
        properties.load(new InputStreamReader(new FileInputStream(path)));
        Enumeration<Object> keys = properties.keys();

        while (keys.hasMoreElements()) {
            String key =(String) keys.nextElement();
            String property = properties.getProperty(key);

            System.out.println("key: " + key);
            System.out.println("property: " + property);
            Class<?> klass = Class.forName(packagePath + key);
            HttpServlet httpServlet = (HttpServlet) klass.getDeclaredConstructor().newInstance();
            System.out.println(httpServlet);
            break;
        }
    }

    @Test
    void makeMultiValueMap() {
        Map<String,List<String>> multiValueParams = new HashMap<>();
        System.out.println(multiValueParams);

        List<String> values = multiValueParams.getOrDefault("1", new ArrayList<String>());
        values.add("1212");
        multiValueParams.put("1", values);
        System.out.println(multiValueParams);

        List<String> value2 = multiValueParams.getOrDefault("1", new ArrayList<String>());
        value2.add("2323");
        multiValueParams.put("1", value2);
        System.out.println(multiValueParams);
    }

    @Test
    void object() {
        Object a = 1L;
        Object b = "123";

        System.out.println(a);
        System.out.println(b);
    }
}