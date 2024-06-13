package rw.ac.rca.spring_boot_template.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;


@Getter
@Setter
public class ApiResponse<T> {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;
    private String message;
    private HttpStatus status;
    private Boolean success;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object error;
    private final String timestamp = LocalDateTime.now().toString();

    // Default constructor
    public ApiResponse() {}

    // Constructor for success response
    public ApiResponse(T data) {
        this.data = data;
        this.success = true;
        this.status = HttpStatus.OK;
        this.message = "Request was successful";
    }

    // Constructor for error response
    public ApiResponse(String message, Object error, HttpStatus status) {
        this.message = message;
        this.error = error;
        this.status = status;
        this.success = false;
    }

    // Static method to create a success response
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data);
    }

    // Static method to create an error response
    public static <T> ApiResponse<T> error(String message, Object error, HttpStatus status) {
        return new ApiResponse<>(message, error, status);
    }

    // Convert to ResponseEntity
    public ResponseEntity<ApiResponse<T>> toResponseEntity() {
        return ResponseEntity.status(this.status).body(this);
    }

}


