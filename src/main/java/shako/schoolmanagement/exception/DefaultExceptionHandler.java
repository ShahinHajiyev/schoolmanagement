package shako.schoolmanagement.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {

    // ── 404 ──────────────────────────────────────────────────────────────────

    @ExceptionHandler(StudentNotExistsException.class)
    public ResponseEntity<ErrorResponse> handleStudentNotExists(StudentNotExistsException ex) {
        log.warn("Student not found: {}", ex.getMessage());
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // ── 400 ──────────────────────────────────────────────────────────────────

    @ExceptionHandler(EmailCannotBeEmptyException.class)
    public ResponseEntity<ErrorResponse> handleEmailCannotBeEmpty(EmailCannotBeEmptyException ex) {
        log.warn("Email validation failed: {}", ex.getMessage());
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(UserTypeCannotBeEmptyException.class)
    public ResponseEntity<ErrorResponse> handleUserTypeCannotBeEmpty(UserTypeCannotBeEmptyException ex) {
        log.warn("User type validation failed: {}", ex.getMessage());
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // ── 409 ──────────────────────────────────────────────────────────────────

    @ExceptionHandler(StudentAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleStudentAlreadyExists(StudentAlreadyExistsException ex) {
        log.warn("Student already exists: {}", ex.getMessage());
        return build(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(EnrollmentOutOfLimitException.class)
    public ResponseEntity<ErrorResponse> handleEnrollmentOutOfLimit(EnrollmentOutOfLimitException ex) {
        log.warn("Enrollment limit exceeded: {}", ex.getMessage());
        return build(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(EnrollmentOutOfTimeException.class)
    public ResponseEntity<ErrorResponse> handleEnrollmentOutOfTime(EnrollmentOutOfTimeException ex) {
        log.warn("Enrollment time restriction: {}", ex.getMessage());
        return build(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(StudentNotActiveRequestException.class)
    public ResponseEntity<ErrorResponse> handleStudentNotActive(StudentNotActiveRequestException ex) {
        log.warn("Student account not active: {}", ex.getMessage());
        return build(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    // ── Bean validation (@Valid) ──────────────────────────────────────────────

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                   HttpHeaders headers,
                                                                   HttpStatus status,
                                                                   WebRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("Validation failed: {}", message);
        ErrorResponse body = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Validation Failed", message);
        return ResponseEntity.badRequest().body(body);
    }

    // ── 400 (IllegalArgument) ─────────────────────────────────────────────────

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Bad request: {}", ex.getMessage());
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // ── Catch-all ────────────────────────────────────────────────────────────

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        log.error("Unhandled exception: {}", ex.getMessage(), ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    }

    // ── Helper ───────────────────────────────────────────────────────────────

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String message) {
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(status.value(), status.getReasonPhrase(), message));
    }
}
