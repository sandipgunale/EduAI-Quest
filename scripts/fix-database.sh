#!/bin/bash

echo "🔧 Fixing Database Issues..."

# Stop any running containers
echo "🛑 Stopping existing containers..."
docker-compose -f docker/docker-compose.yml down

# Remove volumes to start fresh
echo "🗑️ Removing old volumes..."
docker volume rm -f eduai-quest-backend_mysql_data 2>/dev/null || true

# Start MySQL first
echo "🐳 Starting MySQL..."
docker-compose -f docker/docker-compose.yml up -d mysql

# Wait for MySQL to be ready
echo "⏳ Waiting for MySQL to be ready..."
until docker exec eduai-mysql mysql -u root -prootpassword -e "SELECT 1" &> /dev/null; do
    echo "Waiting for MySQL..."
    sleep 5
done

# Create database manually if needed
echo "🗃️ Creating database..."
docker exec eduai-mysql mysql -u root -prootpassword -e "CREATE DATABASE IF NOT EXISTS eduai_quest CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" || {
    echo "❌ Failed to create database"
    exit 1
}

echo "✅ Database created successfully"

# Start all services
echo "🚀 Starting all services..."
docker-compose -f docker/docker-compose.yml up -d

echo "⏳ Waiting for services to be ready..."
sleep 30

# Check if application is running
if curl -f http://localhost:8080/api/actuator/health > /dev/null 2>&1; then
    echo "✅ Application is running successfully!"
    echo ""
    echo "📊 Access URLs:"
    echo "   Application: http://localhost:8080/api"
    echo "   Swagger UI:  http://localhost:8080/api/swagger-ui.html"
    echo ""
    echo "🔑 Default Users:"
    echo "   Admin:   admin@eduai.com / admin123"
    echo "   Teacher: teacher@eduai.com / teacher123"
    echo "   Student: student@eduai.com / student123"
else
    echo "❌ Application failed to start"
    echo "Check logs with: docker-compose -f docker/docker-compose.yml logs app"
fi