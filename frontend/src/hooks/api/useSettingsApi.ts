import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import SettingsService from '@/services/settingService';
import {
  EmailDomainSetting,
  PhoneSetting,
  PhoneSettingRequest,
} from '@/types/setting';

const settingsService = new SettingsService();

// Email Domain Settings Hooks
export const useEmailDomainSetting = () => {
  return useQuery({
    queryKey: ['emailDomainSetting'],
    queryFn: () => settingsService.getEmailDomainSetting(),
  });
};

export const useUpdateEmailDomainSetting = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data: EmailDomainSetting) =>
      settingsService.updateEmailDomainSetting(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['emailDomainSetting'] });
    },
  });
};

// Phone Settings Hooks
export const usePhoneSetting = () => {
  return useQuery({
    queryKey: ['phoneSetting'],
    queryFn: () => settingsService.getPhoneSetting(),
  });
};

export const useUpdatePhoneSetting = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data: PhoneSettingRequest) =>
      settingsService.updatePhoneSetting(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['phoneSetting'] });
    },
  });
};
