package com.example.meal2.auth;

import com.example.meal2.auth.dto.AuthenticationResponseDTO;
import com.example.meal2.user.dto.UserCreationDTO;
import com.example.meal2.user.dto.UserSigninDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authService;

    @Autowired
    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @Valid @RequestBody UserCreationDTO userCreationDTO
    ){
        authService.register(userCreationDTO);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthenticationResponseDTO> signin(
            @Valid @RequestBody UserSigninDTO userSigninDTO
    ){
        return new ResponseEntity<>(authService.authenticate(userSigninDTO), HttpStatus.OK);
    }
}
