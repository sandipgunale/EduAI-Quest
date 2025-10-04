#!/bin/bash

echo "Stopping EduAI Quest Backend..."

docker-compose -f docker/docker-compose.yml down

echo "All services stopped."