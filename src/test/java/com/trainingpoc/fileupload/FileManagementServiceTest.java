package com.trainingpoc.fileupload;

import com.trainingpoc.fileupload.exceptionhandle.FileNotFoundException;
import com.trainingpoc.fileupload.exceptionhandle.InvalidInputException;
import com.trainingpoc.fileupload.entity.File;
import com.trainingpoc.fileupload.repository.FileRepository;
import com.trainingpoc.fileupload.service.FileManagementServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class FileManagementServiceTest {
    private static final String FILE_CONTENT = "test";
    private static final String USER_NAME = "rkattayil";
    private static final String FILE_NAME = "test.txt";
    private static final String FILE_ID = "36b8f84d-df4e-4d49-b662-bcde71a8764f";
    private static final String FILE_DIR_PATH = "C:\\Users\\Rakesh.Kattayil\\Documents\\Uploads\\";
    @Mock
    FileRepository fileRepository;
    @InjectMocks
    FileManagementServiceImpl fileStorageService;

    @AfterEach
    public void cleanup() {
        try {
            Files.deleteIfExists(Path.of(FILE_DIR_PATH + FILE_NAME));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getFileByIdSuccess() throws InvalidInputException, FileNotFoundException {
        UUID fileId = UUID.fromString(FILE_ID);
        File fileToReturn = new File(fileId, FILE_NAME, USER_NAME, new Date());
        Mockito.when(fileRepository.findByUuid(fileId)).thenReturn(fileToReturn);
        File file = fileStorageService.findFileById(FILE_ID);
        Assertions.assertEquals(file, fileToReturn);
    }

    @Test
    void GetFileNotFound() {
        FileNotFoundException exception = Assertions.assertThrows(FileNotFoundException.class, () -> fileStorageService.findFileById(FILE_ID));
        Assertions.assertEquals(exception.getErrMsg(), "File id not found");
    }

    @Test
    void GetFileInvalidFileId() {
        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, () -> fileStorageService.findFileById("test-123"));
        Assertions.assertEquals(exception.getErrMsg(), "File id is not in proper format");
    }

    @Test
    void fileUploadSuccess() throws InvalidInputException {
        MockMultipartFile mockMultipartFile = new MockMultipartFile(FILE_NAME, FILE_NAME, MediaType.TEXT_PLAIN_VALUE, FILE_CONTENT.getBytes());
        File savedFile = new File(UUID.fromString(FILE_ID), FILE_NAME, USER_NAME, new Date());
        Mockito.when(fileRepository.save(Mockito.any(File.class))).thenReturn(savedFile);
        File file = fileStorageService.uploadFile(mockMultipartFile, USER_NAME);
        Assertions.assertEquals(file.getUuid(), UUID.fromString(FILE_ID));
        Assertions.assertEquals(Files.exists(Path.of(FILE_DIR_PATH + FILE_NAME)), true);
    }

    @Test
    void fileUploadWithoutUsername() {
        MockMultipartFile mockMultipartFile = new MockMultipartFile(FILE_NAME, FILE_NAME, MediaType.TEXT_PLAIN_VALUE, FILE_CONTENT.getBytes());
        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, () -> fileStorageService.uploadFile(mockMultipartFile, null));
        Assertions.assertEquals(exception.getErrMsg(), "Invalid username");
    }

    @Test
    void fileUploadWithoutFile() {
        MockMultipartFile mockMultipartFile = new MockMultipartFile(FILE_NAME, FILE_NAME, MediaType.TEXT_PLAIN_VALUE, "".getBytes());
        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, () -> fileStorageService.uploadFile(mockMultipartFile, USER_NAME));
        Assertions.assertEquals(exception.getErrMsg(), "File is empty! Please choose a file");
    }

    @Test
    void fileUploadWithInvalidExt() {
        MockMultipartFile mockMultipartFile = new MockMultipartFile(FILE_NAME, "test.json", MediaType.TEXT_PLAIN_VALUE, FILE_CONTENT.getBytes());
        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, () -> fileStorageService.uploadFile(mockMultipartFile, USER_NAME));
        Assertions.assertEquals(exception.getErrMsg(), "test.json is not a .txt file. Please upload .txt file");
    }
}
