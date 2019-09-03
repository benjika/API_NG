package com.bennykatz.api.service;

import com.bennykatz.api.shared.dto.AddressDto;

import java.util.List;

public interface AddressService {
    List<AddressDto> getAddressesList(String userId);
    AddressDto getAddress(String addressId);
}
