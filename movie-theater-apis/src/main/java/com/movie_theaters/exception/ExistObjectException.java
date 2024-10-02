package com.movie_theaters.exception;

public class ExistObjectException extends RuntimeException{
    public ExistObjectException(String message) {
        super(message);
    }
}
