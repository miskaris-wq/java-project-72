@import hexlet.code.model.Url
@import java.time.format.DateTimeFormatter
@param Url url
@param io.javalin.http.Context ctx

<h1>URL Details</h1>
<p>ID: ${url.getId()}</p>
<p>Name: ${url.getName()}</p>
<p>Created At: ${url.getCreatedAt().toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))}</p>

<a href="/urls">Back to URLs list</a>