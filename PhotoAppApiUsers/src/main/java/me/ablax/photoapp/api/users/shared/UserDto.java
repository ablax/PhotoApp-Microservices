package me.ablax.photoapp.api.users.shared;

import lombok.Data;
import me.ablax.photoapp.api.users.ui.model.AlbumResponseModel;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class UserDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -111111111111L;

    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String encryptedPassword;
    private List<AlbumResponseModel> albums;
}
