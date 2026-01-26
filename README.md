# EduFine Backend - Education Management Platform

A comprehensive Spring Boot backend application for an education management platform, built with Java 21 and following clean architecture principles.

## 🏗️ Project Structure

This project follows a layered architecture pattern for better maintainability and separation of concerns:

```
src/main/java/com/edufine/backend/
├── BackendApplication.java          # Main Spring Boot application class
├── config/                          # Configuration classes
├── controller/                      # REST API controllers
│   └── CourseController.java        # Course management endpoints
├── service/                         # Business logic layer
│   ├── UserService.java            # User management business logic
│   └── CourseService.java          # Course management business logic
├── repository/                     # Data access layer
│   ├── UserRepository.java         # User data access
│   └── CourseRepository.java       # Course data access
├── entity/                         # JPA entities (database models)
│   ├── User.java                   # User entity
│   ├── UserRole.java               # User role enum
│   ├── Course.java                 # Course entity
│   ├── Enrollment.java             # Course enrollment entity
│   └── Lesson.java                 # Course lesson entity
├── dto/                            # Data Transfer Objects
│   ├── UserDto.java                # User data transfer object
│   └── CourseDto.java              # Course data transfer object
├── exception/                      # Exception handling
│   └── GlobalExceptionHandler.java # Global exception handler
└── security/                       # Security configuration (future use)
```

## 🚀 Features

### User Management
- User registration and authentication
- Role-based access (Student, Instructor, Admin)
- User profile management
- Account activation/deactivation

### Course Management
- Create and manage courses
- Course publishing/unpublishing
- Course search and filtering
- Category-based organization
- Instructor assignment

### Enrollment System
- Student course enrollment
- Progress tracking
- Course completion tracking
- Rating and review system

### Content Management
- Lesson organization within courses
- Video content support
- Preview lessons for free access

## 🛠️ Technology Stack

- **Framework**: Spring Boot 3.5.9
- **Language**: Java 21
- **Database**: H2 (development), configurable for production
- **ORM**: Hibernate/JPA
- **Build Tool**: Maven
- **Architecture**: Layered Architecture (Controller → Service → Repository)

## 📋 Prerequisites

- Java 21 or higher
- Maven 3.6+ (or use included Maven wrapper)
- Git

## 🔧 Installation & Setup

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd edufine-backend
   ```

2. **Set Java environment (if not set):**
   ```bash
   # Windows PowerShell
   $env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
   ```

3. **Run the application:**
   ```bash
   # Windows
   .\mvnw spring-boot:run

   # Linux/Mac
   ./mvnw spring-boot:run
   ```

4. **Access the application:**
   - API Base URL: `http://localhost:8080`
   - H2 Database Console: `http://localhost:8080/h2-console`
     - JDBC URL: `jdbc:h2:mem:edufinedb`
     - Username: `sa`
     - Password: (leave empty)

## 📚 API Endpoints

### Course Management
- `GET /api/courses` - Get all courses
- `GET /api/courses/published` - Get published courses
- `GET /api/courses/published/newest` - Get newest published courses
- `GET /api/courses/published/popular` - Get popular published courses
- `GET /api/courses/{id}` - Get course by ID
- `GET /api/courses/instructor/{instructorId}` - Get courses by instructor
- `GET /api/courses/category/{category}` - Get courses by category
- `GET /api/courses/search?keyword={keyword}` - Search courses
- `POST /api/courses/instructor/{instructorId}` - Create new course
- `PUT /api/courses/{id}` - Update course
- `DELETE /api/courses/{id}` - Delete course
- `POST /api/courses/{id}/publish` - Publish course
- `POST /api/courses/{id}/unpublish` - Unpublish course

### Statistics
- `GET /api/courses/stats/published-count` - Get published courses count

## 🗃️ Database Schema

The application uses the following main entities:

- **users**: User accounts with roles (STUDENT, INSTRUCTOR, ADMIN)
- **courses**: Course information with instructor relationship
- **enrollments**: Student course enrollments with progress tracking
- **lessons**: Course content organized by order

## 🔒 Security Features

- Role-based access control
- Password encryption (to be implemented)
- CORS configuration for cross-origin requests
- Global exception handling

## 🧪 Testing

Run tests with:
```bash
./mvnw test
```

## 📦 Build & Deployment

### Development Build
```bash
./mvnw clean compile
```

### Production Build
```bash
./mvnw clean package -Dspring.profiles.active=prod
```

### Run JAR
```bash
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

## 🔄 Development Workflow

1. Create entities in the `entity` package
2. Create corresponding DTOs in the `dto` package
3. Implement repository interfaces in the `repository` package
4. Add business logic in the `service` package
5. Create REST endpoints in the `controller` package
6. Add configuration in the `config` package as needed

## 🤝 Contributing

1. Follow the established package structure
2. Write clear, documented code
3. Add appropriate tests
4. Update documentation as needed

## 📄 License

This project is licensed under the MIT License.

## 📞 Support

For questions or issues, please create an issue in the repository or contact the development team.

---

**Note**: This is a development setup using H2 in-memory database. For production deployment, configure a persistent database like PostgreSQL or MySQL in the `application-prod.properties` file.
