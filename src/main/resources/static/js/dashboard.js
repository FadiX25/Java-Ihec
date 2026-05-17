// Dashboard Page Script

document.addEventListener('DOMContentLoaded', () => {
    checkAuth();
    initDashboard();
});

async function initDashboard() {
    try {
        // Load user data
        const user = getCurrentUser();
        if (user) {
            loadUserProfile(user);
        }

        // Load lessons
        await loadLessons();
    } catch (error) {
        console.error('Error initializing dashboard:', error);
    }
}

function loadUserProfile(user) {
    document.getElementById('userName').textContent = `${user.firstName} ${user.lastName}`;
    document.getElementById('userEmail').textContent = user.email;
    document.getElementById('xpScore').textContent = user.xpScore || 0;
    
    // Calculate XP progress (0-100)
    const maxXP = 500;
    const progress = Math.min((user.xpScore || 0) / maxXP * 100, 100);
    document.getElementById('xpProgress').style.width = progress + '%';
    
    // Set avatar initials
    const initials = (user.firstName?.[0] || 'U') + (user.lastName?.[0] || '');
    document.getElementById('userAvatar').textContent = initials.toUpperCase();
}

async function loadLessons() {
    try {
        const response = await fetch('/api/lessons');
        if (!response.ok) throw new Error('Failed to fetch lessons');
        
        const lessons = await response.json();
        displayLessons(lessons);
        loadCategories(lessons);
    } catch (error) {
        console.error('Error loading lessons:', error);
    }
}

function displayLessons(lessons) {
    const grid = document.getElementById('lessonsGrid');
    grid.innerHTML = '';
    
    lessons.forEach(lesson => {
        const card = createLessonCard(lesson);
        grid.appendChild(card);
    });
}

function createLessonCard(lesson) {
    const card = document.createElement('div');
    card.className = 'lesson-card';
    
    const difficultyClass = `difficulty-${lesson.difficulty?.toLowerCase() || 'beginner'}`;
    const isSaved = getCurrentUser().savedLessonIds?.includes(lesson.id);
    
    card.innerHTML = `
        <div class="lesson-card-header">
            <span class="lesson-category">${lesson.category}</span>
            <h3>${lesson.title}</h3>
        </div>
        <div class="lesson-card-body">
            <span class="lesson-difficulty ${difficultyClass}">${lesson.difficulty || 'Beginner'}</span>
            <div class="lesson-xp">💡 ${lesson.xpReward || 10} XP Reward</div>
            <p class="lesson-description">${lesson.theoryText?.substring(0, 80) || 'Learn new concepts...'}...</p>
        </div>
        <div class="lesson-card-footer">
            <button class="lesson-open" onclick="goToLesson('${lesson.id}')">Learn</button>
            <button class="lesson-save ${isSaved ? 'saved' : ''}" onclick="toggleSaveLesson('${lesson.id}', this)">
                ${isSaved ? '⭐' : '☆'}
            </button>
        </div>
    `;
    
    return card;
}

async function toggleSaveLesson(lessonId, button) {
    try {
        const user = getCurrentUser();
        const isSaved = button.classList.contains('saved');
        
        if (isSaved) {
            await authenticatedFetch(`/api/students/${user.id}/saved-lesson/${lessonId}`, {
                method: 'DELETE'
            });
            button.classList.remove('saved');
            button.textContent = '☆';
        } else {
            await authenticatedFetch(`/api/students/${user.id}/save-lesson/${lessonId}`, {
                method: 'POST'
            });
            button.classList.add('saved');
            button.textContent = '⭐';
        }
    } catch (error) {
        console.error('Error saving lesson:', error);
    }
}

function goToLesson(lessonId) {
    window.location.href = `/learning.html?lessonId=${lessonId}`;
}

function loadCategories(lessons) {
    const categories = [...new Set(lessons.map(l => l.category))];
    const categoryList = document.getElementById('categoryList');
    
    categories.forEach(category => {
        const li = document.createElement('li');
        const a = document.createElement('a');
        a.href = '#';
        a.textContent = category;
        a.onclick = (e) => {
            e.preventDefault();
            filterByCategory(lessons, category, a);
        };
        li.appendChild(a);
        categoryList.appendChild(li);
    });
}

function filterByCategory(lessons, category, element) {
    // Update active state
    document.querySelectorAll('.menu a').forEach(a => a.classList.remove('active'));
    element.classList.add('active');
    
    // Filter and display
    const filtered = category === 'All' ? lessons : lessons.filter(l => l.category === category);
    displayLessons(filtered);
}

// Search functionality
document.getElementById('searchInput').addEventListener('input', async (e) => {
    const query = e.target.value.toLowerCase();
    const response = await fetch('/api/lessons');
    const lessons = await response.json();
    
    if (query === '') {
        displayLessons(lessons);
    } else {
        const filtered = lessons.filter(l =>
            l.title.toLowerCase().includes(query) ||
            l.category.toLowerCase().includes(query) ||
            l.theoryText.toLowerCase().includes(query)
        );
        displayLessons(filtered);
    }
});
