import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import SettingsService from '@/features/settings/api/settingService';
import {
  AdjustmentDurationSetting,
  EmailDomainSetting,
  PhoneSetting,
  PhoneSettingRequest,
} from '@/features/settings/types/setting';
import { showErrorToast, showSuccessToast } from '@/shared/lib/toast-utils';
import { getErrorMessage } from '@/shared/lib/utils';

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
      showSuccessToast('Email domain setting updated successfully');
    },
    onError: (error) => {
      showErrorToast(getErrorMessage(error));
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
    onError: (error) => {
      showErrorToast(getErrorMessage(error));
    },
  });
};

// Adjustment Duration Settings Hooks
export const useAdjustmentDurationSetting = () => {
  return useQuery({
    queryKey: ['adjustmentDurationSetting'],
    queryFn: () => settingsService.getAdjustmentDurationSetting(),
  });
};

export const useUpdateAdjustmentDurationSetting = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data: AdjustmentDurationSetting) =>
      settingsService.updateAdjustmentDurationSetting(data),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ['adjustmentDurationSetting'],
      });
    },
    onError: (error) => {
      showErrorToast(getErrorMessage(error));
    },
  });
};
