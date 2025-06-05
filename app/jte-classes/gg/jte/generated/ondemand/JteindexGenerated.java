package gg.jte.generated.ondemand;
import io.javalin.http.Context;
public final class JteindexGenerated {
	public static final String JTE_NAME = "index.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,1,1,5,5,5,7,7,7,9,9,11,11,13,13,13,15,15,24,24,24,1,1,1,1};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, Context ctx) {
		jteOutput.writeContent("\n<h1>URL Analyzer</h1>\n\n");
		if (ctx.consumeSessionAttribute("flash") != null) {
			jteOutput.writeContent("\n    <div style=\"color: green;\">\n        ");
			jteOutput.setContext("div", null);
			jteOutput.writeUserContent(ctx.sessionAttribute("flash").toString());
			jteOutput.writeContent("\n    </div>\n");
		}
		jteOutput.writeContent("\n\n");
		if (ctx.consumeSessionAttribute("error") != null) {
			jteOutput.writeContent("\n    <div style=\"color: red;\">\n        ");
			jteOutput.setContext("div", null);
			jteOutput.writeUserContent(ctx.sessionAttribute("error").toString());
			jteOutput.writeContent("\n    </div>\n");
		}
		jteOutput.writeContent("\n\n<form action=\"/urls\" method=\"post\">\n    <label for=\"url\">Enter URL:</label>\n    <input type=\"text\" id=\"url\" name=\"url\" required>\n    <button type=\"submit\">Submit</button>\n</form>\n\n<p><a href=\"/urls\">Show all URLs</a></p>\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		Context ctx = (Context)params.get("ctx");
		render(jteOutput, jteHtmlInterceptor, ctx);
	}
}
