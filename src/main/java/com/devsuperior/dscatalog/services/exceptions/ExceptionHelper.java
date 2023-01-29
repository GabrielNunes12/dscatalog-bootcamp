package com.devsuperior.dscatalog.services.exceptions;

import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.util.logging.Logger;

@ControllerAdvice
public class ExceptionHelper {
  private static final Logger logger = (Logger) LoggerFactory.getLogger(ExceptionHelper.class);
  @ExceptionHandler(value = { ResourceNotFoundException.class })
  public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException exception) {
    logger.getLogger("Resource not found", exception.getMessage());
    return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.NOT_FOUND);
  };
  @ExceptionHandler(value = { EmptyResultDataAccessException.class })
  public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException exception) {
    logger.getLogger("Empty results", exception.getMessage());
    return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  };
  @ExceptionHandler(value = { DataIntegrityViolationException.class })
  public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
    logger.getLogger("Data integrity violation", exception.getMessage());
    return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  };
  @ExceptionHandler(value = { EntityNotFoundException.class })
  public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException exception) {
    logger.getLogger("Entity not found", exception.getMessage());
    return new ResponseEntity<Object>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  };
}
