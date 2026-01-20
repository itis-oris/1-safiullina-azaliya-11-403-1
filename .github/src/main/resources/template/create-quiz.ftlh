<!DOCTYPE html>
<html>
<head>
    <title>Создать викторину</title>
    <style>
        body { font-family: Arial; max-width: 800px; margin: 0 auto; padding: 20px; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; }
        input[type="text"], input[type="number"], select, textarea { width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px; }
        textarea { height: 80px; }
        .question-block { background: #f9f9f9; padding: 15px; margin: 15px 0; border: 1px solid #ddd; border-radius: 5px; }
        .btn { background: #4CAF50; color: white; padding: 10px 20px; border: none; border-radius: 4px; cursor: pointer; margin: 5px; }
        .error { color: red; background: #ffe6e6; padding: 10px; border-radius: 4px; }
        .success { color: green; background: #e6ffe6; padding: 10px; border-radius: 4px; }
        .nav { margin-bottom: 20px; }
        .nav a { color: #0066cc; text-decoration: none; margin-right: 15px; }
        .correct-answer { background: #e6ffe6; padding: 10px; border-radius: 4px; margin: 10px 0; }
        .remove-btn { background: #f44336; color: white; padding: 5px 10px; border: none; border-radius: 4px; cursor: pointer; margin-top: 10px; }
    </style>
</head>
<body>
<div class="nav">
    <a href="${contextPath}/">Главная</a>
    <a href="${contextPath}/quizzes">Викторины</a>
    <a href="${contextPath}/profile">Профиль</a>
</div>

<h1>Создать викторину</h1>

<#if user?? && user.role == 'ADMIN'>
    <div class="info" style="background: #e6ffe6; color: green; padding: 10px; border-radius: 5px; margin: 10px 0;">
        ⚡ Вы администратор - ваша викторина будет опубликована сразу!
    </div>
</#if>

<#if error??>
    <div class="error">${error}</div>
</#if>
<#if message??>
    <div class="success">${message}</div>
</#if>

<form method="post">
    <div class="form-group">
        <label>Название викторины:</label>
        <input type="text" name="title" required>
    </div>
    <div class="form-group">
        <label>Описание:</label>
        <textarea name="description" required></textarea>
    </div>
    <div class="form-group">
        <label>Категория:</label>
        <select name="category" required>
            <option value="K-pop">K-pop</option>
            <option value="T-pop">T-pop</option>
            <option value="Корейские дорамы">Корейские дорамы</option>
            <option value="Тайские лакорны">Тайские лакорны</option>
            <option value="J-pop">J-pop</option>
            <option value="Другое">Другое</option>
        </select>
    </div>
    <div class="form-group">
        <label>Сложность (1-3):</label>
        <input type="number" name="difficulty" min="1" max="3" required>
    </div>
    <div class="form-group">
        <label>Лимит времени (минуты):</label>
        <input type="number" name="timeLimit" min="1" max="60" required>
    </div>

    <h3>Вопросы</h3>
    <div id="questions-container">
        <div class="question-block" data-question-index="1">
            <div class="form-group">
                <label>Текст вопроса:</label>
                <input type="text" name="questionText" required>
            </div>
            <div class="form-group">
                <label>Вариант 1:</label>
                <input type="text" name="option1" required>
            </div>
            <div class="form-group">
                <label>Вариант 2:</label>
                <input type="text" name="option2" required>
            </div>
            <div class="form-group">
                <label>Вариант 3:</label>
                <input type="text" name="option3" required>
            </div>
            <div class="form-group">
                <label>Вариант 4:</label>
                <input type="text" name="option4" required>
            </div>
            <div class="correct-answer">
                <label><strong>Правильный ответ:</strong></label>
                <div>
                    <label><input type="radio" name="correctAnswer_1" value="1" checked> Вариант 1</label>
                    <label><input type="radio" name="correctAnswer_1" value="2"> Вариант 2</label>
                    <label><input type="radio" name="correctAnswer_1" value="3"> Вариант 3</label>
                    <label><input type="radio" name="correctAnswer_1" value="4"> Вариант 4</label>
                </div>
            </div>
            <button type="button" class="remove-btn" onclick="removeQuestion(this)" style="display:none;">Удалить вопрос</button>
        </div>
    </div>

    <button type="button" onclick="addQuestion()" class="btn">+ Добавить вопрос</button>
    <button type="submit" class="btn">Создать викторину</button>
</form>

<script>
    let questionCount = 1;

    function addQuestion() {
        questionCount++;
        const container = document.getElementById('questions-container');
        const newQuestion = container.firstElementChild.cloneNode(true);

        newQuestion.setAttribute('data-question-index', questionCount);

        const textInputs = newQuestion.querySelectorAll('input[type="text"]');
        textInputs.forEach(input => input.value = '');

        const radios = newQuestion.querySelectorAll('input[type="radio"]');
        radios.forEach((radio, index) => {
            radio.name = 'correctAnswer_' + questionCount;
            radio.checked = (index === 0);
        });

        const removeBtn = newQuestion.querySelector('.remove-btn');
        removeBtn.style.display = 'block';

        container.appendChild(newQuestion);
    }

    function removeQuestion(button) {
        const questionBlock = button.closest('.question-block');
        if (document.querySelectorAll('.question-block').length > 1) {
            questionBlock.remove();
            reindexQuestions();
        } else {
            alert('Должен остаться хотя бы один вопрос!');
        }
    }

    function reindexQuestions() {
        const questions = document.querySelectorAll('.question-block');
        questionCount = questions.length;

        questions.forEach((question, index) => {
            const newIndex = index + 1;
            question.setAttribute('data-question-index', newIndex);

            const radios = question.querySelectorAll('input[type="radio"]');
            radios.forEach(radio => {
                radio.name = 'correctAnswer_' + newIndex;
            });
        });
    }

    document.querySelector('form').addEventListener('submit', function(e) {
        e.preventDefault();

        const submitBtn = this.querySelector('button[type="submit"]');
        const originalText = submitBtn.textContent;
        submitBtn.textContent = 'Создание...';
        submitBtn.disabled = true;

        const formData = new URLSearchParams();

        formData.append('title', document.querySelector('input[name="title"]').value || '');
        formData.append('description', document.querySelector('textarea[name="description"]').value || '');
        formData.append('category', document.querySelector('select[name="category"]').value || '');
        formData.append('difficulty', document.querySelector('input[name="difficulty"]').value || '1');
        formData.append('timeLimit', document.querySelector('input[name="timeLimit"]').value || '5');

        const questionBlocks = document.querySelectorAll('.question-block');
        questionBlocks.forEach((block, index) => {
            const questionIndex = index + 1;

            const questionText = block.querySelector('input[name="questionText"]').value || '';
            const option1 = block.querySelector('input[name="option1"]').value || '';
            const option2 = block.querySelector('input[name="option2"]').value || '';
            const option3 = block.querySelector('input[name="option3"]').value || '';
            const option4 = block.querySelector('input[name="option4"]').value || '';

            const correctAnswerRadio = block.querySelector('input[type="radio"]:checked');
            const correctAnswer = correctAnswerRadio ? correctAnswerRadio.value : '1';

            if (questionText.trim() !== '') {
                formData.append('questionText', questionText);
                formData.append('option1', option1);
                formData.append('option2', option2);
                formData.append('option3', option3);
                formData.append('option4', option4);
                formData.append('correctAnswer_' + questionIndex, correctAnswer);
            }
        });

        console.log('Отправляемые данные:');
        for (let [key, value] of formData.entries()) {
            console.log(key + ': ' + value);
        }

        if (!document.querySelector('input[name="title"]').value.trim()) {
            showMessage('Название викторины обязательно', null);
            submitBtn.textContent = originalText;
            submitBtn.disabled = false;
            return;
        }

        fetch('${contextPath}/create-quiz', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: formData
        })
            .then(response => {
                if (response.redirected) {
                    window.location.href = response.url;
                    return;
                }
                return response.text();
            })
            .then(html => {
                if (!html) return;

                const parser = new DOMParser();
                const doc = parser.parseFromString(html, 'text/html');
                document.body.innerHTML = doc.body.innerHTML;

                setTimeout(() => {
                    const newForm = document.querySelector('form');
                    if (newForm) {
                        newForm.addEventListener('submit', handleFormSubmit);
                    }
                }, 100);
            })
            .catch(error => {
                console.error('Error:', error);
                showMessage('Ошибка сети: ' + error.message, null);
                submitBtn.textContent = originalText;
                submitBtn.disabled = false;
            });
    });

    function showMessage(error, success) {
        const oldError = document.querySelector('.error');
        const oldSuccess = document.querySelector('.success');
        if (oldError) oldError.remove();
        if (oldSuccess) oldSuccess.remove();

        if (error) {
            const errorDiv = document.createElement('div');
            errorDiv.className = 'error';
            errorDiv.textContent = error;
            document.querySelector('h1').after(errorDiv);
        }

        if (success) {
            const successDiv = document.createElement('div');
            successDiv.className = 'success';
            successDiv.textContent = success;
            document.querySelector('h1').after(successDiv);
            setTimeout(() => successDiv.remove(), 5000);
        }
    }

    function handleFormSubmit(e) {
        e.preventDefault();

        const submitBtn = this.querySelector('button[type="submit"]');
        const originalText = submitBtn.textContent;
        submitBtn.textContent = 'Создание...';
        submitBtn.disabled = true;

        const formData = new URLSearchParams();

        formData.append('title', document.querySelector('input[name="title"]').value || '');
        formData.append('description', document.querySelector('textarea[name="description"]').value || '');
        formData.append('category', document.querySelector('select[name="category"]').value || '');
        formData.append('difficulty', document.querySelector('input[name="difficulty"]').value || '1');
        formData.append('timeLimit', document.querySelector('input[name="timeLimit"]').value || '5');

        const questionBlocks = document.querySelectorAll('.question-block');
        questionBlocks.forEach((block, index) => {
            const questionIndex = index + 1;

            const questionText = block.querySelector('input[name="questionText"]').value || '';
            const option1 = block.querySelector('input[name="option1"]').value || '';
            const option2 = block.querySelector('input[name="option2"]').value || '';
            const option3 = block.querySelector('input[name="option3"]').value || '';
            const option4 = block.querySelector('input[name="option4"]').value || '';

            const correctAnswerRadio = block.querySelector('input[type="radio"]:checked');
            const correctAnswer = correctAnswerRadio ? correctAnswerRadio.value : '1';

            if (questionText.trim() !== '') {
                formData.append('questionText', questionText);
                formData.append('option1', option1);
                formData.append('option2', option2);
                formData.append('option3', option3);
                formData.append('option4', option4);
                formData.append('correctAnswer_' + questionIndex, correctAnswer);
            }
        });

        if (!document.querySelector('input[name="title"]').value.trim()) {
            showMessage('Название викторины обязательно', null);
            submitBtn.textContent = originalText;
            submitBtn.disabled = false;
            return;
        }

        fetch('${contextPath}/create-quiz', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: formData
        })
            .then(response => {
                if (response.redirected) {
                    window.location.href = response.url;
                    return;
                }
                return response.text();
            })
            .then(html => {
                if (!html) return;

                const parser = new DOMParser();
                const doc = parser.parseFromString(html, 'text/html');
                document.body.innerHTML = doc.body.innerHTML;

                setTimeout(() => {
                    const newForm = document.querySelector('form');
                    if (newForm) {
                        newForm.addEventListener('submit', handleFormSubmit);
                    }
                }, 100);
            })
            .catch(error => {
                console.error('Error:', error);
                showMessage('Ошибка сети: ' + error.message, null);
                submitBtn.textContent = originalText;
                submitBtn.disabled = false;
            });
    }

    document.addEventListener('DOMContentLoaded', function() {
        const form = document.querySelector('form');
        if (form) {
            form.addEventListener('submit', handleFormSubmit);
        }
    });
</script>
</body>
</html>