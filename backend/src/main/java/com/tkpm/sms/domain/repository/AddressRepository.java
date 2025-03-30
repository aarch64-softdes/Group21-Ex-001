package com.tkpm.sms.domain.repository;

import com.tkpm.sms.domain.model.Address;

import java.util.Optional;

public interface AddressRepository {
    Address save(Address address);
    Optional<Address> findById(String id);
}