package utils;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TestUtils {

    public static void addUrl(HikariDataSource ds, String url) throws SQLException {
        String sql = "INSERT INTO urls (name, created_at) VALUES (?, CURRENT_TIMESTAMP)";
        try (var conn = ds.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, url);
            stmt.executeUpdate();
        }
    }

    public static Map<String, Object> getUrlByName(HikariDataSource dataSource, String url) throws SQLException {
        String sql = "SELECT * FROM urls WHERE name = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, url);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("id", rs.getLong("id"));
                    result.put("name", rs.getString("name"));
                    return result;
                }
            }
        }
        return null;
    }

    public static void addUrlCheck(HikariDataSource dataSource, long urlId) throws SQLException {
        String sql = "INSERT INTO url_checks (url_id, status_code, title, description, h1, created_at) "
                + "VALUES (?, 200, 'Test page', 'statements of great people', "
                + "'Do not expect a miracle, miracles yourself!', "
                + "CURRENT_TIMESTAMP)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, urlId);
            stmt.executeUpdate();
        }
    }

    public static Map<String, Object> getUrlCheck(HikariDataSource dataSource, long urlId) throws SQLException {
        String sql = "SELECT * FROM url_checks WHERE url_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, urlId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("id", rs.getLong("id"));
                    result.put("url_id", rs.getLong("url_id"));
                    result.put("status_code", rs.getInt("status_code"));
                    result.put("title", rs.getString("title"));
                    result.put("h1", rs.getString("h1"));
                    result.put("description", rs.getString("description"));
                    return result;
                }
            }
        }
        return null;
    }
}

