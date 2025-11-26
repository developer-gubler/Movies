package com.schadraq.movies;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import com.schadraq.movies.dto.Movie;

import jakarta.validation.Valid;

@RestControllerAdvice(basePackageClasses = MovieController.class)
public class MovieControllerAdvice {

	private static Logger log = LogManager.getLogger(MovieControllerAdvice.class);
	
    /**
     * This method is called when the validation (jsr-380) fails either in a
     * controller (via methods with @Valid) or a service where we force
     * validation to occur.
     * 
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return errors;
    }

    /**
     * This method is called when a user attempts to search for an entity with
     * the required parameters, but there is a problem with data binding
     * (ie ResponseEntity<List<Movie>> movie(@Valid Movie movie) - and the
     * user sends the releaseYear as empty).
     * 
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HandlerMethodValidationException.class)
    public Map<String, String> handleValidationExceptions(HandlerMethodValidationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getAllErrors().forEach((error) -> {
            String fieldName = ((DefaultMessageSourceResolvable)error.getArguments()[0]).getCodes()[1];
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return errors;
    }

    /**
     * This method is called when a user attempts to call an endpoint without
     * the required parameters.
     * 
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Map<String, String> handleValidationExceptions(MissingServletRequestParameterException ex) {
        Map<String, String> errors = new HashMap<>();
        String fieldName = "parameters";
        String errorMessage = "Required request parameter(s) not present";
        errors.put(fieldName, errorMessage);

        return errors;
    }

    /**
     * This method is called when a user attempts to add an entity that already
     * exists.
     * 
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Map<String, String> handleValidationExceptions(DataIntegrityViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        String fieldName = "uniqueness";
        String errorMessage = "Object already exists";
        errors.put(fieldName, errorMessage);

        return errors;
    }
    
}
