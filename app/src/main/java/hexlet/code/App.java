package hexlet.code;

import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static Javalin getApp() {
        Javalin app = Javalin.create(config -> {
            if (isDevelopment()) {
                config.requestLogger.http((ctx, ms) -> {
                    LOGGER.info("{} {} {} {}ms", ctx.method(), ctx.path(), ctx.status(), ms);
                });
            }
        });

        app.get("/", ctx -> ctx.result("Hello World"));
        return app;
    }

    public static void main(String[] args) {
        Javalin app = getApp();
        app.start(getPort());
    }

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.parseInt(port);
    }

    private static boolean isDevelopment() {
        return System.getenv("APP_ENV") == null || "development".equals(System.getenv("APP_ENV"));
    }
}