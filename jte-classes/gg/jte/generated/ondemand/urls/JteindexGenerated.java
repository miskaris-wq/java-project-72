package gg.jte.generated.ondemand.urls;
public final class JteindexGenerated {
	public static final String JTE_NAME = "urls/index.jte";
	public static final int[] JTE_LINE_INFO = {10,10,10,10,10,10,12,12,12,12,12,12,12,26,26,26,27,27,27,27,27,27,27,28,28,28,37,37,37,37,37,37};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor) {
		jteOutput.writeContent("<!DOCTYPE html>\n<html lang=\"ru\">\n<head>\n    <meta charset=\"UTF-8\">\n    <title>Список URL-адресов</title>\n    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\">\n</head>\n<body class=\"container mt-5\">\n    <h1 class=\"mb-4\">Список страниц</h1>\n\n    ");
		jteOutput.writeContent("\n    <#if flash??>\n        <div class=\"alert alert-");
		jteOutput.setContext("div", "class");
		jteOutput.writeUserContent(flash.type);
		jteOutput.setContext("div", null);
		jteOutput.writeContent("\">");
		jteOutput.setContext("div", null);
		jteOutput.writeUserContent(flash.message);
		jteOutput.writeContent("</div>\n    </#if>\n\n    <table class=\"table table-bordered\">\n        <thead>\n        <tr>\n            <th>ID</th>\n            <th>Имя</th>\n            <th>Дата добавления</th>\n        </tr>\n        </thead>\n        <tbody>\n        <#for url : urls>\n            <tr>\n                <td>");
		jteOutput.setContext("td", null);
		jteOutput.writeUserContent(url.id);
		jteOutput.writeContent("</td>\n                <td><a href=\"/urls/");
		jteOutput.setContext("a", "href");
		jteOutput.writeUserContent(url.id);
		jteOutput.setContext("a", null);
		jteOutput.writeContent("\">");
		jteOutput.setContext("a", null);
		jteOutput.writeUserContent(url.name);
		jteOutput.writeContent("</a></td>\n                <td>");
		jteOutput.setContext("td", null);
		jteOutput.writeUserContent(url.createdAt);
		jteOutput.writeContent("</td>\n            </tr>\n        </#for>\n        </tbody>\n    </table>\n\n    <a href=\"/\" class=\"btn btn-secondary mt-3\">На главную</a>\n</body>\n</html>\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		render(jteOutput, jteHtmlInterceptor);
	}
}
