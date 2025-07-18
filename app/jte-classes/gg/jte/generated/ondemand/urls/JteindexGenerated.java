package gg.jte.generated.ondemand.urls;
import hexlet.code.dto.BasePage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import gg.jte.Content;
public final class JteindexGenerated {
	public static final String JTE_NAME = "urls/index.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,3,4,5,6,8,8,8,12,12,14,14,28,28,30,30,30,31,31,31,31,31,31,31,33,33,34,34,34,35,35,38,38,39,39,39,40,40,43,43,48,48,48,49,49,49,8,9,10,10,10,10};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, BasePage page, List<Url> urls, Map<Long, UrlCheck> lastChecks) {
		jteOutput.writeContent("\n");
		gg.jte.generated.ondemand.JtelayoutGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\n    <section>\n        <div class=\"container-lg mt-5\">\n            <h1>Сайты</h1>\n            <table class=\"table table-bordered table-hover mt-3\">\n                <thead>\n                <tr>\n                    <th class=\"col-1\">ID</th>\n                    <th>Имя</th>\n                    <th class=\"col-2\">Последняя проверка</th>\n                    <th class=\"col-1\">Код ответа</th>\n                </tr>\n                </thead>\n                <tbody>\n                ");
				for (var url : urls) {
					jteOutput.writeContent("\n                    <tr>\n                        <td>");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(url.getId());
					jteOutput.writeContent("</td>\n                        <td><a href=\"/urls/");
					jteOutput.setContext("a", "href");
					jteOutput.writeUserContent(url.getId());
					jteOutput.setContext("a", null);
					jteOutput.writeContent("\">");
					jteOutput.setContext("a", null);
					jteOutput.writeUserContent(url.getName());
					jteOutput.writeContent("</a></td>\n                        <td>\n                            ");
					if (lastChecks.get(url.getId()) != null) {
						jteOutput.writeContent("\n                                ");
						jteOutput.setContext("td", null);
						jteOutput.writeUserContent(lastChecks.get(url.getId()).getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
						jteOutput.writeContent("\n                            ");
					}
					jteOutput.writeContent("\n                        </td>\n                        <td>\n                            ");
					if (lastChecks.get(url.getId()) != null) {
						jteOutput.writeContent("\n                                ");
						jteOutput.setContext("td", null);
						jteOutput.writeUserContent(lastChecks.get(url.getId()).getStatusCode());
						jteOutput.writeContent("\n                            ");
					}
					jteOutput.writeContent("\n                        </td>\n                    </tr>\n                ");
				}
				jteOutput.writeContent("\n                </tbody>\n            </table>\n        </div>\n    </section>\n");
			}
		}, page);
		jteOutput.writeContent("\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		BasePage page = (BasePage)params.get("page");
		List<Url> urls = (List<Url>)params.get("urls");
		Map<Long, UrlCheck> lastChecks = (Map<Long, UrlCheck>)params.get("lastChecks");
		render(jteOutput, jteHtmlInterceptor, page, urls, lastChecks);
	}
}
