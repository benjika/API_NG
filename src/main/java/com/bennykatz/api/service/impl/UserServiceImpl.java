package com.bennykatz.api.service.impl;

import com.bennykatz.api.UserRepository;
import com.bennykatz.api.io.entity.UserEntity;
import com.bennykatz.api.service.UserService;
import com.bennykatz.api.shared.Utils;
import com.bennykatz.api.shared.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    Utils utils;

    @Override
    public UserDto createUser(UserDto userDto) {

        UserEntity storedUserDetails = userRepository.findByEmail(userDto.getEmail());

        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            throw new RuntimeException("Record already exists");
        }

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDto, userEntity);

        userEntity.setEncryptedPassword("test");
        userEntity.setUserId(utils.generateUserId(30));

        UserEntity storedUserEntity = userRepository.save(userEntity);

        UserDto returnedValue = new UserDto();
        BeanUtils.copyProperties(storedUserEntity, returnedValue);

        return returnedValue;
    }
}
