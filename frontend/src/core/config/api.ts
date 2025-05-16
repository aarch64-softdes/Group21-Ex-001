import axios, { AxiosError, InternalAxiosRequestConfig } from 'axios';
import {
  ApiError,
  ResourceNotFoundError,
  ValidationError,
  AuthenticationError,
  AuthorizationError,
  ServerError,
} from '@/shared/lib/errors';
import i18n from '@/shared/i18n/i18n';

// Create axios instance
const api = axios.create({
  baseURL: 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    // You can add auth headers or other common configs here
    return config;
  },
  (error) => {
    return Promise.reject(error);
  },
);

api.interceptors.request.use((config: InternalAxiosRequestConfig) => {
  // Add authorization token to headers if available
  const lang = i18n.language;
  if (lang) {
    config.headers['Content-Language'] = lang;
  }
  return config;
});

// Response interceptor
api.interceptors.response.use(
  (response) => {
    // Return successful responses as is
    return response;
  },
  (error: AxiosError) => {
    if (!error.response) {
      // Network error or server is down
      return Promise.reject(
        new ApiError('Network error. Please check your connection.'),
      );
    }

    const { status, data, config } = error.response;
    const responseData = data as any;

    switch (status) {
      case 400:
        return Promise.reject(new ValidationError(responseData.message));
      case 401:
        return Promise.reject(new AuthenticationError(responseData.message));
      case 403:
        return Promise.reject(new AuthorizationError(responseData.message));
      case 409:
        return Promise.reject(new ApiError(responseData.message));
      case 404:
        return Promise.reject(new ResourceNotFoundError(responseData.message));

      case 500:
      case 502:
      case 503:
        return Promise.reject(new ServerError(responseData.message));
      default:
        return Promise.reject(
          new ApiError(`Request failed with status ${status}`),
        );
    }
  },
);

export default api;
