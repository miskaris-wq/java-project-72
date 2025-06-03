package hexlet.code;

import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

public final class App {
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static Javalin getApp() {

        return Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
        });
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
    }
}
