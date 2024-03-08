package shako.schoolmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadCredentialsException extends RuntimeException {


    public BadCredentialsException(String message) {
        super(message);
    }
}
