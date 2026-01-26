<!DOCTYPE html>
<html>
<head>
    <title>Профиль пользователя</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
        }
        h1 {
            color: #333;
        }
        .user-info {
            background: #f0f0f0;
            padding: 15px;
            margin: 20px 0;
            border-radius: 5px;
        }
        .stats {
            display: flex;
            gap: 20px;
            margin: 15px 0;
        }
        .stat {
            background: white;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            text-align: center;
            min-width: 120px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
        }
        th, td {
            padding: 8px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #f2f2f2;
        }
        .error {
            color: red;
            background: #ffe6e6;
            padding: 10px;
            border-radius: 5px;
        }
        .info {
            color: #0066cc;
            background: #e6f2ff;
            padding: 10px;
            border-radius: 5px;
            margin: 10px 0;
        }
        .links {
            margin-top: 20px;
        }
        .links a {
            margin-right: 15px;
            color: #0066cc;
            text-decoration: none;
        }
        .links a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<h1>Профиль пользователя</h1>

<#if error??>
    <div class="error">${error}</div>
</#if>

<div class="user-info">
    <h2>${user.username}</h2>
    <div class="stats">
        <div class="stat">
            <strong>${user.score}</strong>
            <div>Общий счет</div>
        </div>
        <div class="stat">
            <strong>${userResults?size}</strong>
            <div>Пройдено викторин</div>
        </div>
        <div class="stat">
            <strong>${user.role}</strong>
            <div>Роль</div>
        </div>
    </div>
</div>

<h3>История викторин</h3>
<#if userResults?? && userResults?size gt 0>
    <table>
        <thead>
        <tr>
            <th>ID викторины</th>
            <th>Счет</th>
            <th>Правильные ответы</th>
            <th>Всего вопросов</th>
            <th>Время (сек)</th>
            <th>Успешность</th>
        </tr>
        </thead>
        <tbody>
        <#list userResults as result>
            <tr>
                <td>${result.quizId}</td>
                <td><strong>${result.score}</strong></td>
                <td>${result.correctAnswers}/${result.totalQuestions}</td>
                <td>${result.totalQuestions}</td>
                <td>${result.timeSpent}</td>
                <td>
                    <#if result.totalQuestions gt 0>
                        ${((result.correctAnswers/result.totalQuestions)*100)?string("0.#")}%
                    <#else>
                        0%
                    </#if>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
<#else>
    <div class="info">
        Вы еще не прошли ни одной викторины.
        <a href="${contextPath}/quizzes">Начните свою первую викторину!</a>
    </div>
</#if>

<div class="links">
    <a href="${contextPath}/">←На главную</a>
    <a href="${contextPath}/quizzes">К викторинам</a>
    <a href="${contextPath}/leaderboard">Таблица лидеров</a>
</div>
</body>
</html>