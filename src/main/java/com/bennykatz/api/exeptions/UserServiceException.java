package com.bennykatz.api.exeptions;

public class UserServiceException extends RuntimeException {

    private static final long serialVersionUID = -828812605183770713L;

    public UserServiceException(String message) {
        super(message);
    }
}
