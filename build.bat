@echo off
REM ============================================================================
REM Java Project Build Script
REM ============================================================================
REM Usage:
REM   build.bat          - Compile and run the application
REM   build.bat compile  - Only compile the source files
REM   build.bat run      - Only run (requires previous compilation)
REM   build.bat clean    - Remove compiled class files
REM   build.bat rebuild  - Clean, compile, and run
REM ============================================================================

SETLOCAL EnableDelayedExpansion

REM ============================================================================
REM CONFIGURATION
REM ============================================================================
SET PROJECT_NAME=Java-Ihec
SET SRC_DIR=src
SET OUT_DIR=out
SET MAIN_CLASS=app.MainApplication
SET JAVA_ENCODING=UTF-8
SET JAVA_VERSION=17
SET EXTRA_CLASSPATH=

REM ============================================================================
REM ENVIRONMENT CHECKS
REM ============================================================================
where javac >nul 2>&1
IF ERRORLEVEL 1 (
    echo [ERROR] Java compiler javac not found in PATH!
    echo Please ensure JDK is installed and JAVA_HOME is set correctly.
    exit /b 1
)

where java >nul 2>&1
IF ERRORLEVEL 1 (
    echo [ERROR] Java runtime java not found in PATH!
    echo Please ensure JDK/JRE is installed and added to PATH.
    exit /b 1
)

echo.
echo ============================================================
echo  %PROJECT_NAME% - Build Script
echo ============================================================
echo.
java -version 2>&1 | findstr /i "version"
echo.

REM ============================================================================
REM PARSE ARGUMENTS
REM ============================================================================
SET ACTION=%~1
IF "%ACTION%"=="" SET ACTION=all

IF /I "%ACTION%"=="compile" GOTO compile
IF /I "%ACTION%"=="run" GOTO run
IF /I "%ACTION%"=="clean" GOTO clean
IF /I "%ACTION%"=="rebuild" GOTO rebuild
IF /I "%ACTION%"=="all" GOTO all
IF /I "%ACTION%"=="help" GOTO help

echo [ERROR] Unknown action: %ACTION%
GOTO help

:help
echo Usage: %~nx0 [action]
echo.
echo Actions:
echo   [none]    Compile and run the application (default)
echo   compile   Only compile the source files
echo   run       Only run the application
echo   clean     Remove compiled class files
echo   rebuild   Clean, compile, and run
echo   help      Display this help message
echo.
exit /b 0

REM ============================================================================
REM CLEAN
REM ============================================================================
:clean
echo [CLEAN] Removing compiled files...
IF EXIST "%OUT_DIR%" (
    rmdir /s /q "%OUT_DIR%"
    echo [CLEAN] Removed %OUT_DIR% directory.
) ELSE (
    echo [CLEAN] Nothing to clean.
)
echo.
exit /b 0

REM ============================================================================
REM COMPILE
REM ============================================================================
:compile
echo [COMPILE] Starting compilation...
echo.

IF NOT EXIST "%OUT_DIR%" (
    mkdir "%OUT_DIR%"
    echo [COMPILE] Created output directory: %OUT_DIR%
)

echo [COMPILE] Searching for Java source files...

SET TEMP_FILE_LIST=%TEMP%\java_files_%RANDOM%.txt
dir /s /b "%SRC_DIR%\*.java" > "%TEMP_FILE_LIST%" 2>nul

FOR %%A IN ("%TEMP_FILE_LIST%") DO IF %%~zA==0 (
    echo [ERROR] No Java source files found in %SRC_DIR%!
    del "%TEMP_FILE_LIST%" 2>nul
    exit /b 1
)

FOR /f %%a IN ('type "%TEMP_FILE_LIST%" ^| find /c /v ""') DO SET FILE_COUNT=%%a
echo [COMPILE] Found %FILE_COUNT% Java source file(s).
echo.

SET CLASSPATH=%OUT_DIR%

echo [COMPILE] Compiling with encoding: %JAVA_ENCODING%
echo [COMPILE] Output directory: %OUT_DIR%
echo.

REM Compile each file individually to handle paths with spaces
SET COMPILE_ERROR=0
FOR /F "delims=" %%F IN ('type "%TEMP_FILE_LIST%"') DO (
    javac --release %JAVA_VERSION% -encoding %JAVA_ENCODING% -d "%OUT_DIR%" -sourcepath "%SRC_DIR%" -cp "%CLASSPATH%" "%%F"
    IF ERRORLEVEL 1 SET COMPILE_ERROR=1
)

IF "%COMPILE_ERROR%"=="1" (
    del "%TEMP_FILE_LIST%" 2>nul
    echo.
    echo ============================================================
    echo [ERROR] Compilation FAILED!
    echo ============================================================
    exit /b 1
)

del "%TEMP_FILE_LIST%" 2>nul

echo.
echo ============================================================
echo [SUCCESS] Compilation completed successfully!
echo ============================================================
echo.
exit /b 0

REM ============================================================================
REM RUN
REM ============================================================================
:run
echo [RUN] Starting application...
echo.

IF NOT EXIST "%OUT_DIR%" (
    echo [ERROR] Output directory %OUT_DIR% not found!
    echo [ERROR] Please compile first: %~nx0 compile
    exit /b 1
)

SET CLASSPATH=%OUT_DIR%;.

echo [RUN] Main class: %MAIN_CLASS%
echo [RUN] Classpath: %CLASSPATH%
echo.
echo ============================================================
echo [RUN] Application starting...
echo ============================================================
echo.

java -cp "%CLASSPATH%" %MAIN_CLASS%

echo.
echo ============================================================
echo [RUN] Application terminated.
echo ============================================================
echo.
exit /b 0

REM ============================================================================
REM ALL (COMPILE + RUN)
REM ============================================================================
:all
CALL :compile
IF ERRORLEVEL 1 exit /b 1
CALL :run
exit /b 0

REM ============================================================================
REM REBUILD (CLEAN + COMPILE + RUN)
REM ============================================================================
:rebuild
CALL :clean
CALL :compile
IF ERRORLEVEL 1 exit /b 1
CALL :run
exit /b 0

ENDLOCAL
