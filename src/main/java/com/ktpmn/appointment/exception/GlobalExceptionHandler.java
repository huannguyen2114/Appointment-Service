package com.ktpmn.appointment.exception;

import java.nio.file.AccessDeniedException;
import java.util.Map;
import java.util.Objects;

import jakarta.validation.ConstraintViolation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ktpmn.appointment.dto.response.ApiResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

//@ControllerAdvice
//@Slf4j
//public class GlobalExceptionHandler {
//
//    // private static final String MIN_ATTRIBUTE = "min";
//
//    // @ExceptionHandler(value = RuntimeException.class)
//    // ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException
//    // exception) {
//    // log.error("Exception: ", exception);
//    // ApiResponse apiResponse = new ApiResponse();
//    // apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
//    // apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
//    // return ResponseEntity.badRequest().body(apiResponse);
//    // }
//
//    // @ExceptionHandler(value = IllegalArgumentException.class)
//    // ResponseEntity<ApiResponse>
//    // handlingIllegalArgumentException(IllegalArgumentException exception) {
//    // log.error("Exception: ", exception);
//    // ApiResponse apiResponse = new ApiResponse();
//    // apiResponse.setCode(400);
//    // apiResponse.setMessage(exception.getMessage());
//    // return ResponseEntity.badRequest().body(apiResponse);
//    // }
//
//    // @ExceptionHandler(value = HttpMessageNotReadableException.class)
//    // ResponseEntity<ApiResponse>
//    // handlingRuntimeException(HttpMessageNotReadableException exception) {
//    // log.error("Exception: ", exception);
//    // ApiResponse apiResponse = new ApiResponse();
//    // apiResponse.setCode(400);
//    // apiResponse.setMessage(exception.getMessage());
//    // return ResponseEntity.badRequest().body(apiResponse);
//    // }
//
//    // @ExceptionHandler(value = AppException.class)
//    // ResponseEntity<ApiResponse> handlingAppException(AppException exception) {
//    // log.error("Exception: ", exception);
//    // ErrorCode errorCode = exception.getErrorCode();
//    // ApiResponse apiResponse = new ApiResponse();
//
//    // apiResponse.setCode(errorCode.getCode());
//    // apiResponse.setMessage(exception.getMessage() != null ?
//    // exception.getMessage() : errorCode.getMessage());
//
//    // return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
//    // }
//
//    // @ExceptionHandler(value = AccessDeniedException.class)
//    // ResponseEntity<ApiResponse>
//    // handlingAccessDeniedException(AccessDeniedException exception) {
//    // log.error("Exception: ", exception);
//    // ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
//
//    // return ResponseEntity.status(errorCode.getStatusCode())
//    // .body(ApiResponse.builder()
//    // .code(errorCode.getCode())
//    // .message(errorCode.getMessage())
//    // .build());
//    // }
//
//    // @ExceptionHandler(value = MethodArgumentNotValidException.class)
//    // ResponseEntity<ApiResponse>
//    // handlingValidation(MethodArgumentNotValidException exception) {
//    // log.error("Exception: ", exception);
//    // ApiResponse apiResponse = new ApiResponse();
//    // apiResponse.setCode(400);
//    // apiResponse.setMessage(exception.getFieldError().getDefaultMessage());
//    // return ResponseEntity.badRequest().body(apiResponse);
//    // }
//
//}


@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = String.format("Invalid %s: %s", ex.getName(), ex.getValue());
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}