package com.hd.common.exception;

import lombok.Getter;

@Getter
public class EmailNotFoundException extends RuntimeException{

    private ErrorCode errorCode;

    public EmailNotFoundException(ErrorCode errorCode){
//        super(messag);
        //this.errorCode.se
        this.errorCode = errorCode;
    }
}