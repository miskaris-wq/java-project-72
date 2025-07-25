@import hexlet.code.dto.BasePage
@import hexlet.code.model.Url
@import hexlet.code.model.UrlCheck
@import java.util.List
@import java.time.format.DateTimeFormatter

@param BasePage page
@param Url url
@param List<UrlCheck> checks

@template.layout(
page = page,
content = @`
    <section>
        <div class="container-lg mt-5">
            <h1>Сайт: ${url.getName()}</h1>

            <table class="table table-bordered table-hover mt-3">
                <tbody>
                <tr>
                    <td>ID</td>
                    <td>${url.getId()}</td>
                </tr>
                <tr>
                    <td>Имя</td>
                    <td>${url.getName()}</td>
                </tr>
                <tr>
                    <td>Дата создания</td>
                    <td>${url.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))}</td>
                </tr>
                </tbody>
            </table>

            <h2 class="mt-5">Проверки</h2>

            <form method="post" action="/urls/${url.getId()}/checks">
                <button type="submit" class="btn btn-primary">Запустить проверку</button>
            </form>

            <table class="table table-bordered table-hover mt-3">
                <thead>
                <tr>
                    <th class="col-1">ID</th>
                    <th class="col-1">Код ответа</th>
                    <th>title</th>
                    <th>h1</th>
                    <th>description</th>
                    <th class="col-2">Дата проверки</th>
                </tr>
                </thead>
                <tbody>
                @for (var check : checks)
                    <tr>
                        <td>${check.getId()}</td>
                        <td>${check.getStatusCode()}</td>
                        <td>${check.getTitle() != null ? check.getTitle() : ""}</td>
                        <td>${check.getH1() != null ? check.getH1() : ""}</td>
                        <td>${check.getDescription() != null ? check.getDescription() : ""}</td>
                        <td>${check.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))}</td>
                    </tr>
                @endfor
                </tbody>
            </table>
        </div>
    </section>
`)
