@echo off
REM Run script for Windows

echo Starting Order Processing Backend...
echo.

if not exist "target\order-processing-1.0.0.jar" (
    echo JAR file not found! Building first...
    call build.bat
    if %ERRORLEVEL% NEQ 0 (
        echo Build failed. Cannot start application.
        exit /b 1
    )
)

echo Starting Spring Boot application on http://localhost:8080
echo Press Ctrl+C to stop
echo.

java -jar target\order-processing-1.0.0.jar
