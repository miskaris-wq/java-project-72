package hexlet.code.tests;

import hexlet.code.App;
import hexlet.code.database.Database;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.BeforeAll;
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
