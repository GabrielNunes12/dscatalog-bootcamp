package com.devsuperior.dscatalog.resources.exceptions;

import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {
  private List<FieldMessage> errors = new ArrayList<>();

  public List<FieldMessage> getErrors() {
    return errors;
  }
  public void addError(String fieldName, String message) {
    this.errors.add(new FieldMessage(fieldName, message));
  }
}
