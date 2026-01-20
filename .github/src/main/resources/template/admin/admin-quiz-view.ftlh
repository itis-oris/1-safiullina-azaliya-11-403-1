<!DOCTYPE html>
<html>
<head>
    <title>Просмотр викторины: ${quiz.title!''}</title>
    <style>
        body { font-family: Arial; max-width: 800px; margin: 0 auto; padding: 20px; }
        .quiz-header { background: #f0f0f0; padding: 20px; border-radius: 8px; margin-bottom: 20px; }
        .question { margin-bottom: 25px; padding: 20px; border: 1px solid #ddd; border-radius: 8px; background: white; }
        .correct-answer { background: #d4edda; padding: 10px; border-radius: 5px; margin-top: 10px; font-weight: bold; border-left: 4px solid #28a745; }
        .option { padding: 8px; margin: 5px 0; border-radius: 4px; }
        .option.correct { background: #d4edda; border-left: 4px solid #28a745; }
        .nav { margin-top: 30px; padding-top: 20px; border-top: 1px solid #ddd; }
        .nav a { color: #0066cc; text-decoration: none; margin-right: 20px; padding: 8px 16px; background: #f8f9fa; border-radius: 4px; }
        .quiz-meta { display: flex; gap: 15px; margin: 15px 0; flex-wrap: wrap; }
        .meta-item { background: #e9ecef; padding: 5px 12px; border-radius: 15px; font-size: 14px; }
    </style>
</head>
<body>
<div class="quiz-header">
    <p>${quiz.description!'Нет описания'}</p>

    <div class="quiz-meta">
        <span class="meta-item">📁 ${quiz.category!'Не указана'}</span>
        <span class="meta-item">🎯 Сложность: ${quiz.difficulty!1}/3</span>
        <span class="meta-item">⏱️ ${quiz.timeLimit!5} мин</span>
        <span class="meta-item">
            Статус:
            <#if quiz.approved?? && quiz.approved>
                ✅ Одобрена
            <#else>
                ⏳ На модерации
            </#if>
        </span>
    </div>

    <#if quiz.creator??>
        <div><strong>👤 Создатель:</strong> ${quiz.creator.username!'Неизвестен'}</div>
    </#if>
</div>

<#if questions?? && questions?size gt 0>
    <#list questions as question>
        <div class="question">
            <h3>Вопрос ${question?index + 1}: ${question.questionText!''}</h3>

            <div>
                <div class="option <#if question.correctAnswer?? && question.correctAnswer == 1>correct</#if>">
                    1. ${question.option1!''} <#if question.correctAnswer?? && question.correctAnswer == 1>✅</#if>
                </div>
                <div class="option <#if question.correctAnswer?? && question.correctAnswer == 2>correct</#if>">
                    2. ${question.option2!''} <#if question.correctAnswer?? && question.correctAnswer == 2>✅</#if>
                </div>
                <div class="option <#if question.correctAnswer?? && question.correctAnswer == 3>correct</#if>">
                    3. ${question.option3!''} <#if question.correctAnswer?? && question.correctAnswer == 3>✅</#if>
                </div>
                <div class="option <#if question.correctAnswer?? && question.correctAnswer == 4>correct</#if>">
                    4. ${question.option4!''} <#if question.correctAnswer?? && question.correctAnswer == 4>✅</#if>
                </div>
            </div>

            <div class="correct-answer">
                ✅ <strong>Правильный ответ:</strong> вариант ${question.correctAnswer!1}
                | <strong>Баллы:</strong> ${question.points!10}
                <#if question.questionOrder??>| <strong>Порядок:</strong> ${question.questionOrder + 1}</#if>
            </div>
        </div>
    </#list>
<#else>
    <div style="text-align: center; padding: 40px; background: #f8f9fa; border-radius: 8px;">
        <h3>📭 В этой викторине нет вопросов</h3>
        <p>Добавьте вопросы через редактирование викторины</p>
    </div>
</#if>

<div class="nav">
    <a href="${contextPath}/admin/admin-quizzes">← Назад к админ-панели</a>
    <a href="${contextPath}/quizzes">📚 К викторинам</a>
    <a href="${contextPath}/quiz/${quiz.id!0}">👀 Посмотреть как пользователь</a>
</div>
</body>
</html>