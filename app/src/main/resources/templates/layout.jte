@import hexlet.code.dto.BasePage
@import gg.jte.Content
@param Content content
@param BasePage page = null

<!DOCTYPE html>
<html lang="ru">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Анализатор страниц</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
    <style>
        .alert-info {
            background-color: #cff4fc !important;
            color: #055160 !important;
            border-color: #b6effb !important;
        }

        .flash-message {
            margin-bottom: 1px;
        }
    </style>
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

@if (page != null && page.getError() != null)
    <div class="alert alert-danger alert-dismissible fade show rounded-0 flash-message" role="alert">
        <p class="m-0">${page.getError()}</p>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Закрыть"></button>
    </div>
@endif

@if (page != null && page.getInfo() != null)
    <div class="alert alert-success alert-dismissible fade show rounded-0 flash-message" role="alert">
        <p class="m-0">${page.getInfo()}</p>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Закрыть"></button>
    </div>
@endif

@if (page != null && page.getFlash() != null)
    <div class="alert alert-info alert-dismissible fade show rounded-0 flash-message" role="alert">
        <p class="m-0">${page.getFlash()}</p>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Закрыть"></button>
    </div>
@endif


<main class="flex-grow-1">
    ${content}
</main>

<footer class="footer border-top py-3 mt-5 bg-light">
    <div class="container-xl text-center text-dark">
        created by <a href="https://ru.hexlet.io" target="_blank">Hexlet</a>
    </div>
</footer>

</body>
</html>
