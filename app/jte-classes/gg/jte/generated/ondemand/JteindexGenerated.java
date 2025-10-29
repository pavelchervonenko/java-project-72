package gg.jte.generated.ondemand;
import hexlet.code.dto.IndexPage;
@SuppressWarnings("unchecked")
public final class JteindexGenerated {
	public static final String JTE_NAME = "index.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,1,1,1,25,25,25,27,27,27,29,29,46,46,46,1,1,1,1};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, IndexPage page) {
		jteOutput.writeContent("\r\n<!DOCTYPE html>\r\n<html lang=\"ru\">\r\n    <head>\r\n      <meta charset=\"UTF-8\">\r\n      <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n      <title>Главная страница</title>\r\n\r\n      <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB\" crossorigin=\"anonymous\">\r\n    </head>\r\n\r\n    <body>\r\n\r\n        <header>\r\n            <h1>Анализатор страниц</h1>\r\n            <h3>Бесплатно проверяйте сайты на SEO пригодность</h3>\r\n            <nav>\r\n                <a href=\"/\">Главная</a>\r\n                <a href=\"/urls\">Сайты</a>\r\n            </nav>\r\n        </header>\r\n\r\n        <main>\r\n            ");
		if (page.getFlash() != null) {
			jteOutput.writeContent("\r\n                <div class=\"alert alert-info\" role=\"alert\">\r\n                    ");
			jteOutput.setContext("div", null);
			jteOutput.writeUserContent(page.getFlash());
			jteOutput.writeContent("\r\n                </div>\r\n            ");
		}
		jteOutput.writeContent("\r\n\r\n            <form action=\"/urls\" method=\"post\">\r\n                <div class=\"input-group\">\r\n                    <input type=\"text\"\r\n                           name=\"url\"\r\n                           class=\"form-control\"\r\n                           placeholder=\"Ссылка\"\r\n                           required>\r\n                    <button class=\"btn\" type=\"submit\">Проверить</button>\r\n                </div>\r\n            </form>\r\n        </main>\r\n\r\n        <script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js\" integrity=\"sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI\" crossorigin=\"anonymous\"></script>\r\n\r\n    </body>\r\n</html>");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		IndexPage page = (IndexPage)params.get("page");
		render(jteOutput, jteHtmlInterceptor, page);
	}
}
