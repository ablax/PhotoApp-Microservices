package me.ablax.photoapp.api.users.service;

import me.ablax.photoapp.api.users.shared.UserDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UsersServiceTest {

    private static final String firstName = "FName";
    private static final String lastName = "LNAME";
    private static final String email = "email@example.com";
    private static final String password = "123Password!@#";
    @Autowired
    private UsersService usersService;

    @BeforeAll
    void createUser() {
        final UserDto userDto = getUserDto();
        final UserDto user = usersService.createUser(userDto);
        assertNotNull(user);
        assertDefaultUserData(user);
    }

    @Test
    void getUserDetailsByEmail() {
        final UserDto user = usersService.getUserDetailsByEmail(email);
        assertNotNull(user);
        assertDefaultUserData(user);
    }

    @Test
    void loadUserByUsername() {
        final UserDto user = usersService.getUserDetailsByEmail(email);
        assertNotNull(user);
        assertDefaultUserData(user);
    }

    private UserDto getUserDto() {
        final UserDto userDto = new UserDto();
        userDto.setFirstName(firstName);
        userDto.setLastName(lastName);
        userDto.setEmail(email);
        userDto.setPassword(password);
        return userDto;
    }

    private void assertDefaultUserData(final UserDto userDto){
        assertUserData(userDto, getUserDto());
    }
    private void assertUserData(final UserDto userDto, final UserDto compareWith) {
        assertEquals(userDto.getFirstName(), compareWith.getFirstName());
        assertEquals(userDto.getLastName(), compareWith.getLastName());
        assertEquals(userDto.getEmail(), compareWith.getEmail());
        if (userDto.getPassword() != null && compareWith.getPassword() != null) {
            assertEquals(userDto.getPassword(), compareWith.getPassword());
        }
        if (userDto.getEncryptedPassword() != null && compareWith.getEncryptedPassword() != null) {
            assertEquals(userDto.getEncryptedPassword(), compareWith.getEncryptedPassword());
        }
        if (userDto.getUserId() != null && compareWith.getUserId() != null) {
            assertEquals(userDto.getUserId(), compareWith.getUserId());
        }
    }
}