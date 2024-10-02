package com.movie_theaters.exception;

public class ObjNotFoundException extends RuntimeException {
    public ObjNotFoundException(String message) {
        super(message);
    }
}