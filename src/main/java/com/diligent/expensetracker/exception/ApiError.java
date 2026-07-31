package com.diligent.expensetracker.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ApiError {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<ValidationError> fieldErrors;

    public ApiError() {
        this.fieldErrors = new ArrayList<>();
    }

    public ApiError(LocalDateTime timestamp,
                    int status,
                    String error,
                    String message,
                    String path,
                    List<ValidationError> fieldErrors) {

        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;

        // Never allow null
        this.fieldErrors = (fieldErrors == null)
                ? new ArrayList<>()
                : fieldErrors;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<ValidationError> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<ValidationError> fieldErrors) {
        this.fieldErrors = (fieldErrors == null)
                ? new ArrayList<>()
                : fieldErrors;
    }
}