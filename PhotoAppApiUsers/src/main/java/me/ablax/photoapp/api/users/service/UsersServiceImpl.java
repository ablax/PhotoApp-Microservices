package me.ablax.photoapp.api.users.service;

import me.ablax.photoapp.api.users.data.AlbumsServiceClient;
import me.ablax.photoapp.api.users.data.UserEntity;
import me.ablax.photoapp.api.users.data.UsersRepository;
import me.ablax.photoapp.api.users.shared.UserDto;
import me.ablax.photoapp.api.users.ui.exceptions.UsersServiceException;
import me.ablax.photoapp.api.users.ui.model.AlbumResponseModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UsersServiceImpl implements UsersService {

    private final ModelMapper modelMapper;
    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Environment environment;
    private final AlbumsServiceClient albumsServiceClient;

    @Autowired
    public UsersServiceImpl(final UsersRepository usersRepository, final BCryptPasswordEncoder bCryptPasswordEncoder, final Environment environment, final AlbumsServiceClient albumsServiceClient) {
        this.usersRepository = usersRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.environment = environment;
        this.albumsServiceClient = albumsServiceClient;
        this.modelMapper = new ModelMapper();
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Override
    public UserDto createUser(final UserDto userDetails) {

        userDetails.setUserId(UUID.randomUUID().toString());
        userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));

        UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);

        usersRepository.save(userEntity);
        return getUserDto(userEntity,null);
    }

    @Override
    public UserDto getUserDetailsByEmail(final String email) {

        final UserEntity userEntity = usersRepository.findByEmail(email);
        return getUserDto(userEntity, new UsernameNotFoundException(email));
    }

    @Override
    public UserDto getUserByUserId(final String userId) {

        final UserEntity userEntity = usersRepository.findByUserId(userId);

        final UserDto userDto = getUserDto(userEntity, new UsersServiceException("User not found"));
        final List<AlbumResponseModel> albums = albumsServiceClient.getAlbums(userId);

        userDto.setAlbums(albums);

        return userDto;
    }

    private UserDto getUserDto(final UserEntity userEntity, final RuntimeException exception) {
        if(userEntity == null) {
            throw exception;
        }
        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final UserEntity userEntity = usersRepository.findByEmail(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException(username);
        }

        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), true, true, true, true, new ArrayList<>());
    }
}
