#!/bin/bash

# Database backup script
BACKUP_DIR="./backups"
DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_FILE="$BACKUP_DIR/eduai_quest_$DATE.sql"

mkdir -p $BACKUP_DIR

echo "Backing up database to $BACKUP_FILE..."

docker exec eduai-mysql mysqldump -u eduai_user -ppassword eduai_quest > $BACKUP_FILE

if [ $? -eq 0 ]; then
    echo "Backup completed successfully: $BACKUP_FILE"

    # Keep only last 5 backups
    ls -t $BACKUP_DIR/eduai_quest_*.sql | tail -n +6 | xargs rm -f
else
    echo "Backup failed!"
    exit 1
fi