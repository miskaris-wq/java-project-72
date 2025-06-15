package hexlet.code.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

public class Database {
    private static HikariDataSource dataSource;

    public static void init() throws SQLException, IOException {
        var hikariConfig = new HikariConfig();

        String jdbcUrl = System.getenv().getOrDefault("JDBC_DATABASE_URL",
                "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;");

        if (jdbcUrl.startsWith("jdbc:h2:")) {
            hikariConfig.setJdbcUrl(jdbcUrl);
            hikariConfig.setUsername("sa");
            hikariConfig.setPassword("");
        } else {
            hikariConfig.setJdbcUrl(jdbcUrl);
            hikariConfig.setMaximumPoolSize(10);
        }

        dataSource = new HikariDataSource(hikariConfig);

        // Test the connection
        try (var connection = dataSource.getConnection()) {
            if (!connection.isValid(1000)) {
                throw new SQLException("Failed to establish database connection");
            }
        }
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

    public static void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
