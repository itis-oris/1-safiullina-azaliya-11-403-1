<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Вход - Asian QuizZone</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 0; background: #f5f5f5; }
        .header { background: #2c3e50; color: white; padding: 15px 0; }
        .container { max-width: 500px; margin: 50px auto; padding: 0 20px; }
        .card { background: white; padding: 40px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
        .form-group { margin-bottom: 20px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; color: #333; }
        input[type="text"], input[type="password"] {
            width: 100%; padding: 12px; border: 1px solid #ddd;
            border-radius: 5px; font-size: 16px; box-sizing: border-box;
        }
        .btn {
            background: #3498db; color: white; padding: 12px;
            border: none; border-radius: 5px; width: 100%;
            font-size: 16px; cursor: pointer;
        }
        .btn:hover { background: #2980b9; }
        .alert { padding: 15px; margin-bottom: 20px; border-radius: 5px; }
        .alert-error { background: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
        .text-center { text-align: center; }
        .logo { font-size: 24px; font-weight: bold; color: #e74c3c; text-decoration: none; }
    </style>
</head>
<body>
<div class="header">
    <div class="container">
        <a href="${contextPath}/" class="logo">🏮 Asian QuizZone</a>
    </div>
</div>

<div class="container">
    <div class="card">
        <h2 style="text-align: center; margin-bottom: 30px;">🔑 Вход в систему</h2>

        <#if error??>
            <div class="alert alert-error">❌ ${error}</div>
        </#if>

        <form method="post">
            <div class="form-group">
                <label>👤 Логин</label>
                <input type="text" name="username" required>
            </div>

            <div class="form-group">
                <label>🔒 Пароль</label>
                <input type="password" name="password" required>
            </div>

            <button type="submit" class="btn">Войти</button>
        </form>

        <div class="text-center" style="margin-top: 20px;">
            <p>Нет аккаунта? <a href="${contextPath}/register">Зарегистрируйтесь</a></p>
        </div>
    </div>
</div>
</body>
</html>