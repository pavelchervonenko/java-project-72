@import hexlet.code.dto.IndexPage
@param IndexPage page

<!DOCTYPE html>
<html lang="ru">
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Главная — Анализатор страниц</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">

    <style>
      :root {
        --lime: #D9FF3A;
        --ink: #0B0B0D;
        --soft: #15151A;
        --line: rgba(255,255,255,.12);
      }

      body {
        background:
          radial-gradient(1200px 600px at 25% -10%, rgba(217,255,58,.18), rgba(0,0,0,0)),
          radial-gradient(900px 500px at 90% 10%, rgba(217,255,58,.10), rgba(0,0,0,0)),
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
        padding: 16px 18px;
        border-bottom: 1px solid rgba(0,0,0,.10);
        background: #fff;
      }

      .brand-center {
        position: absolute;
        left: 50%;
        transform: translateX(-50%);
        top: 12px;
        font-weight: 800;
        letter-spacing: -0.03em;
        margin: 0;
        line-height: 1.1;
      }

      .subtitle-left {
        max-width: 360px;
        font-weight: 600;
        opacity: .78;
        margin: 0;
      }

      .chip {
        display: inline-flex;
        align-items: center;
        gap: 8px;
        padding: 8px 12px;
        border-radius: 999px;
        background: rgba(217,255,58,.35);
        border: 1px solid rgba(0,0,0,.12);
        font-weight: 700;
        font-size: .9rem;
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

      .hero {
        padding: 26px;
      }

      .hero-card {
        border-radius: 22px;
        background: linear-gradient(180deg, #0B0B0D 0%, #101014 100%);
        color: #fff;
        border: 1px solid rgba(255,255,255,.10);
        box-shadow: 0 18px 40px rgba(0,0,0,.22);
        overflow: hidden;
      }

      .hero-inner {
        padding: 26px;
      }

      .hero-title {
        font-weight: 800;
        letter-spacing: -0.03em;
        line-height: 1.05;
        font-size: clamp(28px, 3vw, 44px);
        margin: 0 0 10px 0;
      }

      .hero-text {
        opacity: .82;
        margin: 0 0 18px 0;
        max-width: 44ch;
      }

      .btn-lime {
        background: var(--lime);
        border: 1px solid rgba(255,255,255,.16);
        font-weight: 800;
        border-radius: 14px;
        padding: 12px 14px;
      }
      .btn-lime:hover { filter: brightness(.96); }

      .input-dark {
        background: rgba(255,255,255,.08);
        border: 1px solid rgba(255,255,255,.12);
        color: #fff;
        border-radius: 14px;
        padding: 12px 12px;
      }
      .input-dark::placeholder { color: rgba(255,255,255,.55); }

      .alert-soft {
        border-radius: 16px;
        border: 1px solid rgba(0,0,0,.10);
      }

      .mini-grid .mini {
        border-radius: 18px;
        border: 1px solid rgba(0,0,0,.10);
        background: #fff;
        padding: 16px;
      }

      .mini .k {
        font-weight: 800;
        letter-spacing: -0.02em;
        margin: 0;
      }
      .mini .d { margin: 6px 0 0 0; opacity: .75; }

      .footer {
        padding: 18px 22px;
        border-top: 1px solid rgba(0,0,0,.10);
        background: #fff;
        color: rgba(0,0,0,.72);
        font-weight: 600;
      }
    </style>
  </head>

  <body>
    <div class="container py-4 py-md-5">
      <div class="frame">

        <header class="topbar">
          <div class="d-flex align-items-start justify-content-between gap-3">
            <h1 class="brand-center">Анализатор страниц</h1>

            <nav class="d-flex align-items-center gap-2 nav-pill">
              <a href="/">Главная</a>
              <a href="/urls">Сайты</a>
            </nav>
          </div>
        </header>

        <main class="hero">
          @if(page.getFlash() != null)
            <div class="alert alert-info alert-soft mb-4" role="alert">
              ${page.getFlash()}
            </div>
          @endif

          <div class="row g-4 align-items-stretch">
            <!-- LEFT: HERO CARD -->
            <div class="col-12">
              <div class="hero-card h-100">
                <div class="hero-inner">
                  <h2 class="hero-title">Вставь ссылку — получи результат</h2>
                  <p class="hero-text">
                    Сохраняем сайты, делаем проверки и показываем, что нашлось: <b>status</b>, <b>title</b>, <b>h1</b>, <b>description</b>.
                  </p>

                  <form action="/urls" method="post" class="mt-3">
                    <div class="d-flex flex-column flex-sm-row gap-2">
                      <input
                        type="text"
                        name="url"
                        class="form-control input-dark"
                        placeholder="Ссылка"
                        required
                      >
                      <button class="btn btn-lime" type="submit">Проверить →</button>
                    </div>
                  </form>
                </div>
              </div>
            </div>
          </div>
        </main>

        <footer class="footer d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center gap-2">
          <div>© Анализатор страниц</div>
          <div class="d-flex gap-2">
            <span class="badge rounded-pill text-bg-light" style="border:1px solid rgba(0,0,0,.12);">Javalin</span>
            <span class="badge rounded-pill text-bg-light" style="border:1px solid rgba(0,0,0,.12);">JTE</span>
            <span class="badge rounded-pill text-bg-light" style="border:1px solid rgba(0,0,0,.12);">Bootstrap</span>
          </div>
        </footer>

      </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI"
            crossorigin="anonymous"></script>
  </body>
</html>
