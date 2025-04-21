package com.tkpm.sms.application.dto.request.course;

import com.tkpm.sms.application.annotation.RequiredConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseScheduleDto {
    @RequiredConstraint(field = "Date of week")
    String dateOfWeek;
    @RequiredConstraint(field = "Start time")
    int startTime;
    @RequiredConstraint(field = "End time")
    int endTime;

    @Override
    public String toString() {
        return dateOfWeek + "(" + startTime + "-" + endTime + ")";
    }

    public static CourseScheduleDto getScheduleFromString(String schedule) {
        var dateOfWeek = schedule.substring(0, schedule.indexOf("("));
        int startTime = Integer
                .parseInt(schedule.substring(schedule.indexOf("(") + 1, schedule.indexOf("-")));
        int endTime = Integer
                .parseInt(schedule.substring(schedule.indexOf("-") + 1, schedule.indexOf(")")));

        log.info("Parsed schedule: dateOfWeek={}, startTime={}, endTime={}", dateOfWeek, startTime,
                endTime);

        return CourseScheduleDto.builder().dateOfWeek(dateOfWeek).startTime(startTime)
                .endTime(endTime).build();
    }
}
