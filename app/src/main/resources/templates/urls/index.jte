@import java.util.List
@import hexlet.code.model.Url
@param List<Url> urls

<h1>List of added URLs</h1>

<ul>
@for (var url : urls)
    <li>${url.getName()}</li>
@endfor
</ul>

<p><a href="/">Go back</a></p>
