package shako.schoolmanagement.exception;

import lombok.Getter;

@Getter
public class EnrollmentOutOfTimeException extends RuntimeException{

    public EnrollmentOutOfTimeException(String message) {
        super(message);
    }
}
