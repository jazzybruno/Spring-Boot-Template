package rw.ac.rca.spring_boot_template.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import rw.ac.rca.spring_boot_template.exceptions.*;

public class ExceptionUtils {
    public static  <T> ResponseEntity<ApiResponse<Object>> handleControllerExceptions(Exception e) {

        if (e instanceof NotFoundException) {
            return ApiResponse.error(
                    e.getMessage(),
                    e,
                    HttpStatus.NOT_FOUND
            ).toResponseEntity();
        } else if(e instanceof InvalidUUIDException){
            return ApiResponse.error(
                    e.getMessage(),
                    e,
                    HttpStatus.BAD_REQUEST
            ).toResponseEntity();
        }else if(e instanceof ResourceNotFoundException){
            return ApiResponse.error(
                    e.getMessage(),
                    e,
                    HttpStatus.NOT_FOUND
            ).toResponseEntity();
        }else if (e instanceof InternalServerErrorException) {
            return ApiResponse.error(
                    e.getMessage(),
                    e,
                    HttpStatus.INTERNAL_SERVER_ERROR
            ).toResponseEntity();
        } else if (e instanceof BadCredentialsException) {
            return ApiResponse.error(
                    e.getMessage(),
                    e,
                    HttpStatus.BAD_REQUEST
            ).toResponseEntity();
        } else if(e instanceof BadRequestAlertException){
            return ApiResponse.error(
                    e.getMessage(),
                    e,
                    HttpStatus.BAD_REQUEST
            ).toResponseEntity();
        }else {
            // Handle other exceptions as needed
            return ApiResponse.error(
                    e.getMessage(),
                    e,
                    HttpStatus.INTERNAL_SERVER_ERROR
            ).toResponseEntity();
        }
    }

    public static <T> void handleServiceExceptions(Exception e) throws ResourceNotFoundException {
        System.out.println("Exception caught in service:");
        if (e instanceof NotFoundException) {
            throw new NotFoundException(e.getMessage());
        } else if( e instanceof ResourceNotFoundException){
            throw new ResourceNotFoundException(e.getMessage());
        }else if (e instanceof InternalServerErrorException) {
            throw new InternalServerErrorException(e.getMessage());
        } else if (e instanceof BadRequestAlertException){
            throw new BadRequestAlertException(e.getMessage());
        }
//        else {
//            // Rethrow other exceptions as needed
//            throw new RuntimeException("Failed!! Something went wrong", e);
//        }
    }

}

