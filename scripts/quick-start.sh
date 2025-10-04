#!/bin/bash

echo "🚀 Starting EduAI Quest Backend Setup..."

# Create necessary directories
mkdir -p logs
mkdir -p uploads/courses
mkdir -p uploads/profiles
mkdir -p uploads/temp

# Make scripts executable
chmod +x scripts/*.sh

# Build the application
echo "📦 Building application..."
./mvnw clean package -DskipTests

if [ $? -eq 0 ]; then
    echo "✅ Build successful"
else
    echo "❌ Build failed"
    exit 1
fi

# Start Docker services
echo "🐳 Starting Docker services..."
docker-compose -f docker/docker-compose.yml down
docker-compose -f docker/docker-compose.yml up -d

# Wait for services to be ready
echo "⏳ Waiting for services to be ready..."
sleep 30

# Check if services are running
if curl -f http://localhost:8080/api/actuator/health > /dev/null 2>&1; then
    echo "✅ Application is running!"
    echo ""
    echo "📊 Access URLs:"
    echo "   Application: http://localhost:8080/api"
    echo "   Swagger UI:  http://localhost:8080/api/swagger-ui.html"
    echo "   Health:      http://localhost:8080/api/actuator/health"
    echo ""
    echo "🔑 Default Users:"
    echo "   Admin:   admin@eduai.com / admin123"
    echo "   Teacher: teacher@eduai.com / teacher123"
    echo "   Student: student@eduai.com / student123"
else
    echo "❌ Application failed to start. Check logs with: docker-compose -f docker/docker-compose.yml logs app"
fi