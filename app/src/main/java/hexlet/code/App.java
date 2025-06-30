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
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class App {
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static Javalin getApp() throws SQLException {
        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
        });

        app.get("/", ctx -> {
            Map<String, Object> model = new HashMap<>();
            model.put("ctx", ctx);
            String error = ctx.consumeSessionAttribute("error");
            if (error != null) {
                model.put("error", error);
            }
            ctx.render("index.jte", model);
        });

        app.post("/urls", ctx -> {
            String inputUrl = ctx.formParam("url");

            if (inputUrl == null || inputUrl.trim().isEmpty()) {
                ctx.sessionAttribute("error", "URL не должен быть пустым");
                ctx.redirect("/");
                return;
            }

            try {
                String normalizedUrl = UrlUtils.normalizeUrl(inputUrl.trim());
                var repository = new UrlRepository(Database.getDataSource());

                if (repository.findByName(normalizedUrl).isPresent()) {
                    ctx.sessionAttribute("flash", "Страница уже существует");
                    ctx.redirect("/urls");
                    return;
                }

                var url = new Url();
                url.setName(normalizedUrl);
                repository.save(url);
                ctx.sessionAttribute("info", "Страница успешно добавлена");
                ctx.redirect("/urls");
            } catch (URISyntaxException e) {
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

            try {
                var response = Unirest.get(urlOptional.get().getName()).asString();
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
            var urlRepository = new UrlRepository(Database.getDataSource());
            var urlCheckRepository = new UrlCheckRepository(Database.getDataSource());

            var urls = urlRepository.findAll().stream()
                    .sorted(Comparator.comparing(Url::getId))
                    .toList();

            Map<Long, UrlCheck> lastChecks = new HashMap<>();
            urls.forEach(u -> {
                Optional<UrlCheck> lastCheck = null;
                try {
                    lastCheck = urlCheckRepository.findLastCheckByUrlId(u.getId());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                if (lastCheck.isPresent()) {
                    lastChecks.put(u.getId(), lastCheck.get());
                }
            });

            var model = new HashMap<String, Object>();
            model.put("urls", urls);
            model.put("lastChecks", lastChecks);
            model.put("ctx", ctx);

            String flash = ctx.consumeSessionAttribute("flash");
            if (flash != null) {
                model.put("flash", flash);
            }
            String info = ctx.consumeSessionAttribute("info");
            if (info != null) {
                model.put("info", info);
            }

            ctx.render("urls/index.jte", model);
        });

        app.get("/urls/{id}", ctx -> {
            long id = ctx.pathParamAsClass("id", Long.class).get();
            var urlRepository = new UrlRepository(Database.getDataSource());
            var checkRepository = new UrlCheckRepository(Database.getDataSource());

            var urlOptional = urlRepository.findById(id);
            if (urlOptional.isEmpty()) {
                ctx.status(404).result("URL not found");
                return;
            }

            var checks = checkRepository.findAllByUrlId(id);
            ctx.render("urls/show.jte", Map.of(
                    "url", urlOptional.get(),
                    "checks", checks,
                    "ctx", ctx
            ));
        });

        return app;
    }

    private static TemplateEngine createTemplateEngine() {
        var codeResolver = new ResourceCodeResolver("templates", App.class.getClassLoader());
        return TemplateEngine.create(codeResolver, ContentType.Html);
    }

    public static void runMigrations(DataSource dataSource) throws SQLException, IOException {
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            var sql = new String(App.class.getClassLoader()
                    .getResourceAsStream("init.sql").readAllBytes());
            for (String query : sql.split(";")) {
                if (!query.trim().isEmpty()) {
                    statement.execute(query.trim());
                }
            }
        }
    }

    public static void main(String[] args) throws SQLException, IOException {
        LOG.info("Starting app");
        Database.init();
        runMigrations(Database.getDataSource());
        getApp().start(Integer.parseInt(System.getenv().getOrDefault("PORT", "7070")));
    }
}