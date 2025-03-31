package com.tkpm.sms.application.service.interfaces;

import com.tkpm.sms.application.dto.request.setting.EmailDomainSettingRequestDto;
import com.tkpm.sms.application.dto.request.setting.PhoneSettingRequestDto;
import com.tkpm.sms.application.dto.response.setting.EmailDomainSettingDto;
import com.tkpm.sms.application.dto.response.setting.PhoneSettingDto;
import com.tkpm.sms.domain.model.Setting;

import java.util.List;

public interface SettingService {
    PhoneSettingDto getPhoneSetting();

    EmailDomainSettingDto getEmailSetting();

    EmailDomainSettingDto updateEmailSetting(EmailDomainSettingRequestDto emailSettingRequestDto);

    PhoneSettingDto updatePhoneSetting(PhoneSettingRequestDto phoneSettingRequestDto);
}