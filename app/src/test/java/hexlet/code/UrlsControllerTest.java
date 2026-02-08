package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import hexlet.code.model.Url;

import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.repository.BaseRepository;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import io.javalin.testtools.JavalinTest;
import io.javalin.Javalin;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import java.net.URLEncoder;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.sql.Connection;
import java.sql.Statement;
import javax.sql.DataSource;


public class UrlsControllerTest {
    private static MockWebServer mockWebServer;
    private static String mockBaseUrl;

    private Javalin app;

    private static String loadSchemaSql() throws Exception {
        var url = App.class.getClassLoader().getResource("schema.sql");
        if (url == null) {
            throw new IllegalStateException("Resource not found: schema.sql");
        }

        return Files.readString(Paths.get(url.toURI()));
    }

    @BeforeAll
    public static void beforeAll() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        mockBaseUrl = mockWebServer.url("/").toString();
        mockBaseUrl = mockBaseUrl.replaceAll("/$", "");

    }

    @AfterAll
    public static void afterAll() throws IOException {
        if (mockWebServer != null) {
            mockWebServer.shutdown();
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

            for (String statement : sql.split(";")) {
                String trimmed = statement.trim();
                if (!trimmed.isEmpty()) {
                    st.execute(trimmed);
                }
            }
        }

        try (Connection c = ds.getConnection();
             Statement st = c.createStatement()) {
            st.executeUpdate("DELETE FROM url_checks");
            st.executeUpdate("DELETE FROM urls");
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
    public void testUrlsCreateDuplicate() {
        JavalinTest.test(app, (server, client) -> {
            var url = "https://example.com";
            var form = "url=" + URLEncoder.encode(url, StandardCharsets.UTF_8);

            client.post("/urls", form);
            var saved1 = UrlRepository.findByName(url).orElseThrow();

            client.post("/urls", form);
            var saved2 = UrlRepository.findByName(url).orElseThrow();

            assertThat(saved2.getId()).isEqualTo(saved1.getId());
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

    @Test
    public void testUrlCheckHandler() throws Exception {
        var mockHtml = """
            <!DOCTYPE html>
            <html>
              <head>
                <title>Mock page</title>
              </head>
              <body>
                <h1>Hello from mock</h1>
              </body>
            </html>
            """;

        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(200)
                .setBody(mockHtml)
        );

        JavalinTest.test(app, (server, client) -> {
            var formBody = "url=" + URLEncoder.encode(mockBaseUrl, StandardCharsets.UTF_8);
            client.post("/urls", formBody);

            var saved = UrlRepository.findByName(mockBaseUrl).orElseThrow();
            var urlId = saved.getId();

            client.post("/urls/" + urlId + "/checks");

            var showResp = client.get("/urls/" + urlId);
            assertThat(showResp.code()).isEqualTo(200);
            var body = showResp.body().string();

            assertThat(body).contains("Mock page");
            assertThat(body).contains("Hello from mock");
            assertThat(body).contains("200");

            var checks = UrlCheckRepository.findByUrlId(urlId);
            assertThat(checks).isNotEmpty();

            var lastCheck = checks.get(0);
            assertThat(lastCheck.getStatusCode()).isEqualTo(200);
            assertThat(lastCheck.getTitle()).isEqualTo("Mock page");
            assertThat(lastCheck.getH1()).isEqualTo("Hello from mock");
            assertThat(lastCheck.getDescription()).isNull();
        });

        var recorded = mockWebServer.takeRequest();
        assertThat(recorded.getMethod()).isEqualTo("GET");
    }

    @Test
    public void testUrlCheckHandlerWithoutTags() throws Exception {
        var mockHtml = """
            <!DOCTYPE html>
            <html>
              <head>
              </head>
              <body>
                <p>No title and no h1 here</p>
              </body>
            </html>
            """;

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(mockHtml)
        );

        JavalinTest.test(app, (server, client) -> {
            var formBody = "url=" + URLEncoder.encode(mockBaseUrl, StandardCharsets.UTF_8);
            client.post("/urls", formBody);

            var saved = UrlRepository.findByName(mockBaseUrl).orElseThrow();
            var urlId = saved.getId();

            client.post("/urls/" + urlId + "/checks");

            var showResp = client.get("/urls/" + urlId);
            assertThat(showResp.code()).isEqualTo(200);

            var body = showResp.body().string();

            assertThat(body).contains("200");

        });

        var recorded = mockWebServer.takeRequest();
        assertThat(recorded.getMethod()).isEqualTo("GET");
    }

    @Test
    public void testUrlsCreateInvalidUrl() {
        JavalinTest.test(app, (server, client) -> {
            var invalidUrl = "not-a-url";

            var body = "url=" + URLEncoder.encode(invalidUrl, StandardCharsets.UTF_8);
            var resp = client.post("/urls", body);

            assertThat(resp.code()).isEqualTo(200);

            var html = resp.body().string();

            assertThat(html).contains("Анализатор страниц");
            assertThat(html).contains("Ссылка");

            assertThat(html).doesNotContain(invalidUrl);

            assertThat(UrlRepository.findByName(invalidUrl)).isEmpty();
        });
    }

    @Test
    public void testShowNonExistingUrl() {
        JavalinTest.test(app, (server, client) -> {
            var resp = client.get("/urls/9999");

            assertThat(resp.code()).isEqualTo(404);

            var html = resp.body().string();

            assertThat(html).contains("URL with id = 9999 not found");
        });
    }

    @Test
    public void testCheckNonExistingUrl() {
        JavalinTest.test(app, (server, client) -> {
            var resp = client.post("/urls/9999/checks");

            assertThat(resp.code()).isEqualTo(404);
        });
    }

}
