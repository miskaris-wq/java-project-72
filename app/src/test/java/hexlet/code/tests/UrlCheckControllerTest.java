package hexlet.code.tests;

import static org.assertj.core.api.Assertions.assertThat;

import hexlet.code.App;
import hexlet.code.database.Database;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import kong.unirest.Unirest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;

import java.sql.SQLException;
import java.util.List;

import io.javalin.Javalin;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UrlCheckControllerTest {

    private Javalin app;
    private MockWebServer mockWebServer;
    private UrlRepository urlRepository;
    private UrlCheckRepository urlCheckRepository;

    @BeforeAll
    void beforeAll() throws Exception {
        Database.init();
        App.runMigrations(Database.getDataSource());
        try (var conn = Database.getDataSource().getConnection();
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery("SHOW TABLES")) {
            while (rs.next()) {
                System.out.println("TABLE: " + rs.getString(1));
            }
        }
        app = App.getApp();
        app.start(7070);

        urlRepository = new UrlRepository(Database.getDataSource());
        urlCheckRepository = new UrlCheckRepository(Database.getDataSource());

        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    void afterAll() throws Exception {
        mockWebServer.shutdown();
        app.stop();
    }

    @BeforeEach
    void beforeEach() throws SQLException {
        try (var conn = Database.getDataSource().getConnection();
             var stmt1 = conn.createStatement();
             var stmt2 = conn.createStatement()) {
            stmt1.execute("DELETE FROM url_checks");
            stmt2.execute("DELETE FROM urls");
        }
    }

    @Test
    void testSuccessfulUrlCheck() throws Exception {
        // Добавляем URL с адресом, который будет возвращать mockWebServer
        String testUrl = mockWebServer.url("/").toString();
        var url = new Url();
        url.setName(testUrl);
        urlRepository.save(url);

        // Подготавливаем mock-ответ
        String body = "<html><head><title>Test Title</title><meta name=\"description\" "
                + "content=\"Test description\"></head>"
                + "<body><h1>Test H1 Header</h1></body></html>";
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(body));

        // Выполняем POST-запрос на /urls/{id}/checks
        var response = Unirest.post("http://localhost:7070/urls/" + url.getId() + "/checks")
                .asEmpty();

        // Проверяем, что редирект был успешным (HTTP 302)
        assertThat(response.getStatus()).isEqualTo(302);

        // Проверяем, что в базе появилась запись проверки для этого URL
        List<UrlCheck> checks = urlCheckRepository.findAllByUrlId(url.getId());
        assertThat(checks).hasSize(1);

        UrlCheck check = checks.get(0);
        assertThat(check.getStatusCode()).isEqualTo(200);
        assertThat(check.getTitle()).isEqualTo("Test Title");
        assertThat(check.getH1()).isEqualTo("Test H1 Header");
        assertThat(check.getDescription()).isEqualTo("Test description");
        assertThat(check.getCreatedAt()).isNotNull();
    }

    @Test
    void testUrlCheckNotFoundUrl() throws Exception {
        // Пробуем сделать проверку для несуществующего URL
        long fakeId = 9999L;

        var response = Unirest.post("http://localhost:7070/urls/" + fakeId + "/checks")
                .asString();

        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getBody()).contains("URL not found");
    }

    @Test
    void testUrlCheckWithServerError() throws Exception {
        // 1. Добавляем URL с адресом mock-сервера
        String testUrl = mockWebServer.url("/").toString();
        var url = new Url();
        url.setName(testUrl);
        urlRepository.save(url);

        // 2. Настраиваем mock-ответ с ошибкой 500
        mockWebServer.enqueue(new MockResponse().setResponseCode(500).setBody("Internal Server Error"));

        // 3. Отправляем запрос на проверку URL
        var response = Unirest.post("http://localhost:7070/urls/" + url.getId() + "/checks")
                .asEmpty();

        // 4. Проверяем редирект (302)
        assertThat(response.getStatus()).isEqualTo(302);

        // 5. Проверяем, что запись ВСЁ РАВНО создана (изменили ожидание!)
        List<UrlCheck> checks = urlCheckRepository.findAllByUrlId(url.getId());
        assertThat(checks).hasSize(1);  // Теперь ожидаем 1 запись, а не 0

        // 6. Проверяем, что в записи сохранён статус 500
        UrlCheck check = checks.get(0);
        assertThat(check.getStatusCode()).isEqualTo(500);
        assertThat(check.getTitle()).isNullOrEmpty();  // Для ошибок title может быть null
        assertThat(check.getH1()).isNullOrEmpty();    // или пустым
        assertThat(check.getDescription()).isNullOrEmpty();
    }
}
