package org.example.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class CustomConfig {
    private static final String BASE = "C:\\Users\\Administrator\\IdeaProjects\\webwas\\src\\main\\resources\\";
    private Properties properties;

    public CustomConfig(String configFileName) throws IOException {
        this.properties = new Properties();
        this.properties.load(new InputStreamReader(new FileInputStream(BASE + configFileName)));
    }

    public Properties getProperties() {
        return this.properties;
    }
}
