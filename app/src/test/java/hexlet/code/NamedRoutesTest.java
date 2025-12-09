package hexlet.code;

import hexlet.code.util.NamedRoutes;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NamedRoutesTest {

    @Test
    void testRootPath() {
        assertThat(NamedRoutes.rootPath()).isEqualTo("/");
    }

    @Test
    void testUrlsPath() {
        assertThat(NamedRoutes.urlsPath()).isEqualTo("/urls");
    }

    @Test
    void testUrlPathTemplate() {
        assertThat(NamedRoutes.urlPath()).isEqualTo("/urls/{id}");
    }

    @Test
    void testUrlPathWithId() {
        assertThat(NamedRoutes.urlPath(10L)).isEqualTo("/urls/10");
    }

    @Test
    void testUrlCheckPathTemplate() {
        assertThat(NamedRoutes.urlCheckPath()).isEqualTo("/urls/{id}/checks");
    }

    @Test
    void testUrlCheckPathWithId() {
        assertThat(NamedRoutes.urlCheckPath(7L)).isEqualTo("/urls/7/checks");
    }
}
