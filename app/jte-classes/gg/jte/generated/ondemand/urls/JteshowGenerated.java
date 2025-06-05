package gg.jte.generated.ondemand.urls;
import hexlet.code.model.Url;
import java.time.format.DateTimeFormatter;
public final class JteshowGenerated {
	public static final String JTE_NAME = "urls/show.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,2,2,6,6,6,6,7,7,7,8,8,8,10,10,10,2,3,3,3,3};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, Url url, io.javalin.http.Context ctx) {
		jteOutput.writeContent("\n<h1>URL Details</h1>\n<p>ID: ");
		jteOutput.setContext("p", null);
		jteOutput.writeUserContent(url.getId());
		jteOutput.writeContent("</p>\n<p>Name: ");
		jteOutput.setContext("p", null);
		jteOutput.writeUserContent(url.getName());
		jteOutput.writeContent("</p>\n<p>Created At: ");
		jteOutput.setContext("p", null);
		jteOutput.writeUserContent(url.getCreatedAt().toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		jteOutput.writeContent("</p>\n\n<a href=\"/urls\">Back to URLs list</a>");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		Url url = (Url)params.get("url");
		io.javalin.http.Context ctx = (io.javalin.http.Context)params.get("ctx");
		render(jteOutput, jteHtmlInterceptor, url, ctx);
	}
}
