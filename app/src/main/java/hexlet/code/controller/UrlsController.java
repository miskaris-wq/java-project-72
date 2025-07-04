package hexlet.code.controller;

import hexlet.code.database.Database;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.utils.UrlUtils;
import io.javalin.http.Context;
import kong.unirest.Unirest;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class UrlsController {
    private static final Logger LOG = LoggerFactory.getLogger(UrlsController.class);

    private UrlsController() {}

    public static void listUrls(Context ctx) throws SQLException {
        var urlRepository = new UrlRepository(Database.getDataSource());
        var urlCheckRepository = new UrlCheckRepository(Database.getDataSource());

        var urls = urlRepository.findAllOrderedById();
        Map<Long, UrlCheck> lastChecks = new HashMap<>();

        for (var url : urls) {
            Optional<UrlCheck> lastCheck = urlCheckRepository.findLastCheckByUrlId(url.getId());
            lastCheck.ifPresent(check -> lastChecks.put(url.getId(), check));
        }

        var model = new HashMap<String, Object>();
        model.put("urls", urls);
        model.put("lastChecks", lastChecks);

        String flash = ctx.consumeSessionAttribute("flash");
        if (flash != null) model.put("flash", flash);

        String info = ctx.consumeSessionAttribute("info");
        if (info != null) model.put("info", info);

        ctx.render("urls/index.jte", model);
    }

    public static void createUrl(Context ctx) throws SQLException {
        String inputUrl = ctx.formParam("url");
        if (inputUrl == null || inputUrl.trim().isEmpty()) {
            ctx.sessionAttribute("error", "URL не должен быть пустым");
            ctx.redirect("/");
            return;
        }

        try {
            String normalizedUrl = UrlUtils.normalizeUrl(inputUrl.trim());
            var repository = new UrlRepository(Database.getDataSource());

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

    public static void showUrl(Context ctx) throws SQLException {
        long id = ctx.pathParamAsClass("id", Long.class).get();
        var urlRepository = new UrlRepository(Database.getDataSource());
        var checkRepository = new UrlCheckRepository(Database.getDataSource());

        var urlOptional = urlRepository.findById(id);
        if (urlOptional.isEmpty()) {
            ctx.status(404).result("URL not found");
            return;
        }

        var checks = checkRepository.findAllByUrlId(id);
        ctx.render("urls/show.jte", Map.of(
                "url", urlOptional.get(),
                "checks", checks
        ));
    }

    public static void checkUrl(Context ctx) throws SQLException {
        long urlId = ctx.pathParamAsClass("id", Long.class).get();
        var urlRepository = new UrlRepository(Database.getDataSource());
        var urlOptional = urlRepository.findById(urlId);

        if (urlOptional.isEmpty()) {
            ctx.status(404).result("URL not found");
            return;
        }

        try {
            var response = Unirest.get(urlOptional.get().getName()).asString();
            var document = Jsoup.parse(response.getBody());

            var check = new UrlCheck();
            check.setUrlId(urlId);
            check.setStatusCode(response.getStatus());
            check.setTitle(document.title());
            check.setH1(document.selectFirst("h1") != null ? document.selectFirst("h1").text() : null);
            check.setDescription(document.selectFirst("meta[name=description]") != null
                    ? document.selectFirst("meta[name=description]").attr("content") : null);
            check.setCreatedAt(LocalDateTime.now());

            new UrlCheckRepository(Database.getDataSource()).save(check);
            ctx.sessionAttribute("flash", "Проверка успешно выполнена");
        } catch (Exception e) {
            LOG.error("Ошибка при проверке сайта", e);
            ctx.sessionAttribute("error", "Произошла ошибка при проверке сайта");
        }

        ctx.redirect("/urls/" + urlId);
    }
}
