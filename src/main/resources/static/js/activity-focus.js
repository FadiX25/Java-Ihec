// activity-focus.js
(() => {
    // Config
    const INACTIVITY_MS = 5 * 1000; // 5 seconds for testing
    const MOVE_MIN_DIST = 10; // px cumulative in window to consider significant
    const MOVE_WINDOW_MS = 2000; // window for cumulative movement
    const MOVE_MIN_INTERVAL_MS = 120; // ignore very frequent micro events

    // Elements
    const videoPlayerWrapper = document.getElementById('videoPlayer');
    const videoFrame = document.getElementById('videoFrame');
    const idleModal = document.getElementById('idleModal');
    const idleOkBtn = document.getElementById('idleOkBtn');
    const idleExitBtn = document.getElementById('idleExitBtn');
    const focusToggle = document.getElementById('focusToggle');
    const focusAudio = document.getElementById('focusAudio');

    if (!videoPlayerWrapper || !videoFrame) return; // not on lesson page

    // If iframe src is missing (previous runs might have cleared it), restore from data attribute
    try {
        if ((!videoFrame.src || videoFrame.src.trim() === '') && videoFrame.dataset && videoFrame.dataset.videoUrl) {
            videoFrame.src = videoFrame.dataset.videoUrl;
        }
    } catch (e) { /* ignore */ }

    // Helper to check if video player is currently visible
    function isVideoVisible() {
        try {
            return window.getComputedStyle(videoPlayerWrapper).display !== 'none';
        } catch (e) {
            return false;
        }
    }

    // Helper to send command to YouTube iframe via postMessage
    function postYouTubeCommand(command) {
        if (!videoFrame || !videoFrame.contentWindow) return;
        const message = JSON.stringify({ event: 'command', func: command, args: [] });
        try {
            videoFrame.contentWindow.postMessage(message, '*');
        } catch (e) {
            // ignore
        }
    }

    // Focus Mode
    let focusMode = false;
    let videoWasHiddenBeforeFocus = false;

    function hasVideoSource() {
        return !!(videoFrame.dataset.videoUrl || videoFrame.getAttribute('src'));
    }

    function showFocusVideo() {
        // Focus mode should not show the video.
        if (isVideoVisible()) {
            videoWasHiddenBeforeFocus = true;
            videoPlayerWrapper.style.display = 'none';
        }
    }

    function restoreVideoAfterFocus() {
        if (videoWasHiddenBeforeFocus) {
            videoPlayerWrapper.style.display = 'block';
            videoWasHiddenBeforeFocus = false;
        }
    }

    function updateFocusToggle() {
        if (!focusToggle) return;
        focusToggle.textContent = focusMode ? 'Exit focus' : 'Focus';
        focusToggle.title = focusMode ? 'Exit Focus Mode' : 'Focus Mode';
        focusToggle.setAttribute('aria-pressed', focusMode ? 'true' : 'false');
    }

    function setFocusMode(enabled) {
        focusMode = !!enabled;

        if (focusMode) {
            showFocusVideo();
            playFocusAudio();
            if (isVideoVisible()) {
                postYouTubeCommand('pauseVideo');
            }
        } else {
            restoreVideoAfterFocus();
            pauseFocusAudio();
        }

        document.body.classList.toggle('focus-mode', focusMode);
        updateFocusToggle();
        try { localStorage.setItem('focusMode', focusMode ? '1' : '0'); } catch (e) {}
    }

    function playFocusAudio() {
        if (!focusAudio) return;
        try {
            focusAudio.volume = 0.5;
            const playResult = focusAudio.play();
            if (playResult && typeof playResult.catch === 'function') {
                playResult.catch(() => {});
            }
        } catch (e) {
            // ignore
        }
    }

    function pauseFocusAudio() {
        if (!focusAudio) return;
        try {
            focusAudio.pause();
            focusAudio.currentTime = 0;
        } catch (e) {
            // ignore
        }
    }

    focusToggle && focusToggle.addEventListener('click', () => setFocusMode(!focusMode));
    updateFocusToggle();

    try {
        const saved = localStorage.getItem('focusMode');
        if (saved === '1') setFocusMode(true);
    } catch (e) {}

    document.addEventListener('lesson-video-ready', () => {
        if (focusMode && hasVideoSource()) {
            showFocusVideo();
        }
    });

    // Idle detection
    let lastActivity = Date.now();
    let inactivityTimer = null;

    // Movement buffer for small anti-jiggle
    let moveBuffer = [];
    let lastMoveTs = 0;

    function recordActivity(reason) {
        lastActivity = Date.now();
        scheduleInactivity();
    }

    function scheduleInactivity() {
        if (inactivityTimer) clearTimeout(inactivityTimer);
        inactivityTimer = setTimeout(onInactivity, INACTIVITY_MS);
    }

    function onInactivity() {
        // Pause video via postMessage to YouTube iframe
        if (isVideoVisible()) {
            postYouTubeCommand('pauseVideo');
        }
        if (focusMode) {
            pauseFocusAudio();
        }
        // Show modal
        if (idleModal) {
            idleModal.style.display = 'flex';
            idleModal.setAttribute('aria-hidden', 'false');
        }
    }

    function resumeFromIdle() {
        // Resume video via postMessage
        if (isVideoVisible()) {
            postYouTubeCommand('playVideo');
        }
        if (focusMode) {
            playFocusAudio();
        }
        if (idleModal) {
            idleModal.style.display = 'none';
            idleModal.setAttribute('aria-hidden', 'true');
        }
        recordActivity('resume');
    }

    idleOkBtn && idleOkBtn.addEventListener('click', () => resumeFromIdle());
    idleExitBtn && idleExitBtn.addEventListener('click', () => {
        window.location.href = '/dashboard.html';
    });

    // Activity listeners
    function handlePointerMove(e) {
        const now = Date.now();
        if (now - lastMoveTs < MOVE_MIN_INTERVAL_MS) return; // skip tiny high freq
        lastMoveTs = now;
        moveBuffer.push({x: e.clientX, y: e.clientY, t: now});
        // Remove old
        while (moveBuffer.length && now - moveBuffer[0].t > MOVE_WINDOW_MS) moveBuffer.shift();
        // compute cumulative distance
        let dist = 0;
        for (let i = 1; i < moveBuffer.length; i++) {
            const a = moveBuffer[i - 1];
            const b = moveBuffer[i];
            const dx = b.x - a.x;
            const dy = b.y - a.y;
            dist += Math.hypot(dx, dy);
        }
        if (dist >= MOVE_MIN_DIST) {
            // significant move
            if (idleModal && idleModal.style.display === 'flex') {
                // Do not auto-resume when modal is up; require OK click
            } else {
                recordActivity('move');
            }
            moveBuffer = [];
        }
    }

    function handleGenericActivity() {
        if (idleModal && idleModal.style.display === 'flex') {
            // if modal visible, do not auto-resume by clicks
            return;
        }
        recordActivity('input');
    }

    window.addEventListener('pointermove', handlePointerMove, {passive: true});
    window.addEventListener('click', handleGenericActivity, {passive: true});
    window.addEventListener('keydown', handleGenericActivity, {passive: true});
    window.addEventListener('wheel', handleGenericActivity, {passive: true});

    // If user hides page, consider it inactive but do not immediately show modal until they return.
    document.addEventListener('visibilitychange', () => {
        if (document.visibilityState === 'hidden') {
            if (focusMode) {
                pauseFocusAudio();
            }
        } else {
            recordActivity('visibility');
            if (focusMode) {
                playFocusAudio();
            }
        }
    });

    // Initialize
    scheduleInactivity();

})();
