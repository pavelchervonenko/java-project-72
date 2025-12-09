package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;

import gg.jte.TemplateEngine;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import java.lang.reflect.Method;

public class AppTest {

    @Test
    public void testGetPort() throws Exception {
        Method m = App.class.getDeclaredMethod("getPort");
        m.setAccessible(true);
        int port = (Integer) m.invoke(null);

        assertThat(port).isGreaterThan(0);
    }

    @Test
    public void testReadResourceSchemaSql() throws Exception {
        Method m = App.class.getDeclaredMethod("readResourse", String.class);
        m.setAccessible(true);
        String sql = (String) m.invoke(null, "schema.sql");

        assertThat(sql).isNotBlank();
        assertThat(sql).contains("CREATE TABLE");
    }

    @Test
    public void testBuildDataSourceAndRunMigrations() throws Exception {
        Method buildDs = App.class.getDeclaredMethod("buildDataSource");
        buildDs.setAccessible(true);
        DataSource ds = (DataSource) buildDs.invoke(null);

        assertThat(ds).isNotNull();

        Method runMigrations = App.class.getDeclaredMethod("runMigrations", DataSource.class);
        runMigrations.setAccessible(true);
        runMigrations.invoke(null, ds);

        try (var conn = ds.getConnection()) {
            assertThat(conn.isClosed()).isFalse();
        }
    }

    @Test
    public void testCreateTemplateEngine() throws Exception {
        Method m = App.class.getDeclaredMethod("createTemplateEngine");
        m.setAccessible(true);
        TemplateEngine engine = (TemplateEngine) m.invoke(null);

        assertThat(engine).isNotNull();
    }
}
