@import io.javalin.http.Context
@param Context ctx

<h1>URL Analyzer</h1>

@{
    var errorMessage = ctx.consumeSessionAttribute("error");
    if (errorMessage != null) {
}
<div style="color: red;">@errorMessage</div>
@{
    }
}

<form action="/urls" method="post">
    <label for="url">Enter URL:</label>
    <input type="text" id="url" name="url" required>
    <button type="submit">Submit</button>
</form>

<p><a href="/urls">Show all URLs</a></p>
