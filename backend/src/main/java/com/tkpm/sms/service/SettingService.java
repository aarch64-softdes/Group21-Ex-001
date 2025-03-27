package com.tkpm.sms.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    SettingRepository settingRepository;

    public Setting getPhoneSetting(){
        return settingRepository.findSettingByName(PHONE_NUMBER_SETTING).orElseThrow(
                () -> new ApplicationException(
                        ErrorCode.NOT_FOUND.withMessage("Phone setting not found")));

    }

    @Transactional
    public Setting updateEmailSetting(SettingRequestDto settingRequestDto) {
        var setting = settingRepository.findSettingByName(EMAIL_SETTING).orElseThrow(
                () -> new ApplicationException(
                        ErrorCode.NOT_FOUND.withMessage(
                                String.format("Setting with name %s not found", EMAIL_SETTING))));

        settingMapper.updateSetting(setting, settingRequestDto);
        setting.setDetails(settingRequestDto.getDetails());
        return settingRepository.save(setting);
    }

    @Transactional
    public Setting updatePhoneSetting(PhoneSettingRequestDto phoneSettingRequestDto) {
        var setting = settingRepository.findSettingByName(PHONE_NUMBER_SETTING).orElseThrow(
                () -> new ApplicationException(
                        ErrorCode.NOT_FOUND.withMessage("Phone setting not found")));

        ObjectMapper objectMapper = new ObjectMapper();
        try{
            String details = objectMapper.writeValueAsString(phoneSettingRequestDto.getDetails());
            setting.setDetails(details);
            return settingRepository.save(setting);

        } catch (JsonProcessingException e) {
            throw new ApplicationException(
                    ErrorCode.INVALID_PHONE_SETTING_DETAILS.
                            withMessage("Invalid phone setting details"));
        }

    }
}
