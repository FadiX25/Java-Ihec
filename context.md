# Project Context: IHEC-JLearn Application

## 1. Project Overview
*   **Application Name:** IHEC-JLearn
*   **Description:** A lightweight, desktop-based E-Learning platform designed for IHEC students to learn "Object-Oriented Programming in Java".
*   **Design Philosophy:** "DataCamp-Lite" — A clean, split-screen interface combining theory, video tutorials, and practical gap-fill coding exercises.
*   **Target Users:**
    1.  **Students:** Access courses, watch videos, solve exercises, track XP/Progress.
    2.  **Admins:** Manage content (add lessons via CSV).

## 2. Technical Stack & Constraints
*   **Language:** Java (JDK 17 or higher recommended).
*   **GUI Framework:** Java Swing (`javax.swing` & `java.awt`).
*   **Architecture:** MVC (Model-View-Controller) pattern.
*   **Data Persistence:** **CSV Files** (No SQL Database).
*   **External APIs:** 
    *   **YouTube:** Integrated via `java.awt.Desktop` (opens browser) to keep the app lightweight.
    *   **Java Date API:** `java.time.LocalDate` must be used for tracking dates.
*   **Mandatory OO Concepts:**
    *   **Encapsulation:** Private fields with public Getters/Setters.
    *   **Inheritance:** Abstract parent class `User` with children `Student` and `Admin`.
    *   **Polymorphism:** Overriding methods (e.g., `showDashboard()`) to handle role-specific UI.

## 3. Project File Structure
The project must adhere to this package structure to maintain the MVC pattern:

```text
src/
├── model/                  # Data Transfer Objects (POJOs)
│   ├── User.java           # Abstract Parent Class
│   ├── Student.java        # Extends User
│   ├── Admin.java          # Extends User
│   └── Lesson.java         # Represents a single learning module
├── view/                   # UI Components (Swing)
│   ├── MainApplication.java # The main JFrame & Entry Point
│   ├── LoginView.java      # Login Form
│   ├── DashboardView.java  # Student Progress & Stats
│   └── LearningView.java   # The Split-Pane Lesson Interface
├── controller/             # Business Logic & Data Handling
│   └── CsvDataManager.java # Handles reading/writing to .csv files
└── utils/                  # Helper Classes
    └── StyleUtils.java     # Static colors, fonts, and design constants

4. Data Models & Schema (The "Model")
A. CSV Data Structure (Persistence)
The application reads from and writes to two files in the project root.
1. users.csv
Columns: id, username, password, role, xp_score
Example Data:
1,ahmed,12345,STUDENT,50
2,prof_java,admin123,ADMIN,0

Columns: id, title, youtube_id, date_created, correct_answer_keyword, theory_text
Example Data:

101,Intro to Java,Hl-zzrqQoSE,2023-10-01,class,"Java is an Object Oriented language..."

B. Class Definitions
User (Abstract):
Fields: id, username, password.
Method: abstract void showDashboard().
Lesson (Encapsulation):
Fields: private LocalDate dateCreated.
Logic: Use LocalDate.parse() when reading from CSV.
5. UI/UX Design System (The "View")
The design is based on specific Figma mockups using a professional "Academic Blue" theme.
Color Palette (StyleUtils.java)
Primary Blue: #0056D2 (Headers, Primary Buttons).
Background Gray: #F0F2F5 (Dashboard background).
Card White: #FFFFFF (Content containers).
Editor Background: #2D2D2D (Dark mode for code areas).
Success Green: #28A745 (Submit buttons, Progress bars).
Screen Requirements
Login View:
Centered white card on a gray background.
Fields: Email/Username, Password.
Dashboard View:
Top Navigation Bar (Blue).
Central area showing Course Cards with a JProgressBar.
Learning View (The Core Feature):
Layout: JSplitPane (Horizontal Split).
Left Panel: Theory text (Top) + "Watch Video" Button (Bottom).
Right Panel: Dark JTextArea (Code Editor) + "Submit" Button.
6. Functional Logic (The "Controller")
Feature: Authentication
Input: User enters credentials.
Process: CsvDataManager reads users.csv, loops through rows, matches username/password.
Output: Returns a User object. If role.equals("STUDENT"), instantiate Student; otherwise Admin.
Feature: Lesson Execution
Video: Clicking "Watch Video" triggers Desktop.getDesktop().browse(URI).
Grading:
User types code into the right-hand text area.
Clicking "Submit" compares the user's text against the correct_answer_keyword from Lesson object.
Condition: userInput.contains(correctAnswer).
Result: If true, show success message and increase Student XP.
7. Development Guidelines
Error Handling: All File I/O must be wrapped in try-catch blocks.
Modularity: The UI classes should never access CSV files directly; they must ask CsvDataManager for data.
Simplicity: Do not use external libraries (Maven/Gradle) unless necessary. Standard Java libraries preferred.



# UML Design Documentation: IHEC-JLearn

## 1. Class Diagram (Structure)
This diagram illustrates the **Model-View-Controller (MVC)** architecture, highlighting **Inheritance** (User hierarchy) and **Encapsulation** (Private fields).

```mermaid
classDiagram
    %% --- RELATIONSHIPS ---
    User <|-- Student : Inherits
    User <|-- Admin : Inherits
    MainApplication --> CsvDataManager : Uses
    MainApplication o-- User : Manages Current User
    CsvDataManager ..> User : Creates
    CsvDataManager ..> Lesson : Creates

    %% --- MODEL PACKAGE ---
    class User {
        <<Abstract>>
        #int id
        #String username
        #String password
        #String role
        +getRole() String
        +showDashboard()* void
    }

    class Student {
        -int xpScore
        -List~int~ completedLessonIds
        +getXpScore() int
        +addXp(int points) void
        +showDashboard() void
    }

    class Admin {
        +addLesson(Lesson l) void
        +showDashboard() void
    }

    class Lesson {
        -int id
        -String title
        -String youtubeId
        -String theoryText
        -String correctAnswer
        -LocalDate dateCreated
        +getTitle() String
        +checkAnswer(String input) boolean
    }

    %% --- CONTROLLER/DATA PACKAGE ---
    class CsvDataManager {
        -String USER_FILE
        -String LESSON_FILE
        +loadUsers() List~User~
        +loadLessons() List~Lesson~
        +saveUserProgress(Student s) void
    }

    %% --- VIEW PACKAGE ---
    class MainApplication {
        -CardLayout cardLayout
        -JPanel mainPanel
        +showLogin()
        +showDashboard()
        +showLearning()
    }

    

    Use Case Diagram (Functionality)
