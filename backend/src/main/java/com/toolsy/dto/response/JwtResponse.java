package com.toolsy.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Odpowiedź zawierająca token JWT i informacje o użytkowniku")
public class JwtResponse {
    @Schema(description = "Token JWT", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
    @Schema(description = "Typ tokenu", example = "Bearer")
    private String type = "Bearer";
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
    @Schema(description = "Lista ról użytkownika", example = "[\"USER\"]")
    private List<String> roles;
    @Schema(description = "Zahashowane hasło (tylko do użytku wewnętrznego)", hidden = true)
    private String hashedPassword;

    public JwtResponse(String token, Long id, String username, String email, String firstName, String lastName, List<String> roles) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }
}



