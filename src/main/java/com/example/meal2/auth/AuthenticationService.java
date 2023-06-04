package com.example.meal2.auth;

import com.example.meal2.config.JwtService;
import com.example.meal2.exception.ResourceNotFoundException;
import com.example.meal2.exception.WrongUsernamePasswordException;
import com.example.meal2.user.Role;
import com.example.meal2.user.User;
import com.example.meal2.user.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public void register(RegisterRequest request) {
        Optional<User> u = userRepository.findByUsername(request.getUsername());
        if(u.isEmpty()){
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(Role.USER);
            userRepository.save(user);
            return;
        }
        throw new IllegalArgumentException(String.format("username: %s already exists", request.getUsername()));
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("username: %s not found", request.getUsername())));

        return generateJwt(user);
    }

    private AuthenticationResponse generateJwt(User user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", user.getId());
        var jwtToken = jwtService.generateToken(claims, user);
        var authResponse = new AuthenticationResponse();
        authResponse.setToken(jwtToken);
        return authResponse;
    }
}
