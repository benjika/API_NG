package com.bennykatz.api.service.impl;

import com.bennykatz.api.io.entity.AddressEntity;
import com.bennykatz.api.io.entity.UserEntity;
import com.bennykatz.api.io.repositories.AddressRepository;
import com.bennykatz.api.io.repositories.UserRepository;
import com.bennykatz.api.service.AddressService;
import com.bennykatz.api.shared.dto.AddressDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;

    @Override
    public List<AddressDto> getAddressesList(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        List<AddressDto> returnedValue = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();

        if (userEntity == null) {
            throw new UsernameNotFoundException(userId);
        }

        Iterable<AddressEntity> addressEntities = addressRepository.findAllByUserDetails(userEntity);
        for (AddressEntity addressEntity : addressEntities) {
            returnedValue.add(modelMapper.map(addressEntity, AddressDto.class));
        }
        return returnedValue;
    }

    @Override
    public AddressDto getAddress(String addressId) {

        AddressEntity addressEntity = addressRepository.findByAddressId(addressId);

        if (addressEntity != null) {
            return new ModelMapper().map(addressEntity, AddressDto.class);
        }

        return null;
    }
}
