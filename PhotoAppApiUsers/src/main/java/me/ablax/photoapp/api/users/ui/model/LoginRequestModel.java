package me.ablax.photoapp.api.users.ui.model;

import lombok.Data;

@Data
public class LoginRequestModel {

    private String password;
    private String email;
}
