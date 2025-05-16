import { toast } from 'sonner';
import { getErrorMessage } from './utils';
import { t } from 'i18next';

/**
 * Shows an error toast with the provided error message.
 * Automatically extracts the message if an Error object is provided.
 */
export function showErrorToast(error: unknown, title?: string): void {
  const message = getErrorMessage(error);
  toast.error(title || t('common:toast.error.title'), {
    description: message || t('common:toast.error.description'),
    duration: 5000,
  });
}

/**
 * Shows a success toast with the provided message.
 */
export function showSuccessToast(message: string, title?: string): void {
  toast.success(title || t('common:toast.success.title'), {
    description: message || t('common:toast.success.description'),
    duration: 3000,
  });
}

/**
 * Shows an info toast with the provided message.
 */
export function showInfoToast(message: string, title?: string): void {
  toast.info(title || t('common:toast.information.title'), {
    description: message || t('common:toast.information.description'),
    duration: 3000,
  });
}
