package com.nightguy.spark.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationRequest {
  @NotBlank
  @Size(max = 16, message = "username can't be longer than 16 characters")
  private String login;

  @NotBlank
  @Size(min = 8, max = 72, message = "must be from 8 to 72 characters long")
  private String password;
}
