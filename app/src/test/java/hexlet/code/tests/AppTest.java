package hexlet.code.tests;

import hexlet.code.App;
import hexlet.code.Database;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import io.javalin.testtools.JavalinTest;
import okhttp3.FormBody;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class AppTest {

    private static UrlRepository repo;

    @BeforeAll
    static void beforeAll() throws Exception {
        Database.init();
        App.runMigrations(Database.getDataSource());
        repo = new UrlRepository(Database.getDataSource());
    }

    @BeforeEach
    void beforeEach() throws SQLException {
        try (var conn = Database.getDataSource().getConnection();
             var stmt = conn.createStatement()) {
            stmt.execute("TRUNCATE TABLE urls");
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

    @Test
    void testPostUrl() throws SQLException {
        var repo = new UrlRepository(Database.getDataSource());

        JavalinTest.test(App.getApp(), (server, client) -> {
            var formData = new FormBody.Builder()
                    .add("url", "https://newsite.com")
                    .build();

            var response = client.post("/urls", formData);

            // Проверяем, что сервер отвечает редиректом (302)
            assertThat(response.code()).isEqualTo(302);

            // Проверяем, что URL сохранился в базе
            var savedUrl = repo.findByName("https://newsite.com");
            assertThat(savedUrl).isPresent();

            // Проверяем, что новый URL появляется на странице списка
            var listResponse = client.get("/urls");
            assertThat(listResponse.code()).isEqualTo(200);
            assert listResponse.body() != null;
            assertThat(listResponse.body().string()).contains("https://newsite.com");
        });
    }


}
