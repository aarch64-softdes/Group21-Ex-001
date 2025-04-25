package com.tkpm.sms.application.mapper;

import com.tkpm.sms.application.dto.request.course.CourseScheduleDto;
import com.tkpm.sms.domain.valueobject.Schedule;

public interface ScheduleMapper {
    Schedule toSchedule(String scheduleString);

    Schedule toSchedule(CourseScheduleDto schedule);

    String formatSchedule(Schedule schedule);
}
