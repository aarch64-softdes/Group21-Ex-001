package com.tkpm.sms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tkpm.sms.application.dto.request.setting.EmailDomainSettingRequestDto;
import com.tkpm.sms.application.dto.request.setting.PhoneSettingRequestDto;
import com.tkpm.sms.application.dto.response.setting.EmailDomainSettingDto;
import com.tkpm.sms.application.dto.response.setting.PhoneSettingDto;
import com.tkpm.sms.application.service.implementation.SettingServiceImpl;
import com.tkpm.sms.application.service.interfaces.SettingService;
import com.tkpm.sms.domain.enums.SettingType;
import com.tkpm.sms.domain.exception.GenericDomainException;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.model.Setting;
import com.tkpm.sms.domain.repository.SettingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SettingServiceImplTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private SettingRepository settingRepository;

    @InjectMocks
    private SettingService settingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // @Test
    // void testGetPhoneSetting() {
    // Setting phoneSetting = new Setting();
    // phoneSetting.setDetails("123456789");
    // when(settingRepository.getPhoneSetting()).thenReturn(phoneSetting);

    // PhoneSettingDto phoneSettingDto = settingService.getPhoneSetting();

    // assertNotNull(phoneSettingDto);
    // assertEquals("123456789", phoneSettingDto.());
    // verify(settingRepository, times(1)).getPhoneSetting();
    // }

    @Test
    void testGetEmailSetting() {
        var emailSetting = new String();
        when(settingRepository.getEmailSetting()).thenReturn("@example.com");

        EmailDomainSettingDto emailSettingDto = settingService.getEmailSetting();

        assertNotNull(emailSettingDto);
        assertEquals("@example.com", emailSettingDto.getDomain());
        verify(settingRepository, times(1)).getEmailSetting();
    }

    @Test
    void testUpdateEmailSetting_Success() {
        EmailDomainSettingRequestDto requestDto = new EmailDomainSettingRequestDto();
        requestDto.setDomain("example.com");

        Setting emailSetting = new Setting();
        emailSetting.setName(SettingType.EMAIL.getValue());
        emailSetting.setDetails("@example.com");

        when(settingRepository.findByName(SettingType.EMAIL.getValue())).thenReturn(Optional.of(emailSetting));
        when(settingRepository.save(any(Setting.class))).thenReturn(emailSetting);

        EmailDomainSettingDto updatedSetting = settingService.updateEmailSetting(requestDto);

        assertNotNull(updatedSetting);
        assertEquals("@example.com", updatedSetting.getDomain());
        verify(settingRepository, times(1)).findByName(SettingType.EMAIL.getValue());
        verify(settingRepository, times(1)).save(any(Setting.class));
    }

    @Test
    void testUpdateEmailSetting_NotFound() {
        EmailDomainSettingRequestDto requestDto = new EmailDomainSettingRequestDto();
        requestDto.setDomain("example.com");

        when(settingRepository.findByName(SettingType.EMAIL.getValue())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> settingService.updateEmailSetting(requestDto));
    }

    @Test
    void testUpdatePhoneSetting_Success() throws JsonProcessingException {
        PhoneSettingRequestDto requestDto = new PhoneSettingRequestDto();
        requestDto.setSupportedCountryCodes(List.of("+1", "+84"));

        Setting phoneSetting = new Setting();
        phoneSetting.setName(SettingType.PHONE_NUMBER.getValue());

        when(settingRepository.findByName(SettingType.PHONE_NUMBER.getValue())).thenReturn(Optional.of(phoneSetting));
        when(objectMapper.writeValueAsString(requestDto.getSupportedCountryCodes())).thenReturn("[\"+1\",\"+84\"]");
        when(settingRepository.save(any(Setting.class))).thenReturn(phoneSetting);
        when(objectMapper.readValue("[\"+1\",\"+84\"]", List.class)).thenReturn(List.of("+1", "+84"));

        PhoneSettingDto updatedSetting = settingService.updatePhoneSetting(requestDto);

        assertNotNull(updatedSetting);
        assertEquals(List.of("+1", "+84"), updatedSetting.getSupportedCountryCodes());
        verify(settingRepository, times(1)).findByName(SettingType.PHONE_NUMBER.getValue());
        verify(settingRepository, times(1)).save(any(Setting.class));
    }

    @Test
    void testUpdatePhoneSetting_JsonProcessingException() throws JsonProcessingException {
        PhoneSettingRequestDto requestDto = new PhoneSettingRequestDto();
        requestDto.setSupportedCountryCodes(List.of("+1", "+84"));

        Setting phoneSetting = new Setting();
        phoneSetting.setName(SettingType.PHONE_NUMBER.getValue());

        when(settingRepository.findByName(SettingType.PHONE_NUMBER.getValue())).thenReturn(Optional.of(phoneSetting));
        when(objectMapper.writeValueAsString(requestDto.getSupportedCountryCodes()))
                .thenThrow(JsonProcessingException.class);

        assertThrows(GenericDomainException.class, () -> settingService.updatePhoneSetting(requestDto));
    }

    @Test
    void testUpdatePhoneSetting_NotFound() {
        PhoneSettingRequestDto requestDto = new PhoneSettingRequestDto();
        requestDto.setSupportedCountryCodes(List.of("+1", "+84"));

        when(settingRepository.findByName(SettingType.PHONE_NUMBER.getValue())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> settingService.updatePhoneSetting(requestDto));
    }
}