# EduFine UI Readme (Angular)

This guide describes how to build an Angular UI that covers authentication, courses, modules, departments, and students for the EduFine backend. It maps UI screens to current API endpoints and highlights access rules, data models, and known backend limitations.

## Scope and assumptions

- Backend base URL: http://localhost:8080
- Auth is JWT based. The UI must attach Authorization: Bearer <token> on protected routes.
- Roles use a hierarchy: SUPER_ADMIN > LIC > LECTURER > INSTRUCTOR > STAFF > STUDENT.
- Course create/update/delete endpoints exist but are currently stubs and return placeholder responses.
- There are no lesson CRUD endpoints. Lessons and materials are read-only through module and course tree endpoints.
- Staff management does not have dedicated endpoints. Staff users are handled through roles and permissions only.

## UI stack

- Angular (latest stable), TypeScript, RxJS
- Styling: Angular Material or Tailwind (choose one and keep consistent)
- HTTP: HttpClient with an auth interceptor
- Forms: ReactiveFormsModule

## Project setup

1. Create a new Angular app
   - ng new edufine-ui --routing --style=scss
2. Add Material (optional)
   - ng add @angular/material
3. Environment configuration
   - src/environments/environment.ts
     - apiBaseUrl: "http://localhost:8080"

## High level architecture

- core/
  - auth/ (auth service, token storage, guards, role helpers)
  - interceptors/ (auth interceptor, error interceptor)
  - layout/ (shell, navigation, role-based menu)
- shared/
  - ui components (tables, dialogs, empty states)
  - pipes (date, role label, status)
- features/
  - auth/
  - courses/
  - modules/
  - departments/
  - students/

## Routing map

Public routes
- /login
- /register (generic user registration)
- /register/student
- /courses (course tree browsing)
- /courses/:id
- /departments
- /departments/:id

Protected routes
- /modules
- /modules/:id
- /modules/complete
- /students
- /students/:id
- /students/filters
- /departments/manage (create/update)

Admin routes (guarded by role)
- /modules/manage
- /departments/manage
- /students/manage

## Auth and security

Token storage
- Store token and user info in localStorage or sessionStorage
- Prefer sessionStorage for shared device safety

Interceptor behavior
- Add Authorization header if token exists
- On 401, redirect to /login

Role guard
- Use route data.roles: ["LIC", "SUPER_ADMIN"] etc
- Allow SUPER_ADMIN to access everything

## API mapping by feature

Auth
- POST /api/auth/login
  - Request: { "username": string, "password": string }
  - Response: { token, type, username, role, permissions }
- POST /api/auth/register
  - Request: User entity
  - Response: created user
- POST /api/auth/register/student
  - Request: StudentRequestDto
  - Response: StudentResponseDto

Courses (read-only)
- GET /api/courses/tree
  - Response: CourseResponseDto[] with modules and semesters
- GET /api/courses/tree/{id}
  - Response: CourseResponseDto
- POST /api/courses (LECTURER only, stub)
- PUT /api/courses/{id} (LECTURER only, stub)
- DELETE /api/courses/{id} (LECTURER only, stub)

Modules
- GET /api/modules
- GET /api/modules/with-course
- GET /api/modules/complete
- GET /api/modules/{id}
- GET /api/modules/code/{code}
- GET /api/modules/course/{courseId}
- POST /api/modules (LIC, SUPER_ADMIN)
- PUT /api/modules/{id} (LIC, SUPER_ADMIN)
- DELETE /api/modules/{id} (LIC, SUPER_ADMIN)
- POST /api/modules/{id}/activate (LIC, SUPER_ADMIN)
- POST /api/modules/{id}/deactivate (LIC, SUPER_ADMIN)

