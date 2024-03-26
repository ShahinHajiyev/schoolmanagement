package shako.schoolmanagement.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {



    private static final Logger logger = LoggerFactory.getLogger(DefaultExceptionHandler.class);



    @ExceptionHandler(value = {StudentNotActiveRequestException.class })
    public ResponseEntity<?> handleStudentNotActiveException(StudentNotActiveRequestException ex) {

        ExceptionMessage exceptionMessage = new ExceptionMessage(new Date(), ex.getMessage());

        RestError restError = new RestError(StatusCode.CONFLICT, ex.getMessage());

        String errorMessage = ex.getMessage();
        System.out.println(errorMessage);


        System.out.println(ex + " Not active");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    }

    @ExceptionHandler(value = {ExpiredJwtException.class })
    public ResponseEntity<?> handleExpiredJwtException(ExpiredJwtException ex) {
        ExceptionMessage exceptionMessage = new ExceptionMessage(new Date(), ex.getMessage());

        String errorMessage = "Expired JWT: " + ex.getMessage();
        System.out.println(errorMessage);

        System.out.println(ex + " Expired JWT");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
    }

    @ExceptionHandler(value = {JwtException.class })
    public ResponseEntity<?> handleJwtException(JwtException ex) {

        String errorMessage = "Bad token: " + ex.getMessage();
        System.out.println(errorMessage);

        System.out.println(ex + " JWT");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
    }


    @ExceptionHandler(value = {BadCredentialsException.class})
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException ex, WebRequest webRequest) {
        ExceptionMessage exceptionMessage = new ExceptionMessage(new Date(), ex.getMessage());



        String errorMessage = "BadCredentialsException : " + ex.getMessage();

        System.out.println(ex + " BAD Credentials");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
    }



   @ExceptionHandler(value = {RuntimeException.class})
    @ResponseBody
    public ResponseEntity<?> handleException(RuntimeException ex) {
        RestError re = new RestError(HttpStatus.UNAUTHORIZED.toString(),
                "RUN");
        System.out.println(ex);


            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());

    }


   @ExceptionHandler(value = { InternalAuthenticationServiceException.class })
    @ResponseBody
    public ResponseEntity<RestError> internalAuth(InternalAuthenticationServiceException ex) {
        RestError re = new RestError(HttpStatus.I_AM_A_TEAPOT.toString(),
                "Internal");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(re);

    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}

