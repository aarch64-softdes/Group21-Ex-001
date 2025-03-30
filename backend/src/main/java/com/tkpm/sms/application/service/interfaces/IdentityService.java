package com.tkpm.sms.application.service.interfaces;

import com.tkpm.sms.application.dto.request.identity.IdentityCreateRequestDto;
import com.tkpm.sms.application.dto.request.identity.IdentityUpdateRequestDto;
import com.tkpm.sms.domain.model.Identity;

public interface IdentityService {
    Identity createIdentity(IdentityCreateRequestDto identityCreateRequestDto);
    Identity updateIdentity(String id, IdentityUpdateRequestDto identityUpdateRequestDto);
    boolean canCreateIdentity(Identity identity);
}