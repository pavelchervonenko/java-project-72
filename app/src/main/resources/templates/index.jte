@import hexlet.code.dto.IndexPage
@param IndexPage page

<!DOCTYPE html>
<html lang="ru">
    <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <title>Главная страница</title>

      <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">
    </head>

    <body>

        <header>
            <h1>Анализатор страниц</h1>
            <h5>Бесплатно проверяйте сайты на SEO пригодность</h5>
            <nav>
                <a href="/">Главная</a>
                <a href="/urls">Сайты</a>
            </nav>
        </header>

        <main>
            @if(page.getFlash() != null)
                <div class="alert alert-info" role="alert">
                    ${page.getFlash()}
                </div>
            @endif

            <form action="/urls" method="post">
                <div class="input-group">
                    <input type="text"
                           name="url"
                           class="form-control"
                           placeholder="Ссылка"
                           required>
                    <button class="btn" type="submit">Проверить</button>
                </div>
            </form>
        </main>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js" integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI" crossorigin="anonymous"></script>

    </body>
</html>