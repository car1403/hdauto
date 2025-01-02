package com.hd.common.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseError<T> {


    private String status;

    private String message;

    private T data;

    private String accessToken;
}