package com.trainingpoc.fileupload.repository;

import com.trainingpoc.fileupload.entity.File;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface FileRepository extends MongoRepository<File, String> {
    File findByUuid(UUID id);
}
