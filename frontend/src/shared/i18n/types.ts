import 'react-i18next';
import common from './locales/en/common';
import course from './locales/en/course';
import enrollment from './locales/en/enrollment';
import faculty from './locales/en/faculty';
import program from './locales/en/program';
import setting from './locales/en/setting';
import status from './locales/en/status';
import student from './locales/en/student';
import subject from './locales/en/subject';

declare module 'react-i18next' {
  interface CustomTypeOptions {
    resources: {
      common: typeof common;
      student: typeof student;
      faculty: typeof faculty;
      program: typeof program;
      subject: typeof subject;
      course: typeof course;
      status: typeof status;
      setting: typeof setting;
      enrollment: typeof enrollment;
    };
  }
}
