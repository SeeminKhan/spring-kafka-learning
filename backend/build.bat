@echo off
REM Build script for Windows (when Maven is not installed)

echo Building Order Processing Backend...
echo.

REM Check if Maven is installed
where mvn >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo Maven found! Using local Maven installation...
    mvn clean package -DskipTests
) else (
    echo Maven not found. Using Maven from Docker...
    echo This may take a few minutes on first run...
    echo.
    docker run --rm -v "%CD%":/app -w /app maven:3.9-eclipse-temurin-21 mvn clean package -DskipTests
)

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo Build successful!
    echo JAR file: target\order-processing-1.0.0.jar
    echo ========================================
) else (
    echo.
    echo ========================================
    echo Build failed! Check the errors above.
    echo ========================================
)
