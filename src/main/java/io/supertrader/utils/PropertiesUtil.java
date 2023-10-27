package io.supertrader.utils;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {
    private static final Properties properties = new Properties();

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try {
            properties.load(PropertiesUtil.class.getClassLoader().getResourceAsStream("app.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

}
