package hexlet.code.controller;

import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;

public final class RootController {

    private RootController() { }

    public static void welcome(Context ctx) {
        Map<String, Object> model = new HashMap<>();
        model.put("ctx", ctx);

        String error = ctx.consumeSessionAttribute("error");
        if (error != null) {
            model.put("error", error);
        }

        ctx.render("index.jte", model);
    }
}
