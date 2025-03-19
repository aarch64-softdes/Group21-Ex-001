package com.tkpm.sms.service;

import com.tkpm.sms.dto.request.IdentityCreateRequestDto;
import com.tkpm.sms.dto.request.IdentityUpdateRequestDto;
import com.tkpm.sms.entity.Identity;
import com.tkpm.sms.exceptions.ApplicationException;
import com.tkpm.sms.exceptions.ErrorCode;
import com.tkpm.sms.mapper.IdentityMapper;
import com.tkpm.sms.repository.IdentityRepository;
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

    public Identity createIdentity(IdentityCreateRequestDto identityCreateRequestDto) {
        Identity identity = identityMapper.createIdentity(identityCreateRequestDto);
        return identityRepository.save(identity);
    }

    public Identity updateIdentity(IdentityUpdateRequestDto identityUpdateRequestDto, String id) {
        Identity identity = identityRepository.findById(id).orElseThrow(
            ()-> new ApplicationException(ErrorCode.NOT_FOUND.withMessage("Student identity information not found"))
        );
        identityMapper.updateIdentity(identity, identityUpdateRequestDto);
        return identityRepository.save(identity);
    }
}
