package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

import javax.sql.DataSource;
import java.util.Optional;

public final class Database {
    @Getter
    private static DataSource dataSource;

    public static void init() {
        HikariConfig config = new HikariConfig();
        String jdbcUrl = Optional.ofNullable(System.getenv("JDBC_DATABASE_URL"))
                .orElse("jdbc:h2:mem:project;DB_CLOSE_DELAY=-1");

        config.setJdbcUrl(jdbcUrl);
        dataSource = new HikariDataSource(config);
    }

}
