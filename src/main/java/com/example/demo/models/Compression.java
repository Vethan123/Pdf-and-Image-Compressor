package com.example.demo.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.time.LocalDateTime;

@Document(collection = "compression_tasks")
@Data
public class Compression {
    @Id
    private String id;

    private String fileName;
    private String fileType;
    private String status;
    private Long originalSize;
    private Long compressedSize;
    private String compressedFilePath;
    private LocalDateTime createdAt = LocalDateTime.now();
}
