package hexlet.code;

import hexlet.code.util.DateUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class DateUtilsTest {

    @Test
    void testFormatNonNull() {
        var dateTime = LocalDateTime.of(2024, 1, 2, 3, 4, 5);
        var formatted = DateUtils.format(dateTime);

        assertThat(formatted).isEqualTo("2024-01-02 03:04:05");
    }

    @Test
    void testFormatNull() {
        var formatted = DateUtils.format(null);

        assertThat(formatted).isEqualTo("-");
    }
}
