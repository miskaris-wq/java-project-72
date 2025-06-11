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

        // Главная страница с формой и выводом ошибки из сессии
        app.get("/", ctx -> {
            String error = ctx.consumeSessionAttribute("error"); // забираем и удаляем ошибку из сессии, если есть
            Map<String, Object> model = new HashMap<>();
            model.put("ctx", ctx);
            if (error != null) {
                model.put("error", error);
            }
            ctx.render("index.jte", model);  // вызываем render только один раз с полной моделью
        });


        // Обработка формы добавления URL
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

        // Страница списка URL с выводом flash и error сообщений
        app.get("/urls", ctx -> {
            try {
                var repository = new UrlRepository(Database.getDataSource());
                var urls = repository.findAll()
                        .stream()
                        .sorted(Comparator.comparing(Url::getId))
                        .toList();

                var model = new HashMap<String, Object>();
                model.put("urls", urls);
                model.put("ctx", ctx);

                // Добавим flash и info, если есть
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


        // Страница конкретного URL
        app.get("/urls/{id}", ctx -> {
            try {
                var id = ctx.pathParamAsClass("id", Long.class).get();
                var repository = new UrlRepository(Database.getDataSource());
                Optional<Url> urlOptional = repository.findById(id);

                if (urlOptional.isPresent()) {
                    ctx.render("urls/show.jte", Map.of("url", urlOptional.get(), "ctx", ctx));
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