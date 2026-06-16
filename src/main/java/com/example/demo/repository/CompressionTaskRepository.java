package com.example.demo.repository;

import com.example.demo.models.Compression;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompressionTaskRepository extends MongoRepository<Compression, String> {
}