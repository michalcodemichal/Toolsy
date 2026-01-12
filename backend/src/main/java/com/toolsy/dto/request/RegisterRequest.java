package com.toolsy.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Żądanie rejestracji nowego użytkownika")
public class RegisterRequest {
    @NotBlank
    @Size(min = 3, max = 50)
    @Schema(description = "Nazwa użytkownika (3-50 znaków)", example = "jan_kowalski", required = true)
    private String username;

    @NotBlank
    @Email
    @Schema(description = "Adres email użytkownika", example = "jan.kowalski@example.com", required = true)
    private String email;

    @NotBlank
    @Size(min = 6, message = "Hasło musi zawierać co najmniej 6 znaków")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>]).*$", 
             message = "Hasło musi zawierać co najmniej jedną wielką literę i jeden znak specjalny")
    @Schema(description = "Hasło użytkownika (min. 6 znaków, musi zawierać wielką literę i znak specjalny)", example = "Haslo123!", required = true)
    private String password;

    @NotBlank
    @Size(max = 100)
    @Schema(description = "Imię użytkownika", example = "Jan", required = true)
    private String firstName;

    @NotBlank
    @Size(max = 100)
    @Schema(description = "Nazwisko użytkownika", example = "Kowalski", required = true)
    private String lastName;

    @NotBlank
    @Size(max = 20)
    @Schema(description = "Numer telefonu użytkownika", example = "+48123456789", required = true)
    private String phoneNumber;

    public RegisterRequest() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}



