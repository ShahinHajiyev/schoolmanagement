package shako.schoolmanagement.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shako.schoolmanagement.exception.ErrorResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
public class CustomErrorController implements ErrorController {

    private static final String PATH = "/error";

    @RequestMapping(PATH)
    public ResponseEntity<ErrorResponse> handleError(HttpServletRequest request,
                                                     HttpServletResponse response) throws Throwable {
        Throwable exception = (Throwable) request.getAttribute("javax.servlet.error.exception");

        // If a real exception was forwarded here, re-throw it so @RestControllerAdvice
        // in DefaultExceptionHandler can produce the right JSON error shape.
        if (exception != null) {
            throw exception;
        }

        // No exception attribute means it's a plain 404 / 405 / etc. from the container
        // (e.g. no matching route). Build a generic response from the response status.
        int status = response.getStatus();
        HttpStatus httpStatus = HttpStatus.resolve(status);
        String error = httpStatus != null ? httpStatus.getReasonPhrase() : "Error";
        log.warn("Unmatched request to [{}], status {}", request.getRequestURI(), status);

        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(status, error, "No resource found at this path"));
    }
}