Departments
- GET /api/departments
- GET /api/departments/active
- GET /api/departments/{id}
- GET /api/departments/name/{name}
- POST /api/departments (LIC, SUPER_ADMIN)
- PUT /api/departments/{id} (LIC, SUPER_ADMIN)
- DELETE /api/departments/{id} (LIC, SUPER_ADMIN)
- POST /api/departments/{id}/activate (LIC, SUPER_ADMIN)
- POST /api/departments/{id}/deactivate (LIC, SUPER_ADMIN)

Students
- GET /api/students (STAFF, LECTURER, LIC, SUPER_ADMIN)
- GET /api/students/active
- GET /api/students/{id}
- GET /api/students/user/{userId}
- GET /api/students/enrollment-status/{status}
- GET /api/students/grade/{grade}
- PUT /api/students/{id}
- DELETE /api/students/{id}
- POST /api/students/{id}/activate
- POST /api/students/{id}/deactivate

## Data model notes

StudentRequestDto
- username, email, password, firstName, lastName
- studentId, dateOfBirth, address, phoneNumber
- emergencyContact (optional)
- enrollmentStatus (ACTIVE, INACTIVE, GRADUATED, SUSPENDED)
- enrollmentDate, currentGrade

ModuleRequestDto
- courseId, code, name
- semester (positive integer), credits (positive integer)
- lecturerId (optional)

DepartmentRequestDto
- name, description (optional), headId (optional)

CourseResponseDto
- modules: ModuleWithLessonsDto[] (lessons are read-only)
- semesters: SemesterModulesDto[] (grouped by semester)

## Screen-by-screen specification

Login
- Fields: username, password
- On success: store token, role, permissions
- Route to role-specific dashboard

Register (generic)
- Fields: username, email, password, firstName, lastName, role
- Use this only for staff roles if needed

Register student
- Fields follow StudentRequestDto
- Include date pickers for dateOfBirth and enrollmentDate
- Provide enum select for enrollmentStatus

Courses list (public)
- Tree view of courses -> semesters -> modules -> lessons
- Provide search by course name or code on client-side
- Detail panel for selected course

Course detail (public)
- Show course metadata and semester breakdown
- Modules list with lesson counts and lesson preview

Modules list (protected)
- Table view with filters: course, semester, active
- For LIC or SUPER_ADMIN show create/edit actions
- Module detail dialog uses /api/modules/{id}

Module detail
- Show course info, lessons, materials if using /api/modules/complete
- Material list links to resourceUrl

Departments list (public)
- Table or card view with active toggle indicator
- For LIC or SUPER_ADMIN show manage button

Department manage
- Create/update department with name, description, headId
- Activate/deactivate actions with confirm dialog

Students list (protected)
- Table with columns: name, studentId, grade, status, active
- Filters: grade, enrollmentStatus, active
- Bulk actions optional (activate/deactivate)

Student detail
- Read-only profile view with edit action
- Update uses StudentRequestDto without changing username or password unless required

## Wireframe notes

Global layout
- Left navigation for protected sections
- Top bar with user role and logout
- Public sections only show courses and departments

Login
- Centered card with simple form
- Inline error states for required fields

Courses
- Split layout: tree on the left, detail on the right
- Use expandable nodes for semesters and modules

Modules
- List with filter row and actions on the right
- Detail panel shows lessons and materials if present

Departments
- Grid of cards with department name and headId
- Active badge and manage action for admin roles

Students
- Table with sticky header
- Detail drawer on row click

## UX and validation

- Use reactive forms with server-side validation fallback
- Display API error messages and map 400 errors to field hints
- Empty state messaging for no records
- Confirmation dialogs for delete and activate/deactivate

## Testing checklist

- Login and token persistence
- Role-based route access
- All list pages render with empty data
- Filter combinations do not break
- 401 handling redirects to login

## Known backend limitations

- Course create/update/delete are stubbed and should be hidden or labeled as disabled until implemented.
- Lesson and material management is read-only; no create/update/delete endpoints are available.
- Staff management endpoints do not exist; roles are assigned via user registration or back office tooling.
