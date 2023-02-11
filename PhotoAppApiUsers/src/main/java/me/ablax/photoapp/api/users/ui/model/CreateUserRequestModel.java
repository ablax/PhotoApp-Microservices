package me.ablax.photoapp.api.users.ui.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CreateUserRequestModel {
    @NotNull(message = "First name cannot be empty!")
    @Size(min = 2, message = "First name must be at least 2 characters!")
    private String firstName;
    @NotNull(message = "Last name cannot be empty!")
    @Size(min = 2, message = "Last name must be at least 2 characters!")
    private String lastName;
    @NotNull(message = "Password cannot be empty!")
    @Size(min = 8, max = 40, message = "Password must be at least 8 and at most 40 characters!")
    private String password;
    @NotNull(message = "Email cannot be empty!")
    @Email(message = "Email must be a valid email!")
    private String email;
}
