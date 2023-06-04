package com.example.meal2.auth;

import com.example.meal2.config.JwtService;
import com.example.meal2.user.Role;
import com.example.meal2.user.User;
import com.example.meal2.user.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(RegisterRequest request) {
        //check if username already exists?
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        repository.save(user);
        // todo registration should not return jwt...
        var jwtToken = jwtService.generateToken(user);
        var authResponse = new AuthenticationResponse();
        authResponse.setToken(jwtToken);
        return authResponse;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow();

        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", user.getId());
        var jwtToken = jwtService.generateToken(claims, user);
        var authResponse = new AuthenticationResponse();
        authResponse.setToken(jwtToken);
        return authResponse;
    }
}
