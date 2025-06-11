package gg.jte.generated.ondemand.urls;
import io.javalin.http.Context;
import java.util.List;
import hexlet.code.model.Url;
public final class JteindexGenerated {
	public static final String JTE_NAME = "urls/index.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,3,3,3,111,111,111,113,113,113,114,114,114,114,114,114,114,118,118,129,129,129,3,4,4,4,4};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, Context ctx, List<Url> urls) {
		jteOutput.writeContent("\n<!DOCTYPE html>\n<html lang=\"ru\">\n<head>\n    <meta charset=\"UTF-8\">\n    <title>Анализатор страниц</title>\n    <link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">\n    <link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin>\n    <link href=\"https://fonts.googleapis.com/css2?family=Roboto&display=swap\" rel=\"stylesheet\">\n    <style>\n        body {\n            margin: 0;\n            font-family: 'Roboto', sans-serif;\n            background-color: #ffffff;\n            color: #000000;\n        }\n\n        header {\n            background-color: #f5f5f5;\n            padding: 1rem 2rem;\n            font-size: 1.2rem;\n            display: flex;\n            gap: 2rem;\n            border-bottom: 1px solid #ccc;\n        }\n\n        header a {\n            color: #333;\n            text-decoration: none;\n        }\n\n        header a:hover {\n            text-decoration: underline;\n        }\n\n        .container {\n            padding: 2rem;\n        }\n\n        h1 {\n            font-size: 2.5rem;\n            margin-bottom: 2rem;\n        }\n\n        table {\n            width: 100%;\n            border-collapse: collapse;\n        }\n\n        th, td {\n            padding: 1rem;\n            border: 1px solid #ddd;\n            text-align: left;\n        }\n\n        th {\n            background-color: #f0f0f0;\n        }\n\n        a {\n            color: #0077cc;\n            text-decoration: none;\n        }\n\n        a:hover {\n            text-decoration: underline;\n        }\n\n        .alert-success {\n            color: #4caf50;\n            margin-bottom: 1rem;\n        }\n\n        footer {\n            margin-top: 100px;\n            text-align: center;\n            padding: 1rem;\n            color: #666;\n        }\n\n        footer a {\n            color: #333;\n        }\n    </style>\n</head>\n<body>\n\n<header>\n    <div><strong>Анализатор страниц</strong></div>\n    <a href=\"/\">Главная</a>\n    <a href=\"/urls\">Сайты</a>\n</header>\n\n<div class=\"container\">\n    <h1>Сайты</h1>\n\n    <table>\n        <thead>\n            <tr>\n                <th>ID</th>\n                <th>Имя</th>\n                <th>Последняя проверка</th>\n                <th>Код ответа</th>\n            </tr>\n        </thead>\n        <tbody>\n            ");
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
			jteOutput.writeContent("</a></td>\n                    <td>—</td>\n                    <td>—</td>\n                </tr>\n            ");
		}
		jteOutput.writeContent("\n        </tbody>\n    </table>\n</div>\n\n<footer>\n    created by <a href=\"https://hexlet.io\" target=\"_blank\">Hexlet</a>\n</footer>\n\n</body>\n</html>\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		Context ctx = (Context)params.get("ctx");
		List<Url> urls = (List<Url>)params.get("urls");
		render(jteOutput, jteHtmlInterceptor, ctx, urls);
	}
}
