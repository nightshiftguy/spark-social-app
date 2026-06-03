package com.nightguy.spark.security;

import com.nightguy.spark.auth.AuthenticationRequest;
import com.nightguy.spark.auth.AuthenticationResponse;
import com.nightguy.spark.user.Role;
import com.nightguy.spark.user.User;
import com.nightguy.spark.user.UserRepository;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(@Valid AuthenticationRequest request) {
    Optional<User> optionalUser = repository.findByUsername(request.getLogin());
    if (optionalUser.isPresent()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "User with this login already exists");
    } else {
      User user =
          new User(Role.USER, passwordEncoder.encode(request.getPassword()), request.getLogin());
      repository.save(user);
      return AuthenticationResponse.builder().token(jwtService.generateToken(user)).build();
    }
  }

  public AuthenticationResponse authenticate(@Valid AuthenticationRequest request) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid login or password");
    }
    var user =
        repository
            .findByUsername(request.getLogin())
            .orElseThrow(() -> new IllegalStateException("User missing after authentication"));

    return AuthenticationResponse.builder().token(jwtService.generateToken(user)).build();
  }
}
