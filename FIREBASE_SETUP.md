# 🔥 Firebase Database Setup Guide

## ✅ What's Been Created

Your project now includes **automatic Firebase database initialization** with sample data. Here's what was created:

---

## 📊 Database Structure

### 1. **Users Table** (`/users`)
Database structure for storing all users (Admin and Students):

```
users/
├── admin1/
│   ├── id: "admin1"
│   ├── username: "admin"
│   ├── password: "hashed_password"
│   └── role: "ADMIN"
│
├── student1/
│   ├── id: "student1"
│   ├── username: "ahmed"
│   ├── password: "hashed_password"
│   ├── firstName: "Ahmed"
│   ├── lastName: "Hassan"
│   ├── email: "ahmed@example.com"
│   ├── role: "STUDENT"
│   ├── xpScore: 150
│   ├── completedLessonIds: ["lesson1", "lesson2", "lesson3"]
│   └── savedLessonIds: ["lesson4", "lesson5"]
│
├── student2/
│   ├── id: "student2"
│   ├── username: "fatima"
│   ├── role: "STUDENT"
│   └── ... (similar structure)
│
└── student3/
    ├── id: "student3"
    ├── username: "mohammed"
    ├── role: "STUDENT"
    └── ... (similar structure)
```

### 2. **Lessons Table** (`/lessons`)
Database structure for storing all learning modules:

```
lessons/
├── lesson1/
│   ├── id: "lesson1"
│   ├── category: "OOP Fundamentals"
│   ├── title: "Object-Oriented Programming Basics"
│   ├── youtubeId: "dQw4w9WgXcQ"
│   ├── dateCreated: "2026-04-17"
│   ├── theoryText: "Object-Oriented Programming (OOP) is a programming..."
│   ├── correctAnswer: "Object"
│   ├── difficulty: "BEGINNER"
│   └── xpReward: 10
│
├── lesson2/
│   ├── id: "lesson2"
│   ├── title: "Classes and Objects in Java"
│   └── ... (similar structure)
│
└── lesson3-6/
    └── ... (more lessons)
```

### 3. **Certificates Table** (`/certificates`)
Database structure for student achievements:

```
certificates/
├── cert-uuid-1/
│   ├── id: "unique-uuid"
│   ├── userId: "student1"
│   ├── category: "OOP Fundamentals"
│   ├── certificateName: "OOP Fundamentals Completion"
│   ├── issueDate: "2026-05-12"
│   └── lessonsCompleted: 3
│
└── cert-uuid-2/
    └── ... (more certificates)
```

### 4. **Student Progress Table** (`/studentProgress`)
Real-time tracking of student learning progress:

```
studentProgress/
├── student1/
│   ├── studentId: "student1"
│   ├── totalXp: 150
│   ├── lessonsCompleted: 3
│   ├── certificatesEarned: 1
│   └── lastUpdated: "2026-05-17"
│
└── student2/
    └── ... (more student records)
```

---

## 🚀 How to Use

### **Option 1: Automatic Initialization (Recommended)**

The database will automatically initialize when you start the application:

```bash
mvn clean install
mvn spring-boot:run
```

Check the console logs for:
```
🔥 Starting Firebase database initialization...
📦 Database is empty. Creating tables and sample data...
✓ Admin user created
✓ Student created: ahmed
✓ Student created: fatima
✓ Student created: mohammed
✓ Lesson created: Object-Oriented Programming Basics
... (more messages)
✅ Firebase database initialization completed successfully!
```

### **Option 2: Manual Initialization (via REST API)**

If automatic initialization doesn't run, manually trigger it:

```bash
# Trigger database initialization
curl -X POST http://localhost:8080/api/admin/init/database

# Check initialization status
curl -X POST http://localhost:8080/api/admin/init/status
```

**Response:**
```json
{
  "message": "✅ Firebase database initialized successfully!",
  "data": {
    "users": 4,
    "lessons": 6,
    "certificates": 3,
    "studentProgress": 3
  }
}
```

---

## 📝 Sample Data Credentials

### Admin Account
- **Username:** `admin`
- **Password:** `admin123`
- **Role:** Admin (can manage content)

### Student Accounts
| Username | Password | Email | XP Score | Lessons Completed |
|----------|----------|-------|----------|-------------------|
| ahmed | password123 | ahmed@example.com | 150 | 3 |
| fatima | password123 | fatima@example.com | 200 | 5 |
| mohammed | password123 | mohammed@example.com | 75 | 2 |

