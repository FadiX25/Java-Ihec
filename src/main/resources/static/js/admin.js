// Admin Dashboard Script

document.addEventListener('DOMContentLoaded', () => {
    checkAuth();
    const user = getCurrentUser();
    
    if (!user || user.role !== 'ADMIN') {
        // Not an admin - redirect to dashboard
        window.location.href = '/dashboard.html';
        return;
    }

    initAdmin();
});

async function initAdmin() {
    try {
        // Set up menu
        setupMenuListeners();
        
        // Load initial data
        await loadLessonsAdmin();
        await loadStudents();
        await loadReports();
    } catch (error) {
        console.error('Error initializing admin:', error);
    }
}

function setupMenuListeners() {
    document.querySelectorAll('.menu-link').forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            const section = link.dataset.section;
            
            // Update active menu
            document.querySelectorAll('.menu-link').forEach(l => l.classList.remove('active'));
            link.classList.add('active');
            
            // Show section
            document.querySelectorAll('.admin-section').forEach(s => s.classList.remove('active'));
            const target = document.getElementById(section + 'Section');
            if (target) target.classList.add('active');
        });
    });

    // Add lesson button
    const addBtn = document.getElementById('addLessonBtn');
    if (addBtn) addBtn.addEventListener('click', openLessonModal);

    const updateQuizzesBtn = document.getElementById('updateQuizzesBtn');
    if (updateQuizzesBtn) {
        updateQuizzesBtn.addEventListener('click', async () => {
            await updateQuizzesCall();
        });
    }
}

async function loadLessonsAdmin() {
    try {
        const response = await authenticatedFetch('/api/lessons');
        if (!response.ok) throw new Error('Failed to fetch lessons: ' + response.status);
        const lessons = await response.json();
        if (!lessons || lessons.length === 0) {
            // Show friendly prompt to seed the database
            const tbody = document.getElementById('lessonsList');
            if (tbody) {
                tbody.innerHTML = `
                    <tr>
                        <td colspan="5" style="padding:20px;text-align:center;">
                            <div style="margin-bottom:8px;">No lessons found in the database.</div>
                            <div>
                                <button id="inlineSeedBtn" class="btn btn-warning">Seed sample data</button>
                            </div>
                        </td>
                    </tr>
                `;
                const inlineSeed = document.getElementById('inlineSeedBtn');
                if (inlineSeed) {
                    inlineSeed.addEventListener('click', async () => {
                        await seedDatabaseCall();
                    });
                }
            }
            return;
        }

        displayLessonsAdmin(lessons);
    } catch (error) {
        console.error('Error loading lessons:', error);
        // show user-friendly message
        const tbody = document.getElementById('lessonsList');
        if (tbody) tbody.innerHTML = '<tr><td colspan="5">Could not load lessons. Check server logs or authentication.</td></tr>';
    }
}

// Centralized seed logic used by seed button and inline prompt
async function seedDatabaseCall() {
    try {
        let resp = null;
        try {
            resp = await authenticatedFetch('/api/admin/init/database', { method: 'POST' });
        } catch (e) {
            console.warn('Authenticated init failed, will try public dev endpoint', e);
        }

        // If authenticated call failed or returned 401/403, fallback to public endpoint
        if (!resp || !resp.ok) {
            try {
                resp = await fetch('/api/public/init/database', { method: 'POST' });
            } catch (e) {
                console.error('Public init call failed', e);
                alert('Seeding failed: ' + e.message);
                return false;
            }
        }

        if (!resp.ok) {
            const text = await resp.text();
            alert('Seeding failed: ' + text);
            return false;
        }

        const text = await resp.text();
        alert(text);
        // Refresh data
        await loadLessonsAdmin();
        await loadReports();
        await loadStudents();
        return true;
    } catch (e) {
        alert('Seeding failed: ' + e.message);
        return false;
    }
}

