<!DOCTYPE html>
<html>
<head>
    <title>Викторины</title>
    <style>
        body { font-family: Arial; max-width: 800px; margin: 0 auto; padding: 20px; }
        .nav { background: #f0f0f0; padding: 15px; border-radius: 5px; margin: 20px 0; }
        .nav a { margin-right: 15px; color: #0066cc; text-decoration: none; }
        .quiz-list { display: grid; gap: 15px; }
        .quiz-item { background: white; padding: 15px; border: 1px solid #ddd; border-radius: 5px; }
        .quiz-title { font-size: 18px; font-weight: bold; margin-bottom: 10px; }
        .quiz-meta { color: #666; font-size: 14px; margin-bottom: 10px; }
        .btn { background: #4CAF50; color: white; padding: 8px 15px; text-decoration: none; border-radius: 4px; display: inline-block; }
        .error { color: red; background: #ffe6e6; padding: 10px; border-radius: 4px; }
        .categories { margin-bottom: 20px; }
        .categories a { margin-right: 10px; color: #0066cc; text-decoration: none; }
    </style>
</head>
<body>
<h1>Викторины</h1>

<div class="nav">
    <a href="${contextPath}/">Главная</a>
    <a href="${contextPath}/profile">Профиль</a>
    <a href="${contextPath}/create-quiz">Создать викторину</a>
    <a href="${contextPath}/leaderboard">Таблица лидеров</a>
</div>

<#if error??>
    <div class="error">${error}</div>
</#if>

<div class="categories">
    <strong>Категории:</strong>
    <a href="${contextPath}/quizzes">Все</a>
    <a href="${contextPath}/quizzes?category=K-pop">K-pop</a>
    <a href="${contextPath}/quizzes?category=T-pop">T-pop</a>
    <a href="${contextPath}/quizzes?category=Корейские дорамы">Корейские дорамы</a>
    <a href="${contextPath}/quizzes?category=Тайские лакорны">Тайские лакорны</a>
    <a href="${contextPath}/quizzes?category=J-pop">J-pop</a>
    <a href="${contextPath}/quizzes?category=Другое">Другое</a>
</div>

<#if category??>
    <h2>Категория: ${category}</h2>
</#if>

<div class="quiz-list">
    <#if quizzes?? && quizzes?size gt 0>
        <#list quizzes as quiz>
            <div class="quiz-item">
                <div class="quiz-title">${quiz.title}</div>
                <div class="quiz-meta">
                    Категория: ${quiz.category} |
                    Сложность: ${quiz.difficulty}/3 |
                    Время: ${quiz.timeLimit} мин
                </div>
                <div class="quiz-description">${quiz.description}</div>
                <a href="${contextPath}/quiz?id=${quiz.id}" class="btn">Начать викторину</a>
            </div>
        </#list>
    <#else>
        <p>Нет доступных викторин</p>
    </#if>
</div>
</body>
</html>