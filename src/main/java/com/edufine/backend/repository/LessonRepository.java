package com.edufine.backend.repository;

import com.edufine.backend.entity.Lesson;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends MongoRepository<Lesson, ObjectId> {
    List<Lesson> findByModuleId(String moduleId);
}
