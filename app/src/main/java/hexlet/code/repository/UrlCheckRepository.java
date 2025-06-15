package hexlet.code.repository;

import hexlet.code.model.UrlCheck;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UrlCheckRepository {
    private final DataSource dataSource;

    public UrlCheckRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<UrlCheck> findAllByUrlId(Long urlId) throws SQLException {
        String sql = "SELECT * FROM url_checks WHERE url_id = ? ORDER BY created_at DESC";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, urlId);

            try (ResultSet rs = statement.executeQuery()) {
                List<UrlCheck> checks = new ArrayList<>();
                while (rs.next()) {
                    UrlCheck check = new UrlCheck();
                    check.setId(rs.getLong("id"));
                    check.setUrlId(rs.getLong("url_id"));
                    check.setStatusCode(rs.getInt("status_code"));
                    check.setTitle(rs.getString("title"));
                    check.setH1(rs.getString("h1"));
                    check.setDescription(rs.getString("description"));
                    check.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    checks.add(check);
                }
                return checks;
            }
        }
    }

    public Optional<UrlCheck> findLastCheckByUrlId(long urlId) throws SQLException {
        var sql = "SELECT * FROM url_checks WHERE url_id = ? ORDER BY created_at DESC LIMIT 1";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, urlId);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    var check = new UrlCheck();
                    check.setId(rs.getLong("id"));
                    check.setUrlId(rs.getLong("url_id"));
                    check.setStatusCode(rs.getInt("status_code"));
                    check.setTitle(rs.getString("title"));
                    check.setH1(rs.getString("h1"));
                    check.setDescription(rs.getString("description"));
                    check.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    return Optional.of(check);
                }
            }
        }
        return Optional.empty();
    }


    public void save(UrlCheck check) throws SQLException {
        String sql = """
            INSERT INTO url_checks (url_id, status_code, title, h1, description, created_at)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, check.getUrlId());
            statement.setObject(2, check.getStatusCode(), Types.INTEGER);
            statement.setString(3, check.getTitle());
            statement.setString(4, check.getH1());
            statement.setString(5, check.getDescription());
            statement.setTimestamp(6, Timestamp.valueOf(check.getCreatedAt()));

            statement.executeUpdate();

            try (var generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    check.setId(generatedKeys.getLong(1));
                }
            }
        }
    }

    public List<UrlCheck> findByUrlId(long urlId) throws SQLException {
        String sql = "SELECT * FROM url_checks WHERE url_id = ? ORDER BY created_at DESC";

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(sql)) {

            statement.setLong(1, urlId);
            var resultSet = statement.executeQuery();

            var checks = new ArrayList<UrlCheck>();

            while (resultSet.next()) {
                var check = new UrlCheck();
                check.setId(resultSet.getLong("id"));
                check.setUrlId(resultSet.getLong("url_id"));
                check.setStatusCode(resultSet.getInt("status_code"));
                check.setTitle(resultSet.getString("title"));
                check.setH1(resultSet.getString("h1"));
                check.setDescription(resultSet.getString("description"));
                check.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());

                checks.add(check);
            }

            return checks;
        }
    }

    public UrlCheck findLatestByUrlId(long urlId) throws SQLException {
        String sql = """
            SELECT * FROM url_checks
            WHERE url_id = ?
            ORDER BY created_at DESC
            LIMIT 1
            """;

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(sql)) {

            statement.setLong(1, urlId);
            var resultSet = statement.executeQuery();

            if (resultSet.next()) {
                var check = new UrlCheck();
                check.setId(resultSet.getLong("id"));
                check.setUrlId(resultSet.getLong("url_id"));
                check.setStatusCode(resultSet.getInt("status_code"));
                check.setTitle(resultSet.getString("title"));
                check.setH1(resultSet.getString("h1"));
                check.setDescription(resultSet.getString("description"));
                check.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());

                return check;
            }

            return null;
        }
    }
}
