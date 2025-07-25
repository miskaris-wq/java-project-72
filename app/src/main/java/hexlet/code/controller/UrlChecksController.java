package hexlet.code.controller;

import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import io.javalin.http.Context;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;

import java.sql.SQLException;
import java.util.Objects;

@Slf4j
public class UrlChecksController {

    public static void check(Context ctx) throws SQLException {
        long urlId = ctx.pathParamAsClass("id", Long.class).get();

        var urlRepository = new UrlRepository();
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
            check.setH1(document.selectFirst("h1") != null
                    ? Objects.requireNonNull(document.selectFirst("h1")).text() : null);
            check.setDescription(document.selectFirst("meta[name=description]") != null
                    ? Objects.requireNonNull(document.selectFirst("meta[name=description]"))
                    .attr("content") : null);

            var checkRepository = new UrlCheckRepository();
            checkRepository.save(check);

            ctx.sessionAttribute("flash", "Страница успешно проверена");
            ctx.sessionAttribute("flash-type", "success");
        } catch (Exception e) {
            log.error("Ошибка при проверке сайта", e);
            ctx.sessionAttribute("flash", "Некорректный адрес");
            ctx.sessionAttribute("flash-type", "danger");
        }

        ctx.redirect("/urls/" + urlId);
    }
}
