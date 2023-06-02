package me.ablax.photoapp.api.users.ui.controller;

import me.ablax.photoapp.api.users.service.UsersService;
import me.ablax.photoapp.api.users.shared.UserDto;
import me.ablax.photoapp.api.users.ui.model.CreateUserRequestModel;
import me.ablax.photoapp.api.users.ui.model.CreateUserResponseModel;
import me.ablax.photoapp.api.users.ui.model.UserResponseModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Environment environment;
    private final UsersService usersService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(final Environment environment, final UsersService usersService) {
        this.environment = environment;
        this.usersService = usersService;
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @GetMapping("status/check")
    public String status() {
        return "Working on port " + environment.getProperty("local.server.port");
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<CreateUserResponseModel> createUser(@RequestBody @Valid final CreateUserRequestModel userDetails) {

        final UserDto userDto = modelMapper.map(userDetails, UserDto.class);
        final UserDto user = usersService.createUser(userDto);
        final CreateUserResponseModel responseModel = modelMapper.map(user, CreateUserResponseModel.class);

        return new ResponseEntity<>(responseModel, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{userId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<UserResponseModel> getUser(@PathVariable final String userId) {

        final UserDto userDto = usersService.getUserByUserId(userId);

        final UserResponseModel returnValue = modelMapper.map(userDto, UserResponseModel.class);

        return ResponseEntity.ok(returnValue);
    }

}
