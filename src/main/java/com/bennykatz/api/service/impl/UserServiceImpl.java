package com.bennykatz.api.service.impl;

import com.bennykatz.api.exeptions.UserServiceException;
import com.bennykatz.api.io.entity.UserEntity;
import com.bennykatz.api.io.repositories.UserRepository;
import com.bennykatz.api.service.UserService;
import com.bennykatz.api.shared.Utils;
import com.bennykatz.api.shared.dto.AddressDto;
import com.bennykatz.api.shared.dto.UserDto;
import com.bennykatz.api.ui.model.response.ErrorMessagesEnum;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    Utils utils;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(UserDto userDto) {

        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            throw new RuntimeException("Record already exists");
        }

        int addressesSize = userDto.getAddresses().size();

        ExecutorService executorService = Executors.newFixedThreadPool(addressesSize + 2);
        Future<String> userIdFuture = executorService.submit(new UserIdGenerator());
        Future<String> encryptedPasswordFuture = executorService.submit(new PasswordEncrypter(userDto.getPassword()));
        List<Future<String>> addressesIds = new ArrayList<>();
        for (int i = 0; i < addressesSize; i++) {
            addressesIds.add(executorService.submit(new AddressIdGenerator()));
        }

        if (addressesSize > 0) {

            for (int i = 0; i < addressesSize; i++) {
                AddressDto addressDto = userDto.getAddresses().get(i);
                addressDto.setUserDetails(userDto);
                String addressId = null;
                try {
                    addressId = addressesIds.get(i).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                addressDto.setAddressId(addressId);
                userDto.getAddresses().set(i, addressDto);
            }
        }
        ModelMapper modelMapper = new ModelMapper();

        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);

        String userId = null;
        String encryptedPassword = null;
        try {
            userId = userIdFuture.get();
            encryptedPassword = encryptedPasswordFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        executorService.shutdown();

        userEntity.setUserId(userId);
        userEntity.setEncryptedPassword(encryptedPassword);


        UserEntity storedUserEntity = userRepository.save(userEntity);

        UserDto returnedValue = new UserDto();
        BeanUtils.copyProperties(storedUserEntity, returnedValue);

        return returnedValue;
    }

    @Override
    public UserDto getUser(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }

        UserDto returnedValue = new UserDto();
        BeanUtils.copyProperties(userEntity, returnedValue);
        return returnedValue;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserDto returnedValue = new UserDto();
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null) {
            throw new UsernameNotFoundException(userId);
        }

        BeanUtils.copyProperties(userEntity, returnedValue);

        return returnedValue;
    }

    @Override
    public List<UserDto> getUsersList(int page, int limit) {
        List<UserDto> returnedValue = new ArrayList<>();

        Pageable pageable = PageRequest.of(page, limit);
        Page<UserEntity> userEntityPage = userRepository.findAll(pageable);

        List<UserEntity> userEntities = userEntityPage.getContent();

        for (UserEntity userEntity : userEntities) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(userEntity, userDto);
            returnedValue.add(userDto);
        }

        return returnedValue;
    }

    @Override
    public UserDto updateUser(String userId, UserDto userDto) {
        UserDto returnedValue = new UserDto();
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null) {
            throw new UserServiceException(ErrorMessagesEnum.NO_RECORD_FOUND.getErrorMessage());
        }

        if (userDto.getFirstName() != null) {
            userEntity.setFirstName(userDto.getFirstName());
        }
        if (userDto.getLastName() != null) {
            userEntity.setLastName(userDto.getLastName());
        }

        UserEntity updatedUserEntity = userRepository.save(userEntity);
        BeanUtils.copyProperties(updatedUserEntity, returnedValue);

        return returnedValue;
    }

    @Override
    public void deleteUser(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null) {
            throw new UserServiceException(ErrorMessagesEnum.NO_RECORD_FOUND.getErrorMessage());
        }

        userRepository.delete(userEntity);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null) {
            throw new UserServiceException(ErrorMessagesEnum.NO_RECORD_FOUND.getErrorMessage());
        }

        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }

}

class AddressIdGenerator implements java.util.concurrent.Callable<String> {
    @Override
    public String call() throws Exception {
        return new Utils().generateAddressId(30);
    }
}

class UserIdGenerator implements java.util.concurrent.Callable<String> {
    @Override
    public String call() throws Exception {
        return new Utils().generateUserId(30);
    }
}

class PasswordEncrypter implements java.util.concurrent.Callable<String> {

    private String password;

    public PasswordEncrypter(String password) {
        this.password = password;
    }

    @Override
    public String call() throws Exception {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(password);
    }
}