package com.tkpm.sms.presentation.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tkpm.sms.application.dto.request.setting.EmailDomainSettingRequestDto;
import com.tkpm.sms.application.dto.request.setting.PhoneSettingRequestDto;
import com.tkpm.sms.application.dto.response.common.ApplicationResponseDto;
import com.tkpm.sms.application.dto.response.setting.EmailDomainSettingDto;
import com.tkpm.sms.application.dto.response.setting.PhoneSettingDto;
import com.tkpm.sms.application.exception.ApplicationException;
import com.tkpm.sms.domain.exception.ErrorCode;
import com.tkpm.sms.application.service.interfaces.SettingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/settings")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SettingController {
    SettingService settingService;
    ObjectMapper objectMapper;
    static String PHONE_NUMBER_SETTING = "phone number";

    @GetMapping("/phone-number")
    public ResponseEntity<ApplicationResponseDto<PhoneSettingDto>> getPhoneSetting() {
        var setting = settingService.getPhoneSetting();

        try {
            List<String> details = objectMapper.readValue(setting.getDetails(), new TypeReference<>() {});
            var phoneSettingDto = new PhoneSettingDto(details);
            phoneSettingDto.setSettingName(PHONE_NUMBER_SETTING);
            var response = ApplicationResponseDto.success(phoneSettingDto);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new ApplicationException(
                    ErrorCode.INVALID_PHONE_SETTING_DETAILS.
                            withMessage("Invalid phone setting details"));
        }
    }

    @GetMapping("/email")
    public ResponseEntity<ApplicationResponseDto<EmailDomainSettingDto>> getEmailSetting() {
        var setting = settingService.getEmailSetting();

        var emailDomainSettingDto = EmailDomainSettingDto.builder()
                .settingName(setting.getName())
                .domain(setting.getDetails())
                .build();

        var response = ApplicationResponseDto.success(emailDomainSettingDto);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/email")
    public ResponseEntity<ApplicationResponseDto<EmailDomainSettingDto>> updateEmailDomainSetting(
            @RequestBody EmailDomainSettingRequestDto emailSettingRequestDto
    ) {
        var updatedSetting = settingService.updateEmailSetting(emailSettingRequestDto);
        log.info("Updated setting: name: {} details: {}", updatedSetting.getName(), updatedSetting.getDetails());

        var emailDomainSettingDto = EmailDomainSettingDto.builder()
                .settingName(updatedSetting.getName())
                .domain(updatedSetting.getDetails())
                .build();

        ApplicationResponseDto<EmailDomainSettingDto> response = ApplicationResponseDto.success(emailDomainSettingDto);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/phone-number")
    public ResponseEntity<ApplicationResponseDto<PhoneSettingDto>> updatePhoneSetting(
            @RequestBody PhoneSettingRequestDto phoneSettingRequestDto
    ) {
        var updatedSetting = settingService.updatePhoneSetting(phoneSettingRequestDto);

        try {
            List<String> details = objectMapper.readValue(updatedSetting.getDetails(), new TypeReference<>() {});
            var phoneSettingDto = new PhoneSettingDto(details);
            phoneSettingDto.setSettingName(PHONE_NUMBER_SETTING);
            var response = ApplicationResponseDto.success(phoneSettingDto);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new ApplicationException(
                    ErrorCode.INVALID_PHONE_SETTING_DETAILS.
                            withMessage("Invalid phone setting details"));
        }
    }
}