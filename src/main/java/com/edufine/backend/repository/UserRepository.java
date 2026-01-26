package com.edufine.backend.repository;

import com.edufine.backend.entity.User;
import com.edufine.backend.entity.UserRole;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    long countByRoleAndActive(UserRole role, boolean active);
    List<User> findByRoleAndActive(UserRole role, boolean active);
    List<User> findByRole(UserRole role);
}