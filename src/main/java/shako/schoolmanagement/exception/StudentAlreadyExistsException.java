package shako.schoolmanagement.exception;

public class StudentAlreadyExistsException extends RuntimeException{

    public StudentAlreadyExistsException(String message){
        super(message);
    }
}
