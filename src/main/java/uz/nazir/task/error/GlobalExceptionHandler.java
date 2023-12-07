package uz.nazir.task.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import uz.nazir.task.error.dto.ExceptionResponse;
import uz.nazir.task.error.exceptions.ForbiddenException;
import uz.nazir.task.error.exceptions.NotFoundException;
import uz.nazir.task.error.exceptions.UnauthorizedException;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * 400
     * Override MethodArgumentNotValidException
     * Do not annotate with @ExceptionHandler(MethodArgumentNotValidException.class)
     * It causes “Ambiguous @ExceptionHandler method mapped“ error
     */

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatus status,
            @NonNull WebRequest request
    ) {
        return ResponseEntity
                .status(status)
                .body(new ExceptionResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.name(), LocalDateTime.now()));
    }


    /**
     * 401.
     */
    @ExceptionHandler({UnauthorizedException.class, BadCredentialsException.class})
    public ResponseEntity<ExceptionResponse> handleUnauthorizedException(RuntimeException ex) {
        return buildResponseEntity(ex, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 403.
     */
    @ExceptionHandler({ForbiddenException.class})
    public ResponseEntity<ExceptionResponse> handleUnauthorizedException(ForbiddenException ex) {
        return buildResponseEntity(ex, HttpStatus.FORBIDDEN);
    }

    /**
     * 404
     */
    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ExceptionResponse> handleResourceNotFoundException(NotFoundException ex) {
        return buildResponseEntity(ex, HttpStatus.NOT_FOUND);
    }

    /**
     * 409.
     */
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ExceptionResponse> handleIllegalExceptions(RuntimeException ex) {
        return buildResponseEntity(ex, HttpStatus.CONFLICT);
    }

    /**
     * 500.
     * Throw exception for any other unpredicted reason.
     * Avoid modification of this method to make problem visible in logs.
     * Don't delete this method to avoid descendants declare it and catch 'any error'.
     */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleOtherExceptions(Exception ex) throws Exception {
        throw ex;
    }

    private ResponseEntity<ExceptionResponse> buildResponseEntity(Exception ex, HttpStatus httpStatus) {
        return ResponseEntity
                .status(httpStatus)
                .body(new ExceptionResponse(ex.getMessage(), httpStatus.name(), LocalDateTime.now()));
    }
}
