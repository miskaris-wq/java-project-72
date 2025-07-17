import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.TestUtils;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

import hexlet.code.App;

import static org.assertj.core.api.Assertions.assertThat;

public final class UrlsChecksControllerTest {

    private static MockWebServer mockServer;
    private Javalin app;
    private Map<String, Object> existingUrl;
    private HikariDataSource dataSource;

    private static Path getFixturePath(String fileName) {
        return Paths.get("src", "test", "resources", "fixtures", fileName)
                .toAbsolutePath().normalize();
    }

    private static String readFixture(String fileName) throws IOException {
        Path filePath = getFixturePath(fileName);
        return Files.readString(filePath).trim();
    }

    private static String getDatabaseUrl() {
        return System.getenv().getOrDefault("JDBC_DATABASE_URL", "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1");
    }

    @BeforeAll
    static void beforeAll() throws IOException {
        mockServer = new MockWebServer();
        MockResponse mockedResponse = new MockResponse()
                .setBody(readFixture("index.html"));
        mockServer.enqueue(mockedResponse);
        mockServer.start();
    }

    @AfterAll
    static void afterAll() throws IOException {
        mockServer.shutdown();
    }

    @BeforeEach
    void setUp() throws IOException, SQLException {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(getDatabaseUrl());
        dataSource = new HikariDataSource(hikariConfig);

        var schema = UrlsChecksControllerTest.class.getClassLoader().getResource("schema.sql");
        File file = new File(schema.getFile());
        String sql = Files.lines(file.toPath()).collect(Collectors.joining("\n"));

        try (var conn = dataSource.getConnection();
             var stmt = conn.createStatement()) {
            stmt.execute(sql);
        }

        app = App.getApp();

        String url = mockServer.url("/").toString().replaceAll("/$", "");
        TestUtils.addUrl(dataSource, url);
        existingUrl = TestUtils.getUrlByName(dataSource, url);
    }

    @Test
    void testCheckUrl() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.post("/urls/" + existingUrl.get("id") + "/checks");
            assertThat(response.code()).isEqualTo(200);

            var showResponse = client.get("/urls/" + existingUrl.get("id"));
            assertThat(showResponse.code()).isEqualTo(200);
            assertThat(showResponse.body()).isNotNull();
            String body = showResponse.body().string();

            assertThat(body).contains("Test page");
            assertThat(body).contains("Do not expect a miracle, miracles yourself!");
            assertThat(body).contains("statements of great people");

            var urlCheck = TestUtils.getUrlCheck(dataSource, (Long) existingUrl.get("id"));
            assertThat(urlCheck).isNotNull();
            assertThat(urlCheck.get("title")).isEqualTo("Test page");
            assertThat(urlCheck.get("h1")).isEqualTo("Do not expect a miracle, miracles yourself!");
            assertThat(urlCheck.get("description")).isEqualTo("statements of great people");
        });
    }
}
