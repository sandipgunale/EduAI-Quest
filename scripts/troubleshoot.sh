#!/bin/bash

echo "🔧 EduAI Quest Troubleshooting"

echo "1. Checking Docker..."
docker --version
docker-compose --version

echo "2. Checking running containers..."
docker ps

echo "3. Checking MySQL container..."
docker exec eduai-mysql mysql -u root -prootpassword -e "SELECT 1" > /dev/null 2>&1
if [ $? -eq 0 ]; then
    echo "✅ MySQL is accessible"

    echo "4. Checking database and tables..."
    docker exec eduai-mysql mysql -u eduai_user -ppassword eduai_quest -e "
        SHOW TABLES;
        SELECT COUNT(*) as user_count FROM users;
        SELECT COUNT(*) as course_count FROM courses;
    "
else
    echo "❌ MySQL is not accessible"
fi

echo "5. Checking application logs..."
docker logs eduai-backend --tail 50

echo "6. Checking health endpoint..."
curl -s http://localhost:8080/api/actuator/health | jq . 2>/dev/null || curl -s http://localhost:8080/api/actuator/health

echo "7. Disk space..."
df -h

echo "8. Checking network..."
docker network ls | grep eduai

echo "9. Checking volumes..."
docker volume ls | grep eduai

echo "🔍 Troubleshooting complete!"