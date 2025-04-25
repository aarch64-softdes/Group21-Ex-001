package com.tkpm.sms.infrastructure.mapper;

import com.tkpm.sms.application.dto.request.course.CourseScheduleDto;
import com.tkpm.sms.application.mapper.ScheduleMapper;
import com.tkpm.sms.domain.valueobject.Schedule;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public class ScheduleMapperImpl implements ScheduleMapper {
    @Override
    public Schedule toSchedule(String scheduleString) {
        return Schedule.of(scheduleString);
    }

    @Override
    public Schedule toSchedule(CourseScheduleDto schedule) {
        return Schedule.of(schedule.toString());
    }

    @Override
    public String formatSchedule(Schedule schedule) {
        return schedule != null ? schedule.toString() : null;
    }
}
