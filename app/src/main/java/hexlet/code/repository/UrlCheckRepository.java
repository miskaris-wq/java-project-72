package hexlet.code.repository;

import hexlet.code.model.UrlCheck;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

public final class UrlCheckRepository extends BaseRepository {

    public UrlCheckRepository() {
        super();
    }

    public UrlCheckRepository(DataSource dataSource) {
        super(dataSource);
    }

    public Map<Long, UrlCheck> findLatestChecks() throws SQLException {
        var sql = "SELECT DISTINCT ON (url_id) * from url_checks order by url_id DESC, id DESC";
        try (var connection = ds.getConnection();
             var statement = connection.prepareStatement(sql)) {

            try (var rs = statement.executeQuery()) {
                var checks = new HashMap<Long, UrlCheck>();
                while (rs.next()) {
                    checks.put(rs.getLong("url_id"), mapCheck(rs));
                }
                return checks;
            }
        }
    }
    public List<UrlCheck> findAllByUrlId(Long urlId) throws SQLException {
        String sql = "SELECT * FROM url_checks WHERE url_id = ? ORDER BY created_at DESC";

        try (var connection = ds.getConnection();
             var statement = connection.prepareStatement(sql)) {

            statement.setLong(1, urlId);

            try (var rs = statement.executeQuery()) {
                var checks = new ArrayList<UrlCheck>();
                while (rs.next()) {
                    checks.add(mapCheck(rs));
                }
                return checks;
            }
        }
    }

    public Optional<UrlCheck> findLastCheckByUrlId(long urlId) throws SQLException {
        var sql = "SELECT * FROM url_checks WHERE url_id = ? ORDER BY created_at DESC LIMIT 1";
        try (var conn = ds.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, urlId);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapCheck(rs));
            }
            return Optional.empty();
        }
    }

    public void save(UrlCheck check) throws SQLException {
        var sql = "INSERT INTO url_checks (url_id, status_code, title, h1, description, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (var connection = ds.getConnection();
             var statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            var createdAt = LocalDateTime.now();
            statement.setLong(1, check.getUrlId());
            statement.setObject(2, check.getStatusCode(), Types.INTEGER);
            statement.setString(3, check.getTitle());
            statement.setString(4, check.getH1());
            statement.setString(5, check.getDescription());
            statement.setTimestamp(6, Timestamp.valueOf(createdAt));

            statement.executeUpdate();

            try (var generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    check.setId(generatedKeys.getLong(1));
                    check.setCreatedAt(createdAt);
                }
            }
        }
    }

    public List<UrlCheck> findByUrlId(long urlId) throws SQLException {
        String sql = "SELECT * FROM url_checks WHERE url_id = ? ORDER BY created_at DESC";

        try (var connection = ds.getConnection();
             var statement = connection.prepareStatement(sql)) {

            statement.setLong(1, urlId);
            var resultSet = statement.executeQuery();

            var checks = new ArrayList<UrlCheck>();

            while (resultSet.next()) {
                checks.add(mapCheck(resultSet));
            }

            return checks;
        }
    }

    public UrlCheck findLatestByUrlId(long urlId) throws SQLException {
        String sql = "SELECT * FROM url_checks WHERE url_id = ? ORDER BY created_at DESC LIMIT 1";

        try (var connection = ds.getConnection();
             var statement = connection.prepareStatement(sql)) {

            statement.setLong(1, urlId);
            var resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapCheck(resultSet);
            }

            return null;
        }
    }

    private UrlCheck mapCheck(ResultSet rs) throws SQLException {
        var check = new UrlCheck();
        check.setId(rs.getLong("id"));
        check.setUrlId(rs.getLong("url_id"));
        check.setStatusCode(rs.getInt("status_code"));
        check.setTitle(rs.getString("title"));
        check.setH1(rs.getString("h1"));
        check.setDescription(rs.getString("description"));
        var createdAt = rs.getTimestamp("created_at");
        check.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);
        return check;
    }
}
