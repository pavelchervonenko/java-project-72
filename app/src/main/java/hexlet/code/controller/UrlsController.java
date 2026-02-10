package hexlet.code.controller;

import hexlet.code.dto.urls.UrlChecksPage;
import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;

import hexlet.code.model.Url;

import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;

import hexlet.code.util.NamedRoutes;

import static io.javalin.rendering.template.TemplateUtil.model;

import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import kong.unirest.core.Unirest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URI;
import java.net.URL;


public class UrlsController {
    public static void index(Context ctx) throws Exception {
        var urls = UrlRepository.getEntities();

        var lastChecks = UrlCheckRepository.findLatestChecks();

        var page = new UrlsPage(urls, lastChecks);
        page.setFlash(ctx.consumeSessionAttribute("flash"));

        ctx.render("urls/index.jte", model("page", page));
    }

    public static void show(Context ctx) throws Exception {
        var id = ctx.pathParamAsClass("id", Long.class).get();

        var url = UrlRepository.findById(id)
                .orElseThrow(() -> new NotFoundResponse("URL with id = " + id + " not found"));
        var page = new UrlPage(url);

        var urlChecks = UrlCheckRepository.findByUrlId(id);
        var pageChecks = new UrlChecksPage(urlChecks);

        ctx.render("urls/show.jte", model("page", page, "pageChecks", pageChecks));
    }

    public static void create(Context ctx) throws Exception {
        var param = ctx.formParam("url");

        if (param == null || param.isBlank()) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.redirect(NamedRoutes.rootPath());
            return;
        }

        URI uri;
        URL parsed;

        try {
            uri = new URI(param.trim());
            parsed = uri.toURL();

        } catch (Exception e) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.redirect(NamedRoutes.rootPath());
            return;
        }

        String protocol = parsed.getProtocol();
        String host = parsed.getHost();
        int port = parsed.getPort();

        if (protocol == null || protocol.isBlank()) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.redirect(NamedRoutes.rootPath());
            return;
        }
        if (host == null || host.isBlank()) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.redirect(NamedRoutes.rootPath());
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(protocol).append("://").append(host);

        if (port != -1) {
            sb.append(":").append(port);
        }

        String normalized = sb.toString();

        try {
            var existing = UrlRepository.findByName(normalized);
            if (existing.isPresent()) {
                ctx.sessionAttribute("flash", "Данный URL уже существует");
                ctx.redirect(NamedRoutes.rootPath());
            }
        } catch (Exception e) {
            ctx.sessionAttribute("flash", "Ошибка базы данных");
            ctx.redirect(NamedRoutes.rootPath());
        }

        var url = new Url(normalized);
        UrlRepository.save(url);

        ctx.sessionAttribute("flash", "URL успешно добавлен");
        ctx.redirect(NamedRoutes.urlsPath());
    }

    public static void check(Context ctx) throws Exception {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.findById(id)
                .orElseThrow(() -> new NotFoundResponse("URL with id = " + id + " not found"));

        try {
            var resp = Unirest.get(url.getName()).asString();
            int status = resp.getStatus();
            String body = resp.getBody();

            Document doc = Jsoup.parse(body);
            String title = doc.title();

            Element h1Element = doc.selectFirst("h1");
            String h1 = null;
            if (h1Element != null) {
                h1 = h1Element.text();
            }

            Element descriptionElement = doc.selectFirst("meta[name=description]");
            String description = null;
            if (descriptionElement != null) {
                description = descriptionElement.attr("content");
            }

            var check = new UrlCheck(url.getId(), status, title, h1, description);
            UrlCheckRepository.save(check);

            ctx.redirect(NamedRoutes.urlPath(id));
        } catch (Exception e) {
            ctx.sessionAttribute("flash", "Сервис недоступен");
            ctx.redirect(NamedRoutes.urlPath(id));
        }
    }
}
