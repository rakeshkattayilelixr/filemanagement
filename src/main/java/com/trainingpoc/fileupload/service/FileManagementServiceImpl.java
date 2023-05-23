package com.trainingpoc.fileupload.service;

import com.trainingpoc.fileupload.exceptionhandle.FileNotFoundException;
import com.trainingpoc.fileupload.exceptionhandle.InvalidInputException;
import com.trainingpoc.fileupload.entity.File;
import com.trainingpoc.fileupload.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.UUID;

@Service
public class FileManagementServiceImpl implements FileManagementService {

    @Autowired
    FileRepository fileRepository;
    private static final String BASE_PATH = "C:\\Users\\Rakesh.Kattayil\\Documents\\Uploads\\";
    private static final String TEXT_FILE_EXT = ".txt";

    @Value("${message.error.invalid.extension}")
    private String invalidExtensionMsg;

    @Value("${message.error.filenotfound}")
    private String fileNotFoundMsg;

    @Value("${message.error.invalid.file}")
    private String invalidFileMsg;

    @Value("${message.error.invalid.username}")
    private String invalidUsernameMsg;

    @Value("${message.error.invalid.id}")
    private String invalidFileIdMsg;
    @Override
    public File uploadFile(MultipartFile file, String userName) throws InvalidInputException {
        //Validate file and username
        validateUsername(userName);
        validateFile(file);

        //Upload file to local repository
        Path uploadPath = Paths.get(BASE_PATH + file.getOriginalFilename());
        try {
            Files.copy(file.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioException) {
            throw new InvalidInputException(invalidFileMsg);
        }
        //saving file details to db
        return fileRepository.save(maToFile(file.getOriginalFilename(), userName, UUID.randomUUID()));
    }

    @Override
    public File findFileById(String id) throws InvalidInputException, FileNotFoundException {
        UUID uuid = validateFileId(id);
        File file = fileRepository.findByUuid(uuid);
        if (file == null) {
            throw new FileNotFoundException(fileNotFoundMsg);
        }
        return file;
    }

    private UUID validateFileId(String id) throws InvalidInputException {
        UUID uuid = null;
        try {
            //Checking file id in UUID format
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException exception) {
            throw new InvalidInputException(invalidFileIdMsg);
        }
        return uuid;
    }

    private void validateUsername(String userName) throws InvalidInputException {
        if (!StringUtils.hasText(userName)) {
            throw new InvalidInputException(invalidUsernameMsg);
        }
    }

    //Getting file details from input to save
    public File maToFile(String fileName, String userName, UUID uuid) {
        File file = new File();
        file.setFileName(fileName);
        file.setUserName(userName);
        file.setUuid(uuid);
        file.setUploadTime(new Date());
        return file;
    }

    public void validateFile(MultipartFile file) throws InvalidInputException {
        //checking file has content
        if (file == null || file.isEmpty()) {
            throw new InvalidInputException(invalidFileMsg);
        }

        //only .txt files are valid to upload
        String fileName = file.getOriginalFilename();
        if (!fileName.endsWith(TEXT_FILE_EXT)) {
            throw new InvalidInputException(String.format(invalidExtensionMsg,fileName));
        }
    }
}
