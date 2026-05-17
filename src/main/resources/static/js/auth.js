// Authentication Helper Functions

/**
 * Check if user is authenticated
 */
function isAuthenticated() {
    return localStorage.getItem('token') !== null;
}

/**
 * Get current user from localStorage
 */
function getCurrentUser() {
    const userStr = localStorage.getItem('user');
    return userStr ? JSON.parse(userStr) : null;
}

/**
 * Get authentication token
 */
function getToken() {
    return localStorage.getItem('token');
}

/**
 * Set authentication data
 */
function setAuth(token, user) {
    localStorage.setItem('token', token);
    localStorage.setItem('user', JSON.stringify(user));
}

/**
 * Clear authentication data
 */
function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    window.location.href = '/login.html';
}

/**
 * Check authentication and redirect if needed
 */
function checkAuth() {
    if (!isAuthenticated()) {
        window.location.href = '/login.html';
    }
}

/**
 * Fetch with authentication header
 */
async function authenticatedFetch(url, options = {}) {
    const headers = options.headers || {};
    headers['Content-Type'] = 'application/json';
    headers['Authorization'] = 'Bearer ' + getToken();

    const response = await fetch(url, {
        ...options,
        headers
    });

    if (response.status === 401) {
        logout();
    }

    return response;
}

// Set up logout buttons
document.addEventListener('DOMContentLoaded', () => {
    const logoutBtns = document.querySelectorAll('#logoutBtn');
    logoutBtns.forEach(btn => {
        btn.addEventListener('click', (e) => {
            e.preventDefault();
            logout();
        });
    });
});
