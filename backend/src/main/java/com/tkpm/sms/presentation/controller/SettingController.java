package com.tkpm.sms.presentation.controller;

import com.tkpm.sms.application.dto.request.setting.AdjustmentDurationSettingRequestDto;
import com.tkpm.sms.application.dto.request.setting.EmailDomainSettingRequestDto;
import com.tkpm.sms.application.dto.request.setting.FailingGradeSettingRequestDto;
import com.tkpm.sms.application.dto.request.setting.PhoneSettingRequestDto;
import com.tkpm.sms.application.dto.response.common.ApplicationResponseDto;
import com.tkpm.sms.application.dto.response.setting.AdjustmentDurationSettingDto;
import com.tkpm.sms.application.dto.response.setting.EmailDomainSettingDto;
import com.tkpm.sms.application.dto.response.setting.FailingGradeSettingDto;
import com.tkpm.sms.application.dto.response.setting.PhoneSettingDto;
import com.tkpm.sms.application.service.interfaces.SettingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/adjustment-duration")
    public ResponseEntity<ApplicationResponseDto<AdjustmentDurationSettingDto>> getAdjustmentDurationSetting() {
        var setting = settingService.getAdjustmentDurationSetting();
        return ResponseEntity.ok(ApplicationResponseDto.success(setting));
    }

    @PutMapping("/adjustment-duration")
    public ResponseEntity<ApplicationResponseDto<AdjustmentDurationSettingDto>> updateAdjustmentDurationSetting(
            @RequestBody AdjustmentDurationSettingRequestDto adjustmentDurationSettingRequestDto
    ) {
        var updatedSetting = settingService.updateAdjustmentDurationSetting(adjustmentDurationSettingRequestDto);
        return ResponseEntity.ok(ApplicationResponseDto.success(updatedSetting));
    }

    @GetMapping("/failing-grade")
    public ResponseEntity<ApplicationResponseDto<FailingGradeSettingDto>> getFailingGradeSetting() {
        var setting = settingService.getFailingGradeSetting();
        return ResponseEntity.ok(ApplicationResponseDto.success(setting));
    }

    @PutMapping("/failing-grade")
    public ResponseEntity<ApplicationResponseDto<FailingGradeSettingDto>> updateFailingGradeSetting(
            @RequestBody FailingGradeSettingRequestDto failingGradeSettingRequestDto
    ) {
        var updatedSetting = settingService.updateFailingGradeSetting(failingGradeSettingRequestDto);
        return ResponseEntity.ok(ApplicationResponseDto.success(updatedSetting));
    }
}