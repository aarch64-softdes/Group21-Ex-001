package com.tkpm.sms.infrastructure.utils;

import com.tkpm.sms.application.dto.request.address.AddressCreateRequestDto;
import com.tkpm.sms.application.dto.request.identity.IdentityCreateRequestDto;
import com.tkpm.sms.application.dto.response.student.StudentFileDto;
import com.tkpm.sms.domain.enums.IdentityType;
import com.tkpm.sms.domain.model.Address;
import com.tkpm.sms.domain.model.Identity;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

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
                .type(IdentityType.valueOf(identityType))
                .number(identityNumber)
                .issuedBy(identityIssuedBy)
                .issuedDate(LocalDate.parse(identityIssuedDate))
                .expiryDate(LocalDate.parse(identityExpiryDate))
                .build();
    }

    public static AddressCreateRequestDto parseAddressCreateRequestDto(String address) {
        if (Objects.isNull(address) || address.isEmpty()) {
            return null;
        }

        String[] parts = address.split(",");
        return AddressCreateRequestDto.builder()
                .street(parts[0])
                .ward(parts[1])
                .district(parts[2])
                .province(parts[2] == null ? "" : parts[2])
                .country(parts[3] == null ? "" : parts[3])
                .build();
    }

    public static IdentityCreateRequestDto parseIdentityCreateRequestDto(
            StudentFileDto studentFileImportDto) {
        log.info("Parsing student file {}", studentFileImportDto.getIdentityIssuedDate());
        return IdentityCreateRequestDto.builder()
                .type(studentFileImportDto.getIdentityType())
                .number(studentFileImportDto.getIdentityNumber())
                .issuedBy(studentFileImportDto.getIdentityIssuedBy())
                .issuedDate(LocalDate.parse(studentFileImportDto.getIdentityIssuedDate()))
                .expiryDate(LocalDate.parse(studentFileImportDto.getIdentityExpiryDate()))
                .notes(studentFileImportDto.getIdentityNotes())
                .country(studentFileImportDto.getIdentityCountry())
                .hasChip(IdentityType.CHIP_CARD.equals(
                        IdentityType.fromDisplayName(studentFileImportDto.getIdentityType())
                ))
                .build();
    }

    private static String formalizeString(String str) {
        return str.trim().replace(" ", "");
    }
}
