package com.tkpm.sms.controller;

import com.tkpm.sms.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.dto.request.setting.SettingRequestDto;
import com.tkpm.sms.dto.response.SettingDto;
import com.tkpm.sms.dto.response.common.ApplicationResponseDto;
import com.tkpm.sms.dto.response.common.ListResponse;
import com.tkpm.sms.dto.response.common.PageDto;
import com.tkpm.sms.mapper.SettingMapper;
import com.tkpm.sms.service.SettingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/settings")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SettingController {
    SettingService settingService;
    SettingMapper settingMapper;

    @GetMapping
    public ResponseEntity<ApplicationResponseDto<ListResponse<SettingDto>>> getAllSettings(
            @ModelAttribute BaseCollectionRequest search
    ) {
        Page<SettingDto> settings = settingService
                .getAllSettings(search).
                map(settingMapper::toSettingDto);

        var pageDto = PageDto.builder()
                .totalElements(settings.getTotalElements())
                .pageSize(settings.getSize())
                .pageNumber(settings.getNumber())
                .totalPages(settings.getTotalPages())
                .build();

        var listResponse = ListResponse.<SettingDto>builder().
                page(pageDto).
                data(settings.stream().toList()).
                build();

        return ResponseEntity.ok(ApplicationResponseDto.success(listResponse));
    }

    @GetMapping("/{name}")
    public ResponseEntity<ApplicationResponseDto<SettingDto>> getSettingById(@PathVariable String name){
        var setting = settingService.getSettingByName(name.toLowerCase());
        var response = ApplicationResponseDto.success(
                settingMapper.toSettingDto(setting));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{name}")
    public ResponseEntity<ApplicationResponseDto<SettingDto>> updateSetting(
            @PathVariable String name,
            @RequestBody SettingRequestDto settingRequestDto
    ){
        var updatedSetting = settingService.updateSetting(name.toLowerCase(), settingRequestDto);
        var response = ApplicationResponseDto.success(settingMapper.toSettingDto(updatedSetting));
        return ResponseEntity.ok(response);
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
    public ResponseEntity<ApplicationResponseDto<SettingDto>> updatePhoneSetting(
            @RequestBody SettingRequestDto settingRequestDto
    ){
        var updatedSetting = settingService.updatePhoneSetting(settingRequestDto);
        var response = ApplicationResponseDto.success(settingMapper.toSettingDto(updatedSetting));
        return ResponseEntity.ok(response);
    }
}