This diagram defines the Scope of the application and what each actor (Student vs Admin) is allowed to do.

usecaseDiagram
    actor "Student" as S
    actor "Admin" as A

    package "IHEC-JLearn System" {
        usecase "Login" as UC1
        usecase "View Course Dashboard" as UC2
        usecase "Watch YouTube Video" as UC3
        usecase "Solve Code Exercise" as UC4
        usecase "Track XP Progress" as UC5
        usecase "Add New Lesson (CSV)" as UC6
    }

    %% Student Relationships
    S --> UC1
    S --> UC2
    S --> UC3
    S --> UC4
    S --> UC5

    %% Admin Relationships
    A --> UC1
    A --> UC6

    3. Sequence Diagram (Logic Flow)
This diagram visualizes the Login Process time-flow: from the user entering credentials to the system loading data from the CSV and displaying the correct dashboard.

sequenceDiagram
    participant User as Actor
    participant GUI as LoginView
    participant Main as MainApplication
    participant Data as CsvDataManager
    participant DB as users.csv

    Note over User, DB: Scenario: User Logs In

    User->>GUI: Enters Username/Password & Clicks Login
    GUI->>Main: authenticate(username, password)
    
    activate Main
    Main->>Data: loadUsers()
    
    activate Data
    Data->>DB: Read File
    DB-->>Data: Return CSV Lines
    Data-->>Main: Return List<User>
    deactivate Data

    Main->>Main: Loop & Validate Credentials
    
    alt Credentials Valid
        Main->>Main: Determine Role (Student/Admin)
        Main->>GUI: switchView("DASHBOARD")
        GUI-->>User: Show Dashboard
    else Invalid Credentials
        Main-->>GUI: Show Error Message
    end
    deactivate Main