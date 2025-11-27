@import hexlet.code.dto.urls.UrlsPage
@import hexlet.code.model.Url
@import static hexlet.code.util.DateUtils.format
@import hexlet.code.util.NamedRoutes

@param UrlsPage page

<!DOCTYPE html>
<html lang="ru">
  <head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Сайты</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
  </head>
  <body class="container py-3">
    <header><h1>Сайты</h1></header>

    <main>
      @if (page.getUrls() == null || page.getUrls().isEmpty())
        <p>Пока не проверено ни одного сайта</p>
      @else
        <div class="table-responsive">
          <table class="table table-striped align-middle">
            <thead>
              <tr>
                <th>ID</th>
                <th>Имя</th>
                <th>Последняя проверка</th>
                <th>Код ответа</th>
              </tr>
            </thead>
            <tbody>
              @for (Url url : page.getUrls())
                <tr>
                  <td>${url.getId()}</td>
                  <td>
                    <a href="${NamedRoutes.urlPath(url.getId())}">
                      ${url.getName()}
                    </a>
                  </td>
                  <td>
                    @if (
                      page.getLastChecks() != null &&
                      page.getLastChecks().get(url.getId()) != null &&
                      page.getLastChecks().get(url.getId()).getCreatedAt() != null
                    )
                      ${format(page.getLastChecks().get(url.getId()).getCreatedAt())}
                    @else
                      &mdash;
                    @endif
                  </td>
                  <td>
                    @if (
                      page.getLastChecks() != null &&
                      page.getLastChecks().get(url.getId()) != null &&
                      page.getLastChecks().get(url.getId()).getStatusCode() != null
                    )
                      ${page.getLastChecks().get(url.getId()).getStatusCode()}
                    @else
                      &mdash;
                    @endif
                  </td>
                </tr>
              @endfor
            </tbody>
          </table>
        </div>
      @endif
    </main>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
  </body>
</html>
