package rw.ac.rca.spring_boot_template.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter
@AllArgsConstructor
public class DuplicateRecordException extends Exception {
    private String message = "exceptions.duplicateRecord";
    private Object[] args;

    public DuplicateRecordException(Object ...args){
        this.args = args;
    }
}
