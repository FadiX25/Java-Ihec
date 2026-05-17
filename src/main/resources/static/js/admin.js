// Admin Dashboard Script

document.addEventListener('DOMContentLoaded', () => {
    checkAuth();
    const user = getCurrentUser();
    
    if (user.role !== 'ADMIN') {
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
            document.getElementById(section + 'Section').classList.add('active');
        });
    });

    // Add lesson button
    document.getElementById('addLessonBtn').addEventListener('click', openLessonModal);
}

async function loadLessonsAdmin() {
    try {
        const response = await fetch('/api/lessons');
        const lessons = await response.json();
        displayLessonsAdmin(lessons);
    } catch (error) {
        console.error('Error loading lessons:', error);
    }
}

function displayLessonsAdmin(lessons) {
    const tbody = document.getElementById('lessonsList');
    tbody.innerHTML = '';

    lessons.forEach(lesson => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${lesson.title}</td>
            <td>${lesson.category}</td>
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
    // Note: This would require an API endpoint to get all students
    // For now, showing placeholder
    try {
        // Example: const response = await fetch('/api/students');
        // const students = await response.json();
        // displayStudents(students);
    } catch (error) {
        console.error('Error loading students:', error);
    }
}

async function loadReports() {
    try {
        // Load lessons count
        const lessonsResponse = await fetch('/api/lessons');
        const lessons = await lessonsResponse.json();
        document.getElementById('totalLessons').textContent = lessons.length;

        // Calculate average XP (would need student data)
        // For now, using placeholder
        document.getElementById('totalStudents').textContent = '0';
        document.getElementById('avgXP').textContent = '0';
    } catch (error) {
        console.error('Error loading reports:', error);
    }
}

function openLessonModal() {
    document.getElementById('lessonModal').style.display = 'block';
    document.getElementById('lessonForm').reset();
    document.getElementById('lessonForm').onsubmit = createLesson;
}

function editLesson(lessonId) {
    // Load lesson data and show modal
    // This would fetch the lesson and populate the form
    alert('Edit functionality would fetch lesson ' + lessonId);
}

async function deleteLesson(lessonId) {
    if (!confirm('Are you sure you want to delete this lesson?')) {
        return;
    }

    try {
        const response = await fetch(`/api/lessons/${lessonId}`, {
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
        const response = await fetch('/api/lessons', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(lesson)
        });

        if (response.ok) {
            alert('Lesson created successfully');
            document.getElementById('lessonModal').style.display = 'none';
            await loadLessonsAdmin();
        } else {
            alert('Failed to create lesson');
        }
    } catch (error) {
        console.error('Error creating lesson:', error);
        alert('Error creating lesson');
    }
}

// Modal close handlers
document.querySelector('.close').addEventListener('click', () => {
    document.getElementById('lessonModal').style.display = 'none';
});

window.onclick = (event) => {
    const modal = document.getElementById('lessonModal');
    if (event.target === modal) {
        modal.style.display = 'none';
    }
};
