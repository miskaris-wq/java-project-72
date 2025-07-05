import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.App;
import utils.TestUtils;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

final public class UrlsControllerTest {

    private static MockWebServer mockServer;
    private Javalin app;
    private Map<String, Object> existingUrl;
    private Map<String, Object> existingUrlCheck;
    private HikariDataSource dataSource;

    private static Path getFixturePath(String fileName) {
        return Paths.get("src", "test", "resources", "fixtures", fileName).toAbsolutePath().normalize();
    }

    private static String readFixture(String fileName) throws IOException {
        Path filePath = getFixturePath(fileName);
        return Files.readString(filePath).trim();
    }

    private static String getDatabaseUrl() {
        return System.getenv().getOrDefault("JDBC_DATABASE_URL", "jdbc:h2:mem:project");
    }

    @BeforeAll
    public static void beforeAll() throws IOException {
        mockServer = new MockWebServer();
        MockResponse mockedResponse = new MockResponse()
                .setBody(readFixture("index.html"));
        mockServer.enqueue(mockedResponse);
        mockServer.start();
    }

    @AfterAll
    public static void afterAll() throws IOException {
        mockServer.shutdown();
    }

    @BeforeEach
    public void setUp() throws IOException, SQLException {
        app = App.getApp();

        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(getDatabaseUrl());
        dataSource = new HikariDataSource(hikariConfig);

        var schema = UrlsControllerTest.class.getClassLoader().getResource("schema.sql");
        var file = new File(schema.getFile());
        var sql = Files.lines(file.toPath()).collect(Collectors.joining("\n"));

        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        }

        String url = "https://en.hexlet.io";

        TestUtils.addUrl(dataSource, url);
        existingUrl = TestUtils.getUrlByName(dataSource, url);

        TestUtils.addUrlCheck(dataSource, (long) existingUrl.get("id"));
        existingUrlCheck = TestUtils.getUrlCheck(dataSource, (long) existingUrl.get("id"));
    }

    @Test
    public void testUrlsIndex() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
            var body = response.body().string();
            assertThat(body).contains(existingUrl.get("name").toString());
            assertThat(body).contains(existingUrlCheck.get("status_code").toString());
        });
    }

    @Test
    public void testUrlsShow() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/" + existingUrl.get("id"));
            assertThat(response.code()).isEqualTo(200);
            var body = response.body().string();
            assertThat(body).contains(existingUrl.get("name").toString());
            assertThat(body).contains(existingUrlCheck.get("status_code").toString());
        });
    }

    @Test
    public void testUrlsCreate() {
        String inputUrl = "https://ru.hexlet.io";

        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=" + inputUrl;

            var postResponse = client.post("/urls", requestBody);
            assertThat(postResponse.code()).isIn(200, 302);

            var getResponse = client.get("/urls");
            assertThat(getResponse.code()).isEqualTo(200);
            var body = getResponse.body().string();
            assertThat(body).contains(inputUrl);

            var actualUrl = TestUtils.getUrlByName(dataSource, inputUrl);
            assertThat(actualUrl).isNotNull();
            assertThat(actualUrl.get("name").toString()).isEqualTo(inputUrl);
        });
    }
}
