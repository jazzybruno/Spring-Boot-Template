package rw.ac.rca.spring_boot_template.exceptions;


import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import rw.ac.rca.spring_boot_template.utils.ApiResponse;
import rw.ac.rca.spring_boot_template.utils.ExceptionUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@EnableWebMvc
public class GlobalExceptionHandler {
    private final MessageSource messageSource;
    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> customException(CustomException ex){
        return ExceptionUtils.handleControllerExceptions(ex.getException());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<?> badCredentialsException(BadCredentialsException ex, Locale locale) {
        return new ApiResponse<>(messageSource.getMessage("exceptions.invalidCredentials", null, locale), (Object) "", HttpStatus.UNAUTHORIZED).toResponseEntity();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, Locale locale) {
        String errorMessage = messageSource.getMessage(ex.getMessage(), ex.getArgs(), locale);
        return new ApiResponse<>(errorMessage, (Object) "", HttpStatus.NOT_FOUND).toResponseEntity();
    }


    @ExceptionHandler(BadRequestAlertException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> badRequestAlert(BadRequestAlertException ex, Locale locale) {
        String errorMessage = messageSource.getMessage(ex.getMessage(), ex.getArgs(), locale);
        return new ApiResponse<>(errorMessage, (Object) "", HttpStatus.BAD_REQUEST).toResponseEntity();
    }

    @ExceptionHandler(UnAuthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<?> unAuthorizedException(UnAuthorizedException ex, Locale locale) {
        String errorMessage = messageSource.getMessage(ex.getMessage(), ex.getArgs(), locale);
        return new ApiResponse<>(errorMessage, (Object) "", HttpStatus.UNAUTHORIZED).toResponseEntity();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public  ResponseEntity<?> handlerAccessDeniedException(final Exception ex,
                                                           final HttpServletRequest request, final HttpServletResponse response) {

        return new ApiResponse<>(ex.getMessage(), (Object) "", HttpStatus.UNAUTHORIZED).toResponseEntity();
    }

    @ExceptionHandler(DuplicateRecordException.class)
    public ResponseEntity<?> duplicateRecordException(DuplicateRecordException ex, Locale locale) {
        String errorMessage = messageSource.getMessage(ex.getMessage(), ex.getArgs(), locale);
        return new ApiResponse<>(errorMessage, (Object) "", HttpStatus.BAD_REQUEST).toResponseEntity();
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?>  handleMethodArgumentNotValid(MethodArgumentNotValidException ex, Locale locale) throws JsonProcessingException {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        String errorMessage = messageSource.getMessage("exceptions.validation.message", null, locale);
        return new ApiResponse<>(errorMessage, errors, HttpStatus.BAD_REQUEST).toResponseEntity();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception ex, Locale locale) throws JsonProcessingException {
        String message = ex.getMessage();
        Object error = ex.getMessage();

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (ex.getClass().getSimpleName().equals("InternalAuthenticationServiceException"))
            status = HttpStatus.UNAUTHORIZED;

        if (ex.getClass().getSimpleName().equals("HttpMessageNotReadableException")) {
            status = HttpStatus.BAD_REQUEST;
            message = "Malformed JSON request format";
        }

        String errorMessage = messageSource.getMessage("exceptions.validation.server", null, locale);
        return new ApiResponse<>(errorMessage, error, HttpStatus.INTERNAL_SERVER_ERROR).toResponseEntity();
    }
}

