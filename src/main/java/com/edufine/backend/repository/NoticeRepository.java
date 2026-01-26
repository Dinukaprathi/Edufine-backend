package com.edufine.backend.repository;

import com.edufine.backend.entity.Notice;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends MongoRepository<Notice, ObjectId> {
    List<Notice> findByActive(boolean active);
}
