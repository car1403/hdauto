package com.hd.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseData<T> {

    private String status;

    private String message;

    private T data;

    private String accessToken;
}