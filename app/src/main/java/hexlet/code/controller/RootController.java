package hexlet.code.controller;

import hexlet.code.dto.BasePage;
import io.javalin.http.Context;
import java.util.Collections;

public class RootController {
    public static void welcome(Context ctx) {
        var page = new BasePage();

        String flash = ctx.consumeSessionAttribute("flash");
        String type = ctx.consumeSessionAttribute("flash-type");

        if (flash != null && type != null) {
            switch (type.toLowerCase().trim()) {
                case "danger" -> page.setError(flash);
                case "success" -> page.setInfo(flash);
                case "info" -> page.setFlash(flash);
                default -> page.setFlash(flash);
            }
        }

        ctx.render("index.jte", Collections.singletonMap("page", page));
    }
}
