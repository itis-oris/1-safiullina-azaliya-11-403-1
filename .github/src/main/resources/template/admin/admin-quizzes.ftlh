<!DOCTYPE html>
<html>
<head>
    <title>Админ-панель - Управление викторинами</title>
    <style>
        body { font-family: Arial; max-width: 1200px; margin: 0 auto; padding: 20px; }
        .header { background: #333; color: white; padding: 20px; border-radius: 8px; margin-bottom: 30px; }
        .section { margin-bottom: 40px; }
        .section-title { background: #f0f0f0; padding: 15px; border-radius: 5px; margin-bottom: 20px; font-size: 1.3em; }
        .quiz-list { display: grid; gap: 20px; }
        .quiz-item { border: 1px solid #ddd; padding: 20px; border-radius: 8px; background: white; transition: all 0.3s ease; }
        .quiz-item.removing { opacity: 0.5; background: #fff3cd; }
        .quiz-item.removed { display: none; }
        .quiz-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 15px; }
        .quiz-title { font-size: 1.4em; margin: 0; color: #333; }
        .quiz-meta { display: flex; flex-wrap: wrap; gap: 10px; margin: 10px 0; }
        .meta-item { background: #f5f5f5; padding: 5px 12px; border-radius: 15px; font-size: 0.9em; }
        .creator-info { background: #e3f2fd; padding: 8px 15px; border-radius: 5px; margin: 10px 0; }
        .actions { margin-top: 15px; display: flex; gap: 10px; flex-wrap: wrap; }
        .btn { padding: 8px 16px; border: none; border-radius: 4px; cursor: pointer; text-decoration: none; display: inline-block; font-size: 14px; }
        .btn:disabled { opacity: 0.6; cursor: not-allowed; }
        .btn-approve { background: #4CAF50; color: white; }
        .btn-reject { background: #f44336; color: white; }
        .btn-delete { background: #ff5722; color: white; }
        .btn-view { background: #9C27B0; color: white; }
        .status-approved { color: #4CAF50; font-weight: bold; }
        .status-pending { color: #ff9800; font-weight: bold; }
        .empty-state { text-align: center; padding: 40px; background: #f9f9f9; border-radius: 8px; color: #666; }
        .notification {
            position: fixed;
            top: 20px;
            right: 20px;
            padding: 15px 20px;
            border-radius: 5px;
            z-index: 1000;
            max-width: 400px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.15);
            transition: all 0.3s ease;
        }
        .notification.success { background: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
        .notification.error { background: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
        .loading { display: inline-block; width: 16px; height: 16px; border: 2px solid #f3f3f3; border-top: 2px solid #3498db; border-radius: 50%; animation: spin 1s linear infinite; margin-right: 8px; }
        @keyframes spin { 0% { transform: rotate(0deg); } 100% { transform: rotate(360deg); } }
    </style>
</head>
<body>
<div class="header">
    <h1>⚙️ Админ-панель - Управление викторинами</h1>
    <p>Управление одобренными викторинами и модерация новых</p>
</div>

<div id="notifications"></div>

<#if message??>
    <div class="notification success">
        ✅ ${message}
    </div>
</#if>
<#if error??>
    <div class="notification error">
        ❌ ${error}
    </div>
</#if>

<div class="section">
    <div class="section-title">
        📋 Викторины на модерации (<span id="pending-count">${pendingQuizzes?size}</span>)
    </div>

    <#if pendingQuizzes?? && pendingQuizzes?size gt 0>
        <div class="quiz-list" id="pending-quizzes">
            <#list pendingQuizzes as quiz>
                <div class="quiz-item" id="quiz-${quiz.id}">
                    <div class="quiz-header">
                        <h3 class="quiz-title">${quiz.title!''}</h3>
                        <span class="status-pending">⏳ На модерации</span>
                    </div>

                    <#if quiz.description??>
                        <p>${quiz.description}</p>
                    </#if>

                    <div class="quiz-meta">
                        <span class="meta-item">📁 ${quiz.category!''}</span>
                        <span class="meta-item">🎯 Сложность: ${quiz.difficulty!1}/3</span>
                        <span class="meta-item">⏱️ ${quiz.timeLimit!5} мин</span>
                    </div>


                    <div class="creator-info">
                        <strong>👤 Создатель:</strong>
                        <#if quiz.creator?? && quiz.creator.username??>
                            ${quiz.creator.username}
                        <#else>
                            ID: ${quiz.createdBy!0}
                        </#if>
                    </div>

                    <div class="actions">
                        <button type="button" class="btn btn-approve" onclick="handleQuizAction(${quiz.id?c}, 'approve', this)">
                            ✅ Одобрить
                        </button>
                        <button type="button" class="btn btn-reject" onclick="handleQuizAction(${quiz.id?c}, 'reject', this)">
                            ❌ Отклонить
                        </button>
                        <a href="${contextPath}/quiz/admin/${quiz.id!0}" class="btn btn-view">👁️ Просмотреть</a>
                        <button type="button" class="btn btn-delete" onclick="handleQuizAction(${quiz.id?c}, 'delete', this)">
                            🗑️ Удалить
                        </button>
                    </div>
                </div>
            </#list>
        </div>
    <#else>
        <div class="empty-state">
            <h3>🎉 Нет викторин на модерации</h3>
            <p>Все викторины проверены и одобрены</p>
        </div>
    </#if>
</div>

<div class="section">
    <div class="section-title">
        ✅ Одобренные викторины (<span id="approved-count">${approvedQuizzes?size}</span>)
    </div>

    <#if approvedQuizzes?? && approvedQuizzes?size gt 0>
        <div class="quiz-list" id="approved-quizzes">
            <#list approvedQuizzes as quiz>
                <div class="quiz-item" id="quiz-${quiz.id}">
                    <div class="quiz-header">
                        <h3 class="quiz-title">${quiz.title!''}</h3>
                        <span class="status-approved">✅ Одобрена</span>
                    </div>

                    <#if quiz.description??>
                        <p>${quiz.description}</p>
                    </#if>

                    <div class="quiz-meta">
                        <span class="meta-item">📁 ${quiz.category!''}</span>
                        <span class="meta-item">🎯 Сложность: ${quiz.difficulty!1}/3</span>
                        <span class="meta-item">⏱️ ${quiz.timeLimit!5} мин</span>
                    </div>

                    <div class="creator-info">
                        <strong>👤 Создатель:</strong>
                        <#if quiz.creator?? && quiz.creator.username??>
                            ${quiz.creator.username}
                        <#else>
                            ID: ${quiz.createdBy!0}
                        </#if>
                    </div>

                    <div class="actions">
                        <a href="${contextPath}/quiz/admin/${quiz.id!0}" class="btn btn-view">👁️ Просмотреть</a>
                        <button type="button" class="btn btn-delete" onclick="handleQuizAction(${quiz.id?c}, 'delete', this)">
                            🗑️ Удалить
                        </button>
                    </div>
                </div>
            </#list>
        </div>
    <#else>
        <div class="empty-state">
            <h3>📭 Нет одобренных викторин</h3>
            <p>Одобренные викторины появятся здесь после модерации</p>
        </div>
    </#if>
</div>

<div style="text-align: center; margin-top: 40px; padding-top: 20px; border-top: 1px solid #ddd;">
    <a href="${contextPath}/" class="btn">🏠 На главную</a>
    <a href="${contextPath}/quizzes" class="btn">📚 К викторинам</a>
</div>

<script>
    function showNotification(message, isSuccess = true) {
        const notifications = document.getElementById('notifications');
        const notification = document.createElement('div');
        notification.className = isSuccess ? 'notification success' : 'notification error';
        notification.innerHTML = isSuccess ? '✅ ' + message : '❌ ' + message;

        notifications.appendChild(notification);

        setTimeout(() => {
            notification.style.opacity = '0';
            setTimeout(() => {
                if (notification.parentNode) {
                    notification.parentNode.removeChild(notification);
                }
            }, 300);
        }, 5000);
    }

    function updateCounters() {
        const pendingCount = document.querySelectorAll('#pending-quizzes .quiz-item:not(.removed)').length;
        const approvedCount = document.querySelectorAll('#approved-quizzes .quiz-item:not(.removed)').length;

        document.getElementById('pending-count').textContent = pendingCount;
        document.getElementById('approved-count').textContent = approvedCount;
    }

    async function handleQuizAction(quizId, action, button) {
        const quizElement = document.getElementById('quiz-' + quizId);
        const originalText = button.innerHTML;

        if (action === 'delete' && !confirm('Вы уверены, что хотите удалить эту викторину?')) {
            return;
        }

        button.disabled = true;
        button.innerHTML = '<span class="loading"></span> Обработка...';
        quizElement.classList.add('removing');

        try {
            const params = new URLSearchParams();
            params.append('quizId', quizId.toString());
            params.append('action', action);

            const response = await fetch('${contextPath}/admin/admin-quizzes', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'X-Requested-With': 'XMLHttpRequest'
                },
                body: params.toString()
            });

            const result = await response.json();

            if (result.success) {
                showNotification(result.message, true);

                setTimeout(() => {
                    quizElement.classList.add('removed');
                    updateCounters();

                    const pendingContainer = document.getElementById('pending-quizzes');
                    const approvedContainer = document.getElementById('approved-quizzes');

                    if (pendingContainer && pendingContainer.querySelectorAll('.quiz-item:not(.removed)').length === 0) {
                        showEmptyState('pending');
                    }
                    if (approvedContainer && approvedContainer.querySelectorAll('.quiz-item:not(.removed)').length === 0) {
                        showEmptyState('approved');
                    }
                }, 500);

            } else {
                throw new Error(result.message);
            }

        } catch (error) {
            console.error('Error:', error);
            showNotification(error.message, false);

            button.disabled = false;
            button.innerHTML = originalText;
            quizElement.classList.remove('removing');
        }
    }

    function showEmptyState(sectionType) {
        const sections = document.querySelectorAll('.section');
        let targetSection;

        if (sectionType === 'pending') {
            targetSection = sections[0];
        } else {
            targetSection = sections[1];
        }

        const emptyState = document.createElement('div');
        emptyState.className = 'empty-state';
        if (sectionType === 'pending') {
            emptyState.innerHTML = '<h3>🎉 Нет викторин на модерации</h3><p>Все викторины проверены и одобрены</p>';
        } else {
            emptyState.innerHTML = '<h3>📭 Нет одобренных викторин</h3><p>Одобренные викторины появятся здесь после модерации</p>';
        }

        const quizList = targetSection.querySelector('.quiz-list');
        if (quizList) {
            targetSection.insertBefore(emptyState, quizList.nextSibling);
        }
    }

    document.addEventListener('DOMContentLoaded', function() {
        setTimeout(() => {
            const serverNotifications = document.querySelectorAll('.notification');
            serverNotifications.forEach(notification => {
                notification.style.opacity = '0';
                setTimeout(() => {
                    if (notification.parentNode) {
                        notification.parentNode.removeChild(notification);
                    }
                }, 300);
            });
        }, 5000);
    });
</script>
</body>
</html>