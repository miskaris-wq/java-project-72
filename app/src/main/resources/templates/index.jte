@import io.javalin.http.Context
@param Context ctx

<h1>URL Analyzer</h1>

@if (ctx.consumeSessionAttribute("flash") != null)
    <div style="color: green;">
        ${ctx.sessionAttribute("flash").toString()}
    </div>
@endif

@if (ctx.consumeSessionAttribute("error") != null)
    <div style="color: red;">
        ${ctx.sessionAttribute("error").toString()}
    </div>
@endif

<form action="/urls" method="post">
    <label for="url">Enter URL:</label>
    <input type="text" id="url" name="url" required>
    <button type="submit">Submit</button>
</form>

<p><a href="/urls">Show all URLs</a></p>
