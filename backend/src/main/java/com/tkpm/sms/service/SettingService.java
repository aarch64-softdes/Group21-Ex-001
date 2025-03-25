package com.tkpm.sms.service;

import com.tkpm.sms.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.dto.request.setting.SettingRequestDto;
import com.tkpm.sms.entity.Setting;
import com.tkpm.sms.exceptions.ApplicationException;
import com.tkpm.sms.exceptions.ErrorCode;
import com.tkpm.sms.mapper.SettingMapper;
import com.tkpm.sms.repository.SettingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SettingService {
    SettingRepository settingRepository;
    SettingMapper settingMapper;

    public Page<Setting> getAllSettings(BaseCollectionRequest request) {
        Pageable pageable = PageRequest.of(
                request.getPage() - 1,
                request.getSize(),
                Sort.by(
                        request.getSortDirection().equalsIgnoreCase("desc")
                                ? Sort.Direction.DESC
                                : Sort.Direction.ASC,
                        request.getSortBy()));

        return settingRepository.findAll(pageable);
    }

    public Setting getSettingById(String id) {
        return settingRepository.findById(id).orElseThrow(() -> new ApplicationException(
                ErrorCode.NOT_FOUND.withMessage(
                        String.format("Setting with id %s not found", id))));
    }

    public Setting getSettingByName(String name) {
        return settingRepository.findSettingByName(name).orElseThrow(
                () -> new ApplicationException(
                        ErrorCode.NOT_FOUND.withMessage(
                                String.format("Setting with name %s not found", name))));
    }

    @Transactional
    public Setting updateSetting(String name, SettingRequestDto settingRequestDto) {
        var setting = settingRepository.findSettingByName(name).orElseThrow(
                () -> new ApplicationException(
                        ErrorCode.NOT_FOUND.withMessage(
                                String.format("Setting with name %s not found", name))));

        // TODO: create map method from List<String> to String
        settingMapper.updateSetting(setting, settingRequestDto);
        setting.setDetails(settingRequestDto.getDetails());
        return settingRepository.save(setting);
    }
}
