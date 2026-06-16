package com.example.demo.Controller;

import com.example.demo.Service.CompressionService;
import com.example.demo.models.Compression;
import com.example.demo.repository.CompressionTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
        import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.Map;

@RestController
@RequestMapping("/api/compression")
public class CompressionController {

    @Autowired private CompressionService service;
    @Autowired private CompressionTaskRepository repository;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        String fileType = file.getContentType().contains("pdf") ? "PDF" : "IMAGE";

        String tempDir = System.getProperty("java.io.tmpdir");
        File tempFile = new File(tempDir + File.separator + file.getOriginalFilename());
        file.transferTo(tempFile);

        Compression task = new Compression();
        task.setFileName(file.getOriginalFilename());
        task.setFileType(fileType);
        task.setOriginalSize(file.getSize());
        task.setStatus("PENDING");

        Compression savedTask = repository.save(task);

        service.processCompressionAsync(savedTask.getId(), tempFile.getAbsolutePath(), fileType);

        return ResponseEntity.accepted().body(Map.of(
                "message", "File compression started successfully in MongoDB backend",
                "taskId", savedTask.getId(),
                "status", savedTask.getStatus()
        ));
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<Compression> getStatus(@PathVariable String id) {
        return ResponseEntity.ok(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found")));
    }
}