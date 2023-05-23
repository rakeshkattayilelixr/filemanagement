package com.trainingpoc.fileupload.controller;

import com.trainingpoc.fileupload.exceptionhandle.FileNotFoundException;
import com.trainingpoc.fileupload.exceptionhandle.InvalidInputException;
import com.trainingpoc.fileupload.entity.CommonResponse;
import com.trainingpoc.fileupload.entity.File;
import com.trainingpoc.fileupload.service.FileManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileManagementController {
    @Autowired
    FileManagementService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<CommonResponse> fileUpload(@RequestParam MultipartFile fileInput, @RequestParam String userName) throws InvalidInputException {
        File file=fileStorageService.uploadFile(fileInput, userName);
        return new ResponseEntity(new CommonResponse(HttpStatus.OK.value(), "SUCCESS",file), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse> viewFileInfoById(@PathVariable String id) throws InvalidInputException, FileNotFoundException {
        File file = fileStorageService.findFileById(id);
        return new ResponseEntity(new CommonResponse(HttpStatus.OK.value(), "SUCCESS",file), HttpStatus.OK);
    }
}

