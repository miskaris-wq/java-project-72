package hexlet.code.controller;

import io.javalin.http.Context;

import java.util.HashMap;

public class RootController {
    public static void welcome(Context ctx) {
        var model = new HashMap<String, Object>();
        model.put("ctx", ctx);

        var error = ctx.consumeSessionAttribute("error");
        if (error != null) {
            model.put("error", error);
        }

        ctx.render("index.jte", model);
    }
}
