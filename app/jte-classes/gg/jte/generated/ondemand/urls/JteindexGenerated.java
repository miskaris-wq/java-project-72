package gg.jte.generated.ondemand.urls;
import java.util.List;
import hexlet.code.model.Url;
public final class JteindexGenerated {
	public static final String JTE_NAME = "urls/index.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,2,2,15,15,15,16,16,16,17,17,21,21,21,2,2,2,2};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, List<Url> urls) {
		jteOutput.writeContent("\n<h1>List of added URLs</h1>\n\n\n@{\n    var flashMessage = ctx.consumeSessionAttribute(\"flash\");\n    if (flashMessage != null) {\n        <p style=\"color: green;\">@flashMessage</p>\n    }\n}\n\n<ul>\n");
		for (var url : urls) {
			jteOutput.writeContent("\n    <li>");
			jteOutput.setContext("li", null);
			jteOutput.writeUserContent(url.getName());
			jteOutput.writeContent("</li>\n");
		}
		jteOutput.writeContent("\n</ul>\n\n<p><a href=\"/\">Go back</a></p>\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		List<Url> urls = (List<Url>)params.get("urls");
		render(jteOutput, jteHtmlInterceptor, urls);
	}
}
