@import hexlet.code.dto.urls.UrlPage
@param UrlPage page

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
                <td>${page.getUrl().getCreatedAt().toString()}</td>
            </tr>
        </table>

        <h2>Проверки</h2>

        <table border="1" cellpadding="6" cellspacing="0">
            <tr>
                <th>ID</th>
                <th>Код ответа</th>
                <th>title</th>
                <th>h1</th>
                <th>description</th>
                <th>Дата проверки</th>
            </tr>
        </table>

    </body>
</html>