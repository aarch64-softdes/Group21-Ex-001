import i18n from '@/shared/i18n/i18n';

export interface CourseScheduleDto {
  dateOfWeek: string;
  startTime: number;
  endTime: number;
}

// Helper function to format schedule from backend to frontend format (e.g. "T2(3-6)")
export const formatSchedule = (
  schedule: CourseScheduleDto | string,
): string => {
  if (!schedule) return '';
  if (typeof schedule === 'string') {
    schedule = parseSchedule(schedule);
  }

  if (i18n.language === 'en') {
    return `${fromVietnameseToEnglish(schedule.dateOfWeek)}(${
      schedule.startTime
    }-${schedule.endTime})`;
  }

  return `${schedule.dateOfWeek}(${schedule.startTime}-${schedule.endTime})`;
};

// Helper function to parse frontend format to backend format
export const parseSchedule = (scheduleString: string): CourseScheduleDto => {
  if (!scheduleString) return { dateOfWeek: '', startTime: 0, endTime: 0 };

  try {
    const dateOfWeek = fromEnglishToVietnamese(
      scheduleString.substring(0, scheduleString.indexOf('(')),
    );
    const startTime = parseInt(
      scheduleString.substring(
        scheduleString.indexOf('(') + 1,
        scheduleString.indexOf('-'),
      ),
    );
    const endTime = parseInt(
      scheduleString.substring(
        scheduleString.indexOf('-') + 1,
        scheduleString.indexOf(')'),
      ),
    );

    return {
      dateOfWeek,
      startTime,
      endTime,
    };
  } catch (error) {
    console.error('Error parsing schedule:', error);
    return { dateOfWeek: '', startTime: 0, endTime: 0 };
  }
};

const fromVietnameseToEnglish = (day: string): string => {
  switch (day) {
    case 'T2':
      return 'Mon';
    case 'T3':
      return 'Tue';
    case 'T4':
      return 'Wed';
    case 'T5':
      return 'Thu';
    case 'T6':
      return 'Fri';
    case 'T7':
      return 'Sat';
    case 'CN':
      return 'Sun';
    default:
      return day;
  }
};

const fromEnglishToVietnamese = (day: string): string => {
  switch (day) {
    case 'Mon':
      return 'T2';
    case 'Tue':
      return 'T3';
    case 'Wed':
      return 'T4';
    case 'Thu':
      return 'T5';
    case 'Fri':
      return 'T6';
    case 'Sat':
      return 'T7';
    case 'Sun':
      return 'CN';
    default:
      return day;
  }
};
