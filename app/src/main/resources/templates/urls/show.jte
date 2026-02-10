@import hexlet.code.dto.urls.UrlPage
@import hexlet.code.dto.urls.UrlChecksPage
@import hexlet.code.model.UrlCheck
@import static hexlet.code.util.DateUtils.format
@import hexlet.code.util.NamedRoutes

@param UrlPage page
@param UrlChecksPage pageChecks

<!DOCTYPE html>
<html lang="ru">
  <head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Сайт — Анализатор страниц</title>

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

      .card-soft {
        border-radius: 18px;
        border: 1px solid rgba(0,0,0,.10);
        padding: 18px;
        background: #fff;
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

      .btn-check {
        border-radius: 14px;
        font-weight: 700;
        padding: 10px 14px;
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
          <h1 class="brand-center">Информация о сайте</h1>

          <nav class="d-flex gap-2 nav-pill">
            <a href="/">Главная</a>
            <a href="${NamedRoutes.urlsPath()}">Сайты</a>
          </nav>
        </header>

        <main class="content">

          <div class="card-soft mb-4">
            <h2 class="h5 fw-bold mb-3">${page.getUrl().getName()}</h2>

            <table class="table mb-0">
              <tbody>
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
              </tbody>
            </table>
          </div>

          <form action="${NamedRoutes.urlCheckPath(page.getUrl().getId())}" method="post" class="mb-4">
            <button type="submit" class="btn btn-dark btn-check">
              Запустить проверку →
            </button>
          </form>

          <h2 class="h5 fw-bold mb-3">Проверки</h2>

          <form action="/urls/${page.getUrl().getId()}/checks" method="post" class="mb-4">
            <button type="submit" class="btn btn-dark" style="border-radius:14px; font-weight:700; padding:10px 14px;">
              Запустить проверку →
            </button>
          </form>


          @if (pageChecks.getUrlChecks() == null || pageChecks.getUrlChecks().isEmpty())
            <div class="card-soft text-center opacity-75">
              Проверок пока нет
            </div>
          @else
            <div class="table-responsive">
              <table class="table align-middle mb-0">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Код</th>
                    <th>title</th>
                    <th>h1</th>
                    <th>description</th>
                    <th>Дата</th>
                  </tr>
                </thead>
                <tbody>
                  @for (UrlCheck check : pageChecks.getUrlChecks())
                    <tr>
                      <td>${check.getId()}</td>
                      <td>${check.getStatusCode()}</td>
                      <td>${check.getTitle()}</td>
                      <td>${check.getH1()}</td>
                      <td>${check.getDescription()}</td>
                      <td>${format(check.getCreatedAt())}</td>
                    </tr>
                  @endfor
                </tbody>
              </table>
            </div>
          @endif

        </main>

        <!-- FOOTER -->
        <footer class="footer">
          © Анализатор страниц
        </footer>

      </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
  </body>
</html>
