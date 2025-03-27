package com.tkpm.sms.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tkpm.sms.dto.request.setting.EmailDomainSettingRequestDto;
import com.tkpm.sms.dto.request.setting.PhoneSettingRequestDto;
import com.tkpm.sms.entity.Setting;
import com.tkpm.sms.exceptions.ApplicationException;
import com.tkpm.sms.exceptions.ErrorCode;
import com.tkpm.sms.repository.SettingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SettingService {
    static String EMAIL_SETTING = "email";
    static String PHONE_NUMBER_SETTING = "phonenumber";

    ObjectMapper objectMapper;
    SettingRepository settingRepository;

    public Setting getPhoneSetting() {
        return settingRepository.findSettingByName(PHONE_NUMBER_SETTING).orElseThrow(
                () -> new ApplicationException(
                        ErrorCode.NOT_FOUND.withMessage("Phone setting not found")));

    }

    public Setting getEmailSetting() {
        return settingRepository.findSettingByName(EMAIL_SETTING).orElseThrow(
                () -> new ApplicationException(
                        ErrorCode.NOT_FOUND.withMessage("Phone setting not found")));

    }

    @Transactional
    public Setting updateEmailSetting(EmailDomainSettingRequestDto settingRequestDto) {
        var setting = settingRepository.findSettingByName(EMAIL_SETTING).orElseThrow(
                () -> new ApplicationException(
                        ErrorCode.NOT_FOUND.withMessage(
                                String.format("Setting with name %s not found", EMAIL_SETTING))));

        setting.setDetails(settingRequestDto.getDomain());

        return settingRepository.save(setting);
    }

    @Transactional
    public Setting updatePhoneSetting(PhoneSettingRequestDto phoneSettingRequestDto) {
        var setting = settingRepository.findSettingByName(PHONE_NUMBER_SETTING).orElseThrow(
                () -> new ApplicationException(
                        ErrorCode.NOT_FOUND.withMessage("Phone setting not found")));
        try {
            String details = objectMapper.writeValueAsString(phoneSettingRequestDto.getSupportedCountryCodes());
            setting.setDetails(details);
            return settingRepository.save(setting);

        } catch (JsonProcessingException e) {
            throw new ApplicationException(
                    ErrorCode.INVALID_PHONE_SETTING_DETAILS.
                            withMessage("Invalid phone setting details"));
        }
    }
}
