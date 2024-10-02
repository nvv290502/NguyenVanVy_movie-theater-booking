package com.movie_theaters.exception;

public class EmptyListException extends RuntimeException{
    public EmptyListException(String message) {
        super(message);
    }
}
