package com.tkpm.sms.application.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tkpm.sms.application.dto.request.setting.EmailDomainSettingRequestDto;
import com.tkpm.sms.application.dto.request.setting.PhoneSettingRequestDto;
import com.tkpm.sms.application.dto.response.setting.EmailDomainSettingDto;
import com.tkpm.sms.application.dto.response.setting.PhoneSettingDto;
import com.tkpm.sms.application.service.interfaces.SettingService;
import com.tkpm.sms.domain.enums.SettingType;
import com.tkpm.sms.domain.exception.ErrorCode;
import com.tkpm.sms.domain.exception.GenericDomainException;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.model.Setting;
import com.tkpm.sms.domain.repository.SettingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SettingServiceImpl implements SettingService {
    private static final String AT_SIGN = "@";

    ObjectMapper objectMapper;
    SettingRepository settingRepository;

    @Override
    public PhoneSettingDto getPhoneSetting() {
        return new PhoneSettingDto(settingRepository.getPhoneSetting());
    }

    @Override
    public EmailDomainSettingDto getEmailSetting() {
        return new EmailDomainSettingDto(settingRepository.getEmailSetting());
    }

    @Override
    @Transactional
    public EmailDomainSettingDto updateEmailSetting(EmailDomainSettingRequestDto settingRequestDto) {
        Setting setting = settingRepository.findByName(SettingType.EMAIL.getValue())
                .orElseThrow(() -> new ResourceNotFoundException("Email setting not found"));

        String domain = settingRequestDto.getDomain();
        if (!domain.contains(AT_SIGN)) {
            domain = AT_SIGN + domain;
        }
        setting.setDetails(domain);

        var savedSetting = settingRepository.save(setting);
        return  new EmailDomainSettingDto(savedSetting.getDetails());
    }

    @Override
    @Transactional
    public PhoneSettingDto updatePhoneSetting(PhoneSettingRequestDto phoneSettingRequestDto) {
        Setting setting = settingRepository.findByName(SettingType.PHONE_NUMBER.getValue())
                .orElseThrow(() -> new ResourceNotFoundException("Phone setting not found"));

        try {
            String details = objectMapper.writeValueAsString(phoneSettingRequestDto.getSupportedCountryCodes());
            setting.setDetails(details);
            var savedSetting = settingRepository.save(setting);

            return fromDomainModel(savedSetting);
        } catch (JsonProcessingException e) {
            throw new GenericDomainException("Error processing phone setting", ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private PhoneSettingDto fromDomainModel(Setting setting) {
        try {
            List<String> supportedCountryCodes = objectMapper.readValue(setting.getDetails(), List.class);
            return new PhoneSettingDto(supportedCountryCodes);
        } catch (JsonProcessingException e) {
            throw new GenericDomainException("Error processing phone setting", ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}