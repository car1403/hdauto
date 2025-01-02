package com.hd.common.exception;

import lombok.Getter;

@Getter
public class IdNotFoundException extends RuntimeException{

    private ErrorCode errorCode;

    public IdNotFoundException(String message, ErrorCode errorCode){
        super(message);
        //this.errorCode.se
        this.errorCode = errorCode;
    }
}