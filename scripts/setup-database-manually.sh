#!/bin/bash

echo "🗃️ Manual Database Setup"

# Check if MySQL is running
if ! docker ps | grep -q eduai-mysql; then
    echo "❌ MySQL container is not running"
    echo "Please run: docker-compose -f docker/docker-compose.yml up -d mysql"
    exit 1
fi

# Create database
echo "Creating database..."
docker exec eduai-mysql mysql -u root -prootpassword -e "
CREATE DATABASE IF NOT EXISTS eduai_quest CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE eduai_quest;
SHOW TABLES;
"

echo "✅ Database setup complete"
echo "You can now start the application"