package gg.jte.generated.ondemand;
import hexlet.code.dto.BasePage;
import gg.jte.Content;
public final class JtelayoutGenerated {
	public static final String JTE_NAME = "layout.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,2,2,32,32,32,33,33,33,34,34,35,35,36,36,36,37,37,38,38,39,39,39,40,40,43,43,43,57,57,57,2,3,3,3,3};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, Content content, BasePage page) {
		jteOutput.writeContent("\n<!DOCTYPE html>\n<html lang=\"ru\">\n\n<head>\n    <meta charset=\"UTF-8\">\n    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n    <title>Анализатор страниц</title>\n    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\">\n    <script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js\"></script>\n</head>\n\n<body class=\"d-flex flex-column min-vh-100\">\n<nav class=\"navbar navbar-expand-lg navbar-dark bg-dark\">\n    <div class=\"container-fluid\">\n        <a class=\"navbar-brand\" href=\"/\">Анализатор страниц</a>\n        <button class=\"navbar-toggler\" type=\"button\" data-bs-toggle=\"collapse\" data-bs-target=\"#navbarNav\">\n            <span class=\"navbar-toggler-icon\"></span>\n        </button>\n        <div class=\"collapse navbar-collapse\" id=\"navbarNav\">\n            <div class=\"navbar-nav\">\n                <a class=\"nav-link\" href=\"/\">Главная</a>\n                <a class=\"nav-link\" href=\"/urls\">Сайты</a>\n            </div>\n        </div>\n    </div>\n</nav>\n\n");
		if (page != null && page.getFlash() != null) {
			jteOutput.writeContent("\n    <div class=\"alert alert-success\">");
			jteOutput.setContext("div", null);
			jteOutput.writeUserContent(page.getFlash());
			jteOutput.writeContent("</div>\n");
		}
		jteOutput.writeContent("\n");
		if (page != null && page.getError() != null) {
			jteOutput.writeContent("\n    <div class=\"alert alert-danger\">");
			jteOutput.setContext("div", null);
			jteOutput.writeUserContent(page.getError());
			jteOutput.writeContent("</div>\n");
		}
		jteOutput.writeContent("\n");
		if (page != null && page.getInfo() != null) {
			jteOutput.writeContent("\n    <div class=\"alert alert-info\">");
			jteOutput.setContext("div", null);
			jteOutput.writeUserContent(page.getInfo());
			jteOutput.writeContent("</div>\n");
		}
		jteOutput.writeContent("\n\n<main class=\"flex-grow-1\">\n    ");
		jteOutput.setContext("main", null);
		jteOutput.writeUserContent(content);
		jteOutput.writeContent("\n</main>\n\n<footer class=\"footer border-top py-3 mt-5 bg-light\">\n    <div class=\"container-xl\">\n        <div class=\"text-center\">\n            created by\n            <a href=\"https://ru.hexlet.io\" target=\"_blank\">Hexlet</a>\n        </div>\n    </div>\n</footer>\n\n</body>\n</html>\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		Content content = (Content)params.get("content");
		BasePage page = (BasePage)params.getOrDefault("page", null);
		render(jteOutput, jteHtmlInterceptor, content, page);
	}
}