async function updateQuizzesCall() {
    try {
        const resp = await authenticatedFetch('/api/admin/init/lesson-quizzes', { method: 'POST' });
        if (!resp.ok) {
            const text = await resp.text();
            const message = text && text.trim().length > 0 ? text : `${resp.status} ${resp.statusText}`;
            alert('Quiz update failed: ' + message);
            return false;
        }

        const text = await resp.text();
        alert(text);
        await loadLessonsAdmin();
        return true;
    } catch (e) {
        alert('Quiz update failed: ' + e.message);
        return false;
    }
}

function displayLessonsAdmin(lessons) {
    const tbody = document.getElementById('lessonsList');
    if (!tbody) return;
    tbody.innerHTML = '';

    lessons.forEach(lesson => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${lesson.title || ''}</td>
            <td>${lesson.category || ''}</td>
            <td>${lesson.difficulty || 'Beginner'}</td>
            <td>${lesson.xpReward || 10}</td>
            <td>
                <div class="action-icons">
                    <button class="edit-btn" onclick="editLesson('${lesson.id}')">Edit</button>
                    <button class="delete-btn" onclick="deleteLesson('${lesson.id}')">Delete</button>
                </div>
            </td>
        `;
        tbody.appendChild(row);
    });
}

async function loadStudents() {
    try {
        const resp = await authenticatedFetch('/api/admin/students');
        if (resp && resp.ok) {
            const students = await resp.json();
            displayStudents(students);
            return;
        }

        const text = resp ? await resp.text() : 'No response from server.';
        const tbody = document.getElementById('studentsList');
        if (tbody) tbody.innerHTML = `<tr><td colspan="5">Could not load students. ${text}</td></tr>`;
    } catch (error) {
        console.error('Error loading students:', error);
    }
}

function displayStudents(students) {
    const tbody = document.getElementById('studentsList');
    if (!tbody) return;
    tbody.innerHTML = '';

    students.forEach(s => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${s.firstName || ''} ${s.lastName || ''}</td>
            <td>${s.email || ''}</td>
            <td>${s.xpScore || 0}</td>
            <td>${(s.completedLessonIds || []).length}</td>
            <td>
                <div class="action-icons">
                    <button class="edit-btn" onclick="viewStudent('${s.id}')">View</button>
                    <button class="delete-btn" onclick="deleteStudent('${s.id}', '${s.firstName || ''} ${s.lastName || ''}')">Delete</button>
                </div>
            </td>
        `;
        tbody.appendChild(row);
    });
}

async function loadReports() {
    try {
        // Load lessons count
        const lessonsResp = await authenticatedFetch('/api/lessons');
        if (lessonsResp.ok) {
            const lessons = await lessonsResp.json();
            document.getElementById('totalLessons').textContent = lessons.length;
        }

        // For total students and average XP we'd need a students endpoint; placeholder values handled
    } catch (error) {
        console.error('Error loading reports:', error);
    }
}

function viewStudent(studentId) {
    alert('Student ID: ' + studentId);
}

async function deleteStudent(studentId, displayName) {
    const label = displayName ? ` (${displayName})` : '';
    if (!confirm(`Delete student${label}? This cannot be undone.`)) {
        return;
    }

    try {
        const resp = await authenticatedFetch(`/api/admin/students/${studentId}`, { method: 'DELETE' });
        if (!resp.ok) {
            const text = await resp.text();
            alert('Delete failed: ' + text);
            return;
        }
        alert('Student deleted.');
        await loadStudents();
        await loadReports();
    } catch (e) {
        alert('Delete failed: ' + e.message);
    }
}

function openLessonModal() {
    const modal = document.getElementById('lessonModal');
    if (!modal) return;
    modal.style.display = 'block';
    const form = document.getElementById('lessonForm');
    if (form) {
        form.reset();
        form.onsubmit = createLesson;
    }
}

function editLesson(lessonId) {
    // Load lesson data and show modal
    alert('Edit functionality would fetch lesson ' + lessonId);
}

