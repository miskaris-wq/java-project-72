import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import hexlet.code.App;

import java.io.File;
import java.nio.file.Files;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public final class RootControllerTest {

    private Javalin app;
    private HikariDataSource dataSource;

    @BeforeEach
    void setUp() throws Exception {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        dataSource = new HikariDataSource(hikariConfig);

        System.setProperty("JDBC_DATABASE_URL", hikariConfig.getJdbcUrl());

        var schema = RootControllerTest.class.getClassLoader().getResource("schema.sql");
        var file = new File(schema.getFile());
        var sql = Files.lines(file.toPath()).collect(Collectors.joining("\n"));

        try (var conn = dataSource.getConnection();
             var stmt = conn.createStatement()) {
            stmt.execute(sql);
        }

        app = App.getApp();

        app.get("/test-error", ctx -> {
            ctx.sessionAttribute("error", "Что-то пошло не так");
            ctx.redirect("/");
        });
    }

    @AfterEach
    void tearDown() {
        dataSource.close();
    }

    @Test
    void testWelcomePage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assert response.body() != null;
            var body = response.body().string();

            assertThat(response.code()).isEqualTo(200);
            assertThat(body).contains("Анализатор страниц");
        });
    }

}
