@import java.util.List
@import hexlet.code.model.Url
@param List<Url> urls

<h1>List of added URLs</h1>

<ul>
@for (url : urls)
    <li>${url.getName()}</li>
@end
</ul>

<p><a href="/">Go back</a></p>