async function deleteLesson(lessonId) {
    if (!confirm('Are you sure you want to delete this lesson?')) {
        return;
    }

    try {
        const response = await authenticatedFetch(`/api/lessons/${lessonId}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            alert('Lesson deleted successfully');
            await loadLessonsAdmin();
        } else {
            alert('Failed to delete lesson');
        }
    } catch (error) {
        console.error('Error deleting lesson:', error);
        alert('Error deleting lesson');
    }
}

async function createLesson(e) {
    e.preventDefault();

    const quizOptionsRaw = document.getElementById('lessonQuizOptions').value || '';
    const quizOptions = quizOptionsRaw
        .split('\n')
        .map(line => line.trim())
        .filter(line => line.length > 0);

    const quizQuestion = document.getElementById('lessonQuizQuestion').value.trim();
    const correctOption = document.getElementById('lessonCorrectOption').value.trim().toUpperCase();

    const quizValidationError = validateQuizFields(quizQuestion, quizOptions, correctOption);
    if (quizValidationError) {
        alert(quizValidationError);
        return;
    }

    const lesson = {
        title: document.getElementById('lessonTitle').value,
        category: document.getElementById('lessonCategory').value,
        youtubeId: document.getElementById('lessonYoutubeId').value,
        theoryText: document.getElementById('lessonTheory').value,
        correctAnswer: document.getElementById('lessonCorrectAnswer').value,
        quizQuestion: quizQuestion,
        quizOptions: quizOptions,
        correctOption: correctOption,
        difficulty: document.getElementById('lessonDifficulty').value,
        xpReward: parseInt(document.getElementById('lessonXP').value),
        dateCreated: new Date().toISOString().split('T')[0],
        id: Date.now().toString()
    };

    try {
        const response = await authenticatedFetch('/api/lessons', {
            method: 'POST',
            body: JSON.stringify(lesson)
        });

        if (response.ok) {
            alert('Lesson created successfully');
            const modal = document.getElementById('lessonModal');
            if (modal) modal.style.display = 'none';
            await loadLessonsAdmin();
        } else {
            alert('Failed to create lesson');
        }
    } catch (error) {
        console.error('Error creating lesson:', error);
        alert('Error creating lesson');
    }
}

function validateQuizFields(question, options, correctOption) {
    if (!question) {
        return 'Quiz question is required.';
    }

    if (!Array.isArray(options) || options.length !== 4) {
        return 'Quiz options must include exactly 4 lines.';
    }

    const normalized = options.map((optionText, index) => ({
        text: optionText,
        value: extractOptionValue(optionText, index)
    }));

    const hasEmptyOption = normalized.some(entry => !entry.text || entry.text.trim().length === 0);
    if (hasEmptyOption) {
        return 'Quiz options cannot be empty.';
    }

    if (!correctOption) {
        return 'Correct option is required (A, B, C, or D).';
    }

    const validValues = normalized.map(entry => entry.value);
    if (!validValues.includes(correctOption)) {
        return 'Correct option must match one of the option letters (A, B, C, or D).';
    }

    return '';
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

// After initAdmin is defined, attach admin action buttons
document.addEventListener('DOMContentLoaded', () => {
    // safe attach for admin actions
    const checkBtn = document.getElementById('checkFirebaseBtn');
    const seedBtn = document.getElementById('seedDbBtn');

    if (checkBtn) {
        checkBtn.addEventListener('click', async () => {
            try {
                const resp = await authenticatedFetch('/api/admin/init/status', { method: 'POST' });
                const text = await resp.text();
                alert(text);
            } catch (e) {
                alert('Could not contact server: ' + e.message);
            }
        });
    }

    if (seedBtn) {
        seedBtn.addEventListener('click', async () => {
            if (!confirm('Seed the database with sample data? This will create sample users and lessons.')) return;
            await seedDatabaseCall();
        });
    }
});

// Modal close handlers (safe attach)
const lessonModalClose = document.querySelector('#lessonModal .close');
if (lessonModalClose) lessonModalClose.addEventListener('click', () => {
    const modal = document.getElementById('lessonModal');
    if (modal) modal.style.display = 'none';
});

window.onclick = (event) => {
    const modal = document.getElementById('lessonModal');
    if (event.target === modal) {
        modal.style.display = 'none';
    }
};
