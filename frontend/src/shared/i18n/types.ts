import 'react-i18next';
import common from './locales/en/common';
import student from './locales/en/student';

declare module 'react-i18next' {
  interface CustomTypeOptions {
    resources: {
      common: typeof common;
      student: typeof student;
    };
  }
}
