package com.devsuperior.dscatalog.dto;

import javax.validation.constraints.Size;

public class UserInsertDTO extends UserDTO{
  @Size(min = 1, max = 32, message = "Senha deve ter @/-[A-Z]a-z1-9")
  private String password;
  public UserInsertDTO(Long id, String firstName, String lastName, String email) {
    super(id, firstName, lastName, email);
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