### Sample Lessons
| ID | Title | Category | Difficulty | XP Reward |
|----|-------|----------|-----------|-----------|
| lesson1 | OOP Basics | OOP Fundamentals | BEGINNER | 10 |
| lesson2 | Classes and Objects | OOP Fundamentals | BEGINNER | 15 |
| lesson3 | Inheritance | OOP Concepts | INTERMEDIATE | 20 |
| lesson4 | Polymorphism | OOP Concepts | INTERMEDIATE | 25 |
| lesson5 | Encapsulation | OOP Concepts | BEGINNER | 15 |
| lesson6 | Abstraction | OOP Concepts | INTERMEDIATE | 20 |

---

## 🔍 Verify Data in Firebase Console

1. Open [Firebase Console](https://console.firebase.google.com)
2. Select your project
3. Go to **Realtime Database**
4. Expand the root node to see:
   - ✅ `users` (4 records)
   - ✅ `lessons` (6 records)
   - ✅ `certificates` (3 records)
   - ✅ `studentProgress` (3 records)

---

## 📂 File Structure

New files created for database initialization:

```
src/main/java/com/ihec/
├── util/
│   ├── FirebaseDataInitializer.java    ← Auto-initializes database
│   └── DataMigrationUtil.java          ← Manual CSV migration
│
└── controller/
    ├── DataInitializationController.java ← Manual init endpoints
    └── MigrationController.java         ← Migration endpoints
```

---

## 🔧 Customization

### Add More Sample Data

Edit `FirebaseDataInitializer.java` and add more lessons in the `createSampleLessons()` method:

```java
private List<Lesson> createSampleLessons() {
    // Add your custom lessons here
    Lesson customLesson = new Lesson();
    customLesson.setId("lesson7");
    customLesson.setTitle("Your Custom Lesson");
    customLesson.setCategory("Your Category");
    // ... more properties
    lessons.add(customLesson);
    return lessons;
}
```

### Disable Auto-Initialization

To prevent automatic initialization on startup, rename or comment out the `@EventListener` annotation:

```java
// @EventListener(ApplicationReadyEvent.class)  // Commented out to disable
public void initializeData() {
    // ...
}
```

---

## 🛡️ Security Notes

### Development
- Currently using test mode (anyone can read/write)
- Auto-initialization is enabled

### Before Production
- [ ] Update Firebase Security Rules
- [ ] Remove or secure admin endpoints
- [ ] Disable auto-initialization
- [ ] Use environment variables for sensitive data
- [ ] Implement proper authentication

**Recommended Firebase Rules for Production:**

```json
{
  "rules": {
    "users": {
      "$uid": {
        ".read": "$uid === auth.uid",
        ".write": "$uid === auth.uid || root.child('users').child(auth.uid).child('role').val() === 'ADMIN'",
        ".validate": "newData.hasChildren(['id', 'username', 'role'])"
      }
    },
    "lessons": {
      ".read": "auth !== null",
      ".write": "root.child('users').child(auth.uid).child('role').val() === 'ADMIN'",
      ".validate": "newData.hasChildren(['id', 'title'])"
    },
    "certificates": {
      ".read": "auth !== null",
      ".write": "root.child('users').child(auth.uid).child('role').val() === 'ADMIN'",
      ".validate": "newData.hasChildren(['userId', 'category'])"
    },
    "studentProgress": {
      ".read": "$uid === auth.uid || root.child('users').child(auth.uid).child('role').val() === 'ADMIN'",
      ".write": "root.child('users').child(auth.uid).child('role').val() === 'ADMIN'",
      ".validate": "newData.hasChildren(['studentId'])"
    }
  }
}
```

---

## 🚨 Troubleshooting

| Issue | Solution |
|-------|----------|
| **Data not appearing** | Check console logs for errors; verify Firebase connection |
| **"Cannot initialize database"** | Ensure `firebase-config.json` is in project root |
| **"Permission denied"** | Change Firebase rules to test mode in console |
| **Duplicate data on restart** | Remove old data from Firebase console manually |
| **Students can't log in** | Verify usernames and passwords match the credentials above |

---

## ✅ Next Steps

1. ✅ Build and run the application
2. ✅ Verify data in Firebase Console
3. ✅ Test login with sample credentials
4. ✅ Check student dashboards
5. ✅ Update security rules before production
6. ✅ Replace sample data with real content

---

## 📞 Support

For Firebase help, visit:
- [Firebase Realtime Database Docs](https://firebase.google.com/docs/database)
- [Firebase Admin SDK Java](https://firebase.google.com/docs/database/admin/start)
- [Firebase Security Rules](https://firebase.google.com/docs/database/security)

Your database is now ready! 🚀
