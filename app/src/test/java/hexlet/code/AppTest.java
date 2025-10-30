package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.repository.BaseRepository;

import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;


import java.util.stream.Collectors;

import io.javalin.Javalin;

public class AppTest {

    private Javalin app;

    private static String loadSchemaSql() throws Exception {
        var url = App.class.getClassLoader().getResourceAsStream("schema.sql");
        if (url == null) {
            throw new IllegalStateException("Resource not found: " + "schema.sql");
        }

        try (var sql = new BufferedReader(new InputStreamReader(url))) {
            return sql.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read resource: " + "schema.sql", e);

        }
    }

    @BeforeEach
    public final void setUp() throws Exception {
        var cfg = new HikariConfig();
        cfg.setJdbcUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;");
        DataSource ds = new HikariDataSource(cfg);

        BaseRepository.dataSource = ds;

        String sql = loadSchemaSql();
        try (Connection c = ds.getConnection();
             Statement st = c.createStatement()) {
            st.execute(sql);
        }

        try (Connection c = ds.getConnection();
             Statement st = c.createStatement()) {
            st.execute("TRUNCATE TABLE urls;");
        }

        app = App.getApp();
    }

    @Test
    public void testRootPath() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Анализатор страниц");
        });
    }

    @Test
    public void testUrlsPath() {
        JavalinTest.test(app, (server, client) -> {
            var index1 = client.get("/urls");
            assertThat(index1.code()).isEqualTo(200);
            assertThat(index1.body().string()).contains("Пока не проверено ни одного сайта");

            var body = "url=https%3A%2F%2Fwww.google.com";
            client.post("/urls", body);
            assertThat(UrlRepository.findByName("https://www.google.com")).isPresent();

            var index2 = client.get("/urls");
            assertThat(index2.code()).isEqualTo(200);
            var html2 = index2.body().string();
            assertThat(html2).contains("google.com");
        });
    }

    @Test
    public void testUrlShowPath() throws Exception {
        var url = new Url("https://example.com");
        UrlRepository.save(url);

        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/" + url.getId());
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("example.com");
        });
    }

    @Test
    public void testUrlShowNotFound() {
        JavalinTest.test(app, (server, client) -> {
            var resp = client.get("/urls/999999");
            assertThat(resp.code()).isEqualTo(404);
        });
    }
}
