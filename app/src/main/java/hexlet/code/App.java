package hexlet.code;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hexlet.code.controller.UrlsController;

import hexlet.code.dto.IndexPage;

import hexlet.code.util.NamedRoutes;

import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import static io.javalin.rendering.template.TemplateUtil.model;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import hexlet.code.repository.BaseRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;

import java.util.stream.Collectors;

public final class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    private static final String DEV_H2_URL = "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;";

    public static void main(String[] args) {
        LOGGER.info("Starting application initialization");

        DataSource ds = buildDataSource();
        BaseRepository.dataSource = ds;

        LOGGER.info("Starting application initialization");
        runMigrations(ds);
        LOGGER.info("DB migrations finished successfully");

        int port = getPort();
        LOGGER.info("Starting Javalin server on port {}", port);

        Javalin app = getApp();
        app.start(port);
        LOGGER.info("Application started and listening on port {}", port);
    }

    public static Javalin getApp() {
        LOGGER.debug("Creating Javalin application");

        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
        });
        LOGGER.debug("Registering routes");

        app.get(NamedRoutes.rootPath(), ctx -> {
            LOGGER.debug("Handling GET {}", NamedRoutes.rootPath());
            var page = new IndexPage();
            page.setFlash(ctx.consumeSessionAttribute("flash"));
            ctx.render("index.jte", model("page", page));
        });

        app.get(NamedRoutes.urlsPath(), ctx -> {
            LOGGER.debug("Handling GET {}", NamedRoutes.urlsPath());
            UrlsController.index(ctx);
        });

        app.get(NamedRoutes.urlPath(), ctx -> {
            LOGGER.debug("Handling GET {}", NamedRoutes.urlPath());
            UrlsController.show(ctx);
        });

        app.post(NamedRoutes.urlsPath(), ctx -> {
            LOGGER.debug("Handling POST {}", NamedRoutes.urlsPath());
            UrlsController.create(ctx);
        });

        app.post(NamedRoutes.urlCheckPath(), ctx -> {
            LOGGER.debug("Handling POST {}", NamedRoutes.urlCheckPath());
            UrlsController.check(ctx);
        });

        LOGGER.info("Javalin application created and routes registered");
        return app;
    }

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.valueOf(port);
    }

    private static String getJdbcUrl() {
        String env = System.getenv("JDBC_DATABASE_URL");
        if (env != null && !env.isBlank()) {
            LOGGER.info("Using JDBC url from environment");
            return env;
        }

        LOGGER.info("JDBC_DATABASE_URL not set, using dev H2 url: {}", DEV_H2_URL);
        return DEV_H2_URL;
    }

    private static DataSource buildDataSource() {
        String jdbcUrl = getJdbcUrl();
        LOGGER.info("Building HikariDataSource with url={}", jdbcUrl);

        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl(jdbcUrl);
        DataSource ds = new HikariDataSource(cfg);

        LOGGER.info("HikariDataSource created");
        return ds;
    }

    private static String readResourse(String name) {
        LOGGER.debug("Reading resource '{}'", name);

        var url = App.class.getClassLoader().getResourceAsStream(name);
        if (url == null) {
            LOGGER.error("Resource '{}' not found in classpath", name);
            throw new IllegalStateException("Resource not found: " + name);
        }

        try (var sql = new BufferedReader(new InputStreamReader(url))) {
            String content = sql.lines().collect(Collectors.joining("\n"));
            LOGGER.debug("Resource '{}' successfully read ({} chars)", name, content.length());
            return content;
        } catch (IOException e) {
            LOGGER.error("Failed to read resource '{}'", name, e);
            throw new IllegalStateException("Failed to read resource: " + name, e);

        }
    }

    private static void runMigrations(DataSource ds) {
        LOGGER.debug("Executing schema.sql for DB initialization");
        String sql = readResourse("schema.sql");

        try (var connection = ds.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
            LOGGER.info("Schema.sql executed successfully");
        } catch (Exception e) {
            LOGGER.error("Schema init failed", e);
            throw new RuntimeException("Schema init failed", e);
        }
    }

    private static TemplateEngine createTemplateEngine() {
        LOGGER.debug("Creating JTE TemplateEngine");

        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        TemplateEngine templateEngine = TemplateEngine.create(codeResolver, ContentType.Html);

        LOGGER.info("JTE TemplateEngine created");
        return templateEngine;
    }
}
