package com.tkpm.sms.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tkpm.sms.dto.request.setting.PhoneSettingRequestDto;
import com.tkpm.sms.dto.response.setting.PhoneSettingDto;
import com.tkpm.sms.dto.response.common.ApplicationResponseDto;
import com.tkpm.sms.exceptions.ApplicationException;
import com.tkpm.sms.exceptions.ErrorCode;
import com.tkpm.sms.service.SettingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<ApplicationResponseDto<PhoneSettingDto>> getPhoneSetting(){
        var setting = settingService.getPhoneSetting();

        try{
            List<String> details = objectMapper.readValue(setting.getDetails(), new TypeReference<List<String>>(){});

            var phoneSettingDto = new PhoneSettingDto(details);
            phoneSettingDto.setSettingName(PHONE_NUMBER_SETTING);
            var response = ApplicationResponseDto.success(phoneSettingDto);

            return ResponseEntity.ok(response);
        }catch(Exception e){
            throw new ApplicationException(
                    ErrorCode.INVALID_PHONE_SETTING_DETAILS.
                            withMessage("Invalid phone setting details"));
        }

    }

    @PutMapping("/email")
    public ResponseEntity<ApplicationResponseDto<SettingDto>> updateEmaiSetting(
            @RequestBody SettingRequestDto settingRequestDto
    ){
        var updatedSetting = settingService.updateEmailSetting(settingRequestDto);
        var response = ApplicationResponseDto.success(settingMapper.toSettingDto(updatedSetting));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/phone-number")
    public ResponseEntity<ApplicationResponseDto<PhoneSettingDto>> updatePhoneSetting(
            @RequestBody PhoneSettingRequestDto phoneSettingRequestDto
    ){
        var updatedSetting = settingService.updatePhoneSetting(phoneSettingRequestDto);

        try{
            List<String> details = objectMapper.readValue(updatedSetting.getDetails(), new TypeReference<List<String>>(){});

            var phoneSettingDto = new PhoneSettingDto(details);
            phoneSettingDto.setSettingName(PHONE_NUMBER_SETTING);
            var response = ApplicationResponseDto.success(phoneSettingDto);

            return ResponseEntity.ok(response);
        }catch(Exception e){
            throw new ApplicationException(
                    ErrorCode.INVALID_PHONE_SETTING_DETAILS.
                            withMessage("Invalid phone setting details"));
        }
    }
}
