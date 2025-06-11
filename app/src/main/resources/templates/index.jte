@import io.javalin.http.Context
@param Context ctx
@param String error


<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Анализатор страниц</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</head>

<body class="d-flex flex-column min-vh-100">

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <a class="navbar-brand" href="/">Анализатор страниц</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <div class="navbar-nav">
                <a class="nav-link" href="/">Главная</a>
                <a class="nav-link" href="/urls">Сайты</a>
            </div>
        </div>
    </div>
</nav>

@if (error != null)
    <div class="alert alert-danger alert-dismissible fade show rounded-0 m-0" role="alert">

        <p class="m-0">${error}</p>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Закрыть"></button>
    </div>
@endif

<main class="flex-grow-1">

    <section>
        <div class="container-fluid bg-dark p-5">
            <div class="row">
                <div class="col-md-10 col-lg-8 mx-auto text-white">
                    <h1 class="display-3 mb-0">Анализатор страниц</h1>
                    <p class="lead">Бесплатно проверяйте сайты на SEO пригодность</p>
                    <form action="/urls" method="post" class="rss-form text-body">
                        <div class="row">
                            <div class="col">
                                <div class="form-floating">
                                    <input id="url-input" type="text" required name="url" aria-label="url"
                                           class="form-control w-100" placeholder="ссылка" autocomplete="off">
                                    <label for="url-input">Ссылка</label>
                                </div>
                            </div>
                            <div class="col-auto">

                                <button type="submit" class="h-100 btn btn-lg btn-primary px-sm-5">Проверить</button>
                            </div>
                        </div>
                    </form>
                    <p class="mt-2 mb-0 text-muted">Пример: https://www.example.com</p>
                </div>
            </div>
        </div>

    </section>


</main>

<footer class="footer border-top py-3 mt-3 bg-light">
    <div class="container-xl text-center text-dark">
        created by <a href="https://ru.hexlet.io" target="_blank">Hexlet</a>
    </div>
</footer>

</body>
</html>
