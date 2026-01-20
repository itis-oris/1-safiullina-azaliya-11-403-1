<!DOCTYPE html>
<html>
<head>
    <title>Результаты викторины</title>
    <style>
        body { font-family: Arial; max-width: 600px; margin: 0 auto; padding: 20px; }
        .results-header { text-align: center; margin-bottom: 30px; }
        .score { font-size: 2em; color: #4CAF50; font-weight: bold; margin: 20px 0; }
        .stats { background: #f0f0f0; padding: 20px; border-radius: 5px; margin: 20px 0; }
        .stat-row { display: flex; justify-content: space-between; margin: 10px 0; }
        .btn { background: #4CAF50; color: white; padding: 10px 20px; text-decoration: none; border-radius: 4px; display: inline-block; margin: 5px; }
        .error { color: red; background: #ffe6e6; padding: 10px; border-radius: 4px; }
    </style>
</head>
<body>
<div class="results-header">
    <h1>Результаты викторины</h1>
    <h2>${quiz.title}</h2>
</div>

<#if error??>
    <div class="error">${error}</div>
</#if>

<div class="score">${score} очков</div>

<div class="stats">
    <div class="stat-row">
        <span>Правильные ответы:</span>
        <span><strong>${correctAnswers}/${totalQuestions}</strong></span>
    </div>
    <div class="stat-row">
        <span>Время выполнения:</span>
        <span><strong>${timeSpent} сек</strong></span>
    </div>
    <div class="stat-row">
        <span>Процент успеха:</span>
        <span><strong>
                <#if totalQuestions gt 0>
                    ${((correctAnswers/totalQuestions)*100)?string("0.#")}%
                <#else>
                    0%
                </#if>
            </strong></span>
    </div>
</div>

<div style="text-align: center;">
    <a href="${contextPath}/quizzes" class="btn">К викторинам</a>
    <a href="${contextPath}/leaderboard" class="btn">Таблица лидеров</a>
    <a href="${contextPath}/" class="btn">На главную</a>
</div>
</body>
</html>