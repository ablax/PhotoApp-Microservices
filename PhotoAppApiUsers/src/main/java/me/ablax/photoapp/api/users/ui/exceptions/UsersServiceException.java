package me.ablax.photoapp.api.users.ui.exceptions;

public class UsersServiceException extends RuntimeException {
    public UsersServiceException(final String errorMessage) {
        super(errorMessage);
    }
}
