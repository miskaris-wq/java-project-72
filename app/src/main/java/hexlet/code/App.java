package hexlet.code;

import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static Javalin getApp() {

        return Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
        });
    }

    public static void main(String[] args) {
        String portStr = System.getenv().getOrDefault("PORT", "7070");
        int port = Integer.parseInt(portStr);
        Javalin app = getApp();
        app.start(port);
    }
}
