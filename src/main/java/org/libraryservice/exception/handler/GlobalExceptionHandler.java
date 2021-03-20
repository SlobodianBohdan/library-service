package org.libraryservice.exception.handler;



import lombok.extern.slf4j.Slf4j;
import org.libraryservice.dto.ResponseMessageDto;
import org.libraryservice.exception.EntityDuplicateException;
import org.libraryservice.exception.EntityNotFoundException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<ResponseMessageDto> handleEntityNotFoundException(EntityNotFoundException entityNotFoundException) {
        LOGGER.warn(entityNotFoundException.getMessage(), entityNotFoundException);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ResponseMessageDto(entityNotFoundException.getMessage()));
    }

    @ExceptionHandler({EntityDuplicateException.class})
    public ResponseEntity<ResponseMessageDto> handleEntityDuplicateException(EntityDuplicateException entityDuplicateException) {
        LOGGER.warn(entityDuplicateException.getMessage(), entityDuplicateException);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResponseMessageDto(entityDuplicateException.getMessage()));
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ResponseMessageDto> handleRuntimeException(RuntimeException runtimeException) {
        LOGGER.warn(runtimeException.getMessage(), runtimeException);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseMessageDto(runtimeException.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error-> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }

}
