package hexlet.code;

import io.javalin.Javalin;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import hexlet.code.repository.BaseRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.util.stream.Collectors;

//import lombok.extern.slf4j.Slf4j;

//@Slf4j
public final class App {

    private static final String DEV_H2_URL = "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;";

    public static void main(String[] args) {
        DataSource ds = buildDataSource();
        BaseRepository.dataSource = ds;
        runMigrations(ds);

        Javalin app = getApp();
        app.start(getPort());
    }

    public static Javalin getApp() {
        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
        });

        app.get("/", ctx -> {
            ctx.result("Hello World");
        });

        return app;
    }

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.valueOf(port);
    }

    private static String getJdbcUrl() {
        String env = System.getenv("JDBC_DATABASE_URL");
        if (env != null && !env.isBlank()) {
            return env;
        }

        return DEV_H2_URL;
    }

    private static DataSource buildDataSource() {
        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl(getJdbcUrl());
        return new HikariDataSource(cfg);
    }

    private static String readResourse(String name) {
        var url = App.class.getClassLoader().getResourceAsStream(name);
        if (url == null) {
            throw new IllegalStateException("Resource not found: " + name);
        }

        try (var sql = new BufferedReader(new InputStreamReader(url))) {
            return sql.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read resource: " + name, e);

        }
    }


    private static void runMigrations(DataSource ds) {
        String sql = readResourse("schema.sql");

        try (var connection = ds.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (Exception e) {
            throw new RuntimeException("Schema init failed", e);
        }
    }
}
