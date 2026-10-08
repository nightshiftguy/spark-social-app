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
  @Size(min = 8, message = "must be at least 8 characters long")
  private String password;
}
