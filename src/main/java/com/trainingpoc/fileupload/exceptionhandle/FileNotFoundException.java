package com.trainingpoc.fileupload.exceptionhandle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FileNotFoundException extends Exception{
    private String errMsg;
}
