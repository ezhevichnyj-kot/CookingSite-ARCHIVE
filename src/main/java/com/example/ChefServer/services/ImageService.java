package com.example.ChefServer.services;

import org.springframework.web.multipart.*;

import java.io.*;
import java.nio.file.*;

public class ImageService {
    public class FileUploadUtil {

        public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = multipartFile.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(multipartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException exception) {
                throw new IOException("Could not save image file: " + fileName, exception);
            }
        }
    }
}
