import api from '@/core/config/api';
import {
  AdjustmentDurationSetting,
  EmailDomainSetting,
  PhoneSetting,
  PhoneSettingRequest,
} from '@/features/settings/types/setting';

export default class SettingsService {
  // Email domain settings
  getEmailDomainSetting = async (): Promise<EmailDomainSetting> => {
    const response = await api.get('/api/settings/email');

    return response.data.content;
  };

  updateEmailDomainSetting = async (
    data: EmailDomainSetting,
  ): Promise<EmailDomainSetting> => {
    const response = await api.put('/api/settings/email', data);

    return response.data.content;
  };

  // Phone settings
  getPhoneSetting = async (): Promise<PhoneSetting> => {
    const response = await api.get('/api/settings/phone-number');
    return response.data.content.supportedCountryCodes;
  };

  updatePhoneSetting = async (
    data: PhoneSettingRequest,
  ): Promise<PhoneSetting> => {
    const response = await api.put('/api/settings/phone-number', data);
    return response.data.content.supportedCountryCodes;
  };

  // Adjustment duration settings
  getAdjustmentDurationSetting =
    async (): Promise<AdjustmentDurationSetting> => {
      const response = await api.get('/api/settings/adjustment-duration');
      return response.data.content;
    };

  updateAdjustmentDurationSetting = async (
    data: AdjustmentDurationSetting,
  ): Promise<AdjustmentDurationSetting> => {
    const response = await api.put('/api/settings/adjustment-duration', data);
    return response.data.content;
  };
}
