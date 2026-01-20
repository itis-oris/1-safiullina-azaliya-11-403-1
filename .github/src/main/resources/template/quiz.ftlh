<!DOCTYPE html>
<html>
<head>
    <title>${quiz.title}</title>
    <style>
        body { font-family: Arial; max-width: 800px; margin: 0 auto; padding: 20px; }
        .quiz-header { background: #f0f0f0; padding: 15px; border-radius: 5px; margin-bottom: 20px; }
        .timer { background: #ffeb3b; padding: 10px; text-align: center; font-weight: bold; margin: 10px 0; }
        .question { margin-bottom: 30px; padding: 15px; border: 1px solid #ddd; border-radius: 5px; }
        .options label { display: block; margin: 8px 0; cursor: pointer; }
        .btn { background: #4CAF50; color: white; padding: 10px 20px; border: none; border-radius: 4px; cursor: pointer; }
        .nav { margin-top: 20px; }
        .nav a { color: #0066cc; text-decoration: none; margin-right: 15px; }
    </style>
</head>
<body>
<div class="quiz-header">
    <h1>${quiz.title}</h1>
    <p>${quiz.description}</p>
    <div>Категория: ${quiz.category} | Сложность: ${quiz.difficulty}/3</div>
</div>

<form id="quizForm" method="post" action="${contextPath}/quiz/results">
    <input type="hidden" name="quizId" value="${quiz.id}">
    <input type="hidden" name="timeSpent" id="timeSpent" value="0">
    <div class="timer">
        Время: <span id="timer">0</span> сек
    </div>

    <#list questions as question>
        <div class="question">
            <h3>Вопрос ${question?index + 1}: ${question.questionText}</h3>
            <div class="options">
                <label><input type="radio" name="question_${question.id}" value="1" required> ${question.option1}</label>
                <label><input type="radio" name="question_${question.id}" value="2"> ${question.option2}</label>
                <label><input type="radio" name="question_${question.id}" value="3"> ${question.option3}</label>
                <label><input type="radio" name="question_${question.id}" value="4"> ${question.option4}</label>
            </div>
        </div>
    </#list>

    <button type="submit" class="btn">Завершить викторину</button>
</form>

<div class="nav">
    <a href="${contextPath}/quizzes">← Назад к викторинам</a>
</div>

<script>
    let startTime = Date.now();
    let timerElement = document.getElementById('timer');
    let timeSpentElement = document.getElementById('timeSpent');

    function updateTimer() {
        let elapsed = Math.floor((Date.now() - startTime) / 1000);
        timerElement.textContent = elapsed;
        timeSpentElement.value = elapsed;
    }
    setInterval(updateTimer, 1000);
</script>
</body>
</html>