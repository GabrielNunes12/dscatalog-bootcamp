package com.devsuperior.dscatalog.dto;

public class UserInsertDTO extends UserDTO{
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
