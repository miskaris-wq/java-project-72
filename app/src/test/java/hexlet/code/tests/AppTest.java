package hexlet.code.tests;

import hexlet.code.App;
import hexlet.code.Database;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AppTest {

    private Javalin app;
    private DataSource dataSource;

    @BeforeEach
    void setUp() throws Exception {
        Database.init();
        dataSource = Database.getDataSource();
        App.class.getDeclaredMethod("runMigrations", DataSource.class).invoke(null, dataSource);
        app = App.getApp();
    }

    @Test
    void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
            assert response.body() != null;
            assertThat(response.body().string()).contains("ctx"); // Простая проверка рендера
        });
    }

    @Test
    void testListUrlsPage() throws SQLException {
        var repo = new UrlRepository(dataSource);
        var url = new Url();
        url.setName("https://example.com");
        repo.save(url);

        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
            assert response.body() != null;
            assertThat(response.body().string()).contains("https://example.com");
        });
    }

    @Test
    void testShowUrlPage() throws SQLException {
        var repo = new UrlRepository(dataSource);
        var url = new Url();
        url.setName("https://hexlet.io");
        repo.save(url);

        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/" + url.getId());
            assertThat(response.code()).isEqualTo(200);
            assert response.body() != null;
            assertThat(response.body().string()).contains("https://hexlet.io");
        });
    }

    @Test
    void testPostUrlAndRedirect() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.post("/urls", "url=https://junit.org");
            assertThat(response.code()).isEqualTo(302);
            assertThat(response.headers("Location")).contains("/urls");

            // Проверка, что URL добавлен
            var repo = new UrlRepository(dataSource);
            List<Url> urls = repo.findAll();
            assertThat(urls).anyMatch(u -> u.getName().equals("https://junit.org"));
        });
    }

    @Test
    void testAddInvalidUrl() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.post("/urls", "url=ht@tp://bad-url");
            assertThat(response.code()).isEqualTo(302);
            assertThat(response.headers("Location")).contains("/");

            // Ошибка не должна сохраняться в БД
            var repo = new UrlRepository(dataSource);
            var urls = repo.findAll();
            assertThat(urls).isEmpty();
        });
    }
}

