package com.example.meal2.auth;

import com.example.meal2.config.JwtService;
import com.example.meal2.exception.GenericBadRequestException;
import com.example.meal2.exception.ResourceLimitException;
import com.example.meal2.user.Role;
import com.example.meal2.user.User;
import com.example.meal2.user.UserRepository;
import com.example.meal2.user.dto.UserCreationDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwrodEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    public void setUp(){
        // from @Value application.properties since using SpringBootTest will make it an integration test
        ReflectionTestUtils.setField(authenticationService, "maxUsers", 250);
    }

    @DisplayName("register: normal")
    @Test
    void register() {
        UserCreationDTO ucDTO = new UserCreationDTO("user123", "abc123", "abc123");

        authenticationService.register(ucDTO);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @DisplayName("register: username already exists")
    @Test
    void register1(){
        UserCreationDTO ucDTO = new UserCreationDTO("user123", "abc123", "abc123");

        User user = new User("user123","abc123", Role.USER);

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {authenticationService.register(ucDTO);}
        );
        verify(userRepository, never()).save(any());
    }

    @DisplayName("register: max users")
    @Test
    void register2(){
        UserCreationDTO ucDTO = new UserCreationDTO("user123", "abc123", "abc123");

        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        when(userRepository.count()).thenReturn(250L);

        Assertions.assertThrows(
                ResourceLimitException.class,
                () -> authenticationService.register(ucDTO)
        );
        verify(userRepository, never()).save(any());
    }

    @DisplayName("register: password and passwordConfirmation mismatch ")
    @Test
    void register3(){
        UserCreationDTO ucDTO = new UserCreationDTO("user123", "zbc123", "abc123");

        Assertions.assertThrows(
                GenericBadRequestException.class,
                () -> authenticationService.register(ucDTO)
        );
        verify(userRepository, never()).save(any());
    }

}