package shako.schoolmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CHECKPOINT)
public class EmailCannotBeEmptyException extends RuntimeException{
    private String message;

    public EmailCannotBeEmptyException(String message) {
        super(message);
    }
}
