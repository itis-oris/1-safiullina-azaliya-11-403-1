<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Asian QuizZone</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 0; background: #f5f5f5; }
        .header { background: #2c3e50; color: white; padding: 15px 0; }
        .container { max-width: 1000px; margin: 0 auto; padding: 0 20px; }
        .nav { display: flex; justify-content: space-between; align-items: center; }
        .logo { font-size: 24px; font-weight: bold; color: #e74c3c; text-decoration: none; }
        .nav-links a { color: white; margin-left: 20px; text-decoration: none; }
        .hero { background: #3498db; color: white; padding: 60px 0; text-align: center; }
        .hero h1 { font-size: 48px; margin: 0 0 20px 0; }
        .hero p { font-size: 20px; margin: 0 0 30px 0; }
        .btn {
            background: #e74c3c; color: white; padding: 12px 30px;
            text-decoration: none; border-radius: 5px; font-size: 18px;
            display: inline-block; margin: 0 10px;
        }
        .btn-outline { background: transparent; border: 2px solid white; }
        .features { display: flex; gap: 20px; margin: 50px 0; }
        .feature { flex: 1; background: white; padding: 30px; border-radius: 8px; text-align: center; }
        .feature i { font-size: 40px; color: #3498db; margin-bottom: 15px; }
        .alert { padding: 15px; margin: 20px 0; border-radius: 5px; }
        .alert-success { background: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
        .alert-error { background: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
    </style>
</head>
<body>
<div class="header">
    <div class="container">
        <div class="nav">
            <a href="${contextPath}/" class="logo">🏮 Asian QuizZone</a>
            <div class="nav-links">
                <#if user??>
                    <a href="${contextPath}/profile" class="nav-link">Мой Профиль</a>
                </#if>
                <#if user?? && user.role == 'ADMIN'>
                    <a href="${contextPath}/admin/admin-quizzes">Управление викторинами</a>
                </#if>
                <#if user??>
                    <span>👤 ${user.username} (${user.score} pts)</span>
                    <a href="${contextPath}/quizzes">🎮Викторины</a>
                    <a href="${contextPath}/leaderboard">🏆 Лидеры</a>
                    <a href="${contextPath}/logout">🚪 Выйти</a>
                <#else>
                    <a href="${contextPath}/login">🔑 Войти</a>
                    <a href="${contextPath}/register">📝 Регистрация</a>
                </#if>
            </div>
        </div>
    </div>
</div>

<div class="hero">
    <div class="container">
        <h1>Добро пожаловать в Asian QuizZone! 🎼 </h1>
        <p>Тестируйте свои знания об азиатских группах и актерах дорам</p>
        <#if user??>
            <a href="${contextPath}/quizzes" class="btn">🎮 Начать викторину</a>
        <#else>
            <a href="${contextPath}/register" class="btn">📝 Присоединиться</a>
            <a href="${contextPath}/quizzes" class="btn btn-outline">👀 Смотреть викторины</a>
        </#if>
    </div>
</div>

<div class="container">
    <#if message??>
        <div class="alert alert-success">✅ ${message}</div>
    </#if>
    <#if error??>
        <div class="alert alert-error">❌ ${error}</div>
    </#if>

    <div class="features">
        <div class="feature">
            <div>🎤</div>
            <h3>Азиатские Группы</h3>
            <p>BTS, LYKN, &TEAM, TWICE, SEVENTEEN и другие!</p>
            <a href="${contextPath}/quizzes?category=K-pop">K-pop викторины</a>
        </div>
        <div class="feature">
            <div>🎬</div>
            <h3>Дорамы</h3>
            <p>Угадайте дораму по актеру или сюжету</p>
            <a href="${contextPath}/quizzes?category=Drama">Дорамы</a>
        </div>
        <div class="feature">
            <div>🏆</div>
            <h3>Соревнуйтесь</h3>
            <p>Поднимайтесь в таблице лидеров</p>
            <a href="${contextPath}/leaderboard">Таблица лидеров</a>
        </div>
    </div>
</div>
</body>
</html>