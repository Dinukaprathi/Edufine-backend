package com.edufine.backend.repository;

import com.edufine.backend.entity.Department;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends MongoRepository<Department, ObjectId> {

    Optional<Department> findByName(String name);

    List<Department> findByActive(boolean active);

    boolean existsByName(String name);
}
