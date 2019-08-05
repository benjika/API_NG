package com.bennykatz.api.exeptions;

public class UserServiceExeption extends RuntimeException {

    private static final long serialVersionUID = -828812605183770713L;

    public UserServiceExeption(String message) {
        super(message);
    }
}
