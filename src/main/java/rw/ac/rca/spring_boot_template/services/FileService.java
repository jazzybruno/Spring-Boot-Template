package rw.ac.rca.spring_boot_template.services;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;

@Component
public interface FileService {

    /**
     * Upload a file.
     *
     * @param file the file to upload
     * @return the generated file name or null if the file is empty
     * @throws IOException if an error occurs during file upload
     */
    String uploadFile(MultipartFile file) throws IOException;

    /**
     * Update an existing file with a new one.
     *
     * @param oldFileName the name of the old file to update
     * @param newFile     the new file to replace the old one
     * @return the generated file name or the old file name if newFile is empty
     * @throws IOException if an error occurs during file upload
     */
    String updateFile(String oldFileName, MultipartFile newFile) throws IOException;

    /**
     * Remove a file from the storage.
     *
     * @param fileName the name of the file to remove
     */
    void deleteFile(String fileName) throws IOException;

    /**
     * Get a file by its name.
     *
     * @param fileName the name of the file to retrieve
     * @return the File object representing the file or null if it doesn't exist
     */
    File getFile(String fileName) throws IOException;
}