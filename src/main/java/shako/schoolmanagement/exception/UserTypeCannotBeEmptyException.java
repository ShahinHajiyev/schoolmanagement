package shako.schoolmanagement.exception;

public class UserTypeCannotBeEmptyException extends RuntimeException{

    public UserTypeCannotBeEmptyException(String message){
        super(message);
    }
}
