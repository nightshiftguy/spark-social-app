package com.nightguy.spark.auth;

import com.nightguy.spark.security.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthenticationController {
  AuthenticationService authenticationService;

  @PostMapping("/login")
  public AuthenticationResponse login(@RequestBody AuthenticationRequest loginRequest) {
    return authenticationService.authenticate(loginRequest);
  }

  @PostMapping("/register")
  public AuthenticationResponse register(@RequestBody AuthenticationRequest registrationRequest) {
    return authenticationService.register(registrationRequest);
  }
}
