import axios from 'axios';
import { clsx, type ClassValue } from 'clsx';
import { twMerge } from 'tailwind-merge';

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

export function getErrorMessage(error: unknown): string {
  // Handle Axios errors
  if (axios.isAxiosError(error)) {
    // Return the server's error message if available
    if (error.response?.data?.message) {
      return error.response.data.message;
    }

    // Return status text if available
    if (error.response?.statusText) {
      return `${error.response.statusText} (${error.response.status})`;
    }

    // Return a generic message for network errors
    if (error.code === 'ECONNABORTED' || !error.response) {
      return 'Network error. Please check your connection.';
    }

    // Return a generic message for server errors
    return `Server error: ${error.message}`;
  }

  // Handle standard Error objects
  if (error instanceof Error) {
    return error.message;
  }

  // Handle string errors
  if (typeof error === 'string') {
    return error;
  }

  // Handle unknown error types
  return 'An unexpected error occurred.';
}
