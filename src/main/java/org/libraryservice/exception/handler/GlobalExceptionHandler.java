package org.libraryservice.exception.handler;



import lombok.extern.slf4j.Slf4j;
import org.libraryservice.exception.BookDuplicateException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;



@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({BookDuplicateException.class})
    public String handleEntityDuplicateException(BookDuplicateException entityDuplicateException, Model model) {

        return null;
    }

    @ExceptionHandler({Exception.class})
    public String handleServiceException(Exception exception, Model model) {

        return null;
    }


    @ExceptionHandler({RuntimeException.class})
    public String handleRuntimeException(RuntimeException runtimeException, Model model) {

        return null;
    }

}
