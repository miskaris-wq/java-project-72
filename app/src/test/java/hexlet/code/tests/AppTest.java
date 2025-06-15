package hexlet.code.tests;

import hexlet.code.App;
import hexlet.code.database.Database;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class AppTest {

    private static UrlRepository repo;
    private static DataSource testDataSource;

    @BeforeAll
    static void setUp() throws Exception {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;");
        config.setUsername("sa");
        config.setPassword("");

        testDataSource = new HikariDataSource(config);

        Database.setDataSource(testDataSource);
        Database.init();
        App.runMigrations(testDataSource);

        repo = new UrlRepository(testDataSource);
    }

    @BeforeEach
    void clearTables() throws SQLException {
        try (var conn = testDataSource.getConnection();
             var stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM url_checks");
            stmt.executeUpdate("DELETE FROM urls");
        }
    }


    @Test
    void testMainPage() {
        JavalinTest.test(App.getApp(), (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Анализатор страниц");
        });
    }

    @Test
    void testListUrlsPage() throws SQLException {
        var url = new Url();
        url.setName("https://example.com");
        repo.save(url);

        JavalinTest.test(App.getApp(), (server, client) -> {
            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://example.com");
        });
    }

    @Test
    void testShowUrlPage() throws SQLException {
        var url = new Url();
        url.setName("https://hexlet.io");
        repo.save(url);

        JavalinTest.test(App.getApp(), (server, client) -> {
            var response = client.get("/urls/" + url.getId());
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://hexlet.io");
        });
    }
}
