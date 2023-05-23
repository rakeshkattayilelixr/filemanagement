package com.trainingpoc.fileupload.exceptionhandle;

import com.trainingpoc.fileupload.entity.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    //To handle custom validations on input
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity handleInvalidInputException(InvalidInputException exception){
        return new ResponseEntity(new CommonResponse( HttpStatus.BAD_REQUEST.value(),"FAILED",exception.getErrMsg()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity handleFileNotFoundException(FileNotFoundException exception){
        return new ResponseEntity(new CommonResponse( HttpStatus.NOT_FOUND.value(),"FAILED",exception.getErrMsg()),HttpStatus.NOT_FOUND);
    }
}
