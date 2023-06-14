package com.example.meal2.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UserSigninDTO(
    @NotBlank(message="username => must not be blank")
    String username,
    @NotBlank(message="password => must not be blank")
    String password
) {
}
