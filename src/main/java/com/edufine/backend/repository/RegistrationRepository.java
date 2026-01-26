package com.edufine.backend.repository;

import com.edufine.backend.entity.Registration;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationRepository extends MongoRepository<Registration, ObjectId> {
    List<Registration> findByStudentId(String studentId);
    List<Registration> findByCourseId(String courseId);
}
