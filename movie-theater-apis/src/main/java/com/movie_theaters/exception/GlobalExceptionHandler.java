package com.movie_theaters.exception;

import com.movie_theaters.dto.response.ErrorApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex) {
        return new ResponseEntity<>("Bạn không có quyền truy cập vào tài nguyên này.", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorApiResponse> handleIllegalArgumentException(Exception ex) {
        return new ResponseEntity<>(
                new ErrorApiResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(), ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(new ErrorApiResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
                ex.getFieldError().getDefaultMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorApiResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return new ResponseEntity<>(
                new ErrorApiResponse(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name(), ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    // @ExceptionHandler(RuntimeException.class)
    // public ResponseEntity<ErrorApiResponse> handleRuntimeException(RuntimeException ex) {
    //     return new ResponseEntity<>(
    //             new ErrorApiResponse(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.name(), ex.getMessage()),
    //             HttpStatus.BAD_REQUEST);
    // }

    @ExceptionHandler(ObjNotFoundException.class)
    public ResponseEntity<String> handleObjNotFoundException(ObjNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

     @ExceptionHandler(TokenException.class)
     public ResponseEntity<ErrorApiResponse> handleTokenException(TokenException ex) {
         return new ResponseEntity<>(
                 new ErrorApiResponse(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.name(), ex.getMessage()),
                 HttpStatus.UNAUTHORIZED);
     }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorApiResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        if (ex.getRequiredType() != null && ex.getRequiredType().equals(LocalDate.class)) {
            return new ResponseEntity<>(new ErrorApiResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(), "Ngày nhập vào không hợp lệ. Vui lòng kiểm tra lại!"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ErrorApiResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(), "Tham số đầu vào không hợp lệ!"), HttpStatus.BAD_REQUEST);
    }
}
