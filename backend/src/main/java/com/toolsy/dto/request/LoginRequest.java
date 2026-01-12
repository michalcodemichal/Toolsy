package com.toolsy.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Żądanie logowania użytkownika")
public class LoginRequest {
    @NotBlank
    @Schema(description = "Nazwa użytkownika", example = "user1", required = true)
    private String username;

    @NotBlank
    @Schema(description = "Hasło użytkownika", example = "user123", required = true)
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}







