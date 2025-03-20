package com.tkpm.sms.utils;

import com.tkpm.sms.dto.request.AddressCreateRequestDto;
import com.tkpm.sms.dto.request.IdentityCreateRequestDto;
import com.tkpm.sms.entity.Address;
import com.tkpm.sms.entity.Identity;
import com.tkpm.sms.enums.IdentityType;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.Arrays;

@Slf4j
public class ImportFileUtils {
    public static Address parseAddress(String address) {
        address = formalizeString(address);
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
        if (address == null || address.isEmpty()) {
            return null;
        }

        address = formalizeString(address);
        log.info("Parsing address {}", address);
        String[] parts = address.split(",");
        log.info("Address parts {}", Arrays.asList(parts));
        return AddressCreateRequestDto.builder()
                .street(parts[0])
                .ward(parts[1])
                .district(parts[2])
                .country(parts[3])
                .build();
    }

    public static IdentityCreateRequestDto parseIdentityCreateRequestDto(
            String identityType,
            String identityNumber,
            String identityIssuedBy,
            String identityIssuedDate,
            String identityExpiryDate,
            String identityNotes,
            String identityCountry) {
        return IdentityCreateRequestDto.builder()
                .type(identityType)
                .number(identityNumber)
                .issuedBy(identityIssuedBy)
                .issuedDate(LocalDate.parse(identityIssuedDate))
                .expiryDate(LocalDate.parse(identityExpiryDate))
                .notes(identityNotes)
                .country(identityCountry)
                .hasChip(IdentityType.Chip_Card.equals(identityType))
                .build();
    }

    private static String formalizeString(String str) {
        return str.trim().replace(" ", "");
    }
}
