package gg.jte.generated.ondemand;
import io.javalin.http.Context;
public final class JteindexGenerated {
	public static final String JTE_NAME = "index.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,1,1,32,32,32,35,35,35,38,38,81,81,81,1,2,2,2,2};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, Context ctx, String error) {
		jteOutput.writeContent("\n\n<!DOCTYPE html>\n<html>\n<head>\n    <meta charset=\"UTF-8\">\n    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n    <title>Анализатор страниц</title>\n    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\">\n    <script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js\"></script>\n</head>\n\n<body class=\"d-flex flex-column min-vh-100\">\n\n<nav class=\"navbar navbar-expand-lg navbar-dark bg-dark\">\n    <div class=\"container-fluid\">\n        <a class=\"navbar-brand\" href=\"/\">Анализатор страниц</a>\n        <button class=\"navbar-toggler\" type=\"button\" data-bs-toggle=\"collapse\" data-bs-target=\"#navbarNav\">\n            <span class=\"navbar-toggler-icon\"></span>\n        </button>\n        <div class=\"collapse navbar-collapse\" id=\"navbarNav\">\n            <div class=\"navbar-nav\">\n                <a class=\"nav-link\" href=\"/\">Главная</a>\n                <a class=\"nav-link\" href=\"/urls\">Сайты</a>\n            </div>\n        </div>\n    </div>\n</nav>\n\n");
		if (error != null) {
			jteOutput.writeContent("\n    <div class=\"alert alert-danger alert-dismissible fade show rounded-0 m-0\" role=\"alert\">\n\n        <p class=\"m-0\">");
			jteOutput.setContext("p", null);
			jteOutput.writeUserContent(error);
			jteOutput.writeContent("</p>\n        <button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"alert\" aria-label=\"Закрыть\"></button>\n    </div>\n");
		}
		jteOutput.writeContent("\n\n<main class=\"flex-grow-1\">\n\n    <section>\n        <div class=\"container-fluid bg-dark p-5\">\n            <div class=\"row\">\n                <div class=\"col-md-10 col-lg-8 mx-auto text-white\">\n                    <h1 class=\"display-3 mb-0\">Анализатор страниц</h1>\n                    <p class=\"lead\">Бесплатно проверяйте сайты на SEO пригодность</p>\n                    <form action=\"/urls\" method=\"post\" class=\"rss-form text-body\">\n                        <div class=\"row\">\n                            <div class=\"col\">\n                                <div class=\"form-floating\">\n                                    <input id=\"url-input\" type=\"text\" required name=\"url\" aria-label=\"url\"\n                                           class=\"form-control w-100\" placeholder=\"ссылка\" autocomplete=\"off\">\n                                    <label for=\"url-input\">Ссылка</label>\n                                </div>\n                            </div>\n                            <div class=\"col-auto\">\n\n                                <button type=\"submit\" class=\"h-100 btn btn-lg btn-primary px-sm-5\">Проверить</button>\n                            </div>\n                        </div>\n                    </form>\n                    <p class=\"mt-2 mb-0 text-muted\">Пример: https://www.example.com</p>\n                </div>\n            </div>\n        </div>\n\n    </section>\n\n\n</main>\n\n<footer class=\"footer border-top py-3 mt-3 bg-light\">\n    <div class=\"container-xl text-center text-dark\">\n        created by <a href=\"https://ru.hexlet.io\" target=\"_blank\">Hexlet</a>\n    </div>\n</footer>\n\n</body>\n</html>\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		Context ctx = (Context)params.get("ctx");
		String error = (String)params.get("error");
		render(jteOutput, jteHtmlInterceptor, ctx, error);
	}
}
