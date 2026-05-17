# Quick Start Guide - IHEC-JLearn Spring Boot

## ⚡ 5-Minute Setup

### Step 1: Firebase Setup (2 minutes)
1. Go to https://console.firebase.google.com
2. Create a new project called "IHEC-JLearn"
3. Enable **Realtime Database** (start in test mode)
4. Navigate to **Project Settings** → **Service Accounts**
5. Click "Generate New Private Key"
6. Replace the content of `firebase-config.json` with your downloaded JSON

### Step 2: Build & Run (3 minutes)

```bash
cd c:\Users\pc\Desktop\Java-Ihec
mvn clean install
mvn spring-boot:run
```

Open browser: **http://localhost:8080**

---

## 🔓 Default Test Accounts

### Student Account
- **Username:** student1
- **Password:** (will auto-create on registration)

### Admin Account  
- **Username:** admin1
- **Password:** (will auto-create on registration)

**To create test accounts:**
1. Click "Sign up here" on login page
2. Fill in details with username/password
3. Account is auto-created in Firebase

---

## 📱 Application URLs

| Page | URL |
|------|-----|
| Home | http://localhost:8080 |
| Login | http://localhost:8080/login.html |
| Register | http://localhost:8080/register.html |
| Dashboard | http://localhost:8080/dashboard.html |
| Learning | http://localhost:8080/learning.html |
| Admin Panel | http://localhost:8080/admin-dashboard.html |

---

## 🔧 API Base URL

All API endpoints use the base URL: **http://localhost:8080/api**

Example: `POST http://localhost:8080/api/auth/login`

---

## 📊 Firebase Database Structure

The application automatically creates the following structure in Firebase:

```
root
├── users
│   └── {userId}
│       ├── username
│       ├── email
│       ├── xpScore
│       └── completedLessonIds
└── lessons
    └── {lessonId}
        ├── title
        ├── category
        ├── youtubeId
        └── correctAnswer
```

---

## 🐛 Common Issues & Solutions

### Issue: "Firebase config not found"
**Solution:** Make sure `firebase-config.json` is in the project root directory

### Issue: "Connection refused" at localhost:8080
**Solution:** Maven might still be building. Wait 30 seconds and refresh browser

### Issue: "CORS Error" in browser console
**Solution:** Make sure you're accessing via `http://localhost:8080`, not `http://127.0.0.1:8080`

### Issue: "Lesson data not loading"
**Solution:** 
1. Create lessons via Admin Dashboard
2. Or manually add to Firebase Realtime Database under `/lessons`

---

## 📝 Adding Sample Lessons

1. Register as Admin (or use admin account if set up)
2. Go to Admin Dashboard (`/admin-dashboard.html`)
3. Click "Add Lesson"
4. Fill in:
   - **Title:** e.g., "Java Variables"
   - **Category:** e.g., "Java"
   - **YouTube ID:** e.g., "Hl-zzrqQoSE"
   - **Theory Text:** Lesson content
   - **Correct Answer:** Keyword to match (e.g., "String")
   - **Difficulty:** BEGINNER/INTERMEDIATE/ADVANCED
   - **XP Reward:** e.g., 10

---

## 🚀 Deployment Notes

For production deployment:

1. **Update `firebase.config.path`** in `application.properties` to absolute path
2. **Change security settings** in `SecurityConfig.java`
3. **Enable Firebase Security Rules** in console
4. **Set environment variables:**
   ```
   FIREBASE_DATABASE_URL=your-database-url
   FIREBASE_CONFIG_PATH=/path/to/firebase-config.json
   ```

---

## 📚 Project Structure at a Glance

```
Java-Ihec/
├── pom.xml                              (Maven dependencies)
├── firebase-config.json                 (Firebase credentials)
├── README.md                            (Full documentation)
├── src/main/
│   ├── java/com/ihec/
│   │   ├── IhecApplication.java        (Entry point)
│   │   ├── config/                     (Spring configs)
│   │   ├── controller/                 (REST endpoints)
│   │   ├── model/                      (Data models)
│   │   ├── service/                    (Firebase logic)
│   │   └── dto/                        (Request/Response)
│   └── resources/
│       ├── application.properties      (Config)
│       └── static/                     (HTML/CSS/JS)
└── target/                             (Build output)
```

---

## ✅ Checklist Before Going Live

- [ ] Firebase project created and database enabled
- [ ] Service account JSON downloaded
- [ ] `firebase-config.json` populated
- [ ] Application builds without errors
- [ ] Can login/register successfully
- [ ] Sample lesson created
- [ ] Can complete lesson exercise
- [ ] Admin panel accessible
- [ ] Student profile shows correct XP

---

## 🎓 Key Features to Explore

1. **Split-Screen Learning:** Theory on left, code editor on right
2. **Auto-Grading:** Lessons check if code contains required keywords
3. **XP System:** Students earn points for completing lessons
4. **Save Lessons:** Students can bookmark lessons for later
5. **Admin Tools:** Create and manage lessons easily
6. **Responsive Design:** Works on desktop and mobile

---

## 💡 Tips

- Students can watch YouTube videos directly from lessons
- Admin can manage all content without coding
- Use Firebase Realtime Database console to debug data
- Passwords are hashed with BCrypt for security
- Application logs are available in console output

---

Need help? Check **README.md** for detailed documentation!

**Happy Learning! 🚀**
