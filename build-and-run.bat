@echo off
REM Build and Run Script for IHEC-JLearn Spring Boot Application (Windows)

echo ======================================
echo IHEC-JLearn Spring Boot Build and Run
echo ======================================
echo.

REM Ensure Maven is available for this session
set "MAVEN_BIN=C:\ProgramData\chocolatey\lib\maven\apache-maven-3.9.15\bin"
if exist "%MAVEN_BIN%\mvn.cmd" (
    set "PATH=%PATH%;%MAVEN_BIN%"
)

REM Check Java version
echo Checking Java version...
java -version
echo.

REM Check Maven installation
echo Checking Maven installation...
mvn -version
echo.

REM Clean build
echo Cleaning previous build...
mvn clean

REM Install dependencies
echo.
echo Installing dependencies...
mvn install

REM Check if build was successful
if %ERRORLEVEL% EQU 0 (
    echo.
    echo Build successful!
    echo.
    echo ======================================
    echo Starting Spring Boot Application...
    echo ======================================
    echo.
    echo Application will be available at:
    echo    http://localhost:8080
    echo.
    echo Default Credentials:
    echo    Create new account via registration
    echo.
    echo Press Ctrl+C to stop the application
    echo.
    
    REM Run the application
    mvn spring-boot:run
) else (
    echo.
    echo Build failed! Please check the errors above.
    pause
    exit /b 1
)
