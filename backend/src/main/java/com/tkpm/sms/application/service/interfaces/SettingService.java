package com.tkpm.sms.application.service.interfaces;

import com.tkpm.sms.application.dto.request.setting.AdjustmentDurationSettingRequestDto;
import com.tkpm.sms.application.dto.request.setting.EmailDomainSettingRequestDto;
import com.tkpm.sms.application.dto.request.setting.FailingGradeSettingRequestDto;
import com.tkpm.sms.application.dto.request.setting.PhoneSettingRequestDto;
import com.tkpm.sms.application.dto.response.setting.AdjustmentDurationSettingDto;
import com.tkpm.sms.application.dto.response.setting.EmailDomainSettingDto;
import com.tkpm.sms.application.dto.response.setting.FailingGradeSettingDto;
import com.tkpm.sms.application.dto.response.setting.PhoneSettingDto;

public interface SettingService {
    PhoneSettingDto getPhoneSetting();

    EmailDomainSettingDto getEmailSetting();

    EmailDomainSettingDto updateEmailSetting(EmailDomainSettingRequestDto emailSettingRequestDto);

    PhoneSettingDto updatePhoneSetting(PhoneSettingRequestDto phoneSettingRequestDto);

    AdjustmentDurationSettingDto getAdjustmentDurationSetting();

    AdjustmentDurationSettingDto updateAdjustmentDurationSetting(AdjustmentDurationSettingRequestDto adjustmentDurationSettingRequestDto);

    FailingGradeSettingDto getFailingGradeSetting();

    FailingGradeSettingDto updateFailingGradeSetting(FailingGradeSettingRequestDto failingGradeSettingRequestDto);
}