package com.trainingpoc.fileupload.service;


import com.trainingpoc.fileupload.exceptionhandle.FileNotFoundException;
import com.trainingpoc.fileupload.exceptionhandle.InvalidInputException;
import com.trainingpoc.fileupload.entity.File;
import org.springframework.web.multipart.MultipartFile;

public interface FileManagementService {
    File uploadFile(MultipartFile file, String userName) throws InvalidInputException;

    File findFileById(String id) throws InvalidInputException, FileNotFoundException;
}
