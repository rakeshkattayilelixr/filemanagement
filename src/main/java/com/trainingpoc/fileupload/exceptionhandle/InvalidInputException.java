package com.trainingpoc.fileupload.exceptionhandle;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InvalidInputException extends Exception{
    private String errMsg;
}
