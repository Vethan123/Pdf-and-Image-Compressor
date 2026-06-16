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

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFileName() {
        return fileName;
    }

    public String getStatus() {
        return status;
    }

    public void setOriginalSize(Long originalSize) {
        this.originalSize = originalSize;
    }

    public String getId() {
        return id;
    }

    public void setCompressedSize(Long compressedSize) {
        this.compressedSize = compressedSize;
    }

    public void setCompressedFilePath(String compressedFilePath) {
        this.compressedFilePath = compressedFilePath;
    }

    private LocalDateTime createdAt = LocalDateTime.now();
}
