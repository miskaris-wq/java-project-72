@import hexlet.code.dto.BasePage
@import hexlet.code.model.Url
@import hexlet.code.model.UrlCheck
@import java.time.format.DateTimeFormatter
@import java.util.List
@import java.util.Map
@import gg.jte.Content

@param BasePage page
@param List<Url> urls
@param Map<Long, UrlCheck> lastChecks

@template.layout(
page = page,
content = @`
    <section>
        <div class="container-lg mt-5">
            <h1>Сайты</h1>
            <table class="table table-bordered table-hover mt-3">
                <thead>
                <tr>
                    <th class="col-1">ID</th>
                    <th>Имя</th>
                    <th class="col-2">Последняя проверка</th>
                    <th class="col-1">Код ответа</th>
                </tr>
                </thead>
                <tbody>
                @for (var url : urls)
                    <tr>
                        <td>${url.getId()}</td>
                        <td><a href="/urls/${url.getId()}">${url.getName()}</a></td>
                        <td>
                            @if (lastChecks.get(url.getId()) != null)
                                ${lastChecks.get(url.getId()).getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))}
                            @endif
                        </td>
                        <td>
                            @if (lastChecks.get(url.getId()) != null)
                                ${lastChecks.get(url.getId()).getStatusCode()}
                            @endif
                        </td>
                    </tr>
                @endfor
                </tbody>
            </table>
        </div>
    </section>
`)
