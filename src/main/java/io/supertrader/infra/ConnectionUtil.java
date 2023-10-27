package io.supertrader.infra;

import io.supertrader.utils.PropertiesUtil;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConnectionUtil {
    private static final String USERNAME_PROPERTY = "DB_USERNAME";
    private static final String PASSWORD_PROPERTY = "DB_PASSWORD";
    private static final String URL_PROPERTY = "DB_URL";

    private static BlockingQueue<Connection> pool;

    static {
        initConnectionPool();
    }

    public static Connection get() {
        try {
            return pool.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void initConnectionPool() {
        pool = new ArrayBlockingQueue<>(20);

        for (int i = 0; i < 20; i++) {
            pool.add(getConnection());
        }
    }

    private static Connection getConnection() {
        try {
            return DriverManager.getConnection(PropertiesUtil.get(URL_PROPERTY),
                    PropertiesUtil.get(USERNAME_PROPERTY),
                    PropertiesUtil.get(PASSWORD_PROPERTY));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
