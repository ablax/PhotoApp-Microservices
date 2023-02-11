package me.ablax.photoapp.api.users.service;

import me.ablax.photoapp.api.users.shared.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsersService extends UserDetailsService {

    UserDto createUser(final UserDto userDetails);

    UserDto getUserDetailsByEmail(final String email);

}
