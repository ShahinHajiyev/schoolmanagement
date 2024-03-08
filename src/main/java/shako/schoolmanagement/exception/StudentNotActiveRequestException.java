package shako.schoolmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class StudentNotActiveRequestException extends AuthenticationServiceException {

    public StudentNotActiveRequestException(String message){
        super(message);
    }
}
