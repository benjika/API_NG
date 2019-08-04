package com.bennykatz.api.service;

import com.bennykatz.api.shared.dto.UserDto;

public interface UserService {

    UserDto createUser(UserDto userDto);
}
