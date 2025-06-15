package hexlet.code.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

public class Database {
    @Getter
    private static DataSource dataSource;
    private static boolean customSourceSet = false;

    public static void init() throws SQLException, IOException {
        if (customSourceSet) {
            return; // если в тесте уже установили dataSource — не трогаем
        }

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

        var hikariDataSource = new HikariDataSource(hikariConfig);

        try (var connection = hikariDataSource.getConnection()) {
            if (!connection.isValid(1000)) {
                throw new SQLException("Failed to establish database connection");
            }
        }

        dataSource = hikariDataSource;
    }

    public static void setDataSource(DataSource customDataSource) {
        dataSource = customDataSource;
        customSourceSet = true;
    }

    public static void close() {
        if (dataSource instanceof HikariDataSource hikariSource) {
            hikariSource.close();
        }
    }
}
