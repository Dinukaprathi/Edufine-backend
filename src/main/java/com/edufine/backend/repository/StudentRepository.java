package com.edufine.backend.repository;

import com.edufine.backend.entity.Student;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends MongoRepository<Student, ObjectId> {
    
    Optional<Student> findByStudentId(String studentId);
    
    Optional<Student> findByUserId(String userId);
    
    List<Student> findByEnrollmentStatus(String enrollmentStatus);
    
    List<Student> findByCurrentGrade(String currentGrade);
    
    List<Student> findByActive(boolean active);
    
    boolean existsByStudentId(String studentId);
    
    boolean existsByUserId(String userId);
}
