package hexlet.code;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import hexlet.code.database.Database;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.utils.UrlUtils;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import kong.unirest.Unirest;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class App {
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static Javalin getApp() {
        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
        });

        app.get("/", ctx -> {
            String error = ctx.consumeSessionAttribute("error");
            Map<String, Object> model = new HashMap<>();
            model.put("ctx", ctx);
            if (error != null) {
                model.put("error", error);
            }
            ctx.render("index.jte", model);
        });

        app.post("/urls", ctx -> {
            var inputUrl = ctx.formParam("url");

            if (inputUrl == null || inputUrl.trim().isEmpty()) {
                ctx.sessionAttribute("error", "URL не должен быть пустым");
                ctx.redirect("/");
                return;
            }
            var trimmedUrl = inputUrl.trim();
            var repository = new UrlRepository(Database.getDataSource());
            try {
                String normalizedUrl = UrlUtils.normalizeUrl(trimmedUrl);
                if (repository.findByName(normalizedUrl).isPresent()) {
                    ctx.sessionAttribute("flash", "Страница уже существует");
                    ctx.redirect("/urls");
                } else {
                    var url = new Url();
                    url.setName(normalizedUrl);
                    repository.save(url);
                    ctx.sessionAttribute("info", "Страница успешно добавлена");
                    ctx.redirect("/urls");
                }
            } catch (URISyntaxException | SQLException | RuntimeException e) {
                LOG.error("Error while processing URL", e);
                ctx.sessionAttribute("error", "Некорректный URL");
                ctx.redirect("/");
            }
        });

        app.post("/urls/{id}/checks", ctx -> {
            long urlId = ctx.pathParamAsClass("id", Long.class).get();
            var urlRepository = new UrlRepository(Database.getDataSource());
            var urlOptional = urlRepository.findById(urlId);
            if (urlOptional.isEmpty()) {
                ctx.status(404).result("URL not found");
                return;
            }
            var url = urlOptional.get();
            try {
                var response = Unirest.get(url.getName()).asString();
                var document = Jsoup.parse(response.getBody());
                var check = new UrlCheck();
                check.setUrlId(urlId);
                check.setStatusCode(response.getStatus());
                check.setTitle(document.title());
                check.setH1(document.selectFirst("h1") != null ? document.selectFirst("h1").text() : null);
                check.setDescription(document.selectFirst("meta[name=description]") != null
                        ? document.selectFirst("meta[name=description]").attr("content") : null);
                check.setCreatedAt(LocalDateTime.now());
                new UrlCheckRepository(Database.getDataSource()).save(check);
                ctx.sessionAttribute("flash", "Проверка успешно выполнена");
            } catch (Exception e) {
                LOG.error("Ошибка при проверке сайта", e);
                ctx.sessionAttribute("error", "Произошла ошибка при проверке сайта");
            }

            ctx.redirect("/urls/" + urlId);
        });

        app.get("/urls", ctx -> {
            try {
                var urlRepository = new UrlRepository(Database.getDataSource());
                var urlCheckRepository = new UrlCheckRepository(Database.getDataSource());

                var urls = urlRepository.findAll()
                        .stream()
                        .sorted(Comparator.comparing(Url::getId))
                        .toList();

                // Собираем последние проверки для каждого URL
                Map<Long, UrlCheck> lastChecks = new HashMap<>();
                for (Url url : urls) {
                    var lastCheck = urlCheckRepository.findLastCheckByUrlId(url.getId());
                    lastCheck.ifPresent(check -> lastChecks.put(url.getId(), check));
                }
                var model = new HashMap<String, Object>();
                model.put("urls", urls);
                model.put("lastChecks", lastChecks);
                model.put("ctx", ctx);

                String flash = ctx.consumeSessionAttribute("flash");
                String info = ctx.consumeSessionAttribute("info");

                if (flash != null) {
                    model.put("flash", flash);
                }
                if (info != null) {
                    model.put("info", info);
                }

                ctx.render("urls/index.jte", model);

            } catch (SQLException e) {
                LOG.error("Error fetching URL list", e);
                ctx.status(500).result("Error retrieving the list of URLs");
            }
        });

        app.get("/urls/{id}", ctx -> {
            try {
                var id = ctx.pathParamAsClass("id", Long.class).get();

                var urlRepository = new UrlRepository(Database.getDataSource());
                var checkRepository = new UrlCheckRepository(Database.getDataSource());

                Optional<Url> urlOptional = urlRepository.findById(id);

                if (urlOptional.isPresent()) {
                    var url = urlOptional.get();
                    var checks = checkRepository.findAllByUrlId(id);

                    ctx.render("urls/show.jte", Map.of(
                            "url", url,
                            "checks", checks,
                            "ctx", ctx
                    ));
                } else {
                    ctx.status(404).result("URL not found");
                }
            } catch (SQLException e) {
                LOG.error("Error fetching URL", e);
                ctx.status(500).result("Error retrieving the URL");
            }
        });
        return app;
    }

    public static TemplateEngine createTemplateEngine() {
        var classLoader = App.class.getClassLoader();
        var codeResolver = new ResourceCodeResolver("templates", classLoader);
        return TemplateEngine.create(codeResolver, ContentType.Html);
    }

    public static void runMigrations(DataSource dataSource) throws Exception {
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {

            var sqlStream = App.class.getClassLoader().getResourceAsStream("init.sql");
            if (sqlStream == null) {
                throw new IllegalStateException("init.sql not found in resources");
            }

            var sql = new String(sqlStream.readAllBytes());

            // Разбиваем по точке с запятой и выполняем каждое выражение отдельно
            for (String query : sql.split(";")) {
                var trimmed = query.trim();
                if (!trimmed.isEmpty()) {
                    statement.execute(trimmed);
                }
            }
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
