package hexlet.code.controller;

import hexlet.code.dto.BasePage;
import io.javalin.http.Context;
import java.util.Collections;

public class RootController {
    public static void welcome(Context ctx) {
        var page = new BasePage();

        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setError(ctx.consumeSessionAttribute("error"));
        page.setInfo(ctx.consumeSessionAttribute("info"));

        ctx.render("index.jte", Collections.singletonMap("page", page));
    }
}
