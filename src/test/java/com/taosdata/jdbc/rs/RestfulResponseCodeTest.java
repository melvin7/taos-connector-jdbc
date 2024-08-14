package com.taosdata.jdbc.rs;

import com.taosdata.jdbc.TSDBErrorNumbers;
import com.taosdata.jdbc.utils.SpecifyAddress;
import org.junit.Assert;
import org.junit.Test;

import java.sql.*;

public class RestfulResponseCodeTest {
    private static final String host = "127.0.0.1";

    @Test
    public void cloudTest() {
        String url = System.getenv("TDENGINE_CLOUD_URL");
        if (url == null || "".equals(url.trim())) {
            System.out.println("Environment variable for CloudTest not set properly");
            return;
        }

        url = url.substring(0, url.length() - 1);
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {
            statement.executeQuery("select 1");
        } catch (SQLException e) {
            Assert.assertEquals(TSDBErrorNumbers.ERROR_CONNECTION_TIMEOUT, e.getErrorCode());
        }
    }

    @Test
    public void taosAdapterTest() throws SQLException {
        String url = SpecifyAddress.getInstance().getRestUrl();
        if (url == null) {
            url = "jdbc:TAOS-RS://" + host + ":6041/?user=root&password=taosdata";
        }
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("select 1");
            rs.next();
            Assert.assertEquals(1, rs.getInt(1));
        }
    }
}
