package gg.jte.generated.ondemand;
import io.javalin.http.Context;
public final class JteindexGenerated {
	public static final String JTE_NAME = "index.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,1,1,30,30,30,30,1,1,1,1};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, Context ctx) {
		jteOutput.writeContent("\n<h1>URL Analyzer</h1>\n\n@{\n    var flashMessage = ctx.consumeSessionAttribute(\"flash\");\n    if (flashMessage != null) {\n}\n<div style=\"color: green;\">@flashMessage</div>\n@{\n    }\n}\n\n@{\n    var errorMessage = ctx.consumeSessionAttribute(\"error\");\n    if (errorMessage != null) {\n}\n<div style=\"color: red;\">@errorMessage</div>\n@{\n    }\n}\n\n<form action=\"/urls\" method=\"post\">\n    <label for=\"url\">Enter URL:</label>\n    <input type=\"text\" id=\"url\" name=\"url\" required>\n    <button type=\"submit\">Submit</button>\n</form>\n\n<p><a href=\"/urls\">Show all URLs</a></p>\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		Context ctx = (Context)params.get("ctx");
		render(jteOutput, jteHtmlInterceptor, ctx);
	}
}
