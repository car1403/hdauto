package com.hd.common.exception;

import com.hd.common.dto.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
//@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final Response response;

    public GlobalExceptionHandler(Response response) {
        this.response = response;
    }

    // User Define Exception
    @ExceptionHandler({EmailNotFoundException.class, NameDuplicateException.class,IdNotFoundException.class,
            DataNotFoundException.class })
    public ResponseEntity<?> handleEmailNotFoundException(Exception ex){
        ErrorCode code = null;
        String msg = null;
        if(ex instanceof  EmailNotFoundException){
            code = ((EmailNotFoundException)ex).getErrorCode();
            msg = ((EmailNotFoundException)ex).getMessage();
        }else if(ex instanceof  NameDuplicateException){
            code = ((NameDuplicateException)ex).getErrorCode();
            msg = ((NameDuplicateException)ex).getMessage();
        }else if(ex instanceof  IdNotFoundException){
            code = ((IdNotFoundException)ex).getErrorCode();
            msg = ((IdNotFoundException)ex).getMessage();
        }else if(ex instanceof  DataNotFoundException){
            code = ((DataNotFoundException)ex).getErrorCode();
            msg = ((DataNotFoundException)ex).getMessage();
        }
        return response.fail(code, msg, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex){
        log.info("IllegalArgumentException: %s",ex.getMessage());
        return response.success("",ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex){
        return response.fail(ErrorCode.ALL_Exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}