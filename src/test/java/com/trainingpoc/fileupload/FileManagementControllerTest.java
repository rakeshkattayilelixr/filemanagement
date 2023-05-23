package com.trainingpoc.fileupload;

import com.trainingpoc.fileupload.controller.FileManagementController;
import com.trainingpoc.fileupload.exceptionhandle.FileNotFoundException;
import com.trainingpoc.fileupload.exceptionhandle.InvalidInputException;
import com.trainingpoc.fileupload.entity.File;
import com.trainingpoc.fileupload.service.FileManagementServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;
import java.util.UUID;

@WebMvcTest(controllers = FileManagementController.class)
public class FileManagementControllerTest {
    private static final String FILE_ID = "36b8f84d-df4e-4d49-b662-bcde71a8764f";
    private static final String USER_NAME = "rkattayil";
    private static final String FILE_NAME = "test.txt";
    private static final String INVALID_FILE = "File is empty! Please choose a file";
    private static final String CONTENT_TYPE = "text/plain";
    private static final String UPLOAD_URL = "/file/upload";
    private static final String GET_FILE_URI = "/file/{id}";
    private static final String FILE_CONTENT = "test";
    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILED";
    private static final String FILE_NOT_FOUND="File id not found";
    private static final String INVALID_USERNAME = "Invalid username";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    FileManagementServiceImpl fileStorageService;

    @Test
    void getFileUsingFileId() throws Exception {
        UUID fileId = UUID.fromString(FILE_ID);
        File fileToReturn = new File(fileId, FILE_NAME, USER_NAME, new Date());
        Mockito.when(fileStorageService.findFileById(FILE_ID)).thenReturn(fileToReturn);
        this.mockMvc.perform(MockMvcRequestBuilders.get(GET_FILE_URI, FILE_ID))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode", Matchers.is(HttpStatus.OK.value())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusMsg", Matchers.is(SUCCESS)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.uuid", Matchers.is(FILE_ID)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.fileName", Matchers.is(FILE_NAME)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.userName", Matchers.is(USER_NAME)));
    }

    @Test
    void GetFileWithInvalidInput() throws Exception {
        UUID fileId = UUID.fromString(FILE_ID);
        Mockito.when(fileStorageService.findFileById(FILE_ID)).thenThrow(new InvalidInputException(INVALID_FILE));
        this.mockMvc.perform(MockMvcRequestBuilders.get(GET_FILE_URI, FILE_ID))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode", Matchers.is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusMsg", Matchers.is(FAILURE)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMsg", Matchers.is(INVALID_FILE)));
    }

    @Test
    void GetFileWithFileNotFound() throws Exception {
        UUID fileId = UUID.fromString(FILE_ID);
        Mockito.when(fileStorageService.findFileById(FILE_ID)).thenThrow(new FileNotFoundException(FILE_NOT_FOUND));
        this.mockMvc.perform(MockMvcRequestBuilders.get(GET_FILE_URI, FILE_ID))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode", Matchers.is(HttpStatus.NOT_FOUND.value())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusMsg", Matchers.is(FAILURE)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMsg", Matchers.is(FILE_NOT_FOUND)));
    }

    @Test
    void uploadFileSuccess() throws Exception {
        UUID fileId = UUID.fromString(FILE_ID);
        File fileToReturn = new File(fileId, FILE_NAME, USER_NAME, new Date());
        MockMultipartFile mockMultipartFile = new MockMultipartFile("fileInput", FILE_NAME, CONTENT_TYPE, FILE_CONTENT.getBytes());
        Mockito.when(fileStorageService.uploadFile(mockMultipartFile,USER_NAME)).thenReturn(fileToReturn);
        this.mockMvc.perform(MockMvcRequestBuilders.multipart(UPLOAD_URL)
                        .file(mockMultipartFile)
                        .param("userName", USER_NAME))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode", Matchers.is(HttpStatus.OK.value())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusMsg", Matchers.is(SUCCESS)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.uuid", Matchers.is(FILE_ID)));
    }

    @Test
    void uploadFileInvalidUserName() throws Exception {
        UUID fileId = UUID.fromString(FILE_ID);
        MockMultipartFile mockMultipartFile = new MockMultipartFile("fileInput", FILE_NAME, CONTENT_TYPE, FILE_CONTENT.getBytes());
        Mockito.when(fileStorageService.uploadFile(mockMultipartFile," ")).thenThrow(new InvalidInputException(INVALID_USERNAME));
        this.mockMvc.perform(MockMvcRequestBuilders.multipart(UPLOAD_URL)
                        .file(mockMultipartFile)
                        .param("userName", " "))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode", Matchers.is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusMsg", Matchers.is(FAILURE)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMsg", Matchers.is(INVALID_USERNAME)));
    }

}
