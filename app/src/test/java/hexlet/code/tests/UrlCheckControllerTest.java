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
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;

import java.sql.SQLException;
import java.util.List;

import io.javalin.Javalin;

import javax.sql.DataSource;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UrlCheckControllerTest {

    private Javalin app;
    private MockWebServer mockWebServer;
    private UrlRepository urlRepository;
    private UrlCheckRepository urlCheckRepository;
    private DataSource h2DataSource;

    @BeforeAll
    void beforeAll() throws Exception {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        ds.setUser("sa");
        ds.setPassword("");

        this.h2DataSource = ds;

        Database.setDataSource(h2DataSource);
        Database.init();

        App.runMigrations(h2DataSource);

        urlRepository = new UrlRepository(h2DataSource);
        urlCheckRepository = new UrlCheckRepository(h2DataSource);

        app = App.getApp();
        app.start(7070);

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
        String testUrl = mockWebServer.url("/").toString();
        var url = new Url();
        url.setName(testUrl);
        urlRepository.save(url);

        String body = "<html><head><title>Test Title</title><meta name=\"description\" "
                + "content=\"Test description\"></head>"
                + "<body><h1>Test H1 Header</h1></body></html>";
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(body));

        var response = Unirest.post("http://localhost:7070/urls/" + url.getId() + "/checks")
                .asEmpty();

        assertThat(response.getStatus()).isEqualTo(302);

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
        long fakeId = 9999L;

        var response = Unirest.post("http://localhost:7070/urls/" + fakeId + "/checks")
                .asString();

        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getBody()).contains("URL not found");
    }

    @Test
    void testUrlCheckWithServerError() throws Exception {
        String testUrl = mockWebServer.url("/").toString();
        var url = new Url();
        url.setName(testUrl);
        urlRepository.save(url);

        mockWebServer.enqueue(new MockResponse().setResponseCode(500).setBody("Internal Server Error"));

        var response = Unirest.post("http://localhost:7070/urls/" + url.getId() + "/checks")
                .asEmpty();

        assertThat(response.getStatus()).isEqualTo(302);

        List<UrlCheck> checks = urlCheckRepository.findAllByUrlId(url.getId());
        assertThat(checks).hasSize(1);

        UrlCheck check = checks.get(0);
        assertThat(check.getStatusCode()).isEqualTo(500);
        assertThat(check.getTitle()).isNullOrEmpty();
        assertThat(check.getH1()).isNullOrEmpty();
        assertThat(check.getDescription()).isNullOrEmpty();
    }
}
