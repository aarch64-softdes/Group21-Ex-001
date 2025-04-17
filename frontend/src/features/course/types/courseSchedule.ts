export interface CourseScheduleDto {
  dateOfWeek: string;
  startTime: number;
  endTime: number;
}

// Helper function to format schedule from backend to frontend format (e.g. "T2(3-6)")
export const formatSchedule = (schedule: CourseScheduleDto): string => {
  if (!schedule) return '';
  return `${schedule.dateOfWeek}(${schedule.startTime}-${schedule.endTime})`;
};

// Helper function to parse frontend format to backend format
export const parseSchedule = (scheduleString: string): CourseScheduleDto => {
  if (!scheduleString) return { dateOfWeek: '', startTime: 0, endTime: 0 };

  try {
    const dateOfWeek = scheduleString.substring(0, scheduleString.indexOf('('));
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
