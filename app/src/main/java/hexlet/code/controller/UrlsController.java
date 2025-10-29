package hexlet.code.controller;

import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;

import hexlet.code.model.Url;

import hexlet.code.repository.UrlRepository;

import hexlet.code.util.NamedRoutes;

import static io.javalin.rendering.template.TemplateUtil.model;

import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.net.URI;
import java.net.URL;

public class UrlsController {
    public static void index(Context ctx) throws Exception {
        var urls = UrlRepository.getEntities();
        var page = new UrlsPage(urls);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        ctx.render("urls/index.jte", model("page", page));
    }

    public static void show(Context ctx) throws Exception {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.findById(id)
                .orElseThrow(() -> new NotFoundResponse("URL with id = " + id + " not found"));
        var page = new UrlPage(url);
        ctx.render("urls/show.jte", model("page", page));
    }

    public static void create(Context ctx) {
        var param = ctx.formParam("url");

        if (param == null || param.isBlank()) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.redirect(NamedRoutes.rootPath());
            return;
        }

        String normalized = "";
        try {
            URI uri = new URI(param.trim());
            URL parsed = uri.toURL();

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

            normalized = sb.toString();

        } catch (Exception e) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.redirect(NamedRoutes.rootPath());
        }

        try {
            var existing = UrlRepository.findByName(normalized);

            if (existing.isPresent()) {
                ctx.sessionAttribute("flash", "Данный URL уже существует");
                ctx.redirect(NamedRoutes.rootPath());
            }

            var url = new Url(normalized);
            UrlRepository.save(url);

            ctx.sessionAttribute("flash", "URL успешно добавлен");
            ctx.redirect(NamedRoutes.urlsPath());

        } catch (Exception e) {
            ctx.sessionAttribute("flash", "Ошибка базы данных");
            ctx.redirect(NamedRoutes.rootPath());
        }
    }
}
