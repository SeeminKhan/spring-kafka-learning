@echo off
REM Start Kafka and MongoDB using Docker Compose

echo Starting Kafka and MongoDB...
echo.

docker compose up -d

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo Infrastructure started successfully!
    echo.
    echo MongoDB: mongodb://localhost:27017
    echo Kafka: localhost:9094
    echo.
    echo Waiting 30 seconds for services to initialize...
    echo ========================================
    timeout /t 30 /nobreak
    echo.
    echo Services should be ready now.
    echo.
    echo To check status: docker compose ps
    echo To view logs: docker compose logs
    echo To stop: docker compose down
) else (
    echo.
    echo ========================================
    echo Failed to start infrastructure!
    echo ========================================
)
