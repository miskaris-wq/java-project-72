package hexlet.code.controller;

import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import io.javalin.http.Context;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;

import java.sql.SQLException;
import java.time.LocalDateTime;

@Slf4j
public class UrlChecksController {

    public static void check(Context ctx) throws SQLException {
        long urlId = ctx.pathParamAsClass("id", Long.class).get();

        var urlRepository = new UrlRepository(UrlRepository.dataSource);
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
                    ? document.selectFirst("h1").text() : null);
            check.setDescription(document.selectFirst("meta[name=description]") != null
                    ? document.selectFirst("meta[name=description]").attr("content") : null);
            check.setCreatedAt(LocalDateTime.now());

            new UrlCheckRepository(UrlCheckRepository.dataSource).save(check);
            ctx.sessionAttribute("flash", "Проверка успешно выполнена");
        } catch (Exception e) {
            log.error("Ошибка при проверке сайта", e);
            ctx.sessionAttribute("error", "Произошла ошибка при проверке сайта");
        }

        ctx.redirect("/urls/" + urlId);
    }
}
