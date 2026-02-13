package hexlet.code;

import lombok.extern.slf4j.Slf4j;

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

@Slf4j
public final class App {

    private static final String DEV_H2_URL = "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;";

    private static boolean dbInit = false;

    public static void main(String[] args) {
        //log.info("Starting application initialization");

        //DataSource ds = buildDataSource();
        //BaseRepository.dataSource = ds;

        //log.info("Starting application initialization");
        //runMigrations(ds);
        //log.info("DB migrations finished successfully");

        int port = getPort();
        log.info("Starting Javalin server on port {}", port);

        Javalin app = getApp();
        app.start(port);
        log.info("Application started and listening on port {}", port);
    }

    public static Javalin getApp() {
        initDb();

        log.debug("Creating Javalin application");

        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
        });
        log.debug("Registering routes");

        app.get(NamedRoutes.rootPath(), ctx -> {
            log.debug("Handling GET {}", NamedRoutes.rootPath());
            var page = new IndexPage();
            page.setFlash(ctx.consumeSessionAttribute("flash"));
            ctx.render("index.jte", model("page", page));
        });

        app.get(NamedRoutes.urlsPath(), ctx -> {
            log.debug("Handling GET {}", NamedRoutes.urlsPath());
            UrlsController.index(ctx);
        });

        app.get(NamedRoutes.urlPath(), ctx -> {
            log.debug("Handling GET {}", NamedRoutes.urlPath());
            UrlsController.show(ctx);
        });

        app.post(NamedRoutes.urlsPath(), ctx -> {
            log.debug("Handling POST {}", NamedRoutes.urlsPath());
            UrlsController.create(ctx);
        });

        app.post(NamedRoutes.urlCheckPath(), ctx -> {
            log.debug("Handling POST {}", NamedRoutes.urlCheckPath());
            UrlsController.check(ctx);
        });

        log.info("Javalin application created and routes registered");
        return app;
    }

    private static synchronized void initDb() {
        if (dbInit) {
            return;
        }

        if (BaseRepository.dataSource == null) {
            DataSource ds = buildDataSource();
            BaseRepository.dataSource = ds;
        }

        runMigrations(BaseRepository.dataSource);
        dbInit = true;
    }

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.valueOf(port);
    }

    private static String getJdbcUrl() {
        String env = System.getenv("JDBC_DATABASE_URL");
        if (env != null && !env.isBlank()) {
            log.info("Using JDBC url from environment");
            return env;
        }

        log.info("JDBC_DATABASE_URL not set, using dev H2 url: {}", DEV_H2_URL);
        return DEV_H2_URL;
    }

    private static DataSource buildDataSource() {
        String jdbcUrl = getJdbcUrl();
        log.info("Building HikariDataSource with url={}", jdbcUrl);

        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl(jdbcUrl);

        DataSource ds = new HikariDataSource(cfg);
        log.info("HikariDataSource created");
        return ds;
    }

    private static String readResourse(String name) {
        log.debug("Reading resource '{}'", name);

        var url = App.class.getClassLoader().getResourceAsStream(name);
        if (url == null) {
            log.error("Resource '{}' not found in classpath", name);
            throw new IllegalStateException("Resource not found: " + name);
        }

        try (var sql = new BufferedReader(new InputStreamReader(url))) {
            String content = sql.lines().collect(Collectors.joining("\n"));
            log.debug("Resource '{}' successfully read ({} chars)", name, content.length());
            return content;
        } catch (IOException e) {
            log.error("Failed to read resource '{}'", name, e);
            throw new IllegalStateException("Failed to read resource: " + name, e);

        }
    }

    private static void runMigrations(DataSource ds) {
        log.debug("Executing schema.sql for DB initialization");
        String sql = readResourse("schema.sql");

        try (var connection = ds.getConnection();
             var statement = connection.createStatement()) {

            for (var part : sql.split(";")) {
                var trimmed = part.trim();
                if (!trimmed.isEmpty()) {
                    statement.execute(trimmed);
                }
            }

            log.info("Schema.sql executed successfully");
        } catch (Exception e) {
            log.error("Schema init failed", e);
            throw new RuntimeException("Schema init failed", e);
        }
    }

    private static TemplateEngine createTemplateEngine() {
        log.debug("Creating JTE TemplateEngine");

        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        TemplateEngine templateEngine = TemplateEngine.create(codeResolver, ContentType.Html);

        log.info("JTE TemplateEngine created");
        return templateEngine;
    }
}
