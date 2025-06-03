package hexlet.code;

import gg.jte.ContentType;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;

import javax.sql.DataSource;

public final class App {
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static Javalin getApp() {
        return Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
        }).get("/", ctx -> {
            ctx.render("index.jte");
        });
    }

    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        return TemplateEngine.create(codeResolver, ContentType.Html);
    }

    private static void runMigrations(DataSource dataSource) throws Exception {
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {

            var sql = new String(App.class.getClassLoader()
                    .getResourceAsStream("init.sql").readAllBytes());
            statement.execute(sql);
        }
    }


    public static void main(String[] args) throws Exception {
        LOG.info("Initializing database...");
        Database.init();
        runMigrations(Database.getDataSource());

        String portStr = System.getenv().getOrDefault("PORT", "7070");
        int port = Integer.parseInt(portStr);

        Javalin app = getApp();
        LOG.info("Starting Javalin app on port {}", port);
        app.start(port);
        app.get("/", ctx -> ctx.result("Hello World"));
        app.start(port);
    }
}
