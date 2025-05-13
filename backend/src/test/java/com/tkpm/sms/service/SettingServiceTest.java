package com.tkpm.sms.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tkpm.sms.application.dto.request.setting.AdjustmentDurationSettingRequestDto;
import com.tkpm.sms.application.dto.request.setting.EmailDomainSettingRequestDto;
import com.tkpm.sms.application.dto.request.setting.FailingGradeSettingRequestDto;
import com.tkpm.sms.application.dto.request.setting.PhoneSettingRequestDto;
import com.tkpm.sms.application.dto.response.setting.AdjustmentDurationSettingDto;
import com.tkpm.sms.application.dto.response.setting.EmailDomainSettingDto;
import com.tkpm.sms.application.dto.response.setting.FailingGradeSettingDto;
import com.tkpm.sms.application.dto.response.setting.PhoneSettingDto;
import com.tkpm.sms.application.service.implementation.SettingServiceImpl;
import com.tkpm.sms.domain.enums.SettingType;
import com.tkpm.sms.domain.exception.GenericDomainException;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.model.Setting;
import com.tkpm.sms.domain.repository.SettingRepository;
import com.tkpm.sms.domain.service.DomainEntityNameTranslator;
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
    
    @Mock
    private DomainEntityNameTranslator domainEntityNameTranslator;

    @InjectMocks
    private SettingServiceImpl settingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Setup default translator behavior
        when(domainEntityNameTranslator.getEntityTranslatedName(Setting.class)).thenReturn("Setting");
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
            setting.setDetails("[\"+1\",\"+84\"]");

            when(settingRepository.findByName(SettingType.PHONE_NUMBER.getValue()))
                    .thenReturn(Optional.of(setting));
            when(objectMapper.writeValueAsString(requestDto.getSupportedCountryCodes()))
                    .thenReturn("[\"+1\",\"+84\"]");
            when(settingRepository.save(any(Setting.class))).thenReturn(setting);
            when(objectMapper.readValue(setting.getDetails(), List.class))
                    .thenReturn(List.of("+1", "+84"));

            PhoneSettingDto result = settingService.updatePhoneSetting(requestDto);

            assertNotNull(result);
            assertEquals(List.of("+1", "+84"), result.getSupportedCountryCodes());
            verify(settingRepository).findByName(SettingType.PHONE_NUMBER.getValue());
            verify(objectMapper).writeValueAsString(requestDto.getSupportedCountryCodes());
            verify(settingRepository).save(any(Setting.class));
            verify(objectMapper).readValue(setting.getDetails(), List.class);
        }

        @Test
        @DisplayName("Should throw exception when phone setting not found")
        void testUpdatePhoneSetting_NotFound() {
            PhoneSettingRequestDto requestDto = new PhoneSettingRequestDto();
            when(settingRepository.findByName(SettingType.PHONE_NUMBER.getValue()))
                    .thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> settingService.updatePhoneSetting(requestDto));
            
            verify(settingRepository).findByName(SettingType.PHONE_NUMBER.getValue());
            verify(domainEntityNameTranslator).getEntityTranslatedName(Setting.class);
            verifyNoInteractions(objectMapper);
            verifyNoMoreInteractions(settingRepository);
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
            
            verify(settingRepository).findByName(SettingType.PHONE_NUMBER.getValue());
            verify(objectMapper).writeValueAsString(any());
            verifyNoMoreInteractions(settingRepository);
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
        @DisplayName("Should update email setting successfully with @ symbol")
        void testUpdateEmailSetting_SuccessWithAtSymbol() {
            EmailDomainSettingRequestDto requestDto = new EmailDomainSettingRequestDto();
            requestDto.setDomain("@example.com");

            Setting setting = new Setting();
            setting.setName(SettingType.EMAIL.getValue());
            
            Setting savedSetting = new Setting();
            savedSetting.setName(SettingType.EMAIL.getValue());
            savedSetting.setDetails("@example.com");

            when(settingRepository.findByName(SettingType.EMAIL.getValue()))
                    .thenReturn(Optional.of(setting));
            when(settingRepository.save(any(Setting.class))).thenReturn(savedSetting);

            EmailDomainSettingDto result = settingService.updateEmailSetting(requestDto);

            assertNotNull(result);
            assertEquals("@example.com", result.getDomain());
            verify(settingRepository).findByName(SettingType.EMAIL.getValue());
            verify(settingRepository).save(any(Setting.class));
        }
        
        @Test
        @DisplayName("Should update email setting successfully without @ symbol")
        void testUpdateEmailSetting_SuccessWithoutAtSymbol() {
            EmailDomainSettingRequestDto requestDto = new EmailDomainSettingRequestDto();
            requestDto.setDomain("example.com");

            Setting setting = new Setting();
            setting.setName(SettingType.EMAIL.getValue());
            
            Setting savedSetting = new Setting();
            savedSetting.setName(SettingType.EMAIL.getValue());
            savedSetting.setDetails("@example.com");

            when(settingRepository.findByName(SettingType.EMAIL.getValue()))
                    .thenReturn(Optional.of(setting));
            when(settingRepository.save(any(Setting.class))).thenReturn(savedSetting);

            EmailDomainSettingDto result = settingService.updateEmailSetting(requestDto);

            assertNotNull(result);
            assertEquals("@example.com", result.getDomain());
            verify(settingRepository).findByName(SettingType.EMAIL.getValue());
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
            
            verify(settingRepository).findByName(SettingType.EMAIL.getValue());
            verify(domainEntityNameTranslator).getEntityTranslatedName(Setting.class);
            verifyNoMoreInteractions(settingRepository);
        }
    }
    
    @Nested
    @DisplayName("Adjustment Duration Setting Tests")
    class AdjustmentDurationSettingTests {
        @Test
        @DisplayName("Should get adjustment duration setting successfully")
        void testGetAdjustmentDurationSetting() {
            String expectedDuration = "7";
            when(settingRepository.getAdjustmentDurationSetting()).thenReturn(expectedDuration);

            AdjustmentDurationSettingDto result = settingService.getAdjustmentDurationSetting();

            assertNotNull(result);
            assertEquals(expectedDuration, result.getAdjustmentDuration());
            verify(settingRepository).getAdjustmentDurationSetting();
        }

        @Test
        @DisplayName("Should update adjustment duration setting successfully")
        void testUpdateAdjustmentDurationSetting_Success() {
            AdjustmentDurationSettingRequestDto requestDto = new AdjustmentDurationSettingRequestDto();
            requestDto.setAdjustmentDuration("14");

            Setting setting = new Setting();
            setting.setName(SettingType.ADJUSTMENT_DURATION.getValue());
            
            Setting savedSetting = new Setting();
            savedSetting.setName(SettingType.ADJUSTMENT_DURATION.getValue());
            savedSetting.setDetails("14");

            when(settingRepository.findByName(SettingType.ADJUSTMENT_DURATION.getValue()))
                    .thenReturn(Optional.of(setting));
            when(settingRepository.save(any(Setting.class))).thenReturn(savedSetting);

            AdjustmentDurationSettingDto result = settingService.updateAdjustmentDurationSetting(requestDto);

            assertNotNull(result);
            assertEquals("14", result.getAdjustmentDuration());
            verify(settingRepository).findByName(SettingType.ADJUSTMENT_DURATION.getValue());
            verify(settingRepository).save(any(Setting.class));
        }

        @Test
        @DisplayName("Should throw exception when adjustment duration setting not found")
        void testUpdateAdjustmentDurationSetting_NotFound() {
            AdjustmentDurationSettingRequestDto requestDto = new AdjustmentDurationSettingRequestDto();
            requestDto.setAdjustmentDuration("14");
            
            when(settingRepository.findByName(SettingType.ADJUSTMENT_DURATION.getValue()))
                    .thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> settingService.updateAdjustmentDurationSetting(requestDto));
            
            verify(settingRepository).findByName(SettingType.ADJUSTMENT_DURATION.getValue());
            verify(domainEntityNameTranslator).getEntityTranslatedName(Setting.class);
            verifyNoMoreInteractions(settingRepository);
        }
    }
    
    @Nested
    @DisplayName("Failing Grade Setting Tests")
    class FailingGradeSettingTests {
        @Test
        @DisplayName("Should get failing grade setting successfully")
        void testGetFailingGradeSetting() {
            Double expectedGrade = 5.0;
            when(settingRepository.getFailingGradeSetting()).thenReturn(expectedGrade);

            FailingGradeSettingDto result = settingService.getFailingGradeSetting();

            assertNotNull(result);
            assertEquals(expectedGrade, result.getFailingGrade());
            verify(settingRepository).getFailingGradeSetting();
        }

        @Test
        @DisplayName("Should update failing grade setting successfully")
        void testUpdateFailingGradeSetting_Success() {
            FailingGradeSettingRequestDto requestDto = new FailingGradeSettingRequestDto();
            requestDto.setFailingGrade(4.0);

            Setting setting = new Setting();
            setting.setName(SettingType.FAILING_GRADE.getValue());
            
            Setting savedSetting = new Setting();
            savedSetting.setName(SettingType.FAILING_GRADE.getValue());
            savedSetting.setDetails("4.0");

            when(settingRepository.findByName(SettingType.FAILING_GRADE.getValue()))
                    .thenReturn(Optional.of(setting));
            when(settingRepository.save(any(Setting.class))).thenReturn(savedSetting);

            FailingGradeSettingDto result = settingService.updateFailingGradeSetting(requestDto);

            assertNotNull(result);
            assertEquals(4.0, result.getFailingGrade());
            verify(settingRepository).findByName(SettingType.FAILING_GRADE.getValue());
            verify(settingRepository).save(any(Setting.class));
        }

        @Test
        @DisplayName("Should throw exception when failing grade setting not found")
        void testUpdateFailingGradeSetting_NotFound() {
            FailingGradeSettingRequestDto requestDto = new FailingGradeSettingRequestDto();
            requestDto.setFailingGrade(4.0);
            
            when(settingRepository.findByName(SettingType.FAILING_GRADE.getValue()))
                    .thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> settingService.updateFailingGradeSetting(requestDto));
            
            verify(settingRepository).findByName(SettingType.FAILING_GRADE.getValue());
            verify(domainEntityNameTranslator).getEntityTranslatedName(Setting.class);
            verifyNoMoreInteractions(settingRepository);
        }
    }
}