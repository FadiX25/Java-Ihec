# IHEC-JLearn Spring Boot Migration

Welcome to the modernized IHEC-JLearn platform! This is a full Spring Boot web application with an HTML/CSS frontend and Firebase Realtime Database backend.

## 📋 Prerequisites

- **Java 17** or higher
- **Maven 3.6+**
- **Firebase Project** (with Realtime Database enabled)
- **Node.js/npm** (optional, for frontend tooling)

## 🚀 Getting Started

### 1. Firebase Configuration

1. Create a Firebase project at [https://console.firebase.google.com](https://console.firebase.google.com)
2. Enable Realtime Database
3. Download your service account credentials JSON file
4. Create `firebase-config.json` in the project root and paste your Firebase credentials

```json
{
  "type": "service_account",
  "project_id": "your-project-id",
  "private_key_id": "...",
  "private_key": "...",
  ...
}
```

### 2. Application Configuration

Update `src/main/resources/application.properties`:

```properties
firebase.database.url=https://your-project.firebaseio.com
firebase.config.path=./firebase-config.json
```

### 3. Build the Application

```bash
mvn clean install
```

### 4. Run the Application

```bash
mvn spring-boot:run
```

The application will be available at: **http://localhost:8080**

## 📁 Project Structure

```
src/main/
├── java/com/ihec/
│   ├── IhecApplication.java           # Entry point
│   ├── config/
│   │   ├── FirebaseConfig.java        # Firebase setup
│   │   └── SecurityConfig.java        # Spring Security
│   ├── controller/
│   │   ├── AuthController.java        # Authentication endpoints
│   │   ├── LessonController.java      # Lesson management
│   │   └── StudentController.java     # Student profile & progress
│   ├── model/
│   │   ├── User.java                  # Abstract user class
│   │   ├── Student.java               # Student model
│   │   ├── Admin.java                 # Admin model
│   │   ├── Lesson.java                # Lesson model
│   │   ├── Certificate.java           # Certificate model
│   │   └── SavedCourse.java           # Saved lesson model
│   ├── service/
│   │   ├── FirebaseUserService.java   # User operations
│   │   └── FirebaseLessonService.java # Lesson operations
│   └── dto/
│       ├── AuthRequest.java           # Login request
│       └── AuthResponse.java          # Login response
├── resources/
│   ├── application.properties         # Configuration
│   ├── templates/                     # Thymeleaf templates (if needed)
│   └── static/
│       ├── index.html                 # Entry page
│       ├── login.html                 # Login page
│       ├── register.html              # Registration page
│       ├── dashboard.html             # Student dashboard
│       ├── learning.html              # Learning/exercise page
│       ├── admin-dashboard.html       # Admin panel
│       ├── css/                       # Stylesheets
│       │   ├── style.css              # Main styles
│       │   ├── login.css              # Login page styles
│       │   ├── dashboard.css          # Dashboard styles
│       │   ├── learning.css           # Learning page styles
│       │   └── admin.css              # Admin panel styles
│       └── js/                        # JavaScript files
│           ├── auth.js                # Authentication helpers
│           ├── dashboard.js           # Dashboard functionality
│           ├── learning.js            # Learning page functionality
│           └── admin.js               # Admin panel functionality
```

## 🔑 API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration
- `POST /api/auth/logout` - User logout

### Lessons
- `GET /api/lessons` - Get all lessons
- `GET /api/lessons/{id}` - Get specific lesson
- `GET /api/lessons/category/{category}` - Get lessons by category
- `POST /api/lessons` - Create lesson (admin only)
- `PUT /api/lessons/{id}` - Update lesson (admin only)
- `DELETE /api/lessons/{id}` - Delete lesson (admin only)
- `POST /api/lessons/{id}/check-answer` - Verify exercise answer

### Students
- `GET /api/students/{id}` - Get student profile
- `PUT /api/students/{id}` - Update student profile
- `POST /api/students/{id}/complete-lesson/{lessonId}` - Mark lesson complete
- `POST /api/students/{id}/save-lesson/{lessonId}` - Save/bookmark lesson
- `DELETE /api/students/{id}/saved-lesson/{lessonId}` - Remove saved lesson

## 🎨 Design System

The application uses a modern **Academic Blue** theme:

- **Primary Blue**: #0056D2
- **Dark Blue**: #003D8F
- **Light Blue**: #E8F0FE
- **Background Gray**: #F0F2F5
- **Card White**: #FFFFFF
- **Success Green**: #28A745
- **Error Red**: #DC3545

## 🔒 Security

- Spring Security for authentication
- BCrypt password hashing
- CORS enabled for API access
- JWT-ready infrastructure for token-based authentication

## 📊 Features

✅ User authentication (Student & Admin roles)
✅ Course/Lesson management
✅ Interactive code exercises with auto-grading
✅ YouTube video integration
✅ XP/Progress tracking
✅ Save/bookmark lessons
✅ Certificate tracking
✅ Admin dashboard for content management
✅ Responsive design (mobile-friendly)
✅ Modern UI with animations

## 🛠️ Development

### Running in Development Mode

```bash
mvn spring-boot:run
```

For hot-reload with frontend changes, use:
```bash
mvn spring-boot:run -DskipTests
```

### Building for Production

```bash
mvn clean package -DskipTests
java -jar target/Java-Ihec-1.0-SNAPSHOT.jar
```

## 📝 Firebase Database Schema

### Users Collection
```json
{
  "users": {
    "{userId}": {
      "id": "string",
      "username": "string",
      "password": "hashed",
      "role": "STUDENT|ADMIN",
      "firstName": "string",
      "lastName": "string",
      "email": "string",
      "xpScore": 0,
      "completedLessonIds": ["..."],
      "savedLessonIds": ["..."]
    }
  }
}
```

### Lessons Collection
```json
{
  "lessons": {
    "{lessonId}": {
      "id": "string",
      "category": "string",
      "title": "string",
      "youtubeId": "string",
      "dateCreated": "date",
      "correctAnswer": "string",
      "theoryText": "string",
      "difficulty": "BEGINNER|INTERMEDIATE|ADVANCED",
      "xpReward": 10
    }
  }
}
```

## 🐛 Troubleshooting

### Firebase Connection Issues
- Verify `firebase-config.json` path is correct
- Check Firebase credentials have Realtime Database access
- Ensure Firebase security rules allow read/write

### Port Already in Use
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

### Build Failures
```bash
mvn clean install -U
```

## 📚 Learning Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Firebase Realtime Database](https://firebase.google.com/docs/database)
- [Spring Security](https://spring.io/projects/spring-security)

## 📄 License

This project is part of the IHEC educational platform.

## 🤝 Contributing

For contributions, please follow the existing code structure and naming conventions.

---

**Happy Learning! 🚀**
