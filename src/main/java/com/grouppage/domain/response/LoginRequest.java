package com.grouppage.domain.response;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class LoginRequest {
    @NotBlank(message = "Email nie może być pusty")
    @NotNull(message = "Email nie może być pusty")
    @Email(message = "Wprowadź poprawny email")
    private String email;

    @NotBlank(message = "Hasło nie może być puste")
    @NotNull(message = "Hasło nie może być puste")
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(@NotBlank(message = "Email nie może być pusty") String email,
                        @NotBlank(message = "Hasło nie może być puste") String password) {
        this.email = email;
        this.password = password;
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
}

