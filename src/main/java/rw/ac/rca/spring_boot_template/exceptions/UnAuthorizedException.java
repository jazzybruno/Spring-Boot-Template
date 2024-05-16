package rw.ac.rca.spring_boot_template.exceptions;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
@Getter
@AllArgsConstructor
public class UnAuthorizedException extends RuntimeException {

    private String message = "exceptions.unauthorized";
    private Object[] args;

    public UnAuthorizedException(Object ...args){
        super("Message");
        this.args = args;

    }
}
