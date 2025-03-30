package com.tkpm.sms.application.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tkpm.sms.application.annotation.TranslateDomainException;
import com.tkpm.sms.application.dto.request.setting.EmailDomainSettingRequestDto;
import com.tkpm.sms.application.dto.request.setting.PhoneSettingRequestDto;
import com.tkpm.sms.application.exception.ApplicationException;
import com.tkpm.sms.domain.exception.ErrorCode;
import com.tkpm.sms.application.exception.ExceptionTranslator;
import com.tkpm.sms.application.service.interfaces.SettingService;
import com.tkpm.sms.domain.model.Setting;
import com.tkpm.sms.domain.exception.DomainException;
import com.tkpm.sms.domain.repository.SettingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SettingServiceImpl implements SettingService {
    static String EMAIL_SETTING = "email";
    static String PHONE_NUMBER_SETTING = "phonenumber";
    static String AT_SIGN = "@";

    ObjectMapper objectMapper;
    SettingRepository settingRepository;
    ExceptionTranslator exceptionTranslator;

    @Override
    public Setting getPhoneSetting() {
        try {
            return settingRepository.findByName(PHONE_NUMBER_SETTING)
                    .orElseThrow(() -> new ApplicationException(
                            ErrorCode.NOT_FOUND.withMessage("Phone setting not found")));
        } catch (DomainException e) {
            throw exceptionTranslator.translateException(e);
        }
    }

    @Override
    public Setting getEmailSetting() {
        try {
            return settingRepository.findByName(EMAIL_SETTING)
                    .orElseThrow(() -> new ApplicationException(
                            ErrorCode.NOT_FOUND.withMessage("Email setting not found")));
        } catch (DomainException e) {
            throw exceptionTranslator.translateException(e);
        }
    }

    @Override
    @Transactional
    public Setting updateEmailSetting(EmailDomainSettingRequestDto settingRequestDto) {
            Setting setting = settingRepository.findByName(EMAIL_SETTING)
                    .orElseThrow(() -> new ApplicationException(
                            ErrorCode.NOT_FOUND.withMessage(
                                    String.format("Setting with name %s not found", EMAIL_SETTING))));

            String domain = settingRequestDto.getDomain();
            if (!domain.contains(AT_SIGN)) {
                domain = AT_SIGN + domain;
            }
            setting.setDetails(domain);

            return settingRepository.save(setting);
    }

    @Override
    @Transactional
    public Setting updatePhoneSetting(PhoneSettingRequestDto phoneSettingRequestDto) {
        Setting setting = settingRepository.findByName(PHONE_NUMBER_SETTING)
                .orElseThrow(() -> new ApplicationException(
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