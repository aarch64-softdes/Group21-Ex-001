package com.tkpm.sms.presentation.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tkpm.sms.application.dto.request.setting.EmailDomainSettingRequestDto;
import com.tkpm.sms.application.dto.request.setting.PhoneSettingRequestDto;
import com.tkpm.sms.application.dto.response.common.ApplicationResponseDto;
import com.tkpm.sms.application.dto.response.setting.EmailDomainSettingDto;
import com.tkpm.sms.application.dto.response.setting.PhoneSettingDto;
import com.tkpm.sms.application.service.interfaces.SettingService;
import com.tkpm.sms.domain.exception.ErrorCode;
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

    @GetMapping("/phone-number")
    public ResponseEntity<ApplicationResponseDto<PhoneSettingDto>> getPhoneSetting() {
        var setting = settingService.getPhoneSetting();
        return ResponseEntity.ok(ApplicationResponseDto.success(setting));
    }

    @GetMapping("/email")
    public ResponseEntity<ApplicationResponseDto<EmailDomainSettingDto>> getEmailSetting() {
        var setting = settingService.getEmailSetting();
        return ResponseEntity.ok(ApplicationResponseDto.success(setting));
    }

    @PutMapping("/email")
    public ResponseEntity<ApplicationResponseDto<EmailDomainSettingDto>> updateEmailDomainSetting(
            @RequestBody EmailDomainSettingRequestDto emailSettingRequestDto
    ) {
        var updatedSetting = settingService.updateEmailSetting(emailSettingRequestDto);
        return ResponseEntity.ok(ApplicationResponseDto.success(updatedSetting));
    }

    @PutMapping("/phone-number")
    public ResponseEntity<ApplicationResponseDto<PhoneSettingDto>> updatePhoneSetting(
            @RequestBody PhoneSettingRequestDto phoneSettingRequestDto
    ) {
        var updatedSetting = settingService.updatePhoneSetting(phoneSettingRequestDto);
        return ResponseEntity.ok(ApplicationResponseDto.success(updatedSetting));
    }
}