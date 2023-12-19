package com.M2D.EmissionAssessment.Exception;

//import org.apache.commons.lang.StringUtils;

//import org.omg.CosNaming.NamingContextPackage.NotFound;
import jakarta.persistence.NonUniqueResultException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionalHandler extends ResponseEntityExceptionHandler implements handleMethodArgumentNotValid {

    @ExceptionHandler({HttpServerErrorException.InternalServerError.class})
    public ResponseEntity internalServerError(HttpServerErrorException exception, WebRequest webRequest) {
        final String ERROR_MESSAGE = "internal server error";
        logger.error(ERROR_MESSAGE, exception);
        Error errorResponse = getErrorResponse(HttpStatus.NOT_FOUND, ERROR_MESSAGE, webRequest.getDescription(true));
        return handleExceptionInternal(exception, errorResponse, new HttpHeaders(), HttpStatus.NOT_FOUND,
                webRequest);
    }

//    @ExceptionHandler({HttpClientErrorException.NotFound.class})
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity notFoundException(Exception exception, WebRequest webRequest) {
        final String ERROR_MESSAGE = exception.getMessage();//"NotFound Exception";
        logger.error(ERROR_MESSAGE, exception);
        Error errorResponse = getErrorResponse(HttpStatus.NOT_FOUND, ERROR_MESSAGE, webRequest.getDescription(true));
        return handleExceptionInternal(exception, errorResponse, new HttpHeaders(), HttpStatus.NOT_FOUND,
                webRequest);
    }

    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity handleNullPointerException(Exception exception, WebRequest webRequest) {
        final String ERROR_MESSAGE = "null pointer exception";
        logger.error(ERROR_MESSAGE, exception);
        Error errorResponse = getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_MESSAGE, webRequest.getDescription(true));
        return handleExceptionInternal(exception, errorResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR,
                webRequest);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity arrayIndexOutOfBoundsException(Exception exception, WebRequest webRequest) {
        final String ERROR_MESSAGE = exception.getMessage();
        logger.error(ERROR_MESSAGE, exception);
        Error errorResponse = getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_MESSAGE, webRequest.getDescription(true));
        return handleExceptionInternal(exception, errorResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR,
                webRequest);
    }

    @ExceptionHandler({IllegalStateException.class})
    public ResponseEntity arrayIndexOutOfBoundsException1(Exception exception, WebRequest webRequest) {
        final String ERROR_MESSAGE = exception.getMessage();
        logger.error(ERROR_MESSAGE, exception);
        Error errorResponse = getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_MESSAGE, webRequest.getDescription(true));
        return handleExceptionInternal(exception, errorResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR,
                webRequest);
    }

    @ExceptionHandler({FAExceptopn.class})
    public ResponseEntity<Object> exception(FAExceptopn exception, WebRequest webRequest) {
        logger.error(exception.getMessage(), exception);
        Error errorResponse = getCustomErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), webRequest.getDescription(true), exception.getErrorCode());
        return handleExceptionInternal(exception, errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST,
                webRequest);
    }

    @ExceptionHandler({NonUniqueResultException.class})
    public ResponseEntity<Object> exception(NonUniqueResultException exception, WebRequest webRequest) {
        logger.error(exception.getMessage(), exception);
        Error errorResponse = getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Two Records Found Instead of One", webRequest.getDescription(true));
        return handleExceptionInternal(exception, errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST,
                webRequest);
    }
    @ExceptionHandler({SQLIntegrityConstraintViolationException.class})
    public ResponseEntity<Object> SQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException exception, WebRequest webRequest) {
        logger.error(exception.getMessage(), exception);
        Error errorResponse = getErrorResponse(HttpStatus.BAD_REQUEST, "Try To Add Duplicate Data In Unique Column", webRequest.getDescription(true));
        return handleExceptionInternal(exception, errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST,
                webRequest);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> Exception(Exception exception, WebRequest webRequest) {
        logger.error(exception.getMessage(), exception);
        Error errorResponse = getErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), webRequest.getDescription(true));
        return handleExceptionInternal(exception, errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST,
                webRequest);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<Object> Exception(DataIntegrityViolationException exception, WebRequest webRequest) {
        logger.error(exception.getMessage(), exception);
        Error errorResponse = getErrorResponse(HttpStatus.BAD_REQUEST, "USERNAME ALREADY TAKEN", webRequest.getDescription(true));
        return handleExceptionInternal(exception, errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST,
                webRequest);
    }

    @ExceptionHandler({InvalidDataAccessResourceUsageException.class})
    public ResponseEntity<Object> Exception(InvalidDataAccessResourceUsageException exception, WebRequest webRequest) {
        logger.error(exception.getMessage(), exception);
        Error errorResponse = getErrorResponse(HttpStatus.NOT_FOUND, "Table Dosen't Exist", webRequest.getDescription(true));
        return handleExceptionInternal(exception, errorResponse, new HttpHeaders(), HttpStatus.NOT_FOUND,
                webRequest);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                               HttpHeaders headers,
                                                               HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());

        //Get all errors
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        body.put("errors", errors);

        return new ResponseEntity<>(body, headers, status);

    }

    protected Error getErrorResponse(HttpStatus status, String errorMessage, String path) {
        if (errorMessage.isEmpty()) {
            errorMessage = "An unexpected error occurred";
        }
        return new Error(status.name(), String.valueOf(status.value()), errorMessage, path);
    }

    protected Error getCustomErrorResponse(HttpStatus status, String errorMessage, String path, String customError) {
        if (errorMessage.isEmpty()) {
            errorMessage = "An unexpected error occurred";
        }
        return new Error(status.name(), customError, errorMessage, path);
    }


//    @ExceptionHandler({Exception.class})
//    public ResponseEntity handleAnyException(Exception exception, WebRequest webRequest) {
//        final String ERROR_MESSAGE = "An unexpected error  occurred";
//        logger.error(ERROR_MESSAGE, exception);
//        Error errorResponse = getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_MESSAGE, webRequest.getDescription(true));
//        return handleExceptionInternal(exception, errorResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR,
//                webRequest);
//    }


    private Map<String, String> generateObjectErrors(BindingResult result) {
        Map<String, String> fieldErrorMap = new HashMap<>();
        result.getGlobalErrors().forEach(objectError -> {
            if (objectError.getCode().contains("ConfirmPassword")) {
                fieldErrorMap.put("confirmPassword", objectError.getDefaultMessage());
            }
        });
        return fieldErrorMap;
    }


//    @ExceptionHandler({RuntimeException.class})
//    public ResponseEntity runtimeException(Exception exception, WebRequest webRequest) {
////        final String ERROR_MESSAGE = "RunTime Exception Occured";
//        final String ERROR_MESSAGE = exception.getMessage();
//        logger.error(ERROR_MESSAGE, exception);
//        Error errorResponse = getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_MESSAGE, webRequest.getDescription(true));
//        return handleExceptionInternal(exception, errorResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR,
//                webRequest);
//    }

}