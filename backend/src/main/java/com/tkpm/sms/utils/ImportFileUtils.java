package com.tkpm.sms.utils;

import com.tkpm.sms.dto.request.AddressCreateRequestDto;
import com.tkpm.sms.dto.request.IdentityCreateRequestDto;
import com.tkpm.sms.entity.Address;
import com.tkpm.sms.entity.Identity;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public class ImportFileUtils {
    public static Address parseAddress(String address) {
        address = formalizeString(address);
        log.debug("Parsing address {}", address);
        String[] parts = address.split(",");
        return Address.builder()
                .street(parts[0])
                .ward(parts[1])
                .district(parts[2])
                .country(parts[3])
                .build();
    }

    public static Identity parseIdentity(String identityType,
                                         String identityNumber,
                                         String identityIssuedBy,
                                         String identityIssuedDate,
                                         String identityExpiryDate) {
        return Identity.builder()
                .type(identityType)
                .number(identityNumber)
                .issuedBy(identityIssuedBy)
                .issuedDate(LocalDate.parse(identityIssuedDate))
                .expiryDate(LocalDate.parse(identityExpiryDate))
                .build();
    }

    public static AddressCreateRequestDto parseAddressCreateRequestDto(String address) {
        log.debug("Parsing address {}", address);
        String[] parts = address.split(",");
        return AddressCreateRequestDto.builder()
                .street(parts[0])
                .ward(parts[1])
                .district(parts[2])
                .country(parts[3])
                .build();
    }

    public static IdentityCreateRequestDto parseIdentityCreateRequestDto(String identityType,
                                                         String identityNumber,
                                                         String identityIssuedBy,
                                                         String identityIssuedDate,
                                                         String identityExpiryDate) {
        return IdentityCreateRequestDto.builder()
                .type(identityType)
                .number(identityNumber)
                .issuedBy(identityIssuedBy)
                .issuedDate(LocalDate.parse(identityIssuedDate))
                .expiryDate(LocalDate.parse(identityExpiryDate))
                .build();
    }

    private static String formalizeString(String str) {
        return str.trim();
    }
}
