// Learning Page Script

let currentLesson = null;

document.addEventListener('DOMContentLoaded', () => {
    checkAuth();
    initLearning();
});

async function initLearning() {
    try {
        const params = new URLSearchParams(window.location.search);
        const lessonId = params.get('lessonId');
        
        if (!lessonId) {
            window.location.href = '/dashboard.html';
            return;
        }

        await loadLesson(lessonId);
        setupEventListeners();
    } catch (error) {
        console.error('Error initializing learning:', error);
    }
}

async function loadLesson(lessonId) {
    try {
        const response = await authenticatedFetch(`/api/lessons/${lessonId}`);
        if (!response.ok) throw new Error('Failed to load lesson');
        
        currentLesson = await response.json();
        displayLesson(currentLesson);
    } catch (error) {
        console.error('Error loading lesson:', error);
        alert('Could not load lesson. Redirecting...');
        window.location.href = '/dashboard.html';
    }
}

function displayLesson(lesson) {
    document.getElementById('lessonTitle').textContent = lesson.title;
    document.getElementById('lessonCategory').textContent = lesson.category;
    document.getElementById('lessonDifficulty').textContent = lesson.difficulty || 'Beginner';
    document.getElementById('theoryText').innerHTML = formatTheoryText(lesson.theoryText);

    renderQuiz(lesson);
    
    // Set up video button
    if (lesson.youtubeId) {
        // enable JS API for iframe control
        const videoUrl = `https://www.youtube-nocookie.com/embed/${lesson.youtubeId}?rel=0&enablejsapi=1`;
        const openUrl = `https://www.youtube.com/watch?v=${lesson.youtubeId}`;
        const vf = document.getElementById('videoFrame');
        const ff = document.getElementById('fullVideoFrame');
        // store the canonical URL on a data attribute so other scripts can restore if needed
        vf.dataset.videoUrl = videoUrl;
        ff.dataset.videoUrl = videoUrl;
        vf.src = videoUrl;
        ff.src = videoUrl;
        const openLink = document.getElementById('openOnYoutube');
        openLink.href = openUrl;
        openLink.style.display = 'inline-block';
        document.getElementById('watchVideoBtn').disabled = false;
        document.dispatchEvent(new CustomEvent('lesson-video-ready', { detail: { hasVideo: true } }));
    } else {
        document.getElementById('openOnYoutube').style.display = 'none';
        document.getElementById('watchVideoBtn').disabled = true;
        document.dispatchEvent(new CustomEvent('lesson-video-ready', { detail: { hasVideo: false } }));
    }
}

function renderQuiz(lesson) {
    const questionEl = document.getElementById('quizQuestion');
    const optionsEl = document.getElementById('quizOptions');
    const submitBtn = document.getElementById('submitBtn');

    if (!lesson.quizQuestion || !Array.isArray(lesson.quizOptions) || lesson.quizOptions.length === 0) {
        questionEl.textContent = 'Quiz not available for this lesson yet.';
        optionsEl.innerHTML = '';
        submitBtn.disabled = true;
        return;
    }

    submitBtn.disabled = false;
    questionEl.textContent = lesson.quizQuestion;
    optionsEl.innerHTML = '';

    lesson.quizOptions.forEach((optionText, index) => {
        const optionValue = extractOptionValue(optionText, index);
        const optionId = `quizOption-${index}`;

        const label = document.createElement('label');
        label.className = 'quiz-option';
        label.setAttribute('for', optionId);

        const input = document.createElement('input');
        input.type = 'radio';
        input.name = 'quizOption';
        input.value = optionValue;
        input.id = optionId;

        const text = document.createElement('span');
        text.textContent = optionText;

        label.appendChild(input);
        label.appendChild(text);
        optionsEl.appendChild(label);
    });
}

function extractOptionValue(optionText, index) {
    if (typeof optionText === 'string') {
        const trimmed = optionText.trim();
        const firstChar = trimmed.charAt(0).toUpperCase();
        if (firstChar >= 'A' && firstChar <= 'Z' && trimmed.charAt(1) === '.') {
            return firstChar;
        }
    }
    return String.fromCharCode(65 + index);
}

