package com.kadioglumf.scheduler.config;

import com.kadioglumf.scheduler.payload.response.error.BaseErrorResponse;
import com.kadioglumf.scheduler.payload.response.error.ErrorType;
import com.kadioglumf.scheduler.exceptions.SchedulerServiceException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(IOException.class)
    public ResponseEntity<Object> catchIOException(final IOException ex) {
        BaseErrorResponse response = new BaseErrorResponse(HttpStatus.EXPECTATION_FAILED.value(), ErrorType.UNKNOWN_SCHEDULER_SERVICE_ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> catchDataIntegrityViolationException(final DataIntegrityViolationException ex) {
        ex.printStackTrace();
        BaseErrorResponse response = new BaseErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(), ErrorType.UNKNOWN_SCHEDULER_SERVICE_ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(final IllegalArgumentException ex) {
        BaseErrorResponse response = new BaseErrorResponse(HttpStatus.BAD_REQUEST.value(), ErrorType.UNKNOWN_SCHEDULER_SERVICE_ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(final HttpMessageNotReadableException ex) {
        BaseErrorResponse response = new BaseErrorResponse(HttpStatus.BAD_REQUEST.value(), ErrorType.UNKNOWN_SCHEDULER_SERVICE_ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseErrorResponse> handleConstraintViolationException(final ConstraintViolationException ex) {
        BaseErrorResponse response = new BaseErrorResponse(HttpStatus.BAD_REQUEST.value(), ErrorType.UNKNOWN_SCHEDULER_SERVICE_ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalStateException(final IllegalStateException ex) {
        BaseErrorResponse response = new BaseErrorResponse(HttpStatus.SERVICE_UNAVAILABLE.value(), ErrorType.UNKNOWN_SCHEDULER_SERVICE_ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(InvocationTargetException.class)
    public ResponseEntity<BaseErrorResponse> handleInvocationTargetException(final InvocationTargetException ex) {
        BaseErrorResponse response = new BaseErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorType.UNKNOWN_SCHEDULER_SERVICE_ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseErrorResponse> catchMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
        ex.printStackTrace();
        final List<String> errors = new ArrayList<>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        BaseErrorResponse response = new BaseErrorResponse(HttpStatus.BAD_REQUEST.value(), ErrorType.REQUEST_OBJECT_NOT_VALID_ERROR, String.join("-", errors));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<BaseErrorResponse> handleTransactionSystemException(final TransactionSystemException ex) {
        ex.getStackTrace();
        String errorMessage;
        if ( ex.getCause() != null && ex.getCause().getCause() != null) {
            errorMessage = ex.getCause().getCause().getMessage();
        }
        else {
            errorMessage = ex.getMessage();
        }
        BaseErrorResponse response = new BaseErrorResponse(HttpStatus.BAD_REQUEST.value(), ErrorType.UNKNOWN_SCHEDULER_SERVICE_ERROR, errorMessage);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SchedulerServiceException.class)
    public ResponseEntity<BaseErrorResponse> handleInsuranceServiceException(final SchedulerServiceException ex) {
        return new ResponseEntity<>(ex.getErrorResponse(), HttpStatus.valueOf(ex.getErrorResponse().getHttpStatusCode()));
    }
}

