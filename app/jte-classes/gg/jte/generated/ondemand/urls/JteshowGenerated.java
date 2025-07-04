package gg.jte.generated.ondemand.urls;
import hexlet.code.model.Url;
import java.time.format.DateTimeFormatter;
import java.util.List;
import hexlet.code.model.UrlCheck;
import java.lang.String;
public final class JteshowGenerated {
	public static final String JTE_NAME = "urls/show.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,3,4,5,5,5,19,19,19,21,21,21,24,24,53,53,53,57,57,57,61,61,61,67,67,67,67,82,82,84,84,84,85,85,85,86,86,86,87,87,87,88,88,88,89,89,89,91,91,110,110,110,5,6,7,8,8,8,8};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, String error, List<UrlCheck> checks, Url url, io.javalin.http.Context ctx) {
		jteOutput.writeContent("\n\n<!DOCTYPE html><html><head>\n    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n    <title>Анализатор страниц</title>\n    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65\" crossorigin=\"anonymous\">\n    <script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js\" integrity=\"sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4\" crossorigin=\"anonymous\"></script>\n</head>\n\n");
		if (error != null) {
			jteOutput.writeContent("\n    <div class=\"alert alert-success alert-dismissible fade show rounded-0 flash-message\" role=\"alert\">\n        <p class=\"m-0\">");
			jteOutput.setContext("p", null);
			jteOutput.writeUserContent(error);
			jteOutput.writeContent("</p>\n        <button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"alert\" aria-label=\"Закрыть\"></button>\n    </div>\n");
		}
		jteOutput.writeContent("\n\n<body class=\"d-flex flex-column min-vh-100\">\n<nav class=\"navbar navbar-expand-lg navbar-dark bg-dark\">\n    <div class=\"container-fluid\">\n        <a class=\"navbar-brand\" href=\"/\">Анализатор страниц</a>\n        <button class=\"navbar-toggler\" type=\"button\" data-bs-toggle=\"collapse\" data-bs-target=\"#navbarNav\" aria-controls=\"navbarNav\" aria-expanded=\"false\" aria-label=\"Toggle navigation\">\n            <span class=\"navbar-toggler-icon\"></span>\n        </button>\n        <div class=\"collapse navbar-collapse\" id=\"navbarNav\">\n            <div class=\"navbar-nav\">\n                <a class=\"nav-link\" href=\"/\">Главная</a>\n                <a class=\"nav-link\" href=\"/urls\">Сайты</a>\n            </div>\n        </div>\n    </div>\n</nav>\n\n<main class=\"flex-grow-1\">\n\n    <section>\n\n        <div class=\"container-lg mt-5\">\n            <h1>Сайт: https://www.example.com</h1>\n\n            <table class=\"table table-bordered table-hover mt-3\">\n                <tbody>\n                <tr>\n                    <td>ID</td>\n                    <td>");
		jteOutput.setContext("td", null);
		jteOutput.writeUserContent(url.getId());
		jteOutput.writeContent("</td>\n                </tr>\n                <tr>\n                    <td>Имя</td>\n                    <td>");
		jteOutput.setContext("td", null);
		jteOutput.writeUserContent(url.getName());
		jteOutput.writeContent("</td>\n                </tr>\n                <tr>\n                    <td>Дата создания</td>\n                    <td>");
		jteOutput.setContext("td", null);
		jteOutput.writeUserContent(url.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		jteOutput.writeContent("</td>\n                </tr>\n                </tbody>\n            </table>\n\n            <h2 class=\"mt-5\">Проверки</h2>\n            <form method=\"post\" action=\"/urls/");
		jteOutput.setContext("form", "action");
		jteOutput.writeUserContent(url.getId());
		jteOutput.setContext("form", null);
		jteOutput.writeContent("/checks\">\n                <button type=\"submit\" class=\"btn btn-primary\">Запустить проверку</button>\n            </form>\n\n            <table class=\"table table-bordered table-hover mt-3\">\n                <thead>\n                <tr><th class=\"col-1\">ID</th>\n                    <th class=\"col-1\">Код ответа</th>\n                    <th>title</th>\n                    <th>h1</th>\n                    <th>description</th>\n                    <th class=\"col-2\">Дата проверки</th>\n                </tr></thead>\n                <tbody>\n                <tbody>\n                ");
		for (var check : checks) {
			jteOutput.writeContent("\n                <tr>\n                    <td>");
			jteOutput.setContext("td", null);
			jteOutput.writeUserContent(check.getId());
			jteOutput.writeContent("</td>\n                    <td>");
			jteOutput.setContext("td", null);
			jteOutput.writeUserContent(check.getStatusCode());
			jteOutput.writeContent("</td>\n                    <td>");
			jteOutput.setContext("td", null);
			jteOutput.writeUserContent(check.getTitle() != null ? check.getTitle() : "");
			jteOutput.writeContent("</td>\n                    <td>");
			jteOutput.setContext("td", null);
			jteOutput.writeUserContent(check.getH1() != null ? check.getH1() : "");
			jteOutput.writeContent("</td>\n                    <td>");
			jteOutput.setContext("td", null);
			jteOutput.writeUserContent(check.getDescription() != null ? check.getDescription() : "");
			jteOutput.writeContent("</td>\n                    <td>");
			jteOutput.setContext("td", null);
			jteOutput.writeUserContent(check.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			jteOutput.writeContent("</td>\n                </tr>\n                ");
		}
		jteOutput.writeContent("\n                </tbody>\n\n                </tbody>\n            </table>\n        </div>\n\n    </section>\n</main>\n\n<footer class=\"footer border-top py-3 mt-5 bg-light\">\n    <div class=\"container-xl\">\n        <div class=\"text-center\">\n            created by\n            <a href=\"https://ru.hexlet.io\" target=\"_blank\">Hexlet</a>\n        </div>\n    </div>\n</footer>\n\n</body></html>");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		String error = (String)params.get("error");
		List<UrlCheck> checks = (List<UrlCheck>)params.get("checks");
		Url url = (Url)params.get("url");
		io.javalin.http.Context ctx = (io.javalin.http.Context)params.get("ctx");
		render(jteOutput, jteHtmlInterceptor, error, checks, url, ctx);
	}
}
