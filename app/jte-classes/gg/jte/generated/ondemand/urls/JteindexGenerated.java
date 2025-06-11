package gg.jte.generated.ondemand.urls;
import io.javalin.http.Context;
import java.util.List;
import hexlet.code.model.Url;
public final class JteindexGenerated {
	public static final String JTE_NAME = "urls/index.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,3,3,3,61,61,61,63,63,63,66,66,68,68,70,70,70,73,73,92,92,94,94,94,95,95,95,95,95,95,95,99,99,115,115,115,3,4,5,6,6,6,6};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, Context ctx, List<Url> urls, String flash, String info) {
		jteOutput.writeContent("\n\n<!DOCTYPE html>\n<html>\n<head>\n    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n    <title>Анализатор страниц</title>\n    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65\" crossorigin=\"anonymous\">\n    <script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js\" integrity=\"sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4\" crossorigin=\"anonymous\"></script>\n    <head>\n        <meta charset=\"UTF-8\">\n        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n        <title>Анализатор страниц</title>\n        <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\">\n        <script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js\"></script>\n\n        <style>\n            .alert-info {\n                background-color: #cff4fc !important;\n                color: #055160 !important;\n                border-color: #b6effb !important;\n            }\n\n            .alert-success {\n                background-color: #d1e7dd !important;\n                color: #0f5132 !important;\n                border-color: #badbcc !important;\n            }\n\n            .flash-message {\n                margin-bottom: 1px;\n            }\n        </style>\n\n    </head>\n\n</head>\n<body class=\"d-flex flex-column min-vh-100\">\n<nav class=\"navbar navbar-expand-lg navbar-dark bg-dark\">\n    <div class=\"container-fluid\">\n        <a class=\"navbar-brand\" href=\"/\">Анализатор страниц</a>\n        <button class=\"navbar-toggler\" type=\"button\" data-bs-toggle=\"collapse\" data-bs-target=\"#navbarNav\" aria-controls=\"navbarNav\" aria-expanded=\"false\" aria-label=\"Toggle navigation\">\n            <span class=\"navbar-toggler-icon\"></span>\n        </button>\n        <div class=\"collapse navbar-collapse\" id=\"navbarNav\">\n            <div class=\"navbar-nav\">\n                <a class=\"nav-link\" href=\"/\">Главная</a>\n                <a class=\"nav-link\" href=\"/urls\">Сайты</a>\n            </div>\n        </div>\n    </div>\n</nav>\n\n");
		if (info != null) {
			jteOutput.writeContent("\n    <div class=\"alert alert-success alert-dismissible fade show rounded-0 flash-message\" role=\"alert\">\n        <p class=\"m-0\">");
			jteOutput.setContext("p", null);
			jteOutput.writeUserContent(info);
			jteOutput.writeContent("</p>\n        <button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"alert\" aria-label=\"Закрыть\"></button>\n    </div>\n");
		}
		jteOutput.writeContent("\n\n");
		if (flash != null) {
			jteOutput.writeContent("\n    <div class=\"alert alert-info alert-dismissible fade show rounded-0 flash-message\" role=\"alert\">\n        <p class=\"m-0\">");
			jteOutput.setContext("p", null);
			jteOutput.writeUserContent(flash);
			jteOutput.writeContent("</p>\n        <button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"alert\" aria-label=\"Закрыть\"></button>\n    </div>\n");
		}
		jteOutput.writeContent("\n\n<main class=\"flex-grow-1\">\n\n    <section>\n\n        <div class=\"container-lg mt-5\">\n            <h1>Сайты</h1>\n\n            <table class=\"table table-bordered table-hover mt-3\">\n                <thead>\n                <tr>\n                    <th class=\"col-1\">ID</th>\n                    <th>Имя</th>\n                    <th class=\"col-2\">Последняя проверка</th>\n                    <th class=\"col-1\">Код ответа</th>\n                </tr>\n                </thead>\n                <tbody>\n            ");
		for (var url : urls) {
			jteOutput.writeContent("\n                <tr>\n                    <td>");
			jteOutput.setContext("td", null);
			jteOutput.writeUserContent(url.getId());
			jteOutput.writeContent("</td>\n                    <td><a href=\"/urls/");
			jteOutput.setContext("a", "href");
			jteOutput.writeUserContent(url.getId());
			jteOutput.setContext("a", null);
			jteOutput.writeContent("\">");
			jteOutput.setContext("a", null);
			jteOutput.writeUserContent(url.getName());
			jteOutput.writeContent("</a></td>\n                    <td></td>\n                    <td></td>\n                </tr>\n            ");
		}
		jteOutput.writeContent("\n                </tbody>\n            </table>\n        </div>\n\n    </section>\n</main>\n\n<footer class=\"footer border-top py-3 mt-3 bg-light\">\n    <div class=\"container-xl text-center text-dark\">\n        created by <a href=\"https://ru.hexlet.io\" target=\"_blank\">Hexlet</a>\n    </div>\n</footer>\n\n</body>\n</html>\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		Context ctx = (Context)params.get("ctx");
		List<Url> urls = (List<Url>)params.get("urls");
		String flash = (String)params.get("flash");
		String info = (String)params.get("info");
		render(jteOutput, jteHtmlInterceptor, ctx, urls, flash, info);
	}
}
