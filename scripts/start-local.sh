#!/bin/bash

echo "Starting EduAI Quest Backend locally..."

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "Docker is not running. Please start Docker first."
    exit 1
fi

# Start services
docker-compose -f docker/docker-compose.yml up -d

echo "Services started:"
echo "- MySQL: localhost:3306"
echo "- Redis: localhost:6379"
echo "- Application: http://localhost:8080/api"
echo "- Swagger UI: http://localhost:8080/api/swagger-ui.html"

echo "To view logs: docker-compose -f docker/docker-compose.yml logs -f"