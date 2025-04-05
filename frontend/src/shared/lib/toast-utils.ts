import { toast } from 'sonner';
import { getErrorMessage } from './utils';

/**
 * Shows an error toast with the provided error message.
 * Automatically extracts the message if an Error object is provided.
 */
export function showErrorToast(error: unknown, title?: string): void {
  const message = getErrorMessage(error);
  toast.error(title || 'Error', {
    description: message,
    duration: 5000,
  });
}

/**
 * Shows a success toast with the provided message.
 */
export function showSuccessToast(message: string, title?: string): void {
  toast.success(title || 'Success', {
    description: message,
    duration: 3000,
  });
}

/**
 * Shows an info toast with the provided message.
 */
export function showInfoToast(message: string, title?: string): void {
  toast.info(title || 'Information', {
    description: message,
    duration: 3000,
  });
}
