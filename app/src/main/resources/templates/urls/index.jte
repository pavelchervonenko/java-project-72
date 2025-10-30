@import hexlet.code.dto.urls.UrlsPage
@import hexlet.code.util.NamedRoutes
@import hexlet.code.model.Url
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
                <th scope="col">ID</th>
                <th scope="col">Имя</th>
                <th scope="col">Последняя проверка</th>
                <th scope="col">Код ответа</th>
              </tr>
            </thead>
            <tbody>
              @for (Url u : page.getUrls())
                <tr>
                  <td>${u.getId()}</td>
                  <td><a href="/urls/${u.getId()}">${u.getName()}</a></td>
                  <td>Надо сделать</td>
                  <td>Надо сделать</td>
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
