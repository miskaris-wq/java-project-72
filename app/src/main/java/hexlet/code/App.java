package hexlet.code;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.utils.UrlUtils;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Map;

public final class App {
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static Javalin getApp() {
        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
        });

        app.get("/", ctx -> ctx.render("index.jte", Map.of("ctx", ctx)));

        app.post("/urls", ctx -> {
            var inputUrl = ctx.formParam("url");

            if (inputUrl == null || inputUrl.trim().isEmpty()) {
                ctx.sessionAttribute("error", "URL must not be empty");
                ctx.redirect("/");
                return;
            }

            var trimmedUrl = inputUrl.trim();
            var repository = new UrlRepository(Database.getDataSource());

            try {
                String normalizedUrl = UrlUtils.normalizeUrl(trimmedUrl);

                if (repository.findByName(normalizedUrl).isPresent()) {
                    ctx.sessionAttribute("flash", "Page already exists");
                } else {
                    var url = new Url();
                    url.setName(normalizedUrl);
                    repository.save(url);
                    ctx.sessionAttribute("flash", "Page successfully added");
                }
            } catch (URISyntaxException | SQLException | RuntimeException e) {
                LOG.error("Error while processing URL", e);
                ctx.sessionAttribute("error", "Invalid URL");
            }

            ctx.redirect("/");
        });

        app.get("/urls", ctx -> {
            try {
                var repository = new UrlRepository(Database.getDataSource());
                var urls = repository.findAll();
                ctx.render("urls/index.jte", Map.of("urls", urls));
            } catch (SQLException e) {
                LOG.error("Error fetching URL list", e);
                ctx.status(500).result("Error retrieving the list of URLs");
            }
        });

        return app;
    }

    private static TemplateEngine createTemplateEngine() {
        var classLoader = App.class.getClassLoader();
        var codeResolver = new ResourceCodeResolver("templates", classLoader);
        return TemplateEngine.create(codeResolver, ContentType.Html);
    }

    private static void runMigrations(DataSource dataSource) throws Exception {
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {

            var sqlStream = App.class.getClassLoader().getResourceAsStream("init.sql");
            if (sqlStream == null) {
                throw new IllegalStateException("init.sql not found in resources");
            }

            var sql = new String(sqlStream.readAllBytes());
            statement.execute(sql);
        }
    }

    public static void main(String[] args) throws Exception {
        LOG.info("Initializing database...");
        Database.init();
        runMigrations(Database.getDataSource());

        var port = Integer.parseInt(System.getenv().getOrDefault("PORT", "7070"));
        var app = getApp();

        LOG.info("Starting Javalin app on port {}", port);
        app.start(port);
    }
}
