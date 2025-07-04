package hexlet.code.repository;

import hexlet.code.model.Url;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UrlRepository extends BaseRepository {

    public UrlRepository() {
        super(); // берет dataSource из BaseRepository.dataSource
    }

    public UrlRepository(DataSource dataSource) {
        super(dataSource); // можно при желании передавать вручную
    }

    public void save(Url url) throws SQLException {
        String sql = "INSERT INTO urls(name, created_at) VALUES (?, CURRENT_TIMESTAMP)";
        try (var connection = ds.getConnection(); // <--- используем ds, а не dataSource
             var stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, url.getName());
            stmt.executeUpdate();

            try (var generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long generatedId = generatedKeys.getLong(1);
                    url.setId(generatedId);
                }
            }
        }
    }

    public List<Url> findAll() throws SQLException {
        var sql = "SELECT * FROM urls ORDER BY id DESC";
        try (var conn = ds.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            var rs = stmt.executeQuery();
            var urls = new ArrayList<Url>();
            while (rs.next()) {
                urls.add(mapUrl(rs));
            }
            return urls;
        }
    }

    public Optional<Url> findByName(String name) throws SQLException {
        var sql = "SELECT * FROM urls WHERE name = ?";
        try (var conn = ds.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapUrl(rs));
            }
            return Optional.empty();
        }
    }

    public Optional<Url> findById(Long id) throws SQLException {
        var sql = "SELECT * FROM urls WHERE id = ?";
        try (var conn = ds.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapUrl(resultSet));
            }
            return Optional.empty();
        }
    }

    public List<Url> findAllOrderedById() throws SQLException {
        var sql = "SELECT * FROM urls ORDER BY id ASC";
        try (var conn = ds.getConnection();
             var stmt = conn.prepareStatement(sql);
             var rs = stmt.executeQuery()) {

            var urls = new ArrayList<Url>();
            while (rs.next()) {
                urls.add(mapUrl(rs));
            }
            return urls;
        }
    }

    private Url mapUrl(ResultSet rs) throws SQLException {
        Url url = new Url();
        url.setId(rs.getLong("id"));
        url.setName(rs.getString("name"));
        url.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return url;
    }
}
