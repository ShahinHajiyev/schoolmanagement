package shako.schoolmanagement.exception;

public class StudentNotExistsException extends RuntimeException{

    public StudentNotExistsException(String message){
        super(message);
    }
}
