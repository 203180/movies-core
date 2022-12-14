package com.example.webmailcore.exceptions;

public class HttpException extends Exception{

    private Integer code;

    public HttpException(String message, Integer code){
        super(message);
        this.code = code;
    }
}