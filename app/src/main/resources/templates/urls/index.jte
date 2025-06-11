@import io.javalin.http.Context
@import java.util.List
@import hexlet.code.model.Url
@param Context ctx
@param List<Url> urls

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Анализатор страниц</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet">
    <style>
        body {
            margin: 0;
            font-family: 'Roboto', sans-serif;
            background-color: #ffffff;
            color: #000000;
        }

        header {
            background-color: #f5f5f5;
            padding: 1rem 2rem;
            font-size: 1.2rem;
            display: flex;
            gap: 2rem;
            border-bottom: 1px solid #ccc;
        }

        header a {
            color: #333;
            text-decoration: none;
        }

        header a:hover {
            text-decoration: underline;
        }

        .container {
            padding: 2rem;
        }

        h1 {
            font-size: 2.5rem;
            margin-bottom: 2rem;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th, td {
            padding: 1rem;
            border: 1px solid #ddd;
            text-align: left;
        }

        th {
            background-color: #f0f0f0;
        }

        a {
            color: #0077cc;
            text-decoration: none;
        }

        a:hover {
            text-decoration: underline;
        }

        .alert-success {
            color: #4caf50;
            margin-bottom: 1rem;
        }

        footer {
            margin-top: 100px;
            text-align: center;
            padding: 1rem;
            color: #666;
        }

        footer a {
            color: #333;
        }
    </style>
</head>
<body>

<header>
    <div><strong>Анализатор страниц</strong></div>
    <a href="/">Главная</a>
    <a href="/urls">Сайты</a>
</header>

<div class="container">
    <h1>Сайты</h1>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Имя</th>
                <th>Последняя проверка</th>
                <th>Код ответа</th>
            </tr>
        </thead>
        <tbody>
            @for (var url : urls)
                <tr>
                    <td>${url.getId()}</td>
                    <td><a href="/urls/${url.getId()}">${url.getName()}</a></td>
                    <td>—</td>
                    <td>—</td>
                </tr>
            @endfor
        </tbody>
    </table>
</div>

<footer>
    created by <a href="https://hexlet.io" target="_blank">Hexlet</a>
</footer>

</body>
</html>
