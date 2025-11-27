@import hexlet.code.dto.urls.UrlPage
@import hexlet.code.dto.urls.UrlChecksPage
@import hexlet.code.model.UrlCheck
@import static hexlet.code.util.DateUtils.format

@param UrlPage page
@param UrlChecksPage pageChecks

<html>
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>Информация о сайте</title>
    </head>

    <body>
        <h1>Сайт: ${page.getUrl().getName()}</h1>

        <table border="1" cellpadding="6" cellspacing="0">
            <tr>
                <th>ID</th>
                <td>${page.getUrl().getId()}</td>
            </tr>
            <tr>
                <th>Имя</th>
                <td>${page.getUrl().getName()}</td>
            </tr>
            <tr>
                <th>Дата создания</th>
                <td>${format(page.getUrl().getCreatedAt())}</td>
            </tr>
        </table>

        <h2>Проверки</h2>

        <form action="/urls/${page.getUrl().getId()}/checks" method="post" style="margin: 12px 0;">
            <button type="submit">Запустить проверку</button>
        </form>

        <table border="1" cellpadding="6" cellspacing="0">
            <tr>
                <th>ID</th>
                <th>Код ответа</th>
                <th>title</th>
                <th>h1</th>
                <th>description</th>
                <th>Дата проверки</th>
            </tr>

            @for (UrlCheck check : pageChecks.getUrlChecks())
                <tr>
                    <td>
                        ${check.getId()}
                    </td>
                    <td>
                        ${check.getStatusCode()}
                    </td>
                    <td>
                        ${check.getTitle()}
                    </td>
                    <td>
                        ${check.getH1()}
                    </td>
                    <td>
                        ${check.getDescription()}
                    </td>
                    <td>
                        ${format(check.getCreatedAt())}
                    </td>
                </tr>
            @endfor

        </table>
    </body>
</html>