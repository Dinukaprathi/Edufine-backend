package com.edufine.backend.config;

import com.edufine.backend.entity.*;
import com.edufine.backend.entity.Module;
import com.edufine.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if any users already exist to avoid duplicates
        if (studentRepository.count() > 0 || staffRepository.count() > 0 || courseRepository.count() > 0) {
            System.out.println("Database already seeded. Skipping...");
            return;
        }

        System.out.println("Seeding database with sample data for all collections...");

        // Seed Roles
        seedRoles();

        // Seed Courses
        List<Course> courses = seedCourses();

        // Seed Users (Staff)
        User instructorUser = createStaff(
            "john.instructor", "john.instructor@school.com", "Instructor@123",
            "John", "Smith", "STF2024001",
            LocalDate.of(1990, 5, 20), "456 Oak Ave, City, State",
            "0755035808", "INSTRUCTOR", "Computing Department",
            LocalDate.of(2020, 1, 15), "Master's in Computer Science"
        );

        User lecturerUser = createStaff(
            "maria.lecturer", "maria.lecturer@school.com", "Lecturer@123",
            "Maria", "Johnson", "STF2024002",
            LocalDate.of(1992, 8, 10), "789 Pine Rd, City, State",
            "0755035809", "LECTURER", "Computing Department",
            LocalDate.of(2021, 6, 1), "Ph.D in Software Engineering"
        );

        createStaff(
            "admin.user", "admin@school.com", "Admin@123",
            "Admin", "User", "STF2024003",
            LocalDate.of(1988, 3, 25), "321 Main St, City, State",
            "0755035810", "SUPER_ADMIN", "Administration",
            LocalDate.of(2019, 1, 1), "Bachelor's in Education"
        );

        createStaff(
            "staff.user", "staff@school.com", "Staff@123",
            "Jane", "Davis", "STF2024004",
            LocalDate.of(1995, 6, 14), "567 Elm St, City, State",
            "0755035811", "STAFF", "Student Services",
            LocalDate.of(2022, 3, 10), "Bachelor's in Business Administration"
        );

        createStaff(
            "lic.user", "lic@school.com", "Lic@123",
            "Robert", "Williams", "STF2024005",
            LocalDate.of(1985, 11, 30), "890 Cedar Ln, City, State",
            "0755035812", "LIC", "Computing Department",
            LocalDate.of(2018, 8, 20), "Ph.D in Educational Leadership"
        );

        // Seed Students
        Student student1 = createStudent(
            "Dinuka@1", "dinukatharana13@gmail.com", "Dinukamash@1",
            "Dinuka", "Prathiraja", "STU2024001",
            LocalDate.of(2002, 3, 15), "123 Main St, City, State",
            "0755035806", "0755035807",
            "ACTIVE", LocalDate.of(2024, 9, 1), "10th Grade"
        );

        // Seed Modules for courses
        List<Module> modules = seedModules(courses, lecturerUser);

        // Update courses with module IDs
        updateCoursesWithModuleIds(courses, modules);

        // Seed Lessons for modules
        List<Lesson> lessons = seedLessons(modules, lecturerUser);

        // Seed Materials for lessons
        seedMaterials(lessons, lecturerUser);

        // Seed Course Registrations
        seedRegistrations(student1, courses);

        // Seed Notices
        seedNotices();

        System.out.println("Database seeding completed successfully!");
    }

    private User createStaff(String username, String email, String password,
                            String firstName, String lastName, String staffId,
                            LocalDate dateOfBirth, String address,
                            String phoneNumber, String designation, String department,
                            LocalDate dateOfJoining, String qualifications) {
        
        // Create User
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(UserRole.valueOf(designation));
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        User savedUser = userRepository.save(user);
        System.out.println("Created user: " + username);

        // Create Staff
        Staff staff = new Staff();
        staff.setUserId(savedUser.getId().toHexString());
        staff.setStaffId(staffId);
        staff.setUsername(username);
        staff.setFirstName(firstName);
        staff.setLastName(lastName);
        staff.setDesignation(designation);
        staff.setDepartment(department);
        staff.setDateOfBirth(dateOfBirth);
        staff.setAddress(address);
        staff.setPhoneNumber(phoneNumber);
        staff.setDateOfJoining(dateOfJoining);
        staff.setQualifications(qualifications);
        staff.setActive(true);
        staff.setCreatedAt(LocalDateTime.now());
        staff.setUpdatedAt(LocalDateTime.now());
        
        staffRepository.save(staff);
        System.out.println("Created staff: " + staffId + " - " + firstName + " " + lastName + " (" + designation + ")");
        
        return savedUser;
    }

    private void createNotice(String title, String content) {
        Notice notice = new Notice(title, content);
        notice.setActive(true);
        notice.setCreatedAt(LocalDateTime.now());
        notice.setUpdatedAt(LocalDateTime.now());
        noticeRepository.save(notice);
        System.out.println("Created notice: " + title);
    }

    private Student createStudent(String username, String email, String password,
                              String firstName, String lastName, String studentId,
                              LocalDate dateOfBirth, String address,
                              String phoneNumber, String emergencyContact,
                              String enrollmentStatus, LocalDate enrollmentDate,
                              String currentGrade) {
        
        // Create User
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(UserRole.STUDENT);
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        User savedUser = userRepository.save(user);
        System.out.println("Created user: " + username);

        // Create Student
        Student student = new Student();
        student.setUserId(savedUser.getId().toHexString());
        student.setStudentId(studentId);
        student.setUsername(username);
        student.setPassword(passwordEncoder.encode(password));
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setDateOfBirth(dateOfBirth);
        student.setAddress(address);
        student.setPhoneNumber(phoneNumber);
        student.setEmergencyContact(emergencyContact);
        student.setEnrollmentStatus(enrollmentStatus);
        student.setEnrollmentDate(enrollmentDate);
        student.setCurrentGrade(currentGrade);
        student.setGpa(0.0);
        student.setActive(true);
        student.setCreatedAt(LocalDateTime.now());
        student.setUpdatedAt(LocalDateTime.now());
        
        studentRepository.save(student);
        System.out.println("Created student: " + studentId + " - " + firstName + " " + lastName);
        return student;
    }



    private void seedRoles() {
        List<Role> roles = Arrays.asList(
            new Role("STUDENT", 1, Arrays.asList("view_courses", "view_modules", "view_lessons", "submit_assignments")),
            new Role("STAFF", 2, Arrays.asList("view_courses", "manage_students", "view_reports")),
            new Role("INSTRUCTOR", 3, Arrays.asList("create_content", "manage_modules", "grade_assignments", "manage_classes")),
            new Role("LECTURER", 4, Arrays.asList("create_content", "manage_modules", "create_lessons", "create_materials", "manage_classes")),
            new Role("LIC", 5, Arrays.asList("manage_departments", "manage_lecturers", "approve_content", "manage_intakes")),
            new Role("SUPER_ADMIN", 6, Arrays.asList("manage_all", "system_settings", "user_management", "audit_logs"))
        );
        roleRepository.saveAll(roles);
        System.out.println("Created " + roles.size() + " roles");
    }

    private List<Course> seedCourses() {
        List<Course> courses = Arrays.asList(
            new Course("CS", "Computer Science", 4, "A comprehensive program in computer science covering algorithms, databases, and software engineering", 120),
            new Course("SE", "Software Engineering", 4, "Focused on software development methodologies, design patterns, and project management", 120),
            new Course("IT", "Information Technology", 4, "IT infrastructure, networking, systems administration, and cybersecurity", 120),
            new Course("DS", "Data Science", 3, "Data analysis, machine learning, statistics, and big data technologies", 90)
        );
        courseRepository.saveAll(courses);
        System.out.println("Created " + courses.size() + " courses");
        return courses;
    }

    private List<Module> seedModules(List<Course> courses, User lecturerUser) {
        List<Module> modules = Arrays.asList(
            // CS Course Modules - Year 1
            new Module(courses.get(0).getId().toHexString(), "CS101", "Introduction to Programming", 1, 1, 3, lecturerUser.getId().toHexString()),
            new Module(courses.get(0).getId().toHexString(), "CS102", "Data Structures", 1, 1, 3, lecturerUser.getId().toHexString()),
            new Module(courses.get(0).getId().toHexString(), "CS103", "Web Development Basics", 1, 2, 3, lecturerUser.getId().toHexString()),
            new Module(courses.get(0).getId().toHexString(), "CS104", "Database Fundamentals", 1, 2, 4, lecturerUser.getId().toHexString()),

            // CS Course Modules - Year 2
            new Module(courses.get(0).getId().toHexString(), "CS201", "Algorithms", 2, 1, 4, lecturerUser.getId().toHexString()),
            new Module(courses.get(0).getId().toHexString(), "CS202", "Object-Oriented Programming", 2, 1, 4, lecturerUser.getId().toHexString()),
            new Module(courses.get(0).getId().toHexString(), "CS203", "Software Engineering", 2, 2, 4, lecturerUser.getId().toHexString()),
            new Module(courses.get(0).getId().toHexString(), "CS204", "Database Management", 2, 2, 4, lecturerUser.getId().toHexString()),

            // SE Course Modules - Year 1
            new Module(courses.get(1).getId().toHexString(), "SE101", "Software Development Lifecycle", 1, 1, 3, lecturerUser.getId().toHexString()),
            new Module(courses.get(1).getId().toHexString(), "SE102", "Object-Oriented Programming", 1, 1, 4, lecturerUser.getId().toHexString()),
            new Module(courses.get(1).getId().toHexString(), "SE103", "Requirements Engineering", 1, 2, 3, lecturerUser.getId().toHexString()),
            new Module(courses.get(1).getId().toHexString(), "SE104", "Software Testing", 1, 2, 4, lecturerUser.getId().toHexString()),

            // SE Course Modules - Year 2
            new Module(courses.get(1).getId().toHexString(), "SE201", "Design Patterns", 2, 1, 4, lecturerUser.getId().toHexString()),
            new Module(courses.get(1).getId().toHexString(), "SE202", "Software Architecture", 2, 1, 4, lecturerUser.getId().toHexString()),
            new Module(courses.get(1).getId().toHexString(), "SE203", "Project Management", 2, 2, 3, lecturerUser.getId().toHexString()),
            new Module(courses.get(1).getId().toHexString(), "SE204", "Software Maintenance", 2, 2, 3, lecturerUser.getId().toHexString()),

            // IT Course Modules - Year 1
            new Module(courses.get(2).getId().toHexString(), "IT101", "Networking Fundamentals", 1, 1, 3, lecturerUser.getId().toHexString()),
            new Module(courses.get(2).getId().toHexString(), "IT102", "Systems Administration", 1, 1, 3, lecturerUser.getId().toHexString()),
            new Module(courses.get(2).getId().toHexString(), "IT103", "Operating Systems", 1, 2, 4, lecturerUser.getId().toHexString()),
            new Module(courses.get(2).getId().toHexString(), "IT104", "Server Management", 1, 2, 3, lecturerUser.getId().toHexString()),

            // IT Course Modules - Year 2
            new Module(courses.get(2).getId().toHexString(), "IT201", "Cybersecurity Basics", 2, 1, 4, lecturerUser.getId().toHexString()),
            new Module(courses.get(2).getId().toHexString(), "IT202", "Network Security", 2, 1, 4, lecturerUser.getId().toHexString()),
            new Module(courses.get(2).getId().toHexString(), "IT203", "Cloud Computing", 2, 2, 3, lecturerUser.getId().toHexString()),
            new Module(courses.get(2).getId().toHexString(), "IT204", "Advanced Networking", 2, 2, 4, lecturerUser.getId().toHexString()),

            // DS Course Modules - Year 1
            new Module(courses.get(3).getId().toHexString(), "DS101", "Statistics for Data Science", 1, 1, 3, lecturerUser.getId().toHexString()),
            new Module(courses.get(3).getId().toHexString(), "DS102", "Python for Data Science", 1, 1, 4, lecturerUser.getId().toHexString()),
            new Module(courses.get(3).getId().toHexString(), "DS103", "Data Analysis", 1, 2, 3, lecturerUser.getId().toHexString()),
            new Module(courses.get(3).getId().toHexString(), "DS104", "Data Visualization", 1, 2, 3, lecturerUser.getId().toHexString())
        );
        moduleRepository.saveAll(modules);
        System.out.println("Created " + modules.size() + " modules");
        return modules;
    }

    private List<Lesson> seedLessons(List<Module> modules, User createdByUser) {
        List<Lesson> lessons = Arrays.asList(
            // CS101 Lessons
            new Lesson(modules.get(0).getId().toHexString(), "Variables and Data Types", "Introduction to variables, primitive types, and type conversion", 1),
            new Lesson(modules.get(0).getId().toHexString(), "Control Flow", "If-else statements, loops, and switch cases", 2),
            new Lesson(modules.get(0).getId().toHexString(), "Functions and Methods", "Function definition, parameters, return types, and scope", 3),

            // CS102 Lessons
            new Lesson(modules.get(1).getId().toHexString(), "Arrays and Lists", "Array operations, list interfaces, and dynamic arrays", 1),
            new Lesson(modules.get(1).getId().toHexString(), "Linked Lists", "Node structures, insertion, deletion, and traversal", 2),
            new Lesson(modules.get(1).getId().toHexString(), "Stacks and Queues", "LIFO and FIFO structures with applications", 3),

            // SE101 Lessons
            new Lesson(modules.get(4).getId().toHexString(), "SDLC Models", "Waterfall, Agile, and other development methodologies", 1),
            new Lesson(modules.get(4).getId().toHexString(), "Requirements Analysis", "Gathering and documenting software requirements", 2),

            // IT101 Lessons
            new Lesson(modules.get(7).getId().toHexString(), "OSI Model", "Seven layers of the Open Systems Interconnection model", 1),
            new Lesson(modules.get(7).getId().toHexString(), "TCP/IP Protocols", "Internet protocols and communication", 2),

            // DS101 Lessons
            new Lesson(modules.get(9).getId().toHexString(), "Descriptive Statistics", "Mean, median, mode, and distribution analysis", 1),
            new Lesson(modules.get(9).getId().toHexString(), "Probability Distributions", "Normal, binomial, and Poisson distributions", 2)
        );
        for (Lesson lesson : lessons) {
            lesson.setCreatedBy(createdByUser.getId().toHexString());
        }
        lessonRepository.saveAll(lessons);
        System.out.println("Created " + lessons.size() + " lessons");
        return lessons;
    }

    private void seedMaterials(List<Lesson> lessons, User uploadedByUser) {
        List<Material> materials = Arrays.asList(
            // Materials for Variables and Data Types lesson
            new Material(lessons.get(0).getId().toHexString(), "Introduction to Variables", "PDF", "https://example.com/variables.pdf"),
            new Material(lessons.get(0).getId().toHexString(), "Variable Tutorial Video", "VIDEO", "https://example.com/variables-video.mp4"),
            new Material(lessons.get(0).getId().toHexString(), "Practice Exercises", "LINK", "https://example.com/exercises/variables"),

            // Materials for Control Flow lesson
            new Material(lessons.get(1).getId().toHexString(), "Control Flow Guide", "PDF", "https://example.com/control-flow.pdf"),
            new Material(lessons.get(1).getId().toHexString(), "Loop Examples", "VIDEO", "https://example.com/loops-video.mp4"),

            // Materials for Arrays and Lists lesson
            new Material(lessons.get(3).getId().toHexString(), "Arrays Overview", "PDF", "https://example.com/arrays.pdf"),
            new Material(lessons.get(3).getId().toHexString(), "Array Visualization", "VIDEO", "https://example.com/arrays-video.mp4"),

            // Materials for SDLC Models lesson
            new Material(lessons.get(6).getId().toHexString(), "SDLC Models Comparison", "PDF", "https://example.com/sdlc-models.pdf"),
            new Material(lessons.get(6).getId().toHexString(), "Agile Methodology", "VIDEO", "https://example.com/agile-video.mp4"),

            // Materials for OSI Model lesson
            new Material(lessons.get(8).getId().toHexString(), "OSI Model Layers", "PDF", "https://example.com/osi-model.pdf"),
            new Material(lessons.get(8).getId().toHexString(), "OSI Model Animation", "VIDEO", "https://example.com/osi-animation.mp4"),

            // Materials for Descriptive Statistics lesson
            new Material(lessons.get(10).getId().toHexString(), "Statistics Handbook", "PDF", "https://example.com/statistics.pdf"),
            new Material(lessons.get(10).getId().toHexString(), "Statistical Analysis Tools", "LINK", "https://example.com/stat-tools")
        );
        for (Material material : materials) {
            material.setUploadedBy(uploadedByUser.getId().toHexString());
            material.setFileSize(Math.random() > 0.5 ? 2048000L : 1024000L);
        }
        materialRepository.saveAll(materials);
        System.out.println("Created " + materials.size() + " materials");
    }

    private void seedRegistrations(Student student, List<Course> courses) {
        List<Registration> registrations = Arrays.asList(
            new Registration(student.getId().toHexString(), courses.get(0).getId().toHexString(), "2024-2025", "ACTIVE"),
            new Registration(student.getId().toHexString(), courses.get(1).getId().toHexString(), "2024-2025", "ACTIVE"),
            new Registration(student.getId().toHexString(), courses.get(2).getId().toHexString(), "2024-2025", "ACTIVE")
        );
        registrationRepository.saveAll(registrations);
        System.out.println("Created " + registrations.size() + " course registrations for student: " + student.getStudentId());
    }

    private void seedNotices() {
        List<Notice> notices = Arrays.asList(
            createNoticeEntity("Welcome to EduFine", "The portal is now live. Please log in and verify your details.", "ALL", null),
            createNoticeEntity("Semester Start", "The new semester starts on Feb 10. Check your timetable.", "ALL", null),
            createNoticeEntity("Maintenance Window", "System maintenance on Jan 30, 10pm-12am. Expect downtime.", "ALL", null),
            createNoticeEntity("Assignment Deadline", "CS101 Assignment 1 is due on Feb 15, 2025.", "STUDENTS", null),
            createNoticeEntity("Module Update", "CS102 (Data Structures) module content has been updated.", "STUDENTS", null),
            createNoticeEntity("Lecture Postponement", "The SE201 lecture scheduled for Feb 20 has been rescheduled to Feb 22.", "STUDENTS", null)
        );
        noticeRepository.saveAll(notices);
        System.out.println("Created " + notices.size() + " notices");
    }

    private Notice createNoticeEntity(String title, String content, String targetAudience, String courseId) {
        Notice notice = new Notice(title, content);
        notice.setVisibleTo(targetAudience);
        notice.setActive(true);
        notice.setCreatedAt(LocalDateTime.now());
        notice.setUpdatedAt(LocalDateTime.now());
        return notice;
    }

    private void updateCoursesWithModuleIds(List<Course> courses, List<Module> modules) {
        for (Course course : courses) {
            String courseId = course.getId().toHexString();
            List<String> moduleIds = new java.util.ArrayList<>();
            for (Module module : modules) {
                if (module.getCourseId().equals(courseId)) {
                    moduleIds.add(module.getId().toHexString());
                }
            }
            course.setModuleIds(moduleIds);
            courseRepository.save(course);
        }
        System.out.println("Updated courses with module IDs");
    }
}
