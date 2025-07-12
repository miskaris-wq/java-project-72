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
import java.util.Optional;


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

        var flash = ctx.consumeSessionAttribute("flash");
        if (flash != null) {
            model.put("flash", flash);
        }

        var info = ctx.consumeSessionAttribute("info");
        if (info != null) {
            model.put("info", info);
        }

        ctx.render("urls/index.jte", model);
    }

    public static void create(Context ctx) throws SQLException {
        var inputUrl = ctx.formParam("url");

        if (inputUrl == null || inputUrl.trim().isEmpty()) {
            ctx.sessionAttribute("error", "URL не должен быть пустым");
            ctx.redirect("/");
            return;
        }

        try {
            String normalizedUrl = UrlUtils.normalizeUrl(inputUrl.trim());
            var repository = new UrlRepository();  // ✅

            if (repository.findByName(normalizedUrl).isPresent()) {
                ctx.sessionAttribute("flash", "Страница уже существует");
                ctx.redirect("/urls");
                return;
            }

            var url = new Url();
            url.setName(normalizedUrl);
            repository.save(url);

            ctx.sessionAttribute("info", "Страница успешно добавлена");
            ctx.redirect("/urls");

        } catch (URISyntaxException e) {
            ctx.sessionAttribute("error", "Некорректный URL");
            ctx.redirect("/");
        }
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

        var flash = ctx.consumeSessionAttribute("flash");
        if (flash != null) {
            model.put("flash", flash);
        }

        var error = ctx.consumeSessionAttribute("error");
        if (error != null) {
            model.put("error", error);
        }

        ctx.render("urls/show.jte", model);
    }

}
