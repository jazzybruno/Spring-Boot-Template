package rw.ac.rca.spring_boot_template.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class ErrorDetails {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final Date timestamp;
    private final String message;
    private final int status;
    private Object error;

    public ErrorDetails(Date timestamp, String message, int status) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.status = status;
    }

    public ErrorDetails(Date timestamp, String message, Object error, int status) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.status = status;
        this.error = error;
    }


    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }
}
