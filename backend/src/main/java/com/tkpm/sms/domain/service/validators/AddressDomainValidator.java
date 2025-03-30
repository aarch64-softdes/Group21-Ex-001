package com.tkpm.sms.domain.service.validators;

import com.tkpm.sms.domain.model.Address;
import com.tkpm.sms.domain.exception.InvalidAddressException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressDomainValidator {
    public void validateAddressFields(Address address) {
        if ((address.getStreet() == null || address.getStreet().isBlank()) &&
            (address.getDistrict() == null || address.getDistrict().isBlank()) &&
            (address.getProvince() == null || address.getProvince().isBlank()) &&
            (address.getCountry() == null || address.getCountry().isBlank())) {
            throw new InvalidAddressException("Address must have at least one component specified");
        }
    }

    // Check for logical consistency in address hierarchy
    // For example, if ward is specified, district should also be specified
    public void validateAddressConsistency(Address address) {
        if (address.getWard() != null && !address.getWard().isBlank() &&
            (address.getDistrict() == null || address.getDistrict().isBlank())) {
            throw new InvalidAddressException("Ward cannot be specified without a district");
        }
    }
}