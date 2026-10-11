@echo off
REM Stop Kafka and MongoDB

echo Stopping Kafka and MongoDB...
echo.

docker compose down

echo.
echo Infrastructure stopped.
