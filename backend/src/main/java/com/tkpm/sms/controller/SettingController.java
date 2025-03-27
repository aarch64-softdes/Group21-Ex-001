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


    @GetMapping("/phone-number")
    public ResponseEntity<ApplicationResponseDto<PhoneSettingDto>> getPhoneSetting(){
        var setting = settingService.getPhoneSetting();

        ObjectMapper mapper = new ObjectMapper();
        try{
            List<String> details = mapper.readValue(setting.getDetails(), new TypeReference<List<String>>(){});
            var response = ApplicationResponseDto.success(new PhoneSettingDto(details));
            return ResponseEntity.ok(response);
        }catch(Exception e){
            throw new ApplicationException(
                    ErrorCode.INVALID_PHONE_SETTING_DETAILS.
                            withMessage("Invalid phone setting details"));
        }

    }

    @PutMapping("/phone-number")
    public ResponseEntity<ApplicationResponseDto<PhoneSettingDto>> updatePhoneSetting(
            @RequestBody PhoneSettingRequestDto phoneSettingRequestDto
    ){
        var updatedSetting = settingService.updatePhoneSetting(phoneSettingRequestDto);

        ObjectMapper mapper = new ObjectMapper();
        try{
            List<String> details = mapper.readValue(updatedSetting.getDetails(), new TypeReference<List<String>>(){});
            var response = ApplicationResponseDto.success(new PhoneSettingDto(details));
            return ResponseEntity.ok(response);
        }catch(Exception e){
            throw new ApplicationException(
                    ErrorCode.INVALID_PHONE_SETTING_DETAILS.
                            withMessage("Invalid phone setting details"));
        }
    }
}
