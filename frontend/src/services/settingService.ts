import api from '@/lib/api';
import { EmailDomainSetting, PhoneSetting } from '@/types/setting';

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
    return response.data.content;
  };

  updatePhoneSetting = async (data: PhoneSetting): Promise<PhoneSetting> => {
    const response = await api.put('/api/settings/phone-number', data);
    return response.data.content;
  };
}
