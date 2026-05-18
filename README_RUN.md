Run instructions for IHEC-JLearn (Windows - cmd.exe)

Prereqs:
- JDK 17+ installed and `java` on PATH
- Internet access (first run to download Maven and dependencies)

From project root (D:\Downloads-d\Java-Ihec):

1) Verify Java
```cmd
java -version
```

2) Build with the Maven Wrapper (no global Maven required)
```cmd
cd /d D:\Downloads-d\Java-Ihec
.\mvnw.cmd -DskipTests package
```

3) Run the app
```cmd
.\mvnw.cmd spring-boot:run
```

4) Or run the packaged jar after a successful package
```cmd
java -jar target\Java-Ihec-1.0-SNAPSHOT.jar
```

Firebase service account
- If the app needs the Google credentials via environment variable, set it before running:
```cmd
setx GOOGLE_APPLICATION_CREDENTIALS "D:\Downloads-d\Java-Ihec\learnhub-40adc-firebase-adminsdk-fbsvc-ef7f7a6078.json"
# then open a new terminal so the env var is available
```

Troubleshooting:
- If `mvnw.cmd` is not recognized in PowerShell, prefix with `./mvnw.cmd` or run from cmd.exe.
- If Maven download fails: check firewall/proxy settings.

