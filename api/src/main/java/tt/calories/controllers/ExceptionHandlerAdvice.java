package tt.calories.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.rest.webmvc.RepositoryRestExceptionHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * TODO: description
 *
 * @author Cloves Almeida
 * @version 1.0.0
 */
@ControllerAdvice
@Component
public class ExceptionHandlerAdvice extends RepositoryRestExceptionHandler {

    @Autowired
    public ExceptionHandlerAdvice(MessageSource messageSource) {
        super(messageSource);
    }
}
