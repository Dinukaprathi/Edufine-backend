package com.edufine.backend.repository;

import com.edufine.backend.entity.Module;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModuleRepository extends MongoRepository<Module, ObjectId> {
    List<Module> findByCourseId(String courseId);
    Optional<Module> findByCode(String code);
}
