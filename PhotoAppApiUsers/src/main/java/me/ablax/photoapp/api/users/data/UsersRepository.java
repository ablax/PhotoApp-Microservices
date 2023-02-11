package me.ablax.photoapp.api.users.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);

}
