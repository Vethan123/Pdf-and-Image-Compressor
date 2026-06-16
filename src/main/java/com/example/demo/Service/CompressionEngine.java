package com.example.demo.Service;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.graphics.image.*;
import org.springframework.stereotype.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Component
public class CompressionEngine {

    public void compressImage(File input, File output) throws IOException {
        Thumbnails.of(input)
                .scale(0.80)         // Scale dimensions to 80%
                .outputQuality(0.70) // Drop visual compression target to 70%
                .toFile(output);
    }

    public void compressPdf(File input, File output) throws IOException {
        try (PDDocument document = Loader.loadPDF(input)) {
            for (PDPage page : document.getPages()) {
                PDResources resources = page.getResources();
                for (COSName name : resources.getXObjectNames()) {
                    if (resources.isImageXObject(name)) {
                        PDImageXObject image = (PDImageXObject) resources.getXObject(name);
                        BufferedImage bufferedImage = image.getImage();

                        // Re-render embedded image streams down to a 50% quality JPEG string
                        PDImageXObject compressedImg = JPEGFactory.createFromImage(document, bufferedImage, 0.5f);
                        resources.put(name, compressedImg);
                    }
                }
            }
            document.save(output);
        }
    }
}