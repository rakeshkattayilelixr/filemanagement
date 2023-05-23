package com.trainingpoc.fileupload.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse<T> {
    private int statusCode;
    private String statusMsg;
    private String errorMsg;
    private T content;
    public CommonResponse(int statusCode, String statusMsg){
        this.statusCode=statusCode;
        this.statusMsg=statusMsg;
    }

    public CommonResponse(int statusCode, String statusMsg,String errorMsg){
        this.statusCode=statusCode;
        this.statusMsg=statusMsg;
        this.errorMsg=errorMsg;
    }

    public CommonResponse(int statusCode, String statusMsg,T content){
        this.statusCode=statusCode;
        this.statusMsg=statusMsg;
        this.content= content;
    }
}
