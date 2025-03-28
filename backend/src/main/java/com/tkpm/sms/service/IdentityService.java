package com.tkpm.sms.service;

import com.tkpm.sms.dto.request.identity.IdentityCreateRequestDto;
import com.tkpm.sms.dto.request.identity.IdentityUpdateRequestDto;
import com.tkpm.sms.entity.Identity;
import com.tkpm.sms.exceptions.ApplicationException;
import com.tkpm.sms.exceptions.ErrorCode;
import com.tkpm.sms.mapper.IdentityMapper;
import com.tkpm.sms.repository.IdentityRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityService {
    IdentityMapper identityMapper;
    IdentityRepository identityRepository;

    @Transactional
    public Identity createIdentity(IdentityCreateRequestDto identityCreateRequestDto) {
        if (identityRepository.existsIdentityByNumberAndType(identityCreateRequestDto.getNumber(), identityCreateRequestDto.getType())) {
            throw new ApplicationException(ErrorCode.CONFLICT.withMessage(
                    String.format(
                            "Student with the %s and number %s already existed",
                            identityCreateRequestDto.getType(),
                            identityCreateRequestDto.getNumber())));
        }

        Identity identity = identityMapper.createIdentity(identityCreateRequestDto);

        return identityRepository.save(identity);
    }

    @Transactional
    public Identity updateIdentity(IdentityUpdateRequestDto identityUpdateRequestDto, String id) {
        Identity identity = identityRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(
                        ErrorCode.NOT_FOUND.withMessage("Student identity information not found")));

        if (identityRepository.existsIdentityByNumberAndTypeAndIdNot(identityUpdateRequestDto.getNumber(), identityUpdateRequestDto.getType(), id)) {
            throw new ApplicationException(
                    ErrorCode.CONFLICT.withMessage(
                            String.format(
                                    "Student with the %s and number %s already existed",
                                    identityUpdateRequestDto.getType(),
                                    identityUpdateRequestDto.getNumber())));
        }
        identityMapper.updateIdentity(identity, identityUpdateRequestDto);

        return identityRepository.save(identity);
    }
}
