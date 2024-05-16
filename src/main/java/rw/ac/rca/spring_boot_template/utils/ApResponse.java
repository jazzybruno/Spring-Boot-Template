package rw.ac.rca.spring_boot_template.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class ApResponse<T> {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;
    private String message = "";
    private HttpStatus status;
    private Boolean success;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object error = null;
    private final String timestamp = LocalDateTime.now().toString();

    public ApResponse() {
    }
    public ApResponse(boolean success, Object data) {
        this.success = success;
        this.message = message;
    }

    public static ApResponse success(Object data) {
        return new ApResponse(true, data);
    }
    public ApResponse(String message, Object error, HttpStatus status) {
        this.message = message;
        this.error = error;
        this.status = status;
    }
    public ApResponse(T data, String message, Object error, HttpStatus status) {
        this.message = message;
        this.error = error;
        this.status = status;
    }

    public ApResponse(T data) {
        this.data = data;
    }

    public ApResponse(String message) {
        this.message = message;
    }

    public ApResponse(T data, HttpStatus status) {
        this.data = data;
        this.status = status;
    }

    public ApResponse(T data, String message) {
        this.data = data;
        this.message = message;
    }
    public ApResponse(T data, String message, HttpStatus status) {
        this.data = data;
        this.message = message;
        this.status = status;
    }

    public ResponseEntity<ApResponse<T>> toResponseEntity() {
        assert this.status != null;
        return ResponseEntity.status(this.status).body(this);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public Object getError() {
        return error;
    }
}


