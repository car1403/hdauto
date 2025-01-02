package com.hd.common.exception;

import lombok.Getter;

@Getter
public class DataNotFoundException extends RuntimeException{

    private ErrorCode errorCode;

    public DataNotFoundException(String message, ErrorCode errorCode){
        super(message);
        //this.errorCode.se
        this.errorCode = errorCode;
    }
}