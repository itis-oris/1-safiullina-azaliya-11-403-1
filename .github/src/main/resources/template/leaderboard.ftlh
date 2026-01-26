<!DOCTYPE html>
<html>
<head>
    <title>Таблица лидеров</title>
    <style>
        body { font-family: Arial; max-width: 800px; margin: 0 auto; padding: 20px; }
        .nav { background: #f0f0f0; padding: 15px; border-radius: 5px; margin: 20px 0; }
        .nav a { margin-right: 15px; color: #0066cc; text-decoration: none; }
        table { width: 100%; border-collapse: collapse; margin: 20px 0; }
        th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background-color: #f2f2f2; }
        tr:hover { background-color: #f5f5f5; }
        .rank-1 { background-color: #FFD700; }
        .rank-2 { background-color: #C0C0C0; }
        .rank-3 { background-color: #CD7F32; }
        .error { color: red; background: #ffe6e6; padding: 10px; border-radius: 4px; }
    </style>
</head>
<body>
<h1>Таблица лидеров</h1>

<div class="nav">
    <a href="${contextPath}/">Главная</a>
    <a href="${contextPath}/quizzes">Викторины</a>
    <a href="${contextPath}/profile">Профиль</a>
</div>

<#if error??>
    <div class="error">${error}</div>
</#if>

<#if topUsers?? && topUsers?size gt 0>
    <table>
        <thead>
        <tr>
            <th>Место</th>
            <th>Пользователь</th>
            <th>Очки</th>
            <th>Роль</th>
        </tr>
        </thead>
        <tbody>
        <#list topUsers as user>
            <tr class="rank-${user?index + 1}">
                <td>${user?index + 1}</td>
                <td>${user.username}</td>
                <td><strong>${user.score}</strong></td>
                <td>${user.role}</td>
            </tr>
        </#list>
        </tbody>
    </table>
<#else>
    <p>Пока нет данных для таблицы лидеров</p>
</#if>
</body>
</html>