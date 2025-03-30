package com.tkpm.sms.application.service.interfaces;

import com.tkpm.sms.application.dto.request.setting.EmailDomainSettingRequestDto;
import com.tkpm.sms.application.dto.request.setting.PhoneSettingRequestDto;
import com.tkpm.sms.domain.model.Setting;

public interface SettingService {
    Setting getPhoneSetting();
    Setting getEmailSetting();
    Setting updateEmailSetting(EmailDomainSettingRequestDto emailSettingRequestDto);
    Setting updatePhoneSetting(PhoneSettingRequestDto phoneSettingRequestDto);
}