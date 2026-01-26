package com.edufine.backend.repository;

import com.edufine.backend.entity.Material;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends MongoRepository<Material, ObjectId> {
    List<Material> findByLessonId(String lessonId);
}
