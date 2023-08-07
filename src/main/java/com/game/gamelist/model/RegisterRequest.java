package com.game.gamelist.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegisterRequest {
    @Email(message = "Email should be valid")
    private String email;
    @NotEmpty(message = "Password is required")
    private String password;
    @Size(min = 2, message = "Username should have at least 2 characters")
    private String username;
}