function formatTheoryText(text) {
    // Simple markdown-like formatting
    return text
        .replace(/^### (.*?)$/gm, '<h3>$1</h3>')
        .replace(/^## (.*?)$/gm, '<h2>$1</h2>')
        .replace(/^# (.*?)$/gm, '<h1>$1</h1>')
        .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
        .replace(/\*(.*?)\*/g, '<em>$1</em>')
        .replace(/`(.*?)`/g, '<code>$1</code>')
        .replace(/\n/g, '<br>');
}

function setupEventListeners() {
    // Watch video
    document.getElementById('watchVideoBtn').addEventListener('click', () => {
        document.getElementById('videoPlayer').style.display =
            document.getElementById('videoPlayer').style.display === 'none' ? 'block' : 'none';
    });

    // Submit answer
    document.getElementById('submitBtn').addEventListener('click', checkAnswer);

    // Clear selection
    document.getElementById('resetBtn').addEventListener('click', () => {
        document.querySelectorAll('input[name="quizOption"]').forEach(input => {
            input.checked = false;
        });
        const feedbackDiv = document.getElementById('feedbackMessage');
        feedbackDiv.style.display = 'none';
    });

    // Save lesson
    document.getElementById('saveBtn').addEventListener('click', saveLesson);

    // Modal close
    document.querySelector('.close').addEventListener('click', () => {
        document.getElementById('videoModal').style.display = 'none';
    });
}

async function checkAnswer() {
    const selected = document.querySelector('input[name="quizOption"]:checked');

    if (!selected) {
        showFeedback('Please select an answer first!', false);
        return;
    }

    try {
        const response = await authenticatedFetch(`/api/lessons/${currentLesson.id}/check-answer`, {
            method: 'POST',
            body: JSON.stringify({ option: selected.value })
        });

        const result = await response.json();

        if (result.correct) {
            showFeedback('🎉 Correct! You earned ' + result.xpReward + ' XP', true);
            await completeLesson(result.xpReward);
            
            // Show success animation
            setTimeout(() => {
                alert('Lesson completed! Returning to dashboard...');
                window.location.href = '/dashboard.html';
            }, 2000);
        } else {
            const correctText = getOptionTextByValue(currentLesson);
            const suffix = correctText ? `Correct answer: ${correctText}` : 'Try again!';
            showFeedback(suffix, false);
        }
    } catch (error) {
        console.error('Error checking answer:', error);
        showFeedback('Error checking your answer', false);
    }
}

function getOptionTextByValue(lesson) {
    if (!lesson || !lesson.correctOption || !Array.isArray(lesson.quizOptions)) {
        return '';
    }
    const target = lesson.correctOption.trim().toUpperCase();
    const match = lesson.quizOptions.find(optionText => {
        const value = extractOptionValue(optionText, lesson.quizOptions.indexOf(optionText));
        return value === target;
    });
    return match || '';
}

async function completeLesson(xpReward) {
    try {
        const user = getCurrentUser();
        await authenticatedFetch(`/api/students/${user.id}/complete-lesson/${currentLesson.id}`, {
            method: 'POST',
            body: JSON.stringify({ xpReward })
        });
    } catch (error) {
        console.error('Error completing lesson:', error);
    }
}

async function saveLesson() {
    try {
        const user = getCurrentUser();
        const button = document.getElementById('saveBtn');
        const isSaved = button.classList.contains('saved');

        if (isSaved) {
            await authenticatedFetch(`/api/students/${user.id}/saved-lesson/${currentLesson.id}`, {
                method: 'DELETE'
            });
            button.classList.remove('saved');
            button.textContent = '☆ Save Lesson';
        } else {
            await authenticatedFetch(`/api/students/${user.id}/save-lesson/${currentLesson.id}`, {
                method: 'POST'
            });
            button.classList.add('saved');
            button.textContent = '⭐ Saved';
        }
    } catch (error) {
        console.error('Error saving lesson:', error);
    }
}

function showFeedback(message, isSuccess) {
    const feedbackDiv = document.getElementById('feedbackMessage');
    feedbackDiv.textContent = message;
    feedbackDiv.className = `feedback-message ${isSuccess ? 'success' : 'error'}`;
    feedbackDiv.style.display = 'block';

    // Auto-hide after 5 seconds
    setTimeout(() => {
        feedbackDiv.style.display = 'none';
    }, 5000);
}
