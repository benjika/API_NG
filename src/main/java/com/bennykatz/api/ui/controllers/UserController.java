package com.bennykatz.api.ui.controllers;

import com.bennykatz.api.exeptions.UserServiceException;
import com.bennykatz.api.service.AddressService;
import com.bennykatz.api.service.UserService;
import com.bennykatz.api.shared.dto.AddressDto;
import com.bennykatz.api.shared.dto.UserDto;
import com.bennykatz.api.ui.model.request.UserRequestModel;
import com.bennykatz.api.ui.model.response.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AddressService addressService;

    @PostMapping(
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest createUser(@RequestBody UserRequestModel userDetails) throws Exception {
        UserRest returnedValue = new UserRest();

        if (userDetails.getFirstName().isEmpty() || userDetails.getLastName().isEmpty() ||
                userDetails.getPassword().isEmpty() || userDetails.getEmail().isEmpty()) {
            throw new UserServiceException(ErrorMessagesEnum.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

        UserDto userDto = new ModelMapper().map(userDetails, UserDto.class);

        UserDto createdUser = userService.createUser(userDto);
        returnedValue = new ModelMapper().map(createdUser, UserRest.class);

        return returnedValue;
    }

    @GetMapping(path = "/{id}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest getUser(@PathVariable String id) {

        UserDto userDto = userService.getUserByUserId(id);

        UserRest returnedValue = new ModelMapper().map(userDto, UserRest.class);

        return returnedValue;
    }

    @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public List<UserRest> getUsersList(@RequestParam(value = "page", defaultValue = "0") int page,
                                       @RequestParam(value = "limit", defaultValue = "2") int limit) {
        List<UserRest> returnedValue = new ArrayList<>();
        List<UserDto> userDtoList = userService.getUsersList(page, limit);
        ModelMapper modelMapper = new ModelMapper();

        for (UserDto userDto : userDtoList) {
            UserRest userRest = modelMapper.map(userDto, UserRest.class);
            //BeanUtils.copyProperties(userDto, userRest);
            returnedValue.add(userRest);
        }

        return returnedValue;
    }


    @PutMapping(path = "/{id}",
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest updateUser(@PathVariable String id, @RequestBody UserRequestModel userDetails) {

        ModelMapper modelMapper = new ModelMapper();

        UserDto userDto = modelMapper.map(userDetails, UserDto.class);
        //BeanUtils.copyProperties(userDetails, userDto);

        UserDto updatedUser = userService.updateUser(id, userDto);
        UserRest returnedValue = modelMapper.map(updatedUser, UserRest.class);
        //BeanUtils.copyProperties(updatedUser, returnedValue);

        return returnedValue;
    }

    @DeleteMapping(path = "/{id}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public OperationStatusModel deleteUser(@PathVariable String id) {
        OperationStatusModel operationStatusModel = new OperationStatusModel();
        operationStatusModel.setOperationName(RequestOperationName.DELETE.name());

        userService.deleteUser(id);

        operationStatusModel.setOperationResult(RequestOperationStatus.SUCCESS.name());
        return operationStatusModel;
    }

    @GetMapping(path = "/{id}/addresses",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public List<AddressRest> getAddressesList(@PathVariable String id) {

        List<AddressRest> returnedValue = new ArrayList<>();

        List<AddressDto> addressDtoList = addressService.getAddressesList(id);

        if (addressDtoList != null && !addressDtoList.isEmpty()) {
            Type listType = new TypeToken<List<AddressRest>>() {
            }.getType();
            returnedValue = new ModelMapper().map(addressDtoList, listType);
        }

        return returnedValue;
    }

    @GetMapping(path = "/{userId}/addresses/{addressId}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public AddressRest getAddress(@PathVariable String addressId) {

        AddressRest returnedValue = new AddressRest();

        AddressDto addressDto = addressService.getAddress(addressId);

        if (addressDto == null) {
            throw new UserServiceException(ErrorMessagesEnum.ADDRESS_NOT_FOUND.getErrorMessage());
        }

        return new ModelMapper().map(addressDto, AddressRest.class);
    }
}