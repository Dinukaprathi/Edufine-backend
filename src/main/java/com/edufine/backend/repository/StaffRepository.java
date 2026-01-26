package com.edufine.backend.repository;

import com.edufine.backend.entity.Staff;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends MongoRepository<Staff, ObjectId> {
    
    Optional<Staff> findByUserId(String userId);
    
    Optional<Staff> findByStaffId(String staffId);
    
    List<Staff> findByDepartment(String department);
    
    List<Staff> findByDesignation(String designation);
    
    List<Staff> findByActive(boolean active);
    
    boolean existsByUserId(String userId);
    
    boolean existsByStaffId(String staffId);
}
