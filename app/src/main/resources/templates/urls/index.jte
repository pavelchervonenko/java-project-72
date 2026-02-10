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
    <title>Сайты — Анализатор страниц</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>
      body {
        background:
          radial-gradient(1000px 500px at 20% -10%, rgba(217,255,58,.15), transparent),
          #f6f6f7;
      }

      .frame {
        border-radius: 28px;
        border: 2px solid #101014;
        background: #fff;
        overflow: hidden;
        box-shadow: 0 20px 60px rgba(0,0,0,.14);
      }

      .topbar {
        position: relative;
        padding: 18px 22px;
        border-bottom: 1px solid rgba(0,0,0,.10);
      }

      .brand-center {
        position: absolute;
        left: 50%;
        transform: translateX(-50%);
        top: 14px;
        font-weight: 800;
        letter-spacing: -0.03em;
        margin: 0;
      }

      .nav-pill a {
        text-decoration: none;
        padding: 8px 12px;
        border-radius: 999px;
        border: 1px solid rgba(0,0,0,.12);
        color: rgba(0,0,0,.78);
        font-weight: 600;
      }
      .nav-pill a:hover { background: rgba(0,0,0,.04); }

      .content {
        padding: 26px;
      }

      .empty {
        border-radius: 18px;
        border: 1px dashed rgba(0,0,0,.18);
        padding: 28px;
        text-align: center;
        font-weight: 600;
        opacity: .7;
      }

      table {
        border-radius: 16px;
        overflow: hidden;
      }

      thead th {
        font-weight: 700;
        opacity: .75;
        border-bottom: 1px solid rgba(0,0,0,.12);
      }

      tbody tr:hover {
        background: rgba(0,0,0,.03);
      }

      a.url-link {
        font-weight: 600;
        text-decoration: none;
      }

      a.url-link:hover {
        text-decoration: underline;
      }

      .footer {
        padding: 16px 22px;
        border-top: 1px solid rgba(0,0,0,.10);
        font-weight: 600;
        opacity: .7;
      }
    </style>
  </head>

  <body>
    <div class="container py-4 py-md-5">
      <div class="frame">

        <header class="topbar">
          <h1 class="brand-center">Сайты</h1>

          <nav class="d-flex gap-2 nav-pill">
            <a href="/">Главная</a>
            <a href="/urls">Сайты</a>
          </nav>
        </header>

        <main class="content">
          @if (page.getUrls() == null || page.getUrls().isEmpty())
            <div class="empty">
              Пока не проверено ни одного сайта
            </div>
          @else
            <div class="table-responsive">
              <table class="table align-middle mb-0">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Сайт</th>
                    <th>Последняя проверка</th>
                    <th>Код ответа</th>
                  </tr>
                </thead>
                <tbody>
                  @for (Url url : page.getUrls())
                    <tr>
                      <td>${url.getId()}</td>
                      <td>
                        <a class="url-link" href="${NamedRoutes.urlPath(url.getId())}">
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

        <footer class="footer">
          © Анализатор страниц
        </footer>

      </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
  </body>
</html>
