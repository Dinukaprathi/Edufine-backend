package com.edufine.backend.repository;

import com.edufine.backend.entity.Course;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends MongoRepository<Course, ObjectId> {
    Optional<Course> findByCode(String code);
}
