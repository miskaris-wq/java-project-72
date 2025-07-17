package hexlet.code.controller;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.utils.UrlUtils;
import io.javalin.http.Context;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UrlsController {

    public static void list(Context ctx) throws SQLException {
        var urlRepository = new UrlRepository();
        var urlCheckRepository = new UrlCheckRepository();

        var urls = urlRepository.findAllOrderedById();
        Map<Long, UrlCheck> lastChecks = urlCheckRepository.findLatestChecks();

        var model = new HashMap<String, Object>();
        model.put("urls", urls);
        model.put("lastChecks", lastChecks);
        model.put("ctx", ctx);

        populateSessionMessages(ctx, model, "flash", "flash-type");

        ctx.render("urls/index.jte", model);
    }

    public static void create(Context ctx) throws SQLException {
        var inputUrl = ctx.formParam("url");

        if (inputUrl == null || inputUrl.trim().isEmpty()) {
            ctx.sessionAttribute("flash", "URL не должен быть пустым");
            ctx.sessionAttribute("flash-type", "danger");
            ctx.redirect("/");
            return;
        }

        String normalizedUrl;
        try {
            normalizedUrl = UrlUtils.normalizeUrl(inputUrl.trim());
        } catch (URISyntaxException e) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.sessionAttribute("flash-type", "danger");
            ctx.redirect("/");
            return;
        }

        var repository = new UrlRepository();

        if (repository.findByName(normalizedUrl).isPresent()) {
            ctx.sessionAttribute("flash", "Страница уже существует");
            ctx.sessionAttribute("flash-type", "warning");
            ctx.redirect("/urls");
            return;
        }

        var url = new Url();
        url.setName(normalizedUrl);
        repository.save(url);

        ctx.sessionAttribute("flash", "Страница успешно добавлена");
        ctx.sessionAttribute("flash-type", "success");
        ctx.redirect("/urls");
    }

    public static void show(Context ctx) throws SQLException {
        long id = ctx.pathParamAsClass("id", Long.class).get();

        var urlRepository = new UrlRepository();
        var checkRepository = new UrlCheckRepository();

        var urlOptional = urlRepository.findById(id);
        if (urlOptional.isEmpty()) {
            ctx.status(404).result("URL not found");
            return;
        }

        var checks = checkRepository.findAllByUrlId(id);

        var model = new HashMap<String, Object>();
        model.put("url", urlOptional.get());
        model.put("checks", checks);
        model.put("ctx", ctx);

        populateSessionMessages(ctx, model, "flash", "flash-type");

        ctx.render("urls/show.jte", model);
    }

    private static void populateSessionMessages(Context ctx, Map<String, Object> model, String... keys) {
        for (String key : keys) {
            var value = ctx.consumeSessionAttribute(key);
            if (value != null) {
                model.put(key, value);
            }
        }
    }
}
