package hexlet.code.repository;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RepositoryTest {

    @Test
    void testFindByIdWhenEmpty() throws Exception {
        var result = UrlRepository.findById(9999L);

        assertThat(result).isEmpty();
    }

    @Test
    void testFindByNameWhenEmpty() throws Exception {
        var result = UrlRepository.findByName("https://this-url-does-not-exist.hexlet");

        assertThat(result).isEmpty();
    }

    @Test
    void testFindChecksByUrlIdWhenEmpty() throws Exception {
        List<UrlCheck> checks = UrlCheckRepository.findByUrlId(9999L);

        assertThat(checks).isEmpty();
    }

    @Test
    void testFindLastCheckByUrlIdWhenEmpty() throws Exception {
        var result = UrlCheckRepository.findLastByUrlId(9999L);

        assertThat(result).isEmpty();
    }

    @Test
    void testSaveUrlCheckWithNullStatusCode() throws Exception {
        var url = new Url();
        url.setName("https://null-status-code-example.com");
        UrlRepository.save(url);

        var check = new UrlCheck();
        check.setUrlId(url.getId());
        check.setStatusCode(null);
        check.setTitle("title");
        check.setH1("h1");
        check.setDescription("desc");

        UrlCheckRepository.save(check);

        var savedCheck = UrlCheckRepository.findLastByUrlId(url.getId()).orElseThrow();

        assertThat(savedCheck.getStatusCode()).isNull();
        assertThat(savedCheck.getUrlId()).isEqualTo(url.getId());
        assertThat(savedCheck.getTitle()).isEqualTo("title");
        assertThat(savedCheck.getH1()).isEqualTo("h1");
        assertThat(savedCheck.getDescription()).isEqualTo("desc");
    }
}
