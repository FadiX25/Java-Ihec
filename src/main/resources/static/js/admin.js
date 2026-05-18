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
        // If an API exists for all students, call it; otherwise display placeholders
        // Attempt to call /api/students (may require admin endpoint); fallback to empty
        try {
            const resp = await authenticatedFetch('/api/students');
            if (resp && resp.ok) {
                const students = await resp.json();
                displayStudents(students);
                return;
            }
        } catch (e) {
            // ignore and fall through to placeholder
        }

        // Placeholder: show message
        const tbody = document.getElementById('studentsList');
        if (tbody) tbody.innerHTML = '<tr><td colspan="5">Student listing not available via API.</td></tr>';
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
            <td><button onclick="alert('Implement student actions')">Actions</button></td>
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

    const lesson = {
        title: document.getElementById('lessonTitle').value,
        category: document.getElementById('lessonCategory').value,
        youtubeId: document.getElementById('lessonYoutubeId').value,
        theoryText: document.getElementById('lessonTheory').value,
        correctAnswer: document.getElementById('lessonCorrectAnswer').value,
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
