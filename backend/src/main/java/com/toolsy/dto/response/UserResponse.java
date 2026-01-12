package com.toolsy.dto.response;

import com.toolsy.model.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Odpowiedź zawierająca informacje o użytkowniku")
public class UserResponse {
    @Schema(description = "ID użytkownika", example = "1")
    private Long id;
    @Schema(description = "Nazwa użytkownika", example = "user1")
    private String username;
    @Schema(description = "Email użytkownika", example = "user1@example.com")
    private String email;
    @Schema(description = "Imię użytkownika", example = "Jan")
    private String firstName;
    @Schema(description = "Nazwisko użytkownika", example = "Kowalski")
    private String lastName;
    @Schema(description = "Numer telefonu użytkownika", example = "+48123456789")
    private String phoneNumber;
    @Schema(description = "Rola użytkownika", example = "USER")
    private UserRole role;
    @Schema(description = "Czy konto jest aktywne", example = "true")
    private Boolean active;
    @Schema(description = "Data utworzenia konta")
    private LocalDateTime createdAt;

    public UserResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}







