# Clear Database - Fix Duplicate Users

## Problem
The database has duplicate users with username "Dinuka@1" causing login failures.

## Solution: Clear MongoDB Collections

### Option 1: Using MongoDB Compass or mongo shell
```javascript
// Connect to your MongoDB and run:
use edufine

// Drop all collections
db.users.deleteMany({})
db.students.deleteMany({})
db.staff.deleteMany({})
db.departments.deleteMany({})
db.courses.deleteMany({})
db.enrollments.deleteMany({})
```

### Option 2: Using MongoDB Atlas Web Interface
1. Go to MongoDB Atlas: https://cloud.mongodb.com
2. Navigate to your cluster
3. Click "Collections"
4. For each collection (users, students, staff), click the trash icon to delete all documents

### After Clearing
1. Stop your Spring Boot application (Ctrl+C in terminal)
2. Start it again with: `mvn spring-boot:run`
3. The seeder will run and create fresh data without duplicates

## Prevention
The DatabaseSeeder has been updated to check `userRepository.count()` to prevent duplicate seeding.
