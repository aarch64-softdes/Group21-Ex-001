package com.tkpm.sms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tkpm.sms.application.dto.request.setting.EmailDomainSettingRequestDto;
import com.tkpm.sms.application.dto.request.setting.PhoneSettingRequestDto;
import com.tkpm.sms.application.dto.response.setting.EmailDomainSettingDto;
import com.tkpm.sms.application.dto.response.setting.PhoneSettingDto;
import com.tkpm.sms.application.service.implementation.SettingServiceImpl;
import com.tkpm.sms.domain.enums.SettingType;
import com.tkpm.sms.domain.exception.GenericDomainException;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.model.Setting;
import com.tkpm.sms.domain.repository.SettingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("SettingService Tests")
class SettingServiceTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private SettingRepository settingRepository;

    @InjectMocks
    private SettingServiceImpl settingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("Phone Setting Tests")
    class PhoneSettingTests {
        @Test
        @DisplayName("Should get phone setting successfully")
        void testGetPhoneSetting() {
            List<String> expectedCodes = List.of("+1", "+84");
            when(settingRepository.getPhoneSetting()).thenReturn(expectedCodes);

            PhoneSettingDto result = settingService.getPhoneSetting();

            assertNotNull(result);
            assertEquals(expectedCodes, result.getSupportedCountryCodes());
            verify(settingRepository).getPhoneSetting();
        }

        @Test
        @DisplayName("Should update phone setting successfully")
        void testUpdatePhoneSetting_Success() throws JsonProcessingException {
            PhoneSettingRequestDto requestDto = new PhoneSettingRequestDto();
            requestDto.setSupportedCountryCodes(List.of("+1", "+84"));

            Setting setting = new Setting();
            setting.setName(SettingType.PHONE_NUMBER.getValue());

            when(settingRepository.findByName(SettingType.PHONE_NUMBER.getValue()))
                    .thenReturn(Optional.of(setting));
            when(objectMapper.writeValueAsString(requestDto.getSupportedCountryCodes()))
                    .thenReturn("[\"+1\",\"+84\"]");
            when(settingRepository.save(any(Setting.class))).thenReturn(setting);
            when(objectMapper.readValue("[\"+1\",\"+84\"]", List.class))
                    .thenReturn(List.of("+1", "+84"));

            PhoneSettingDto result = settingService.updatePhoneSetting(requestDto);

            assertNotNull(result);
            assertEquals(List.of("+1", "+84"), result.getSupportedCountryCodes());
            verify(settingRepository).save(any(Setting.class));
        }

        @Test
        @DisplayName("Should throw exception when phone setting not found")
        void testUpdatePhoneSetting_NotFound() {
            PhoneSettingRequestDto requestDto = new PhoneSettingRequestDto();
            when(settingRepository.findByName(SettingType.PHONE_NUMBER.getValue()))
                    .thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> settingService.updatePhoneSetting(requestDto));
        }

        @Test
        @DisplayName("Should throw exception when JSON processing fails")
        void testUpdatePhoneSetting_JsonProcessingException() throws JsonProcessingException {
            PhoneSettingRequestDto requestDto = new PhoneSettingRequestDto();
            requestDto.setSupportedCountryCodes(List.of("+1", "+84"));

            Setting setting = new Setting();
            setting.setName(SettingType.PHONE_NUMBER.getValue());

            when(settingRepository.findByName(SettingType.PHONE_NUMBER.getValue()))
                    .thenReturn(Optional.of(setting));
            when(objectMapper.writeValueAsString(any())).thenThrow(JsonProcessingException.class);

            assertThrows(GenericDomainException.class,
                    () -> settingService.updatePhoneSetting(requestDto));
        }
    }

    @Nested
    @DisplayName("Email Setting Tests")
    class EmailSettingTests {
        @Test
        @DisplayName("Should get email setting successfully")
        void testGetEmailSetting() {
            String expectedDomain = "@example.com";
            when(settingRepository.getEmailSetting()).thenReturn(expectedDomain);

            EmailDomainSettingDto result = settingService.getEmailSetting();

            assertNotNull(result);
            assertEquals(expectedDomain, result.getDomain());
            verify(settingRepository).getEmailSetting();
        }

        @Test
        @DisplayName("Should update email setting successfully")
        void testUpdateEmailSetting_Success() {
            EmailDomainSettingRequestDto requestDto = new EmailDomainSettingRequestDto();
            requestDto.setDomain("example.com");

            Setting setting = new Setting();
            setting.setName(SettingType.EMAIL.getValue());
            setting.setDetails("@example.com");

            when(settingRepository.findByName(SettingType.EMAIL.getValue()))
                    .thenReturn(Optional.of(setting));
            when(settingRepository.save(any(Setting.class))).thenReturn(setting);

            EmailDomainSettingDto result = settingService.updateEmailSetting(requestDto);

            assertNotNull(result);
            assertEquals("@example.com", result.getDomain());
            verify(settingRepository).save(any(Setting.class));
        }

        @Test
        @DisplayName("Should throw exception when email setting not found")
        void testUpdateEmailSetting_NotFound() {
            EmailDomainSettingRequestDto requestDto = new EmailDomainSettingRequestDto();
            when(settingRepository.findByName(SettingType.EMAIL.getValue()))
                    .thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> settingService.updateEmailSetting(requestDto));
        }
    }
}