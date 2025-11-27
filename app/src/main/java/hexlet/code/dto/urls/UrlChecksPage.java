package hexlet.code.dto.urls;

import hexlet.code.dto.BasePage;
import hexlet.code.model.UrlCheck;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class UrlChecksPage extends BasePage {
    private List<UrlCheck> urlChecks;
}
