package com.example.demo.Service;

import com.example.demo.models.Compression;
import com.example.demo.repository.CompressionTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.io.File;

@Service
public class CompressionService {

    @Autowired private CompressionEngine engine;
    @Autowired private CompressionTaskRepository repository;

    private static final String STORAGE_DIR = System.getProperty("user.home") + "/compressed_files/";

    @Async("compressionExecutor")
    public void processCompressionAsync(String taskId, String tempInputPath, String fileType) {
        Compression task = repository.findById(taskId).orElseThrow();
        task.setStatus("PROCESSING");
        repository.save(task);

        try {
            File inputFile = new File(tempInputPath);
            File dir = new File(STORAGE_DIR);
            if (!dir.exists()) dir.mkdirs();

            File outputFile = new File(STORAGE_DIR + "compressed_" + task.getFileName());

            if ("PDF".equalsIgnoreCase(fileType)) {
                engine.compressPdf(inputFile, outputFile);
            } else {
                engine.compressImage(inputFile, outputFile);
            }

            // Clean up temporary upload file, update DB tracking data
            inputFile.delete();
            task.setStatus("SUCCESS");
            task.setCompressedSize(outputFile.length());
            task.setCompressedFilePath(outputFile.getAbsolutePath());
            repository.save(task);

        } catch (Exception e) {
            task.setStatus("FAILED");
            repository.save(task);
            e.printStackTrace();
        }
    }
}